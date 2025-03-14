package content.region.misc.dialogue.mosle

import core.api.inEquipment
import core.api.inInventory
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class CaveyDaveyDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inEquipment(player, Items.WITCHWOOD_ICON_8923)) {
            npcl(FaceAnim.HALF_GUILTY, "Be ye here te deal with the Horrors?").also { stage = 19 }
        } else if (inInventory(player, Items.WITCHWOOD_ICON_8923)) {
            npcl(
                FaceAnim.HALF_GUILTY,
                "Be ye some form of simpleton? Do ye not hear the howlin' of the Horrors?",
            ).also {
                stage =
                    14
            }
        } else {
            npcl(FaceAnim.HALF_GUILTY, "Be ye mad? There be Horrors in this cave!")
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> playerl(FaceAnim.HALF_GUILTY, "What do you mean?").also { stage++ }
            1 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Have ye ever heard of the sort of evil, flesh-eatin' horrors that dwell in the darkest pits of the world?",
                ).also {
                    stage++
                }
            2 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "The sort of dark, sanity-breakin' THINGS that cause the livin' to drop to their knees and weep for the fate of all creation?",
                ).also {
                    stage++
                }
            3 -> npcl(FaceAnim.HALF_GUILTY, "Well, have ye?").also { stage++ }
            4 -> playerl(FaceAnim.HALF_GUILTY, "Yes, I think I've killed a few of them as well.").also { stage++ }
            5 -> npcl(FaceAnim.HALF_GUILTY, "Well, that's okay then.").also { stage++ }
            6 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "But, ye'll need a Witchwood Icon from a slayer master if ye want te go in these caves and live.",
                ).also {
                    stage++
                }
            7 -> playerl(FaceAnim.HALF_GUILTY, "Why?").also { stage++ }
            8 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Well, ye see them Jungle Horrors? Well down in the caves there be Cave Horrors.",
                ).also {
                    stage++
                }
            9 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "They are bigger, badder, meaner, and have a howl that freezes the blood in yer veins.",
                ).also {
                    stage++
                }
            10 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Wearin' earmuffs or a helmet won't work, cos them masks they weear make the sound magical. Only thing that works is wearin' a Witchwood Icon.",
                ).also {
                    stage++
                }
            11 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "That is, o'course, if we can see them, cos if ye don't have any light down there then yer likely te be picked te bones by the insects before the Horrors get ye.",
                ).also {
                    stage++
                }
            12 -> playerl(FaceAnim.HALF_GUILTY, "I see, thanks for the warning.").also { stage++ }
            13 -> npcl(FaceAnim.HALF_GUILTY, "Yer welcome.").also { stage = END_DIALOGUE }
            14 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Be ye some form of simpleton? Do ye not hear the howlin' of the Horrors?",
                ).also {
                    stage++
                }
            15 -> npcl(FaceAnim.HALF_GUILTY, "Well, I can hear something now you mention it.").also { stage++ }
            16 -> npcl(FaceAnim.HALF_GUILTY, "That be them, howlin', always howlin'!").also { stage++ }
            17 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "If ye value yer limbs ye'll put that Icon round yer neck, and hope they don't come out into the light!",
                ).also {
                    stage++
                }
            18 -> npcl(FaceAnim.HALF_GUILTY, "Err, all right, I'll get right on that.").also { stage = END_DIALOGUE }
            19 -> playerl(FaceAnim.HALF_GUILTY, "I might well give it a shot.").also { stage++ }
            20 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Aye, well, keep yer Icon with ye if we wants te walk out alive again.",
                ).also {
                    stage++
                }
            21 -> playerl(FaceAnim.HALF_GUILTY, "Don't worry, I will.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return CaveyDaveyDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CAVEY_DAVEY_4358)
    }
}
