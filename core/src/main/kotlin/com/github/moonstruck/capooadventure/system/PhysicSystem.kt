package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.physics.box2d.*
import com.badlogic.gdx.physics.box2d.World
import com.github.moonstruck.capooadventure.component.*
import com.github.moonstruck.capooadventure.system.EntitySpawnSystem.Companion.AI_SENSOR
import com.github.quillraven.fleks.*
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([PhysicComponent::class, ImageComponent::class])
class  PhysicSystem (
    private val phWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    private val tiledCmps: ComponentMapper<TiledComponent>,
    private val collisionCmps: ComponentMapper<CollisionComponent>,
    private val aiCmps : ComponentMapper<AiComponent>
    //1/60 -> run 60 frames/s
) : ContactListener, IteratingSystem(interval = Fixed(1 / 60f)) {

    init {
        phWorld.setContactListener(this)
    }

    override fun onUpdate() {
        if (phWorld.autoClearForces) {
            log.error { "AutoClearForces must be set to false to guarantee a correct physic simulation" }
            phWorld.autoClearForces = false
        }
        super.onUpdate()
        phWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        phWorld.step(deltaTime,6,2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicCmp = physicCmps[entity]
        physicCmp.prevPos.set(physicCmp.body.position)

        if(!physicCmp.impulse.isZero) {
            physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
            physicCmp.impulse.setZero()
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val physicCmp = physicCmps[entity]
        val imageCmp = imageCmps[entity]

        val (prevX, prevY) = physicCmp.prevPos
        val (bodyX, bodyY) = physicCmp.body.position
        imageCmp.image.run {
            setPosition(
                MathUtils.lerp(prevX, bodyX, alpha) - width * 0.5f,
                MathUtils.lerp(prevY, bodyY, alpha) - height * 0.5f,
            )
        }
    }
    companion object {
        private val log = logger<PhysicSystem>()
    }

    override fun beginContact(contact: Contact) {
        val entityA: Entity = contact.fixtureA.entity
        val entityB: Entity = contact.fixtureB.entity

        val isEntityATiledCollisionSensor = entityA in tiledCmps && contact.fixtureA.isSensor
        val isEntityBTiledCollisionFixture = entityB in collisionCmps && !contact.fixtureB.isSensor

        val isEntityBTiledCollisionSensor = entityB in tiledCmps && contact.fixtureB.isSensor
        val isEntityATiledCollisionFixture = entityA in collisionCmps && !contact.fixtureA.isSensor

        val isEntityAAiSensor = entityA in aiCmps && contact.fixtureA.isSensor && contact.fixtureA.userData == AI_SENSOR
        val isEntityBAiSensor = entityB in aiCmps && contact.fixtureB.isSensor && contact.fixtureB.userData == AI_SENSOR

        when {
            isEntityATiledCollisionSensor && isEntityBTiledCollisionFixture -> {
                tiledCmps[entityA].nearbyEntities += entityB
            }
            isEntityBTiledCollisionSensor && isEntityATiledCollisionFixture -> {
                tiledCmps[entityB].nearbyEntities += entityA
            }
            isEntityAAiSensor && isEntityBTiledCollisionFixture -> {
                aiCmps[entityA].nearbyEntities += entityB
            }
            isEntityBAiSensor && isEntityATiledCollisionFixture -> {
                aiCmps[entityB].nearbyEntities += entityA
            }
        }
    }

    override fun endContact(contact: Contact) {
        val entityA: Entity = contact.fixtureA.entity
        val entityB: Entity = contact.fixtureB.entity

        val isEntityATiledCollisionSensor = entityA in tiledCmps && contact.fixtureA.isSensor
        val isEntityBTiledCollisionSensor = entityB in tiledCmps && contact.fixtureB.isSensor

        val isEntityAAiSensor = entityA in aiCmps && contact.fixtureA.isSensor && contact.fixtureA.userData == AI_SENSOR
        val isEntityBAiSensor = entityB in aiCmps && contact.fixtureB.isSensor && contact.fixtureB.userData == AI_SENSOR

        when {
            isEntityATiledCollisionSensor && !contact.fixtureB.isSensor -> {
                tiledCmps[entityA].nearbyEntities -= entityB
            }
            isEntityBTiledCollisionSensor && !contact.fixtureA.isSensor -> {
                tiledCmps[entityB].nearbyEntities -= entityA
            }
            isEntityAAiSensor && !contact.fixtureB.isSensor -> {
                aiCmps[entityA].nearbyEntities -= entityB
            }
            isEntityBAiSensor && !contact.fixtureA.isSensor -> {
                aiCmps[entityB].nearbyEntities -= entityA
            }
        }
    }

    private fun Fixture.isStaticBody()= this.body.type == BodyDef.BodyType.StaticBody
    private fun Fixture.isDynamicBody() = this.body.type == BodyDef.BodyType.DynamicBody

    override fun preSolve(contact: Contact, oldManifold: Manifold) {
        contact.isEnabled = (contact.fixtureA.isStaticBody() && contact.fixtureB.isDynamicBody()) ||
                            (contact.fixtureB.isStaticBody()&& contact.fixtureA.isDynamicBody())
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) = Unit
}

