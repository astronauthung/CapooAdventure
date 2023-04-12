package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.moonstruck.capooadventure.CapooAdventure.Companion.UNIT_SCALE
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y


@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val spawnCmps:ComponentMapper<SpawnComponent>,
) : EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val cfg = spawnCfg(type)

            world.entity {
                add<ImageComponent> {
                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(1f,1f)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }
            }

        }
        world.remove(entity)
    }
    private fun spawnCfg(name:String): SpawnCfg = cachedCfgs.getOrPut(name) {
        when (name){
            "Player" -> SpawnCfg(AnimationActor.PLAYER)
            "Slime" -> SpawnCfg(AnimationActor.SLIME)
            else -> gdxError("Type $name has no SpawnCfg setup")
        }
    }

    override fun handle(event: Event): Boolean {
        when(event){
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach{ mapObj ->
                    val type = mapObj.type ?: gdxError("MapObject $mapObj does not have a type!")
                    world.entity{
                        add<SpawnComponent> {
                            this.type = type
                            this.location.set(mapObj.x * UNIT_SCALE, mapObj.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }
        }
        return false
    }

}
