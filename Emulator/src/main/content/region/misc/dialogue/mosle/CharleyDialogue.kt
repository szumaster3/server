package content.region.misc.dialogue.mosle

import core.api.inInventory
import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CharleyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inInventory(player, Items.BOOK_O_PIRACY_7144)) {
            player(FaceAnim.FRIENDLY, "Hello!")
        } else {
            npc(FaceAnim.FRIENDLY, "I got fish, you got gold?").also { stage = 10 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Arr? Be ye wantin' te go on account with our gang o' fillibusters?",
                ).also { stage++ }
            1 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The powder monkey be takin' a caulk after gettin' rowdy on bumboo, so there be plenty of room for ye.",
                ).also {
                    stage++
                }
            2 -> player(FaceAnim.FRIENDLY, "Riiiiight...").also { stage++ }
            3 -> player(FaceAnim.FRIENDLY, "I'll just be over here if you need me.").also { stage = END_DIALOGUE }
            10 -> options("Yes.", "Yes, but I don't want your fish.", "What happened to your legs?").also { stage++ }
            11 ->
                when (buttonId) {
                    1 -> player(FaceAnim.FRIENDLY, "Yes.").also { stage = 80 }
                    2 -> player(FaceAnim.FRIENDLY, "Yes, but I don't want your fish.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_ASKING, "What happened to your legs?").also { stage = 30 }
                }

            20 -> npcl(FaceAnim.FRIENDLY, "Then what are ye doin' in a fish shop? Looking fer work?").also { stage++ }
            21 -> player(FaceAnim.FRIENDLY, "Possibly, do you have any quests?").also { stage++ }
            22 ->
                npcl(
                    FaceAnim.HALF_ASKING,
                    "I dunno, I haven't gone through the last catch yet. What sort of a fish is it?",
                ).also {
                    stage++
                }
            23 -> player(FaceAnim.FRIENDLY, "A quest isn't a type of fish!").also { stage++ }
            24 ->
                npcl(FaceAnim.FRIENDLY, "Then I don't have any, and yer wastin' my time!").also {
                    stage = END_DIALOGUE
                }
            30 -> npcl(FaceAnim.FRIENDLY, "Ye wanna know what happened to my legs?").also { stage++ }
            31 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Yer too much of a lilly-livered, hat wearin' landlubber to know what happened to my legs!",
                ).also {
                    stage++
                }
            32 -> playerl(FaceAnim.FRIENDLY, "No I'm not! I've seen some freaky stuff! I can take it!").also { stage++ }
            33 -> npcl(FaceAnim.FRIENDLY, "All right, lad, since yer so insistent, I'll tell ye.").also { stage++ }
            34 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "See, I was clingin' onto a barrel, me ship havin' just had an encounter with this albatross.",
                ).also {
                    stage++
                }
            35 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "The sea was thrashin' and wild, but not so wild that I didn't see the fins of some sharks closin' in on me.",
                ).also {
                    stage++
                }
            36 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "I managed to yank a sliver of wood from the barrel just as one of them grabbed me from below, but I slipped down the things throat by about two feet before I managed te kill it.",
                ).also {
                    stage++
                }
            37 -> player(FaceAnim.FRIENDLY, "How did you survive?").also { stage++ }
            38 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "A passin' ship saw the sharks and knew there would be survivors in the water. They sent a longboat and picked me up, but not before the sharks had taken off my legs.",
                ).also {
                    stage++
                }
            39 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "And that lad is why they call me two feet Charley, because they found me jammed two feet down a shark's throat.",
                ).also {
                    stage++
                }
            40 -> player(FaceAnim.DISGUSTED, "I think I'm gonna be sick...").also { stage++ }
            41 -> npcl(FaceAnim.FRIENDLY, "I knew ye couldn't handle the truth!").also { stage = END_DIALOGUE }
            80 -> {
                end()
                openNpcShop(player, NPCs.CHARLEY_3161)
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CharleyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CHARLEY_3161)
    }
}
