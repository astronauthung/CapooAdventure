package com.github.moonstruck.capooadventure.system

import com.github.moonstruck.capooadventure.component.AiComponent
import com.github.moonstruck.capooadventure.component.DeadComponent
import com.github.quillraven.fleks.*

@AllOf([AiComponent::class])
@NoneOf([DeadComponent::class])
class AiSystem(
    private val aiCmps: ComponentMapper<AiComponent>
) :IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        with(aiCmps[entity]){
            behaviorTree.step()
        }
    }
}
