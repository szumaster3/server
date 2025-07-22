package content.region.desert.pollniveach.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import org.rs.consts.NPCs

/**
 * Represents the Villager dialogues.
 *
 * ```
 * Varbit 340
 * Value: 0 // Pickpocket
 * Value: 1 // Pickpocket, Lure
 * Value: 2 // Pickpocket, Lure, Knock-Out
 * Value: 3 // Hide NPCs
 * ```
 */
@Initializable
class VillagerDialogue(player: Player? = null) : Dialogue(player) {


    private val randomDialogue = arrayOf(
        "I'm very well thank you.",
        "I'm busy right now.",
        "Get out of my way, I'm in a hurry!",
        "Do I know you? I'm in a hurry!",
        "Hello there! Nice weather we've been having.",
        "Not too bad, thanks.",
        "None of your business.",
        "I think we need a new mayor. The one we've got isn't very good.",
        "That is classified information."
    )

    override fun open(vararg args: Any?): Boolean {
        player("Hello, how's it going?")
        val stages = intArrayOf(0, 2, 4, 6, 10, 12, 15)
        val randomStage = stages.random()
        stage = randomStage
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player("Do you wish to trade?").also { stage = 1 }
            1 -> npcl(FaceAnim.HALF_GUILTY, "No, I have nothing I wish to get rid of. If you want to do some trading, there are plenty of shops and market stalls around though.").also { stage = 16 }

            2 -> player("I'm in search of a quest.").also { stage = 3 }
            3 -> npc("I'm sorry I can't help you there.").also { stage = 16 }

            4 -> player("I'm in search of enemies to kill.").also { stage = 5 }
            5 -> npcl(FaceAnim.HALF_GUILTY, "Well there's always those wild Ugthanki, snakes and wolves to kill on the outskirts of town.").also { stage = 16 }

            6 -> npcl(FaceAnim.HALF_GUILTY, "You know, even a stopped clock tells the right time twice a day.").also { stage = 7 }

            7 -> player("What are you talking about?").also { stage = 8 }
            8 -> npc("Uneducated fool! It will die, it will die!").also { stage = 9 }
            9 -> player("Ohhh kaaay, I'll leave you to it then.").also { stage = 16 }

            10 -> npc("I'm fine, how are you?").also { stage = 11 }
            11 -> player("Very well, thank you.").also { stage = 20 }

            12 -> npc("Who are you?").also { stage = 13 }
            13 -> player("I'm a bold adventurer.").also { stage = 14 }
            14 -> npc("Ah, a very noble profession.").also { stage = 16 }

            15 -> npcl(FaceAnim.HALF_GUILTY, randomDialogue.random()).also { stage = 16 }

            16 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = VillagerDialogue(player)

    override fun getIds(): IntArray = intArrayOf(
        1887, // Wrapper
        NPCs.VILLAGER_1888, // Male
        NPCs.VILLAGER_1889,
        NPCs.VILLAGER_1890,
        NPCs.VILLAGER_1891,
        NPCs.VILLAGER_1892,
        NPCs.VILLAGER_1893,
        NPCs.VILLAGER_1894,
        NPCs.VILLAGER_1895,
        NPCs.VILLAGER_1896, // Female
        NPCs.VILLAGER_1897,
        NPCs.VILLAGER_1898,
        NPCs.VILLAGER_1899,
        NPCs.VILLAGER_1900,
    )
}
