package com.github.moonstruck.capooadventure.actor

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable

class FlipImage : Image() {
    var flipX = false

    override fun draw(batch: Batch, parentAlpha: Float) {
        validate()
        batch.setColor(color.r,color.g,color.b,color.a*parentAlpha)

        val toDraw = drawable
        if(toDraw is TransformDrawable && (scaleX != 1f || scaleY != 1f || rotation != 0f)){
            toDraw.draw(
                batch,
                if(flipX) x+imageX+imageWidth*scaleX else x + imageX,
                y+imageY,
                originX - imageX,
                originY - imageY,
                imageWidth,
                imageHeight,
                if(flipX) -scaleX else scaleX,
                scaleY,
                rotation
            )
        }else{
            toDraw?.draw(
                batch,
                if(flipX) x+imageX+imageWidth*scaleX else x + imageX,
                y+imageY,
                if(flipX) -imageWidth * scaleX else imageWidth * scaleX,
                imageHeight * scaleY,
            )
        }
    }
}
