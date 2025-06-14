package content.region.desert.pollnivneach.dialogue

import content.data.GameAttributes
import content.region.misthalin.draynor_village.quest.swept.plugin.SweptUtils
import core.api.*
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AliTheHagDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (isQuestComplete(player, Quests.SWEPT_AWAY) && getDynLevel(player, Skills.MAGIC) > 33 && inInventory(
                player,
                Items.BROOMSTICK_14057
            ) && getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_HETTY_ENCH, false)
        ) {
            options("Good day, Ali.", "Could you enchant this broom for me?")
            stage = 10
        } else if (hasRequirement(player, Quests.THE_FEUD)) {
            player("Good day, Hag.").also { stage = 20 }
        } else {
            player(FaceAnim.FRIENDLY, "Good day, old hag.")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.LAUGH, "Old hag indeed! I have a name you know!").also { stage++ }
            1 -> player(FaceAnim.THINKING, "Let me guess it wouldn't be Ali would it?").also { stage++ }
            2 -> npc(
                FaceAnim.ANNOYED,
                "Well how else would you abbreviate Alice then?",
                "And no, you can't call me Al!",
            ).also {
                stage++
            }

            3 -> npc(
                FaceAnim.ASKING,
                "Now what do you want from the Old hag of Pollnivneach?",
                "To hex someone? Power, beauty, eternal youth",
                "or something else drab like that?",
            ).also {
                stage++
            }

            4 -> player(
                FaceAnim.HALF_GUILTY,
                "Actually none of those, I'm new in town and",
                "I just wanted to get to know the locals.",
            ).also {
                stage++
            }

            5 -> npc(
                FaceAnim.ANNOYED,
                "I'm busy brewing potions, so if you ",
                "disturb me again without reason,",
                "I will turn you into a frog!",
            ).also {
                stage++
            }

            6 -> player(FaceAnim.SCARED, "Oh I'm sorry I won't let it happen again.").also { stage = END_DIALOGUE }
            10 -> when (buttonId) {
                1 -> if (hasRequirement(player, Quests.THE_FEUD)) {
                    player("Good day, Hag.").also { stage = 20 }
                } else {
                    player(FaceAnim.FRIENDLY, "Good day, old hag.").also { stage = 0 }
                }

                2 -> player("Could you enchant this broom for me?").also { stage = 25 }
            }

            20 -> npcl(FaceAnim.HALF_GUILTY, "Good day to you too, Blockhead.").also { stage++ }
            21 -> npcl(
                FaceAnim.HALF_GUILTY,
                "Now what do you want from the old hag of Pollnivneach? Hexes? Curses? Eternal youth? Poisons? You name it, I can brew it.",
            ).also {
                stage++
            }

            22 -> playerl(
                FaceAnim.HALF_GUILTY,
                "Actually I don't want any of that stuff, I'm new in town and I wanted to get to know the locals.",
            ).also {
                stage++
            }

            23 -> npcl(
                FaceAnim.HALF_GUILTY,
                "I'm busy brewing potions, so if you disturb me again without reason, I will turn you into a frog!",
            ).also {
                stage++
            }

            24 -> playerl(FaceAnim.HALF_GUILTY, "Sorry! I won't let it happen again.").also { stage = END_DIALOGUE }
            25 -> npc(
                "Ah, this is Maggie's broom, is it? Yes, I can enchant it",
                "for you. Just one moment",
            ).also { stage++ }

            26 -> {
                end()
                lock(player, 1)
                visualize(player, -1, SweptUtils.BROOM_ENCHANTMENT_GFX)

                removeAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_HETTY_ENCH)
                sendDoubleItemDialogue(player, -1, Items.BROOMSTICK_14057, "You receive 1,997 Magic experience.")
                rewardXP(player, Skills.MAGIC, 1997.0)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AliTheHagDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.ALI_THE_HAG_1871)
}
