package content.region.misc.dialogue.tutorial

import content.region.misc.handlers.tutorial.TutorialStage
import core.api.*
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class MasterChefDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val bucket = Item(Items.BUCKET_1925, 1)
    val pot = Item(Items.EMPTY_POT_1931, 1)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            18 -> npc(FaceAnim.FRIENDLY, "Ah! Welcome, newcomer. I am the Master Chef, Lev. It", "is here I will teach you how to cook food truly fit for a", "king.")
            19, 20 -> npc(FaceAnim.HAPPY, "Hello again.")
            in 21..100 -> {
                setTitle(player!!, 3)
                sendDialogueOptions(player!!, title = "What would you like to hear more about?", "Making dough.", "Range cooking.", "Nothing, thanks.")
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (getAttribute(player, TutorialStage.TUTORIAL_STAGE, 0)) {
            18 -> when (stage) {
                0 -> npc(
                    "I already know how to cook. Brynna taught me just",
                    "now.",
                ).also {
                    stage++
                }

                1 -> npc(
                    FaceAnim.LAUGH, "Hahahahahaha! You call THAT cooking? Some shrimp",
                    "on an open log fire? Oh, no, no no. I am going to",
                    "teach you the fine art of cooking bread.",
                ).also {
                    stage++
                }

                2 -> npc(
                    "And no fine meal is complete without good music, so",
                    "we'll cover that while you're here too.",
                ).also {
                    stage++
                }

                3 -> {
                    Component.setUnclosable(
                        player,
                        interpreter.sendDoubleItemMessage(
                            Items.BUCKET_OF_WATER_1929,
                            Items.POT_OF_FLOUR_1933,
                            "The Cooking Guide gives you a <col=08088A>bucket of water<col> and a <col=08088A>pot of flour</col>.",
                        ),
                    )
                    addItem(player, Items.BUCKET_OF_WATER_1929)
                    addItem(player, Items.POT_OF_FLOUR_1933)
                    stage++
                }

                4 -> {
                    end()
                    setAttribute(player, TutorialStage.TUTORIAL_STAGE, 19)
                    TutorialStage.load(player, 19)
                }
            }

            19, 20 -> when (stage) {
                0 -> {
                    if (!player.inventory.containsItems(
                            Item(Items.BUCKET_OF_WATER_1929, 1),
                            Item(Items.POT_OF_FLOUR_1933, 1),
                        ) || player.inventory.containsItems(Item(Items.BUCKET_1925, 1), Item(Items.EMPTY_POT_1931, 1))
                    ) {
                        removeItem(player, Item(Items.EMPTY_POT_1931, 1), Container.INVENTORY)
                        removeItem(player, Item(Items.BUCKET_1925, 1), Container.INVENTORY)
                        sendDoubleItemDialogue(
                            player,
                            Items.BUCKET_OF_WATER_1929,
                            Items.POT_OF_FLOUR_1933,
                            "The Cooking Guide gives you a <col=08088A>bucket of water<col> and a <col=08088A>pot of flour</col>.",
                        )
                        addItem(player, Items.BUCKET_OF_WATER_1929, 1, Container.INVENTORY)
                        addItem(player, Items.POT_OF_FLOUR_1933, 1, Container.INVENTORY)
                        TutorialStage.load(player, 19)
                        return true
                    }
                    if (!player.inventory.containsItems(Item(Items.BUCKET_OF_WATER_1929, 1)) && removeItem(
                            player, Items.BUCKET_1925
                        )
                    ) {
                        sendItemDialogue(
                            player,
                            Items.BUCKET_OF_WATER_1929,
                            "The Master Chef gives you another bucket of water.",
                        )
                        addItem(player, Items.BUCKET_OF_WATER_1929, 1)
                        TutorialStage.load(player, 19)
                        return true
                    }
                    if (!player.inventory.containsItems(Item(Items.POT_OF_FLOUR_1933, 1)) && removeItem(
                            player, Item(Items.EMPTY_POT_1931)
                        )
                    ) {
                        Component.setUnclosable(
                            player,
                            interpreter.sendItemMessage(
                                Items.POT_OF_FLOUR_1933,
                                "The Master Chef gives you another pot of flour.",
                            ),
                        )
                        addItem(player, Items.POT_OF_FLOUR_1933, 1)
                        TutorialStage.load(player, 19)
                        return true
                    }
                    return false
                }
            }

            in 20..100 -> when (stage) {
                0 -> when (buttonId) {
                    1 -> sendNPCDialogue(
                        player!!,
                        npc!!.id,
                        "This is the base for many of the meals. To make dough we must mix flour and water. First, right click the bucket of water and select use, then left click on the pot of flour.",
                    ).also {
                        stage = 3
                    }

                    2 -> sendNPCDialogue(
                        player!!,
                        npc!!.id,
                        "To cook the dough, use it with the range shown by the arrow.",
                    ).also {
                        stage = 3
                    }

                    3 -> TutorialStage.rollback(player)
                }

                3 -> TutorialStage.rollback(player)
            }

        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MasterChefDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MASTER_CHEF_942)
}