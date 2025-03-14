package content.region.misthalin.dialogue.lumbridge

import core.api.quest.isQuestComplete
import core.api.sendGraphics
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.IronmanMode
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LumbridgeGuideDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val ironman = player.ironmanManager.isIronman
        val sheepShearerComplete = isQuestComplete(player, Quests.SHEEP_SHEARER)
        val cooksAssistantComplete = isQuestComplete(player, Quests.COOKS_ASSISTANT)

        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Greetings, adventurer. I am Phileas, the Lumbridge Guide. I am here to give information and directions to new players. Do you require any help?",
                ).also {
                    stage++
                }
            1 ->
                showTopics(
                    Topic("Where can I find a quest to go on?", 10),
                    Topic("What monsters should I fight?", 20),
                    Topic("Where can I make money?", 30),
                    Topic("I'd like to know more about security.", 40),
                    IfTopic("Where can I find a bank?", 50, !ironman),
                    IfTopic("More Options...", 100, ironman, skipPlayer = true),
                )
            100 ->
                showTopics(
                    Topic("Where can I find a bank?", 50),
                    IfTopic("I would like to de-iron.", 51, ironman),
                    Topic("Go back...", 1, skipPlayer = true),
                )
            10 ->
                if (!cooksAssistantComplete) {
                    npcl(
                        FaceAnim.HALF_THINKING,
                        "You can try talking to the Cook in the Lumbridge Castle. I hear he is always looking for some help.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else if (!sheepShearerComplete) {
                    npcl(
                        FaceAnim.HALF_THINKING,
                        "You can try talking to Fred the Farmer north-west of here. I hear he is always looking for some help.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "You are such an accomplished adventurer already; you should be telling me some good quests to go on.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
            20 ->
                if (player.properties.currentCombatLevel >= 30) {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "You're strong enough to work out what monsters to fight for yourself now, but the tutors might help you with any questions you have about the skills; they're just south of the general store.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "There are things to kill all over the place! At your level, you might like to try wandering westwards to the Wizards' Tower or north-west to the Barbarian Village.",
                    ).also {
                        stage++
                    }
                }
            21 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Non-player characters usually appear as yellow dots on your mini-map, although there are some that you won't be able to fight, such as myself. Watch out for monsters which are tougher",
                ).also {
                    stage++
                }
            22 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "than you. A monster's combat level is shown next to their 'Attack' option. If that level is coloured green it means the monster is weaker than you. If it is red, it means the monster is tougher than you.",
                ).also {
                    stage++
                }
            23 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Remember, you will do better if you have better armour and weapons and it's always worth carrying a bit of food to heal yourself.",
                ).also {
                    stage =
                        1
                }
            30 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There are many ways to make money in the game. I would suggest either killing monsters or doing a trade skill such as Smithing or Fishing.",
                ).also {
                    stage++
                }
            31 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Please don't try to get money by begging off other players. It will make you unpopular. Nobody likes a beggar. It is very irritating to have other players asking for your hard-earned cash.",
                ).also {
                    stage =
                        1
                }
            40 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I can tell you about password security, avoiding item scamming and in-game moderation. I can also tell you about a place called the Stronghold of Security, where you can learn more about account security and have a",
                ).also {
                    stage++
                }
            41 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "bit of an adventure at the same time. In fact, why don't you just head there instead? It's a lot more fun, I promise. You can find it down the hole in the middle of Barbarian Village to the north-west.",
                ).also {
                    stage =
                        1
                }
            50 ->
                npcl(FaceAnim.FRIENDLY, "You'll find a bank upstairs in Lumbridge Castle - go right to the top!").also {
                    stage =
                        1
                }
            51 -> npcl(FaceAnim.FRIENDLY, "Of course, but first let me give you a word of warning.").also { stage++ }
            52 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Should you choose to step away from the path of the ironman now, you will not have the option to return.",
                ).also {
                    stage++
                }
            53 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Now I ask you to make sure, are you sure you want to <b>permanently</b> remove ironman mode?",
                ).also {
                    stage++
                }
            54 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Yes, I'm sure.", 55),
                    Topic(FaceAnim.FRIENDLY, "No, I've changed my mind.", END_DIALOGUE),
                )
            55 -> npcl(FaceAnim.FRIENDLY, "Very well, let me just check one thing...").also { stage++ }
            56 ->
                if (player.ironmanManager.mode == IronmanMode.HARDCORE) {
                    npcl(
                        FaceAnim.WORRIED,
                        "Oh, dear, it's just as I feared. You're a hardcore ironman! My apologies, but there's nothing I can do to help.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Oh, wonderful. It appears everything is in order. Sit still for a moment...",
                    ).also { stage++ }
                }

            57 -> {
                sendGraphics(342, player.location)
                sendDialogue("The wise old wizard casts a strange spell.")
                stage++
            }

            58 -> {
                player.ironmanManager.mode = IronmanMode.NONE
                npcl(
                    FaceAnim.HALF_ASKING,
                    "There, I believe it is done. You should no longer be restricted from the wider world.",
                )
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LUMBRIDGE_GUIDE_2244)
    }
}
