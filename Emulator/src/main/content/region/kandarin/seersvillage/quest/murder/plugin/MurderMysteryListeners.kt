package content.region.kandarin.seersvillage.quest.murder.plugin

import core.api.*
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class MurderMysteryListeners : InteractionListener {
    override fun defineListeners() {
        class PaperInSackDialogue : DialogueFile() {
            override fun handle(
                componentID: Int,
                buttonID: Int,
            ) {
                when (stage) {
                    0 -> sendDialogue(player!!, "There's some flypaper in there. Should I take it?").also { stage++ }
                    1 -> options("Yes, it might be useful.", "No, I don't see any need for it.").also { stage++ }
                    2 -> {
                        if (buttonID == 1) {
                            addItem(player!!, Items.FLYPAPER_1811, 1)
                            stage = 3
                        } else {
                            end()
                        }
                    }

                    3 -> sendDialogue(player!!, "You take a piece of flypaper. There is still plenty left. Should I take it?").also { stage = 1 }
                }
            }
        }

        on(Scenery.SMASHED_WINDOW_26111, IntType.SCENERY, "investigate") { player, _ ->
            val caseNumber = RandomFunction.random(0, 2)
            if (getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendMessage(player, "You need the guards' permission to do that.")
                return@on true
            }

            sendDialogue(player, "Some thread seems to have been caught on a loose nail on the window.")
            addSuspect(
                player,
                when (caseNumber) {
                    0 -> Items.CRIMINALS_THREAD_1808 to MurderMysteryUtils.ATTRIBUTE_ELIZABETH
                    1 -> Items.CRIMINALS_THREAD_1809 to MurderMysteryUtils.ATTRIBUTE_ANNA
                    2 -> Items.CRIMINALS_THREAD_1810 to MurderMysteryUtils.ATTRIBUTE_DAVID
                    else -> Items.CRIMINALS_THREAD_1808 to MurderMysteryUtils.ATTRIBUTE_ELIZABETH
                },
            )

            return@on true
        }


        val mansionObjects = mapOf(
            Scenery.ANNA_S_BARREL_2656 to Items.SILVER_NECKLACE_1796,
            Scenery.BOB_S_BARREL_2657 to Items.SILVER_CUP_1798,
            Scenery.CAROL_S_BARREL_2658 to Items.SILVER_BOTTLE_1800,
            Scenery.DAVID_S_BARREL_2659 to Items.SILVER_BOOK_1802,
            Scenery.ELIZABETH_S_BARREL_2660 to Items.SILVER_NEEDLE_1804,
            Scenery.FRANK_S_BARREL_2661 to Items.SILVER_POT_1806
        )

        on(MurderMysteryUtils.MANSION_OBJECTS, IntType.SCENERY, "search") { player, node ->
            if(getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendMessage(player, "You need the guards' permission to do that.")
                return@on true
            }

            mansionObjects[node.id]?.let { item ->
                val itemName = getItemName(item)
                if (!inInventory(player, item, 1)) {
                    sendDialogue(player, "There's something shiny hidden at the bottom. You take $itemName.")
                    addItem(player, item)
                } else {
                    sendMessage(player, "I already have $itemName.")
                }
            }

            return@on true
        }

        val investigationContent = mapOf(
            Scenery.SINCLAIR_FAMILY_CREST_2655 to "This crest appears to be part of the Sinclair family. I wonder if it is connected to the murder.",
            Scenery.SPIDERS_NEST_26109 to "You investigate the spider's nest. It seems it has not been used for a long time.",
            Scenery.SINCLAIR_FAMILY_BEEHIVE_26121 to "The hive is empty. There are a few dead bees and a faint smell of poison.",
            Scenery.SINCLAIR_FAMILY_COMPOST_HEAP_26120 to "You search through the compost heap. It smells foul and has a few pieces of rubbish in it.",
            Scenery.SINCLAIR_MANSION_DRAIN_2843 to "You peer into the drain but see nothing unusual.",
            Scenery.SINCLAIR_FAMILY_FOUNTAIN_2654 to "The fountain is filled with stagnant water. There seems to be something in there.",
        )

        on(MurderMysteryUtils.CRIME_SCENE_OBJECTS, IntType.SCENERY, "investigate") { player, node ->
            if(getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendMessage(player, "You need the guards' permission to do that.")
                return@on true
            }
            investigationContent[node.id]?.let { sendDialogue(player, it) }
            return@on true
        }

        val evidenceContent = mapOf(
            Items.PUNGENT_POT_1812 to "You sprinkle a small amount of flour on the strange smelling pot. The surface isn't shiny enough to take a fingerprint from.",
            Items.CRIMINALS_DAGGER_1813 to "You sprinkle a small amount of flour on the murder weapon. The murder weapon is now coated with a thin layer of flour.",
            Items.SILVER_NECKLACE_1796 to "You sprinkle the flour on Anna's necklace. The Necklace is now coated with a thin layer of flour.",
            Items.SILVER_CUP_1798 to "You sprinkle the flour on Bob's cup. The cup is now coated with a thin layer of flour.",
            Items.SILVER_BOTTLE_1800 to "You sprinkle the flour on Carol's bottle. The Bottle is now coated with a thin layer of flour.",
            Items.SILVER_BOOK_1802 to "You sprinkle the flour on David's book. The Book is now coated with a thin layer of flour.",
            Items.SILVER_NEEDLE_1804 to "You sprinkle the flour on Elizabeth's needle. The Needle is now coated with a thin layer of flour.",
            Items.SILVER_POT_1806 to "You sprinkle the flour on Frank's pot. The Pot is now coated with a thin layer of flour.",
        )

        onUseWith(IntType.ITEM, Items.POT_OF_FLOUR_1933, *MurderMysteryUtils.EVIDENCE_ITEMS) { player, used, with ->
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                evidenceContent[with.id]?.let {
                    sendMessage(player, it)
                    addItem(player, with.id + 1, 1)
                }
                addItemOrDrop(player, Items.EMPTY_POT_1931, 1)
            }
            return@onUseWith true
        }

        val impressionContent = mapOf(
            Items.SILVER_NECKLACE_1797 to "You have a clean impression of Anna's fingerprints.",
            Items.SILVER_CUP_1799 to "You have a clean impression of Bob's fingerprints.",
            Items.SILVER_BOTTLE_1801 to "You have a clean impression of Carol's fingerprints.",
            Items.SILVER_BOOK_1803 to "You have a clean impression of David's fingerprints.",
            Items.SILVER_NEEDLE_1805 to "You have a clean impression of Elizabeth's fingerprints.",
            Items.SILVER_POT_1807 to "You have a clean impression of Frank's fingerprints."
        )

        onUseWith(IntType.ITEM, MurderMysteryUtils.EVIDENCE_ITEMS_2, Items.FLYPAPER_1811) { player, used, with ->
            if (removeItem(player, used.asItem())) {
                impressionContent[used.id]?.let {
                    sendMessage(player, it)
                    addItem(player, Items.UNKNOWN_PRINT_1822, 1)
                }
            }
            return@onUseWith true
        }

        onUseWith(IntType.ITEM, MurderMysteryUtils.GUILTY_NPC_PRINT_ITEMS, Items.UNKNOWN_PRINT_1822,) { player, used, _ ->
            val guiltyPerson = MurderMysteryUtils.getGuiltyPerson(player)
            if (removeItem(player, used.asItem())) {
                sendMessage(player, guiltyPerson?.let {
                    "You look closely at the fingerprint and find it's a match for $it." }
                    ?: "You find no match for the fingerprint."
                )
            }
            return@onUseWith true
        }

        on(intArrayOf(Scenery.STURDY_WOODEN_GATE_2664, Scenery.STURDY_WOODEN_GATE_2665), IntType.SCENERY, "investigate",) { player, _ ->
            if(getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendMessage(player, "You need the guards' permission to do that.")
            } else {
                sendDialogue(player, "As you approach the gate the Guard Dog starts barking loudly at you. It must have been someone the dog knew.")
                sendChat(findNPC(NPCs.SINCLAIR_GUARD_DOG_821)!!, "BARK")
            }
            return@on true
        }

        on(intArrayOf(Scenery.SMASHED_WINDOW_26110,Scenery.SMASHED_WINDOW_26111), IntType.SCENERY, "break") { player, _ ->
            if(getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendMessage(player, "You need the guards' permission to do that.")
            } else {
                sendDialogue(player, "You don't want to damage evidence!")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.SILVER_POT_1806, Items.SILVER_POT_1807, Items.PUNGENT_POT_1812, Scenery.BARREL_OF_FLOUR_26122) { player, _, _ ->
            sendDialogue(player, "You probably shouldn't use evidence from a crime scene to keep flour in...")
            return@onUseWith true
        }

        on(Scenery.SACKS_2663, IntType.SCENERY, "investigate") { player, _ ->
            openDialogue(player, PaperInSackDialogue())
            return@on true
        }

        on(Items.PUNGENT_POT_1812, IntType.GROUND_ITEM, "take") { player, node ->
            if(getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendDialogue(player, "You need the guards' permission to do that.")
                return@on true
            }
            if(inInventory(player, node.id)) {
                sendMessage(player, "I already have the poisoned pot.")
                return@on true
            }
            sendDialogueLines(player, "It seems like Lord Sinclair was drinking from this before he died.")
            addItem(player, node.id, 1)
            return@on true
        }

        on(Items.CRIMINALS_DAGGER_1813, IntType.GROUND_ITEM, "take") { player, node ->
            if(getQuestStage(player, Quests.MURDER_MYSTERY) == 0) {
                sendDialogue(player, "You need the guards' permission to do that.")
                return@on true
            }
            if(inInventory(player, node.id)) {
                sendMessage(player, "I already have the murder weapon.")
                return@on true
            }
            sendDialogueLines(player, "This knife doesn't seem sturdy enough to have killed Lord Sinclair.")
            addItem(player, node.id, 1)
            return@on true
        }
    }

    private fun addSuspect(
        player: Player,
        suspect: Pair<Int, String>,
    ) {
        val (item, attribute) = suspect
        val hasThread = hasAnItem(player, Items.CRIMINALS_THREAD_1808, Items.CRIMINALS_THREAD_1809, Items.CRIMINALS_THREAD_1810).container != null
        if(!getAttribute(player, attribute.any().toString(), false) && !hasThread) {
            MurderMysteryUtils.initialSuspects(player)
            addItem(player, item)
        } else if(getAttribute(player, attribute.any().toString(), false) && !hasThread) {
            sendDialogue(player, "Lucky for you there's some thread left. You should be less careless in future.")
            addItem(player, item)
        } else {
            sendDialogue(player, "You have already taken the thread.")
        }
    }
}
