package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import ktx.actors.onChangeEvent
import ktx.scene2d.*

class GameView(
    skin: Skin,
    model : GameModel
    ) : Table(skin), KTable {
    init {
        setFillParent(true)
        table{
            bottomTableCell ->
            touchpad(0f){ cell ->
                this.onChangeEvent {  }
                cell.expand()
                    .align(Align.left)
                    .bottom()
                    .pad(-10f,-170f,-70f,5f)
            }
        }
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(skin,model),init)


