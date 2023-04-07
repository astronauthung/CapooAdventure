package com.github.moonstruck.capooadventure.component

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Entity
import java.awt.event.ComponentListener

class ImageComponent: Comparable<ImageComponent> {
    lateinit var image: Image
    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if(yDiff != 0 ){
            yDiff
        }else{
            other.image.x.compareTo(image.x)
        }
    }
    companion object{
        class ImageComponentListener(private val stage: Stage) : com.github.quillraven.fleks.ComponentListener<ImageComponent>
        {
            override fun onComponentAdded(entity: Entity, component: ImageComponent) {
                stage.addActor(component.image)
            }

            override fun onComponentRemoved(entity: Entity, component: ImageComponent) {
                stage.root.removeActor(component.image)
            }
        }
    }
}
