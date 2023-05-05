package com.github.moonstruck.capooadventure.Ai

import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram

interface EntityState : State<AiEntity> {
    override fun enter(entity: AiEntity)= Unit

    override fun update(entity: AiEntity)= Unit

    override fun exit(entity: AiEntity)= Unit

    override fun onMessage(entity: AiEntity, telegram: Telegram?): Boolean = false
}
