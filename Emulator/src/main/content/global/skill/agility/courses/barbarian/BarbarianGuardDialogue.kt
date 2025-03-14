package content.global.skill.agility.courses.barbarian

import content.region.kandarin.miniquest.barcrawl.BarcrawlManager
import core.api.sendDialogue
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class BarbarianGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        if (!BarcrawlManager.getInstance(player).isStarted()) {
            npc("O, waddya want?")
        } else if (BarcrawlManager.getInstance(player).isFinished && !BarcrawlManager.getInstance(player).isStarted()) {
            npc("'Ello friend.").also { stage = END_DIALOGUE }
        } else {
            npc("So, how's the Barcrawl coming along?").also { stage = 12 }
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("I want to come through this gate.", "I want some money.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("I want to come through this gate.").also { stage = 4 }
                    2 -> player("I want some money.").also { stage++ }
                }

            3 -> npc("Do I look like a bank to you?").also { stage = END_DIALOGUE }
            4 ->
                if (BarcrawlManager.getInstance(player).isFinished) {
                    npc("You may pass if you like. You are a true", "barbarian now.").also { stage = END_DIALOGUE }
                } else {
                    npc("Barbarians only. Are you a barbarian? You don't look", "like one.").also { stage++ }
                }

            5 -> {
                options(
                    "Hmm, yep you've got me there.",
                    "Looks can be deceiving, I am in fact a barbarian.",
                ).also { stage++ }
            }

            6 ->
                when (buttonId) {
                    1 -> player("Hmm, yep you've got me there.").also { stage = END_DIALOGUE }
                    2 -> player("Looks can be deceiving, I am in fact a barbarian.").also { stage++ }
                }

            7 ->
                npc(
                    "If you're a barbarian you need to be able to drink like",
                    "one. We barbarians like a good drink.",
                ).also { stage++ }

            8 ->
                npc(
                    "I have the perfect challenge for you... the Alfred",
                    "Grimhand Barcrawl! First completed by Alfred",
                    "Grimhand.",
                ).also { stage++ }

            9 -> {
                BarcrawlManager.getInstance(player).reset()
                BarcrawlManager.getInstance(player).setStarted(true)
                player.inventory.add(BarcrawlManager.BARCRAWL_CARD, player)
                sendDialogue("The guard hands you a Barcrawl card.")
                stage++
            }

            10 ->
                npc(
                    "Take that card to each of the bards named on it. The",
                    "bartenders will know what it means. We're kinda well",
                    "known.",
                ).also { stage++ }

            11 ->
                npc(
                    "They'll give you their strongest drink and sign your",
                    "card. When you've done all that, we'll be happy to let",
                    "you in.",
                ).also { stage = END_DIALOGUE }

            12 ->
                if (!BarcrawlManager.getInstance(player).hasCard()) {
                    player("I've lost my barcrawl card...").also { stage = 14 }
                } else if (BarcrawlManager.getInstance(player).isFinished) {
                    player("I tink I jusht 'bout done dem all... but I losht count...").also { stage = 15 }
                } else {
                    player("I haven't finished it yet.").also { stage++ }
                }

            13 -> npc("Well come back when you have, you lightweight.").also { stage = END_DIALOGUE }
            14 -> npc("What are you like? You're gonna have to start all over", "now.").also { stage = 9 }
            15 -> {
                if (!player.inventory.containsItem(BarcrawlManager.BARCRAWL_CARD)) {
                    end()
                }
                BarcrawlManager.getInstance(player).setStarted(false)
                player.bank.remove(BarcrawlManager.BARCRAWL_CARD)
                player.inventory.remove(BarcrawlManager.BARCRAWL_CARD)
                sendDialogue(player, "You give the card to the barbarian.").also { stage++ }
            }

            16 ->
                npc(
                    "Yep that seems fine, you can come in now. I never",
                    "learned to read, but you look like you've drunk plenty.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return BarbarianGuardDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BARBARIAN_GUARD_384)
    }
}
