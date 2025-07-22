package core.game.bots

import content.minigame.pestcontrol.bots.PestControlIntermediateBot
import content.minigame.pestcontrol.bots.PestControlNoviceBot
import core.game.world.map.Location

class PvMBotsBuilder {
    companion object {
        var botsSpawned = 0

        fun create(l: Location?): PvMBots {
            botsSpawned++
            return PvMBots(l)
        }

        @JvmStatic
        fun createPestControlTestBot2(l: Location?): PestControlIntermediateBot {
            botsSpawned++
            return PestControlIntermediateBot(l!!)
        }

        @JvmStatic
        fun createPestControlTestBot(l: Location?): PestControlNoviceBot {
            botsSpawned++
            return PestControlNoviceBot(l!!)
        }
    }
}
