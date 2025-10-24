package content.global.skill.summoning.pet.dialogue

import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

@Initializable
class BulldogDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when(stage) {
            0 -> if (npc.id == 6969 || npc.id == 7259 || npc.id == 7260) {
                npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf, grumble snuffle whurf whurf? (By 'eck, lad/lass, are we goin' ter be walkin' all day?)").also { stage++ }
            } else {
                if(inInventory(player, Items.CUP_OF_TEA_712)){
                    npcl(FaceAnim.CHILD_NORMAL, "[Give us a sip of that tea, pet, I'm parched!]").also { stage = 5 }
                } else {
                    when ((0..3).random()) {
                        0 -> npcl(FaceAnim.CHILD_NORMAL,"Whurfwhurf whurf, whurfwhurf whurf. (This is right grand, this is.)").also { stage = 11 }
                        1 -> npcl(FaceAnim.CHILD_NORMAL,"Whurf whurfwhurf whurf. Grumblewhurf. (Yer lookin' tired. You should have a nap.)").also { stage = 15 }
                        2 -> npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf Whurf. (I could do wi' a cup of tea, mind.)").also { stage = 19 }
                        3 -> npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf? (Are we stoppin')").also { stage = 21 }
                    }
                }
            }

            1 -> playerl(FaceAnim.FRIENDLY, "Well, I could carry you if you like.").also { stage++ }
            2 -> npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf, whurf. (That'd be grand, like.)").also { stage++ }
            3 -> playerl(FaceAnim.FRIENDLY, "You know, you won't grow up to be big and strong if you keep getting carried everywhere.").also { stage++ }
            4 -> npc(FaceAnim.CHILD_NORMAL, "Whurf, whurf. (Alright, "+if (player.isMale) "lad" else "lass"+": I know, I know.)").also { stage = END_DIALOGUE }

            5  -> playerl(FaceAnim.FRIENDLY, "I'm not giving you tea. You might get sick!").also { stage++ }
            6  -> npcl(FaceAnim.CHILD_NORMAL, "['Ow will we know if we don't try?]").also { stage++ }
            7  -> playerl(FaceAnim.FRIENDLY, "How about we just say that I did?").also { stage++ }
            8  -> npcl(FaceAnim.CHILD_NORMAL, "[Did it work?]").also { stage++ }
            9  -> playerl(FaceAnim.FRIENDLY, "Yes, and you liked the taste a lot.").also { stage++ }
            10 -> npcl(FaceAnim.CHILD_NORMAL, "[Aye, it were a grand cuppa.]").also { stage = END_DIALOGUE }

            11 -> playerl(FaceAnim.FRIENDLY, "What? This place or what we are doing?").also { stage++ }
            12 -> npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf grumble. (Mostly the sittin' still.)").also { stage++ }
            13 -> playerl(FaceAnim.FRIENDLY, "You're felling lazy today, aren't you?").also { stage++ }
            14 -> npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf! Whurf whurf Whurfwhurf! (I'm not lazy! I'm conservin' energy!)").also { stage = END_DIALOGUE }

            15 -> playerl(FaceAnim.FRIENDLY, "Is it me who needs a rest or you?").also { stage++ }
            16 -> npcl(FaceAnim.CHILD_NORMAL, "Whurfwhurf whurf. (Well, if you took one, I might too...)").also { stage++ }
            17 -> playerl(FaceAnim.FRIENDLY, "Yeah, I thought as much.").also { stage++ }
            18 -> npcl(FaceAnim.CHILD_NORMAL, "Grumble... (Grumble...)").also { stage = END_DIALOGUE }

            19 -> playerl(FaceAnim.FRIENDLY, "I don't know if a cup of tea would be good for you.").also { stage++ }
            20 -> npcl(FaceAnim.CHILD_NORMAL, "Whurf whurf whurfwhurf. (Fair 'nuff, then.)").also { stage = END_DIALOGUE }

            21 -> playerl(FaceAnim.FRIENDLY, "Not for a while I guess...").also { stage++ }
            22 -> npcl(FaceAnim.CHILD_NORMAL, "*grumble* Whurf (*sigh* Typical)").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(
        NPCs.BULLDOG_PUPPY_6969,
        NPCs.BULLDOG_PUPPY_7259,
        NPCs.BULLDOG_PUPPY_7260,
        NPCs.BULLDOG_6968,
        NPCs.BULLDOG_7257,
        NPCs.BULLDOG_7258
    )


}