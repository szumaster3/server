package content.region.misthalin.dialogue.varrock

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class HooknosedJackDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hello.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "What?").also { stage++ }
            1 ->
                npc(FaceAnim.HALF_GUILTY, "Actually I've got no time for this. I don't want to talk to", "you.").also {
                    stage =
                        END_DIALOGUE
                }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ah, it's Player, isn't it? Jimmy D said you'd be along. So what can I do for you?",
                ).also {
                    stage++
                }
            4 ->
                sendDialogue(
                    player,
                    "You can't help but stare at Jack's mangled nose. It's not only huge, but dreadfully crooked.",
                ).also {
                    stage++
                }
            5 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "- Aside - I wonder if he can smell around corners with that nose.",
                ).also { stage++ }
            6 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Uh, sorry, yeah Jimmy Dazzler told me that you're 'the ticket' if I wanted to learn about grown cats.",
                ).also {
                    stage++
                }
            7 -> playerl(FaceAnim.FRIENDLY, "You quickly avert your gaze from his face.").also { stage++ }
            8 -> npcl(FaceAnim.FRIENDLY, "Do you have a problem with my face?").also { stage++ }
            9 ->
                sendDialogue(
                    player,
                    "With determination not to show any signs of embarrassment, you look Jack in the eyes.",
                ).also {
                    stage++
                }
            10 -> playerl(FaceAnim.FRIENDLY, "No. Should I?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "Uh... I guess not. It's just that most... ah, forget it.").also { stage++ }
            12 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "So will you help me? Or do I have to do some task for you first to prove that I'm worthy, or in payment?",
                ).also {
                    stage++
                }
            13 -> npcl(FaceAnim.FRIENDLY, "Nah. I'm too busy.").also { stage++ }
            14 -> playerl(FaceAnim.FRIENDLY, "What? Oh come on.").also { stage++ }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "No really, I'm just too busy at the moment. I've got three jobs going at the minute and I just don't have any time for helping the likes of you out.",
                ).also {
                    stage++
                }
            16 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Look if you need help catching rats I can lend you a hand, I got some good hands on experience from Jimmy Dazzler, Phingspet and Grimesquit.",
                ).also {
                    stage++
                }
            17 -> npcl(FaceAnim.FRIENDLY, "Ah you've met the sisters. Lovely girls aren't they?").also { stage++ }
            18 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "- Aside - What? Does this guy have any taste? Or sense of smell, for that matter?",
                ).also {
                    stage++
                }
            19 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Uhh... yeah, lovely girls. How about I take some of your workload off you? Then you would have some time to teach me.",
                ).also {
                    stage++
                }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I suppose there is this job I have just south of here. I need to get someone to clear out a warehouse of rats. It should be a simple job of poisoning a few rat holes. Nothing too complicated.",
                ).also {
                    stage++
                }
            21 -> playerl(FaceAnim.FRIENDLY, "So will any type of poison do?").also { stage++ }
            22 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "No, you need rat poison to kill rats. You can make it yourself. I'm sure I've seen some lying around.",
                ).also {
                    stage++
                }
            23 -> playerl(FaceAnim.FRIENDLY, "How would I do that?").also { stage++ }
            24 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Argh! Don't worry your little head about it, you're obviously incapable. Just bring me the base ingredients.",
                ).also {
                    stage++
                }
            25 -> playerl(FaceAnim.FRIENDLY, "What are these ingredients then?").also { stage++ }
            26 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Get me some Kwuarm and some red spider eggs. Oh yeah, an empty vial would be good too.",
                ).also {
                    stage++
                }
            27 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Actually I happen to be carrying all that stuff at the moment.",
                ).also { stage++ }
            28 ->
                sendDialogue(
                    player,
                    "You hand Jack the ingredients and quick as a flash he hands you back a vial of the rat poison.",
                ).also {
                    stage++
                }
            29 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "To poison the holes you'll need some bait. Use some cheese for this.",
                ).also { stage++ }
            30 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Pour some of the poison on the cheese and then place some of the poisoned cheese in each of the rat holes.",
                ).also {
                    stage++
                }
            31 -> playerl(FaceAnim.FRIENDLY, "Ok, can do.").also { stage++ }
            32 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Report back to me when you're done. I want to get this job wrapped as soon as possible.",
                ).also {
                    stage++
                }
            33 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Well that's one less rat hole. I had better finish the job off quick before they start to reproduce again.",
                ).also {
                    stage++
                }
            34 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Hmm, that should do the trick. Those rats should soon die off.",
                ).also { stage++ }
            35 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I've completed that task you set me. Now will you teach me?",
                ).also { stage++ }
            36 -> npcl(FaceAnim.FRIENDLY, "Give me a sec.").also { stage++ }
            37 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oh come on, I've gone and done a job for you, the least you could do is say thanks or something.",
                ).also {
                    stage++
                }
            38 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I'm upset at the moment. I just found my cat Pox, and the poor thing's half dead.",
                ).also {
                    stage++
                }
            39 -> playerl(FaceAnim.FRIENDLY, "Oh!").also { stage++ }
            40 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "They found Pox at the warehouse you were working at. He must have eaten some of the rats you poisoned.",
                ).also {
                    stage++
                }
            41 -> playerl(FaceAnim.FRIENDLY, "That's too bad. Good bye").also { stage++ }
            42 -> playerl(FaceAnim.FRIENDLY, "Can I help?").also { stage++ }
            43 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "Oh no. I feel somewhat responsible for this. Can I help in any way?",
                ).also { stage++ }
            44 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "There is an apothecary here in town; seek his help. Tell him that the poison was made from red spider eggs and kwuarm, and he should know what to do.",
                ).also {
                    stage++
                }
            45 -> npcl(FaceAnim.FRIENDLY, "Go quickly!").also { stage++ }
            46 ->
                options(
                    "Can you tell me where I would find ....",
                    "Can you tell me more about the pits?",
                    "Can you show me to the rat pits?",
                ).also { stage++ }
            47 ->
                when (buttonId) {
                    1 ->
                        options(
                            "Grimesquit and Phingspet",
                            "Jimmy Dazzler",
                            "Smokin' Joe",
                            "The face",
                            "Felkrash",
                        ).also { stage = 48 }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Can you tell me more about the pits?").also { stage = 59 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "Can you show me to the rat pits?").also { stage = 93 }
                }
            48 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Can you tell me where I could find Phingspet and Grimesquit?",
                        ).also {
                            stage =
                                49
                        }
                    2 ->
                        playerl(FaceAnim.HALF_ASKING, "Can you tell me where I would find Jimmy Dazzler?").also {
                            stage =
                                50
                        }
                    3 ->
                        playerl(FaceAnim.HALF_ASKING, "Can you tell me where I could find Smokin Joe?").also {
                            stage =
                                55
                        }
                    4 ->
                        playerl(FaceAnim.HALF_ASKING, "Can you tell me where I would find 'The Face'?").also {
                            stage =
                                56
                        }
                    5 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Can you tell me where I would find Felkrash?",
                        ).also { stage = 57 }
                }
            49 ->
                npcl(FaceAnim.FRIENDLY, "Oh the girls? You'll find them down in the sewers here in Varrock.").also {
                    stage =
                        END_DIALOGUE
                }
            50 -> npcl(FaceAnim.FRIENDLY, "That conceited popinjay?").also { stage++ }
            51 -> playerl(FaceAnim.FRIENDLY, "You could call him that. But where would I find him?").also { stage++ }
            52 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Probably cowering in his apartment at the back of the Flying Horse in Ardougne, praying that I don't go looking for him.",
                ).also {
                    stage++
                }
            53 -> playerl(FaceAnim.FRIENDLY, "So you two know each other?").also { stage++ }
            54 ->
                sendDialogue(player, "Jack shoots you a look that could rout a regiment of black knights.").also {
                    stage =
                        END_DIALOGUE
                }
            55 ->
                npcl(FaceAnim.FRIENDLY, "He should be in Keldagrim, the city of the dwarves.").also {
                    stage =
                        END_DIALOGUE
                }
            56 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "She'll be in Port Sarim, scaring people away from the entrance of the pits with her face.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            57 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Her nibs? She'll be in the pits of Port Sarim.",
                ).also { stage = END_DIALOGUE }
            58 ->
                if (!inInventory(player, Items.BOOK_6767)) {
                    npcl(FaceAnim.FRIENDLY, "Here take this, you might find it useful")
                    stage = 100
                } else {
                    npcl(FaceAnim.FRIENDLY, "Well what do you want to know?")
                    stage = 60
                }

            59 -> npcl(FaceAnim.FRIENDLY, "Well what do you want to know?").also { stage++ }
            60 ->
                options(
                    "Do you have any pointers for me and my cat?",
                    "What benefits does my cat get from training in the pits?",
                ).also {
                    stage++
                }
            61 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "Do you have any pointers for me and my cat?",
                        ).also { stage = 62 }
                    2 ->
                        playerl(FaceAnim.HALF_ASKING, "What benefits does my cat get from training in the pits?").also {
                            stage =
                                89
                        }
                }
            62 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You should wear an amulet of catspeak. It will allow you to organise tactics with your cat.",
                ).also {
                    stage++
                }
            63 -> playerl(FaceAnim.FRIENDLY, "What would be the best tactic to adopt then?").also { stage++ }
            64 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, that depends on your outlook, whether you're ready to risk your cat's life or not and how defensive or aggressive you want your cat to be.",
                ).also {
                    stage++
                }
            65 ->
                options(
                    "I will not risk my cat's life.",
                    "I am willing to risk my cat if it improves my chances.",
                ).also {
                    stage++
                }

            66 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.HALF_ASKING, "I will not risk my cat's life.").also { stage = 67 }
                    2 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "I am willing to risk my cat, if it increases my chances of victory.",
                        ).also {
                            stage =
                                78
                        }
                }
            67 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well if that's the case then your are left with two choices.",
                ).also { stage++ }
            68 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You can adopt an ultra defensive strategy, which increases your cat's hitpoints and defence, the drawback to this is that your cat will flee sooner.",
                ).also {
                    stage++
                }
            69 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Your other option is to adopt a less defensive strategy which will allow your cat to stay in the pit for longer.",
                ).also {
                    stage++
                }
            70 ->
                options(
                    "I think I would adopt a very defensive strategy.",
                    "I think I would adopt a less defensive strategy.",
                ).also {
                    stage++
                }
            71 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "I think I would adopt a very defensive strategy.",
                        ).also { stage = 72 }

                    2 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "I think I would adopt a less defensive strategy.",
                        ).also { stage = 76 }
                }
            72 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "In that case you should tell your cat, 'Just take care of yourself cat.'",
                ).also {
                    stage++
                }
            73 -> playerl(FaceAnim.FRIENDLY, "Thanks for your help.").also { stage++ }
            74 -> npcl(FaceAnim.FRIENDLY, "Do you have any other questions about the pits?").also { stage = 60 }
            75 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "In that case you should tell your cat, 'Stay in for as long as you can.'",
                ).also {
                    stage++
                }
            76 -> playerl(FaceAnim.FRIENDLY, "Thanks for your help.").also { stage++ }
            77 -> npcl(FaceAnim.FRIENDLY, "Do you have any other questions about the pits?").also { stage = 60 }
            78 -> npcl(FaceAnim.FRIENDLY, "In that case you are left with two choices.").also { stage++ }
            79 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You could go for all out attack which will boost your cat's attack and strength, but weaken it",
                ).also {
                    stage++
                }
            80 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "or go for a less aggressive stance which will increase your cat's strength and attack to a lesser degree, but carries no defensive penalties.",
                ).also {
                    stage++
                }
            81 ->
                options(
                    "I think I would adopt a very aggressive strategy.",
                    "I think I would adopt a less aggressive strategy.",
                ).also {
                    stage++
                }
            82 ->
                when (buttonId) {
                    1 ->
                        playerl(FaceAnim.HALF_ASKING, "I think I would adopt a very aggressive strategy.").also {
                            stage =
                                83
                        }
                    2 ->
                        playerl(
                            FaceAnim.HALF_ASKING,
                            "I think I would adopt a less defensive strategy.",
                        ).also { stage = 86 }
                }
            83 -> npcl(FaceAnim.FRIENDLY, "In that case you should tell your cat, 'Go berserk!'").also { stage++ }
            84 -> playerl(FaceAnim.FRIENDLY, "Thanks for your help.").also { stage++ }
            85 -> npcl(FaceAnim.FRIENDLY, "Do you have any other questions about the pits?").also { stage++ }
            86 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "In that case you should tell this to your cat, 'No guts, no glory!'",
                ).also { stage++ }
            87 -> playerl(FaceAnim.FRIENDLY, "Thanks for your help.").also { stage++ }
            88 -> npcl(FaceAnim.FRIENDLY, "Do you have any other questions about the pits?").also { stage = 60 }
            89 -> npcl(FaceAnim.FRIENDLY, "It depends on what kind of cat you have.").also { stage++ }
            90 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "In the Varrock pits, your cat will grow up faster by gaining more experience.",
                ).also {
                    stage++
                }
            91 -> playerl(FaceAnim.FRIENDLY, "Thanks for your help.").also { stage++ }
            92 -> npcl(FaceAnim.FRIENDLY, "Do you have any other questions about the pits?").also { stage = 60 }
            93 ->
                if (!isQuestComplete(player, Quests.RATCATCHERS)) {
                    npcl(FaceAnim.FRIENDLY, "Step away from that manhole, you've no business down there.").also {
                        stage =
                            END_DIALOGUE
                    }
                } else {
                    npcl(
                        FaceAnim.FRIENDLY,
                        "Argh you just don't listen do you? See that manhole yonder? Well climb down and you'll find the pits.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }

            100 -> {
                end()
                sendItemDialogue(player, Items.BOOK_6767, "You are handed a manual concerning the ratpits.")
                addItemOrDrop(player, Items.BOOK_6767)
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return HooknosedJackDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HOOKNOSED_JACK_2948)
    }
}
