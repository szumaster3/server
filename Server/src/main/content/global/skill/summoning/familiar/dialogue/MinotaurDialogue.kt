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
 * Represents the Minotaurs familiar dialogues.
 */
@Initializable
class MinotaurDialogue : Dialogue {
    override fun newInstance(player: Player?): Dialogue = MinotaurDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    private var branch = -1

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        if (inEquipment(player, Items.GUTHANS_HELM_4724, 1)) {
            npcl(FaceAnim.FAMILIAR_NEUTRAL, "...")
            branch = 0
            stage = 0
            return true
        }

        branch = (Math.random() * 4).toInt() + 1
        stage = 0

        when (branch) {
            1 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "All this walking about is making me angry.")
            2 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Can you tell me why we're not fighting yet?")
            3 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hey, no-horns?")
            4 -> npcl(FaceAnim.FAMILIAR_NEUTRAL, "Hey no-horns!")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "What?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Are you having a laugh?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I'm not sure I know what you-"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Listen, no-horns, you have two choices: take off the horns yourself or I'll headbutt you until they fall off."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "Yessir."); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Good, no-horns. Let's not have this conversation again."); stage = END_DIALOGUE }
                }
            }
            1 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "You seem to be quite happy about that."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Yeah! There's nothing like getting a good rage on and then working it out on some no-horns."); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "I can't say I know what you mean."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Well I didn't think a no-horns like you would get it!"); stage = END_DIALOGUE }
                }
            }
            2 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.FRIENDLY, "Buck up; I'll find you something to hit soon."); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "You'd better, no-horns, because that round head of yours is looking mighty axeable."); stage = END_DIALOGUE }
                }
            }
            3 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Why do you keep calling me no-horns?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Do I really have to explain that?"); stage++ }
                    2 -> { playerl(FaceAnim.FRIENDLY, "No, thinking about it, it's pretty self-evident."); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Glad we're on the same page, no-horns."); stage++ }
                    4 -> { playerl(FaceAnim.FRIENDLY, "So, what did you want?"); stage++ }
                    5 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "I've forgotten, now. I'm sure it'll come to me later."); stage = END_DIALOGUE }
                }
            }
            4 -> {
                when (stage) {
                    0 -> { playerl(FaceAnim.HALF_ASKING, "Yes?"); stage++ }
                    1 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "Oh, I don't have anything to say, I was just yelling at you."); stage++ }
                    2 -> { playerl(FaceAnim.HALF_ASKING, "Why?"); stage++ }
                    3 -> { npcl(FaceAnim.FAMILIAR_NEUTRAL, "No reason. I do like to mess with the no-horns, though."); stage = END_DIALOGUE }
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray =
        intArrayOf(
            NPCs.BRONZE_MINOTAUR_6853,
            NPCs.BRONZE_MINOTAUR_6854,
            NPCs.IRON_MINOTAUR_6855,
            NPCs.IRON_MINOTAUR_6856,
            NPCs.STEEL_MINOTAUR_6857,
            NPCs.STEEL_MINOTAUR_6858,
            NPCs.MITHRIL_MINOTAUR_6859,
            NPCs.MITHRIL_MINOTAUR_6860,
            NPCs.ADAMANT_MINOTAUR_6861,
            NPCs.ADAMANT_MINOTAUR_6862,
            NPCs.RUNE_MINOTAUR_6863,
            NPCs.RUNE_MINOTAUR_6864
        )
}
