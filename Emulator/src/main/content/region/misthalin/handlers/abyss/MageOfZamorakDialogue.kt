package content.region.misthalin.handlers.abyss

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Represents the Mage Of Zamorak dialogue.
 */
@Initializable
class MageOfZamorakDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var varrockMage = false

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        varrockMage = npc.id == 2261 || npc.id == 2260
        if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
            end()
            sendMessage(player, "The mage doesn't seem interested in talking to you.")
            return true
        }
        if (!varrockMage) {
            when (getStage()) {
                0 -> npc("Meet me in Varrock's Chaos Temple.", "Here is not the place to talk.")
                1, 2, 3 -> npc("I already told you!", "meet me in the Varrock Chaos Temple!")
                4 -> npc("This is no place to talk!", "Meet me at the Varrock Chaos Temple!")
            }
        } else {
            when (getStage()) {
                0 -> npc("I am in no mood to talk to you", "stranger!")
                1 ->
                    npc(
                        "Ah, you again.",
                        "What was it you wanted?",
                        "The wilderness is hardly the appropriate place for a",
                        "conversation now, is it?",
                    )

                2 -> npc("Well?", "Have you managed to use my scrying orb to obtain the", "information yet?")
                3 -> player("So... that's my end of the deal upheld.", "What do I get in return?")
                4 ->
                    options(
                        "So what is this 'abyss' stuff?",
                        "Is this abyss dangerous?",
                        "Can you teleport me there now?",
                    )
            }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!varrockMage) {
            when (getStage()) {
                0 -> {
                    setStage(1)
                    end()
                }

                1, 2, 3, 4 -> end()
            }
        } else {
            when (getStage()) {
                0 -> end()
                1 ->
                    when (stage) {
                        0 ->
                            options(
                                "I'd like to buy some runes!",
                                "Where do you get your runes from?",
                                "All hail Zamorak!",
                                "Nothing, thanks.",
                            ).also {
                                stage++
                            }

                        1 ->
                            when (buttonId) {
                                1 -> player("I'd like to buy some runes!").also { stage = 10 }
                                2 ->
                                    player(
                                        "Where do you get your runes from?",
                                        "No offence, but people around here don't exactly like",
                                        "'your type'.",
                                    ).also {
                                        stage =
                                            20
                                    }

                                3 ->
                                    player(
                                        "All hail Zamorak!",
                                        "He's the man!",
                                        "If he can't do it, maybe some other guy can!",
                                    ).also {
                                        stage =
                                            30
                                    }

                                4 ->
                                    player(
                                        "I didn't really want anything, thanks.",
                                        "I just like talking to random people I meet around the",
                                        "world.",
                                    ).also {
                                        stage =
                                            40
                                    }
                            }

                        10 ->
                            npc(
                                "I do not conduct business in this pathetic city.",
                                "Speak to me in the wilderness if you desire to purchase",
                                "runes from me.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }

                        20 -> npc("My 'type' Explain.").also { stage++ }
                        21 ->
                            player(
                                "You know...",
                                "Scary bearded men in dark clothing with unhealthy",
                                "obsessions with destruction and stuff.",
                            ).also {
                                stage++
                            }

                        22 ->
                            npc(
                                "Hmmm.",
                                "Well, you may be right, the foolish Saradominists that",
                                "own this pathetic city don't appreciate loyal Zamorakians,",
                                "it is true.",
                            ).also {
                                stage++
                            }

                        23 ->
                            player(
                                "So you can't be getting your runes anywhere around",
                                "here...",
                            ).also { stage++ }

                        24 ->
                            npc(
                                "That is correct stranger.",
                                "My mysteries of manufacturing Runes is a closely",
                                "guarded secret of the Zamorakian brotherhood.",
                            ).also {
                                stage++
                            }

                        25 ->
                            player(
                                "Oh, you mean the whole teleporting to the Rune",
                                "essence mine, mining some essence, then using the",
                                "talismans to locate the Rune Temples, then binding",
                                "runes there?",
                            ).also {
                                stage++
                            }

                        26 -> player("I know all about it...").also { stage++ }
                        27 -> npc("WHAT?", "I... but... you...").also { stage++ }
                        28 ->
                            npc(
                                "Tell me, this is important:",
                                "You know of the ancient temples?",
                                "You have been to a place where this 'rune essence'",
                                "material is freely available?",
                            ).also {
                                stage++
                            }

                        29 -> npc("How did you get to such place?").also { stage = 200 }
                        200 ->
                            player(
                                "Well, I helped deliver some research notes to Sedridor",
                                "at the Wizards Tower, and he teleported me to a huge",
                                "mine he said was hidden off to the North somewhere",
                                "where I could mine essence.",
                            ).also {
                                stage++
                            }

                        201 ->
                            npc(
                                "And there is an abundant supply of this 'essence' there",
                                "you say?",
                            ).also { stage++ }

                        202 ->
                            player(
                                "Yes, but I thought you said that you knew how to make",
                                "runes?",
                                "All this stuff is fairly basic knowledge I thought.",
                            ).also {
                                stage++
                            }

                        203 -> npc("No.", "No, not at all.").also { stage++ }
                        204 ->
                            npc(
                                "We occasionally manage to plunder small samples of this",
                                "'essence' and we have recently discovered these temples",
                                "you speak of, but I have never ever heard of these talismans",
                                "before, and I was certainly not aware that this 'essence'",
                            ).also {
                                stage++
                            }

                        205 ->
                            npc(
                                "substance is a heavily stockpiled resource at the Wizards",
                                "Tower.",
                            ).also { stage++ }

                        206 -> npc("This changes everything.").also { stage++ }
                        207 -> player("How do you mean?").also { stage++ }
                        208 ->
                            npc(
                                "For many years there has been a struggle for power",
                                "on this world.",
                                "You may dispute the morality of each side as you wish,",
                                "but the stalemate that exists between my Lord Zamorak",
                            ).also {
                                stage++
                            }

                        209 ->
                            npc(
                                "and that pathetic meddling fool Saradomin has meant",
                                "that our struggle have become more secretive.",
                                "We exist in a 'cold war' if you will, each side fearful of",
                                "letting the other gain too much power, and each side",
                            ).also {
                                stage++
                            }

                        210 ->
                            npc(
                                "equally fearful of entering into open warfare for fear of",
                                "bringing our struggles to the attention of... other",
                                "beings.",
                            ).also {
                                stage++
                            }

                        211 -> player("You mean Guthix?").also { stage++ }
                        212 ->
                            npc(
                                "Indeed.",
                                "Amongst others.",
                                "But you now tell me that the Saradominist Wizards",
                                "have the capability to mass produce runes, I can only",
                            ).also {
                                stage++
                            }

                        213 ->
                            npc(
                                "conclude that they have been doing so secretly for some",
                                "time now.",
                            ).also { stage++ }

                        214 ->
                            npc(
                                "I implore you adventurer, you may or may not agree",
                                "with my aims, but you cannot allow such a one-sided",
                                "shift in the balance of power to occur.",
                            ).also {
                                stage++
                            }

                        215 ->
                            npc(
                                "Will you help me and my fellow Zamorakians to access",
                                "this 'essence' mine?",
                                "In return I will share with you the research we have",
                                "gathered.",
                            ).also {
                                stage++
                            }

                        216 -> player("Okay, I'll help you.", "What can I do?").also { stage++ }
                        217 ->
                            npc(
                                "All I need from you is the spell that will teleport me to",
                                "this essence mine.",
                                "That should be sufficient for the armies of Zamorak to",
                                "once more begin stockpiling magic for war.",
                            ).also {
                                stage++
                            }

                        218 -> player("Oh.", "Erm...", "I don't actually know that spell.").also { stage++ }
                        219 -> npc("What?", "Then how do you access this location?").also { stage++ }
                        220 ->
                            player(
                                "Oh, well, people who do know the spell teleport me there",
                                "directly.",
                                "Apparently they wouldn't teach it to me to try and keep",
                                "the location secret.",
                            ).also {
                                stage++
                            }

                        221 ->
                            npc(
                                "Hmmm.",
                                "Yes, yes I see.",
                                "Very well then, you may still assist us in finding this",
                                "mysterious essence mine.",
                            ).also {
                                stage++
                            }

                        222 -> player("How would I do that?").also { stage++ }
                        223 -> {
                            end()
                            setStage(2)
                            player.inventory.add(ORBS[0], player)
                            npc(
                                "Here, take this scrying orb.",
                                "I have cast a standard cypher spell upon it, so that it",
                                "will absorb mystical energies that it is exposed to.",
                            )
                        }

                        30 -> end()
                        40 ->
                            npc(
                                "...I see.",
                                "Well, in the future, do not waste my time, or you will",
                                "feel the wrath of Zamorak upon you.",
                            ).also {
                                stage++
                            }

                        41 -> end()
                    }

                2 ->
                    when (stage) {
                        0 -> {
                            if (!player.hasItem(ORBS[0]) && !player.inventory.containsItem(ORBS[1])) {
                                player(
                                    "Uh...",
                                    "No...",
                                    "I kinda lost that orb thingy that you gave me.",
                                ).also { stage++ }
                            }
                            if (!player.inventory.containsItem(ORBS[1])) {
                                player(
                                    "No...",
                                    "Actually, I had something I wanted to ask you...",
                                ).also { stage = 3 }
                            } else {
                                player("Yes I have! I've got it right here!").also { stage = 50 }
                            }
                        }

                        1 -> {
                            end()
                            player.inventory.add(ORBS[0], player)
                            npc(
                                "What?",
                                "Incompetent fool. Take this.",
                                "And do not make me regret allying myself with you.",
                            )
                        }

                        3 ->
                            npc(
                                "I assume the task to be self-explanatory.",
                                "What is it you wish to know?",
                            ).also { stage++ }

                        4 ->
                            player(
                                "Please excuse me, I have a very bad short term",
                                "memory.",
                                "What exactly am I supposed to be doing again?",
                            ).also {
                                stage++
                            }

                        5 ->
                            npc(
                                "All I wish for you to do is to teleport to this 'rune",
                                "essence' location from three different locations while",
                                "carrying the scrying orb I gave you.",
                                "It will collect the data as you teleport.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }

                        224 ->
                            npc(
                                "Bring it with you and teleport to the rune essence",
                                "location, and it will absorb the mechanics of the spell and",
                                "allow us to reverse-engineer the magic behind it.",
                            ).also {
                                stage++
                            }

                        225 ->
                            npc(
                                "The important part is that you must teleport to the",
                                "essence location from three entirely separate locations.",
                            ).also {
                                stage++
                            }

                        226 ->
                            npc(
                                "More than three may be helpful to us, but we need a",
                                "minimum of three in order to triangulate the position of",
                                "this essence mine.",
                            ).also {
                                stage++
                            }

                        227 -> npc("Is that all clear, stranger?").also { stage++ }
                        228 -> player("Yeah, I think so.").also { stage++ }
                        229 ->
                            npc("Good.", "If you encounter any difficulties speak to me again.").also {
                                stage =
                                    END_DIALOGUE
                            }

                        50 ->
                            npc(
                                "Excellent.",
                                "Give it here, and I shall examine the findings.",
                                "Speak to me in a small while.",
                            ).also {
                                stage++
                            }

                        51 -> {
                            setStage(3)
                            player.inventory.remove(ORBS[1])
                            end()
                        }
                    }

                3 ->
                    when (stage) {
                        0 -> npc("Indeed, a deal is always a deal.").also { stage++ }
                        1 ->
                            npc(
                                "I offer you three things as a reward for your efforts on",
                                "behalf of my Lord Zamorak;",
                            ).also { stage++ }

                        2 ->
                            npc(
                                "The first is knowledge.",
                                "I offer you my collected research on the abyss.",
                                "I also offer you 1000 points of experience in",
                                "RuneCrafting for your trouble.",
                            ).also {
                                stage++
                            }

                        3 ->
                            npc(
                                "Your second gift is convenience.",
                                "Here you may take this pouch I discovered amidst my",
                                "research.",
                                "You will find it to have certain... interesting properties.",
                            ).also {
                                stage++
                            }

                        4 ->
                            npc(
                                "Your final gift is that of movement",
                                "I will from now on offer you a teleport to the abyss",
                                "whenever you should require it.",
                            ).also {
                                stage++
                            }

                        5 -> {
                            setStage(4)
                            addItem(player, Items.ABYSSAL_BOOK_5520)
                            addItem(player, Items.SMALL_POUCH_5509)
                            rewardXP(player, Skills.RUNECRAFTING, 1000.0)
                            player(
                                "Huh?",
                                "Abyss?",
                                "What are you talking about?",
                                "You told me that you would help me with",
                            )
                            stage++
                        }
                    }

                4 ->
                    when (stage) {
                        0 ->
                            when (buttonId) {
                                1 ->
                                    player(
                                        "Uh...",
                                        "I really don't see how this talk about an 'abyss' relates",
                                        "to RuneCrafting in the slightest...",
                                    ).also {
                                        stage =
                                            10
                                    }

                                2 ->
                                    player(
                                        "So...",
                                        "This 'abyss' place...",
                                        "Is it dangerous?",
                                    ).also { stage = 20 }

                                3 ->
                                    player(
                                        "Well, I reckon I'm prepared to go there now.",
                                        "Beam me there, or whatever it is that you do!",
                                    ).also {
                                        stage =
                                            30
                                    }
                            }

                        10 ->
                            npc(
                                "My primary research responsibility was not towards the",
                                "manufacture of runes, this is true.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }

                        20 ->
                            npc(
                                "Well, the creatures there ARE particularly offensive...",
                            ).also { stage = END_DIALOGUE }

                        30 ->
                            npc(
                                "No, not from here.",
                                "The use of my Lord Zamorak magic in this land will",
                                "draw too much attention to myself.",
                            ).also {
                                stage =
                                    8
                            }

                        6 -> player("RuneCrafting!").also { stage++ }
                        7 ->
                            npc(
                                "And so I have done.",
                                "Read my research notes, they may enlighten you",
                                "somewhat.",
                            ).also { stage++ }

                        8 -> end()
                    }
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            2257,
            NPCs.MAGE_OF_ZAMORAK_2258,
            NPCs.MAGE_OF_ZAMORAK_2259,
            NPCs.MAGE_OF_ZAMORAK_2260,
            NPCs.MAGE_OF_ZAMORAK_2261,
        )

    override fun setStage(stage: Int) {
        setVarp(player, 492, stage, true)
    }

    fun getStage(): Int = getVarp(player, 492)

    companion object {
        private val ORBS = arrayOf(Item(Items.SCRYING_ORB_5519), Item(Items.SCRYING_ORB_5518))
    }
}