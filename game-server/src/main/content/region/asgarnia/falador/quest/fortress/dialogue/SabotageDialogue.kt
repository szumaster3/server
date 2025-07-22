package content.region.asgarnia.falador.quest.fortress.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.*

/**
 * Represents the Sabotage dialogue.
 *
 * # Relations
 * - [Black Knights Fortress][content.region.asgarnia.falador.quest.fortress.BlackKnightsFortress]
 */
@Initializable
class SabotageDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        if (args.size == 2) {
            sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.NEUTRAL, false, "Where has Greldo got to with that magic cabbage!")
            player.animate(Animation(Animations.MULTI_TAKE_832))
            playAudio(player, Sounds.BK_THROW_CABBAGE_1420)
            stage = 10
            return true
        }
        sendNPCDialogueLines(player, NPCs.BLACK_KNIGHT_CAPTAIN_610, FaceAnim.ASKING, false, "So... how's the secret weapon coming along?")
        stage = 0
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> sendNPCDialogue(player, NPCs.WITCH_611, "The invincibility potion is almost ready...", FaceAnim.HAPPY).also { stage++ }
            1 -> sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.HAPPY, false, "It's taken me FIVE YEARS, but it's almost ready.").also { stage++ }
            2 -> sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.NEUTRAL, false, "Greldo the Goblin here is just going to fetch the last", "ingredient for me.").also { stage++ }
            3 -> sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.NEUTRAL, false, "It's a special grown cabbage grown by my cousin", "Helda who lives in Draynor Manor.").also { stage++ }
            4 -> sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.NEUTRAL, false, "The soil there is slightly magical and it gives the", "cabbages slight magical properties....").also { stage++ }
            5 -> sendNPCDialogue(player, NPCs.WITCH_611, "...not to mention the trees!", FaceAnim.AMAZED).also { stage++ }
            6 -> sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.NEUTRAL, false, "Now remember Greldo, only a Draynor Manor", "cabbage will do! Don't get lazy and bring any old", "cabbage, THAT would ENTIRELY wreck the potion!").also { stage++ }
            7 -> sendNPCDialogue(player, NPCs.GRELDO_612, "Yeth, Mithreth.", FaceAnim.OLD_NORMAL).also { stage++ }
            8 -> {
                end()
                animate(player, Animations.GET_UP_FROM_LISTENING_TO_DOOR_4197)
                setQuestStage(player, Quests.BLACK_KNIGHTS_FORTRESS, 20)
            }

            10 -> {
                playAudio(player, Sounds.BK_CABBAGE_HIT_1414)
                sendNPCDialogue(player, NPCs.BLACK_KNIGHT_CAPTAIN_610, "What's that noise?", FaceAnim.ASKING).also { stage++ }
            }
            11 -> sendNPCDialogueLines(player, NPCs.WITCH_611, FaceAnim.NEUTRAL, false, "Hopefully Greldo with the cabbage... yes, look her it", "co....NOOOOOoooo!").also { stage++ }
            12 -> {
                playAudio(player, Sounds.BK_EXPLOSION_1418)
                sendNPCDialogue(player, NPCs.WITCH_611, "My potion!", FaceAnim.EXTREMELY_SHOCKED).also { stage++ }
            }
            13 -> sendNPCDialogue(player, NPCs.BLACK_KNIGHT_CAPTAIN_610, "Oh boy, this doesn't look good!", FaceAnim.WORRIED).also { stage++ }
            14 -> sendNPCDialogue(player, NPCs.BLACK_CAT_4607, "Meow!", FaceAnim.CHILD_FRIENDLY).also { stage++ }
            15 -> if (removeItem(player, Items.CABBAGE_1965)) {
                end()
                setVarbit(player, 2494, 1, true)
                setQuestStage(player, Quests.BLACK_KNIGHTS_FORTRESS, 30)
                player(FaceAnim.HAPPY, "Looks like my work here is done. Seems like that's", "successfully sabotaged their little secret weapon plan.")
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(992752973)
}