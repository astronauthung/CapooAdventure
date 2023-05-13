package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle
import com.github.moonstruck.capooadventure.component.*
import com.github.quillraven.fleks.*


@AllOf([LifeComponent::class])
@NoneOf([DeadComponent::class])
class LifeSystem(
    private val lifeCmps : ComponentMapper<LifeComponent>,
    private val deadCmps : ComponentMapper<DeadComponent>,
    private val playerCmps : ComponentMapper<PlayerComponent>,
    private val physicCmps : ComponentMapper<PhysicComponent>,
    private val animationCmps : ComponentMapper<AnimationComponent>,
) : IteratingSystem(){
    private val damageFont =  BitmapFont(Gdx.files.internal("damage.fnt"))

    private val floatingTextStyle = LabelStyle(damageFont, Color.WHITE)

    override fun onTickEntity(entity: Entity) {
        val lifeCmp = lifeCmps[entity]
        lifeCmp.life = (lifeCmp.life + lifeCmp.regeneration* deltaTime).coerceAtMost(lifeCmp.max)

        if(lifeCmp.takeDamage > 0f){
            val physicCmp = physicCmps[entity]
            lifeCmp.life -= lifeCmp.takeDamage
            floatingText(lifeCmp.takeDamage.toInt().toString(),physicCmp.body.position,physicCmp.size)
            lifeCmp.takeDamage = 0f
        }

        if(lifeCmp.isDead)
        {
            animationCmps.getOrNull(entity)?.let{animationCmp ->
                animationCmp.nextAnimation(AnimationType.DEAD)
                animationCmp.playMode= Animation.PlayMode.NORMAL
            }

            configureEntity(entity){
                deadCmps.add(it){
                    if(it in playerCmps){
                        reviveTime = 7f
                    }
                }
            }
        }
    }

    private fun floatingText(text: String, position: Vector2, size: Vector2) {
        world.entity{
            add<FloatingTextComponent>{
                txtLocation.set(position.x,position.y - size.y * 0.5f)
                lifeSpan = 1.5f
                label = Label(text, floatingTextStyle)
            }
        }
    }

    override fun onDispose() {
        damageFont.dispose()
    }

}
