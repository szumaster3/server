package core.game.bots.impl

import core.game.bots.*
import core.game.interaction.DestinationFlag
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.interaction.MovementPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items

@PlayerCompatible
@ScriptName("Draynor Willows")
@ScriptDescription("Start in Draynor with an axe equipped or in inventory.")
@ScriptIdentifier("draynor_trees")
class DraynorWillows : Script() {
    val willowZone = ZoneBorders(3094, 3225, 3079, 3239)
    val bankZone = ZoneBorders(3092, 3245, 3092, 3242)
    var overlay: ScriptAPI.BottingOverlay? = null
    var state = State.INIT
    var logCount = 0
    var stage = 0

    override fun tick() {
        when (state) {
            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Woodcutting")
                overlay!!.setTaskLabel("Logs cut:")
                overlay!!.setAmount(0)
                state = State.CHOPPING
            }

            State.CHOPPING -> {
                if (!willowZone.insideBorder(bot)) {
                    scriptAPI.walkTo(willowZone.randomLoc)
                } else {
                    val tree = scriptAPI.getNearestNode("Willow", true)
                    bot.interfaceManager.close()
                    tree?.let { InteractionListeners.run(tree.id, IntType.SCENERY, "Chop down", bot, tree) }
                    if (bot.inventory.isFull) {
                        state = State.BANKING
                    }
                    overlay!!.setAmount(logCount + bot.inventory.getAmount(Items.WILLOW_LOGS_1519))
                }
            }

            State.BANKING -> {
                if (!bankZone.insideBorder(bot)) {
                    scriptAPI.walkTo(bankZone.randomLoc)
                } else {
                    val bank = scriptAPI.getNearestNode(2213, true)
                    if (bank != null) {
                        bot.pulseManager.run(
                            object : MovementPulse(bot, bank, DestinationFlag.OBJECT) {
                                override fun pulse(): Boolean {
                                    bot.faceLocation(bank.location)
                                    logCount += bot.inventory.getAmount(Items.WILLOW_LOGS_1519)
                                    scriptAPI.bankItem(Items.WILLOW_LOGS_1519)
                                    state = State.CHOPPING
                                    return true
                                }
                            },
                        )
                    }
                }
            }
        }
    }

    init {
        inventory.add(Item(Items.ADAMANT_AXE_1357))
        skills[Skills.WOODCUTTING] = 40
    }

    override fun newInstance(): Script {
        val script = DraynorWillows()
        return script
    }

    enum class State {
        INIT,
        CHOPPING,
        BANKING,
    }
}
