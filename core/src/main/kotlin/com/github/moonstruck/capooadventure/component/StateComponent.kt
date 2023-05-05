package com.github.moonstruck.capooadventure.component

import com.badlogic.gdx.ai.fsm.DefaultStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.github.moonstruck.capooadventure.Ai.AiEntity
import com.github.moonstruck.capooadventure.Ai.DefaultState
import com.github.moonstruck.capooadventure.Ai.EntityState
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World


data class StateComponent (
    var nextState : EntityState = DefaultState.IDLE,
    val stateMachine : DefaultStateMachine<AiEntity,EntityState> = DefaultStateMachine()
)
{
    companion object{
        class StateComponentListener(
            private val world: World
        ) : ComponentListener<StateComponent>{
            override fun onComponentAdded(entity: Entity, component: StateComponent) {
                component.stateMachine.owner = AiEntity(entity,world)
            }

            override fun onComponentRemoved(entity: Entity, component: StateComponent) = Unit
        }
    }
}
