package com.github.moonstruck.capooadventure.screen


import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import ktx.app.KtxScreen
import ktx.log.logger

class GameScreen : KtxScreen {
    private val stage : Stage = Stage(ExtendViewport(16f, 9f))
    private val texture : Texture = Texture(
        "move.png"
    )
    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }


    override fun show() {
        log.debug { "GameScreen get shown" }
        stage.addActor(
            Image(texture).apply {
                setPosition(3f, 3f)
                setSize(3f, 3f)
                setScaling(Scaling.fill)
            }
        )
    }
    override fun render(delta: Float) {
        with(stage) {
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        stage.dispose()
        texture.dispose()
    }

    companion object {
        private val log = logger<GameScreen>()
    }
}

