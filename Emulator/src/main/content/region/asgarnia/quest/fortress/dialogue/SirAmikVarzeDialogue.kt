package content.region.asgarnia.quest.fortress.dialogue

import core.api.*
import core.api.quest.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class SirAmikVarzeDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.BLACK_KNIGHTS_FORTRESS)
        when (quest!!.getStage(player)) {
            30 -> player(FaceAnim.HAPPY, "I have ruined the Black Knights' invincibility potion.")
            10, 20 -> npc(FaceAnim.ASKING, "How's the mission going?")
            100 -> player(FaceAnim.HAPPY, "Hello Sir Amik.")
            else ->
                npc(
                    FaceAnim.THINKING,
                    "I am the leader of the White Knights of Falador. Why",
                    "do you seek my audience?",
                )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (quest!!.getStage(player)) {
            100 ->
                when (stage) {
                    0 ->
                        if (isQuestComplete(player!!, Quests.BLACK_KNIGHTS_FORTRESS) &&
                            isQuestComplete(
                                player!!,
                                Quests.DRUIDIC_RITUAL,
                            ) &&
                            getQuestStage(player, Quests.RECRUITMENT_DRIVE) == 0
                        ) {
                            playerl(
                                FaceAnim.THINKING,
                                "Do you have any other quests for me to do?",
                            ).also { stage = 112 }
                        } else if (isQuestInProgress(player!!, Quests.RECRUITMENT_DRIVE, 1, 100)) {
                            options(
                                "Who are you?",
                                "Can I just skip the test to become a Temple Knight?",
                                "Tell me about the Temple Knights.",
                                "Do you know anything about the siege of Falador?",
                                "Farewell.",
                            ).also { stage = 100 }
                        } else {
                            npcl(
                                FaceAnim.THINKING,
                                "A quest? Alas I do not have any quests I can offer you at this time.",
                            ).also { stage = END_DIALOGUE }
                        }

                    100 ->
                        when (buttonId) {
                            1 -> playerl(FaceAnim.HALF_ASKING, "Who are you?").also { stage = 111 }
                            2 ->
                                playerl(
                                    FaceAnim.HALF_ASKING,
                                    "Can I just skip the test to become a Temple Knight?",
                                ).also { stage = 110 }

                            3 -> playerl(FaceAnim.HALF_ASKING, "Tell me about the Temple Knights.").also { stage = 101 }
                            4 ->
                                playerl(
                                    FaceAnim.HALF_ASKING,
                                    "Do you know anything about the siege of Falador?",
                                ).also { stage = 105 }

                            5 -> player("Farewell.").also { stage = END_DIALOGUE }
                        }

                    101 ->
                        npc(
                            "I cannot tell you much... They are called the Temple",
                            "Knights, and are an organisation that was founded by",
                            "Saradomin personally many centuries ago.",
                        ).also { stage = 102 }

                    102 ->
                        npc(
                            "There are many rumours and fables about their works and actions, but",
                            "official records of their presence are non-existent. It",
                            "is a secret organisation of extraordinary power and",
                            "resourcefulness...",
                        ).also { stage = 103 }

                    103 ->
                        npc(
                            "Let me put it this way: Should you decide to take them up on",
                            "their generous offer to join, you will find yourself in an",
                            "advantageous position that many in this world would envy,",
                            "and that few are called to occupy.",
                        ).also { stage = 104 }

                    104 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Well, that wasn't quite as helpful as I thought it would be...but thanks anyway, I guess.",
                        ).also { stage = 0 }

                    105 ->
                        npc(
                            "Ah yes, the Kinshra siege was a close-run thing indeed.",
                            "All hope seemed lost. Then she returned - Kara-Meir",
                            "- with Sir Theodore at her side.",
                        ).also { stage = 106 }

                    106 ->
                        playerl(
                            FaceAnim.HALF_GUILTY,
                            "Sir Theodore? I haven't seen anyone of that name around here.",
                        ).also { stage = 107 }

                    107 -> npc("No, you wouldn't have. He is...no longer", "with us.").also { stage = 108 }
                    108 -> player("Oh, I'm sorry.").also { stage = 109 }
                    109 ->
                        npc("So am I, young ${if (player!!.isMale) "man" else "woman"}.", "So am I.").also {
                            stage =
                                0
                        }

                    110 ->
                        npc(
                            "No, I am afraid not. I suggest you go meet Sir Tiffy",
                            "in Falador Park, he will be expecting you.",
                        ).also { stage = 0 }

                    111 ->
                        npc(
                            "I am the leader of the White Knights of Falador.",
                            "Why do you seek my audience?",
                        ).also { stage = 0 }

                    112 ->
                        npc(
                            FaceAnim.FRIENDLY,
                            "Well, there is an organisation that is always looking for",
                            "capable adventurers to assist them.",
                        ).also { stage = 113 }

                    113 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "They are called the Temple Knights, and you are to meet Sir Tiffy Cashien in Falador park for testing immediately.",
                        ).also { stage = 114 }

                    114 -> player("I'll go do that, then.").also { stage = 115 }
                    115 -> {
                        end()
                        setQuestStage(player!!, Quests.RECRUITMENT_DRIVE, 1)
                        updateQuestTab(player!!)
                    }
                }

            30 ->
                when (stage) {
                    0 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Yes, we have just received a message from the Black",
                            "Knights saying they withdraw their demands, which",
                            "would seem to confirm your story.",
                        ).also { stage++ }

                    1 ->
                        player(
                            FaceAnim.HALF_ASKING,
                            "Now I believe there was some talk of a cash reward...",
                        ).also { stage++ }

                    2 -> npc(FaceAnim.HAPPY, "Absolutely right. Please accept this reward.").also { stage++ }
                    3 -> sendDialogue(player, "Sir Amik hands you 2500 coins.").also { stage++ }
                    4 -> {
                        addItemOrDrop(player, Items.COINS_995, 2500)
                        finishQuest(player, Quests.BLACK_KNIGHTS_FORTRESS)
                        updateQuestTab(player)
                        end()
                    }
                }

            20 ->
                when (stage) {
                    0 ->
                        player(
                            FaceAnim.NEUTRAL,
                            "I have managed to find what the secret weapon is",
                            "I am now in the process of destroying it.",
                        ).also { stage = 1 }

                    1 -> end()
                }

            10 ->
                when (stage) {
                    0 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "I haven't managed to find what the secret weapon is",
                            "yet...",
                        ).also { stage = 1 }

                    1 -> npc(FaceAnim.NEUTRAL, "Well keep at it! Falador's future is at stake!").also { stage++ }
                    2 ->
                        if (!player.inventory.containsItem(Item(Items.DOSSIER_9589)) &&
                            !player.bank.containsItem(Item(Items.DOSSIER_9589))
                        ) {
                            npc(FaceAnim.NEUTRAL, "Here's the dossier on the case.").also { stage = 3 }
                        } else {
                            npc(
                                FaceAnim.NEUTRAL,
                                "Don't forget to read that dossier of information I gave",
                                "you.",
                            ).also { stage = END_DIALOGUE }
                        }

                    3 -> {
                        addItem(player, Items.DOSSIER_9589)
                        end()
                    }

                    4 -> end()
                }

            0 ->
                when (stage) {
                    0 ->
                        if (getQuestPoints(player) < 12) {
                            player(FaceAnim.NEUTRAL, "I don't I'm just looking around.").also { stage = END_DIALOGUE }
                        } else {
                            options("I seek a quest!", "I don't, I'm just looking around.").also { stage = 1 }
                        }

                    1 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.NEUTRAL, "I seek a quest.").also { stage = 5 }
                            2 ->
                                player(FaceAnim.NEUTRAL, "I don't I'm just looking around.").also {
                                    stage =
                                        END_DIALOGUE
                                }
                        }

                    2 -> end()
                    3 -> npc(FaceAnim.HALF_WORRIED, "Ok. Please don't break anything.").also { stage = END_DIALOGUE }
                    4 -> end()
                    5 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Well, I need some spy work doing but it's quite",
                            "dangerous. It will involve going into the Black Knights'",
                            "fortress.",
                        ).also { stage++ }

                    6 ->
                        options(
                            "I laugh in the face of danger!",
                            "I go and cower in the corner at the first sign of danger!",
                        ).also { stage++ }

                    7 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.HAPPY, "I laugh in the face of danger!").also { stage = 15 }
                            2 ->
                                player(
                                    FaceAnim.HALF_GUILTY,
                                    "I go and cower in a corner at the first sign of danger!",
                                ).also { stage = 8 }
                        }

                    8 -> npc(FaceAnim.NEUTRAL, "Err....").also { stage++ }
                    9 -> npc(FaceAnim.NEUTRAL, "Well.").also { stage++ }
                    10 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "I... suppose spy work DOES involve a little hiding in",
                            "corners.",
                        ).also { stage++ }

                    11 ->
                        options(
                            "Oh. I suppose I'll give it a go then.",
                            "No, I'm not ready to do that.",
                        ).also { stage++ }
                    12 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.FRIENDLY, "Oh. I suppose I'll give it a go then.").also { stage = 17 }
                            2 -> player(FaceAnim.HALF_GUILTY, "No, I'm not ready to do that.").also { stage = 13 }
                        }

                    13 ->
                        npc(FaceAnim.NEUTRAL, "Come see me again if you change your mind.").also {
                            stage =
                                END_DIALOGUE
                        }
                    14 -> end()
                    15 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Well that's good. Don't get too overconfident though.",
                        ).also { stage++ }
                    16 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "You've come along at just the right time actually. All of",
                            "my knights are already known to the Black Knights.",
                        ).also { stage++ }

                    17 -> npc(FaceAnim.HALF_GUILTY, "Subtlety isn't exactly our strong point.").also { stage++ }
                    18 ->
                        player(
                            FaceAnim.HALF_ASKING,
                            "Can't you just take your White Knights' armour off?",
                            "They wouldn't recognise you then!",
                        ).also { stage++ }

                    19 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "I am afraid our charter prevents us using espionage in",
                            "any form, that is the domain of the Temple Knights.",
                        ).also { stage++ }

                    20 -> player(FaceAnim.HALF_ASKING, "Temple Knights? Who are they?").also { stage++ }
                    21 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "The information is classified. I am forbidden to share it",
                            "with outsiders.",
                        ).also { stage++ }

                    22 -> player(FaceAnim.HALF_ASKING, "So... What do you need doing?").also { stage++ }
                    23 ->
                        npc(
                            FaceAnim.ANGRY,
                            "Well, the Black Knights have started making strange",
                            "threats to us; demanding large amounts of money and",
                            "land, and threatening to invade Falador if we don't pay",
                            "them.",
                        ).also { stage++ }

                    24 -> npc(FaceAnim.NEUTRAL, "Now, NORMALLY this wouldn't be a problem...").also { stage++ }
                    25 -> npc(FaceAnim.ANGRY, "But they claim to have a powerful new secret weapon.").also { stage++ }
                    26 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Your mission, should you decide to accept it, is to",
                            "infiltrate their fortress, find out what their secret",
                            "weapon is, and then sabotage it.",
                        ).also { stage++ }

                    27 -> options("Ok, I'll do my best.", "No, I'm not ready to do that.").also { stage++ }
                    28 ->
                        when (buttonId) {
                            1 -> player(FaceAnim.NEUTRAL, "Ok, I'll do my best.").also { stage = 31 }
                            2 -> player(FaceAnim.NEUTRAL, "No, I'm not ready to do that.").also { stage++ }
                        }

                    29 ->
                        npc(FaceAnim.NEUTRAL, "Come see me again if you change your mind.").also {
                            stage =
                                END_DIALOGUE
                        }
                    30 -> end()
                    31 ->
                        npc(
                            FaceAnim.NEUTRAL,
                            "Good luck! Let me know how you get on. Here's the",
                            "dossier for the case, I've already given you the details.",
                        ).also { stage++ }

                    32 -> {
                        end()
                        if (freeSlots(player) == 0) {
                            sendMessage(player, "You don't have enough inventory space.")
                        } else {
                            addItem(player, Items.DOSSIER_9589)
                            quest!!.start(player)
                        }
                    }
                }

            else ->
                sendDialogue(player, "Sir Amik Varze is not interested in talking right now.").also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SirAmikVarzeDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIR_AMIK_VARZE_608)
    }
}
