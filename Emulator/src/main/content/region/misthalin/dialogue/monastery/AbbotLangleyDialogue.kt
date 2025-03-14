package content.region.misthalin.dialogue.monastery

import core.api.*
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.NPCs

@Initializable
class AbbotLangleyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args.size >= 1) {
            if (args[0] is NPC) {
                npc = args[0] as NPC
            } else {
                npc("Only members of our order can go up there.")
                stage = 23
                return true
            }
        }
        npc("Greetings traveller.")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> {
                if (player.getSavedData().globalData.isJoinedMonastery()) {
                    options("Can you heal me? I'm injured.", "Isn't this place built a bit out the way?")
                } else {
                    options(
                        "Can you heal me? I'm injured.",
                        "Isn't this place built a bit out the way?",
                        "How do I get further into the monastery?",
                    )
                }
                stage = 1
            }

            1 ->
                when (buttonId) {
                    1 -> player("Can you heal me? I'm injured.").also { stage = 10 }
                    2 -> player("Isn't this place built a bit out of the way?").also { stage = 20 }
                    3 -> player("How do I get further into the monastery?").also { stage = 30 }
                }

            10 -> npc("Ok.").also { stage++ }
            11 -> {
                visualize(npc!!, Animations.CAST_SPELL_717, Graphics.MONK_CAST_HEAL_84)
                heal(player, (getStatLevel(player, Skills.HITPOINTS) * 0.20).toInt())
                sendDialogue(
                    player,
                    "Abbot Langley places his hands on your head. You feel a little better.",
                ).also { stage = END_DIALOGUE }
            }

            20 ->
                npc(
                    "We like it that way actually! We get disturbed less. We still",
                    "get rather a large amount of travellers looking for",
                    "sanctuary and healing here as it is!",
                ).also { stage = END_DIALOGUE }

            22 -> npc("Only members of our order can go up there.").also { stage++ }
            23 -> options("Well can I join your order?", "Oh, sorry.").also { stage++ }
            24 ->
                when (buttonId) {
                    1 -> player("Well can I join your order?").also { stage = 26 }
                    2 -> player("Oh, sorry.").also { stage = END_DIALOGUE }
                }

            26 ->
                if (getStatLevel(player, Skills.PRAYER) < 31) {
                    npc("No. I am sorry, but I feel you are not devout enough.")
                    stage++
                } else {
                    npc("Ok, I see you are someone suitable for our order. You", "may join.").also { stage = 31 }
                }

            27 -> {
                sendMessage(player, "You need a prayer level of 31 to join the order.")
                end()
            }

            30 ->
                npc(
                    "I'm sorry but only members of our order are allowed",
                    "in the second level of the monastery.",
                ).also { stage = 23 }

            31 -> {
                player.getSavedData().globalData.setJoinedMonastery(true)
                end()
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return AbbotLangleyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ABBOT_LANGLEY_801)
    }
}
