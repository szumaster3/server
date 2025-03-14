package content.region.asgarnia.handlers.guilds

import core.api.anyInEquipment
import core.api.hasLevelStat
import core.api.inEquipment
import core.api.openDialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

class CraftingGuildListener : InteractionListener {
    override fun defineListeners() {
        on(GUILD_DOORS, IntType.SCENERY, "open") { player, door ->
            if (player.location == Location.create(2933, 3289, 0)) {
                if (!hasLevelStat(player, Skills.CRAFTING, 40)) {
                    openDialogue(player, CraftingGuildDoor(2))
                    return@on false
                }

                if (inEquipment(player, BROWN_APRON) || anyInEquipment(player, *CRAFTING_CAPE)) {
                    openDialogue(player, CraftingGuildDoor(0))
                    DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                    return@on true
                } else {
                    openDialogue(player, CraftingGuildDoor(1))
                    return@on false
                }
            } else {
                DoorActionHandler.handleAutowalkDoor(player, door.asScenery())
                return@on true
            }
        }
    }

    inner class CraftingGuildDoor(
        val it: Int,
    ) : DialogueFile() {
        override fun handle(
            componentID: Int,
            buttonID: Int,
        ) {
            npc =
                core.game.node.entity.npc
                    .NPC(NPCs.MASTER_CRAFTER_805)
            when (it) {
                0 ->
                    when (stage) {
                        0 ->
                            npcl(FaceAnim.FRIENDLY, "Welcome to the Guild of Master Craftsmen.").also {
                                stage =
                                    END_DIALOGUE
                            }
                    }
                1 ->
                    when (stage) {
                        0 ->
                            npcl(
                                FaceAnim.ASKING,
                                "Where's your brown apron? You can't come in here unless you're wearing one.",
                            ).also {
                                stage++
                            }
                        1 -> player(FaceAnim.SAD, "Err... I haven't got one.").also { stage = END_DIALOGUE }
                    }
                2 ->
                    when (stage) {
                        0 ->
                            npc(
                                FaceAnim.NEUTRAL,
                                "Sorry, only experienced crafters are allowed in here.",
                                "You must be level 40 or above to enter.",
                            ).also {
                                stage =
                                    END_DIALOGUE
                            }
                    }
            }
        }
    }

    companion object {
        private const val GUILD_DOORS = Scenery.GUILD_DOOR_2647
        private const val BROWN_APRON = Items.BROWN_APRON_1757
        private val CRAFTING_CAPE = intArrayOf(Items.CRAFTING_CAPE_9780, Items.CRAFTING_CAPET_9781)
    }
}
