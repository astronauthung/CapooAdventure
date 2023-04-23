package com.github.moonstruck.capooadventure.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2


const val DEFAULT_SPEED = 100f
data class SpawnCfg (
    val model : AnimationActor,
    val speedScaling : Float = 1f,
)
class SpawnComponent {
    var name : String = ""
    var location : Vector2 = vec2()

}
