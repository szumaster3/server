package content.region.fremennik.handlers.general_shadows

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class ShadowRealmGhostDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (!inEquipment(player, Items.GHOSTSPEAK_AMULET_552)) {
            npc("Whoooo wooo Whooooooooo.").also { stage = END_DIALOGUE }
            return true
        }
        if (!getAttribute(player, GeneralShadowUtils.GS_COMPLETE, false)) {
            sendDialogue(player, "Ghost seems too busy to talk.").also { stage = END_DIALOGUE }
            return true
        }
        playerl(FaceAnim.HALF_GUILTY, "I fought a really big ghost dog in here - is he gone now?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val hasShadowSword = hasAnItem(player, Items.SHADOW_SWORD_10858).container != null
        when (stage) {
            0 -> npc("Yes, he is gone from this place.", "You did a great thing removing him.").also { stage++ }
            1 ->
                if (!hasShadowSword) {
                    playerl(
                        FaceAnim.HALF_GUILTY,
                        "But I lost my shadow sword and I don't know how to get it back.",
                    ).also { stage++ }
                } else {
                    npc(
                        "You were well rewarded for you effort.",
                        "But you should leave this place, for it is not wise to tarry.",
                    ).also {
                        stage =
                            END_DIALOGUE
                    }
                }
            2 ->
                npc(
                    FaceAnim.NEUTRAL,
                    "Ah, so that is why you return. I can assist you in that",
                    "and give you another.",
                ).also {
                    stage++
                }
            3 -> {
                player(FaceAnim.HALF_ASKING, "Who are you anyway...or were you...?")
                addItemOrDrop(player, Items.SHADOW_SWORD_10858)
                stage++
            }
            4 -> npc(FaceAnim.NEUTRAL, "Who I was does not matter.").also { stage++ }
            5 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Now, I am a guardian for the Shadow Realm and I am protecting this area from any more disturbances.",
                ).also {
                    stage++
                }
            6 ->
                npcl(FaceAnim.NEUTRAL, "There are those who walk in the Shadow Realm who should not.").also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GHOST_5572)
    }
}
