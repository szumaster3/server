package content.region.other.tutorial_island.dialogue

import content.data.GameAttributes
import content.region.other.tutorial_island.plugin.TutorialStage
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class SurvivalExpertDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val player = player ?: return true
        val tutStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)

        when (tutStage) {
            4 -> npc(FaceAnim.FRIENDLY,
                "Hello there, newcomer. My name is Brynna. My job is",
                "to teach you a few survival tips and tricks. First off",
                "we're going to start with the most basic survival skill of",
                "all: making a fire."
            )
            5, 14, 15 -> {
                val hasAxe = inInventory(player, Items.BRONZE_AXE_1351)
                val hasTinderbox = inInventory(player, Items.TINDERBOX_590)

                if (!hasAxe) {
                    sendItemDialogue(player, Items.BRONZE_AXE_1351, "The Survival Expert gives you a spare bronze axe.")
                    addItem(player, Items.BRONZE_AXE_1351)
                }

                if (!hasTinderbox) {
                    sendItemDialogue(player, Items.TINDERBOX_590, "The Survival Expert gives you a spare tinderbox.")
                    addItem(player, Items.TINDERBOX_590)
                }

                npc(FaceAnim.NEUTRAL, "Light the logs in your backpack to make a fire.")
                stage = END_DIALOGUE
            }

            8 -> npcl(FaceAnim.FRIENDLY, "Light the logs in your backpack to make a fire.").also { stage = END_DIALOGUE }
            11 -> npc(FaceAnim.HAPPY,
                "Well done! Next we need to get some food in our",
                "bellies. We'll need something to cook. There are shrimp",
                "in the pond there, so let's catch and cook some."
            )
            12 -> {
                if (!inInventory(player, Items.SMALL_FISHING_NET_303)) {
                    sendItemDialogue(player, Items.SMALL_FISHING_NET_303, "The Survival Guide gives you a <col=08088A>net</col>!")
                    addItem(player, Items.SMALL_FISHING_NET_303)
                } else {
                    npc(
                        FaceAnim.HAPPY,
                        "Well done! Next we need to get some food in our",
                        "bellies. We'll need something to cook. There are shrimp",
                        "in the pond there, so let's catch and cook some."
                    )
                }
                stage = END_DIALOGUE
            }

            16 -> npc(FaceAnim.HALF_ASKING, "Hello again. Is there something you'd like to hear more about?")

            else -> sendDialogue(player, "You should complete your objective before talking to Brynna.")
                .also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val player = player ?: return true
        val tutStage = getAttribute(player, GameAttributes.TUTORIAL_STAGE, 0)

        when (tutStage) {
            4 -> when (stage) {
                0 -> {
                    sendDoubleItemDialogue(player, Items.TINDERBOX_590, Items.BRONZE_AXE_1351, "The Survival Guide gives you a <col=08088A>tinderbox</col> and a <col=08088A>bronze axe</col>!")
                    addItem(player, Items.TINDERBOX_590)
                    addItem(player, Items.BRONZE_AXE_1351)
                    stage++
                }

                1 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 5)
                    TutorialStage.load(player, 5)
                }
            }

            11 -> when (stage) {
                0 -> {
                    sendItemDialogue(player, Items.SMALL_FISHING_NET_303, "The Survival Guide gives you a <col=08088A>net</col>!")
                    addItem(player, Items.SMALL_FISHING_NET_303)
                    stage++
                }

                1 -> {
                    end()
                    setAttribute(player, GameAttributes.TUTORIAL_STAGE, 12)
                    TutorialStage.load(player, 12)
                }
            }

            16 ->  when (stage) {
                0 -> {
                    setTitle(player, 5)
                    sendOptions(
                        player,
                        title = "What would you like to hear more about?",
                        "Tell me about my skills again.",
                        "Tell me about Woodcutting again.",
                        "Tell me about Firemaking again.",
                        "Tell me about Fishing again.",
                        "Tell me about Cooking again."
                    )
                    stage++
                }
                1 -> when (buttonId) {
                    1 -> player("Tell me about my skills again.").also { stage++ }
                    2 -> player("Tell me about Woodcutting again.").also { stage = 6 }
                    3 -> player("Tell me about Firemaking again.").also { stage = 9 }
                    4 -> player("Tell me about Fishing again.").also { stage = 12 }
                    5 -> player("Tell me about Cooking again.").also { stage = 16 }
                }

                2 -> npcl(FaceAnim.FRIENDLY, "Every skill is listed in the skills menu. Here you can see what your current levels are and how much experience you have.").also { stage++ }
                3 -> npcl(FaceAnim.FRIENDLY, "As you move your mouse over the various skills the small yellow popup box will show you the exact amount of experience you have and how much is needed to get to the next level.").also { stage++ }
                4 -> npcl(FaceAnim.FRIENDLY, "You can also click on a skill to open up the relevant skillguide. In the skillguide, you can see all of the unlocks available in that skill.").also { stage++ }

                5 -> npcl(FaceAnim.FRIENDLY, "Is there anything else you'd like to hear more about?").also { stage = 0 }

                6 -> npcl(FaceAnim.FRIENDLY, "Woodcutting, eh? Don't worry, newcomer, it's really very easy. Simply equip your axe and click on a nearby tree to chop away.").also { stage++ }
                7 -> npcl(FaceAnim.FRIENDLY, "As you explore the mainland you will discover many different kinds of trees that will require different Woodcutting levels to chop down.").also { stage++ }
                8 -> npcl(FaceAnim.FRIENDLY, "Logs are not only useful for making fires. Many archers use the skill known as Fletching to craft their own bows and arrows from trees.").also { stage = 5 }

                9  -> npcl(FaceAnim.FRIENDLY, "Certainly, newcomer. When you have logs simply use your tinderbox on them. If successful, you will start a fire.").also { stage++ }
                10 -> npcl(FaceAnim.FRIENDLY, "You can also set fire to logs you find lying on the floor already, and some other things can also be set alight...").also { stage++ }
                11 -> npcl(FaceAnim.FRIENDLY, "A tinderbox is always a useful item to keep around!").also { stage = 5 }

                12 -> npcl(FaceAnim.FRIENDLY, "Ah, yes. Fishing! Fishing is undoubtedly one of the more popular hobbies here in Gielinor!").also { stage++ }
                13 -> npcl(FaceAnim.FRIENDLY, "Whenever you see sparkling waters, you can be sure there's probably some good fishing to be had there!").also { stage++ }
                14 -> npcl(FaceAnim.FRIENDLY, "Not only are fish absolutely delicious when cooked, they will also heal lost health.").also { stage++ }
                15 -> npcl(FaceAnim.FRIENDLY, "I would recommend everybody has a go at Fishing at least once in their lives!").also { stage = 5 }

                16 -> npcl(FaceAnim.FRIENDLY, "Yes, the most basic of survival techniques. Most simple foods can be used on a fire to cook them. If you're feeling a bit fancier, you can also use a range to cook the food instead.").also { stage++ }
                17 -> npcl(FaceAnim.FRIENDLY, "Eating cooked food will restore lost health. The harder something is to cook, the more it will heal you.").also { stage = 5 }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SurvivalExpertDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.SURVIVAL_EXPERT_943)
}