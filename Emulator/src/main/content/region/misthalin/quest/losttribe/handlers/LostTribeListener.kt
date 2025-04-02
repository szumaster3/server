package content.region.misthalin.quest.losttribe.handlers

import content.data.GameAttributes
import content.data.items.SkillingTool
import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class LostTribeListener : InteractionListener {
    override fun defineListeners() {
        on(CHEST, IntType.SCENERY, "open") { player, _ ->
            if (getQuestStage(player, Quests.THE_LOST_TRIBE) == 47 && inInventory(player, Items.KEY_423, 1)) {
                removeItem(player, Item(Items.KEY_423))
                for (item in arrayOf(Items.HAM_ROBE_4300, Items.HAM_SHIRT_4298, Items.HAM_HOOD_4302).map { Item(it) }) {
                    if (!player.inventory.add(item)) {
                        GroundItemManager.create(item, player)
                    }
                }
                setQuestStage(player, Quests.THE_LOST_TRIBE, 48)
            } else {
                sendMessage(player, "This chest requires a key.")
            }
            return@on true
        }

        on(SIGMUND, IntType.NPC, "pickpocket") { player, _ ->
            player.lock()
            GameWorld.Pulser.submit(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(Animation(Animations.PICK_POCKET_881))
                            3 -> {
                                if (getQuestStage(player, Quests.THE_LOST_TRIBE) == 47 &&
                                    !inInventory(
                                        player,
                                        Items.KEY_423,
                                    )
                                ) {
                                    addItemOrDrop(player, Items.KEY_423)
                                    sendItemDialogue(player, Items.KEY_423, "You find a small key on Sigmund.")
                                } else {
                                    sendNPCDialogue(player, SIGMUND, "What do you think you're doing?!", FaceAnim.ANGRY)
                                }
                                player.unlock()
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(BROOCH, IntType.ITEM, "look-at") { player, _ ->
            openInterface(player, Components.BROOCH_VIEW_50)
            return@on true
        }

        on(GOBLIN_BOOK, IntType.ITEM, "read") { player, _ ->
            openInterface(player, Components.GOBLIN_SYMBOL_BOOK_183)
            return@on true
        }

        on(BOOKCASE, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the bookcase...")
            val hasAnBook = hasAnItem(player, Items.GOBLIN_SYMBOL_BOOK_5009).container != null
            if (!hasAnBook && getQuestStage(player, Quests.THE_LOST_TRIBE) >= 41) {
                sendDialogue(player, "'A History of the Goblin Race.' This must be it.")
                addItemOrDrop(player, Items.GOBLIN_SYMBOL_BOOK_5009)
            } else {
                sendMessage(player, "You don't find anything that you'd ever want to read.")
            }
            return@on true
        }

        on(CRATE, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the bookcase...")
            if (!inInventory(player, Items.SILVERWARE_5011) && getQuestStage(player, Quests.THE_LOST_TRIBE) == 48) {
                sendItemDialogue(player, Items.SILVERWARE_5011, "You find the missing silverware!")
                addItemOrDrop(player, Items.SILVERWARE_5011)
                setQuestStage(player, Quests.THE_LOST_TRIBE, 49)
            } else {
                sendMessage(player, "You find nothing.")
            }
            return@on true
        }

        on(GOBLIN_FOLLOWERS, IntType.NPC, "follow") { player, node ->
            if (node.asNpc().id == NPCs.MISTAG_2084) {
                GoblinFollower.sendToLumbridge(player)
            } else {
                GoblinFollower.sendToMines(player)
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, pickaxeIDs, *rubbleIDs) { player, used, _ ->
            if (getQuestStage(player, Quests.THE_LOST_TRIBE) < 30) {
                sendItemDialogue(
                    player,
                    used.id,
                    "I should probably figure out what happened, before vandalizing the castle more.",
                )
                return@onUseWith false
            }

            val tool = SkillingTool.getPickaxe(player)
            if (tool == null) {
                sendDialogue(player, "You don't have a pick which you have the level to use.")
                return@onUseWith false
            }

            if (getStatLevel(player, Skills.MINING) < 13) {
                sendDialogue(player, "You need 13 mining to break through.")
                return@onUseWith false
            }

            player.lock()
            GameWorld.Pulser.submit(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> {
                                player.walkingQueue.reset()
                                player.walkingQueue.addPath(3219, 9618, true)
                            }

                            1 -> {
                                player.animator.animate(Animation(tool.animation))
                                delay = animationDuration(Animation(tool.animation))
                            }

                            2 -> {
                                sendItemDialogue(player, tool.id, "You dig a narrow tunnel through the rocks.")
                                setAttribute(player, GameAttributes.QUEST_LOST_TRIBE_LUMBRIDGE_CELLAR, true)
                                setVarbit(player, Vars.VARBIT_QUEST_LOST_TRIBE_CAVE_HOLE_STATUS_532, 4, true)
                                unlock(player)
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@onUseWith true
        }
    }

    companion object {
        val GOBLIN_FOLLOWERS = intArrayOf(NPCs.MISTAG_2084, NPCs.KAZGAR_2086)
        private const val GOBLIN_BOOK = Items.GOBLIN_SYMBOL_BOOK_5009
        private const val BROOCH = Items.BROOCH_5008
        private const val CRATE = Scenery.CRATE_6911
        private const val BOOKCASE = Scenery.BOOKCASE_6916
        private const val CHEST = Scenery.CHEST_6910
        private const val SIGMUND = NPCs.SIGMUND_2082
        private val pickaxeIDs =
            intArrayOf(
                Items.BRONZE_PICKAXE_1265,
                Items.IRON_PICKAXE_1267,
                Items.STEEL_PICKAXE_1269,
                Items.MITHRIL_PICKAXE_1273,
                Items.ADAMANT_PICKAXE_1271,
                Items.RUNE_PICKAXE_1275,
            )
        private val rubbleIDs = intArrayOf(6898, Scenery.RUBBLE_6903, Scenery.SHELVES_6904, Scenery.HOLE_6905)
    }
}
