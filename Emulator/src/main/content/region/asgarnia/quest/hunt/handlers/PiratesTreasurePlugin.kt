package content.region.asgarnia.quest.hunt.handlers

import content.region.asgarnia.quest.hunt.PiratesTreasure
import core.api.setAttribute
import core.cache.def.impl.ItemDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DigAction
import core.game.global.action.DigSpadeHandler.register
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.HintIconManager
import core.game.node.item.GroundItemManager
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Plugin
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class PiratesTreasurePlugin : OptionHandler() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(org.rs.consts.Scenery.CHEST_2079).handlers["option:open"] = this
        ItemDefinition.forId(Items.PIRATE_MESSAGE_433).handlers["option:read"] = this
        ItemDefinition.forId(Items.CASKET_7956).handlers["option:open"] = this
        for (l in TreasureDigPlugin.LOCATIONS) {
            register(l, DIG_ACTION)
        }
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
        option: String,
    ): Boolean {
        val id = if (node is Scenery) node.getId() else node.id
        when (id) {
            2079 ->
                when (option) {
                    "open" -> player.packetDispatch.sendMessage("The chest is locked.")
                }

            7956 ->
                if (player.inventory.remove(PiratesTreasure.CASKET)) {
                    for (i in PiratesTreasure.CASKET_REWARDS) {
                        if (!player.inventory.add(i)) {
                            GroundItemManager.create(i, player)
                        }
                    }
                    player.packetDispatch.sendMessage("You open the casket, and find One-Eyed Hector's treasure.")
                }

            433 ->
                when (option) {
                    "read" -> {
                        player.interfaceManager.open(PiratesTreasure.MESSAGE_COMPONENT)
                        player.packetDispatch.sendString("Visit the city of the White Knights. In the park,", 222, 5)
                        player.packetDispatch.sendString("Saradomin points to the X which marks the spot.", 222, 6)
                        setAttribute(player, "/save:pirate-read", true)
                    }
                }
        }
        return true
    }

    class TreasureDigPlugin : DigAction {
        override fun run(player: Player?) {
            val quest = player!!.getQuestRepository().getQuest(Quests.PIRATES_TREASURE)
            player.lock(2)
            if (quest.getStage(player) == 20) {
                if (player.getSavedData().questData.isGardenerAttack) {
                    player.packetDispatch.sendMessage("You dig a hole in the ground...")
                    player.packetDispatch.sendMessage("and find a little chest of treasure.")
                    quest.finish(player)
                } else {
                    if (player.getAttribute("gardener-dug", false)) {
                        return
                    }
                    val npc = NPC.create(NPCs.GARDENER_1217, player.location.transform(0, 1, 0))
                    npc.setAttribute("target", player)
                    npc.isRespawn = false
                    npc.init()
                    npc.sendChat("First moles, now this! Take this, vandal!")
                    npc.properties.combatPulse.attack(player)
                    HintIconManager.registerHintIcon(player, npc)
                    setAttribute(player, "gardener-dug", true)
                }
            }
        }

        companion object {
            internal val LOCATIONS: Array<Location> = arrayOf(Location(2999, 3383, 0), Location.create(3000, 3383, 0))
        }
    }

    companion object {
        private val DIG_ACTION: DigAction = TreasureDigPlugin()
    }
}
