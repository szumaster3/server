package content.global.skill.smithing

import content.global.skill.smithing.smelting.Bar
import content.global.skill.smithing.special.DragonShieldPulse
import content.global.skill.smithing.special.DragonfireShieldPulse
import core.api.*
import core.api.quest.isQuestComplete
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class SmithingInteraction : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles the two shield halves combining at anvil to produce dragon square shield.
         */

        onUseWith(IntType.SCENERY, DRAGON, *ANVIL) { player, used, _ ->
            if (getDynLevel(player, Skills.SMITHING) < 60) {
                sendDialogue(player, "You need to have a Smithing level of at least 60 to do this.")
                return@onUseWith false
            }
            if (!inInventory(player, Items.HAMMER_2347, 1)) {
                sendDialogue(player, "You need a hammer to work the metal with.")
                return@onUseWith false
            }

            sendPlainDialogue(
                player,
                false,
                "You set to work trying to fix the ancient shield. It's seen some",
                "heavy reward and needs some serious work doing to it.",
            )
            addDialogueAction(player) { player, button ->
                if (button >= 3) {
                    lock(player, 5)
                    animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                    submitIndividualPulse(player, DragonShieldPulse(player, used.asItem()))
                }
                return@addDialogueAction
            }

            return@onUseWith true
        }

        /*
         * Handles the Anti-dragon shield fused with a draconic visage.
         */

        onUseWith(IntType.SCENERY, DRACONIC, *ANVIL) { player, used, _ ->
            if (!inInventory(player, Items.ANTI_DRAGON_SHIELD_1540)) {
                sendDialogue(player, "You need to have an anti-dragon-shield to attach the visage onto.")
                return@onUseWith false
            }
            if (!inInventory(player, Items.DRACONIC_VISAGE_11286)) {
                sendDialogue(player, "You need to have a draconic visage so it can be attached on a shield.")
                return@onUseWith false
            }
            if (getDynLevel(player, Skills.SMITHING) < 90) {
                sendDialogue(player, "You need to have a Smithing level of at least 90 to do this.")
                return@onUseWith false
            }
            if (!inInventory(player, Items.HAMMER_2347, 1)) {
                sendDialogue(player, "You need a hammer to work the metal with.")
                return@onUseWith false
            }
            sendPlainDialogue(
                player,
                false,
                "You set to work trying to fix the ancient shield. It's seen some",
                "heavy reward and needs some serious work doing to it.",
            )
            addDialogueAction(player) { player, button ->
                if (button >= 3) {
                    lock(player, 5)
                    animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
                    submitIndividualPulse(player, DragonfireShieldPulse(player, used.asItem()))
                    return@addDialogueAction
                }
            }
            return@onUseWith true
        }

        /*
         * Handles fuse godsword shards at an anvil to get godsword blade.
         */

        onUseWith(IntType.SCENERY, GODSWORD, *ANVIL) { player, used, _ ->
            if (!inInventory(player, Items.HAMMER_2347, 1)) {
                sendDialogue(player, "You need a hammer to work the metal with.")
                return@onUseWith false
            }
            if (getDynLevel(player, Skills.SMITHING) < 80) {
                sendDialogue(player, "You need to have a Smithing level of at least 80 to do this.")
                return@onUseWith false
            }
            player.dialogueInterpreter.open(62362, used.id)
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, HILT, *GODSWORD) { player, _, _ ->
            sendMessage(player, "The hilt of the godsword can only be connected to a completely reforged blade.")
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, GODSWORD, *GODSWORD) { player, _, _ ->
            sendMessage(player, "Those pieces of the godsword can't be joined together like that - try forging them.")
            return@onUseWith true
        }

        /*
         * Handles using the bars on anvil.
         */

        onUseWith(IntType.SCENERY, BARS, *ANVIL) { player, used, with ->
            if (!inInventory(player, Items.HAMMER_2347, 1)) {
                sendDialogue(player, "You need a hammer to work the metal with.")
                return@onUseWith false
            }
            if (!isQuestComplete(player, Quests.DORICS_QUEST) && with.asScenery().id == Scenery.ANVIL_2782) {
                sendDialogue(player, "Property of Doric the Dwarf.")
                return@onUseWith false
            }
            if (!isQuestComplete(player, Quests.THE_KNIGHTS_SWORD) && used.id == Items.BLURITE_BAR_9467) {
                sendDialogue(player, "You need complete the Knights' Sword quest to work the metal with.")
                return@onUseWith false
            }

            val bar = Bar.forId(used.asItem().id)
            var item = used.asItem()

            if (used.asItem().id == Items.HAMMER_2347) {
                for (i in player.inventory.toArray()) {
                    if (i == null) {
                        continue
                    }
                    val bar = Bar.forId(i.id)
                    if (bar != null) {
                        item = i
                        break
                    }
                }
            }
            if (bar == null) {
                return@onUseWith false
            }
            if (getStatLevel(player, Skills.SMITHING) < bar.level) {
                sendDialogue(
                    player,
                    "You need a smithing level of at least " + bar.level + " to work " +
                        getItemName(bar.product.id).lowercase() +
                        "s.",
                )
                return@onUseWith false
            }

            var builder = SmithingBuilder(item!!)
            builder.build(player)
            return@onUseWith true
        }
    }

    companion object {
        private val ANVIL =
            intArrayOf(
                Scenery.ANVIL_2782,
                Scenery.ANVIL_2783,
                Scenery.ANVIL_4306,
                Scenery.ANVIL_6150,
                Scenery.ANVIL_22725,
                Scenery.LATHE_26817,
                Scenery.ANVIL_26822,
                Scenery.ANVIL_37622,
            )
        private val BARS =
            intArrayOf(
                Items.BRONZE_BAR_2349,
                Items.IRON_BAR_2351,
                Items.STEEL_BAR_2353,
                Items.MITHRIL_BAR_2359,
                Items.ADAMANTITE_BAR_2361,
                Items.RUNITE_BAR_2363,
                Items.BLURITE_BAR_9467,
            )
        private val DRAGON = intArrayOf(Items.SHIELD_LEFT_HALF_2366, Items.SHIELD_RIGHT_HALF_2368)
        private val DRACONIC = intArrayOf(Items.DRACONIC_VISAGE_11286, Items.ANTI_DRAGON_SHIELD_1540)
        private val GODSWORD =
            intArrayOf(
                Items.GODSWORD_SHARDS_11692,
                Items.GODSWORD_SHARD_1_11710,
                Items.GODSWORD_SHARDS_11688,
                Items.GODSWORD_SHARD_2_11712,
                Items.GODSWORD_SHARDS_11686,
                Items.GODSWORD_SHARD_3_11714,
            )
        private val HILT =
            intArrayOf(
                Items.BANDOS_HILT_11704,
                Items.ARMADYL_HILT_11702,
                Items.ZAMORAK_HILT_11708,
                Items.SARADOMIN_HILT_11706,
            )
    }
}
