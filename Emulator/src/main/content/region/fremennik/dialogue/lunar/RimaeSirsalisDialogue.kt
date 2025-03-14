package content.region.fremennik.dialogue.lunar

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class RimaeSirsalisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player(FaceAnim.FRIENDLY, "Hello there.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What can you sell me?",
                    "Do you have any use for suqah teeth or hides?",
                    "It's a very interesting island you have here.",
                    "I'm good thanks, bye.",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> {
                        end()
                        openNpcShop(player, NPCs.RIMAE_SIRSALIS_4518)
                    }

                    2 ->
                        player(
                            FaceAnim.HALF_ASKING,
                            "Do you have any use for suqah teeth or hides?",
                        ).also { stage = 5 }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Why thank you. It's been our haven for a great many generations.",
                        ).also {
                            stage =
                                10
                        }
                    4 -> player(FaceAnim.FRIENDLY, "I'm good thanks, bye.").also { stage = END_DIALOGUE }
                }
            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Most certainly! The hides are great for making clothes and the teeth are particularly useful for broaches, necklaces and the like. But I won't accept them from anyone.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            10 -> playerl(FaceAnim.FRIENDLY, "Everything seems to have a magical feel to it.").also { stage++ }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course. We integrate magic into all areas of our lives. It is a part of everyone, so why deny it? It's best to make the most of this innate gift we have all been given.",
                ).also {
                    stage++
                }
            12 -> player(FaceAnim.FRIENDLY, "What sort of things do you use your magic for?").also { stage++ }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Take a look around. We use it in our day to day lives, from making a cup of tea to travelling around the island.",
                ).also {
                    stage++
                }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You see our ancestors were the ones that found the first rune essence and put it to use! The various factions were eventually created,",
                ).also {
                    stage++
                }
            15 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "with people having different ideas on how the essence should be used (or not in some cases!)",
                ).also {
                    stage++
                }
            16 -> player(FaceAnim.FRIENDLY, "What has kept you so secluded on this island?").also { stage++ }
            17 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It may be a bit beyond you, but although magic comes from within, we are all strongly linked to the moon and the",
                ).also {
                    stage++
                }
            18 -> npc(FaceAnim.FRIENDLY, "effects it has on us cannot be denied!").also { stage++ }
            19 -> player(FaceAnim.FRIENDLY, "It can't?").also { stage++ }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Of course not. This very island has a great link to our moon, which helps us understand ourselves - especially our dreams,",
                ).also {
                    stage++
                }
            21 -> npc(FaceAnim.FRIENDLY, "which is the path to understanding magic.").also { stage++ }
            22 ->
                playerl(FaceAnim.FRIENDLY, "I think I will just have to take your word on that.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return RimaeSirsalisDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.RIMAE_SIRSALIS_4518)
    }
}
