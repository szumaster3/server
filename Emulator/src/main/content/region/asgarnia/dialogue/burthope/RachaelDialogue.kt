package content.region.asgarnia.dialogue.burthope

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.START_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Rachael dialogue.
 */
@Initializable
class RachaelDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            START_DIALOGUE -> npcl(FaceAnim.FRIENDLY, "Welcome to the Burthorpe Games Rooms!").also { stage++ }
            1 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "How do I play board games?", 10),
                    Topic(FaceAnim.FRIENDLY, "What games can I play?", 30),
                    Topic(FaceAnim.FRIENDLY, "Can I buy a drink please?", 20),
                    Topic(FaceAnim.FRIENDLY, "Thanks!", END_DIALOGUE),
                )
            10 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "You can challenge someone to a game anywhere in the games rooms by using the right click option. Choose the type of game and then choose the options you want such as time per move. If you want to play a particular",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "game there are challenge rooms dedicated to that game. In the challenge rooms you can see other players ranks by right clicking them, their skill will be displayed instead of their combat level. Once you have enough",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "experience you will be able to use the challenge rooms on the first floor! If your opponent accepts the challenge you will be taken to one of the tables in the main room where you will play your game of choice.",
                ).also {
                    stage++
                }
            13 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Once you have finished your game you will go back to the challenge room where you can challenge again!",
                ).also {
                    stage =
                        1
                }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Certainly ${if (player.isMale) "sir" else "miss"}, our speciality is Asgarnian Ale, we also serve Wizard's Mind Bomb and Dwarven Stout.",
                ).also {
                    stage++
                }
            21 -> {
                end()
                openNpcShop(player, npc.id)
            }
            30 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Currently we offer Draughts, Runelink, Runesquares, and Runeversi.",
                ).also { stage++ }
            31 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Draughts?", 35),
                    Topic(FaceAnim.FRIENDLY, "Runelink?", 36),
                    Topic(FaceAnim.FRIENDLY, "Runesquares?", 37),
                    Topic(FaceAnim.FRIENDLY, "Runeversi?", 38),
                )
            35 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Draughts uses standard rules, apart from: a draw is declared if no piece has been taken or promoted for forty moves. To play Draughts go to the challenge room in the SW corner.",
                ).also {
                    stage =
                        1
                }
            36 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yup, you have to get four runes in row. The challenge room for Runelink is in the SE corner.",
                ).also {
                    stage =
                        1
                }
            37 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yes, you take it in turns to add a line with the goal of making squares. Everytime you make a square you take another turn. The challenge room for Runesquares is in the SW corner.",
                ).also {
                    stage =
                        1
                }
            38 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yep, the aim is to have more of your runes on the board than your opponent. You can take your opponents pieces by trapping them between your own. The challenge room for Runeversi is in the SE corner.",
                ).also {
                    stage =
                        1
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SAM_1357, NPCs.RACHAEL_1358)
    }
}
