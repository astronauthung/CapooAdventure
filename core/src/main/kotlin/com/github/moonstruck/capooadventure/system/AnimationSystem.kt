package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.moonstruck.capooadventure.component.AnimationComponent
import com.github.moonstruck.capooadventure.component.AnimationComponent.Companion.NO_ANIMATION
import com.github.moonstruck.capooadventure.component.ImageComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import ktx.app.gdxError
import ktx.collections.map


@AllOf([AnimationComponent::class , ImageComponent::class])
class AnimationSystem(
    private val textureAtlas: TextureAtlas,
    private val animationCmps: ComponentMapper<AnimationComponent>,
    private val imageCmps: ComponentMapper<ImageComponent>
) :IteratingSystem() {

    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val animationCmps = animationCmps[entity]

         if(animationCmps.nextAnimation == NO_ANIMATION){
            animationCmps.stateTime += deltaTime
        }else
        {
            animationCmps.animation = animation(animationCmps.nextAnimation)
            animationCmps.stateTime = 0f
            animationCmps.nextAnimation = NO_ANIMATION
        }
        animationCmps.animation.playMode = animationCmps.playMode
        imageCmps[entity].image.drawable = animationCmps.animation.getKeyFrame(animationCmps.stateTime)
    }

    private fun animation(animationKeyPath:String):Animation<TextureRegionDrawable>
    {
        return cachedAnimations.getOrPut(animationKeyPath){
            val regions = textureAtlas.findRegions(animationKeyPath)
            if(regions.isEmpty)
            {
                gdxError("There are no texture region for $animationKeyPath")
            }
            Animation(1/8f,regions.map { TextureRegionDrawable(it) })
        }
    }
}
