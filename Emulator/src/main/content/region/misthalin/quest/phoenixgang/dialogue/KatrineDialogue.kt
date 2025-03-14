package content.region.misthalin.quest.phoenixgang.dialogue

import content.region.asgarnia.quest.hero.dialogue.KatrineDialogueFile
import content.region.misthalin.quest.phoenixgang.ShieldofArrav
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

class KatrineDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var quest: Quest? = null

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        quest = player.getQuestRepository().getQuest(Quests.SHIELD_OF_ARRAV)
        when (quest!!.getStage(player)) {
            100 -> {
                if (ShieldofArrav.isBlackArm(player)) {
                    val heroesQuest: Quest = player.getQuestRepository().getQuest(Quests.HEROES_QUEST)
                    if (0 < heroesQuest.getStage(player) && heroesQuest.getStage(player) < 100) {
                        openDialogue(player, KatrineDialogueFile(), npc)
                        return true
                    }
                }
                if (ShieldofArrav.isPhoenix(player)) {
                    npc("You've got some guts coming here, Phoenix guy!")
                    stage = 200
                } else {
                    player("Hey.")
                    stage = 0
                }
            }

            90, 80, 70 ->
                if (ShieldofArrav.isPhoenix(player)) {
                    npc("You've got some guts coming here, Phoenix guy!")
                    stage = 200
                } else {
                    player("Hey.")
                    stage = 0
                }

            60 ->
                if (ShieldofArrav.isBlackArmMission(player)) {
                    npc("Have you got those crossbows for me yet?")
                    stage = 200
                } else {
                    player("What is this place?")
                    stage = 0
                }

            else -> {
                player("What is this place?")
                stage = 0
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (quest!!.getStage(player) == 60 && stage >= 200) {
            when (stage) {
                200 ->
                    if (!player.inventory.containsItem(CROSSBOWS)) {
                        player("No, I haven't found them yet.")
                        stage = 201
                    } else {
                        player("Yes, I have.")
                        stage = 204
                    }

                201 -> {
                    npc(
                        "I need two crossbows stolen from the Phoenix Gang",
                        "weapons stash, which if you head east for a bit, is a",
                        "building on the south side of the road.",
                    )
                    stage = 202
                }

                202 -> {
                    npc("Come back when you got 'em.")
                    stage = 203
                }

                203 -> end()
                204 -> {
                    interpreter.sendDialogue("You give the crossbows to Katrine.")
                    stage = 205
                }

                205 -> {
                    if (!player.inventory.containsItem(CROSSBOWS)) {
                        end()
                        return true
                    }
                    if (player.inventory.remove(CROSSBOWS)) {
                        npc("Ok. You can join our gang now. Feel free to enter", "any of the rooms of the ganghouse.")
                        stage = 206
                        quest!!.setStage(player, 70)
                        ShieldofArrav.setBlackArm(player)
                    }
                }

                206 -> end()
            }
            return true
        }
        when (quest!!.getStage(player)) {
            80, 90, 100, 70 ->
                when (stage) {
                    0 -> {
                        npc("Hey.")
                        stage = 1
                    }

                    1 -> end()
                    200 -> {
                        interpreter.sendDialogue("Katrine spits.")
                        stage = 201
                    }

                    201 -> {
                        npc("Now get lost!")
                        stage = 202
                    }

                    202 -> end()
                    206 -> end()
                }

            60, 50 ->
                when (stage) {
                    0 -> {
                        npc("It's a private business. Can I help you at all?")
                        stage = 1
                    }

                    1 -> {
                        options(
                            "I've heard you're the Black Arm Gang.",
                            "What sort of business?",
                            "I'm looking for fame and riches.",
                        )
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("I've heard you're the Black Arm Gang.")
                                stage = 100
                            }

                            2 -> {
                                player("What sort of business?")
                                stage = 10
                            }

                            3 -> {
                                player("I'm looking for fame and riches.")
                                stage = 20
                            }
                        }

                    100 -> {
                        npc("Who told you that?")
                        stage = 101
                    }

                    101 -> {
                        options(
                            "I'd rather not reveal my sources.",
                            "It was Charlie, the tramp outside.",
                            "Everyone knows - it's no great secret.",
                        )
                        stage = 102
                    }

                    102 ->
                        when (buttonId) {
                            1 -> {
                                player("I'd rather not reveal my sources.")
                                stage = 110
                            }

                            2 -> {
                                player("It was Charlie, the tramp outside.")
                                stage = 120
                            }

                            3 -> {
                                player("Everyone knows - it's not great secret.")
                                stage = 130
                            }
                        }

                    110 -> {
                        npc("Yes, I can understand that. So what do you want with", "us?")
                        stage = 111
                    }

                    111 -> {
                        options(
                            "I want to become a member of your gang.",
                            "I want some hints for becoming a thief.",
                            "I'm looking for the door out of here.",
                        )
                        stage = 112
                    }

                    112 ->
                        when (buttonId) {
                            1 -> {
                                player("I want to become a member of your gang.")
                                stage = 160
                            }

                            2 -> {
                                player("I want some hints for becoming a thief.")
                                stage = 113
                            }

                            3 -> {
                                player("I'm looking for the door out of here.")
                                stage = 115
                            }
                        }

                    160 -> {
                        npc("How unusual.")
                        stage = 161
                    }

                    161 -> {
                        npc(
                            "Normally we recruit for our gang by watching local",
                            "thugs and thieves in reward. People don't normally waltz",
                            "in here saying 'hello, can I play'.",
                        )
                        stage = 162
                    }

                    162 -> {
                        npc("How can I be sure you can be trusted?")
                        stage = 163
                    }

                    163 -> {
                        options(
                            "Well, you can give me a try can't you?",
                            "Well, people tell me I have an honest face.",
                        )
                        stage = 164
                    }

                    164 ->
                        when (buttonId) {
                            1 -> {
                                player("Well, you can give me a try can't you?")
                                stage = 167
                            }

                            2 -> {
                                player("Well, people tell me I have an honest face.")
                                stage = 165
                            }
                        }

                    165 -> {
                        npc(
                            "... How unusual. Someone honest wanting to join a",
                            "gang of thieves. Excuse me if I remain unconvinced.",
                        )
                        stage = 166
                    }

                    166 -> {
                        npc("Thinking about it... I may have a solution actually.")
                        stage = 169
                    }

                    167 -> {
                        npc("I'm not so sure.")
                        stage = 166
                    }

                    169 -> {
                        npc("Our rival gang - the Phoenix Gang - has a weapons", "stash a little east of here.")
                        stage = 170
                    }

                    170 -> {
                        npc(
                            "We're fresh out of crossbows, so if you could steal a",
                            "couple of crossbows for us it would be very much",
                            "appreciated.",
                        )
                        stage = 171
                    }

                    171 -> {
                        npc("Then I'll be happy to call you a Black Arm.")
                        stage = 172
                    }

                    172 -> {
                        player("Sounds simple enough. Any particular reason you need", "two of them?")
                        stage = 173
                    }

                    173 -> {
                        npc(
                            "I have an idea for framing a local merchant who is",
                            "refusing to pay our, very reasonable, 'keep-your-life-",
                            "pleasant' insurance rates. I need two phoenix crossbows;",
                            "one to kill somebody important with and the other to",
                        )
                        stage = 174
                    }

                    174 -> {
                        npc(
                            "hide in the merchant's house where the local law can",
                            "find it! When they find it, they'll suspect him of",
                            "murdering the target for the Phoenix gang and",
                            "hopefully, arrest the whole gang! Leaving us as the only",
                        )
                        stage = 175
                    }

                    175 -> {
                        npc("thieves gang in Varrock! Brilliant, eh?")
                        stage = 176
                    }

                    176 -> {
                        player("Yeah, brilliant. So who are you planning to murder?")
                        stage = 177
                    }

                    177 -> {
                        npc(
                            "I haven't decided yet, but it'll need to be somebody",
                            "important. Say, why you being so nosey? You aren't",
                            "with the law are you?",
                        )
                        stage = 178
                    }

                    178 -> {
                        player("No, no! Just curious.")
                        stage = 179
                    }

                    179 -> {
                        npc(
                            "You'd better just keep your mouth shut about this plan,",
                            "or I'll make sure it stays shut for you. Now, are you",
                            "going to get those crossbows or not?",
                        )
                        stage = 180
                    }

                    180 -> {
                        options("Ok, no problem.", "Sounds a little tricky. Got anything easier?")
                        stage = 181
                    }

                    181 ->
                        when (buttonId) {
                            1 -> {
                                player("Ok, no problem.")
                                stage = 184
                            }

                            2 -> {
                                player("Sounds a little tricky. Got anything easier?")
                                stage = 182
                            }
                        }

                    182 -> {
                        npc(
                            "If you're not up to a little bit of dager I don't think",
                            "you've got anything to offer our gang.",
                        )
                        stage = 183
                    }

                    183 -> end()
                    184 -> {
                        quest!!.setStage(player, 60)
                        ShieldofArrav.setBlackArmMission(player)
                        npc(
                            "Great! You'll find the Phoenix gang's weapon stash just",
                            "next to a temple, due east of here.",
                        )
                        stage = 185
                    }

                    185 -> end()
                    113 -> {
                        npc("Well, I'm sorry luv, I'm not giving away any of my", "secrets.")
                        stage = 114
                    }

                    114 -> end()
                    115 -> {
                        interpreter.sendDialogue("Katrine groans.")
                        stage = 116
                    }

                    116 -> {
                        npc("Try... the one you just came in?")
                        stage = 117
                    }

                    117 -> end()
                    120 -> {
                        npc(
                            "Is that guy still out there? He's getting to be a",
                            "nuisance. Remind me to send someone to kill him.",
                        )
                        stage = 121
                    }

                    121 -> end()
                    130 -> {
                        npc("I thought we were safe back here!")
                        stage = 131
                    }

                    131 -> {
                        player("Oh no, not at all... It's so obvious! Even the town", "guard have caught on...")
                        stage = 132
                    }

                    132 -> {
                        npc("Wow! We MUSE be obvious! I guess they'll be", "expecting bribes again soon in that case.")
                        stage = 133
                    }

                    133 -> {
                        npc("Thanks for the information.")
                        stage = 134
                    }

                    134 -> end()
                    10 -> {
                        npc("A small, family business. We give financial advice to", "other companies.")
                        stage = 11
                    }

                    11 -> end()
                    20 -> {
                        npc("And you expect to find it up the back streets of", "Varrock?")
                        stage = 21
                    }

                    21 -> end()
                }

            else ->
                when (stage) {
                    0 -> {
                        npc("It's a private business. Can I help you at all?")
                        stage = 1
                    }

                    1 -> {
                        options("What sort of business?", "I'm looking for fame and riches.")
                        stage = 2
                    }

                    2 ->
                        when (buttonId) {
                            1 -> {
                                player("What sort of business?")
                                stage = 10
                            }

                            2 -> {
                                player("I'm looking for fame and riches.")
                                stage = 20
                            }
                        }

                    10 -> {
                        npc("A small, family business. We give financial advice to", "other companies.")
                        stage = 11
                    }

                    11 -> end()
                    20 -> {
                        npc("And you expect to find it up the back streets of", "Varrock?")
                        stage = 21
                    }

                    21 -> end()
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return KatrineDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KATRINE_642)
    }

    companion object {
        private val CROSSBOWS = Item(Items.PHOENIX_CROSSBOW_767, 2)
    }
}
