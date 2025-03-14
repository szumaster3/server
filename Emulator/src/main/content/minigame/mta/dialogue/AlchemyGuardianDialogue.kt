package content.minigame.mta.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AlchemyGuardianDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.HALF_GUILTY, "Hi.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NORMAL, "Greetings young one. What wisdom do you seek?").also { stage++ }
            1 ->
                options(
                    "What do I have to do in this room?",
                    "What are the rewards?",
                    "Got any tips that may help me?",
                    "Thanks, bye!",
                ).also {
                    stage++
                }
            2 ->
                when (buttonId) {
                    1 -> player("What do I have to do in this room?").also { stage++ }
                    2 -> player("What are the rewards?").also { stage = 7 }
                    3 -> player("Got any tips that may help me?").also { stage = 9 }
                    4 -> player("Thanks, bye!").also { stage = END_DIALOGUE }
                }
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "In this room you will see various cupboards. It is your",
                    "task to search the cupboards to find items to turn into",
                    "gold using your low or high alchemy spells. You must",
                    "deposit the money in the receptacle at the end of the",
                ).also {
                    stage++
                }
            4 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "hall in order to receive your Alchemist Pizazz Points,",
                    "otherwise the money will be taken from you as you",
                    "leave through the portal. This money is used for the",
                    "upkeep of the training arena as well as magic shops all",
                ).also {
                    stage++
                }
            5 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "around Keldagrim. Keep an eye on the cost of each",
                    "items as these will change from time-to-time, as will the",
                    "location of the items. Occasionally one of the items will",
                    "be indicated as costing no runestones to convert to",
                ).also {
                    stage++
                }
            6 -> npc(FaceAnim.OLD_NORMAL, "money.").also { stage = 0 }
            7 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "You will get experience from casting the alchemist",
                    "spells, as well as 1 Alchemist Pizazz Point for every 100",
                    "coins you deposit, and 10% of the coins you deposit will",
                    "be given to you as you leave. Keep in mind that you",
                ).also {
                    stage++
                }
            8 ->
                npc(FaceAnim.OLD_NORMAL, "will not be able to take more than 1000 coins back out", "with you.").also {
                    stage =
                        0
                }
            9 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "You must remember to keep ane eye on the various",
                    "costs of the items. If you watch the movements of the",
                    "other players, you might be able to guess which are the",
                    "best places to visit. You will get 1 Pizazz Point for",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "every 100 coins, so if you have 190 coins, why not get",
                    "an extra 10?",
                ).also {
                    stage++
                }
            11 -> player("I see.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Oh, and a word of warning: should you decide to leave",
                    "this room by a method other than the exit portals, you",
                    "will be teleported to the entrance and have any items",
                    "that you picked up in the room removed.",
                ).also {
                    stage =
                        0
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ALCHEMY_GUARDIAN_3099)
    }
}
