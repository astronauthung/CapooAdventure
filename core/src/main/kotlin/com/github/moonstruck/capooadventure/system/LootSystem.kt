package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.AnimationComponent
import com.github.moonstruck.capooadventure.component.AnimationType
import com.github.moonstruck.capooadventure.component.LootComponent
import com.github.moonstruck.capooadventure.event.EntityLootEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.quillraven.fleks.*


@AllOf([LootComponent::class])
class LootSystem(
    private val lootCmps: ComponentMapper<LootComponent>,
    private val animationCmps : ComponentMapper<AnimationComponent>,
    @Qualifier("GameStage") private val stage: Stage,
) :IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        with(lootCmps[entity]){
            if(interactEntity == null){
                return
            }
            animationCmps.getOrNull(entity)?.let{animationCmps ->
                animationCmps.nextAnimation(AnimationType.OPEN)
                animationCmps.playMode = Animation.PlayMode.NORMAL
            }
            stage.fire(EntityLootEvent())
            configureEntity(entity){ lootCmps.remove(it)}
        }
    }
}
