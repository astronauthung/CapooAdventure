package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.Game
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.github.moonstruck.capooadventure.Ui.Drawables
import com.github.moonstruck.capooadventure.Ui.Widget.TouchpadInfo
import com.github.moonstruck.capooadventure.Ui.Widget.touchpadInfo
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import ktx.actors.onChangeEvent
import ktx.scene2d.*

class GameView(
    skin: Skin,
    model : GameModel
    ) : Table(skin), KTable {

    private val playerInfo : TouchpadInfo
    private val enemyInfo : TouchpadInfo

    init {
        setFillParent(true)
        playerInfo = touchpadInfo(Drawables.PLAYER)
        enemyInfo = touchpadInfo(Drawables.PLAYER){
            it.row()
        }
//        table{
//            bottomTableCell ->
//            touchpad(0f){ cell ->
//                this.onChangeEvent {
//                    model.onTouchChange(knobPercentX,knobPercentY)
//                }
//                cell.expand()
//                    .align(Align.left)
//                    .bottom()
//                    .pad(-10f,-170f,-70f,5f)
//            }
//        }
    }

    fun playerLife(percentage: Float){
        playerInfo.life(percentage)
    }

    fun showEnemyInfo(charDrawable: Drawables,lifePercentage: Float){
        enemyInfo.character(charDrawable)
        enemyInfo.life(lifePercentage,0f)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(skin,model),init)


