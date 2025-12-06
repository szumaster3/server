package content.region.kandarin.piscatoris.quest.phoenix.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.IfTopic
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

@Initializable
class PhoenixDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val activityReward = getAttribute<Boolean>(player, GameAttributes.PHOENIX_LAIR_ACTIVITY_REWARD, false)

        if (!isQuestComplete(player, Quests.IN_PYRE_NEED)) {
            player(FaceAnim.SCARED, "H...hello?")
            stage = 0
        } else {
            if(activityReward) {
                removeAttribute(player, GameAttributes.PHOENIX_LAIR_ACTIVITY_REWARD)
                addItemOrDrop(player, Items.PHOENIX_QUILL_14616, 5)
                sendItemDialogue(player, Items.PHOENIX_QUILL_14616, "The phoenix plucks five large quills from its wings and gives them to you.")
                stage = 35
            } else {
                npcl(FaceAnim.FAMILIAR_NEUTRAL, "(Welcome back, ${player.username}. It is good to see you are enthusiastic, as ever. Heh heh heh!)")
                stage = 40
            }
        }

        return true
    }

    override fun handle(componentID: Int, buttonID: Int): Boolean {
        when (stage) {
            0 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(Hello, human. So, it is you whom I owe a great deal of", "thanks.)")
                stage++
            }

            1 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "Oh, it was nothing, really.")
                stage++
            }

            2 -> {
                npc(
                    FaceAnim.FAMILIAR_HAPPY,
                    "(Ah, such modesty. In that case, I shall withhold the",
                    "reward I planned for you, and send you away with my",
                    "thanks.)"
                )
                stage++
            }

            3 -> {
                player(
                    FaceAnim.FRIENDLY,
                    "Well, actually, it was an arduous trek through a",
                    "confusing and inhospitably hot cave, coupled with a",
                    "rather difficult Crafting task. And I burned my fingers",
                    "lighting the pyre."
                )
                stage++
            }

            4 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Heh heh heh! Well, say what you mean in the future,",
                    "not what makes you sound best, lest you really miss",
                    "out on what you're due!)"
                )
                stage++
            }

            5 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(In all seriousness, I would not have you walk out of",
                    "ere with merely my thanks.)"
                )
                stage++
            }

            6 -> {
                sendItemDialogue(
                    player,
                    Items.PHOENIX_QUILL_14616,
                    "The phoenix plucks five large quills from its wings and gives them to you."
                )
                addItemOrDrop(player, Items.PHOENIX_QUILL_14616, 5)
                stage++
            }

            7 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(Those feathers are, as you plainly saw, a part of me.)")
                stage++
            }

            8 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(When combined with other ingredients and energies,",
                    "they can be used to summon me to your location",
                    "temporarily.)"
                )
                stage++
            }

            9 -> {
                player(FaceAnim.FRIENDLY, "I can use them to make a Summoning pouch, in other", "words.")
                stage++
            }

            10 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Correct. So, Pikkupstix taught you the ways of the",
                    "summoner already. How fortunate!)"
                )
                stage++
            }

            11 -> {
                player(FaceAnim.HALF_ASKING, "How do you know Pikkupstix?")
                stage = 14
            }

            14 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "Well, in the long time I have lived on this world...let us",
                    "just say that I been around."
                )
                stage++
            }

            15 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Now, on the second part of your reward. I grant",
                    "you the right to challenge me.)"
                )
                stage++
            }

            16 -> {
                player(FaceAnim.SCARED, "Huh?")
                stage++
            }

            17 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Once a day, you may enter my lair, brave your way",
                    "through it and challenge me in combat, starting from",
                    "now.)"
                )
                stage++
            }

            18 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(If you defeat me, I will give you five more quills,",
                    "plucked from me, by me.)"
                )
                stage++
            }

            19 -> {
                player(
                    FaceAnim.FRIENDLY,
                    "Forgive me, but I fail to see the advantage this presents",
                    "you. Why would you offer me this as a reward?"
                )
                stage++
            }

            20 -> {
                npc(
                    FaceAnim.FAMILIAR_SAD,
                    "(You, my friend, will be completing my rebirth ritual.",
                    "Each time you defeat me, my life will extend further.)"
                )
                stage++
            }

            21 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(My recent near-death has somewhat shaken me. I wish",
                    "to stay in my lair for a while.)"
                )
                stage++
            }

            22 -> {
                npc(
                    FaceAnim.FAMILIAR_SAD,
                    "(I need to keep at least one friend close at all times to",
                    "ensure my continued existence.)"
                )
                stage++
            }

            23 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(" + RED + "You must be an accomplished Slayer before you can</col>",
                    RED + "challenge me, though</col>. Our arrangement is no good to",
                    "either of us if you lose.)"
                )
                stage++
            }

            24 -> {
                player(FaceAnim.FRIENDLY, "I see; and in return for my friendship, I gain yours.")
                stage++
            }

            25 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(An agreeable situation, is it not?)")
                stage++
            }

            26 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Anyway, my friend, I fear I must ask you to leave me",
                    "for a while. I am still quite fatigued from my ordeal,",
                    "and would like some peace and quiet to rest.)"
                )
                stage++
            }

            27 -> {
                player(FaceAnim.FRIENDLY, "I understand. The priest will want to know you're", "alright, anyway.")
                stage++
            }

            28 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(Priest? What priest?)")
                stage++
            }

            29 -> {
                player(
                    FaceAnim.FRIENDLY,
                    "There is a priest who has been studying you unseen",
                    "for quite some time. If it was not for him, I would not",
                    "have known the ritual and would have been powerless to",
                    "help you."
                )
                stage++
            }

            30 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Oh, THAT priest. While I am aware he has been",
                    "studying me, he has not done so unseen.)"
                )
                stage++
            }

            31 -> {
                npc(
                    FaceAnim.FAMILIAR_NEUTRAL,
                    "(Thank him for the shrine for me. Also, ask him when I",
                    "can have my trinkets back. Heh heh heh!)"
                )
                stage++
            }

            32 -> {
                player(FaceAnim.FRIENDLY, "I'll do that for you. Rest well; I'm sure you'll be seeing", "me soon.")
                stage++
            }

            33 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(Farewell, " + player.username + ".)")
                stage++
            }

            34 -> {
                player(FaceAnim.FRIENDLY, "Farewell!")
                setVarbit(player, Vars.VARBIT_QUEST_IN_PYRE_NEED_PROGRESS_5761, 10, true)
                stage = END_DIALOGUE
            }

            35 -> {
                npcl(FaceAnim.FAMILIAR_NEUTRAL, "(Welcome back, " + player.username + ". It is good to see you are enthusiastic, as ever. Heh heh heh!)")
                stage = 41
            }

            40 -> {
                player("Hello to you, too. Thanks for the workout!")
                stage++
            }

            41 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(Was there something you wanted to ask about?)")
                stage++
            }

            42 -> showTopics(
                Topic<Any?>("Tell me about summoning you again.", 105, true),
                Topic<Any?>("If you knew about the priest...", 98, true),
                IfTopic<Any?>("", 72, inInventory(player, Items.PHOENIX_EGGLING_14626, 1), true),
                IfTopic<Any?>("Who was Si'morgh?", 48, getAttribute(player, GameAttributes.PHOENIX_LAIR_VISITED, false), true),
                Topic<Any?>("Not really, I just came to say hello.", 43, true)
            )

            43 -> {
                player("Not really. I just came to say hello.")
                stage++
            }

            44 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(Hello!)")
                stage++
            }

            45 -> {
                player("Hello!")
                stage++
            }

            46 -> {
                npc(FaceAnim.FAMILIAR_NEUTRAL, "(And goodbye!)")
                stage++
            }

            47 -> {
                player("Goodbye!")
                stage = END_DIALOGUE
            }

            48 -> {
                player(FaceAnim.HALF_ASKING, "Who was Si'morgh? You don't have to tell me if you don't want to.")
                stage++
            }

            49 -> {
                npcl(FaceAnim.FAMILIAR_SAD, "(It happened a millennia ago now. You see, I hatched from a lucky egg. My parent was a solo phoenix that stayed alive for a mere two cycles. One egg - mine - was already fertile, and when my parent died, the other egg, Si'morgh, became fertile.)")
                stage++
            }

            50 -> {
                npcl(FaceAnim.FAMILIAR_SAD, "(We hatched together, brother and sister for all intents and purposes, and used to live in this very lair together.)")
                stage++
            }

            51 -> {
                playerl(FaceAnim.FRIENDLY, "Wow, the chances of that are very slim indeed! It must have been nice to have grown up with someone.")
                stage++
            }

            52 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Yes, it was. Si'morgh learned a lot faster than I did. He was always looking out for me, finding me food and protecting me from danger as I grew up. We were about the age at which you could call a phoenix a teenager when it happened.)")
                stage++
            }

            53 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(There was a strange new creature outside of our lair. Curiosity led me to leave the safety of the cave and investigate.)")
                stage++
            }

            54 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I had never seen a dragon before.)")
                stage++
            }

            55 -> {
                player("It was a dragon?")
                stage++
            }

            56 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Si'morgh was out looking for something to eat at the time. The dragon attacked me. I tried to defend myself, but I was overpowered. At the last moment, as the dragon was about to land the killing blow, Si'morgh returned.)")
                stage++
            }

            57 -> {
                npcl(
                    FaceAnim.FAMILIAR_HAPPY,
                    "(He threw himself at the dragon to protect me, and screamed at me to run. I got away as fast as I could, hiding in our lair, where the huge dragon could not follow. Hours passed, and Si'morgh did not return.)"
                )
                stage++
            }

            58 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(That night, I took a cautious peek out of the cave. I couldn't see or hear the dragon anywhere, so I went out looking for Si'morgh.)")
                stage++
            }

            59 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I found him. Bits of him, at least.)")
                stage++
            }

            60 -> {
                player("That's terrible.")
                stage++
            }

            61 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(He gave his life in exchange for mine, but he left me alone.)")
                stage++
            }

            62 -> {
                player("So, that's why your lair is so well-defended.")
                stage++
            }

            63 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Yes. I had to grow up quickly, as there was no Si'morgh to look after me. Essentially, I had to fend for myself, so the first thing I did was to fortify my home.)")
                stage++
            }

            64 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I started becoming more adventurous when the loneliness got to me. After all, I was used to company, and living with none was quite difficult.)")
                stage++
            }

            65 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I started collecting trinkets to keep myself occupied, leaving my lair for centuries at a time and returning only to reborn. I became a people-watcher - watching the races as they developed with time.)")
                stage++
            }

            66 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(You know the rest of my story, " + player.username + ", because you have helped to continue writing it.)")
                stage++
            }

            67 -> {
                playerl(FaceAnim.FRIENDLY, "Thank you for telling me about this. I know it must have been difficult for you.")
                stage++
            }

            68 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(It is no bother. In truth, it is nice to have someone to talk to - someone to call a friend. Thank you for listening, " + player.username + ".)")
                stage++
            }

            69 -> {
                player("It was my pleasure.")
                stage++
            }

            70 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I do not wish to be rude, friend, but may I have some time alone with my thoughts?)")
                stage++
            }

            71 -> {
                player("Of course. I'll see you again soon.")
                stage = END_DIALOGUE
            }

            72 -> {
                player(FaceAnim.NEUTRAL, "I got lost on the way out, and-")
                stage++
            }

            73 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(Found my egg chamber?)")
                stage++
            }

            74 -> {
                player(FaceAnim.NEUTRAL, "Yes. One of the eggs hatched.")
                stage++
            }

            75 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(Yes, I know. This is extremely rare. I imagine you have many questions. Ask away.)")
                stage++
            }

            76 -> {
                player(FaceAnim.NEUTRAL, "Okay. Firstly, are you not upset that I took the eggling with me?")
                stage++
            }

            77 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(Not at all. You helped me in my time of need. I know you'll take good care of my eggling until such a time that it can take care of itself.)")
                stage++
            }

            78 -> {
                player(FaceAnim.FRIENDLY, "What a relief!")
                stage++
            }

            79 -> {
                player(FaceAnim.NEUTRAL, "I have found myself wondering how the egg became fertile. You are the only phoenix alive at the moment, right?")
                stage++
            }

            80 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(Barring the eggling you've adopted for me and any others that may hatch, that is true, yes. The matter of reproduction in my species...differs from the norm, somewhat.)")
                stage++
            }

            81 -> {
                player(FaceAnim.THINKING, "Care to elaborate?")
                stage++
            }

            82 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(Certainly! I had no idea you were such an avid appreciator of nature.)")
                stage++
            }

            83 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(After a phoenix performs its rebirth ritual, its ashes are stored within a magical egg. I perform this part of my ritual after you have left; I need some rest before I can utilize the necessary magicks to perform the act.)")
                stage++
            }

            84 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(I keep all of these eggs in my egg chamber. They are mostly dormant at the moment, though.)")
                stage++
            }

            85 -> {
                npc(FaceAnim.FAMILIAR_HAPPY, "(The eggs are a sort of contingency plan. You see, the phoenixes are linked. It is a link similar to that which I share with my guardians; a sort of shared consciousness.)")
                stage++
            }

            86 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(If all the phoenixes were to die out at once, one of the eggs would become magically fertile, ensuring that, as long as we complete our rebirth ritual at least once before meeting our end, there will always be an eggling phoenix to take over when we...pass on.)")
                stage++
            }

            87 -> {
                player(FaceAnim.AMAZED, "Wow! That's amazing!")
                stage++
            }

            88 -> {
                player(FaceAnim.SCARED, "Hold on: you're not dead! Why did the egg hatch?")
                stage++
            }

            89 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(There is a very small chance that an egg will become magically fertile without the death of all phoenixes. Seeing as we phoenixes perform our rituals once every five hundred years - under normal circumstances - this means the population is usually one.)")
                stage++
            }

            90 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(The most phoenixes alive at once, since my birth, has been two.)")
                stage++
            }

            91 -> {
                npcl(FaceAnim.FAMILIAR_SAD, "(Ah, Si'morgh. I miss you so.)")
                stage++
            }

            92 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Anyway, to make a long story short, you were simply very lucky to find a hatching egg.)")
                stage++
            }

            93 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(${player.username}, promise me something.)")
                stage++
            }

            94 -> {
                player(FaceAnim.FRIENDLY, "What would you ask of me?")
                stage++
            }

            95 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Let my child see the world, but teach it to fear danger. I don't want it to end up dead like Si'morgh, or a frightened hermit like me.)")
                stage++
            }

            96 -> {
                player(FaceAnim.NEUTRAL, "I will try my best.")
                stage++
            }

            97 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Thank you, " + player.username + ".)")
                stage++
            }

            98 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Why was he not slain like the other intruders in my lair?)")
                stage++
            }

            99 -> {
                player(FaceAnim.FRIENDLY, "Erm...well, yes.")
                stage++
            }

            100 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I must admit, I did regard him with cautions for a while; but in all his trips into my lair, he never did anything worth stopping.)")
                stage++
            }

            101 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I found him most curious, carefully avoiding my guardians with all his stealth and guile. I knew he was there, but I was holding them back.)")
                stage++
            }

            102 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(In all his visits, all he ever did was look around and take notes; watch me as I went about my business; and study my guardians going about theirs.)")
                stage++
            }

            103 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I did take exception to him taking away my antiques collection, I suppose, but he provided me with a lovely shrine and tapestry in exchange. So, I'm content. Heh heh.)")
                stage++
            }

            104 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Was there something you wanted to ask about?)")
                stage = 42
            }

            105 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(The feathers I gave you are a part of me. You can use them to infuse a summoning pouch with some of my essence.)")
                stage++
            }

            106 -> {
                player(FaceAnim.FRIENDLY, "Some of your essence?")
                stage++
            }

            107 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(Indeed. The energy from the pouch creates a spectral version of my, but the essence contained within it links it to my mind.)")
                stage++
            }

            108 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(You see, there is only one of me, but using the summoning pouches and spectral essences means I can be in many places at once.)")
                stage++
            }

            109 -> {
                playerl(FaceAnim.FRIENDLY, "I think I understand. What powers do you have in this form?")
                stage++
            }

            110 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(I will still be a powerful magical fighter, and will also have the power to conjure up ashes and hurl them at your foe, blinding them temporarily.)")
                stage++
            }

            111 -> {
                npcl(FaceAnim.FAMILIAR_HAPPY, "(When these ashes fall to the floor, use my summoning scroll on them. This will be particularly useful if I am running low on life points. Heh heh heh!)")
                stage++
            }

            112 -> {
                player(FaceAnim.FRIENDLY, "Okay, thanks for the information.")
                stage = 104
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.PHOENIX_8548)
    }
}
