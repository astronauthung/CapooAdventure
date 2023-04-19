package com.github.moonstruck.capooadventure.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad
import com.github.moonstruck.capooadventure.component.MoveComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import ktx.app.KtxInputAdapter


class PlayerInputProcessor(
    private val world:com.github.quillraven.fleks.World,
    private val moveCmps:ComponentMapper<MoveComponent>,
): KtxInputAdapter {

    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    init {
        Gdx.input.inputProcessor = this
    }
}
