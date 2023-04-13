package com.github.moonstruck.capooadventure.system

import com.github.moonstruck.capooadventure.component.ImageComponent
import com.github.moonstruck.capooadventure.component.PhysicComponent
import com.badlogic.gdx.physics.box2d.World
import com.github.quillraven.fleks.*
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2

@AllOf([PhysicComponent::class, ImageComponent::class])
class PhysicSystem (
    private val phWorld: World,
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>
    //1/60 -> run 60 frames/s
) : IteratingSystem(interval = Fixed(1 / 60f)) {

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
        val imageCmp = imageCmps[entity]

        val (bodyX, bodyY) = physicCmp.body.position
        imageCmp.image.run {
            setPosition(bodyX - width * 0.5f, bodyY - height * 0.5f)
        }
    }
    companion object {
        private val log = logger<PhysicSystem>()
    }
}

