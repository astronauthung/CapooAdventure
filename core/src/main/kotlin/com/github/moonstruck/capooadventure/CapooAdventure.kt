package com.github.moonstruck.capooadventure

import com.github.moonstruck.capooadventure.screen.GameScreen
import com.github.moonstruck.capooadventure.screen.UiScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class CapooAdventure : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(GameScreen())
        addScreen(UiScreen())
        setScreen<UiScreen>()

    }
    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}
