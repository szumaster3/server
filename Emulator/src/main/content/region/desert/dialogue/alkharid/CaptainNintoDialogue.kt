package content.region.desert.dialogue.alkharid

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class CaptainNintoDialogue(
    player: Player? = null,
) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.OLD_DEFAULT, "Hello, what are you doing here, so far from home?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when(stage) {
            0 -> playerl(FaceAnim.DRUNK, "I'm enjoyn' the local hoschpitalieee. hee hee.").also { stage++ }
            1 -> npcl(FaceAnim.OLD_DEFAULT, "Looks like you've enjoyed more than your fair share of hospitality.").also { stage++ }
            2 -> playerl(FaceAnim.DRUNK, "tee hee, I probblie schoodn' 'av anuva drinkie right now. But dis dwarven beer ish kind of moreish after the fiff pint.").also { stage++ }
            3 -> npcl(FaceAnim.OLD_DEFAULT, "I'd go easy on the dwarven stout if I were you.").also { stage++ }
            4 -> playerl(FaceAnim.DRUNK, "I yousht to be a tesht pilot yoo know. I reeel hero.").also { stage++ }
            5 -> npcl(FaceAnim.OLD_DEFAULT, "But I loscht my bottle. Scho I now ffind scholiss in the bottle.").also { stage++ }
            6 -> playerl(FaceAnim.DRUNK, "What happened? Did you have a glider crash or get attacked by huge flying birds or something?").also { stage++ }
            7 -> npcl(FaceAnim.OLD_DEFAULT, "Naaah, I realished I woz scared ov heightsh.").also { stage++ }
            8 -> playerl(FaceAnim.DRUNK, "Ah, I can see that would be a problem ... You should keep an eye on your drinking, though.").also { stage++ }
            9 -> npcl(FaceAnim.OLD_DEFAULT, "I'll try. Bofe eyez...").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CaptainNintoDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAPTAIN_NINTO_4594)
    }
}
