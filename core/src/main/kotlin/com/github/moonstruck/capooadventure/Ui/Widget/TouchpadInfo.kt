package com.github.moonstruck.capooadventure.Ui.Widget

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup
import com.badlogic.gdx.utils.Scaling
import com.github.moonstruck.capooadventure.Ui.Drawables
import com.github.moonstruck.capooadventure.Ui.get
import com.github.moonstruck.capooadventure.Ui.View.GameView
import ktx.actors.plusAssign
import ktx.scene2d.*

class TouchpadInfo(charDrawable:Drawables?,private val skin: Skin) : WidgetGroup(), KGroup {
    private val touchBackground : Image = Image(skin.getDrawable("touch_pad"))
    private val touchKnob : Image = Image(skin.getDrawable("touch_knob"))

    private val background : Image = Image(skin[Drawables.CHAR_INFO_BGD])
    private val charBgd : Image = Image(if(charDrawable==null) null else skin[charDrawable])
    private val lifeBar : Image = Image(skin[Drawables.LIFE_BAR])
    private val manaBar : Image = Image(skin[Drawables.MANA_BAR])

    init {
        this += background
        this += charBgd.apply {
            setPosition(1.5f,1f)
            setSize(22f,20f)
            setScaling(Scaling.contain)
        }
        this += lifeBar.apply { setPosition(26f,19f) }
        this += manaBar.apply { setPosition(26f,13f) }
    }

    override fun getPrefWidth() = touchBackground.drawable.minWidth

    override fun getPrefHeight() = touchBackground.drawable.minHeight


    fun character(charDrawable: Drawables?){
        if(charDrawable == null){
            charBgd.drawable = null
        }else{
            charBgd.drawable = skin[charDrawable]
        }
    }

    fun life(percentage: Float , duration : Float = 0.75f){
        lifeBar.clearActions()
        lifeBar += Actions.scaleTo(MathUtils.clamp(percentage,0f,1f),1f,duration)
    }
}

@Scene2dDsl
fun <S> KWidget<S>.touchpadInfo(
    charDrawable: Drawables?,
    skin: Skin = Scene2DSkin.defaultSkin,
    init: TouchpadInfo.(S) -> Unit = {}
) : TouchpadInfo = actor(TouchpadInfo(charDrawable,skin),init)
