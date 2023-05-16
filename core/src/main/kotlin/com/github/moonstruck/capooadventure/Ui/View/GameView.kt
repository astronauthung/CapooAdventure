package com.github.moonstruck.capooadventure.Ui.View

import com.badlogic.gdx.Game
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.actions.Actions.*
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
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
import ktx.actors.*
import ktx.scene2d.*

class GameView(
    skin: Skin,
    model : GameModel
    ) : Table(skin), KTable {

    private val playerInfo : TouchpadInfo
    private val enemyInfo : TouchpadInfo
    private val popupLabel : Label

    init {
        //ui

        setFillParent(true)
        enemyInfo = touchpadInfo(null,skin){
            this.alpha = 0f
            it.row()
        }

        table{
            background = skin[Drawables.FRAME_BGD]

           this@GameView.popupLabel = label(text = "",style = Labels.FRAME.skinKey){ lblCell->
                this.setAlignment(Align.topLeft)
                this.wrap = true
                lblCell.expand().fill().pad(7f)
            }
            this.alpha = 0f
            it.expand().width(100f).height(65f).top().row()

        }

        playerInfo = touchpadInfo(Drawables.PLAYER,skin){cell->
            cell.align(Align.topLeft).row()
        }
        touchpad(0f){ cell ->
            this.onChangeEvent {
                model.onTouchChange(knobPercentX,knobPercentY)
            }
            cell.left().bottom()
        }
        imageButton {
            this.onClick {
                model.clickAttack()
            }
            it.right().bottom()
        }



        //data binding
        model.onPropertyChange(GameModel::playerLife){ playerLife ->
            playerLife(playerLife)
        }
        model.onPropertyChange(GameModel::lootText){ lootInfo->
            popup(lootInfo)
        }
        model.onPropertyChange(GameModel::enemyLife){ enemyLife->
            enemyLife(enemyLife)
        }
        model.onPropertyChange(GameModel::enemyType){ enemyType->
            when(enemyType){
                "slime" -> {
                    showEnemyInfo(Drawables.SLIME,model.enemyLife)
                }
            }
        }
    }

    fun playerLife(percentage: Float){
        playerInfo.life(percentage)
    }
    fun enemyLife(percentage: Float){
        enemyInfo.life(percentage)
    }

    private fun Actor.resetFadeOutDelay(){
        this.actions
            .filterIsInstance<SequenceAction>()
            .lastOrNull()
            ?.let { sequence->
                val delay = sequence.actions.last() as DelayAction
                delay.time = 0f
            }
    }

    fun showEnemyInfo(charDrawable: Drawables,lifePercentage: Float){
        enemyInfo.character(charDrawable)
        enemyInfo.life(lifePercentage,0f)

        if(enemyInfo.alpha == 0f){
            enemyInfo.clearActions()
            enemyInfo += sequence(fadeIn(1f, Interpolation.bounceIn), delay(5f, fadeOut(0.5f)))
        }else{
            enemyInfo.resetFadeOutDelay()
        }



    }

    fun popup(infoText:String ){
        popupLabel.txt = infoText
        if(popupLabel.parent.alpha == 0f){
            popupLabel.parent.clearActions()
            popupLabel.parent += sequence(fadeIn(0.2f), delay(4f, fadeOut(0.75f)))
        }else{
            popupLabel.parent.resetFadeOutDelay()
        }

    }
}

@Scene2dDsl
fun <S> KWidget<S>.gameView(
    model: GameModel,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: GameView.(S) -> Unit = {}
): GameView = actor(GameView(skin,model),init)


