package com.github.moonstruck.capooadventure.system

import com.github.moonstruck.capooadventure.component.DeadComponent
import com.github.moonstruck.capooadventure.component.LifeComponent
import com.github.quillraven.fleks.AllOf
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem


@AllOf([DeadComponent::class])
class DeadSystem(
    private val deadCmps: ComponentMapper<DeadComponent>,
    private val lifeCmps : ComponentMapper<LifeComponent>,
) : IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        val deadCmp = deadCmps[entity]
        if(deadCmp.reviveTime == 0f){
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
