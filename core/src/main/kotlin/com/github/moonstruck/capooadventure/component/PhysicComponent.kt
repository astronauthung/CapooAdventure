package com.github.moonstruck.capooadventure.component

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.ComponentListener
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateCfg
import ktx.box2d.BodyDefinition
import ktx.box2d.body

class PhysicComponent {
    lateinit var body : Body

    companion object {
        fun EntityCreateCfg.physicsCmpFromImage (
            world: World,
            image: Image,
            bodyType: BodyType,
            fixtureAction: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
        ) :PhysicComponent {
            val x = image.x
            val y = image.y
            val w = image.width
            val h = image.height
            // func tao 1 cai box bao quanh entities
            return add<PhysicComponent> {
                body = world.body(bodyType) {
                    //no la 1 cai box hinh vuong, vat the nam o giua box => xac dinh vi tri vat nam trong box
                    //lam viec voi box 2d thi *0.5f, vi ong tac gia keu v :D
                    position.set(x + w * 0.5f, y + h * 0.5f)
                    fixedRotation = true
                    allowSleep = false
                    this.fixtureAction(this@add, w, h)
                }
            }
        }
        class PhysicComponentListener : ComponentListener<PhysicComponent> {
            override fun onComponentAdded(entity: Entity, component: PhysicComponent) {
                component.body.userData = entity
            }

            override fun onComponentRemoved(entity: Entity, component: PhysicComponent) {
                val body = component.body
                body.world.destroyBody(body)
                body.userData = null
            }
        }
    }
}
