package com.github.moonstruck.capooadventure.system

import com.github.moonstruck.capooadventure.component.DeadComponent
import com.github.moonstruck.capooadventure.component.LifeComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.quillraven.fleks.*


@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    private val lifeCmps : ComponentMapper<LifeComponent>,
    private val deadCmps : ComponentMapper<DeadComponent>,
    private val playerCmps : ComponentMapper<PlayerComponent>,
) : IteratingSystem(){
    override fun onTickEntity(entity: Entity) {
        val lifeCmp = lifeCmps[entity]
        lifeCmp.life = (lifeCmp.life + lifeCmp.regeneration* deltaTime).coerceAtMost(lifeCmp.max)

        if(lifeCmp.takeDamage > 0f){
            lifeCmp.life -= lifeCmp.takeDamage
            lifeCmp.takeDamage = 0f
        }

        if(lifeCmp.isDead)
        {
            configureEntity(entity){
                deadCmps.add(it){
                    if(it in playerCmps){
                        reviveTime = 7f
                    }
                }
            }
        }
    }
}
