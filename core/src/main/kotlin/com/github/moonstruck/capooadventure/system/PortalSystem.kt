package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.CapooAdventure.Companion.UNIT_SCALE
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.component.PhysicComponent.Companion.bodyFromImageAndCfg
import com.github.moonstruck.capooadventure.component.PhysicComponent.Companion.physicCmpFromShape2D
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.moonstruck.capooadventure.system.EntitySpawnSystem.Companion.PLAYER_CFG
import com.github.quillraven.fleks.*
import ktx.app.gdxError
import ktx.assets.disposeSafely
import ktx.tiled.*

@AllOf([PortalComponent::class])
class PortalSystem(
    private val phWorld : World,
    @Qualifier("GameStage")private val gameStage: Stage,
    private val portalCmps : ComponentMapper<PortalComponent>,
    private val physicCmps : ComponentMapper<PhysicComponent>,
    private val imageCmps : ComponentMapper<ImageComponent>,
    private val lightCmps : ComponentMapper<LightComponent>,
) : IteratingSystem() , EventListener{

    private var currentMap: TiledMap? = null

    override fun onTickEntity(entity: Entity) {
        val (_, toMap, toPortal, triggeringEntities) = portalCmps[entity]
        if(triggeringEntities.isNotEmpty()){
            triggeringEntities.clear()
            setMap("$toMap.tmx",toPortal)
        }
    }

    fun setMap(path:String, targetPortalId:Int = -1){
        currentMap?.disposeSafely()
        //destroying all non-player entities
        world.family(noneOf = arrayOf(PlayerComponent::class)).forEach { world.remove(it) }

        val newMap = TmxMapLoader().load(path)
        gameStage.fire(MapChangeEvent(newMap))
        currentMap = newMap

        if(targetPortalId>=0){
            //teleport player to target portal location
            world.family(allOf = arrayOf(PlayerComponent::class)).forEach { playerEntity ->
                val targetPortal = targetPortalById(newMap, targetPortalId)
                val image = imageCmps[playerEntity].image
                image.setPosition(
                    targetPortal.x*UNIT_SCALE - image.width*0.5f + targetPortal.width*0.5f* UNIT_SCALE,
                    targetPortal.y* UNIT_SCALE - targetPortal.height*0.5f* UNIT_SCALE
                )

                configureEntity(playerEntity){
                    physicCmps.remove(it)
                    physicCmps.add(it){
                        body = bodyFromImageAndCfg(phWorld, image , PLAYER_CFG)
                    }
                    lightCmps[it].light.attachToBody(physicCmps[it].body)
                }
            }
        }

    }

    private fun targetPortalById(map: TiledMap, portalId: Int): MapObject {
        return map.layer("portals").objects.first{it.id == portalId}
            ?: gdxError("There is no portal with id $portalId")
    }

    override fun handle(event: Event): Boolean {
        if(event is MapChangeEvent){
            val portalLayer = event.map.layer("portals")
            portalLayer.objects.forEach {mapObj ->
                val toMap = mapObj.property("toMap","")
                val toPortal = mapObj.property("toPortal",-1)

                if(toMap.isBlank()){
                    return@forEach
                }else if(toPortal == -1){
                    gdxError("Portal ${mapObj.id} does not have a toPortal property")
                }

                world.entity{
                    add<PortalComponent>{
                        this.id = mapObj.id
                        this.toMap = toMap
                        this.toPortal = toPortal
                    }
                    physicCmpFromShape2D(phWorld,0,0,mapObj.shape,true)
                }
            }
            return true
        }
        return false
    }

    override fun onDispose() {
        currentMap?.disposeSafely()
    }
}
