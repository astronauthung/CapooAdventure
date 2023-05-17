package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.github.moonstruck.capooadventure.Ui.Labels
import ktx.actors.plusAssign
import ktx.scene2d.*
import kotlin.math.PI

class PauseView(skin: Skin) :KTable,Table(skin){
    init{
        setFillParent(true)

        if(!skin.has(PIXMAP_KEY,TextureRegionDrawable::class.java)){
            skin.add(PIXMAP_KEY,TextureRegionDrawable(
                Texture(
                    Pixmap(1,1, Pixmap.Format.RGBA8888).apply {
                        this.drawPixel(0,0, Color.rgba8888(0.1f,0.1f,0.1f,0.7f))
                    }
                )
            ))
        }

        background = skin.get(PIXMAP_KEY,TextureRegionDrawable::class.java)

        this += label("[#FF0000]Pause[]", Labels.LARGE.skinKey){ lblCell->
            lblCell.expand().fill()
            this.setAlignment(Align.center)
        }
    }

    companion object{
        private const val PIXMAP_KEY = "pauseTexturePixmap"
    }
}

@Scene2dDsl
fun <S> KWidget<S>.pauseView(
    skin: Skin = Scene2DSkin.defaultSkin,
    init: PauseView.(S) -> Unit = {}
): PauseView = actor(PauseView(skin), init)
