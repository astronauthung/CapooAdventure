package com.github.moonstruck.capooadventure.Ai

import com.badlogic.gdx.ai.btree.LeafTask
import com.badlogic.gdx.ai.btree.Task

abstract class Condition : LeafTask<AiEntity>() {
    val entity: AiEntity
        get() = `object`

    abstract fun condition() : Boolean

    override fun copyTo(task: Task<AiEntity>?): Task<AiEntity> {
        TODO("Not yet implemented")
    }

    override fun execute(): Status {
        return when{
            condition() -> Status.SUCCEEDED
            else -> Status.FAILED
        }
    }
}

class canAttack : Condition(){
    override fun condition() = entity.canAttack()
}

class isEnemyNearby : Condition(){
    override fun condition() = entity.hasEnemyNearby()
}
