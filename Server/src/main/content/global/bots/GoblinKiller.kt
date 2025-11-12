package content.global.bots

import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction

class GoblinKiller : Script() {
    var state = State.KILLING
    private var spawnZone = ZoneBorders(3243, 3244, 3263, 3235)
    private var goblinZone = ZoneBorders(3240, 3228, 3264, 3254)
    private var waitTicks = 0
    private var delay = 0

    val forceChat = arrayOf(
        "i kill u next",
        "wat ur lvl?",
        "lol i pwn u",
        "use spells dildo boi",
        "look at my sord",
        "u bot",
        "I heard bank not working",
        "fernando were u running",
        "goblin > u",
        "cant find goblins",
        "noob armor smh",
        "who hits me?"
    )

    init {
        goblinZone.addException(ZoneBorders(3240, 3228, 3264, 3254))
    }

    override fun tick() {
        dialogue()

        when (state) {
            State.KILLING -> {
                scriptAPI.attackNpcInRadius(bot, "Goblin", 10)
                state = State.WAITING
                waitTicks = 5
            }

            State.WAITING -> {
                if (waitTicks-- <= 0) {
                    state = State.KILLING
                }
            }
        }
    }

    enum class State {
        KILLING,
        WAITING
    }

    private fun dialogue() {
        if (delay-- <= 0) {
            scriptAPI.sendChat(forceChat.random())
            delay = RandomFunction.random(10, 30)
        }
    }

    override fun newInstance(): Script {
        val script = GoblinKiller()
        val bot = CombatBotAssembler().produce(
            CombatBotAssembler.Type.MELEE,
            CombatBotAssembler.Tier.LOW,
            spawnZone.randomLoc,
        )

        script.bot = bot
        script.state = State.KILLING
        return script
    }
}