package content.region.morytania.quest.route.dialogue

import core.api.quest.getQuestStage
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class CyregPaddlehornDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (getQuestStage(player, Quests.IN_SEARCH_OF_THE_MYREQUE) == 1) {
            npcl(FaceAnim.FRIENDLY, "Can you tell me how to find the <col=08088A>Myreque</col>?").also { stage++ }
        } else if (getQuestStage(player, Quests.IN_SEARCH_OF_THE_MYREQUE) == 2) {
            npcl(FaceAnim.FRIENDLY, "Will you still take me to the <col=08088A>Myreque</col>?").also { stage++ }
        } else if (getQuestStage(player, Quests.IN_SEARCH_OF_THE_MYREQUE) >= 3) {
            playerl(FaceAnim.FRIENDLY, "Can I ask some more questions?").also { stage++ }
        } else {
            end()
            sendDialogue(player, "Cyreg Paddlehorn is not interested in talking.")
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.IN_SEARCH_OF_THE_MYREQUE)) {
            1 ->
                when (stage) {
                    START_DIALOGUE ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Their base is well hidden, and I'm sorry but I can't reveal the directions. Sorry, but I guess you're all out of luck.",
                        ).also {
                            stage =
                                2
                        }
                    2 -> playerl(FaceAnim.FRIENDLY, "Oh come on, you can tell me!").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm sorry, I can't. I just can't... people are watching... eyes everywhere!",
                        ).also {
                            stage++
                        }
                    4 ->
                        options(
                            "Well, I guess they'll just die without weapons.",
                            "I'll give you some cash if you help me.",
                            "I just want to help them, I think they need help.",
                        ).also {
                            stage++
                        }
                    5 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Well, I guess they'll just die without weapons.",
                                ).also { stage++ }
                            2 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "I'll give you some cash if you help me.",
                                ).also { stage = 30 }
                            3 ->
                                playerl(FaceAnim.FRIENDLY, "I just want to help them, I think they need help.").also {
                                    stage =
                                        33
                                }
                        }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hmm, you don't seem too concerned about their welfare... I'm glad I didn't tell you where they were. In any case, they're resourceful. They can look after themselves.",
                        ).also {
                            stage++
                        }
                    7 -> playerl(FaceAnim.FRIENDLY, "What's that supposed to mean?").also { stage++ }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "They're resourceful folks, that's all I'm saying. Their leader, <col=08088A>Veliaf</col>, looks after them well.",
                        ).also {
                            stage++
                        }
                    9 ->
                        options(
                            "Resourceful enough to get their own steel weapons?",
                            "I'll give you some cash if you help me.",
                        ).also {
                            stage++
                        }
                    10 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Resourceful enough to get their own steel weapons?",
                                ).also { stage++ }

                            2 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "I'll give you some cash if you help me.",
                                ).also { stage = 30 }
                        }
                    11 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Maybe they are... what do you care anyway? They've been up against it ever since they got started. All of 'em have suffered more loss and heartache than you'll ever know. Now, leave me be!",
                        ).also {
                            stage++
                        }
                    12 ->
                        options(
                            "What have they been up against?",
                            "What kind of loss and heartache?",
                            "If you don't tell me, their deaths will be on your head!",
                        ).also {
                            stage++
                        }
                    13 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "What have they been up against?").also { stage++ }
                            2 -> playerl(FaceAnim.FRIENDLY, "What kind of loss and heartache?").also { stage = 15 }
                            3 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "If you don't tell me, their deaths will be on your head!",
                                ).also {
                                    stage =
                                        18
                                }
                        }
                    14 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You're not from around here or you wouldn't be asking such foolish questions. <col=08088A>Morytania</col> is ruled by a cruel dark overlord by the name of Drakan. His reign over Morytania means we all live in fear.",
                        ).also {
                            stage =
                                12
                        }
                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The worst kind, most have lost members of their family. Little <col=08088A>Sani Piliu</col>, she was orphaned overnight when a vampyre went on the rampage... Imagine that, losing your entire family in one night? Terrible!",
                        ).also {
                            stage++
                        }
                    16 -> playerl(FaceAnim.FRIENDLY, "It sounds awful... Who is Sani Piliu?").also { stage++ }
                    17 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "She's the only female member of the Myreque. She's already proven herself with her agility and light fingers, if you know what I mean!",
                        ).also {
                            stage =
                                12
                        }
                    18 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There's death aplenty in this forsaken place... what do I care that some foolhardy vigilantes decided to go it alone against the Drakans? Stupidity of youth is to blame, I shan't carry it on my shoulders!",
                        ).also {
                            stage++
                        }
                    19 ->
                        options(
                            "One mans vigilante is another mans freedom fighter!",
                            "Who are the Drakans?",
                            "What kind of man are you to say that you don't care?",
                            "Why do you say that this place is 'forsaken'?",
                        ).also {
                            stage++
                        }
                    20 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "One mans vigilante is another mans freedom fighter!",
                                ).also { stage++ }
                            2 -> playerl(FaceAnim.FRIENDLY, "Who are the Drakans?").also { stage = 24 }
                            3 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "What kind of man are you to say that you don't care?",
                                ).also {
                                    stage =
                                        26
                                }
                            4 ->
                                playerl(FaceAnim.FRIENDLY, "Why do you say that this place is 'forsaken'?").also {
                                    stage =
                                        29
                                }
                        }
                    21 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Aye, you can see it from both sides I suppose. But many of us consider it fool hardy to fight for something we'll never get, even Polmafi, a scholar, such as he was, agrees that the chances are slim.",
                        ).also {
                            stage++
                        }
                    22 -> playerl(FaceAnim.FRIENDLY, "Polmafi? Who's he?").also { stage++ }
                    23 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "<col=08088A>Polmafi Ferdygris</col> is one of the Myreque. He's a technical sort and advises on all sorts of things to <col=08088A>Veliaf</col>. He was a scholar before he became a renegade.",
                        ).also {
                            stage =
                                20
                        }
                    24 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The Drakans are the family of overlords that rule Morytania. They're the ones to whom the blood tithes are paid. Too much I have told you already!",
                        ).also {
                            stage++
                        }
                    25 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ignorance is better than these truths, I tell you! I can pretend once more that I am a free man, and some relief from this gloom can I feel again. Be gone with you now and leave me with my dreams.",
                        ).also {
                            stage =
                                20
                        }
                    26 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Don't dare to judge me, young fool... what do you know of the heartache I carry? Can you not see the anchor of woe that holds me fast?",
                        ).also {
                            stage++
                        }
                    27 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Very well, if you would take your chance to help these strangers, who am I to stop you?",
                        ).also {
                            stage++
                        }
                    28 -> npcl(FaceAnim.FRIENDLY, "But will you help me? Will you take me to them?").also { stage = 19 }
                    29 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "All of these lands are forsaken of <col=08088A>Saradomin's kindness</col>, only cold death from the evil gods do we now feel. Those lucky ones to the west of the <col=08088A>Salve</col> little realise their fate if the river should one day become tainted.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    30 -> npcl(FaceAnim.FRIENDLY, "You think you can buy me!").also { stage++ }
                    31 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Er, no, I just want to compensate you for your trouble!",
                        ).also { stage++ }
                    32 ->
                        npcl(FaceAnim.FRIENDLY, "You keep your money and I'll keep my secrets.").also {
                            stage =
                                END_DIALOGUE
                        }

                    33 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Aye, well, they may do... but it's just not safe and it's not likely to get safer any time soon. Though I do fell sorry for Ivan, the baby of the group. He's seen too few winters to be involved in such toil.",
                        ).also {
                            stage++
                        }
                    34 -> playerl(FaceAnim.FRIENDLY, "Ok, thanks.").also { stage = END_DIALOGUE }
                }

            2 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "No, I won't take you, but you can use my boat. You'll be going through <col=08088A>Mort Myre</col>, though, so I won't be letting you go unless you've some defense against the <col=08088A>Ghasts</col>.",
                        ).also {
                            stage++
                        }
                    2 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I don't have anything which I can use against them at this time.",
                        ).also { stage++ }
                    3 -> sendDialogue(player, "You show the boatman your druid pouch.").also { stage++ }
                    4 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I have this druid pouch! This turns the <col=08088A>Ghasts</col> visible and I can kill them once I can see them.",
                        ).also {
                            stage++
                        }
                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Very well, but I still cannot let you pass unprepared!",
                        ).also { stage++ }
                    6 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "My boat needs some work, so I'll need three <col=08088A>wooden planks</col>. The bridge you'll need to cross later is rotten and may need to be mended, so you'll need three more planks as well as a hammer, and I'd say around 75 <col=08088A>nails</col>.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    7 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I also have the three wooden planks you asked for.",
                        ).also { stage++ }
                    8 -> sendDialogue(player, "the boatman takes 3 wooden planks from you.").also { stage++ }
                    9 -> playerl(FaceAnim.FRIENDLY, "Not just yet, sorry.").also { stage = END_DIALOGUE }
                }

            3 ->
                when (stage) {
                    1 -> npcl(FaceAnim.FRIENDLY, "Sure you can.").also { stage++ }
                    2 ->
                        options(
                            "Where do I go after I get to the hollows?",
                            "Tell me about the '<col=08088A>Myreque.</col>'",
                            "Tell me about the <col=08088A>Drakans</col>.",
                            "Tell me about the yourself.",
                        ).also {
                            stage++
                        }
                    3 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Where do I go after I get to the hollows?",
                                ).also { stage++ }
                            2 ->
                                playerl(FaceAnim.FRIENDLY, "Tell me about the '<col=08088A>Myreque</col>.'").also {
                                    stage =
                                        5
                                }
                            3 ->
                                playerl(FaceAnim.FRIENDLY, "Tell me about the <col=08088A>Drakans</col>.").also {
                                    stage =
                                        13
                                }
                            4 -> playerl(FaceAnim.FRIENDLY, "Tell me about the yourself.").also { stage = 14 }
                        }
                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You should head north and look for an unusual tree. You look like you know a thing or two about exploring so I guess I don't need to tell you to keep your eyes peeled!",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    5 -> npcl(FaceAnim.FRIENDLY, "What do you want to know?").also { stage++ }
                    6 ->
                        options(
                            "Why are they call 'the Myreque'?",
                            "Tell me about the members of the Myreque",
                        ).also { stage++ }
                    7 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Why are they call 'the Myreque'?").also { stage++ }
                            2 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Tell me about the members of the Myreque",
                                ).also { stage = 5 }
                        }
                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The locals just called them after the place where they hide out, in Mort Myre. They are Myreque, 'hidden in the myre'.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, you have <col=08088A>Radigad Ponfit</col>... he's your average mercenary from <col=08088A>Asgarnia</col>, ready to slice anyone's head off for a price. He's got a personal score to settle with the Drakans, though no one knows what it is.",
                        ).also {
                            stage++
                        }
                    11 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Then there's <col=08088A>Veliaf</col>, he's the leader. and then there's Ivan, he's the baby of the group. There's Sani Piliu, she's a lovely girl, though a bit shady if yo know what I mean.",
                        ).also {
                            stage++
                        }
                    12 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "You've also got <col=08088A>Harold Evans</col>, he's a bit hot-headed, always straight into the fray. And the brains of the operation, reporting directly to Veliaf, is Polmafi Ferdygris, he used to be quite a clever scholar!",
                        ).also {
                            stage++
                        }
                    13 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well it's rumoured that they're controlling the whole of Morytania. They live in the Sanguinesti region, but I've never been, like most villagers in Morytania, I'm afraid to leave the village and dread what lies beyond.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    14 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm just a humble boatman, <col=08088A>Cyreg Paddlehorn</col> is my name. Like most of the Paddlehorns before me, I make my living by tracking the swamps of <col=08088A>Mort Myre.</col>",
                        ).also {
                            stage++
                        }
                    15 -> playerl(FaceAnim.FRIENDLY, "Ok thanks.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CYREG_PADDLEHORN_1567)
}
