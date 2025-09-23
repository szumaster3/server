package content.region.asgarnia.portsarim.quest.hunt.plugin

import content.region.asgarnia.portsarim.quest.hunt.PiratesTreasure
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.GroundItemManager
import core.game.world.map.Location
import shared.consts.*

class PiratesTreasureListener : InteractionListener {

    companion object {
        private val DIG_LOCATIONS = arrayOf(
            Location(2999, 3383, 0),
            Location(3000, 3383, 0)
        )
    }

    override fun defineListeners() {

        /*
         * Handles opening chest upstairs in Blue Moon Inn.
         */

        on(Scenery.CHEST_2079, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "The chest is locked.")
            return@on true
        }

        /*
         * Handles read the pirate message.
         */

        on(Items.PIRATE_MESSAGE_433, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.BLANK_SCROLL_222)
            sendString(player, "Visit the city of the White Knights. In the park,", 222, 5)
            sendString(player, "Saradomin points to the X which marks the spot.", 222, 6)
            setAttribute(player, "/save:pirate-read", true)
            return@on true
        }

        /*
         * Handles opening reward casket.
         */

        on(Items.CASKET_7956, IntType.ITEM, "open") { player, node ->
            if (removeItem(player, node.asItem())) {
                for (reward in PiratesTreasure.CASKET_REWARDS) {
                    if (!addItem(player, reward.id)) {
                        GroundItemManager.create(reward, player)
                    }
                }
                sendMessage(player, "You open the casket, and find One-Eyed Hector's treasure.")
            }
            return@on true
        }

        /*
         * Handles dig at treasure locations.
         */

        DIG_LOCATIONS.forEach { location ->
            onDig(location) { player ->
                val quest = getQuest(player, Quests.PIRATES_TREASURE)
                if (quest.getStage(player) != 20) {
                    sendMessage(player, "You dig a hole in the ground...")
                    sendMessage(player, "But find nothing.")
                    return@onDig
                }

                if (getAttribute(player, "gardener-dug", false)) {
                    return@onDig
                }

                if (player.savedData.questData.isGardenerAttack) {
                    sendMessage(player, "You dig a hole in the ground...")
                    sendMessage(player, "and find a little chest of treasure.")
                    quest.finish(player)
                } else {
                    val npc = core.game.node.entity.npc.
                    NPC.create(NPCs.GARDENER_1217, player.location.transform(0, 1, 0))
                    npc.setAttribute("target", player)
                    npc.isRespawn = false
                    npc.init()
                    npc.sendChat("First moles, now this! Take this, vandal!")
                    npc.properties.combatPulse.attack(player)
                    setAttribute(player, "gardener-dug", true)
                    registerHintIcon(player, npc)
                }
            }
        }
    }
}
