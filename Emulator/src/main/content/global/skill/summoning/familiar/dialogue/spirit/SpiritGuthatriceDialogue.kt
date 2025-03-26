package content.global.skill.summoning.familiar.dialogue.spirit

import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * The type Spirit guthatrice dialogue.
 */
@Initializable
class SpiritGuthatriceDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return SpiritGuthatriceDialogue(player)
    }

    /**
     * Instantiates a new Spirit guthatrice dialogue.
     */
    constructor()

    /**
     * Instantiates a new Spirit guthatrice dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        if (inEquipment(player, Items.MIRROR_SHIELD_4156, 1)) {
            npcl(FaceAnim.OLD_NORMAL, "You know, I'm sensing some trust issues here.")
            stage = 0
            return true
        }
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npcl(FaceAnim.OLD_NORMAL, "Is this what you do for fun?")
                stage = 11
            }

            1 -> {
                playerl(FaceAnim.FRIENDLY, "You know, I think I might train as a hypnotist.")
                stage = 15
            }

            2 -> {
                npcl(FaceAnim.OLD_NORMAL, "Come on, lets have a staring contest!")
                stage = 23
            }

            3 -> {
                npcl(FaceAnim.OLD_NORMAL, "You know, sometimes I don't think we're good friends.")
                stage = 27
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "I'm not sure I know what you are talking about.")
                stage++
            }

            1 -> {
                npcl(FaceAnim.OLD_NORMAL, "What are you holding?")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "A mirror shield.")
                stage++
            }

            3 -> {
                npcl(FaceAnim.OLD_NORMAL, "And what do those do?")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Mumblemumble...")
                stage++
            }

            5 -> {
                npcl(FaceAnim.OLD_NORMAL, "What was that?")
                stage++
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "It protects me from your gaze attack.")
                stage++
            }

            7 -> {
                npcl(FaceAnim.OLD_NORMAL, "See! Why would you need one unless you didn't trust me?")
                stage++
            }

            8 -> {
                playerl(FaceAnim.FRIENDLY, "Who keeps demanding that we stop and have staring contests?")
                stage++
            }

            9 -> {
                npcl(FaceAnim.OLD_NORMAL, "How about we drop this and call it even?")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Fine by me.")
                stage = END_DIALOGUE
            }

            11 -> {
                npcl(FaceAnim.HALF_ASKING, "Is this what you do for fun?")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "Sometimes. Why, what do you do for fun?")
                stage++
            }

            13 -> {
                npcl(FaceAnim.FRIENDLY, "I find things and glare at them until they die!")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "Well...everyone needs a hobby, I suppose.")
                stage = END_DIALOGUE
            }

            15 -> {
                playerl(FaceAnim.FRIENDLY, "You know, I think I might train as a hypnotist.")
                stage++
            }

            16 -> {
                playerl(FaceAnim.HALF_ASKING, "Isn't that an odd profession for a cockatrice?")
                stage++
            }

            17 -> {
                npcl(FaceAnim.FRIENDLY, "Not at all! I've already been practicing!")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, really? How is that going?")
                stage++
            }

            19 -> {
                npcl(FaceAnim.FRIENDLY, "Not good. I tell them to look in my eyes and that they are feeling sleepy.")
                stage++
            }

            20 -> {
                playerl(FaceAnim.FRIENDLY, "I think I can see where this is headed.")
                stage++
            }

            21 -> {
                npcl(FaceAnim.FRIENDLY, "And then they just lie there and stop moving.")
                stage++
            }

            22 -> {
                playerl(FaceAnim.FRIENDLY, "I hate being right sometimes.")
                stage = END_DIALOGUE
            }

            23 -> {
                npcl(FaceAnim.FRIENDLY, "Come on, lets have a staring contest!")
                stage++
            }

            24 -> {
                playerl(FaceAnim.FRIENDLY, "You win!")
                stage++
            }

            25 -> {
                npcl(FaceAnim.FRIENDLY, "Yay! I win again!")
                stage++
            }

            26 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, it's no contest alright.")
                stage = END_DIALOGUE
            }

            27 -> {
                npcl(FaceAnim.FRIENDLY, "You know, sometimes I don't think we're good friends.")
                stage++
            }

            28 -> {
                playerl(FaceAnim.HALF_ASKING, "What do you mean?")
                stage++
            }

            29 -> {
                npcl(FaceAnim.FRIENDLY, "Well, you never make eye contact with me for a start.")
                stage++
            }

            30 -> {
                playerl(FaceAnim.HALF_ASKING, "What happened the last time someone made eye contact with you?")
                stage++
            }

            31 -> {
                npcl(FaceAnim.CHILD_NORMAL, "Oh, I petrified them really good! Ooooh...okay, point taken.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SPIRIT_GUTHATRICE_6877, NPCs.SPIRIT_GUTHATRICE_6878)
    }
}
