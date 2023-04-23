package com.github.moonstruck.capooadventure.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.github.moonstruck.capooadventure.component.MoveComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import ktx.app.KtxInputAdapter
import com.github.quillraven.fleks.World


class PlayerInputProcessor(

    world: World,
    private val moveCmps: ComponentMapper<MoveComponent>,

): KtxInputAdapter {
    private var playerSin = 0f
    private var playerCos = 0f
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    init {
        Gdx.input.inputProcessor = this
    }

    private fun Int.isMovementkey(): Boolean {
        return this == UP || this == DOWN || this == RIGHT || this == LEFT
    }
    private fun updatePlayerMovement() {
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = playerCos
                sin = playerSin
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if(keycode.isMovementkey()) {
            when (keycode) {
                UP -> playerSin = 1f
                DOWN -> playerSin = -1f
                RIGHT -> playerCos = 1f
                LEFT -> playerCos = -1f
            }
            updatePlayerMovement()
            return true
        }
        return false
    }
    override fun keyUp(keycode: Int): Boolean {
        if(keycode.isMovementkey()) {
            when (keycode) {
                UP -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                DOWN -> playerSin = if (Gdx.input.isKeyPressed(DOWN)) 1f else 0f
                RIGHT -> playerCos = if (Gdx.input.isKeyPressed(DOWN)) -1f else 0f
                LEFT -> playerCos = if (Gdx.input.isKeyPressed(DOWN)) 1f else 0f
            }
            updatePlayerMovement()
            return true
        }
        return false
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
