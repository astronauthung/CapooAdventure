package com.github.moonstruck.capooadventure.Ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.input.PlayerInputProcessor
import com.github.quillraven.fleks.World

class GameModel(
//    world: World,
    stage: Stage,
//    private val playerInputProcessor: PlayerInputProcessor,
) : EventListener{
    init{
        stage.addListener(this)
    }

    override fun handle(event: Event?): Boolean {
        return true
    }

//    fun onTouchChange(knobPercentX: Float, knobPercentY: Float) {
//        playerInputProcessor.touchpadMove(knobPercentX,knobPercentY)
//    }
}
