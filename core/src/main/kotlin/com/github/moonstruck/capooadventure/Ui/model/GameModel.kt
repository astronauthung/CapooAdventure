package com.github.moonstruck.capooadventure.Ui.model

import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.moonstruck.capooadventure.component.AnimationComponent
import com.github.moonstruck.capooadventure.component.AttackComponent
import com.github.moonstruck.capooadventure.component.LifeComponent
import com.github.moonstruck.capooadventure.component.PlayerComponent
import com.github.moonstruck.capooadventure.event.EntityAggroEvent
import com.github.moonstruck.capooadventure.event.EntityDamageEvent
import com.github.moonstruck.capooadventure.event.EntityLootEvent
import com.github.moonstruck.capooadventure.input.PlayerInputProcessor
import com.github.quillraven.fleks.ComponentMapper
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Qualifier
import com.github.quillraven.fleks.World


class GameModel(
    world: World,
    @Qualifier("UiStage")private val stage: Stage,
    private val playerInputProcessor: PlayerInputProcessor,
) : PropertyChangeSource(),EventListener{

    private val playerCmps:ComponentMapper<PlayerComponent> = world.mapper()
    private val lifeCmps:ComponentMapper<LifeComponent> = world.mapper()
    private val animationCmps:ComponentMapper<AnimationComponent> = world.mapper()
    private val attackCmps: ComponentMapper<AttackComponent> = world.mapper()
    private val playerEntities = world.family(allOf = arrayOf(PlayerComponent::class))

    var playerLife by propertyNotify(1f)

    private var lastEnemy = Entity(-1)
    var enemyType by propertyNotify("")

    var enemyLife by propertyNotify(1f)

    var lootText by propertyNotify("")
    init{
        stage.addListener(this)
    }

    private fun updateEnemy(enemy: Entity){
        val lifeCmp = lifeCmps[enemy]
        enemyLife = lifeCmp.life/lifeCmp.max
        if(lastEnemy != enemy){
            lastEnemy = enemy
            animationCmps.getOrNull(enemy)?.actor?.atlasKey?.let { type ->
                    this.enemyType = type
            }
        }
    }

    override fun handle(event: Event): Boolean {
        when(event){
            is EntityDamageEvent -> {
                val isPlayer = event.entity in playerCmps
                val lifeCmp  = lifeCmps[event.entity]
                if(isPlayer){
                    playerLife = lifeCmp.life/lifeCmp.max
                }else{
                    updateEnemy(event.entity)
                }
            }

            is EntityLootEvent ->{
                lootText = "You found something cool in the chest!"
            }

            is EntityAggroEvent ->{
                updateEnemy(event.entity)
            }
            else -> return false
        }
        return true
    }

    fun onTouchChange(knobPercentX: Float, knobPercentY: Float) {
        playerInputProcessor.touchpadMove(knobPercentX,knobPercentY)
    }
    fun clickAttack(){
        playerEntities.forEach {
            with(attackCmps[it]){
                startAttack()
                doAttack = true
                delay = 0f
            }
        }
    }
    fun openInventory(){
        playerInputProcessor.inventory()
    }
}
