package content.region.misthalin.handlers.rc_guild

import core.api.*
import core.api.ui.closeDialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.item.Item
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

/*
 * TODO CHECKLIST
 * [ ] - Entering the portal in the Dagon'Hai caves now works with an omni-talisman. 26 November 2008
 * [ ] - Unlike the Omni-tiara, the Omni-talisman cannot grant access to the free to play altars, such as air, mind, water, earth, fire, and body, while in a free players' world.
 *          1. https://youtu.be/EAhQXrs4TOo?si=mDf3NpWxcE3svq6w&t=448
 * [ ] - The Omni-talisman counts for a Soul talisman, even if the player obtained their omni-talisman prior to the soul talisman's release.
 * [ ] - Access to all Elriss dialogues requires Ring of Charos (a) and a set of Runecrafter robes (any colour) equipped.
 */
class RunecraftingGuildListener : InteractionListener {
    companion object {
        private val RC_HAT =
            intArrayOf(
                Items.RUNECRAFTER_HAT_13626,
                Items.RUNECRAFTER_HAT_13625,
                Items.RUNECRAFTER_HAT_13621,
                Items.RUNECRAFTER_HAT_13620,
                Items.RUNECRAFTER_HAT_13616,
                Items.RUNECRAFTER_HAT_13615,
            )

        private val WIZARD_NPCs =
            intArrayOf(
                NPCs.WIZARD_8033,
                NPCs.WIZARD_8034,
                NPCs.WIZARD_8035,
                NPCs.WIZARD_8036,
                NPCs.WIZARD_8037,
                NPCs.WIZARD_8038,
                NPCs.WIZARD_8039,
                NPCs.WIZARD_8040,
            )
    }

    override fun defineListeners() {
        /*
         * Handles interactions with various objects inside the guild.
         */

        on(Scenery.CONTAINMENT_UNIT_38327, IntType.SCENERY, "activate") { player, node ->
            lockInteractions(player, 1)
            animateScenery(node.asScenery(), 10193)
            return@on true
        }
        on(Scenery.GLASS_SPHERES_38331, IntType.SCENERY, "activate") { player, node ->
            lockInteractions(player, 1)
            animateScenery(node.asScenery(), 10128)
            return@on true
        }
        on(Scenery.GYROSCOPE_38330, IntType.SCENERY, "activate") { player, node ->
            lockInteractions(player, 1)
            animateScenery(node.asScenery(), 10127)
            return@on true
        }
        on(Scenery.RUNESTONE_ACCELERATOR_38329, IntType.SCENERY, "activate") { player, node ->
            lockInteractions(player, 1)
            animateScenery(node.asScenery(), 10196)
            return@on true
        }

        /*
         * Handles the interaction with the RC Hat item by toggling between two variations.
         * You can switch between wearing the goggles on the hat or without them.
         */

        on(RC_HAT, IntType.ITEM, "Goggles") { player, node ->
            val newHatId =
                when (node.id) {
                    13626 -> 13625
                    13625 -> 13626
                    13621 -> 13620
                    13620 -> 13621
                    13616 -> 13615
                    13615 -> 13616
                    else -> return@on false
                }
            replaceSlot(player, node.asItem().slot, Item(newHatId))
            return@on true
        }

        /*
         * Handles interaction with Wizard Elriss NPC to open the rewards interface.
         */

        on(NPCs.WIZARD_ELRISS_8032, IntType.NPC, "Exchange") { player, _ ->
            openInterface(player, Components.RCGUILD_REWARDS_779)
            return@on true
        }

        /*
         * Handles dialogue interaction with Wizards.
         */

        on(WIZARD_NPCs, IntType.NPC, "talk-to") { player, _ ->
            sendDialogueOptions(player, "Select an option", "I want to join the orb project!", "Never mind.")
            addDialogueAction(player) { player, button ->
                closeDialogue(player)
            }
            return@on true
        }

        /*
         * Handles dialogue interaction with Wizard Vief.
         */

        on(NPCs.WIZARD_VIEF_8030, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Ah! You'll help me, won't you?", FaceAnim.HAPPY)
            return@on true
        }
    }
}
