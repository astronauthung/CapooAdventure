package com.github.moonstruck.capooadventure.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys.*
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.MathUtils
import com.github.moonstruck.capooadventure.component.AttackComponent
import com.github.moonstruck.capooadventure.component.MoveComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.quillraven.fleks.ComponentMapper
import ktx.app.KtxInputAdapter
import com.github.quillraven.fleks.World


class PlayerInputProcessor(

    world: World,
    private val moveCmps: ComponentMapper<MoveComponent> = world.mapper(),
    private val attackCmps: ComponentMapper<AttackComponent> = world.mapper(),

): KtxInputAdapter {
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))
    init {
        Gdx.input.inputProcessor = this
    }

    fun touchpadMove(x:Float,y:Float){
        updatePlayerMovement(y,x)
    }

    private fun updatePlayerMovement(playerSin: Float, playerCos: Float) {
        playerEntities.forEach { player ->
            with(moveCmps[player]) {
                cos = playerCos
                sin = playerSin
            }
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if(keycode == SPACE){
            playerEntities.forEach {
                with(attackCmps[it]){
                    doAttack = true
                    startAttack()
                }
            }
        }
        return true
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
