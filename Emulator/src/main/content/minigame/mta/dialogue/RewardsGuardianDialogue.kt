package content.minigame.mta.dialogue

import core.api.addItemOrDrop
import core.api.openInterface
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class RewardsGuardianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (args.size > 1) {
            npc(FaceAnim.OLD_NORMAL, "Have you spoken to my fellow guardian downstairs?").also { stage = 3 }
            return true
        }
        player("Hi.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        if (!player.getSavedData().activityData.isStartedMta) {
            when (stage) {
                0 ->
                    npc(
                        FaceAnim.OLD_NORMAL,
                        "Greetings. Have you spoken to my fellow Guardian",
                        "downstairs?",
                    ).also { stage++ }
                1 -> player("Nope.").also { stage++ }
                2 -> npc(FaceAnim.OLD_NORMAL, "Well, you need to talk to him first.").also { stage = END_DIALOGUE }
                3 -> player("Nope.").also { stage++ }
                4 -> npc(FaceAnim.OLD_NORMAL, "Well, you need to talk to him first.").also { stage = END_DIALOGUE }
            }
            return true
        }
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Greetings. What wisdom do you seek?").also { stage++ }
            1 ->
                options(
                    "Who are you?",
                    "Can I trade my Pizazz Points please?",
                    "Got anything else I can buy?",
                ).also { stage++ }
            2 ->
                when (buttonId) {
                    1 -> player("Who are you?").also { stage++ }
                    2 -> player("Can I trade my Pizazz Points please?").also { stage = 5 }
                    3 -> player("Got anything else I can buy?").also { stage = 7 }
                }

            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Me? I'm here to grant you rewards for any of the",
                    "Pizazz Points you may have earned in this training",
                    "arena. Like my fellow Guardians, I am part of the",
                    "arena and live to ensure its safe running.",
                ).also {
                    stage++
                }
            4 -> player("I see.").also { stage = 1 }
            5 -> npc(FaceAnim.OLD_NORMAL, "Why of course.").also { stage++ }
            6 -> {
                end()
                openInterface(player, Components.MAGICTRAINING_SHOP_197)
            }
            7 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Well, we do stock a special book that you may be",
                    "interested in, which provides a comprehensive guide to",
                    "this training arena. It costs 200gp. Would like",
                    "one?",
                ).also {
                    stage++
                }
            8 -> {
                end()
                if (!removeItem(player, Item(Items.COINS_995, 200))) {
                    sendMessage(player, "You don't have enough coins for that.")
                } else {
                    addItemOrDrop(player, Items.ARENA_BOOK_6891)
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.REWARDS_GUARDIAN_3103)
    }
}
