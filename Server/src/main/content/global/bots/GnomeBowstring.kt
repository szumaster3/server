package content.global.bots

import core.api.freeSlots
import core.game.bots.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListeners
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.path.Pathfinder
import core.game.world.map.zone.ZoneBorders
import shared.consts.Items

@PlayerCompatible
@ScriptName("Gnome Stronghold Bowstring")
@ScriptDescription("Start in Gnome Stronghold, South of the Agility Course")
@ScriptIdentifier("gnome_bowstring")
class GnomeBowstring : Script() {

    enum class State { INIT, PICKING, TO_SPINNER, SPINNING, FIND_BANK, RETURN_TO_FLAX, BANKING }

    private var state = State.INIT
    private var sLadderSwitch = false
    private var bLadderSwitch = false
    private var stage = 0
    private var overlay: ScriptAPI.BottingOverlay? = null

    private val flaxzone = ZoneBorders(2457, 3391, 2493, 3413, 0)
    private val spinnerbottomLadder = ZoneBorders(2475, 3400, 2476, 3399, 0)
    private val spinnertopLadder = ZoneBorders(2474, 3397, 2476, 3399, 1)
    private val bankbottomLadder = ZoneBorders(2444, 3413, 2445, 3414, 0)
    private val banktopLadder = ZoneBorders(2445, 3415, 2446, 3414, 1)
    private val bank = ZoneBorders(2447, 3415, 2444, 3434)

    override fun tick() {
        when (state) {
            State.INIT -> {
                overlay = scriptAPI.getOverlay()
                overlay!!.init()
                overlay!!.setTitle("Picking")
                overlay!!.setTaskLabel("Flax Picked")
                overlay!!.setAmount(0)
                state = State.PICKING
            }

            State.PICKING -> {
                bot.interfaceManager.close()
                if (!flaxzone.insideBorder(bot)) {
                    scriptAPI.walkTo(flaxzone.randomLoc)
                } else {
                    val flax = scriptAPI.getNearestNode(2646, true)
                    if (flax != null) scriptAPI.interact(bot, flax, "pick")
                }

                if (bot.inventory.getAmount(Items.FLAX_1779) > 27) {
                    sLadderSwitch = true
                    state = State.TO_SPINNER
                }
            }

            State.TO_SPINNER -> {
                if (sLadderSwitch) {
                    if (!spinnerbottomLadder.insideBorder(bot.location)) {
                        scriptAPI.walkTo(spinnerbottomLadder.randomLoc)
                    } else {
                        val ladder = scriptAPI.getNearestNode("Staircase", true)
                        if (ladder != null) {
                            ladder.interaction.handle(bot, ladder.interaction[0])
                            sLadderSwitch = false
                        } else {
                            scriptAPI.walkTo(spinnerbottomLadder.randomLoc)
                        }
                    }
                }

                when (bot.location) {
                    Location.create(2475, 3399, 1) -> Pathfinder.find(bot, Location.create(2477, 3399, 1)).walk(bot)
                    Location.create(2477, 3399, 1) -> Pathfinder.find(bot, Location.create(2477, 3398, 1)).walk(bot)
                    Location.create(2477, 3398, 1) -> Pathfinder.find(bot, Location.create(2476, 3398, 1)).walk(bot)
                    Location.create(2476, 3398, 1) -> {
                        val spinner = scriptAPI.getNearestNode(2644, true)
                        if (spinner != null) {
                            bot.faceLocation(spinner.location)
                            InteractionListeners.run(spinner.id, IntType.SCENERY, "spin", bot, Item(Items.FLAX_1779))
                            state = State.FIND_BANK
                        }
                    }
                }
            }

            State.FIND_BANK -> {
                if (sLadderSwitch) {
                    val ladder = scriptAPI.getNearestNode("staircase", true)
                    if (ladder != null) {
                        ladder.interaction.handle(bot, ladder.interaction[0])
                        sLadderSwitch = false
                    }
                }

                if (!bankbottomLadder.insideBorder(bot.location) && !spinnertopLadder.insideBorder(bot)) {
                    scriptAPI.walkTo(bankbottomLadder.randomLoc)
                } else if (bankbottomLadder.insideBorder(bot)) {
                    bLadderSwitch = true
                }

                if (bLadderSwitch) {
                    val ladder = scriptAPI.getNearestNode("staircase", true)
                    if (ladder != null) {
                        ladder.interaction.handle(bot, ladder.interaction[0])
                        bLadderSwitch = false
                    }
                }

                if (banktopLadder.insideBorder(bot)) {
                    state = State.BANKING
                }
            }

            State.BANKING -> {
                val bankObj = scriptAPI.getNearestNode(2213, true)
                if (bankObj != null) {
                    bot.faceLocation(bankObj.location)
                    scriptAPI.bankItem(Items.BOW_STRING_1777)
                }

                if (freeSlots(bot) > 27) {
                    bLadderSwitch = true
                    state = State.RETURN_TO_FLAX
                }
            }

            State.RETURN_TO_FLAX -> {
                if (!banktopLadder.insideBorder(bot.location) && bLadderSwitch) {
                    scriptAPI.walkTo(banktopLadder.randomLoc)
                } else if (bLadderSwitch) {
                    val ladder = scriptAPI.getNearestNode("Staircase", true)
                    if (ladder != null) {
                        ladder.interaction.handle(bot, ladder.interaction[0])
                        bLadderSwitch = false
                    } else {
                        scriptAPI.walkTo(banktopLadder.randomLoc)
                    }
                }

                if (!flaxzone.insideBorder(bot)) {
                    scriptAPI.walkTo(flaxzone.randomLoc)
                } else {
                    state = State.PICKING
                }
            }

            else -> {}
        }
    }

    override fun newInstance(): Script {
        val script = GnomeBowstring()
        script.bot = SkillingBotAssembler().produce(SkillingBotAssembler.Wealth.POOR, bot.startLocation)
        return script
    }
}
