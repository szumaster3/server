package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Spirit Cockatrice familiar dialogue.
 */
@Initializable
class SpiritCockatriceDialogue(player: Player? = null) : Dialogue(player) {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = SpiritCockatriceDialogue(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (inEquipment(player, Items.MIRROR_SHIELD_4156, 1)) {
            npcl(FaceAnim.OLD_NORMAL, "You know, I'm sensing some trust issues here.")
            stage = 0
            branch = 0
            return true
        }

        branch = (0..3).random()

        when (branch) {
            0 -> npcl(FaceAnim.OLD_NORMAL, "Is this what you do for fun?")
            1 -> playerl(FaceAnim.FRIENDLY, "You know, I think I might train as a hypnotist.")
            2 -> npcl(FaceAnim.OLD_NORMAL, "Come on, let's have a staring contest!")
            3 -> npcl(FaceAnim.OLD_NORMAL, "You know, sometimes I don't think we're good friends.")
        }

        stage = when (branch) {
            0 -> 11
            1 -> 14
            2 -> 21
            3 -> 24
            else -> 0
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "I'm not sure I know what you are talking about."); stage++ }
                1 -> { npcl(FaceAnim.OLD_NORMAL, "What are you holding?"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "A mirror shield."); stage++ }
                3 -> { npcl(FaceAnim.OLD_NORMAL, "And what do those do?"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Mumblemumble..."); stage++ }
                5 -> { npcl(FaceAnim.OLD_NORMAL, "What was that?"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "It protects me from your gaze attack."); stage++ }
                7 -> { npcl(FaceAnim.OLD_NORMAL, "See! Why would you need one unless you didn't trust me?"); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Who keeps demanding that we stop and have staring contests?"); stage++ }
                9 -> { npcl(FaceAnim.OLD_NORMAL, "How about we drop this and call it even?"); stage++ }
                10 -> { playerl(FaceAnim.FRIENDLY, "Fine by me."); stage = END_DIALOGUE }
            }
            1 -> when (stage) {
                11 -> { playerl(FaceAnim.FRIENDLY, "Sometimes. Why, what do you do for fun?"); stage++ }
                12 -> { npcl(FaceAnim.OLD_NORMAL, "I find things and glare at them until they die!"); stage++ }
                13 -> { playerl(FaceAnim.FRIENDLY, "Well...everyone needs a hobby, I suppose."); stage = END_DIALOGUE }
            }
            2 -> when (stage) {
                14 -> { playerl(FaceAnim.HALF_ASKING, "Isn't that an odd profession for a cockatrice?"); stage++ }
                15 -> { npcl(FaceAnim.OLD_NORMAL, "Not at all! I've already been practicing!"); stage++ }
                16 -> { playerl(FaceAnim.FRIENDLY, "Oh, really? How is that going?"); stage++ }
                17 -> { npcl(FaceAnim.OLD_NORMAL, "Not good. I tell them to look in my eyes and that they are feeling sleepy."); stage++ }
                18 -> { playerl(FaceAnim.FRIENDLY, "I think I can see where this is headed."); stage++ }
                19 -> { npcl(FaceAnim.OLD_NORMAL, "And then they just lie there and stop moving."); stage++ }
                20 -> { playerl(FaceAnim.FRIENDLY, "I hate being right sometimes."); stage = END_DIALOGUE }
            }
            3 -> when (stage) {
                24 -> { playerl(FaceAnim.HALF_ASKING, "What do you mean?"); stage++ }
                25 -> { npcl(FaceAnim.OLD_NORMAL, "Well, you never make eye contact with me for a start."); stage++ }
                26 -> { playerl(FaceAnim.HALF_ASKING, "What happened the last time someone made eye contact with you?"); stage++ }
                27 -> { npcl(FaceAnim.CHILD_NORMAL, "Oh, I petrified them really good! Ooooh...okay, point taken."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SPIRIT_COCKATRICE_6875, NPCs.SPIRIT_COCKATRICE_6876)
}
