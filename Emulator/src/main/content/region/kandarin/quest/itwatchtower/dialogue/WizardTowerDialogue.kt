package content.region.kandarin.quest.itwatchtower.dialogue

import core.api.item.allInInventory
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.removeItem
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
class WizardTowerDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private val evidenceItems =
        intArrayOf(
            Items.FINGERNAILS_2384,
            Items.DAMAGED_DAGGER_2387,
            Items.TATTERED_EYE_PATCH_2388,
            Items.OLD_ROBE_2385,
            Items.UNUSUAL_ARMOUR_2386,
        )

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc.id == NPCs.WIZARD_5195) {
            player("What's going on here?").also { stage = 100 }
        } else {
            npc("Who are you? Are you one of the new guards?")
        }
        if (getQuestStage(player, Quests.WATCHTOWER) >= 1) {
            npc("Hello again. Did you find anything of interest?").also { stage = 30 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("No, I'm an adventurer.").also { stage++ }
            1 -> npc("Well what are you doing here?").also { stage++ }
            2 -> player("Looking for adventures - what else?").also { stage++ }
            3 -> npc("Oh my, oh my! What does it all matter in the end,", "anyway?").also { stage++ }
            4 -> options("What's the matter?", "You wizards are always complaining.").also { stage++ }
            5 ->
                when (buttonId) {
                    1 -> player("What's the matter?").also { stage++ }
                    2 -> player("You wizards are always complaining.").also { stage = 200 }
                }
            6 -> npc("Oh dear, oh dear. Darn and drat!").also { stage++ }
            7 -> npc("We try hard to keep this town protected.").also { stage++ }
            8 -> npc("But how can we do that when the Watchtower isn't", "working?").also { stage++ }
            9 -> player("What do you mean it isn't working?").also { stage++ }
            10 ->
                npc(
                    "The Watchtower here works by the power of magic. An",
                    "ancient spell designed to ward off ogres that has been",
                    "in place here for many moons.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    "The exact knowledge of the spell is lost to us now, but",
                    "the essence of the spell has been infused into four",
                    "powering crystals that keep the tower protected from",
                    "the hordes in the Feldips.",
                ).also {
                    stage++
                }
            12 -> options("So how come the spell doesn't work?", "You wizards are always complaining.").also { stage++ }
            13 ->
                when (buttonId) {
                    1 -> player("So how come the spell doesn't work?").also { stage++ }
                    2 -> player("You wizards are always complaining.").also { stage = 200 }
                }

            14 -> npc("The crystals! The crystals have been taken!").also { stage++ }
            15 -> player("Taken?").also { stage++ }
            16 -> npc("Stolen!").also { stage++ }
            17 -> player("Stolen?").also { stage++ }
            18 -> npc("Yes, yes! Do I have to repeat myself?").also { stage++ }
            19 -> options("Can I be of help?", "I'm not sure I can help.", "I'm not interested.").also { stage++ }
            20 ->
                when (buttonId) {
                    1 -> player("Can I be of help?").also { stage++ }
                    2 -> player("I'm not sure I can help.").also { stage = 300 }
                    3 -> player("I'm not interested.").also { stage = 400 }
                }
            21 -> npc("Help? Oh wonderful, dear traveller!").also { stage++ }
            22 -> npc("Yes I could do with an extra pair of eyes here.").also { stage++ }
            23 -> player("???").also { stage++ }
            24 ->
                npc(
                    "There must be some evidence of what has happened",
                    "here. Perhaps you could assist me in searching for",
                    "clues?",
                ).also {
                    stage++
                }
            25 -> player("I would be happy to.").also { stage++ }
            26 ->
                npc(
                    "Try searching the surrounding area. If you find",
                    "anything unusual, bring it here.",
                ).also { stage++ }
            27 ->
                npc(
                    "Try the bushes - I've read enough adventure stories to",
                    "know that clues get caught in bushes all the time.",
                ).also {
                    stage++
                }
            28 ->
                npc(
                    "I will tell the guards to let you past - that way, you",
                    "can just use the ladder to get in and out.",
                ).also {
                    stage++
                }
            29 -> {
                end()
                setQuestStage(player, Quests.WATCHTOWER, 1)
            }

            30 ->
                if (!allInInventory(player, *evidenceItems)) {
                    player("Have a look at these...").also { stage++ }
                } else {
                    npc("Interesting, very interesting.").also { stage += 2 }
                }

            31 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "No, sorry, this is not evidence. You need to keep searching, I'm afraid.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            32 -> npc("Long nails...grey in colour...well chewed...").also { stage++ }
            33 -> npc("Of course! They belong to a skavid!").also { stage++ }
            34 -> player("A skavid?").also { stage++ }
            35 ->
                npc(
                    "A servant race to the ogres: grey, depressed-looking",
                    "creatures, always losing nails, teeth and hair!",
                ).also {
                    stage++
                }
            36 -> npc("They inhabit the caves in the Feldip Hills.").also { stage++ }
            37 ->
                npc(
                    "They normally keep to themselves, though. It's unusual",
                    "for them to venture from their caves.",
                ).also {
                    stage++
                }
            38 -> options("What do you suggest I do?", "Shall I search the caves?").also { stage++ }
            39 ->
                when (buttonId) {
                    1 -> player("What do you suggest I do?").also { stage++ }
                    2 -> player("Shall I search the caves?").also { stage = 48 }
                }
            40 -> npc("It's no good searching the caves. Well, not yet, anyway.").also { stage++ }
            41 -> player("Why not?").also { stage++ }
            42 ->
                npc(
                    "They are deep and complex. The only way you will",
                    "navigate the caves is to have a map or something.",
                ).also {
                    stage++
                }
            43 -> npc("It may be that the ogres have one...").also { stage++ }
            44 -> player("And how do you know that?").also { stage++ }
            45 -> npc("Well... I don't.").also { stage++ }
            46 -> options("So what do I do?", "I won't bother then.").also { stage++ }
            47 ->
                when (buttonId) {
                    1 -> player("So what do I do?").also { stage++ }
                    2 -> player("I won't bother then.").also { stage = 500 }
                }
            48 ->
                npc(
                    "You need to be fearless and gain entrance to",
                    "Gu'Tanoth, the city of the ogres, then find out how to",
                    "navigate the caves.",
                ).also {
                    stage++
                }
            49 -> player("That sounds scary...").also { stage++ }
            50 ->
                npc(
                    "Ogres are nasty creatures, yes. Only a strong warrior,",
                    "and a clever one at that, can get the better of them.",
                ).also {
                    stage++
                }
            51 -> player("What do I need to do to get into the city?").also { stage++ }
            52 -> {
                npc(
                    "Well, the guards need to be dealt with. You could start",
                    "by checking out the ogre settlements found around",
                    "here.",
                )
                removeItem(player, Items.FINGERNAILS_2384)
                stage++
            }
            53 -> npc("Tribal ogres often dislike their neighbours...").also { stage++ }
            54 -> npc("In the meantime, I'll throw those fingernails out for", "you.").also { stage++ }
            55 -> {
                end()
                setQuestStage(player, Quests.WATCHTOWER, 2)
            }

            100 -> npc("You'll have to speak with the Watchtower Wizard", "about that.").also { stage = END_DIALOGUE }
            200 -> npc("Complaining? Complaining!").also { stage++ }
            201 ->
                npcl(
                    FaceAnim.ANNOYED,
                    "What folks these days don't realise is that if it weren't for us wizards, this entire world would be overrun with every creature you could possibly imagine. And some you couldn't even conceive of!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            300 ->
                npcl(FaceAnim.NEUTRAL, "That's typical, nowadays. It's left to us wizards to do all the work.").also {
                    stage =
                        END_DIALOGUE
                }
            400 -> npc("Hmph! Suit yourself.").also { stage = END_DIALOGUE }
            500 ->
                npcl(FaceAnim.NEUTRAL, "Won't bother? Won't bother! Perhaps this quest is too hard for you?").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WATCHTOWER_WIZARD_872, NPCs.WIZARD_5195)
    }
}
