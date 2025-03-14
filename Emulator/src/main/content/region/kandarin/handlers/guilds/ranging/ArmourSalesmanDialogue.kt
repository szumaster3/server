package content.region.kandarin.handlers.guilds.ranging

import core.api.interaction.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.isMaster
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Armour salesman dialogue.
 */
@Initializable
class ArmourSalesmanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("Good day to you.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> npc("And to you. Can I help you?").also { stage++ }
            1 ->
                if (isMaster(player, Skills.RANGE)) {
                    options(
                        "What do you do here?",
                        "I'd like to see what you sell.",
                        "Can I buy a Skillcape of Range?",
                        "I've seen enough, thanks.",
                    ).also { stage = 100 }
                } else {
                    options(
                        "What do you do here?",
                        "I'd like to see what you sell.",
                        "I've seen enough, thanks.",
                    ).also { stage++ }
                }
            2 ->
                when (buttonId) {
                    1 -> player("What do you do here?").also { stage++ }
                    2 -> player("I'd like to see what you sell.").also { stage = 20 }
                    3 -> player("I've seen enough, thanks.").also { stage = 30 }
                }
            10 ->
                npc(
                    "I am a supplier of leather armours and accessories. Ask and I will tell you what I know.",
                ).also { stage++ }
            11 ->
                options(
                    "Tell me about your armours.",
                    "Tell me about your accessories.",
                    "I've seen enough, thanks.",
                ).also { stage++ }
            12 ->
                when (buttonId) {
                    1 -> player("Tell me about your armours.").also { stage++ }
                    2 -> player("Tell me about your accessories.").also { stage += 2 }
                    3 -> player("I've seen enough, thanks.").also { stage = 30 }
                }
            13 -> npc("I have normal, studded and hard types.").also { stage = 200 }
            14 ->
                npc(
                    "Ah yes we have a new range accessories in stock. Essential items for an archer like you. We have vambraces, chaps, cowls, and coifs.",
                ).also {
                    stage =
                        300
                }
            20 -> npc("Indeed, cast your eyes on my wares, adventurer.").also { stage++ }
            21 -> end().also { openNpcShop(player, npc.id) }
            30 -> npc("Very good, adventurer.").also { stage = END_DIALOGUE }
            100 ->
                when (buttonId) {
                    1 -> player("What do you do here?").also { stage = 10 }
                    2 -> player("I'd like to see what you sell.").also { stage = 20 }
                    3 -> player("Can I buy a Skillcape of Range?").also { stage++ }
                    4 -> player("I've seen enough, thanks.").also { stage = 30 }
                }
            101 -> npc("Certainly! Right when you give me 99000 coins.").also { stage++ }
            102 -> options("Okay, here you go.", "No, thanks.").also { stage++ }
            103 ->
                when (buttonId) {
                    1 -> player("Okay, here you go.").also { stage++ }
                    2 -> end()
                }
            104 -> if (purchase(player, Skills.RANGE)) npc("There you go! Enjoy.") else end()

            // Tell me about your armours.
            200 ->
                options(
                    "Tell me about normal leather.",
                    "What's studded leather?",
                    "What's hard leather?",
                    "Enough about armour.",
                ).also {
                    stage++
                }
            201 ->
                when (buttonId) {
                    1 -> player("Tell me about normal leather.").also { stage++ }
                    2 -> player("What's studded leather?").also { stage = 203 }
                    3 -> player("What's hard leather?").also { stage = 204 }
                    4 -> player("Enough about armour.").also { stage = 30 }
                }
            202 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Indeed, lather armour is excellent for archers. It's supple and not very heavy.",
                ).also {
                    stage =
                        200
                }
            203 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Ah now that's leather covered with studs. It's more protective than ordinary leather.",
                ).also {
                    stage =
                        200
                }
            204 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Hard leather is specially treated using oils and drying methods to create a hard wearing armour.",
                ).also {
                    stage =
                        200
                }

            // Tell me about your accessories.
            300 ->
                options(
                    "Tell me about vambraces.",
                    "Tell me about chaps.",
                    "Tell me about cowls.",
                    "Tell me about coifs.",
                    "Enough about armour.",
                ).also {
                    stage++
                }
            301 ->
                when (buttonId) {
                    1 -> player("Tell me about vambraces.").also { stage++ }
                    2 -> player("Tell me about chaps.").also { stage = 304 }
                    3 -> player("Tell me about cowls.").also { stage = 306 }
                    4 -> player("Tell me about coifs.").also { stage = 308 }
                    5 -> player("Enough about accessories.").also { stage = 30 }
                }
            302 -> npcl(FaceAnim.NEUTRAL, "Ah yes, vambraces. These useful items are for your arms.").also { stage++ }
            303 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "A protective sheath that favours the bow and arrow. An essential purchase.",
                ).also {
                    stage =
                        30
                }
            304 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "Chaps have two functions; firstly to protect your legs, and secondly for ease of reloading arrows.",
                ).also {
                    stage++
                }
            305 -> npcl(FaceAnim.NEUTRAL, "I can highly recommend these to you for quick archery.").also { stage = 30 }
            306 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The cowl is a soft leather hat, ideal for protection with manoeuvrability.",
                ).also {
                    stage++
                }
            307 -> npcl(FaceAnim.NEUTRAL, "These are highly favoured with our guards.").also { stage = 30 }
            308 ->
                npcl(
                    FaceAnim.NEUTRAL,
                    "The coif is a specialized cowl, that has extra chain protection to keep your neck and shoulders safe.",
                ).also {
                    stage++
                }
            309 -> npcl(FaceAnim.NEUTRAL, "An excellent addition to our range, traveller.").also { stage = 30 }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ARMOUR_SALESMAN_682)
    }
}
