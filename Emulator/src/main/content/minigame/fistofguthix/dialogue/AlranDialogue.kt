package content.minigame.fistofguthix.dialogue

import core.api.sendDialogueOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AlranDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Welcome to this grand cave of Guthix, traveller.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "So, what is this place?",
                    "Who's the weird looking giant earwig?",
                    "I'd better get going.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player("So, what is this place?").also { stage++ }
                    2 -> player("Who's the weird looking giant earwig?").also { stage = 38 }
                    3 -> player("I'd better get going.").also { stage = END_DIALOGUE }
                }

            2 -> npc("This is a sacred site of Guthix.").also { stage++ }
            3 -> player("Why is it sacred?").also { stage++ }
            4 ->
                npc(
                    "Guthix places guardians in places of holy power.",
                    "There is one such guardian here.",
                ).also { stage++ }

            5 -> player("But what is special about this place?").also { stage++ }
            6 -> npc("It exudes a dangerous magical power that we have", "been entrusted to protect.").also { stage++ }
            7 -> player("Where did this power come from?").also { stage++ }
            8 -> npc("It comes from the centre of the big cave.").also { stage++ }
            9 -> player("What's special about the centre?").also { stage++ }
            10 -> npc("It's very magical.").also { stage++ }
            11 -> player("Yes, I get that. Why is it magical?").also { stage++ }
            12 -> npc("Something to do with the Fist of Guthix.").also { stage++ }
            13 -> player("And what is that, exactly?").also { stage++ }
            14 ->
                npc(
                    "The truth is, none of us really know. This place is special",
                    "to Guthix, because Fiara is here. And we know it has",
                    "something to do with the 'Fist of Guthix' because",
                    "Fiara occasionally mentions it.",
                ).also { stage++ }

            15 -> npc("But other than that, we only have theories.", "Do you want to hear about them?").also { stage++ }
            16 ->
                sendDialogueOptions(
                    player,
                    "I suppose so...",
                    "Getorix's theory.",
                    "Pontimer's theory.",
                    "Alran's theory.",
                    "I'd better get going.",
                ).also { stage++ }

            17 ->
                when (buttonId) {
                    1 ->
                        npc(
                            "Well, this is Getorix's silly theory. Listen to it, if you",
                            "must. At the end of the God Wars, Guthix returned",
                            "to Gielinor and was angered by the destruction caused",
                            "by the other gods in their vanity",
                        ).also { stage = 19 }

                    2 ->
                        npc(
                            "This is Pontimer's personal delusion on the subject.",
                            "After the God Wars, Guthix found something dangerous",
                            "here and used his power to protect it.",
                        ).also { stage = 28 }

                    3 ->
                        npc(
                            "This is what Alran thinks, for what it's worth...which",
                            " isn't very much, if you ask me. Guthix has apparently",
                            "left us a test. He deliberately created this site of power",
                            "as a test of individual strength and balance.",
                        ).also { stage = 35 }

                    4 -> player("I'd better get going.").also { stage = END_DIALOGUE }
                }

            19 ->
                npc(
                    "and quest for power. So, maybe he punched the ground",
                    "in anger, leaving an imprint of power on the ground",
                    "that has lasted ever since. Because he was angry at",
                    "the time, the power would have been tainted with",
                ).also { stage++ }

            20 ->
                npc(
                    "aggression, which is why he left Fiara as a guardian, to",
                    "make sure the power did not escape unfettered.",
                ).also { stage++ }

            21 -> player("Do you believe this theory?").also { stage++ }
            22 ->
                npc(
                    "No, I think it's rubbish; if it was true, that would mean",
                    "Guthix's hand would be a very strange shape. The",
                    "indentation in the centre of the cave doesn't look",
                    "like a fist to me.",
                ).also { stage++ }

            23 ->
                npc(
                    "Getorix has just let his imagination run away with him.",
                    "My theory is better.",
                ).also { stage++ }

            24 -> npc("Do you want to hear another theory?").also { stage++ }
            25 ->
                sendDialogueOptions(
                    player,
                    "I suppose so...",
                    "Pontimer's theory.",
                    "Alran's theory.",
                    "I'd better get going.",
                ).also { stage++ }

            26 ->
                when (buttonId) {
                    1 ->
                        npc(
                            "This is Pontimer's personal delusion on the subject.",
                            "After the God Wars, Guthix found something dangerous",
                            "here and used his power to protect it.",
                        ).also { stage = 28 }

                    2 ->
                        npc(
                            "This is what Alran thinks, for what it's worth...which",
                            " isn't very much, if you ask me. Guthix has apparently",
                            "left us a test. He deliberately created this site",
                            "of power as a test of individual strength and balance.",
                        ).also { stage = 35 }

                    3 -> player("I'd better get going.").also { stage = END_DIALOGUE }
                }

            28 -> player("What did he find?").also { stage++ }
            29 ->
                npc(
                    "I don't know for certain, but it must be under that strange",
                    "impression in the big cave. I think that mark is some sort of",
                    "ward that Guthix has left to protect the site.",
                ).also { stage++ }

            30 -> player("So where does the Fist of Guthix come into this, then?").also { stage++ }
            31 ->
                npc(
                    "Hmmm. I'm not really sure. I think that when Fiara refers",
                    "to the Fist of Guthix she is talking metaphorically about an",
                    "abstract concept of fist-ness rather than the clenched",
                    "right hand of Guthix itself.",
                ).also { stage++ }

            32 -> player("That sounds a bit...made up to me.").also { stage++ }
            33 ->
                npc(
                    "Why you impudent...bah. It's better than anything",
                    "that Getorix and Alran can come up with, anyway.",
                ).also { stage = 16 }

            35 -> player("Does Guthix do that sort of thing?").also { stage++ }
            36 ->
                npc(
                    "Well, no! I agree with you entirely. This theory has so",
                    "many holes that it makes no sense at all!",
                ).also { stage++ }

            37 -> npc("Do you want to hear another theory?").also { stage = 16 }
            38 -> npc("Shhh! Give Fiara some respect! She canhear you, ", "you know.").also { stage++ }
            39 -> player("Fiara? It has a name?").also { stage++ }
            40 -> npc("Of course, Guthix chooses names for all his guardians.").also { stage++ }
            41 -> player("If...err...Fiara is a Guardian, ", "what is she guarding?").also { stage++ }
            42 -> npc("She guards this site. It is a holy site of Guthix.").also { stage++ }
            43 -> options("How long has Fiara been here?", "I'd better get going.").also { stage++ }
            44 ->
                when (buttonId) {
                    1 -> player("How long has Fiara been here?").also { stage++ }
                    2 -> player("I'd better get going.").also { stage = END_DIALOGUE }
                }

            45 ->
                npc(
                    "Errr, ages. Since the end of the God Wars I think.",
                    "She doesn't talk about that much.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALRAN_7604)
    }
}
