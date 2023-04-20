package com.github.moonstruck.capooadventure.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
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

fun gdxInputProcessor(processor: InputProcessor) {
    val currProcessor = Gdx.input.inputProcessor
    if (currProcessor == null) {
        Gdx.input.inputProcessor = processor
    } else {
        if (currProcessor is InputMultiplexer) {
            if (processor !in currProcessor.processors) {
                currProcessor.addProcessor(processor)
            }
        } else {
            Gdx.input.inputProcessor = InputMultiplexer(currProcessor, processor)
        }
    }
}
