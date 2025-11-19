package content.region.kandarin.plugin.ancient_cavern

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Animator
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.*

class DragonForgePlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles smithing ruined pieces.
         */

        onUseWith(IntType.SCENERY, DRAGON_ANVIL, *RUINED_PIECES) { player, _, _ ->
            if (!hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS)) return@onUseWith false

            if (getStatLevel(player, Skills.SMITHING) < 92) {
                sendMessage(player, "You need at least 92 smithing level to do this.")
                return@onUseWith false
            }

            if (!player.inventory.containsItem(FUSION_HAMMER)) {
                sendMessage(player, "You are missing some required items or your hammer.")
                return@onUseWith false
            }

            if (!allInInventory(player, *RUINED_PIECES)) {
                sendMessage(player, "You do not have the required items.")
                return@onUseWith false
            }

            val removedPieces = removeAll(player, RUINED_PIECES)
            val removedHammer = removeItem(player, FUSION_HAMMER)
            if (removedPieces && removedHammer) {
                lock(player, SMITHING_ANIMATION.duration + 3)
                player.animate(SMITHING_ANIMATION)
                sendMessage(player, "You set to work repairing the ruined armour.")
                queueScript(player, SMITHING_ANIMATION.duration, QueueStrength.SOFT) {
                    addItem(player, DRAGON_PLATEBODY.id, 1)
                    rewardXP(player, Skills.SMITHING, 2000.0)
                    sendMessage(player, "You finish your efforts and forge the Dragon Platebody!")
                    return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles opening the mithril doors.
         */

        on(MITHRIL_DOOR, IntType.SCENERY, "open") { player, node ->
            if (!inInventory(player, Items.DRAGONKIN_KEY_14471) && !hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)) {
                sendDialogue(player, "The door is solid and resists all attempts to open it. There's no way past it at all, so best to ignore it.")
            } else {
                sendMessage(player, "The door opens easily.")
                setAttribute(player, "dragon_head_room", true)
                val destination = if (node.id == Scenery.MITHRIL_DOOR_25341) location(1823, 5273, 0) else location(1759, 5342, 1)
                teleport(player, destination)
            }
            return@on true
        }

        /*
         * Handles using dragonkin key on the mithril doors.
         */

        onUseWith(IntType.SCENERY, Items.DRAGONKIN_KEY_14471, *MITHRIL_DOOR) { player, _, node ->
            if (!inInventory(player, Items.DRAGONKIN_KEY_14471) && !hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)) {
                sendDialogue(player, "The door is solid and resists all attempts to open it. There's no way past it at all, so best to ignore it.")
            } else {
                sendMessage(player, "The door opens easily.")
                val destination = if (node.id == Scenery.MITHRIL_DOOR_25341) location(1823, 5273, 0) else location(1759, 5342, 1)
                teleport(player, destination)
            }
            return@onUseWith true
        }

        /*
         * Handles creating dragonkin key.
         */

        onUseWith(IntType.NPC, STRANGE_KEYS, *MITHRIL_DRAGON_NPC) { player, _, with ->
            val npc = with.asNpc()

            if (!hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)) {
                sendMessage(player, "You cannot currently use items on this dragon yet.")
                return@onUseWith false
            }

            if (!anyInEquipment(player, *REQUIRED_SHIELD)) {
                sendMessage(player, "You need an appropriate shield to approach the dragon safely.")
                return@onUseWith false
            }

            if (!anyInInventory(player, *STRANGE_KEYS)) {
                sendMessage(player, "You have no keys to use on the dragon.")
                return@onUseWith false
            }

            if (removeAll(player, STRANGE_KEYS, Container.INVENTORY)) {
                visualize(npc, DRAGON_BREATH_ANIMATION, DRAGON_BREATH_GFX)
                sendItemDialogue(player, DRAGONKIN_KEY, "The intense heat of the mithril dragon's breath fuses the key halves together.")
                if (!addItem(player, DRAGONKIN_KEY.id, 1)) {
                    produceGroundItem(player, DRAGONKIN_KEY.id, 1, player.location)
                }
                runTask(player, 3) {
                    if (!npc.isActive) {
                        npc.attack(player)
                    }
                }
            }

            return@onUseWith true
        }
    }

    companion object {
        private const val DRAGON_ANVIL = Scenery.ANVIL_40200
        private val FUSION_HAMMER = Item(Items.BLAST_FUSION_HAMMER_14478, 1)
        private val RUINED_PIECES = intArrayOf(Items.RUINED_DRAGON_ARMOUR_LUMP_14472, Items.RUINED_DRAGON_ARMOUR_SLICE_14474, Items.RUINED_DRAGON_ARMOUR_SHARD_14476)
        private val DRAGON_PLATEBODY = Item(Items.DRAGON_PLATEBODY_14479, 1)
        private val REQUIRED_SHIELD = intArrayOf(Items.ANTI_DRAGON_SHIELD_1540, Items.DRAGONFIRE_SHIELD_11283, Items.DRAGONFIRE_SHIELD_11285)
        private val SMITHING_ANIMATION = Animation(Animations.HIT_WITH_BLAST_FUSION_HAMMER_10758, Animator.Priority.HIGH)
        private val MITHRIL_DOOR = intArrayOf(Scenery.MITHRIL_DOOR_25341, Scenery.MITHRIL_DOOR_40208)
        private val MITHRIL_DRAGON_NPC = intArrayOf(NPCs.MITHRIL_DRAGON_5363, NPCs.MITHRIL_DRAGON_8424)
        private val STRANGE_KEYS = intArrayOf(Items.STRANGE_KEY_LOOP_14469, Items.STRANGE_KEY_TEETH_14470)
        private val DRAGONKIN_KEY = Item(Items.DRAGONKIN_KEY_14471, 1)
        private val DRAGON_BREATH_ANIMATION = Animation(81, Animator.Priority.HIGH)
        private val DRAGON_BREATH_GFX = Graphics(shared.consts.Graphics.DRAGON_FIRE_BREATH_DARKER_COLOR_953)
    }
}
