package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.ImageComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.collection.compareEntity


@AllOf([ImageComponent::class])

class RenderSystem(private val stage:Stage,private val imageCmps: ComponentMapper<ImageComponent>): IteratingSystem(
    comparator = compareEntity{e1,e2 -> imageCmps[e1].compareTo(imageCmps[e2]) }
) {

    override fun onTick() {
        super.onTick()

        with(stage) {
            viewport.apply()
            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        imageCmps[entity].image.toFront()
    }
}
