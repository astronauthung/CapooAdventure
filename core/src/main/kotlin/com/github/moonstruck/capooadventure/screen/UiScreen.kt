package com.github.moonstruck.capooadventure.screen

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.Ui.View.GameView
import com.github.moonstruck.capooadventure.Ui.disposeSkin
import com.github.moonstruck.capooadventure.Ui.loadSkin
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors

class UiScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(16f,9f))
    init {
        loadSkin()
    }
    override fun dispose() {
        stage.dispose()
        disposeSkin()
    }

    override fun render(delta: Float) {
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height)
    }

    override fun show() {
        stage.clear()
        stage.actors {
            stage.addActor(GameView(Scene2DSkin.defaultSkin))
        }
    }


}
