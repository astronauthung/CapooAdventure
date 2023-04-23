package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.moonstruck.capooadventure.CapooAdventure.Companion.UNIT_SCALE
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.component.PhysicComponent.Companion.physicsCmpFromImage
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.box2d.box
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y


@AllOf([SpawnComponent::class])
class EntitySpawnSystem(
    private val phWorld: World,
    private val atlas: TextureAtlas,
    private val spawnCmps:ComponentMapper<SpawnComponent>,
) : EventListener, IteratingSystem() {
    private val cachedCfgs = mutableMapOf<String, SpawnCfg>()
    private val cachedSizes = mutableMapOf<AnimationActor, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(spawnCmps[entity]) {
            val cfg = spawnCfg(name)
            val relativeSize = size(cfg.model)

            world.entity {
                val imageCmp = add<ImageComponent> {
                    image = Image().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                physicsCmpFromImage(phWorld, imageCmp.image, BodyDef.BodyType.DynamicBody) {phCmp, width, height ->
                    box(width, height) {
                        isSensor = false
                    }
                }
                if (cfg.speedScaling > 0f) {
                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }
                if (name == "Player") {
                    add<PlayerComponent>()
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
                    val name = mapObj.name ?: gdxError("MapObject $mapObj does not have a type!")
                    world.entity{
                        add<SpawnComponent> {
                            this.name = name
                            this.location.set(mapObj.x * UNIT_SCALE, mapObj.y * UNIT_SCALE)
                        }
                    }
                }
                return true
            }
        }
        return false
    }
    private fun size (model: AnimationActor) = cachedSizes.getOrPut(model) {
        val regions = atlas.findRegions("${model.atlasKey}/${AnimationType.IDLE.atlasKey}")
        if(regions.isEmpty) {
            gdxError("There are no regions for the idle animation of model $model")
        }
        val firstname = regions.first()
        vec2(firstname.originalWidth * UNIT_SCALE, firstname.originalHeight * UNIT_SCALE)
    }

}
