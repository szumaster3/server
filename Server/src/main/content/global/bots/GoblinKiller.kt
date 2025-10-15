package content.global.bots

import core.game.bots.CombatBotAssembler
import core.game.bots.Script
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.zone.ZoneBorders
import core.tools.RandomFunction
import shared.consts.Items

class GoblinKiller : Script() {
    var state = State.KILLING
    var spawnZone = ZoneBorders(3243, 3244, 3263, 3235)
    var goblinZone = ZoneBorders(3240, 3228, 3264, 3254)
    var delay = 0

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
                state = State.LOOTING
            }

            State.LOOTING -> {
                bot.pulseManager.run(
                    object : Pulse(4) {
                        override fun pulse(): Boolean {
                            scriptAPI.takeNearestGroundItem(Items.BONES_526)
                            buryBones()
                            state = State.KILLING
                            return true
                        }
                    },
                )
            }
        }
    }

    enum class State {
        KILLING,
        LOOTING
    }

    private fun dialogue() {
        if (delay-- <= 0) {
            scriptAPI.sendChat(forceChat.random())
            delay = RandomFunction.random(10, 30)
        }
    }

    private fun buryBones() {
        if (bot.inventory.containsAtLeastOneItem(Items.BONES_526)) {
            InteractionListeners.run(
                Items.BONES_526, IntType.ITEM, "bury",
                bot, bot.inventory.get(Item(Items.BONES_526))
            )
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
