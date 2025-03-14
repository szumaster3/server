package content.region.kandarin.handlers.ancient

import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Animator
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.*

class DragonForgeListener : InteractionListener {
    override fun defineListeners() {
        onUseWith(IntType.SCENERY, DRAGON_ANVIL, *RUINED_PIECES) { player, _, _ ->
            if (!hasRequirement(player, "While Guthix Sleeps")) return@onUseWith false
            if (getStatLevel(player, Skills.SMITHING) < 92) {
                sendMessage(player, "You need at least 92 smithing level to do this.")
                return@onUseWith false
            }

            if (!player.inventory.containsItem(FUSION_HAMMER)) {
                sendDialogue(player, "You need a hammer to work the metal with.")
                return@onUseWith false
            }

            if (!anyInInventory(player, *RUINED_PIECES)) {
                sendDialogue(player, "you do not have the required items.")
                return@onUseWith false
            }

            lock(player, 1000)
            lockInteractions(player, 1000)
            queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                when (stage) {
                    0 -> {
                        sendMessage(player, "You set to work repairing the ruined armour.")
                        animate(player, SMITHING_ANIMATION)
                        return@queueScript delayScript(player, SMITHING_ANIMATION.duration)
                    }

                    1 -> {
                        if (removeAll(player, RUINED_PIECES)) {
                            sendMessage(player, "You finish your efforts...")
                            removeItem(player, FUSION_HAMMER)
                            player.inventory.add(DRAGON_PLATEBODY)
                            rewardXP(player, Skills.SMITHING, 2000.0)
                        }
                        unlock(player)
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@onUseWith true
        }

        on(MITHRIL_DOOR, IntType.SCENERY, "open") { player, node ->
            if (!inInventory(player, Items.DRAGONKIN_KEY_14471) &&
                !hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)
            ) {
                sendDialogue(
                    player,
                    "The door is solid and resists all attempts to open it. There's no way past it at all, so best to ignore it.",
                )
                return@on true
            }
            sendMessage(player, "The door opens easily.")
            setAttribute(player, "dragon_head_room", true)
            when (node.id) {
                Scenery.MITHRIL_DOOR_25341 -> teleport(player, location(1823, 5273, 0))
                else -> teleport(player, location(1759, 5342, 1))
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.DRAGONKIN_KEY_14471, *MITHRIL_DOOR) { player, _, node ->
            if (!inInventory(player, Items.DRAGONKIN_KEY_14471) &&
                !hasRequirement(player, Quests.WHILE_GUTHIX_SLEEPS, false)
            ) {
                sendDialogue(
                    player,
                    "The door is solid and resists all attempts to open it. There's no way past it at all, so best to ignore it.",
                )
                return@onUseWith true
            }
            sendMessage(player, "The door opens easily.")
            when (node.id) {
                Scenery.MITHRIL_DOOR_25341 -> teleport(player, location(1823, 5273, 0))
                else -> teleport(player, location(1759, 5342, 1))
            }
            return@onUseWith true
        }

        onUseWith(IntType.NPC, STRANGE_KEYS, *MITHRIL_DRAGON_NPC) { player, _, with ->
            val npc = with.asNpc()
            if (player.inventory.containItems(*STRANGE_KEYS) &&
                player.equipment.containsAtLeastOneItem(REQUIRED_SHIELD)
            ) {
                if (!hasRequirement(player, "While Guthix Sleeps", false)) {
                    sendMessage(player, "You cannot currently use any items on that dragon.")
                    return@onUseWith true
                }
                face(npc, player)
                submitIndividualPulse(
                    player,
                    object : Pulse(1) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> {
                                    face(player, npc)
                                    visualize(npc!!, DRAGON_BREATH_ANIMATION, DRAGON_BREATH_GFX)
                                }

                                1 -> {
                                    visualize(player, DRAGON_SHIELD_ABSORB_ANIM, DRAGON_BREATH_ABSORB_GFX)
                                }

                                3 -> {
                                    player.inventory.removeAll(STRANGE_KEYS)
                                    sendItemDialogue(
                                        player,
                                        DRAGONKIN_KEY,
                                        "The intense heat of the mithril dragon's breath fuses the key halves together.",
                                    )
                                    player.inventory.add(DRAGONKIN_KEY)
                                }

                                6 -> {
                                    npc.attack(player)
                                    return true
                                }
                            }
                            return false
                        }
                    },
                )
            }
            return@onUseWith true
        }
    }

    companion object {
        private const val DRAGON_ANVIL = Scenery.ANVIL_40200
        private val FUSION_HAMMER = Item(Items.BLAST_FUSION_HAMMER_14478, 1)
        private val RUINED_PIECES =
            intArrayOf(
                Items.RUINED_DRAGON_ARMOUR_LUMP_14472,
                Items.RUINED_DRAGON_ARMOUR_SLICE_14474,
                Items.RUINED_DRAGON_ARMOUR_SHARD_14476,
            )
        private val DRAGON_PLATEBODY = Item(Items.DRAGON_PLATEBODY_14479, 1)
        private val REQUIRED_SHIELD =
            intArrayOf(Items.ANTI_DRAGON_SHIELD_1540, Items.DRAGONFIRE_SHIELD_11283, Items.DRAGONFIRE_SHIELD_11285)
        private val SMITHING_ANIMATION =
            Animation(Animations.HIT_WITH_BLAST_FUSION_HAMMER_10758, Animator.Priority.HIGH)
        private val MITHRIL_DOOR = intArrayOf(Scenery.MITHRIL_DOOR_25341, Scenery.MITHRIL_DOOR_40208)
        private val MITHRIL_DRAGON_NPC = intArrayOf(NPCs.MITHRIL_DRAGON_5363, NPCs.MITHRIL_DRAGON_8424)
        private val STRANGE_KEYS = intArrayOf(Items.STRANGE_KEY_LOOP_14469, Items.STRANGE_KEY_TEETH_14470)
        private val DRAGONKIN_KEY = Item(Items.DRAGONKIN_KEY_14471, 1)
        private val DRAGON_BREATH_ANIMATION = Animation(Animations.DRAGON_BREATH_81, Animator.Priority.HIGH)
        private val DRAGON_BREATH_GFX = Graphics(org.rs.consts.Graphics.DRAGON_FIRE_BREATH_DARKER_COLOR_953)
        private val DRAGON_BREATH_ABSORB_GFX = Graphics(org.rs.consts.Graphics.DFS_HIT_PROJECTILE_1165)
        private val DRAGON_SHIELD_ABSORB_ANIM = Animation(Animations.DEFEND_10663)
    }
}
