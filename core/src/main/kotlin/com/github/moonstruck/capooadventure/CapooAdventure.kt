package com.github.moonstruck.capooadventure

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.moonstruck.capooadventure.screen.GameScreen
import com.github.moonstruck.capooadventure.screen.InventoryUiScreen
import com.github.moonstruck.capooadventure.screen.UiScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.app.clearScreen
import ktx.assets.disposeSafely

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class CapooAdventure : KtxGame<KtxScreen>() , EventListener{
    private val batch: Batch by lazy { SpriteBatch() }
    val gameStage by lazy { Stage(ExtendViewport(16f, 9f), batch) }
    val uiStage by lazy { Stage(ExtendViewport(320f, 180f), batch) }
    private val paused = false
    override fun create() {

        gameStage.addListener(this)

        addScreen(GameScreen(this))
        addScreen(UiScreen())
        addScreen(InventoryUiScreen())
        setScreen<GameScreen>()
//        setScreen<InventoryUiScreen>()
//        setScreen<UiScreen>()

    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        gameStage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    override fun render() {
        clearScreen(0f, 0f, 0f, 1f)
        // dt is set to zero during pause to
        // stop animations
        val dt = if (paused) 0f else Gdx.graphics.deltaTime
        currentScreen.render(dt)
    }

    override fun dispose() {
        super.dispose()
        gameStage.disposeSafely()
        uiStage.disposeSafely()
        batch.disposeSafely()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }

    override fun handle(event: Event?): Boolean {
        return true;
    }
}
