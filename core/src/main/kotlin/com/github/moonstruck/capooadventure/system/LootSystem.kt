package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.github.moonstruck.capooadventure.component.AnimationComponent
import com.github.moonstruck.capooadventure.component.AnimationType
import com.github.moonstruck.capooadventure.component.LootComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([LootComponent::class])
class LootSystem(
    private val lootCmps: ComponentMapper<LootComponent>,
    private val animationCmps : ComponentMapper<AnimationComponent>
) :IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        with(lootCmps[entity]){
            if(interactEntity == null){
                return
            }

            configureEntity(entity){ lootCmps.remove(it)}
            animationCmps.getOrNull(entity)?.let{animationCmps ->
                animationCmps.nextAnimation(AnimationType.OPEN)
                animationCmps.playMode = Animation.PlayMode.NORMAL
            }
        }
    }
}
