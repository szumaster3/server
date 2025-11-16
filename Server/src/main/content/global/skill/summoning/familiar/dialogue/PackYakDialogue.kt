package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Pack Yak familiar dialogue.
 */
@Initializable
class PackYakDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = PackYakDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (Math.random() * 3).toInt()
        stage = 0

        when (branch) {
            0 -> npcl(FaceAnim.CHILD_NORMAL, "Barroobaroooo baaaaaaaaarooo...")
            1 -> npcl(FaceAnim.CHILD_NORMAL, "Baroo barrrooooo barooobaaaarrroooo...")
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Barooo, barrrooooooooo...")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "(When I came of age, the herd pushed me out. It was always this way for an adult whose father still lived.").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "I would have to wander across the Fremennik lands until I found a new herd to join, and I had to hope that the men whose paths I crossed wouldn't").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "decide that I might make a delicious meal. The trolls, too, were a concern, though they were easily avoided and yak-skin is thick and tough.").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "So I set out on my journey, only 8 years old for a human, but already a man in yak society.)").also { stage++ }
                4 -> npcl(FaceAnim.CHILD_NORMAL, "Barrrrooooo. (I'd forgotten how depressing my life is...)").also { stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "(When I was an adolescent, wandering the fields and slopes of my homeland, I knew that I was destined for greatness.").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "Though I was merely a yak, and though all yaks dream of far-off lands and grand adventures, I knew my horns to be sharper than most and my tongue more agile;").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "I was better able to cast my lowing voice across the chasms that separated me from the other yaks, and my voice became a song to the yaks that remained in herds.").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "I, unlike all the others of my kind, was not interested in rejoining the society - in my exile I had come to love solitude and the friendly sounds between the spaces").also { stage++ }
                4 -> npcl(FaceAnim.CHILD_NORMAL, "of silence that the mountains whispered.)").also { stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> npcl(FaceAnim.CHILD_NORMAL, "(I was born on a miserable night in Bennath. My mother, bless her soul, died in labour and was eaten by the Fremennik").also { stage++ }
                1 -> npcl(FaceAnim.CHILD_NORMAL, "who tended our herd. My father was never about, as he spent much of his time showing the adult females how much he could carry, for how long and").also { stage++ }
                2 -> npcl(FaceAnim.CHILD_NORMAL, "how quickly he could traverse a mountain pass. He was a foolish yak and was laughed at by the herd, though he had no inkling of that.").also { stage++ }
                3 -> npcl(FaceAnim.CHILD_NORMAL, "With no obvious father to raise me, I was left very much to my own devices and relied on the generosity of others to make my way through infancy and childhood.").also { stage++ }
                4 -> npcl(FaceAnim.CHILD_NORMAL, "I was forced to be cunning, wise and, sometimes, ruthless.)").also { stage++ }
                5 -> npcl(FaceAnim.CHILD_NORMAL, "Baroooooo. (I don't know what it is to be ruth, though. It is a silly word)").also { stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.PACK_YAK_6873, NPCs.PACK_YAK_6874)
}
