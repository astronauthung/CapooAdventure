package com.github.moonstruck.capooadventure.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

enum class AnimationActor{
    PLAYER,SLIME,CHICKEN,CHEST,UNDEFINED;

    val atlasKey: String = this.toString().lowercase()
}

enum class AnimationType{
    IDLE,RUN,ATTACK,DEAD,OPEN;

    val atlasKey:String = this.toString().lowercase()
}

class AnimationComponent(
    var actor:AnimationActor = AnimationActor.UNDEFINED,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP

) {
    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation:String = ""

    val isAnimationDone : Boolean
    get() = animation.isAnimationFinished(stateTime)
    fun nextAnimation(actor: AnimationActor, type: AnimationType){
        this.actor = actor
        nextAnimation = "${actor.atlasKey}/${type.atlasKey}"
    }
    fun nextAnimation( type: AnimationType){
        nextAnimation = "${actor.atlasKey}/${type.atlasKey}"
    }

    companion object {
        val NO_ANIMATION = ""
    }
}
