package com.github.moonstruck.capooadventure.component



enum class AttackState{
    READY, PREPARE, ATTACKING, DEAL_DAMAGE
}
data class AttackComponent (
    var doAttack:Boolean = false,
    var state:AttackState = AttackState.READY,
    var damage: Int = 0,
    var delay : Float = 0f,
    var maxDelay : Float = 0f,
    var extraRange : Float = 0f,
    ){
    val isReady:Boolean
        get() = state == AttackState.READY

    val isPrepared:Boolean
        get() = state == AttackState.PREPARE
    val isAttacking:Boolean
        get() = state == AttackState.ATTACKING

    fun startAttack(){
        state = AttackState.PREPARE
    }
}
