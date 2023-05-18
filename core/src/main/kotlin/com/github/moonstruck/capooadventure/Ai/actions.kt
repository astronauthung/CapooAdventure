package com.github.moonstruck.capooadventure.Ai

import com.badlogic.gdx.ai.GdxAI
import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute
import com.badlogic.gdx.ai.utils.random.FloatDistribution
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.math.MathUtils
import com.github.moonstruck.capooadventure.component.AnimationType
import com.github.moonstruck.capooadventure.event.EntityAggroEvent
import ktx.math.vec2

abstract class Action :LeafTask<AiEntity>(){
    val entity: AiEntity
    get() = `object`

    override fun copyTo(task: Task<AiEntity>) = task
}

class idleTask(
    @JvmField
    @TaskAttribute(required = true)
    var duration:FloatDistribution? = null
) : Action(){
    private var currentDuration = 0f

    override fun execute(): Status {
        if(status != Status.RUNNING){
            entity.animation(AnimationType.IDLE)
            currentDuration = duration?.nextFloat() ?:1f
            return Status.RUNNING
        }

        currentDuration -= GdxAI.getTimepiece().deltaTime
        if(currentDuration != 0f){
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }

    override fun copyTo(task: Task<AiEntity>): Task<AiEntity> {
        (task as idleTask).duration = duration
        return task
    }
}
class wanderTask : Action(){

    private val startPos = vec2()
    private val targetPos = vec2()

    override fun execute(): Status {
        if(status != Status.RUNNING){
            entity.animation(AnimationType.RUN)
            if(startPos.isZero){
                startPos.set(entity.position)
            }
            targetPos.set(startPos)
            targetPos.x += MathUtils.random(-1f,1f)
            targetPos.y += MathUtils.random(-1f,1f)
            entity.moveTo(targetPos)
            return Status.RUNNING
        }

        if(entity.inRange(0.5f,targetPos)){
            entity.stopMovement()
            return Status.SUCCEEDED
        }
        return Status.RUNNING
    }
}


class attackTask : Action(){
    override fun execute(): Status {
        if(status != Status.RUNNING){
            entity.animation(AnimationType.ATTACK,Animation.PlayMode.NORMAL,true)
            entity.fireEvent(EntityAggroEvent(entity.entity))
            entity.doAndStartAttack()
            return Status.RUNNING
        }

        if(entity.isAnimationDone){
            entity.animation(AnimationType.IDLE)
            entity.stopMovement()
            return Status.SUCCEEDED
        }

        return Status.RUNNING
    }
}
