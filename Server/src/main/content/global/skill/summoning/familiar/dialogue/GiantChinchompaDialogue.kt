package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Giant Chinchompa familiar dialogues.
 */
@Initializable
class GiantChinchompaDialogue : Dialogue {
    private var branch: Int = -1

    override fun newInstance(player: Player?) = GiantChinchompaDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..4).random()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Half a pound of tuppenny rice, half a pound of treacle...")
            1 -> playerl(FaceAnim.HALF_ASKING, "A brown balloon?")
            2 -> playerl(FaceAnim.HALF_ASKING, "Bomb? Bang? Boom? Blowing-up-little-chipmunk?")
            3 -> playerl(FaceAnim.HALF_ASKING, "Well done. Anything in it?")
            4 -> playerl(FaceAnim.HALF_ASKING, "What is it, ratty?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "I hate it when you sing that song."); stage++ }
                1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "...that's the way the money goes..."); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "Couldn't you sing 'Kumbaya' or something?"); stage++ }
                3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "...BANG, goes the chinchompa!"); stage++ }
                4 -> { playerl(FaceAnim.HALF_ASKING, "Sheesh."); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "A chinchompa! Pull my finger."); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "I'm not pulling your finger."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Nothing will happen. Truuuuust meeeeee."); stage++ }
                3 -> { playerl(FaceAnim.FRIENDLY, "Oh, go away."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "No. Body odour. You should wash a bit more."); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "Well, that was pleasant. You don't smell all that great either, you know."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Stop talking, stop talking! Your breath stinks!"); stage++ }
                3 -> { playerl(FaceAnim.HALF_ASKING, "We're never going to get on, are we?"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hmmm. Let me see. It seems to be full of some highly sought after, very expensive...chinchompa breath!"); stage++ }
                1 -> { playerl(FaceAnim.FRIENDLY, "No, don't pop it!"); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "You just cannot help yourself, can you?"); stage = END_DIALOGUE }
            }

            4 -> when (stage) {
                0 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You got something in your backpack that you'd like to tell me about?"); stage++ }
                1 -> { playerl(FaceAnim.HALF_ASKING, "I was wondering when you were going to bring up the chinchompa. I'm sure they like it in my inventory."); stage++ }
                2 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Did they not teach you anything in school? Chinchompas die in hot bags. You know what happens when chinchompas die. Are you attached to your back?"); stage++ }
                3 -> { playerl(FaceAnim.HALF_ASKING, "Medically, yes. And I kind of like it too. I get the point."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(NPCs.GIANT_CHINCHOMPA_7353, NPCs.GIANT_CHINCHOMPA_7354)
}
