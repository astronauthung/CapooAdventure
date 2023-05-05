package com.github.moonstruck.capooadventure.system

import com.github.moonstruck.capooadventure.component.StateComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([StateComponent::class])
class StateSystem(
    private val stateCmps: ComponentMapper<StateComponent>
) : IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        with(stateCmps[entity]){
            if(nextState != stateMachine.currentState){
                stateMachine.changeState(nextState)
            }

            stateMachine.update()
        }
    }
}
