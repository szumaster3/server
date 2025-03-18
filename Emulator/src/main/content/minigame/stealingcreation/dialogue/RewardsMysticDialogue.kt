package content.minigame.stealingcreation.dialogue

import core.api.openInterface
import core.api.sendDialogueOptions
import core.api.setTitle
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class RewardsMysticDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (!GameWorld.settings!!.isMembers) {
                    npc(
                        FaceAnim.THINKING,
                        "Only in the sancity of a members' world will I risk",
                        "discourse with those tainted by wordly desires.",
                    ).also {
                        stage++
                    }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "If you've worked hard towards your cause, perhaps you'd be interested in some rewards?",
                    ).also {
                        stage +=
                            3
                    }
                }
            1 ->
                player(
                    FaceAnim.HALF_THINKING,
                    "Er... I need to be in a members' world to speak to",
                    " you?",
                ).also { stage++ }
            2 ->
                npc(FaceAnim.HAPPY, "Well done! A fast learner, that's what I like to see.", "Goodbye.").also {
                    stage =
                        END_DIALOGUE
                }
            3 -> {
                setTitle(player, 3)
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "Show me the rewards, please.",
                    "Tell me about the rewards.",
                    "Never mind.",
                ).also {
                    stage++
                }
            }
            4 ->
                when (buttonId) {
                    1 ->
                        player(FaceAnim.HAPPY, "Show me the rewards, please.").also {
                            end()
                            openInterface(player, Components.SC_REWARD_SHOP_811)
                        }

                    2 -> player("Tell me about the rewards.").also { stage++ }
                    3 -> end()
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "By helping your team gather sacred clay, you earn points that you can spend on rewards here.",
                ).also {
                    stage++
                }
            6 -> npcl(FaceAnim.FRIENDLY, "There are two types of reward: tools and equipment. ").also { stage++ }
            7 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "All the rewards are made from the same sacred clay that your team works so hard to harvest, which gives them some rather special properties.",
                ).also {
                    stage++
                }
            8 -> {
                setTitle(player, 4)
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "Tell me more about tool rewards.",
                    "Tell me more about equipment rewards.",
                    "Show me the rewards, please.",
                    "Thanks.",
                ).also {
                    stage++
                }
            }
            9 ->
                when (buttonId) {
                    1 -> player(FaceAnim.NEUTRAL, "Tell me more about tool rewards.").also { stage++ }
                    2 -> player(FaceAnim.NEUTRAL, "Tell me more about equipment rewards.").also { stage = 14 }
                    3 ->
                        player(FaceAnim.HAPPY, "Show me the rewards, please.").also {
                            end()
                            openInterface(player, Components.SC_REWARD_SHOP_811)
                        }

                    4 -> player("Thanks.").also { stage = END_DIALOGUE }
                }
            10 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "These hallowed tools are made from sacred clay, and can be transformed into a pickaxe, woodcutting hatchet, harpoon, butterfly net, fletching knife, hammer or needle.",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The morphic tool allows you to decide which tool it will transform into. As you use the tool, you'll get extra experience in that skill.",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The volatile tool is made of more powerful clay, but is more unstable, so although you get more bonus experience, you have no control over which tool it becomes.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Eventually, either type of tool will run out of sacred power and stop working. You can always come back here and buy more charges for it with points from the primordial realm.",
                ).also {
                    stage =
                        8
                }
            14 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Ah, now these rewards truly reveal the power of the sacred clay. The morphic armour and weapon can adapt to your fighting style.",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "When you operate a piece of the morphic armour and choose a new combat style, all currently equipped morphic armour and weapons will change to that style of combat.",
                ).also {
                    stage++
                }
            16 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "While you are using the equipment, you will gain extra experience in whichever combat skills you are training.",
                ).also {
                    stage++
                }
            17 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Eventually, the armour will run out of sacred energy, but you can always come back and earn more charges for it.",
                ).also {
                    stage =
                        8
                }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.REWARDS_MYSTIC_8228)
}
