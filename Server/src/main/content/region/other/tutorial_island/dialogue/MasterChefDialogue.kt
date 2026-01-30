package content.region.other.tutorial_island.dialogue

import content.data.GameAttributes
import content.region.other.tutorial_island.plugin.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class MasterChefDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            18 -> npc(FaceAnim.FRIENDLY, "Ah! Welcome, newcomer. I am the Master Chef, Lev. It", "is here I will teach you how to cook food truly fit for a", "king.")
            19 -> npc(FaceAnim.HAPPY, "Hello again.")
            in 20..100 -> npc(FaceAnim.HALF_ASKING, "Do you need something?")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val player = player ?: return true
        val hasWater = player.inventory.containsItems(Item(Items.BUCKET_OF_WATER_1929, 1))
        val hasFlour = player.inventory.containsItems(Item(Items.POT_OF_FLOUR_1933, 1))

        when (getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)) {
            18 -> when (stage) {
                0 -> npc("I already know how to cook. Brynna taught me just", "now.").also { stage++ }
                1 -> npc(FaceAnim.LAUGH, "Hahahahahaha! You call THAT cooking? Some shrimp", "on an open log fire? Oh, no, no no. I am going to", "teach you the fine art of cooking bread.").also { stage++ }
                2 -> npc("And no fine meal is complete without good music, so", "we'll cover that while you're here too.").also { stage++ }
                3 -> {
                    sendDoubleItemDialogue(player,
                        Items.POT_OF_FLOUR_1933,
                        Items.BUCKET_OF_WATER_1929,
                        "The master chef gives you some <col=08088A>flour</col> and some <col=08088A>water</col>."
                    )

                    removeItem(player, Items.BUCKET_1925)
                    removeItem(player, Items.EMPTY_POT_1931)
                    addItem(player, Items.BUCKET_OF_WATER_1929)
                    addItem(player, Items.POT_OF_FLOUR_1933)

                    stage++
                }

                4 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 19)
                    TutorialStage.load(player, 19)
                }
            }

            19 -> {
                if (!hasWater || !hasFlour) {
                    sendDoubleItemDialogue(player,
                        Items.POT_OF_FLOUR_1933,
                        Items.BUCKET_OF_WATER_1929,
                        "The master chef gives you some <col=08088A>flour</col> and some <col=08088A>water</col>."
                    )
                    removeItem(player, Items.BUCKET_1925)
                    removeItem(player, Items.EMPTY_POT_1931)
                    addItem(player, Items.BUCKET_OF_WATER_1929)
                    addItem(player, Items.POT_OF_FLOUR_1933)
                    TutorialStage.load(player, 19)
                }
                return true
            }

            in 20..100 -> when (stage) {
                0 -> {
                    setTitle(player, 3)
                    sendOptions(
                        player,
                        title = "What would you like to hear more about?",
                        "Tell me about making dough again.",
                        "Tell me about range cooking again.",
                        "Nothing, thanks."
                    )
                    stage++
                }
                1 -> when (buttonId) {
                    1 -> player("Tell me about making dough again.").also { stage++ }
                    2 -> player("Tell me about range cooking again.").also { stage = 4 }
                    3 -> player("Nothing thanks.").also { stage = END_DIALOGUE }
                }

                2 -> npcl(FaceAnim.FRIENDLY, "It's quite simple. Just use a pot of flour on a bucket of water, or vice versa, and you'll make dough. You can fill a bucket with water at any sink.").also { stage++ }
                3 -> npc(FaceAnim.HALF_ASKING, "Do you need anything else?").also { stage = 0 }

                4 -> npcl(FaceAnim.FRIENDLY, "The range is the only place you can cook a lot of the more complex foods in ${GameWorld.settings?.name}. To cook on a range, all you need to do is click on it.").also { stage++ }
                5 -> npcl(FaceAnim.FRIENDLY, "You'll need to make sure you have the required items in your inventory though.").also { stage = 3 }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = MasterChefDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.MASTER_CHEF_942)
}