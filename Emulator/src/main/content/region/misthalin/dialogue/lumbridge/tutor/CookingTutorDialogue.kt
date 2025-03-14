package content.region.misthalin.dialogue.lumbridge.tutor

import core.api.sendDoubleItemDialogue
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CookingTutorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options(
            "Can you teach me the basics of cooking please?",
            "Tell me about different food I can make.",
            "Goodbye.",
        ).also {
            stage =
                0
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 ->
                        player(FaceAnim.HALF_GUILTY, "Can you teach me the basics of cooking please?").also {
                            stage =
                                10
                        }

                    2 -> player(FaceAnim.HALF_GUILTY, "Tell me about different food I can make.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Goodbye.").also { stage = END_DIALOGUE }
                }

            10 -> npc(FaceAnim.HALF_GUILTY, "The simplest thing to cook is raw meat or fish.").also { stage = 11 }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Fish can be caught, speak to the fishing tutor south of",
                    "here in the swamp. Killing cows or chickens will yield",
                    "raw meat to cook too.",
                ).also {
                    stage++
                }

            12 ->
                sendItemDialogue(
                    player,
                    Items.NULL_5090,
                    "When you have a full inventory of meat or fish, find a range. Look for this icon on your minimap.",
                ).also { stage++ }

            13 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "You can use your own fire... but it's not as effective",
                    "and you'll burn more. To build a fire use a tinderbox",
                    "on logs.",
                ).also {
                    stage++
                }

            14 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Once you've found your range, click on your raw",
                    "meat or fish in your inventory, then click on the",
                    "cooking range. This will bring up an interface which",
                    "you can right-click on to select the number to cook.",
                ).also {
                    stage++
                }

            15 ->
                sendItemDialogue(
                    player,
                    Items.NULL_5080,
                    "When you have a full inventory of cooked food, drop the useless burnt food and find a bank. Look for this symbol on your minimap after climbing the stairs of the Lumbridge Castle to the top. There are numerous banks around the world, all marked with that symbol.",
                ).also { stage++ }

            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "If you're interested in quests, I heard my friend the cook in Lumbridge Castle is in need of a hand. Just talk to him and he'll set you off.",
                ).also {
                    stage++
                }

            17 ->
                options(
                    "Can you teach me the basics of cooking please?",
                    "Tell me about different food I can make.",
                    "Goodbye.",
                ).also {
                    stage = 0
                }
            20 -> options("Fish and Meat", "Brewing", "Vegetables", "Pie", "Go back to teaching").also { stage = 21 }
            21 ->
                when (buttonId) {
                    1 ->
                        sendDoubleItemDialogue(
                            player,
                            Items.RAW_BEEF_2133,
                            Items.COOKED_MEAT_2142,
                            "Fish and meat of most varieties can be cooked very simply on either a fire or range, experiment which one works for you.",
                        ).also {
                            stage =
                                20
                        }

                    2 ->
                        sendDoubleItemDialogue(
                            player,
                            Items.DRAGON_BITTER_1911,
                            Items.ASGARNIAN_ALE_1905,
                            "You can brew your own beers using the fermenting vats in either Keldagrim or Port Phasmatys. Use two buckets of water, two handfuls of barley malt, 4 hops of your choice and a pot of ale yeast, then leave to",
                        ).also {
                            stage =
                                220
                        }

                    3 ->
                        sendDoubleItemDialogue(
                            player,
                            Items.POTATO_1942,
                            Items.MUSHROOM_POTATO_7058,
                            "Baked potatoes are excellent foods and they are healthy too! You'll need to churn some butter and cheese. Look for the churn icon on the mini map. There is a churn in the farm building northwest of Lumbridge.",
                        ).also {
                            stage =
                                230
                        }

                    4 ->
                        sendDoubleItemDialogue(
                            player,
                            Items.BUCKET_OF_WATER_1929,
                            Items.POT_OF_FLOUR_1933,
                            "Use a pot of flour with a bucket of water. You will then get an option to make bread dough, pitta bread dough, pastry dough, or pizza dough. Select pizza or pastry dough.",
                        ).also {
                            stage =
                                240
                        }

                    5 ->
                        options(
                            "Can you teach me the basics of cooking please?",
                            "Tell me about different food I can make.",
                            "Goodbye.",
                        ).also {
                            stage =
                                0
                        }
                }

            220 ->
                sendDoubleItemDialogue(
                    player,
                    Items.DRAGON_BITTER_1911,
                    Items.ASGARNIAN_ALE_1905,
                    "ferment for a few days!",
                ).also {
                    stage =
                        221
                }

            221 ->
                sendDoubleItemDialogue(
                    player,
                    Items.DRAGON_BITTER_1911,
                    Items.ASGARNIAN_ALE_1905,
                    "Simply turn the tap and let the ale flow into the barrel next to the vat, then use empty beer glasses on the barrel.",
                ).also {
                    stage =
                        20
                }

            230 ->
                sendDoubleItemDialogue(
                    player,
                    Items.PAT_OF_BUTTER_6697,
                    Items.CHEESE_1985,
                    "The nearest dairy cow is north of the nearby water mill. Take buckets of milk with you to the churn and use the milk on the churn to make butter or cheese.",
                ).also {
                    stage =
                        20
                }

            240 ->
                sendDoubleItemDialogue(
                    player,
                    Items.PIE_DISH_2313,
                    Items.PASTRY_DOUGH_1953,
                    "Use the pastry dough with a pie dish then add your filling such as apple or red berries.",
                ).also {
                    stage =
                        241
                }

            241 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Finally cook your pie by using the unbaked pie on a",
                    "cooking range. Mmmm...pie.",
                ).also {
                    stage =
                        242
                }

            242 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "There's pizza too! Find yourself some tomato and",
                    "cheese, use on the Pizza dough. Cook the pizza on a",
                    "range then add any other topping you want, such as",
                    "anchovies.",
                ).also {
                    stage =
                        20
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.COOKING_TUTOR_4899)
    }
}
