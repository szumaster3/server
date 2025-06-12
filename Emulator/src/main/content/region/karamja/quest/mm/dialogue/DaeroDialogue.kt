package content.region.karamja.quest.mm.dialogue

import core.api.openOverlay
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendItemDialogue
import core.api.teleport
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

class DaeroDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        when (stage) {
            0 -> playerl("Are you Daero?").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Indeed i am - and you are?").also { stage++ }
            2 -> playerl("I am an adventurer. I am currently in service of your King.").also { stage++ }
            3 -> {
                if (getQuestStage(player!!, Quests.MONKEY_MADNESS) <= 11) {
                    end()
                } else {
                    npcl(
                        FaceAnim.OLD_DEFAULT,
                        "I see. You must be the individual who helped defeat my predecessor Glough. I hope you'll find me a more honest replacement.",
                    ).also { stage++ }
                }
            }

            4 -> playerl("I have been asked to give you orders from King Narnode.").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "Well, hand them over here then.").also { stage++ }
            6 -> {
                if (player!!.inventory.containsItem(Item(Items.NARNODES_ORDERS_4005))) {
                    sendItemDialogue(
                        player!!,
                        Items.NARNODES_ORDERS_4005,
                        "You hand King Narnode's order to Daero.",
                    ).also { stage++ }
                } else {
                    sendItemDialogue(
                        player!!,
                        Items.NARNODES_ORDERS_4005,
                        "You need the King Narnode's order.",
                    ).also { stage = END_DIALOGUE }
                }
            }

            7 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "It is written in a old military code. Bear with me whilst i decode this.",
                ).also { stage++ }

            8 -> npcl(FaceAnim.OLD_DEFAULT, "I hope like you feel like a quest adventurer ...").also { stage++ }
            9 -> playerl("Why is that?").also { stage++ }
            10 -> npcl(FaceAnim.OLD_DEFAULT, "... because you're going to get one.").also { stage++ }
            11 -> playerl("Tell me what the order say!").also { stage++ }
            12 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Given your recent performance of uncovering and neutralising a treat at the very extremes of the Gnome hierarchy, the King has decreed that you are to undertake a reconnaissance mission.",
                ).also { stage++ }

            13 -> playerl("Where to?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You are to be taken far to the south of Karamja, further tha any Gnome has purposely travelled before. You are to investigate Canarock's claim that Garkor's squad were blown off course.",
                ).also { stage++ }

            15 -> npcl(FaceAnim.OLD_DEFAULT, "You must really have impressed the King in the past.").also { stage++ }
            16 ->
                options(
                    "Talk about the journey...",
                    "Talk about the 10th squad...",
                    "Talk about Canarock...",
                ).also { stage++ }

            17 ->
                when (buttonID) {
                    1 ->
                        options(
                            "What lies to the south of Karamja?",
                            "How will i travel?",
                            "Are you coming with me?",
                            "Return to previous menu.",
                        ).also { stage = 18 }

                    2 ->
                        options(
                            "Why did the king send a squad of the royal guard?",
                            "Who is Garkor?",
                            "Why are the 10th squad so famous?",
                        ).also { stage = 24 }

                    3 ->
                        options(
                            "Who is Caranock?",
                            "What is a Gnome Liaison Officer?",
                            "I am not sure about Caranock...",
                        ).also { stage = 34 }
                }

            18 ->
                when (buttonID) {
                    1 -> playerl("What lies to the south of Karamja?").also { stage = 19 }
                    2 -> playerl("How will i travel?").also { stage = 22 }
                    3 -> playerl("Are you coming with me?").also { stage = 23 }
                    4 -> {
                        stage = 16
                    }
                }

            19 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "We do not know. Initial reports spoke of a large atoll populated by monkeys.",
                ).also { stage++ }

            20 -> playerl("Monkeys? Like on Karamja?").also { stage++ }
            21 ->
                npcl(FaceAnim.OLD_DEFAULT, "From what i have heard, not quite like those monkeys... ").also {
                    stage = 16
                }

            22 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "It is my responsibility to make arrangements for your mission. We will shortly visit a colleague of mine how will be accompanying you.",
                ).also { stage = 16 }

            23 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I am afraid not. I must remain here to safeguard the Grand Tree. I will assign a Gnome agent to travel with you.",
                ).also { stage = 16 }

            24 ->
                when (buttonID) {
                    1 -> playerl("Why did the king send a squad of the royal guard?").also { stage = 25 }
                    2 -> playerl("Who is Garkor?").also { stage = 29 }
                    3 -> playerl("Why are the 10th squad so famous?").also { stage = 31 }
                    4 -> {
                        stage = 16
                    }
                }

            25 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "The Royal Guard is composed of particularly elite soldier who have proven themselves in battle. They are duty bound to protect the Grand Tree, its King and his interests.",
                ).also { stage++ }

            26 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "In the face of danger, they can more than take care of themselves.",
                ).also { stage++ }

            27 -> playerl("So the King worries we have come across an equally formidable foe?").also { stage++ }
            28 -> npcl(FaceAnim.OLD_DEFAULT, "He worries about this, yes.").also { stage = 16 }
            29 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "Sergeant Garkor holds the command of the 10th squad. As a soldier, he is extremely able. If his men are in trouble, he will be tirelessly working to save them.",
                ).also { stage++ }

            30 -> npcl(FaceAnim.OLD_DEFAULT, "You should aim to make contact with him.").also { stage = 16 }
            31 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "They are, as you humans might say, the best of the best. As well as Sergeant Garkor, they have in their company a High mage, two sappers and battle hardened foot soldiers.",
                ).also { stage++ }

            32 -> playerl("What is a sapper?").also { stage++ }
            33 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "You might consider the role as that of an engineer. Or perhaps of a munitions expert.",
                ).also {
                    stage = 16
                }

            34 ->
                when (buttonID) {
                    1 -> playerl("Who is Caranock?").also { stage = 35 }
                    2 -> playerl("What is a Gnome Liaison Officer?").also { stage = 36 }
                    3 -> playerl("I am not sure about Caranock...").also { stage = 37 }
                }

            35 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I have never heard of him. According to the report you made to the King, he is the Gnome Liaison Officer at the eastern Karamja shipyard.",
                ).also { stage = 16 }

            36 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "It was a position Glough introduced. Gnnome Liaison Officers are responsible for coordinating activities between Gnomes and other beings in our remote operations.",
                ).also { stage = 16 }

            37 -> npcl(FaceAnim.OLD_DEFAULT, "In what way?").also { stage++ }
            38 ->
                playerl(
                    "I do not know. He just seemed a little suspicious. He was very keen to see me leave.",
                ).also { stage++ }
            39 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I do not know him; he is from before my time. Glough would presumably have hand picked him.",
                ).also { stage++ }

            40 ->
                options(
                    "Talk about the journey...",
                    "Talk about the 10th squad...",
                    "Talk about Canarock...",
                    "Leave...",
                ).also { stage++ }

            41 ->
                when (buttonID) {
                    1 ->
                        options(
                            "What lies to the south of Karamja?",
                            "How will i travel?",
                            "Are you coming with me?",
                            "Return to previous menu.",
                        ).also { stage = 18 }

                    2 ->
                        options(
                            "Why did the king send a squad of the royal guard?",
                            "Who is Garkor?",
                            "Why are the 10th squad so famous?",
                        ).also { stage = 24 }

                    3 ->
                        options(
                            "Who is Caranock?",
                            "What is a Gnome Liaison Officer?",
                            "I am not sure about Caranock...",
                        ).also { stage = 34 }

                    4 -> playerl("Let us go then.").also { stage = 42 }
                }

            42 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "I must first introduce you to a colleague of mine who will be accompanying you on your mission.",
                ).also { stage++ }

            43 -> options("Who is it?", "I work better on my own...").also { stage++ }
            44 ->
                when (buttonID) {
                    1 -> playerl("Who is it?").also { stage = 45 }
                    2 -> playerl("I work better on my own...").also { stage = 40 }
                }

            45 -> npcl(FaceAnim.OLD_DEFAULT, "His name is Flight Commander Waydar.").also { stage++ }
            46 ->
                npcl(
                    FaceAnim.OLD_DEFAULT,
                    "We must go now and meet Waydar. For security reasons I must ask you to wear a blindfold.",
                ).also { stage++ }

            47 ->
                sendItemDialogue(
                    player!!,
                    Items.EYE_PATCH_1025,
                    "You were the blindfold Daero hands you.",
                ).also { stage++ }

            48 -> {
                openOverlay(player!!, Components.FADE_TO_BLACK_120)
                GameWorld.Pulser.submit(
                    object : Pulse(8) {
                        override fun pulse(): Boolean {
                            openOverlay(player!!, Components.FADE_FROM_BLACK_170)
                            teleport(player!!, Location.create(2390, 9886, 0)).also {
                                setQuestStage(player!!, Quests.MONKEY_MADNESS, 21)
                                stage = END_DIALOGUE
                                end()
                            }
                            return true
                        }
                    },
                )
            }
        }
    }
}
