package content.global.random.event.frog_princess

import content.global.random.RandomEventNPC
import core.api.openDialogue
import core.api.utils.WeightBasedTable
import core.game.node.entity.npc.NPC
import core.tools.RandomFunction
import shared.consts.NPCs

/**
 * Represents Frog herald random event NPC.
 * @author szu
 */
class FrogHeraldNPC(override var loot: WeightBasedTable? = null) : RandomEventNPC(NPCs.FROG_2471) {

    private val phrases = arrayOf(
        "@name, the Frog @gender needs your help.",
        "Greetings from the Frog @gender, @name!",
        "Talk to the Frog @gender, @name!",
        "The Frog @gender needs your help, @name!",
        "Please respond to the Frog @gender, @name!"
    )

    override fun init() {
        super.init()
        sendChat(randomPhrase())
    }

    override fun tick() {
        if (RandomFunction.random(1, 15) == 5) {
            sendChat(randomPhrase())
        }
        super.tick()
    }

    override fun talkTo(npc: NPC) {
        openDialogue(player, FrogHeraldDialogue(false), this.asNpc())
    }

    /**
     * Generates a random chat phrase with the player's name and gender replaced.
     */
    private fun randomPhrase(): String {
        val nameFormatted = player.name.replaceFirstChar { it.titlecase() }
        val gender = if (player.isMale) "Princess" else "Prince"
        return phrases.random()
            .replace("@name", nameFormatted)
            .replace("@gender", gender)
    }
}