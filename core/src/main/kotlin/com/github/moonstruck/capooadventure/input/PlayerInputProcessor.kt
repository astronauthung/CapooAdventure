package com.github.moonstruck.capooadventure.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.MathUtils
import com.github.moonstruck.capooadventure.component.MoveComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import ktx.app.KtxInputAdapter
import com.github.quillraven.fleks.World


class PlayerInputProcessor(

    world: World,
    private val moveCmps: ComponentMapper<MoveComponent>,

): KtxInputAdapter {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    init {
        Gdx.input.inputProcessor = this
    }

    fun touchpadMove(x:Float,y:Float){
        val angle = MathUtils.atan2(y,x)
        val cos = MathUtils.cos(angle)
        val sin = MathUtils.sin(angle)

        updatePlayerMovement(sin,cos)
    }

    private fun updatePlayerMovement(playerSin: Float, playerCos: Float) {
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = playerCos
                sin = playerSin
            }
        }
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
