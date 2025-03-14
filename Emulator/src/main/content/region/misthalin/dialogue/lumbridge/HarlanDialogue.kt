package content.region.misthalin.dialogue.lumbridge

import core.api.addItem
import core.api.getStatLevel
import core.api.hasAnItem
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class HarlanDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        options("Can you tell me about different weapon types I can use?", "Please tell me about Skillcapes.", "Bye.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                when (buttonId) {
                    1 ->
                        player(
                            FaceAnim.HALF_GUILTY,
                            "Can you tell me about different weapon types I can",
                            "use?",
                        ).also {
                            stage =
                                10
                        }

                    2 -> player(FaceAnim.HALF_GUILTY, "Please tell me about Skillcapes.").also { stage = 20 }
                    3 -> player(FaceAnim.HALF_GUILTY, "Bye.").also { stage = END_DIALOGUE }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well let me see now...There are stabbing type weapons",
                    "such as daggers, then you have swords which are",
                    "slashing, maces that have great crushing abilities, battle",
                    "axes which are powerful and spears which can be good",
                ).also {
                    stage++
                }
            11 -> npc(FaceAnim.HALF_GUILTY, "for Defence and many forms of Attack.").also { stage++ }
            12 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It depends a lot on how you want to fight. Experiment",
                    "and find out what is best for you. Never be scared to",
                    "try out a new weapon; you never know, you might like",
                    "it! Why, I tried all of them for a while and settled on",
                ).also {
                    stage++
                }
            13 -> npc(FaceAnim.HALF_GUILTY, "this rather good sword!").also { stage++ }
            14 -> options("I'd like a training sword and shield.", "Bye").also { stage++ }
            15 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "I'd like a training sword and shield.").also { stage++ }
                    2 -> player(FaceAnim.HALF_GUILTY, "Bye.").also { stage = END_DIALOGUE }
                }
            16 -> {
                if (hasAnItem(player, Items.TRAINING_SWORD_9703).container != null &&
                    hasAnItem(player, Items.TRAINING_SHIELD_9704).container != null
                ) {
                    npc(
                        "You already have them! If they're not in your",
                        "inventory, perhaps you should check your bank.",
                    )
                    stage = 30
                    return true
                }
                if (hasAnItem(player, Items.TRAINING_SHIELD_9704).container != null) {
                    npc("You already have a shield but I can give you a sword.")
                    stage = 16
                    return true
                }
                if (hasAnItem(player, Items.TRAINING_SWORD_9703).container != null) {
                    npc("You already have a sword but I can give you a shield.")
                    stage = 17
                    return true
                }
                if (addItem(player, Items.TRAINING_SWORD_9703)) {
                    sendItemDialogue(player, Items.TRAINING_SWORD_9703, "Harlan gives you a training sword.")
                } else {
                    end()
                }
                stage++
            }

            17 -> {
                if (addItem(player, Items.TRAINING_SHIELD_9704)) {
                    sendItemDialogue(player, Items.TRAINING_SHIELD_9704, "Harlan gives you a training shield.")
                } else {
                    end()
                }
                stage = END_DIALOGUE
            }

            20 ->
                npc(
                    "Of course. Skillcapes are a symbol of achievement. Only",
                    "people who have mastered a skill and reached level 99",
                    "can get their hands on them and gain the benefits they",
                    "carry. Is there something else I can help you with,",
                ).also {
                    stage++
                }
            21 -> npc("perhaps?").also { stage++ }
            22 -> {
                if (getStatLevel(player, Skills.DEFENCE) >= 99) {
                    options(
                        "Can you tell me about different weapon types I can use?",
                        "Can I purchase a defence cape?",
                        "Bye.",
                    ).also { stage++ }
                } else {
                    options(
                        "Can you tell me about different weapon types I can use?",
                        "Please tell me about skillcapes.",
                        "Bye.",
                    ).also { stage = 0 }
                }
            }

            23 -> npc("You will have to pay a fee of 99,000 gp.").also { stage++ }
            24 -> options("Okay.", "No, thanks.").also { stage++ }
            25 ->
                when (buttonId) {
                    1 -> player("Okay.").also { stage++ }
                    2 -> player("No, thanks.").also { stage = END_DIALOGUE }
                }
            26 -> {
                if (purchase(player, Skills.DEFENCE)) {
                    npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
                }
                stage = 30
            }

            30 -> end()
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MELEE_TUTOR_705)
    }
}
