package content.region.morytania.quest.druidspirit

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.splitLines
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class FillimanTarlockDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var questStage = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val quest = player.questRepository.getQuest(Quests.NATURE_SPIRIT)
        questStage = quest.getStage(player)

        if (questStage > 10 && !inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            sendDialogue(player, "This spirit seems too ethereal to communicate with.")
            setQuest(15)
            return false
        }

        when (questStage) {
            10, 15 -> sendDialogue("A shifting apparition appears in front of you.").also { stage = 1000 }
            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, hello there, do you still think I'm dead? It's hard to see how I could be dead when I'm still in the world. I can see everything quite clearly. And nothing of what you say reflects the truth.",
                )
            25 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, hello... Sorry, you've caught me at a bad time, it's just that I've had a sign you see and I need to find my journal.",
                ).also {
                    stage =
                        7
                }
            30 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Thanks for the journal, I've been reading it. It looks like I came to a violent and bitter end but that's not really important. I just have to figure out what I am going to do now?",
                ).also {
                    stage =
                        14
                }
            35 -> npcl(FaceAnim.NEUTRAL, "Hello there, have you been blessed yet?").also { stage = 60 }
            45 -> {
                if (inInventory(player, Items.MORT_MYRE_FUNGUS_2970)) {
                    npcl(FaceAnim.NEUTRAL, "Did you manage to get something from nature?").also { stage = 80 }
                } else {
                    playerl(FaceAnim.NEUTRAL, "Hello, I've been blessed but I don't know what to do now.").also {
                        stage =
                            70
                    }
                }
            }

            50 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    " Hello again! I don't suppose you've found out what the other components of the Nature spell are have you?",
                ).also {
                    stage =
                        90
                }

            else -> npcl(FaceAnim.NEUTRAL, ".......").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                if (inInventory(player, Items.MIRROR_2966)) {
                    sendDialogue("You use the mirror on the spirit", "of the dead Filliman Tarlock.").also { stage++ }
                } else {
                    playerl(FaceAnim.NEUTRAL, "Yes, I do think you're dead and I'll prove it somehow.").also {
                        stage = 1002
                    }
                }

            1 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "Here take a look at this, perhaps you can see that you're utterly transparent now!",
                ).also { stage++ }

            2 -> sendDialogue("The spirit of Filliman reaches forwards and takes the mirror.").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, that is the most peculiar thing I've ever experienced. Strange how well it reflects the stagnant swamp behind me, but there is nothing of my own visage apparent.",
                ).also { stage++ }

            4 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "That's because you're dead! Dead as a door nail... Deader in fact... You bear a remarkable resemblance to wormbait! Err... No offence...",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I think you might be right my friend, though I still feel very much alive. It is strange how I still come to be here and yet I've not turned into a Ghast.",
                ).also { stage++ }

            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    " It must be a sign... Yes a sign... I must try to find out what it means. Now, where did I put my journal?",
                ).also { stage++ }

            7 -> {
                if (!inInventory(player, Items.JOURNAL_2967)) {
                    playerl(FaceAnim.NEUTRAL, "Where did you put it?").also {
                        stage++
                        setQuest(25)
                    }
                } else {
                    sendDialogue("You give the journal to Filliman Tarlock").also {
                        removeItem(player, Items.JOURNAL_2967, Container.INVENTORY)
                        stage = 10
                        setQuest(30)
                    }
                }
            }

            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, if I knew that, I wouldn't still be looking for it. However, I do remember something about a knot? Perhaps I was meant to tie a knot or something?",
                ).also { stage = END_DIALOGUE }

            10 -> playerl(FaceAnim.NEUTRAL, "Here, I found this, maybe you can use it?").also { stage++ }
            11 -> npcl(FaceAnim.FRIENDLY, "My journal! That should help to collect my thoughts.").also { stage++ }
            12 ->
                sendDialogue(
                    "~ The spirit starts leafing through the journal. ~",
                    "~ He seems quite distant as he regards the pages. ~",
                    "~ After some time the druid faces you again. ~",
                ).also { stage++ }

            13 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "It's all coming back to me now. It looks like I came to a violent and bitter end but that's not important now. I just have to figure out what I am going to do now?",
                ).also { stage++ }

            14 ->
                options(
                    "Being dead, what options do you think you have?",
                    "So, what's your plan?",
                    "Well, good luck with that.",
                    "How can I help?",
                    "Ok thanks.",
                ).also { stage++ }

            15 ->
                when (buttonId) {
                    1 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Being dead, what options do you think you have? I'm not trying to be rude or anything, but it's not like you have many options is it? I mean, it's either up or down for you isn't it?",
                        ).also { stage = 20 }

                    2 -> playerl(FaceAnim.NEUTRAL, "So, what's your plan?").also { stage = 30 }
                    3 -> playerl(FaceAnim.NEUTRAL, "Well, good luck with that.").also { stage = 40 }
                    4 -> playerl(FaceAnim.NEUTRAL, "How can I help?").also { stage = 50 }
                    5 -> playerl(FaceAnim.NEUTRAL, "Ok thanks.").also { stage = END_DIALOGUE }
                }

            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hmm, well you're a poetic one aren't you. Your material world logic stands you in good stead... If you're standing in the material world...",
                ).also { stage = 14 }

            30 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "In my former incarnation I was Filliman Tarlock, a great druid of some power. I spent many years in this place, which was once a forest and I would wish to protect it as a nature spirit.",
                ).also { stage = 14 }

            40 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Won't you help me to become a nature spirit? I could really use your help!",
                ).also { stage = 14 }

            50 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Will you help me to become a nature spirit? The directions for becoming one are a bit vague, I need three things but I know how to get one of them. Perhaps you can help collect the rest?",
                ).also { stage++ }

            51 -> playerl(FaceAnim.NEUTRAL, "I might be interested, what's involved?").also { stage++ }
            52 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, the book says, that I need, and I quote:- 'Something with faith', 'something from nature' and the 'spirit-to-become' freely given'. Hmm, I know how to get something from nature.",
                ).also { stage++ }

            53 -> playerl(FaceAnim.NEUTRAL, "Well, that does seem a bit vague.").also { stage++ }
            54 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Hmm, it does and I could understand if you didn't want to help. However, if you could perhaps at least get the item from nature, that would be a start. Perhaps we can figure out the rest as we go along.",
                ).also { stage++ }

            55 ->
                sendDialogue(*splitLines("The druid produces a small sheet of papyrus with some writing on it.")).also {
                    addItemOrDrop(
                        player,
                        Items.DRUIDIC_SPELL_2968,
                    )
                    setQuest(35)
                    stage++
                }

            56 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "This spell needs to be cast in the swamp after you have been blessed. I'm afraid you'll need to go to the temple to the North and ask a member of the clergy to bless you.",
                ).also { stage++ }

            57 -> playerl(FaceAnim.NEUTRAL, "Blessed, what does that do?").also { stage++ }
            58 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "It is required if you're to cast this druid spell. Once you've cast the spell, you should find something from nature. Bring it back to me and then we'll try to figure out the other things we need.",
                ).also { stage = END_DIALOGUE }

            60 -> playerl(FaceAnim.NEUTRAL, "No, not yet.").also { stage++ }
            61 -> npcl(FaceAnim.NEUTRAL, "Well, hurry up!").also { stage++ }
            62 ->
                if (inInventory(player, Items.DRUIDIC_SPELL_2968) || inBank(player, Items.DRUIDIC_SPELL_2968)) {
                    end()
                } else {
                    playerl(FaceAnim.NEUTRAL, "Could I have another bloom scroll please?").also { stage++ }
                }

            63 -> npcl(FaceAnim.NEUTRAL, "Sure, but please look after this one.").also { stage++ }
            64 ->
                sendDialogue("The spirit of Filliman Tarlock gives you another bloom spell.").also {
                    addItemOrDrop(
                        player,
                        Items.DRUIDIC_SPELL_2968,
                    )
                    stage = END_DIALOGUE
                }

            70 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, you need to bring 'something from nature', 'something with faith' and 'something of the spirit-to- become freely given.'",
                ).also { stage++ }

            71 -> playerl(FaceAnim.NEUTRAL, "Yeah, but what does that mean?").also { stage++ }
            72 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Hmm, it is a conundrum, however, if you use that spell I gave you, you should be able to get from nature. Once you have that, we may be puzzle the rest out.",
                ).also { stage++ }

            73 ->
                if (!inInventory(player, Items.DRUIDIC_SPELL_2968) && !inBank(player, Items.DRUIDIC_SPELL_2968)) {
                    playerl(FaceAnim.NEUTRAL, "Could I have another bloom scroll please?").also { stage++ }
                } else {
                    end()
                }

            74 -> npcl(FaceAnim.NEUTRAL, "Sure, but please look after this one.").also { stage++ }
            75 ->
                sendDialogue(
                    "The spirit of Filliman Tarlock gives you",
                    "another bloom spell.",
                ).also {
                    addItem(player, Items.DRUIDIC_SPELL_2968)
                    stage = END_DIALOGUE
                }

            80 -> sendDialogue("You show the fungus to Filliman.").also { stage++ }
            81 -> playerl(FaceAnim.NEUTRAL, "Yes, I have a fungus here that I picked.").also { stage++ }
            82 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Wonderful, the mushroom represents 'something from nature'. Now we need to work out what the other components of the spell are!",
                ).also {
                    stage = 90
                    setQuest(50)
                }

            90 ->
                options(
                    "What are the things that are needed?",
                    "What should I do when I have those things?",
                    "I think I've solved the puzzle!",
                    "Could I have another bloom scroll please?",
                    "Ok, thanks.",
                ).also { stage++ }

            91 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.NEUTRAL, "What are the things that are needed?").also { stage = 100 }
                    2 -> playerl(FaceAnim.NEUTRAL, "What should I do when I have those things?").also { stage = 110 }
                    3 -> playerl(FaceAnim.FRIENDLY, "I think I've solved the puzzle!").also { stage = 120 }
                    4 -> playerl(FaceAnim.FRIENDLY, "Can I have another bloom scroll please?").also { stage = 130 }
                    5 -> playerl(FaceAnim.NEUTRAL, "Ok, thanks.").also { stage = END_DIALOGUE }
                }

            100 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The three things are: 'Something with faith', 'something from nature' and 'something of the spirit-to-become freely given'.",
                ).also { stage++ }

            101 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    " Ok, and 'something from nature' is the mushroom from the bloom spell you gave me?",
                ).also { stage++ }

            102 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, that's correct, that seems right to me. The other things we need are 'something with faith' and 'something of the spirit-to-become freely given.",
                ).also { stage++ }

            103 -> playerl(FaceAnim.NEUTRAL, "Do you have any ideas what those things are?").also { stage++ }
            104 -> npcl(FaceAnim.HALF_GUILTY, "I'm sorry my friend, but I do not.").also { stage = 90 }
            110 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "It says,.. 'to arrange upon three rocks around the spirit-to-become...'. Then I must cast a spell. As you can see, I've already placed the rocks.",
                ).also { stage++ }

            111 -> playerl(FaceAnim.NEUTRAL, "Can we just place the components on any rock?").also { stage++ }
            112 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, the only thing the journal says is that 'something with faith stands south of the spirit-to-become', but I'm so confused now I don't really know what that means.",
                ).also { stage = 90 }

            120 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Oh really.. Have you placed all the items on the stones? Ok, well, let's try!",
                ).also { stage++ }

            121 -> sendDialogue("~ The druid attempts to cast a spell. ~").also { stage++ }
            122 -> {
                animate(npc, 812)
                if (NSUtils.hasPlacedCard(player) && NSUtils.hasPlacedFungus(player) && NSUtils.onStone(player)) {
                    end()
                    player.lock()
                    val locations =
                        arrayOf(
                            Location.create(3439, 3336, 0),
                            Location.create(3441, 3336, 0),
                            Location.create(3440, 3335, 0),
                        )
                    repeat(3) { i ->
                        spawnProjectile(locations[i], Location.create(3440, 3336, 0), 268, 0, 35, 0, 100, 20)
                    }
                    submitIndividualPulse(
                        player,
                        object : Pulse(4) {
                            override fun pulse(): Boolean {
                                sendNPCDialogue(
                                    player,
                                    npc.originalId,
                                    "Aha, everything seems to be in place! You can come through now into the grotto for the final section of my transformation.",
                                )
                                setQuest(55)
                                unlock(player)
                                return true
                            }

                            override fun stop() {
                                visualize(
                                    npc,
                                    -1,
                                    Graphics(266, 80),
                                )
                                super.stop()
                            }
                        },
                    )
                } else {
                    npcl(
                        FaceAnim.NEUTRAL,
                        "Hmm, something still doesn't seem right. I think we need something more before we can continue.",
                    )
                }
                stage = END_DIALOGUE
            }

            130 -> {
                if (inInventory(player, Items.DRUIDIC_SPELL_2968) || inBank(player, Items.DRUIDIC_SPELL_2968)) {
                    npcl(FaceAnim.NEUTRAL, "No, you've already got one!").also { stage = END_DIALOGUE }
                } else {
                    npcl(FaceAnim.NEUTRAL, "Sure, but look after this one.")
                    addItem(player, Items.DRUIDIC_SPELL_2968)
                    stage = END_DIALOGUE
                }
            }

            1000 -> playerl(FaceAnim.HALF_ASKING, "Hello?").also { stage++ }
            1001 -> {
                if (inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
                    npcl(
                        FaceAnim.EXTREMELY_SHOCKED,
                        "Oh, I understand you! At last, someone who doesn't just mumble. I understand what you're saying!",
                    ).also { stage++ }
                } else {
                    npcl(FaceAnim.HALF_GUILTY, "OooOOoOOoOOOOo.").also { stage = 1033 }
                }
            }

            1002 ->
                options(
                    "I'm wearing an amulet of ghost speak!",
                    "How long have you been a ghost?",
                    "What's it like being a ghost?",
                    "Ok, thanks.",
                ).also { stage++ }

            1003 ->
                when (buttonId) {
                    1 ->
                        playerl(FaceAnim.NEUTRAL, "I'm wearing an amulet of ghost speak!").also {
                            stage = 1010
                            setQuest(20)
                        }

                    2 ->
                        playerl(FaceAnim.NEUTRAL, "How long have you been a ghost?").also {
                            stage = 1020
                            setQuest(20)
                        }
                    3 ->
                        playerl(FaceAnim.NEUTRAL, "What's it like being a ghost?").also {
                            stage = 1030
                            setQuest(20)
                        }
                    4 -> playerl(FaceAnim.NEUTRAL, "Ok, thanks.").also { stage = END_DIALOGUE }
                }

            1010 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Why you poor fellow, have you passed away and you want to send a message back to a loved one?",
                ).also { stage++ }

            1011 -> playerl(FaceAnim.HALF_THINKING, "Err.. Not exactly...").also { stage++ }
            1012 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You have come to haunt my dreams until I pass on your message to a dearly loved one. I understand. Pray, tell me who would you like me to pass a message on to?",
                ).also { stage++ }

            1013 -> playerl(FaceAnim.NEUTRAL, "Ermm, you don't understand... It's just that...").also { stage++ }
            1014 -> npcl(FaceAnim.HALF_GUILTY, "Yes!").also { stage++ }
            1015 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "Well please don't be upset or anything... But you're the ghost!",
                ).also { stage++ }

            1016 ->
                npcl(FaceAnim.HALF_GUILTY, "Don't be silly now! That in no way reflects the truth!").also {
                    stage = 1002
                }

            1020 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "What?! Don't be preposterous! I'm not a ghost! How could you say something like that?",
                ).also { stage++ }

            1021 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "But it's true, you're a ghost... well, at least that is to say, you're sort of not alive anymore.",
                ).also { stage++ }

            1022 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Don't be silly, I can see you. I can see that tree. If I were dead, I wouldn't be able to see anything. What you say just doesn't reflect the truth. You'll have to try harder to put one over on me!",
                ).also { stage = 1002 }

            1030 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Oh, it's quite.... Oh... Trying to catch me out were you! Anyone can clearly see that I am not a ghost!",
                ).also { stage++ }

            1031 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "But you are a ghost, look at yourself! I can see straight through you! You're as dead as this swamp! Err... No offence or anything...",
                ).also { stage++ }

            1032 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "No I won't take offence because I'm not dead and I'm afraid you'll have to come up with some pretty conclusive proof before I believe it. What a strange dream this is.",
                ).also { stage = 1002 }

            1033 -> sendDialogue(player, "You cannot understand the ghost.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FILLIMAN_TARLOCK_1050)
    }

    fun setQuest(stage: Int) {
        player.questRepository.getQuest(Quests.NATURE_SPIRIT).setStage(player, stage)
    }
}

class FillimanCompletionDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        npc = NPC(NPCs.FILLIMAN_TARLOCK_1050)
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, hello there again. I was just enjoying the grotto. Many thanks for your help, I couldn't have become a Spirit of nature without you.",
                ).also { stage++ }

            1 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "I must complete the transformation now. Just stand there and watch the show, apparently it's quite good!",
                ).also { stage++ }

            2 -> {
                end()
                lock(player!!, 10)
                submitWorldPulse(CompleteSpellPulse(player!!))
            }
        }
    }
}
