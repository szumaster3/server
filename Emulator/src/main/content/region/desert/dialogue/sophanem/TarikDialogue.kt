package content.region.desert.dialogue.sophanem

import core.api.sendDialogue
import core.api.setAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class TarikDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(FaceAnim.WORRIED, "Ouch!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_WORRIED, "Are you alright?").also { stage++ }
            1 -> npcl(FaceAnim.FRIENDLY, "I'm fine, I'm fine. Just a scratch.").also { stage++ }
            2 ->
                options(
                    "Who are you then?",
                    "How did you injure yourself?",
                    "Do you know anything about this pyramid?",
                ).also { stage++ }
            3 ->
                when (buttonId) {
                    1 -> playerl(FaceAnim.FRIENDLY, "Who are you then?").also { stage = 10 }
                    2 -> playerl(FaceAnim.HALF_WORRIED, "How did you injure yourself?").also { stage = 20 }
                    3 -> playerl(FaceAnim.HALF_ASKING, "So what's in the pyramid?").also { stage = 30 }
                }
            10 -> npcl(FaceAnim.FRIENDLY, "Me? I'm Tarik.").also { stage++ }
            11 ->
                playerl(
                    FaceAnim.HALF_ASKING,
                    "That hat you're wearing doesn't look like it comes from around here?",
                ).also {
                    stage++
                }
            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "It was a present, my employer thought it would suit me. I'm not sure what it does, though it does keep the sun off very well.",
                ).also {
                    stage++
                }
            13 -> playerl(FaceAnim.FRIENDLY, "Who is your employer then?").also { stage++ }
            14 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Simon Templeton, though I haven't seen him since the town gates were closed off.",
                ).also {
                    stage++
                }
            15 -> playerl(FaceAnim.HALF_THINKING, "Simon Templeton ... that name seems familiar?").also { stage++ }
            16 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "He's an archaeologist, I worked as an assistant for him. Though I work for myself now.",
                ).also {
                    stage++
                }
            17 -> playerl(FaceAnim.FRIENDLY, "So what have you been up to?").also { stage++ }
            18 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Simon suggested that there might be riches to be found in that pyramid over there.",
                ).also {
                    stage =
                        30
                }
            20 -> npcl(FaceAnim.NEUTRAL, "I've been investigating that pyramid over there.").also { stage = 30 }
            30 ->
                npcl(
                    FaceAnim.HALF_THINKING,
                    "Well, I'm not sure. First of all there's something odd about the doors on the place. There's four of them - but three of them lead to an empty tomb. The other one is guarded by something.",
                ).also {
                    stage++
                }
            31 ->
                npcl(
                    FaceAnim.THINKING,
                    "If I make it through the door there's a Mummy waiting. But which door the right one is seems to change, it's all quite confusing.",
                ).also {
                    stage++
                }
            32 -> playerl(FaceAnim.HALF_ASKING, "What about this Mummy?").also { stage++ }
            33 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I don't think he likes people. However he does allow you to enter some of the rooms in the tomb. They are dangerous though.",
                ).also {
                    stage++
                }
            34 -> playerl(FaceAnim.HAPPY, "Bah, I laugh in the face of danger!").also { stage++ }
            35 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Well, if you go into that pyramid then you'll be laughing a lot then. It's full of poisonous snakes and scarabs, and rather nasty Mummies as well. You could die in there.",
                ).also {
                    stage++
                }
            36 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I managed to get into one room, but the next room was harder. My lockpicking skills weren't good enough, maybe I should have brought a lockpick.",
                ).also {
                    stage++
                }
            37 ->
                sendDialogue(
                    player,
                    "The first room in the pyramid requires a thieving level of 21. Each subsequent room requires an extra 10 levels to enter.",
                ).also {
                    stage++
                }
            38 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "There are also lots of poisonous snakes in the urns. They'll bite you if they can. You might be able to charm them if you know how. I'd bring some antipoison anyway if I were you.",
                ).also {
                    stage++
                }
            39 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "It all sounds like fun to me. So is there anything valuable in there?",
                ).also {
                    stage++
                }
            40 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "There are lots of artefacts, you should be able to sell them on the black market, I mean, to a legitimate trader, for some money.",
                ).also {
                    stage++
                }
            41 -> playerl(FaceAnim.NEUTRAL, "You mean Simon Templeton?").also { stage++ }
            42 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, he's a 'legitimate' as you can get, if you can get out of this city and get to him.",
                ).also {
                    stage++
                }
            43 ->
                playerl(
                    FaceAnim.FRIENDLY,
                    "I'll find a way. Is there anything else of value in there?",
                ).also { stage++ }
            44 ->
                npcl(
                    FaceAnim.SUSPICIOUS,
                    "I have heard a rumour that there is a valuable magic sceptre in there as well.",
                ).also {
                    stage++
                }
            45 ->
                playerl(
                    FaceAnim.HALF_THINKING,
                    "Ah, now you have piqued my interest. What do you know about this sceptre?",
                ).also {
                    stage++
                }
            46 ->
                npcl(
                    FaceAnim.SUSPICIOUS,
                    "Not a lot. It is apparently made of gold and covered in jewels, and used to be owned by one of Tumeken's sons.",
                ).also {
                    stage++
                }
            47 ->
                playerl(FaceAnim.HALF_ASKING, "Tumeken?").also {
                    setAttribute(player, "/save:tarik-spoken-to", true)
                    stage++
                }
            48 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Tumeken, the sun god and head of the gods. His sons were the rulers, chosen by him to rule in his name.",
                ).also {
                    stage++
                }
            49 ->
                playerl(
                    FaceAnim.HALF_THINKING,
                    "So these sons were rich and powerful then? This sceptre should be pretty good then.",
                ).also {
                    stage++
                }
            50 -> npcl(FaceAnim.NEUTRAL, "Hmmm. Well it is supposed to have some magical powers.").also { stage++ }
            51 -> playerl(FaceAnim.HALF_ASKING, "Magical powers? This sounds good. What are they?").also { stage++ }
            52 -> npcl(FaceAnim.LAUGH, "I'll let you know when I find it!").also { stage++ }
            53 -> playerl(FaceAnim.ANNOYED, "Not if I find it first!").also { stage++ }
            54 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Hey! If you find it I deserve a share of the profits! You wouldn't have known it existed without my help.",
                ).also {
                    stage++
                }
            55 ->
                playerl(
                    FaceAnim.NEUTRAL,
                    "I'll think about it... Right, I think I'd better investigate this place.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TARIK_4478)
    }
}
