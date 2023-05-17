package com.github.moonstruck.capooadventure.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.github.moonstruck.capooadventure.event.*
import com.github.quillraven.fleks.IntervalSystem
import ktx.assets.disposeSafely
import ktx.log.logger
import ktx.tiled.propertyOrNull

class AudioSystem : EventListener, IntervalSystem() {

    private val musicCache = mutableMapOf<String, Music>()
    private val soundCache = mutableMapOf<String, Sound>()
    private val soundRequests = mutableMapOf<String, Sound>()
    private var music:Music? = null
    override fun onTick() {
        if (soundRequests.isEmpty()) {
            return
        }

        soundRequests.values.forEach { it.play(1f) }
        soundRequests.clear()
    }

    override fun handle(event: Event?): Boolean {
        when (event) {
            is MapChangeEvent -> {
                event.map.propertyOrNull<String>("music")?.let { path ->
                    log.debug {"Changing music to $path"}
                    music = musicCache.getOrPut(path) {
                        Gdx.audio.newMusic(Gdx.files.internal(path)).apply {
                            isLooping = true
                        }
                    }
                    music?.play()
                }
                return true
            }
            is EntityAttackEvent -> queueSound("audio/${event.atlasKey}_attack.wav")
            is EntityDeathEvent -> queueSound("audio/${event.atlasKey}_death.wav")
            is EntityLootEvent -> queueSound("audio/chest_open.wav")

            is GamePauseEvent -> {
                music?.pause()
                soundCache.values.forEach {
                    it.pause()
                }
            }
            is GameResumeEvent ->{
                music?.play()
                soundCache.values.forEach {
                    it.resume()
                }
            }
        }
        return false
    }

    private fun queueSound(sounPath: String) {
        log.debug { "Queueing new sound '$sounPath'" }
        if (sounPath in soundRequests) {
            return
        }
        val sound = soundCache.getOrPut(sounPath) {
            Gdx.audio.newSound(Gdx.files.internal(sounPath))
        }
        soundRequests[sounPath] = sound

    }

    override fun onDispose() {
        musicCache.values.forEach {it.disposeSafely()}
        soundCache.values.forEach {it.disposeSafely()}
    }

    companion object {
        private val log = logger<AudioSystem>()
    }
}
