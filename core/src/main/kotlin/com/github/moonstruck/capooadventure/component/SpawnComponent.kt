package com.github.moonstruck.capooadventure.component

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

data class SpawnCfg (val model : AnimationActor, )

class SpawnComponent {
    var name : String = ""
    var location : Vector2 = vec2()

}
