package content.region.asgarnia.dialogue.portsarim

import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class RedbeardFrankDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var replacementReward = false
    private val level = 0
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.PIRATES_TREASURE)
        npc("Arr, Matey!")
        stage = 0
        replacementReward = Diary.canReplaceReward(player, DiaryType.FALADOR, level)
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (quest!!.getStage(player) == 20 &&
                    !player.inventory.containsItem(KEY) &&
                    !player.bank.containsItem(KEY)
                ) {
                    player("I seem to have lost my chest key...")
                    stage = 700
                    return true
                }
                if (quest!!.getStage(player) == 10) {
                    npc("Have ye brought some rum for yer ol' mate Frank?")
                    stage = 20
                }
                if (quest!!.getStage(player) == 0) {
                    options(
                        "I'm in search of treasure.",
                        "Arr!",
                        "Do you have anything for trade?",
                        "Tell me about the Falador Achievement Diary.",
                    )
                    stage = 11
                }
                options("Arr!", "Do you have anything for trade?", "Tell me about the Falador Achievement Diary.")
                stage = 10
            }

            10 ->
                when (buttonId) {
                    1 -> {
                        player("Arr!")
                        stage = 12
                    }

                    2 -> {
                        player("Do you have anything for trade?")
                        stage = 13
                    }

                    3 -> {
                        player("Tell me about the Falador Achievement Diary.")
                        stage = 80
                    }
                }

            11 ->
                when (buttonId) {
                    1 -> {
                        player("I'm in search of treasure.")
                        stage = 40
                    }

                    2 -> {
                        player("Arr!")
                        stage = 12
                    }

                    3 -> {
                        player("Do you have anything for trade?")
                        stage = 13
                    }

                    4 -> {
                        player("Tell me about the Falador Achievement Diary.")
                        stage = 80
                    }
                }

            12 -> {
                npc("Arr!")
                stage = 0
            }

            13 -> {
                npc("Nothin' at the moment, but then again the Customs", "Agents are on the warpath right now.")
                stage = 999
            }

            20 ->
                if (!player.inventory.containsItem(KARAMJAN_RUM)) {
                    player("No, not yet.")
                    stage = 21
                } else {
                    player("Yes, I've got some.")
                    stage = 31
                }

            21 -> {
                npc("Not suprising, tis no easy task to get it off Karamja.")
                stage = 22
            }

            22 -> {
                player("What do you mean?")
                stage = 23
            }

            23 -> {
                npc(
                    "The Customs office has been clampin' down on the",
                    "export of spirits. You seem like a resourceful young lad,",
                    "I'm sure ye'll be able to find a way to slip the stuff past",
                    "them.",
                )
                stage = 24
            }

            24 -> {
                player("Well I'll give it another shot.")
                stage = 25
            }

            999 -> end()
            31 -> {
                npc(
                    "Now a deal's a deal, I'll tell ye about the treasure. I",
                    "used to serve under a pirate captain called One-Eyed",
                    "Hector.",
                )
                stage = 32
            }

            32 -> {
                npc(
                    "Hector were very successful and became very rich.",
                    "But about a year ago we were boarded by the Customs",
                    "and Excise Agents.",
                )
                stage = 33
            }

            33 -> {
                npc(
                    "Hector were killed along with many of the crew, I were",
                    "one of the few to escape and I escaped with this.",
                )
                stage = 34
            }

            34 ->
                if (player.inventory.remove(KARAMJAN_RUM)) {
                    if (!player.inventory.add(KEY)) {
                        GroundItemManager.create(KEY, player)
                    }
                    quest!!.setStage(player, 20)
                    interpreter.sendItemMessage(KEY.id, "Frank happily takes the rum... ... and hands you a key")
                    stage = 35
                }

            35 -> {
                npc(
                    "This be Hector's key. I believe it opens his chest in his",
                    "old room in the Blue Moon Inn in Varrock.",
                )
                stage = 36
            }

            36 -> {
                npc("With any luck his treasure will be in there.")
                stage = 37
            }

            37 -> {
                player("Ok thanks, I'll go and get it.")
                stage = 999
            }

            40 -> {
                npc(
                    "Arr, treasure you be after eh? Well I might be able to",
                    "tell you where to find some... For a price...",
                )
                stage = 41
            }

            41 -> {
                player("What sort of price?")
                stage = 42
            }

            42 -> {
                npc("Well for example if you go and get me a bottle of rum...", "Not just any rum mind...")
                stage = 43
            }

            43 -> {
                npc("I'd like some rum made on Karamja Island. There's no", "rum like Karamaja Rum!")
                stage = 44
            }

            44 -> {
                options("Ok, I will bring you some rum.", "Not right now.")
                stage = 45
            }

            45 ->
                when (buttonId) {
                    1 -> {
                        quest!!.start(player)
                        player("Ok, I will bring you some rum.")
                        stage = 47
                    }

                    2 -> {
                        player("Not right now.")
                        stage = 46
                    }
                }

            46 -> {
                npc("Fair enough. I'll still be here and thirsty whenever you", "feel like helpin' out.")
                stage = 999
            }

            47 -> {
                npc("Yer a saint, although it'll take a miracle to get it off", "Karamja.")
                stage = 48
            }

            48 -> {
                player("What do you mean?")
                stage = 49
            }

            49 -> {
                npc(
                    "The Customs office has been clampin' down on the",
                    "export of spirits. You seem like a resourceful young lad,",
                    "I'm sure ye'll be able to find a way to slip the stuff past",
                    "them.",
                )
                stage = 50
            }

            50 -> {
                player("Well I'll give it a shot.")
                stage = 51
            }

            51 -> {
                npc("Arr, that's the spirit!")
                stage = 999
            }

            700 -> {
                npc("Arr, silly you. Fortunately I took the precaution to have", "another one made.")
                stage = 701
            }

            701 -> {
                interpreter.sendItemMessage(KEY.id, "Frank hands you a chest key.")
                stage = 702
            }

            702 -> {
                end()
                if (!player.inventory.add(KEY)) {
                    GroundItemManager.create(KEY, player)
                }
            }

            80 ->
                if (player.getAttribute("falador-diary-talk-first-time", false)) {
                    npc("So you're interested in exploring Falador and it's", "surrounding areas, eh?")
                    setAttribute(player, "/save:falador-diary-talk-first-time", true)
                    stage = 100
                } else {
                    npc("How are you getting on with the Achievement Diary?")
                    stage = 90
                }

            90 -> {
                options("I've come for my reward.", "I'm doing good.", "I have a question.")
                stage = 91
            }

            91 ->
                when (buttonId) {
                    1 -> {
                        player("I've come for my reward.")
                        stage = 200
                    }

                    2 -> {
                        player("I'm doing good.")
                        stage = 220
                    }

                    3 -> {
                        player("I have a question.")
                        stage = 105
                    }
                }

            100 -> {
                player("Er... I guess.")
                stage = 101
            }

            101 -> {
                npc(
                    "Arrr! That's the spirit! Soon ye'll be exploring",
                    "underground caverns, sailin' the high seas and",
                    "plundering booty!",
                )
                stage = 102
            }

            102 -> {
                player(FaceAnim.AMAZED, "Plundering booty?")
                stage = 103
            }

            103 -> {
                npc("Arrr! Nay, that be a lie, I be getting carried away.")
                stage = 104
            }

            104 -> {
                npc("Have you got any questions?")
                stage = 105
            }

            105 ->
                if (!Diary.hasClaimedLevelRewards(player, DiaryType.FALADOR, level)) {
                    options(
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later.",
                    )
                    stage = 106
                } else {
                    options(
                        "Can you remind me what my Falador shield does, please?",
                        "What is the Achievement Diary?",
                        "What are the rewards?",
                        "How do I claim the rewards?",
                        "See you later.",
                    )
                    stage = 107
                }

            106 ->
                when (buttonId) {
                    1 -> {
                        player("What is the Achievement Diary?")
                        stage = 110
                    }

                    2 -> {
                        player("What are the rewards?")
                        stage = 120
                    }

                    3 -> {
                        player("How do I claim the rewards?")
                        stage = 130
                    }

                    4 -> {
                        player("See you later.")
                        stage = 999
                    }
                }

            107 ->
                when (buttonId) {
                    1 -> {
                        player("Can you remind me what my Falador shield does, please?")
                        stage = 150
                    }

                    2 -> {
                        player("What is the Achievement Diary?")
                        stage = 110
                    }

                    3 -> {
                        player("What are the rewards?")
                        stage = 120
                    }

                    4 -> {
                        player("How do I claim the rewards?")
                        stage = 130
                    }

                    5 -> {
                        player("See you later.")
                        stage = 999
                    }
                }

            110 -> {
                npc(
                    "Ah, well it's a diary that helps you keep track of",
                    "particular achievements you've made here on",
                    "Gielinor.",
                )
                stage = 111
            }

            111 -> {
                npc(
                    "If you manage to complete a particular set of tasks,",
                    "you will be rewarded for your explorative efforts.",
                )
                stage = 112
            }

            112 -> {
                npc(
                    "You can access your Achievement Diary by going to",
                    "the Quest Journal, then clicking on the green star icon",
                    "in the top-right hand corner.",
                )
                stage = 105
            }

            120 -> {
                npc(
                    "Ah, well there are different rewards for each",
                    "Achievement Diary. For completing each stage of the",
                    "Falador diary, you are presented with a Falador shield.",
                )
                stage = 121
            }

            121 -> {
                npc("There are three shields available, one for each difficulty", "level.")
                stage = 122
            }

            122 -> {
                npc("When you are presented with your rewards, you will", "be told of their uses.")
                stage = 105
            }

            130 -> {
                npc(
                    "You need to complete all of the tasks in a particular",
                    "difficulty, then you can claim your reward.",
                )
                stage = 131
            }

            131 -> {
                npc(
                    "Some of Falador's tasks are simple, some will require",
                    "certain skill levels, and some might require quests to be",
                    "started or completed.",
                )
                stage = 132
            }

            132 -> {
                npc(
                    "To claim your Falador Achievement Diary rewards,",
                    "speak to the chemist in Rimmington, Sir Vyvin's squire",
                    "in the White Knight's Castle, or myself.",
                )
                stage = 105
            }

            150 -> {
                npc(
                    "This is the first stage fo the Falador shield: a buckler. It",
                    "grants you access to a Prayer restore ability and an",
                    "emote.",
                )
                stage = 151
            }

            151 -> {
                npc(
                    "Each of these features can be triggered while wielding",
                    "the shield by selecting the 'Operate' option. The Prayer",
                    "restore can also be activated from your inventory.",
                )
                stage = 152
            }

            152 -> {
                npc(
                    "The Prayer restore ability can only be used once per",
                    "day, and gives you back a quarter of your Prayer",
                    "points.",
                )
                stage = 153
            }

            153 -> {
                npc(
                    "As well as all of these features, the shield is pretty",
                    "handy in combat, and gives you a small Prayer boost.",
                )
                stage = 105
            }

            200 ->
                if (Diary.canReplaceReward(player, DiaryType.FALADOR, level)) {
                    playerl(FaceAnim.HALF_GUILTY, "I seem to have lost my shield...")
                    stage = 250
                } else if (Diary.hasClaimedLevelRewards(player, DiaryType.FALADOR, level)) {
                    npc("But you've already gotten yours!")
                    stage = 105
                } else if (Diary.hasCompletedLevel(player, DiaryType.FALADOR, level)) {
                    npc("So, you've finished. Well done! I believe congratulations", "are in order.")
                    stage = 201
                } else {
                    npc("But you haven't finished!")
                    stage = 105
                }

            201 -> {
                player("I believe rewards are in order.")
                stage = 202
            }

            202 -> {
                npc("Right you are.")
                stage = 203
            }

            203 -> {
                npc(
                    "This is the first stage of the Falador shield: a buckler. It",
                    "grants you access to a Prayer restore ability and an",
                    "emote.",
                )
                Diary.flagRewarded(player, DiaryType.FALADOR, level)
                stage = 204
            }

            204 -> {
                npc(
                    "Each of these features can be triggered while wielding",
                    "the shield by selecting the 'Operate' option. The Prayer",
                    "restore can also be activated from your inventory.",
                )
                stage = 205
            }

            205 -> {
                npc(
                    "The Prayer restore ability can only be used once per",
                    "day, and gives you back a quarter of your Prayer",
                    "points.",
                )
                stage = 206
            }

            206 -> {
                npc(
                    "As well as all of these features, the shield is pretty",
                    "handy in combat, and gives you a small Prayer boost.",
                )
                stage = 207
            }

            207 -> {
                npc("I've even thrown in a bit of booty I found on my", "travels.")
                stage = 208
            }

            208 -> {
                player(FaceAnim.AMAZED, "Wow, thanks!")
                stage = 209
            }

            209 -> {
                npc("If you should lose your shield, come back and see me", "for another one.")
                stage = 105
            }

            220 -> {
                npc("Keep it up!")
                stage = 105
            }

            250 -> {
                npcl(FaceAnim.LAUGH, "Alright, matey, I'll give ye a new one.")
                stage++
            }

            251 -> {
                Diary.grantReplacement(player, DiaryType.FALADOR, level)
                npcl(FaceAnim.FRIENDLY, "Be more careful this time, aye?")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.REDBEARD_FRANK_375)
    }

    companion object {
        private val KARAMJAN_RUM = Item(431)
        private val KEY = Item(432)
    }
}
