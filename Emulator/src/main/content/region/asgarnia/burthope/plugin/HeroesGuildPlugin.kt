package content.region.asgarnia.burthope.plugin

import content.data.EnchantedJewellery
import content.data.EnchantedJewellery.Companion.idMap
import content.global.skill.summoning.familiar.Familiar
import core.api.animate
import core.api.lock
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.api.replaceSlot
import core.api.sendMessage
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class HeroesGuildPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles access to Heroes guild.
         */

        on(GUILD_GATE, IntType.SCENERY, "open") { player, node ->
            if (!hasRequirement(player, Quests.HEROES_QUEST, false)) {
                sendMessage(player, "You need to complete the Heroes' Quest.")
                return@on false
            }
            handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles recharge jewellery.
         */

        onUseWith(IntType.SCENERY, JEWELLERY, FOUNTAIN) { player, used, with ->
            rechargeJewellery(player, used.asItem(), with)
            return@onUseWith true
        }

        /*
         * Handles recharge jewellery by used it on familiar.
         */

        onUseWith(IntType.NPC, SPECIAL, NPCs.GEYSER_TITAN_7339, NPCs.GEYSER_TITAN_7340) { player, used, with ->
            rechargeJewellery(player, used, with)
            return@onUseWith true
        }
    }

    companion object {
        private val GUILD_GATE = intArrayOf(org.rs.consts.Scenery.DOOR_2624, org.rs.consts.Scenery.DOOR_2625)
        private val FOUNTAIN = org.rs.consts.Scenery.FOUNTAIN_OF_HEROES_36695
        private val SPECIAL = intArrayOf(*EnchantedJewellery.AMULET_OF_GLORY.ids, *EnchantedJewellery.AMULET_OF_GLORY_T.ids)
        private val JEWELLERY = intArrayOf(Items.RING_OF_SLAYING7_13282, Items.RING_OF_SLAYING6_13283, Items.RING_OF_SLAYING5_13284, Items.RING_OF_SLAYING4_13285, Items.RING_OF_SLAYING3_13286, Items.RING_OF_SLAYING2_13287, Items.RING_OF_SLAYING1_13288, Items.AMULET_OF_GLORY3_1710, Items.AMULET_OF_GLORY2_1708, Items.AMULET_OF_GLORY1_1706, Items.AMULET_OF_GLORY_1704, Items.AMULET_OF_GLORYT3_10356, Items.AMULET_OF_GLORYT2_10358, Items.AMULET_OF_GLORYT1_10360, Items.AMULET_OF_GLORYT_10362)
    }

    /**
     * Recharges jewellery using a fountain or familiar.
     */
    private fun rechargeJewellery(player: Player, n: Node, node: Node) {
        val usedItem = n as? Item ?: return
        val usedWith = (node as? Scenery) ?: (node as? Familiar ?: return)

        val jewellery = idMap[usedItem.id] ?: return
        if (!isQuestComplete(player, Quests.HEROES_QUEST)) return

        if (usedWith is Familiar) {
            if (!jewellery.canBeRechargedByFamiliar() || !player.familiarManager.isOwner(usedWith)) return
            usedWith.animate(Animation.create(7882))
        }

        lock(player, 1)
        animate(player, Animations.HUMAN_BURYING_BONES_827)
        val recharged = Item(jewellery.ids[0])
        replaceSlot(player, usedItem.slot, recharged)

        val name = jewellery.getJewelleryName(recharged).lowercase()
        val type = listOf("amulet", "bracelet", "ring", "necklace").find { it in name } ?: name

        when (usedWith) {
            is Scenery -> {
                if ("glory" in usedItem.name.lowercase()) {
                    player.dialogueInterpreter.sendItemMessage(
                        Items.AMULET_OF_GLORY_1704,
                        "You feel a power emanating from the fountain as it",
                        "recharges your amulet. You can now rub the amulet to",
                        "teleport and wear it to get more gems whilst mining."
                    )
                } else {
                    sendMessage(player, "You dip the $type in the fountain...")
                }
            }
            is Familiar -> sendMessage(player, "Your titan recharges the glory.")
        }
    }

    /**
     * Checks if the jewellery can be recharged by a familiar.
     */
    private fun EnchantedJewellery.canBeRechargedByFamiliar(): Boolean =
        this == EnchantedJewellery.AMULET_OF_GLORY || this == EnchantedJewellery.AMULET_OF_GLORY_T
}
