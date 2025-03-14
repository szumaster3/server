package core.game.bots

import core.Server
import core.api.TickListener
import core.api.getWorldTicks
import core.game.bots.impl.Idler
import core.game.interaction.MovementPulse
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.RandomFunction

class GBCTick : TickListener {
    override fun tick() {
        GeneralBotCreator.botPulsesTriggeredThisTick = 0
    }
}

class GeneralBotCreator {
    constructor(botScript: Script, loc: Location?) {
        botScript.bot = AIPBuilder.create(loc)
        GameWorld.Pulser.submit(BotScriptPulse(botScript))
    }

    constructor(botScript: Script, bot: AIPlayer?) {
        botScript.bot = bot
        GameWorld.Pulser.submit(
            BotScriptPulse(botScript).also {
                AIRepository.PulseRepository[
                    it.botScript.bot.username
                        .lowercase(),
                ] = it
            },
        )
    }

    constructor(botScript: Script, player: Player, isPlayer: Boolean) {
        botScript.bot = player
        val pulse = BotScriptPulse(botScript, isPlayer)
        GameWorld.Pulser.submit(pulse)
        player.setAttribute("/save:not_on_highscores", true)
        player.setAttribute("botting:script", pulse)
    }

    companion object {
        var botPulsesTriggeredThisTick = 0
    }

    inner class BotScriptPulse(
        public val botScript: Script,
        val isPlayer: Boolean = false,
    ) : Pulse(1) {
        var ticks = 0

        init {
            botScript.init(isPlayer)
        }

        var randomDelay = 0
        var lastBotLocation: Location = botScript.bot.location.transform(0, 0, 0)
        var lastBotMoveTicks = getWorldTicks()

        override fun pulse(): Boolean {
            if (randomDelay > 0) {
                randomDelay -= 1
                return false
            }
            if (botScript.bot.pulseManager.hasPulseRunning()) {
                if (botScript.bot.pulseManager.current is MovementPulse) {
                    if (botScript.bot.location != lastBotLocation) {
                        lastBotLocation = botScript.bot.location.transform(0, 0, 0)
                        lastBotMoveTicks = getWorldTicks()
                    }
                    if (lastBotLocation == botScript.bot.location && getWorldTicks() - lastBotMoveTicks > 5) {
                        botScript.bot.pulseManager.current
                            .stop()
                    }
                }
            }

            if (botScript.bot.scripts.getActiveScript() != null &&
                botScript.bot.hasModalOpen() &&
                botScript.endDialogue
            ) {
                botScript.bot.interfaceManager.closeChatbox()
                botScript.bot.interfaceManager.openChatbox(137)
                botScript.bot.interfaceManager.close()
                botScript.bot.dialogueInterpreter.close()
            }

            if (!botScript.bot.pulseManager.hasPulseRunning() && botScript.bot.scripts.getActiveScript() == null) {
                if (!botScript.running) return true
                if (botPulsesTriggeredThisTick++ >= 75) {
                    return false
                }

                val idleRoll = RandomFunction.random(10)
                if (idleRoll == 2 && botScript !is Idler) {
                    randomDelay += RandomFunction.random(20, 50)
                    return false
                }
                botScript.tick()
            }
            return false
        }

        override fun stop() {
            ticks = Integer.MAX_VALUE - 20
            pulse()
            super.stop()
            if (Server.running) {
                AIRepository.PulseRepository.remove(
                    this.botScript.bot.username
                        .lowercase(),
                )
            }
        }
    }

    inner class TransitionPulse(
        val script: Script,
    ) : Pulse(RandomFunction.random(60, 200)) {
        override fun pulse(): Boolean {
            GameWorld.Pulser.submit(
                BotScriptPulse(script.newInstance()).also {
                    AIRepository.PulseRepository[
                        it.botScript.bot.username
                            .lowercase(),
                    ] = it
                },
            )
            return true
        }
    }
}
