package core.game.bots.impl

import core.game.bots.*
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items

@PlayerCompatible
@ScriptDescription("Start in varrock bank with rune mysteries complete and a pickaxe equipped/in inventory")
@ScriptName("Varrock Essence Miner")
@ScriptIdentifier("essence_miner")
class VarrockEssenceMiner : Script() {
    var state = State.TO_ESSENCE
    val auburyZone = ZoneBorders(3252, 3398, 3254, 3402)
    val bankZone = ZoneBorders(3251, 3420, 3254, 3422)

    override fun tick() {
        when (state) {
            State.TO_ESSENCE -> {
                bot.interfaceManager.close()
                if (!auburyZone.insideBorder(bot)) {
                    scriptAPI.walkTo(auburyZone.randomLoc)
                } else {
                    val aubury = scriptAPI.getNearestNode("Aubury")
                    aubury?.interaction?.handle(bot, aubury.interaction[3])
                    state = State.MINING
                }
            }

            State.MINING -> {
                if (bot.inventory.isFull) {
                    state = State.TO_BANK
                } else {
                    val essence = scriptAPI.getNearestNode(2491, true)
                    essence?.let { InteractionListeners.run(essence.id, IntType.SCENERY, "mine", bot, essence) }
                }
            }

            State.TO_BANK -> {
                val portal = scriptAPI.getNearestNode("Portal", true)
                if (portal != null && portal.location.withinDistance(bot.location, 20)) {
                    portal.interaction.handle(bot, portal.interaction[0])
                } else {
                    if (!bankZone.insideBorder(bot)) {
                        scriptAPI.walkTo(bankZone.randomLoc)
                    } else {
                        state = State.BANKING
                    }
                }
            }

            State.BANKING -> {
                val bank = scriptAPI.getNearestNode("bank booth", true)
                val item =
                    if (bot.inventory.getAmount(Items.RUNE_ESSENCE_1436) >
                        0
                    ) {
                        Items.RUNE_ESSENCE_1436
                    } else {
                        Items.PURE_ESSENCE_7936
                    }
                if (bank != null) {
                    bot.pulseManager.run(
                        object : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
                            override fun pulse(): Boolean {
                                bot.faceLocation(bank.location)
                                scriptAPI.bankItem(item)
                                state = State.TO_ESSENCE
                                return true
                            }
                        },
                    )
                }
            }

            State.TELE_GE -> {
                if (bot.location != Location.create(3165, 3482, 0)) {
                    scriptAPI.walkTo(Location.create(3165, 3482, 0))
                } else {
                    state = State.SELL_GE
                }
            }

            State.SELL_GE -> {
                scriptAPI.sellOnGE(Items.PURE_ESSENCE_7936)
                state = State.TO_ESSENCE
            }
        }
    }

    enum class State {
        TO_ESSENCE,
        TO_BANK,
        MINING,
        BANKING,
        TELE_GE,
        SELL_GE,
    }

    override fun newInstance(): Script {
        val script = VarrockEssenceMiner()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR, bot.startLocation)
        return script
    }
}
