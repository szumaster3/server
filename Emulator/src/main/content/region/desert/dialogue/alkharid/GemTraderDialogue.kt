package content.region.desert.dialogue.alkharid

import core.api.interaction.openNpcShop
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class GemTraderDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = (args[0] as NPC).getShownNPC(player)
        val qstage = player?.questRepository?.getStage(Quests.FAMILY_CREST) ?: -1
        if (qstage == 12) {
            npc("Good day to you, traveller. ", "Would you be interested in buying some gems?")
            stage = 1
        } else {
            npc("Good day to you, traveller. ", "Would you be interested in buying some gems?")
            stage = 2
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                options(
                    "Yes, please.",
                    "No, thank you.",
                    "I'm in search of a man named Avan Fitzharmon",
                ).also { stage = 10 }

            2 -> options("Yes, please.", "No, thank you.").also { stage = 20 }
            10 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.GEM_TRADER_540)
                    }

                    2 -> player("No, thank you.").also { stage = END_DIALOGUE }
                    3 ->
                        npc(
                            "Fitzharmon, eh? Hmmm... If I'm not mistaken, ",
                            "that's the family name of a member ",
                            "of the Varrockian nobility.",
                        ).also {
                            stage =
                                100
                        }
                }

            20 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.GEM_TRADER_540)
                    }
                    2 -> player("No, thank you.").also { stage = END_DIALOGUE }
                }
            100 ->
                npc(
                    "You know, I HAVE seen someone of that",
                    "persuasion around here recently...",
                    "wearing a 'poncey' yellow cape, he was.",
                ).also {
                    stage++
                }
            101 ->
                npc(
                    "Came in here all la-di-dah, high and mighty,",
                    "asking for jewellery made from 'perfect gold' - ",
                    "whatever that is - like 'normal' gold just isn't ",
                    "good enough for 'little lord fancy pants' there!",
                ).also {
                    stage++
                }
            102 ->
                npc(
                    "I told him to head to the desert 'cos ",
                    "I know there's gold out there, in them there sand dunes. ",
                    "And if it's not up to his lordship's ",
                    "high standards of 'gold perfection', then...",
                ).also {
                    stage++
                }
            103 -> npc("Well, maybe we'll all get lucky ", "and the scorpions will deal with him.").also { stage++ }
            104 -> {
                end()
                setQuestStage(player, Quests.FAMILY_CREST, 13)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return GemTraderDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GEM_TRADER_540)
    }
}
