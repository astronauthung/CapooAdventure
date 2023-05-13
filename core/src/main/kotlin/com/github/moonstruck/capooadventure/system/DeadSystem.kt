package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.Ai.DefaultGlobalState
import com.github.moonstruck.capooadventure.Ai.DefaultState
import com.github.moonstruck.capooadventure.component.AnimationComponent
import com.github.moonstruck.capooadventure.component.DeadComponent
import com.github.moonstruck.capooadventure.component.LifeComponent
import com.github.moonstruck.capooadventure.component.StateComponent
import com.github.moonstruck.capooadventure.event.EntityDeathEvent
import com.github.moonstruck.capooadventure.event.fire
import com.github.quillraven.fleks.*


@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val lifeCmps : ComponentMapper<LifeComponent>,
    private val animationCmps : ComponentMapper<AnimationComponent>,
    @Qualifier("GameStage") private val stage: Stage,
) : IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]
        if(deadCmp.reviveTime == 0f){
            stage.fire(EntityDeathEvent(animationCmps[entity].actor.atlasKey))
            world.remove(entity)
            return
        }

        deadCmp.reviveTime -= deltaTime
        if(deadCmp.reviveTime <= 0f){
            with(lifeCmps[entity]){
                life = max
            }

            configureEntity(entity){
                deadCmps.remove(entity)
            }
        }
    }
}
