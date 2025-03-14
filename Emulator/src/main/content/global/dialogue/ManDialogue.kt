package content.global.dialogue

import core.api.item.produceGroundItem
import core.api.sendMessage
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ManDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (npc == null) return false
        if ((args.size > 1 && args[1] is Item) &&
            (args[1] as Item) == Item(Items.CIDER_5763) &&
            player.inventory.remove(Item(Items.CIDER_5763))
        ) {
            if (!player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.isComplete(0, 6)) {
                if (player.getAttribute("diary:seers:pub-cider", 0) >= 4) {
                    setAttribute(player, "/save:diary:seers:pub-cider", 5)
                    player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)!!.updateTask(player, 0, 6, true)
                } else {
                    setAttribute(
                        player,
                        "/save:diary:seers:pub-cider",
                        player.getAttribute("diary:seers:pub-cider", 0) + 1,
                    )
                }
            }
            npc("Ah, a glass of cider, that's very generous of you. I", "don't mind if I do. Thanks!").also {
                stage = END_DIALOGUE
            }
            return true
        }
        player(FaceAnim.HALF_GUILTY, "Hello, how's it going?")
        stage = RandomFunction.random(0, 5)
        if (stage == 1) {
            stage = 0
        }
        val rand = RandomFunction.random(0, 1000)
        if (rand == 1) {
            sendMessage(player, "Something drops out of Man's pocket onto the floor.")
            sendMessage(player, "It looks like a piece of paper.")
            produceGroundItem(player, Items.FLIER_956, 1, npc.location)
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.HALF_GUILTY, "I'm very well thank you.").also { stage = END_DIALOGUE }
            2 -> npc(FaceAnim.HALF_GUILTY, "Who are you?").also { stage = 6 }
            3 -> npc(FaceAnim.HALF_GUILTY, "I'm fine, how are you?").also { stage = 8 }
            4 -> npc(FaceAnim.HALF_GUILTY, "No, I don't want to buy anything!").also { stage = END_DIALOGUE }
            5 ->
                npc(FaceAnim.HALF_GUILTY, "I think we need a new king. The one we've got isn't", "very good.").also {
                    stage =
                        END_DIALOGUE
                }
            6 -> player(FaceAnim.HALF_GUILTY, "I'm a bold adventurer.").also { stage++ }
            7 -> npc(FaceAnim.HALF_GUILTY, "Ah, a very noble profession.").also { stage = END_DIALOGUE }
            8 -> player(FaceAnim.HALF_GUILTY, "Very well thank you.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return ManDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.MAN_1,
            NPCs.MAN_2,
            NPCs.MAN_3,
            NPCs.WOMAN_4,
            NPCs.WOMAN_5,
            NPCs.WOMAN_6,
            NPCs.MAN_16,
            NPCs.MAN_24,
            NPCs.WOMAN_25,
            NPCs.MAN_170,
            NPCs.MAN_351,
            NPCs.WOMAN_352,
            NPCs.WOMAN_353,
            NPCs.WOMAN_354,
            NPCs.MAN_359,
            NPCs.WOMAN_360,
            NPCs.WOMAN_361,
            NPCs.WOMAN_362,
            NPCs.WOMAN_363,
            NPCs.MAN_726,
            NPCs.MAN_727,
            NPCs.MAN_728,
            NPCs.MAN_729,
            NPCs.MAN_730,
            NPCs.MAN_1086,
            NPCs.MAN_2675,
            NPCs.WOMAN_2776,
            NPCs.MAN_3224,
            NPCs.MAN_3225,
            NPCs.WOMAN_3227,
            NPCs.MAN_5923,
            NPCs.WOMAN_5924,
            NPCs.MAN_7873,
            NPCs.MAN_7874,
            NPCs.MAN_7875,
            NPCs.MAN_7876,
            NPCs.MAN_7877,
            NPCs.MAN_7878,
            NPCs.MAN_7879,
            NPCs.WOMAN_7880,
            NPCs.WOMAN_7881,
            NPCs.WOMAN_7882,
            NPCs.WOMAN_7883,
            NPCs.WOMAN_7884,
            NPCs.WOMAN_7925,
        )
    }
}
