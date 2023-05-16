package com.github.moonstruck.capooadventure.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.InputListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.Ui.View.GameView
import com.github.moonstruck.capooadventure.Ui.View.gameView
import com.github.moonstruck.capooadventure.Ui.disposeSkin
import com.github.moonstruck.capooadventure.Ui.loadSkin
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import ktx.app.KtxScreen
import ktx.scene2d.Scene2DSkin
import ktx.scene2d.actors

class UiScreen : KtxScreen {
    private val stage: Stage = Stage(ExtendViewport(320f,180f))


    init {
        loadSkin()
    }
    override fun dispose() {
        stage.dispose()
        disposeSkin()
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            hide()
            show()
        }
        stage.act()
        stage.draw()
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width,height)
    }

    override fun show() {
        stage.clear()
        stage.actors {
            gameView(GameModel(stage))
        }
        Gdx.input.inputProcessor = stage
        stage.isDebugAll = true
    }


}
