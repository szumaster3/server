package content.global.skill.runecrafting.abyss

import content.global.skill.runecrafting.PouchManager.Pouches
import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Dark mage dialogue at Abyss.
 */
class DarkMageDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (args.size >= 2) {
            if (repair()) {
                npc("There, I have repaired your pouches.", "Now leave me alone. I'm concentrating.").also { stage = 30 }
                return true
            } else {
                npc("You don't seem to have any pouches in need of repair.", "Leave me alone.").also { stage = 30 }
                return true
            }
        }
        player("Hello there.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (stage) {
            0 -> npc("Quiet!", "You must not break my concentration!").also { stage++ }
            1 -> options("Why not?", "What are you doing here?", "Can you repair my pouches?", "Ok, Sorry").also { stage++ }

            2 -> when (buttonId) {
                1 -> player("Why not?").also { stage = 10 }
                2 -> player("What are you doing here?").also { stage = 20 }
                3 -> player("Can you repair my pouches, please?").also { stage = 50 }
                4 -> player("Ok, sorry.").also { stage = END_DIALOGUE }
            }

            10 -> npc(
                "Well, if my concentration is broken while keeping this",
                "gate open, then if we are lucky, everyone within a one",
                "mile radius will either have their heads explode, or will be",
                "consumed internally by the creatures of the Abyss.",
            ).also {
                stage++
            }

            11 -> player("Erm...", "And if we are unlucky?").also { stage++ }
            12 -> npc(
                "If we are unlucky, then the entire universe will begin",
                "to fold in upon itself, and all reality as we know it will",
                "be annihilated in a single stroke.",
            ).also {
                stage++
            }

            13 -> npc("So leave me alone!").also { stage = END_DIALOGUE }
            20 -> npc(
                "Do you mean what am I doing here in Abyssal space,",
                "Or are you asking me what I consider my ultimate role",
                "to be in this voyage that we call life?",
            ).also {
                stage++
            }

            21 -> player("Um... the first one.").also { stage++ }
            22 -> npc(
                "By remaining here and holding this portal open, I am",
                "providing a permanent link between normal space and",
                "this strange dimension that we call Abyssal space.",
            ).also {
                stage++
            }

            23 -> npc(
                "As long as this spell remains in effect, we have the",
                "capability to teleport into abyssal space at will.",
            ).also {
                stage++
            }

            24 -> npc("Now leave me be!", "I can afford no distraction in my task!").also { stage = END_DIALOGUE }
            50 -> npc("Fine, fine! Give them here.").also { stage++ }
            51 -> {
                repair()
                npc("There, I've repaired them all.", "Now get out of my sight!").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    private fun repair(): Boolean {
        player.pouchManager.pouches.forEach { (id: Int, pouch: Pouches) ->
            pouch.currentCap = pouch.capacity
            val newCharges: Int = when (id) {
                Items.MEDIUM_POUCH_5510 -> 264
                Items.LARGE_POUCH_5512 -> 186
                Items.GIANT_POUCH_5514 -> 140
                else -> 3
            }
            pouch.charges = newCharges
            var essence: Item? = null
            if (!pouch.container.isEmpty) {
                val ess = pouch.container[0].id
                val amount = pouch.container.getAmount(ess)
                essence = Item(ess, amount)
            }
            pouch.remakeContainer()
            if (essence != null) {
                pouch.container.add(essence)
            }
            if (id != 5509) {
                if (player.inventory.contains(id + 1, 1)) {
                    player.inventory.remove(Item(id + 1, 1))
                    player.inventory.add(Item(id, 1))
                }
                if (player.bank.contains(id + 1, 1)) {
                    player.bank.remove(Item(id + 1, 1))
                    player.bank.add(Item(id, 1))
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.DARK_MAGE_2262)
}