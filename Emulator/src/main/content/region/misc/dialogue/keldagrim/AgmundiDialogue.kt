package content.region.misc.dialogue.keldagrim

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AgmundiDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.OLD_NOT_INTERESTED, "Oh no, not another human... what do you want then?").also { stage++ }
            1 -> player(FaceAnim.ASKING, "Oh, do you get humans here often?").also { stage++ }
            2 -> npc(FaceAnim.OLD_NORMAL, "Not that often, no, but sometimes.").also { stage++ }
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Of course, since you people are too big for dwarven",
                    "clothes, they typically don't stay very long.",
                ).also {
                    stage++
                }
            4 -> player(FaceAnim.SUSPICIOUS, "Why don't you make bigger clothes then?").also { stage++ }
            5 ->
                npc(
                    FaceAnim.OLD_NOT_INTERESTED,
                    "What'd be the point? Besides, I don't make",
                    "these clothes myself.",
                ).also {
                    stage++
                }
            6 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Who makes these clothes then?", 10),
                    Topic(FaceAnim.FRIENDLY, "I still want to buy your clothes.", 8, true),
                    Topic(FaceAnim.FRIENDLY, "So do you have any quests for me?", 20),
                )
            8 -> {
                end()
                openNpcShop(player, NPCs.AGMUNDI_2161)
            }
            10 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "Oh, my sister, she lives in Keldagrim-East.",
                    "Has a little stall on the other side of",
                    "the Kelda.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.OLD_DEFAULT,
                    "If she only worked a little harder, like me,",
                    "she wouldn't have to live in the sewers of the city.",
                    "Shame really.",
                ).also {
                    stage++
                }
            12 -> player(FaceAnim.FRIENDLY, "The sewers? Your sister lives in the sewers?").also { stage++ }
            13 ->
                npc(
                    FaceAnim.OLD_SAD,
                    "Keldagrim-East, such a ghastly place.",
                    "Not civil, polite and clean like we are in the West.",
                ).also {
                    stage++
                }
            14 -> player(FaceAnim.SUSPICIOUS, "Uh-huh.").also { stage = END_DIALOGUE }
            20 -> npc(FaceAnim.OLD_NOT_INTERESTED, "Quests? Why would I have any quests?").also { stage++ }
            21 -> player(FaceAnim.FRIENDLY, "Oh, just anything to do would be fine.").also { stage++ }
            22 ->
                npc(
                    FaceAnim.OLD_NOT_INTERESTED,
                    "No, not right now... maybe I'll have something",
                    "for you to do later, but nothing at the moment.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return AgmundiDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AGMUNDI_2161)
    }
}
