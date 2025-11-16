package content.global.skill.summoning.familiar.dialogue

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
 * Represents the Obsidian Golem familiar dialogue.
 */
@Initializable
class ObsidianGolemDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = ObsidianGolemDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (inEquipment(player, Items.FIRE_CAPE_6570, 1)) {
            branch = 0
            stage = 0
            npcl(FaceAnim.FAMILIAR_NEUTRAL, "Truly, you are a powerful warrior, Master!")
            return true
        }

        branch = (Math.random() * 3).toInt() + 1
        stage = 0

        when (branch) {
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "How many foes have you defeated, Master?")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Master! We are truly a mighty duo!")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Do you ever doubt your programming, Master?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Let us go forth and prove our strength, Master!").also { stage++ }
                1 -> playerl(FaceAnim.HALF_ASKING, "Where would you like to prove it?").also { stage++ }
                2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "The caves of the TzHaar are filled with monsters for us to defeat, Master! TzTok-Jad shall quake in his slippers!").also { stage++ }
                3 -> playerl(FaceAnim.HALF_ASKING, "Have you ever met TzTok-Jad?").also { stage++ }
                4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Alas, Master, I have not. No Master has ever taken me to see him.").also { stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "Quite a few, I should think.").also { stage++ }
                1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Was your first foe as mighty as the volcano, Master?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Um, not quite.").also { stage++ }
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "I am sure it must have been a deadly opponent, Master!").also { stage++ }
                4 -> player(FaceAnim.FRIENDLY, "*Cough*", "It might have been a chicken.", "*Cough*").also { stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> playerl(FaceAnim.HALF_ASKING, "Do you think so?").also { stage++ }
                1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Of course, Master! I am programmed to believe so.").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Do you do anything you're not programmed to?").also { stage++ }
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "No, Master.").also { stage++ }
                4 -> playerl(FaceAnim.FRIENDLY, "I guess that makes things simple for you...").also { stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> playerl(FaceAnim.FRIENDLY, "I don't have programming. I can think about whatever I like.").also { stage++ }
                1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "What do you think about, Master?").also { stage++ }
                2 -> playerl(FaceAnim.FRIENDLY, "Oh, simple things: the sound of one hand clapping, where the gods come from...Simple things.").also { stage++ }
                3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Paradox check = positive. Error. Reboot.").also { stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.OBSIDIAN_GOLEM_7345, NPCs.OBSIDIAN_GOLEM_7346)
}
