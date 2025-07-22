package content.region.asgarnia.guild.crafting.plugin

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class CraftingGuildPlugin : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles access to the Crafting Guild.
         */

        on(GUILD_DOORS, IntType.SCENERY, "open") { player, door ->
            if (!hasLevelStat(player, Skills.CRAFTING, 40)) {
                sendNPCDialogueLines(player, NPCs.MASTER_CRAFTER_805, FaceAnim.NEUTRAL, false, "Sorry, only experienced crafters are allowed in here.", "You must be level 40 or above to enter.")
                return@on false
            }

            val doorScenery = door.asScenery()

            if (player.location.y < 3289) {
                DoorActionHandler.handleAutowalkDoor(player, doorScenery)
                return@on true
            }

            val hasApron = inEquipment(player, BROWN_APRON)
            val hasCape = anyInEquipment(player, Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)

            if (hasApron || hasCape) {
                DoorActionHandler.handleAutowalkDoor(player, doorScenery)
                sendNPCDialogue(player, NPCs.MASTER_CRAFTER_805, "Welcome to the Guild of Master Craftsmen.", FaceAnim.HAPPY)
            } else {
                sendNPCDialogue(player, NPCs.MASTER_CRAFTER_805, "Where's your brown apron? You can't come in here unless you're wearing one.", FaceAnim.HALF_ASKING)
                addDialogueAction(player) { _, _ ->
                    sendPlayerDialogue(player, "Err... I haven't got one.", FaceAnim.HALF_THINKING)
                }
            }

            return@on true
        }
    }

    companion object {
        private const val GUILD_DOORS = Scenery.GUILD_DOOR_2647
        private const val BROWN_APRON = Items.BROWN_APRON_1757
    }
}