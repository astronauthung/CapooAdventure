package com.github.moonstruck.capooadventure.Ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.World

class GameModel(
    world: World,
    stage: Stage,
) : EventListener{
    init{
        stage.addListener(this)
    }

    override fun handle(event: Event?): Boolean {
        return true
    }
}
