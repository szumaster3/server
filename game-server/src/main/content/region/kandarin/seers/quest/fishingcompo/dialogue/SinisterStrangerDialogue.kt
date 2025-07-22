package content.region.kandarin.seers.quest.fishingcompo.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable

/**
 * Represents the Sinister Stranger dialogue.
 *
 * ```
 * NPC Wrapper ID: 226
 * Varbit ID: 2054
 * set values:
 *   0 = NPC_ID: 3677
 *   1 = NPC_ID: 3678
 * ```
 *
 * # Relations
 * - [Fishing Contest][content.region.kandarin.quest.fishingcompo.FishingContest]
 */
@Initializable
class SinisterStrangerDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("...")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            // Meeting the Competition.
            0 -> options("...?", "Who are you?", "So... you like fishing?").also { stage++ }
            1 -> when (buttonId) {
                1 -> player("...?").also { stage = 10 }
                2 -> player("Who are you?").also { stage = 20 }
                3 -> player("So... you like fishing?").also { stage = 30 }
            }

            2 -> options("You're a vampire aren't you?", "Is it nice there?", "So you like fishing?").also { stage++ }
            3 -> when (buttonId) {
                1 -> player("You're a vampire aren't you?").also { stage = 21 }
                2 -> player("Is it nice there?").also { stage = 50 }
                3 -> player("So you like fishing?").also { stage = 30 }
            }

            4 -> options(
                "You're a vampire aren't you?", "So you like fishing?", "Well, good luck with the fishing."
            ).also { stage++ }

            5 -> when (buttonId) {
                1 -> player("You're a vampire aren't you?").also { stage = 21 }
                2 -> player("So you like fishing?").also { stage = 30 }
                3 -> player("Well, good luck with the fishing.").also { stage = 60 }
            }

            6 -> options(
                "You're a vampire aren't you?",
                "If you get thirsty you should drink something.",
                "Well, good luck with the fishing."
            ).also { stage++ }

            7 -> when (buttonId) {
                1 -> player("You're a vampire aren't you?").also { stage = 21 }
                2 -> player("If you get thirsty you should drink something.").also { stage = 40 }
                3 -> player("Well, good luck with the fishing.").also { stage = 60 }
            }

            9 -> end()

            10 -> npc("...").also { stage++ }
            11 -> player("......?").also { stage++ }
            12 -> npc("......").also { stage = 9 }

            20 -> npc("My name is Vlad.", "I come from far avay,", "vere the sun iz not so bright.").also { stage = 2 }
            21 -> npc("Just because I can't stand ze smell ov garlic", "and I don't like bright sunlight doesn't", "necessarily mean I'm ein vampire!").also { stage = 9 }
            30 -> npc("My doctor told me to take up ein velaxing hobby.", "Vhen I am stressed I tend to get ein little...").also { stage++ }
            31 -> npc("...thirsty.").also { stage = 6 }
            40 -> npc("I tsink I may do zat soon...").also { stage = 9 }
            50 -> npc("It is vonderful!", "Zev omen are beautiful und ze nights are long!").also { stage = 4 }
            60 -> npc("Luck haz notsing to do vith it.", "It is all in ze technique.").also { stage = 9 }

        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(226)
    // NPCs.SINISTER_STRANGER_3677, NPCs.SINISTER_STRANGER_3678
}
