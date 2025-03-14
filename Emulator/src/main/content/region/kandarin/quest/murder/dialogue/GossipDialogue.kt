package content.region.kandarin.quest.murder.dialogue

import core.api.quest.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GossipDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.MURDER_MYSTERY) in 1..100) {
            playerl(FaceAnim.FRIENDLY, "I'm investigating the murder up at the Sinclair place.")
        } else {
            npcl(
                FaceAnim.FRIENDLY,
                "There's some kind of commotion up at the Sinclair place I hear. Not surprising all things considered.",
            ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val questStage = getQuestStage(player!!, Quests.MURDER_MYSTERY)
        when (questStage) {
            in 1..4 ->
                when (stage) {
                    0 -> npcl(FaceAnim.FRIENDLY, "Murder is it? Well, I'm not really surprised...").also { stage = 2 }
                    2 -> playerl(FaceAnim.FRIENDLY, "What can you tell me about the Sinclairs?").also { stage++ }
                    3 -> npcl(FaceAnim.FRIENDLY, "Well, what do you want to know?").also { stage++ }
                    4 ->
                        options(
                            "Tell me about Lord Sinclair.",
                            "Why do the Sinclairs live so far from town?",
                            "What can you tell me about his sons?",
                            "What can you tell me about his daughters?",
                            "Who do you think was responsible?",
                        ).also { stage++ }

                    5 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Tell me about Lord Sinclair.").also { stage++ }
                            2 ->
                                playerl(FaceAnim.FRIENDLY, "Why do the Sinclairs live so far from town?").also {
                                    stage =
                                        9
                                }
                            3 -> playerl(FaceAnim.FRIENDLY, "What can you tell me about his sons?").also { stage = 11 }
                            4 ->
                                playerl(FaceAnim.FRIENDLY, "What can you tell me about his daughters?").also {
                                    stage =
                                        27
                                }
                            5 -> playerl(FaceAnim.FRIENDLY, "Who do you think was responsible?").also { stage = 40 }
                        }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Old Lord Sinclair was a great man with a lot of respect in these parts. More than his worthless children have anyway.",
                        ).also {
                            stage++
                        }
                    7 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "His children? They have something to gain by his death?",
                        ).also { stage++ }
                    8 ->
                        npcl(FaceAnim.FRIENDLY, "Yes. You could say that. Not that I'm one to gossip.").also {
                            stage =
                                END_DIALOGUE
                        }
                    9 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, they used to live in the big castle, but old Lord Sinclair gave it up so that those strange knights could live there instead. So the king built him a new house to the North.",
                        ).also {
                            stage++
                        }
                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "It's more cramped than his old place, but he seemed to like it. His children were furious at him for doing it though!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    11 -> playerl(FaceAnim.FRIENDLY, "What can you tell me about his sons?").also { stage++ }
                    12 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "His sons eh? They all have their own skeletons in their cupboards. You'll have to be more specific. Who are you interested in exactly?",
                        ).also {
                            stage++
                        }
                    13 -> options("Tell me about Bob.", "Tell me about David.", "Tell me about Frank.").also { stage++ }
                    14 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Tell me about Bob.").also { stage++ }
                            2 -> playerl(FaceAnim.FRIENDLY, "Tell me about David.").also { stage = 20 }
                            3 -> playerl(FaceAnim.FRIENDLY, "Tell me about Frank.").also { stage = 7 }
                        }
                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Bob is an odd character indeed... I'm not one to gossip, but I heard Bob is addicted to Tea. He can't make",
                        ).also {
                            stage++
                        }
                    16 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "it through the day without having at least 20 cups!",
                        ).also { stage++ }
                    17 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You might not think that's such a big thing, but he has spent thousands of gold to feed his habit!",
                        ).also {
                            stage++
                        }
                    18 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "At one point he stole a lot of silverware from the kitchen and pawned it just so he could afford to buy his daily tea allowance.",
                        ).also {
                            stage++
                        }
                    19 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "If his father ever found out, he would be in so much trouble... he might even get disowned!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    20 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "David... oh David... not many people know this, but David really has an anger problem. He's always screaming and shouting",
                        ).also {
                            stage++
                        }
                    21 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "at the household servants when he's angry, and they live in a state of fear, always walking on eggshells around him, but none of them have the courage",
                        ).also {
                            stage++
                        }
                    22 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "to talk to his father about his behaviour. If they did, Lord Sinclair would almost certainly",
                        ).also {
                            stage++
                        }
                    23 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "kick him out of the house, as some of the servants have been there longer than he has, and he definitely has no right to treat them like he does... but I'm not one to gossip about people.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    24 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm not one to talk ill of people behind their back, but Frank is a real piece of work. He is an absolutely terrible gambler... he can't pass 2 dogs in the street without putting a bet on which one will bark first!",
                        ).also {
                            stage++
                        }
                    25 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "He has already squandered all of his allowance, and I heard he had stolen a number of paintings of his fathers to sell to try and cover his debts, but he still owes a lot of",
                        ).also {
                            stage++
                        }
                    26 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "people a lot of money. If his father ever found out, he would stop his income, and then he would be in serious trouble!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    27 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "His daughters eh? They're all nasty pieces of work. which of them specifically did you want to know about?",
                        ).also {
                            stage++
                        }
                    28 ->
                        options(
                            "Tell me about Anna.",
                            "Tell me about Carol.",
                            "Tell me about Elizabeth.",
                        ).also { stage++ }

                    29 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Tell me about Anna.").also { stage++ }
                            2 -> playerl(FaceAnim.FRIENDLY, "Tell me about Carol.").also { stage = 33 }
                            3 -> playerl(FaceAnim.FRIENDLY, "Tell me about Elizabeth.").also { stage = 7 }
                        }
                    30 -> npcl(FaceAnim.FRIENDLY, "Anna... ah yes... Anna has 2 great loves:").also { stage++ }
                    31 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Sewing and gardening. But one thing she has kept secret is that she once had an affair with Stanford the gardener, and tried to get him fired when they broke up,",
                        ).also {
                            stage++
                        }
                    32 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "by killing all of the flowers in the garden. If her father ever found out she had done that he would be so furious he would probably disown her.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    33 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Oh Carol... she is such a fool. You didn't hear this from me, but I heard a while ago she was conned out of a lot of money by a travelling salesman who sold her a box full",
                        ).also {
                            stage++
                        }
                    34 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "of beans by telling her they were magic. But they weren't. She sold some rare books from the library to cover her debts, but",
                        ).also {
                            stage++
                        }
                    35 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "her father would be incredibly annoyed if he ever found out - he might even throw her out of the house!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    36 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Elizabeth? Elizabeth has a strange problem... She cannot help herself, but is always stealing small objects - it's pretty sad that she is rich enough to afford to buy things, but would rather steal them instead.",
                        ).also {
                            stage++
                        }
                    37 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Now, I don't want to spread stories, but I heard she even stole a silver needle from her father that had great sentimental value for him.",
                        ).also {
                            stage++
                        }
                    38 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "He was devastated when it was lost, and cried for a week thinking he had lost it!",
                        ).also {
                            stage++
                        }
                    39 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "If he ever found out that it was her who had stolen it he would go absolutely mental, maybe even disowning her!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    40 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, I guess it could have been an intruder, but with that big guard dog of theirs I seriously doubt it. I suspect it was someone closer to home...",
                        ).also {
                            stage++
                        }
                    41 ->
                        if (getQuestStage(player, Quests.MURDER_MYSTERY) == 1) {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Especially as I heard that the poison salesman in the Seers' village made a big sale to one of the family the other day.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                        } else {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Especially as I heard that the poison salesman in the Seers' village made a big sale to one of the family the other day.",
                            ).also {
                                stage++
                            }
                        }

                    42 -> playerl(FaceAnim.FRIENDLY, "I think the butler did it.").also { stage++ }
                    43 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "And I think you've been reading too many cheap detective novels. Hobbes is kind of uptight, but his loyalty to old Lord Sinclair is beyond question.",
                        ).also {
                            stage++
                        }
                    44 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "I am so confused about who did it... Think you could give me any hints?",
                        ).also {
                            stage++
                        }
                    45 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, I don't know if it's related, but I heard from that Poison Salesman in town that he sold some poison to one of the Sinclair family the other day. I don't think he has any stock left now though...",
                        ).also {
                            stage++
                        }
                    46 ->
                        if (getQuestStage(player, Quests.MURDER_MYSTERY) == 2) {
                            npcl(
                                FaceAnim.FRIENDLY,
                                "Well, this might be of some help to you. My father was in the guards when he was younger and he always said that there isn't a crime that can't be solved through careful examination of the crime scene and all surrounding areas.",
                            ).also { stage = END_DIALOGUE }
                        } else {
                            end()
                        }
                }

            100 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There's some kind of commotion up at the Sinclair place I hear. Not surprising all things considered.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOSSIP_813)
    }
}
