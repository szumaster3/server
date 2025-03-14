package content.global.handlers.scenery

import core.api.log
import core.api.quest.hasRequirement
import core.api.sendMessage
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import core.tools.Log
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class TotemPoleListener : InteractionListener {
    private val skillsNecklaces: IntArray =
        intArrayOf(
            Items.SKILLS_NECKLACE_11113,
            Items.SKILLS_NECKLACE1_11111,
            Items.SKILLS_NECKLACE2_11109,
            Items.SKILLS_NECKLACE3_11107,
        )

    private val combatBracelets: IntArray =
        intArrayOf(
            Items.COMBAT_BRACELET_11126,
            Items.COMBAT_BRACELET1_11124,
            Items.COMBAT_BRACELET2_11122,
            Items.COMBAT_BRACELET3_11120,
        )

    override fun defineListeners() {
        /*
         * Handles use the totem pole allowed to
         * re-charge skill necklaces and combat bracelets.
         * TODO: Investigate information about caskets and implement it.
         */

        onUseWith(IntType.SCENERY, (combatBracelets + skillsNecklaces), Scenery.TOTEM_POLE_2938) { player, used, with ->
            if (!hasRequirement(player, Quests.LEGENDS_QUEST)) return@onUseWith false

            val baseItem =
                when (used.id) {
                    in skillsNecklaces -> Items.SKILLS_NECKLACE4_11105
                    in combatBracelets -> Items.COMBAT_BRACELET4_11118
                    else -> return@onUseWith false
                }

            val inventory = player.inventory
            val slot = inventory.getSlot(used.asItem())

            if (slot != -1) {
                inventory.replace(Item(baseItem), slot, true)
                player.dialogueInterpreter.sendItemMessage(
                    used.id,
                    if (used.name.contains("bracelet", ignoreCase = true)) {
                        StringBuilder()
                            .append("You feel a power emanating from the totem pole as it<br>")
                            .append("recharges your bracelet. You can now rub the bracelet<br>")
                            .append("to teleport and wear it to get information while on a<br>")
                            .append("Slayer assignment.")
                    } else {
                        StringBuilder()
                            .append("You feel a power emanating from the totem pole as it<br>")
                            .append("recharges your necklace. You can now rub the<br>")
                            .append("necklace to teleport and wear it to get more caskets<br>")
                            .append("while big net Fishing.")
                    }.toString(),
                )
            } else {
                sendMessage(player, "You can't do that.")
                log(
                    this.javaClass,
                    Log.WARN,
                    "I'm Agent Mulder, this is Agent Scully. We're investigating a series of unusual occurrences in this area. [${with.location}}",
                )
            }
            return@onUseWith true
        }

        /*
         * Handles looking on the totem.
         * Sources: https://classic.fandom.com/wiki/Totem_Pole_(scenery)
         */

        on(Scenery.TOTEM_POLE_2938, IntType.SCENERY, "look") { player, _ ->
            player.dialogueInterpreter.sendDialogue(
                "This totem pole is truly awe inspiring.",
                "It depicts powerful Karamja jungle animals.",
                "It is very well carved and brings a sense of power",
                "and spiritual fullfilmentto anyone who looks at it.",
            )
            return@on true
        }

        /*
         * Handles looking on evil totem before replaced. (Varp: 139)
         */

        on(Scenery.TOTEM_POLE_2937, IntType.SCENERY, "look") { player, _ ->
            player.dialogueInterpreter.sendDialogue(
                "This totem pole looks very corrupted,",
                "there is a darkness about it that seems quite unnatural.",
                "You don't like to look at it for too long.",
            )
            return@on true
        }

        /*
         * Handles looking on evil totem after replaced. (139)
         * Note: 2144 is WrapperID.
         */

        on(Scenery.TOTEM_POLE_2936, IntType.SCENERY, "look") { player, _ ->
            player.dialogueInterpreter.sendDialogues(
                "You remove the evil totem pole.",
                "And replace it with the one you carved yourself.",
                "As you do so, you feel a lightness in the air,",
                "almost as if the Kharazi jungle were sighing.",
                "Perhaps Gujuo would like to see the totem pole.",
            )
            return@on true
        }
    }
}
