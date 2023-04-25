package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.ImageComponent
import com.github.moonstruck.capooadventure.component.PhysicComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([PlayerComponent::class, ImageComponent::class])
class CameraSystem (
    private val imageCmps: ComponentMapper<ImageComponent>,
    private val physicCmps: ComponentMapper<PhysicComponent>,
    stage: Stage,
) : IteratingSystem(){
    private val camera = stage.camera

    override fun onTickEntity(entity: Entity) {
        with(physicCmps[entity]) {
            camera.position.set(
                body.position.x,
                body.position.y,
                camera.position.z
            )
        }
    }
}
