package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g3d.model.Animation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.moonstruck.capooadventure.CapooAdventure.Companion.UNIT_SCALE
import com.github.moonstruck.capooadventure.actor.FlipImage
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.component.PhysicComponent.Companion.physicsCmpFromImage
import com.github.moonstruck.capooadventure.event.MapChangeEvent
import com.github.quillraven.fleks.*
import ktx.app.gdxError
import ktx.box2d.box
import ktx.box2d.circle
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.x
import ktx.tiled.y
import kotlin.math.roundToInt


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
                    image = FlipImage().apply {
                        setPosition(location.x, location.y)
                        setSize(relativeSize.x, relativeSize.y)
                        setScaling(Scaling.fill)
                    }
                }
                add<AnimationComponent> {
                    nextAnimation(cfg.model, AnimationType.IDLE)
                }

                val physicCmp = physicsCmpFromImage(phWorld, imageCmp.image, cfg.bodyType) {phCmp, width, height ->
                    val w = width*cfg.physicScaling.x
                    val h = height*cfg.physicScaling.y
                    phCmp.offset.set(cfg.physicOffset)
                    phCmp.size.set(w,h)

                    box(w,h,cfg.physicOffset) {
                        isSensor = cfg.bodyType != BodyDef.BodyType.StaticBody
                        userData = HIT_BOX_SENSOR
                    }
                    if (cfg.bodyType != BodyDef.BodyType.StaticBody) {
                        val collH = h * 0.4f
                        val collOffset = vec2().apply { set(cfg.physicOffset) }
                        collOffset.y -= h * 0.5f - collH * 0.5f
                        box (w, collH, collOffset)
                    }
                }
                if (cfg.speedScaling > 0f) {
                    add<MoveComponent> {
                        speed = DEFAULT_SPEED * cfg.speedScaling
                    }
                }
                if(cfg.canAttack){
                    add<AttackComponent>{
                        maxDelay = cfg.attackDelay
                        damage = (DEFAULT_ATTACK_DAMAGE * cfg.attackScaling).roundToInt()
                        extraRange = cfg.attackExtraAttackRange
                    }
                }
                if(cfg.lifeScaling>0f){
                    add<LifeComponent>{
                        max = DEFAULT_LIFE * cfg.lifeScaling
                        life = max
                    }
                }

                if (name == "Player") {
                    add<PlayerComponent>()
                    add<StateComponent>()
                    add<InventoryComponent> {
                        itemsToAdd += ItemType.ARMOR
                        itemsToAdd += ItemType.HELMET
                        itemsToAdd += ItemType.BOOTS
                        itemsToAdd += ItemType.WEAPON
                    }
                }

                if(cfg.lootable){
                    add<LootComponent>()
                }
                if (cfg.bodyType != BodyDef.BodyType.StaticBody) {
                    add<CollisionComponent>()
                }

                if(cfg.aiTreePath.isNotBlank()){
                    add<AiComponent>{
                        treePath = cfg.aiTreePath
                    }
                    physicCmp.body.circle(4f){
                        isSensor = true
                        userData = AI_SENSOR
                    }
                }
            }
        }
        world.remove(entity)
    }
    private fun spawnCfg(name:String): SpawnCfg = cachedCfgs.getOrPut(name) {
        when (name){
            "Player" -> SpawnCfg(AnimationActor.PLAYER,
                attackExtraAttackRange= 0.6f,
                attackScaling = 1.25f,
                physicScaling =  vec2(0.3f,0.3f),
                physicOffset = vec2(0f,-10f* UNIT_SCALE))
            "Slime" -> SpawnCfg(AnimationActor.SLIME,
                lifeScaling = 0.75f,
                physicScaling =  vec2(0.3f,0.3f),
                physicOffset = vec2(0f,-2f* UNIT_SCALE),
                aiTreePath = "ai/slime.tree"
            )
            "Chest" -> SpawnCfg(AnimationActor.CHEST,
                bodyType = BodyDef.BodyType.StaticBody,
                canAttack = false,
                lifeScaling = 0f,
                lootable = true)
            "Chicken" -> SpawnCfg(AnimationActor.CHICKEN,
                canAttack = false,
                lifeScaling = 0.75f,
                physicScaling =  vec2(0.3f,0.3f),
                physicOffset = vec2(0f,-2f* UNIT_SCALE))

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

    companion object {
        const val HIT_BOX_SENSOR = "Hitbox"
        const val AI_SENSOR = "AiSensor"
    }
}
