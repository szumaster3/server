package content.region.asgarnia.quest.rd.handlers.tests

import content.region.asgarnia.quest.rd.RecruitmentDrive
import content.region.asgarnia.quest.rd.cutscene.FailTest
import core.api.*
import core.api.ui.closeDialogue
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

/**
 * Handles the logic test interaction within the Recruitment Drive quest.
 * This test requires players to transport a fox, chicken, and grain across a bridge
 * while adhering to specific constraints to complete the puzzle.
 */
class LogicTest : InteractionListener {
    companion object {
        /**
         * Map items to varbits.
         */
        private val itemVarbits =
            mapOf(
                Items.FOX_5608 to Pair(680, 681),
                Items.CHICKEN_5609 to Pair(682, 683),
                Items.GRAIN_5607 to Pair(684, 685),
            )

        private val fromZoneBorder = ZoneBorders(2479, 4967, 2490, 4977)
        private val toZoneBorder = ZoneBorders(2471, 4967, 2478, 4977)

        /**
         * Counts the number of riddle items currently in the player's equipment.
         * @param player The player.
         * @return The number of items.
         */
        private fun countEquipmentItems(player: Player) = itemVarbits.keys.count { inEquipment(player, it) }

        /**
         * Checks if the riddle is completed.
         * @param player The player.
         */
        private fun checkFinished(player: Player) {
            if (itemVarbits.values.all { getVarbit(player, it.second) == 1 }) {
                sendMessage(player, "Congratulations! You have solved this room's puzzle!")
                setAttribute(player, RecruitmentDrive.stagePass, 1)
            }
        }

        /**
         * Checks if the player has failed the riddle.
         * @param player The player.
         * @return True if the player has failed, false otherwise.
         */
        private fun checkFail(player: Player): Boolean {
            val (left, right) = itemVarbits.values.partition { getVarbit(player, it.first) == 1 }
            return (left.size == 2 && !playerInLeftZone(player)) || (right.size == 2 && !playerInRightZone(player))
        }

        private fun playerInLeftZone(player: Player) = fromZoneBorder.insideBorder(player)

        private fun playerInRightZone(player: Player) = toZoneBorder.insideBorder(player)

        /**
         * Resets the puzzle stage.
         * @param player The player.
         */
        private fun resetStage(player: Player) {
            itemVarbits.forEach { (item, vars) ->
                setVarbit(player, vars.first, 0)
                setVarbit(player, vars.second, 0)
                removeItem(player, item, Container.EQUIPMENT)
            }
        }
    }

    override fun defineListeners() {
        listOf(Scenery.PRECARIOUS_BRIDGE_7286, Scenery.PRECARIOUS_BRIDGE_7287).forEach { bridge ->

            /*
             * Handles crossing the bridge.
             */
            on(bridge, IntType.SCENERY, "cross") { player, _ ->
                when {
                    countEquipmentItems(player) > 1 ->
                        sendDialogue(
                            player,
                            "I shouldn't carry more than 5Kg across the bridge...",
                        )

                    checkFail(player) -> {
                        closeDialogue(player)
                        openDialogue(player, PatienceTest(2), NPCs.SIR_SPISHYUS_2282)
                    }

                    else -> {
                        lock(player, 5)
                        sendMessage(player, "You carefully walk across the bridge...")
                        val path =
                            if (bridge == Scenery.PRECARIOUS_BRIDGE_7286) listOf(2476, 4972) else listOf(2484, 4972)
                        player.walkingQueue.reset()
                        player.walkingQueue.addPath(path[0], path[1])
                    }
                }
                return@on true
            }
        }

        itemVarbits.forEach { (item, vars) ->
            /*
             * Handles picking up an item from scenery.
             */
            on(getSceneryForItem(item), SCENERY, "pick-up") { player, _ ->
                if (getAttribute(player, RecruitmentDrive.stageFail, 0) == 0) {
                    val inLeftZone = fromZoneBorder.insideBorder(player)
                    val inRightZone = toZoneBorder.insideBorder(player)
                    if (inLeftZone || inRightZone) {
                        replaceSlot(player, getEquipmentSlot(item), Item(item), null, Container.EQUIPMENT)
                        setVarbit(player, if (inLeftZone) vars.first else vars.second, if (inLeftZone) 1 else 0)
                    }
                }
                return@on true
            }

            /*
             * Handles unequipping an item.
             */

            onUnequip(item) { player, _ ->
                val inLeftZone = fromZoneBorder.insideBorder(player)
                val inRightZone = toZoneBorder.insideBorder(player)
                removeItem(player, item, Container.EQUIPMENT)
                setVarbit(player, if (inLeftZone) vars.first else vars.second, if (inLeftZone) 0 else 1)
                if (inRightZone) checkFinished(player)
                return@onUnequip true
            }
        }
    }

    /**
     * Gets the corresponding scenery object for an item.
     * @param item The item ID.
     * @return The associated scenery ID.
     */
    private fun getSceneryForItem(item: Int) =
        when (item) {
            Items.GRAIN_5607 -> Scenery.GRAIN_7284
            Items.FOX_5608 -> Scenery.FOX_7277
            Items.CHICKEN_5609 -> Scenery.CHICKEN_7281
            else -> throw IllegalArgumentException("Invalid item ID")
        }

    /**
     * Gets the equipment slot for a given item.
     * @param item The item ID.
     * @return The corresponding equipment slot index.
     */
    private fun getEquipmentSlot(item: Int) =
        when (item) {
            Items.GRAIN_5607 -> EquipmentSlot.CAPE.ordinal
            Items.FOX_5608 -> EquipmentSlot.WEAPON.ordinal
            Items.CHICKEN_5609 -> EquipmentSlot.SHIELD.ordinal
            else -> throw IllegalArgumentException("Invalid item ID")
        }
}

class SirSpishyusDialogueFile(
    private val dialogueNum: Int = 0,
) : DialogueBuilderFile() {
    override fun create(b: DialogueBuilder) {
        b
            .onPredicate { player -> getAttribute(player, RecruitmentDrive.stagePass, false) }
            .npc(FaceAnim.HAPPY, "Excellent work, @name.", "Please step through the portal to your next challenge.")
            .end()

        b
            .onPredicate { player -> dialogueNum == 2 || getAttribute(player, RecruitmentDrive.stageFail, false) }
            .betweenStage { _, player, _, _ -> setAttribute(player, RecruitmentDrive.stageFail, true) }
            .npc(
                FaceAnim.SAD,
                "No... I am very sorry.",
                "You are not up to the challenge.",
                "Better luck in the future.",
            ).endWith { _, player ->
                removeAttribute(player, PatienceTest.patience)
                setAttribute(player, RecruitmentDrive.stagePass, false)
                setAttribute(player, RecruitmentDrive.stageFail, false)
                runTask(player, 3) {
                    FailTest(player).start()
                }
            }

        b
            .onPredicate { true }
            .npcl(FaceAnim.FRIENDLY, "Ah, welcome @name.")
            .playerl(FaceAnim.FRIENDLY, "Hello. What am I supposed to do here?")
            .npcl(FaceAnim.FRIENDLY, "Take the fox, chicken, and grain across the bridge, but be careful!")
            .npcl(
                FaceAnim.FRIENDLY,
                "You can only carry one at a time, and leaving the wrong pair alone will result in failure.",
            ).playerl(FaceAnim.FRIENDLY, "Got it. I'll see what I can do.")
    }
}
