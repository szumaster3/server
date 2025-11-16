package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the War Tortoise familiar dialogue.
 */
@Initializable
class WarTortoiseDialogue : Dialogue {

    private var branch = -1

    override fun newInstance(player: Player?): Dialogue = WarTortoiseDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC

        branch = (0..3).random()

        stage = when (branch) {
            0 -> 0
            1 -> 4
            2 -> 10
            3 -> 16
            else -> 0
        }

        when (branch) {
            0 -> npc(FaceAnim.OLD_NORMAL, "*The tortoise waggles its head about.*", "What are we doing in this dump?")
            1 -> npc(FaceAnim.OLD_NORMAL, "Hold up a minute, there.")
            2 -> npc(FaceAnim.OLD_NORMAL, "*The tortoise bobs its head around energetically.*", "Oh, so now you're paying attention to", "me, are you?")
            3 -> npc(FaceAnim.OLD_NORMAL, "*The tortoise exudes an air of reproach.*", "Are you going to keep rushing", "around all day?")
        }

        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Well, I was just going to take care of a few things."); stage++ }
                1 -> { npc(FaceAnim.OLD_NORMAL, "*The tortoise shakes its head.*", "I don't believe it. Stuck here with this young whippersnapper", "running around having fun."); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "You know, I'm sure you would enjoy it if you gave it a chance."); stage++ }
                3 -> { npcl(FaceAnim.OLD_NORMAL, "Oh, you would say that, wouldn't you?"); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                4 -> { playerl(FaceAnim.FRIENDLY, "What do you want?"); stage++ }
                5 -> { npc(FaceAnim.OLD_NORMAL, "*The tortoise bobs its head sadly.*", "For you to slow down!"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Well, I've stopped now."); stage++ }
                7 -> { npcl(FaceAnim.OLD_NORMAL, "Yes, but you'll soon start up again, won't you?"); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Probably."); stage++ }
                9 -> { npc(FaceAnim.OLD_NORMAL, "*The tortoise waggles its head despondently.*", "I don't believe it...."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                10 -> { playerl(FaceAnim.FRIENDLY, "I pay you plenty of attention!"); stage++ }
                11 -> { npcl(FaceAnim.OLD_NORMAL, "Only when you want me to carry those heavy things of yours."); stage++ }
                12 -> { playerl(FaceAnim.FRIENDLY, "I don't ask you to carry anything heavy."); stage++ }
                13 -> { npcl(FaceAnim.OLD_NORMAL, "What about those lead ingots?"); stage++ }
                14 -> { playerl(FaceAnim.HALF_ASKING, "What lead ingots?"); stage++ }
                15 -> { npc(FaceAnim.OLD_NORMAL, "*The tortoise droops its head.*", "Well, that's what it felt like....", "*grumble grumble*"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                16 -> { playerl(FaceAnim.FRIENDLY, "Only for as long as I have the energy to."); stage++ }
                17 -> { npcl(FaceAnim.OLD_NORMAL, "Oh. I'm glad that my not being able to keep up with you brings you such great amusement."); stage++ }
                18 -> { playerl(FaceAnim.FRIENDLY, "I didn't mean it like that."); stage++ }
                19 -> { npc(FaceAnim.OLD_NORMAL, "*The tortoise waggles its head disapprovingly.*", "Well, when you are QUITE finished laughing at my expense,", "how about you pick up a rock larger than your body", "and go crawling about with it?"); stage++ }
                20 -> { npcl(FaceAnim.OLD_NORMAL, "We'll see how energetic you are after an hour or two of that."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.WAR_TORTOISE_6815, NPCs.WAR_TORTOISE_6816)
}