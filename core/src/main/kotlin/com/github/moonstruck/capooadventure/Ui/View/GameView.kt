package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import ktx.actors.onChangeEvent
import ktx.scene2d.*

class GameView(skin: Skin) : Table(skin), KTable {

    init {
        table{
            bottomTableCell ->
            setBounds(15f,15f,200f,200f)
            touchpad(0f){ cell ->
                this.onChangeEvent {  }

                cell.expand()
                    .align(Align.left)
                    .bottom()
                    .pad(0f,5f,5f,5f)
            }
        }
        setFillParent(true)
    }
}


