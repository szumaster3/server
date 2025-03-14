package content.region.misthalin.dialogue.lumbridge

import core.api.*
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class HieronymusAvlafrimDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (getAttribute(player, "gnomecopter:unlocked", false)) {
            npc(FaceAnim.OLD_NORMAL, "Hello again, human.", "What can Gnomecopter Tours do for you?")
        } else {
            npc(
                FaceAnim.OLD_NORMAL,
                "Hello human and welcome to our amazing",
                "Gnomecopter Tours. What can we do for you?",
            )
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (getAttribute(player, "gnomecopter:unlocked", false)) {
                    setTitle(player, 5)
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "Remind me what it is you do here.",
                        "Tell me how you made the gnomecopters.",
                        "Have you got anything good for sale?",
                        "Can I buy a gnomecopter?",
                        "Nothing at the moment, thank you.",
                    ).also { stage++ }
                } else {
                    setTitle(player, 3)
                    sendDialogueOptions(
                        player,
                        "What would you like to say?",
                        "What's Gnomecopter Tours?",
                        "Have you got anything good for sale?",
                        "Nothing at the moment, thank you.",
                    ).also { stage++ }
                }
            }

            1 ->
                if (getAttribute(player, "gnomecopter:unlocked", false)) {
                    when (buttonId) {
                        1 -> player("Remind me what it is you do here.").also { stage = 16 }
                        2 -> player("Tell me how you made the gnomecopters.").also { stage = 5 }
                        3 -> end().also { openNpcShop(player, NPCs.HIERONYMUS_AVLAFRIM_7420) }
                        4 -> player("Can I buy a gnomecopter?").also { stage = 15 }
                        5 -> player("Oh, forget it.").also { stage = END_DIALOGUE }
                    }
                } else {
                    when (buttonId) {
                        1 -> player("What's Gnomecopter Tours?").also { stage++ }
                        2 -> end().also { openNpcShop(player, NPCs.HIERONYMUS_AVLAFRIM_7420) }
                        3 -> player("Oh, forget it.").also { stage = END_DIALOGUE }
                    }
                }

            2 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "The gnomecopters are something Sasquine and I",
                    "invented. Using a unique blend of gnomic engineering",
                    "and human magic, we've launched a whole new kind of",
                    "tour experience.",
                ).also {
                    stage++
                }

            3 -> {
                setTitle(player, 2)
                sendDialogueOptions(
                    player,
                    "What would you like to say?",
                    "How did you make them?",
                    "What exactly can these things do?",
                ).also { stage++ }
            }
            4 ->
                when (buttonId) {
                    1 -> player("How did you make them?").also { stage++ }
                    2 -> player("What exactly can these things do?").also { stage = 16 }
                }
            5 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Oh, it's very clever. Sasquine and I saw some men",
                    "riding around on big magic carpet thingies in the desert",
                    "south of Al Kharid and we couldn't resist 'borrowing'",
                    "one or two.",
                ).also {
                    stage++
                }
            6 -> player("You 'borrowed' some magic carpets?").also { stage++ }
            7 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Well, we didn't exactly ask permission but they're for",
                    "research purposes. That's allowed! Anyway, we're going",
                    "off topic.",
                ).also {
                    stage++
                }
            8 -> player("Alright, so you stole some magic carpets.", "What did you do then?").also { stage++ }
            9 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "We managed to interface the carpets with the latest in",
                    "gnome glider technology. My brother's an expert flight",
                    "engineer; he helped a lot. The result is what you see",
                    "here; the gnomecopter.",
                ).also {
                    stage++
                }
            10 -> player("It doesn't look much like a gnome glider or a magic", "carpet.").also { stage++ }
            11 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "The rotors are mostly made from the fabric of the",
                    "carpets. Our expert gnomic engineering makes it far",
                    "easier to handle than the original carpet and its magic",
                    "allows the gnomecopters to do things that normal gliders",
                ).also {
                    stage++
                }
            12 -> npc(FaceAnim.OLD_NORMAL, "can't.").also { stage++ }
            13 -> player("Such a letting non-members have a preview of", "members' areas.").also { stage++ }
            14 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Precisely! So, is there anything else you'd like to know?",
                ).also { stage = 0 }
            15 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Oh no, I coudn't part with one of those.",
                    "They're very hard to make.",
                ).also {
                    stage =
                        0
                }
            16 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Well, this may sound strange but we've using them to",
                    "show members' areas to people on non-member's worlds.",
                ).also {
                    stage++
                }
            17 ->
                player(
                    "Hang on, we're on a members' world here!",
                    "I could just go to these places myself.",
                ).also { stage++ }
            18 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Yes, you probably could. That said we're currently",
                    "offering our service for free and you can't beat that",
                    "price! Chat to Sasquine to get going.",
                ).also {
                    stage++
                }
            19 ->
                player("I'll have a think about it.").also {
                    setAttribute(player, "/save:gnomecopter:unlocked", true)
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HIERONYMUS_AVLAFRIM_7420)
    }
}
