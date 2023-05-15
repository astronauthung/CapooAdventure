package com.github.moonstruck.capooadventure.Ai

import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.github.moonstruck.capooadventure.component.*
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.World
import ktx.math.component1
import ktx.math.component2
import ktx.math.vec2

private val TMP_RECT = Rectangle()

data class AiEntity(
    private val entity: Entity,
    private val world: World,
    private val animationCmps: ComponentMapper<AnimationComponent> = world.mapper(),
    private val moveCmps : ComponentMapper<MoveComponent> = world.mapper(),
    private val attackCmps : ComponentMapper<AttackComponent> = world.mapper(),
    private val lifeCmps : ComponentMapper<LifeComponent> = world.mapper(),
    private val stateCmps : ComponentMapper<StateComponent> = world.mapper(),
    private val physicCmps : ComponentMapper<PhysicComponent> = world.mapper(),
    private val aiCmps : ComponentMapper<AiComponent> = world.mapper(),
    private val playerCmps : ComponentMapper<PlayerComponent> = world.mapper(),
) {

    val position: Vector2
    get() = physicCmps[entity].body.position

    val wantToAttack : Boolean
        get() = attackCmps.getOrNull(entity)?.doAttack?:false

    val wantToRun : Boolean
        get() {
            val moveCmp = moveCmps[entity]
            return moveCmp.cos != 0f || moveCmp.sin != 0f
        }
    val attackCmp : AttackComponent
        get() = attackCmps[entity]

    val isAnimationDone : Boolean
        get() = animationCmps[entity].isAnimationDone
    val isDead : Boolean
        get() = lifeCmps[entity].isDead


    fun animation(type: AnimationType , mode: PlayMode = PlayMode.LOOP,resetAnimation : Boolean = false) {
        with(animationCmps[entity]){
            nextAnimation(type)
            playMode = mode
            if(resetAnimation){
                stateTime = 0f
            }
        }
    }

    fun state(next: EntityState,immediateChange:Boolean = false){
        with(stateCmps[entity]){
            nextState = next
            if(immediateChange){
                stateMachine.changeState(nextState)
            }
        }
    }

    fun changeToPreviousState(){
        with(stateCmps[entity]){
           nextState = stateMachine.previousState
        }
    }

    fun root(enable : Boolean){
        with(moveCmps[entity]){
            root = enable
        }
    }

    fun startAttack(){
        with(attackCmps[entity]){
            startAttack()
        }
    }

    fun doAndStartAttack(){
        with(attackCmps[entity]){
            doAttack = true
            startAttack()
        }
    }

    fun moveTo(target:Vector2){
        val(targetX,targetY) = target
        val physicCmp = physicCmps[entity]
        val (sourceX,sourceY) = physicCmp.body.position
        with(moveCmps[entity]){
            val angleRad = MathUtils.atan2(targetY-sourceY,targetX-sourceX)
            cos = MathUtils.cos(angleRad)
            sin = MathUtils.sin(angleRad)
        }
    }

    fun inRange(range:Float , target: Vector2): Boolean{
        val physicCmp = physicCmps[entity]
        val (sourceX,sourceY) = physicCmp.body.position
        val (offX,offY) = physicCmp.offset
        var (sizeX,sizeY) = physicCmp.size
        sizeX += range
        sizeY += range

        TMP_RECT.set(sourceX + offX - sizeX*0.5f,sourceY + offY - sizeY*0.5f,sizeX,sizeY)

        return TMP_RECT.contains(target)
    }

    fun canAttack():Boolean{
        val attackCmp = attackCmps[entity]
        if(!attackCmp.isReady){
            return false
        }

        val enemy = nearbyEnemies().firstOrNull()
        if(enemy == null){
            return false
        }

        val enemyPhysicCmp = physicCmps[enemy]
        val (sourceX,sourceY) = enemyPhysicCmp.body.position
        val (offX,offY) = enemyPhysicCmp.offset
        var (sizeX,sizeY) = enemyPhysicCmp.size

        return inRange(1.5f+attackCmp.extraRange, vec2(sourceX+offX,sourceY+offY))
    }


    private fun nearbyEnemies() : List<Entity> {
        val aiCmp = aiCmps[entity]
        return aiCmp.nearbyEntities
            .filter { it in playerCmps && !lifeCmps[it].isDead }
    }
    fun hasEnemyNearby():Boolean = nearbyEnemies().isNotEmpty()

    fun stopMovement(){
        with(moveCmps[entity]){
            cos = 0f
            sin = 0f
        }
    }

    fun enableGlobalState(enable: Boolean) {
        with(stateCmps[entity]){
            if(enable){
                stateMachine.globalState = DefaultGlobalState.CHECK_ALIVE
            }
            else{
                stateMachine.globalState = null
            }
        }
    }
}
