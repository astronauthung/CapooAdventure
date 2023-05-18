package com.github.moonstruck.capooadventure.system

import box2dLight.RayHandler
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.github.moonstruck.capooadventure.component.LightComponent
import com.github.quillraven.fleks.*


@AllOf([LightComponent::class])
class LightSystem(
    private val rayHandler: RayHandler,
    private val lightCmps: ComponentMapper<LightComponent>,
) : IteratingSystem() {
    // time convert light and dark
    // =1f when the convert is successfully
    //ngay tu dau ban dem luon thi set = 1f :D
    private var ambientTransitionTime = 1f
    private var ambientColor = Color(1f,1f,1f,1f)
    private var ambientFrom = dayAmbientLight
    private var ambientTo = nightAmbientLight

    override fun onTick() {
        super.onTick()

        if (Gdx.input.isKeyJustPressed(Input.Keys.N) && ambientTransitionTime == 1f) {
            //switch day to night
            ambientTransitionTime = 0f
            ambientFrom = dayAmbientLight
            ambientTo = nightAmbientLight
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.D) && ambientTransitionTime == 1f) {
            ambientTransitionTime = 0f
            ambientFrom = nightAmbientLight
            ambientTo = dayAmbientLight
        }

        if (ambientTransitionTime < 1f) {
            ambientTransitionTime = (ambientTransitionTime + deltaTime * 0.5f).coerceAtMost(1f)

            ambientColor.r = interpolation.apply(ambientFrom.r, ambientTo.r, ambientTransitionTime)
            ambientColor.g = interpolation.apply(ambientFrom.g, ambientTo.g, ambientTransitionTime)
            ambientColor.b = interpolation.apply(ambientFrom.b, ambientTo.b, ambientTransitionTime)
            ambientColor.a = interpolation.apply(ambientFrom.a, ambientTo.a, ambientTransitionTime)

            rayHandler.setAmbientLight(ambientColor)
        }
    }

    override fun onTickEntity(entity: Entity) {
        val lightCmp = lightCmps[entity]
        val (disance, time, direction, light) = lightCmp

        lightCmp.distanceTime = (time + direction * deltaTime).coerceIn(0f, 1f)

        if (lightCmp.distanceTime == 0f || lightCmp.distanceTime == 1f) {
            lightCmp.distanceDirection *= -1
        }

        light.distance = interpolation.apply(disance.start, disance.endInclusive, lightCmp.distanceTime)
    }

    companion object {
        private val interpolation: Interpolation = Interpolation.smoother
        private val dayAmbientLight = Color.WHITE
        private val nightAmbientLight = Color.ROYAL
    }
}
