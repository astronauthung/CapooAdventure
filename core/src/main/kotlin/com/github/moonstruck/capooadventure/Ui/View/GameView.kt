package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.Game
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.Align
import com.github.moonstruck.capooadventure.Ui.Drawables
import com.github.moonstruck.capooadventure.Ui.Labels
import com.github.moonstruck.capooadventure.Ui.get
import com.github.moonstruck.capooadventure.Ui.Widget.TouchpadInfo
import com.github.moonstruck.capooadventure.Ui.Widget.touchpadInfo
import com.github.moonstruck.capooadventure.Ui.model.GameModel
import ktx.actors.alpha
import ktx.actors.onChangeEvent
import ktx.actors.plusAssign
import ktx.actors.txt
import ktx.scene2d.*

class GameView(
    skin: Skin,
    model : GameModel
    ) : Table(skin), KTable {

    private val playerInfo : TouchpadInfo
    private val enemyInfo : TouchpadInfo
    private val popupLabel : Label

    init {
        setFillParent(true)
        enemyInfo = touchpadInfo(Drawables.PLAYER){
            this.alpha = 0f
            it.row()
        }

        table{
            background = skin[Drawables.FRAME_BGD]

           this@GameView.popupLabel = label(text = "",style = Labels.FRAME.skinKey){ lblCell->
                this.setAlignment(Align.topLeft)
                this.wrap = true
                lblCell.expand().fill().pad(14f)
            }
            this.alpha = 0f
            it.expand().width(130f).height(90f).top().row()
        }

        playerInfo = touchpadInfo(Drawables.PLAYER)

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

        enemyInfo.clearActions()
        enemyInfo += sequence(fadeIn(1f, Interpolation.bounceIn), delay(5f, fadeOut(0.5f)))
    }

    fun popup(infoText:String ){
        popupLabel.txt = infoText
        popupLabel.parent.clearActions()
        popupLabel.parent += sequence(fadeIn(0.2f), delay(4f, fadeOut(0.75f)))
    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(skin,model),init)


