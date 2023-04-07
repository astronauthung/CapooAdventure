package com.github.moonstruck.capooadventure.screen


import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.log.logger

class GameScreen : KtxScreen {
    private val spriteBatch : Batch = SpriteBatch()
    private val stage : Stage = Stage()
    private val texture : Texture = Texture(
        "move.png"
    )
    private val background : Texture = Texture(
        "fck.png"
    )

    override fun show() {
        log.debug { "GameScreen get shown" }
    }
    override fun render(delta: Float) {
        spriteBatch.use {
            it.draw(texture,0f,0f)
            it.draw(background, 340f,340f)
        }
    }
    companion object {
        private val log = logger<GameScreen>()
    }
}

