package content.region.asgarnia.falador.quest.rd.plugin.tests

import content.region.asgarnia.falador.quest.rd.RDUtils
import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

class SirSpishyusRoomListeners : InteractionListener {

    companion object {
        /**
         * Varbits tracking the starting and ending positions of the fox.
         */
        const val foxFromVarbit = 680
        const val foxToVarbit = 681

        /**
         * Varbits tracking the starting and ending positions of the chicken.
         */
        const val chickenFromVarbit = 682
        const val chickenToVarbit = 683

        /**
         * Varbits tracking the starting and ending positions of the grain.
         */
        const val grainFromVarbit = 684
        const val grainToVarbit = 685

        /**
         * Zone representing the "from" area in this room puzzle.
         */
        val fromZoneBorder = ZoneBorders(2479, 4967, 2490, 4977)

        /**
         * Zone representing the "to" area in this room puzzle.
         */
        val toZoneBorder = ZoneBorders(2471, 4967, 2478, 4977)

        /**
         * Counts how many of the puzzle items (grain, fox, chicken) the player currently has equipped.
         *
         * @param player The player whose equipment is checked.
         * @return The number of items currently equipped by the player.
         */
        fun countEquipmentItems(player: Player): Int {
            var count = 0
            if (inEquipment(player, Items.GRAIN_5607)) {
                count++
            }
            if (inEquipment(player, Items.FOX_5608)) {
                count++
            }
            if (inEquipment(player, Items.CHICKEN_5609)) {
                count++
            }
            return count
        }

        /**
         * Checks if the player has successfully moved all
         * puzzle items to their correct positions.
         *
         * @param player The player to check.
         */
        fun checkFinished(player: Player) {
            if (
                getVarbit(player, foxToVarbit) == 1 &&
                getVarbit(player, chickenToVarbit) == 1 &&
                getVarbit(player, grainToVarbit) == 1
            ) {
                sendMessage(player, "Congratulations! You have solved this room's puzzle!")
                setAttribute(player, RecruitmentDrive.stageFail, true)
            }
        }

        /**
         * Checks if the player has made an invalid move that would fail the puzzle.
         *
         * @param player The player to check.
         * @return `true` if the puzzle conditions indicate failure, `false` otherwise.
         */
        fun checkFail(player: Player): Boolean {
            return ((getVarbit(player, foxFromVarbit) == 0 &&
                    getVarbit(player, chickenFromVarbit) == 0 &&
                    getVarbit(player, grainFromVarbit) == 1) ||
                    (getVarbit(player, foxFromVarbit) == 1 &&
                            getVarbit(player, chickenFromVarbit) == 0 &&
                            getVarbit(player, grainFromVarbit) == 0) ||
                    (getVarbit(player, foxToVarbit) == 1 &&
                            getVarbit(player, chickenToVarbit) == 1 &&
                            getVarbit(player, grainToVarbit) == 0) ||
                    (getVarbit(player, foxToVarbit) == 0 &&
                            getVarbit(player, chickenToVarbit) == 1 &&
                            getVarbit(player, grainToVarbit) == 1))
        }
    }

    override fun defineListeners() {
        on(Scenery.PRECARIOUS_BRIDGE_7286, SCENERY, "cross") { player, node ->
            if (countEquipmentItems(player) > 1) {
                sendDialogue(player, "I really don't think I should be carrying more than 5Kg across that rickety bridge...")
            } else if (checkFail(player)) {
                openDialogue(player, SirTinleyDialogueFile(2), NPC(NPCs.SIR_SPISHYUS_2282)) // Fail
            } else {
                lock(player, 5)
                sendMessage(player, "You carefully walk across the rickety bridge...")
                player.walkingQueue.reset()
                player.walkingQueue.addPath(2476, 4972)
            }
            return@on true
        }

        on(Scenery.PRECARIOUS_BRIDGE_7287, SCENERY, "cross") { player, node ->
            if (countEquipmentItems(player) > 1) {
                sendDialogue(player, "I really don't think I should be carrying more than 5Kg across that rickety bridge...")
            } else if (checkFail(player)) {
                openDialogue(player, SirTinleyDialogueFile(2), NPC(NPCs.SIR_SPISHYUS_2282)) // Fail
            } else {
                lock(player, 5)
                sendMessage(player, "You carefully walk across the rickety bridge...")
                player.walkingQueue.reset()
                player.walkingQueue.addPath(2484, 4972)
            }
            return@on true
        }

        on(Scenery.GRAIN_7284, SCENERY, "pick-up") { player, _ ->
            if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                if (fromZoneBorder.insideBorder(player)) {
                    replaceSlot(
                        player,
                        EquipmentSlot.CAPE.ordinal,
                        Item(Items.GRAIN_5607),
                        null,
                        Container.EQUIPMENT
                    )
                    setVarbit(player, grainFromVarbit, 1)
                }
                if (toZoneBorder.insideBorder(player)) {
                    replaceSlot(
                        player,
                        EquipmentSlot.CAPE.ordinal,
                        Item(Items.GRAIN_5607),
                        null,
                        Container.EQUIPMENT
                    )
                    setVarbit(player, grainToVarbit, 0)
                }
            }
            return@on true
        }

        onUnequip(Items.GRAIN_5607) { player, _ ->
            if (fromZoneBorder.insideBorder(player)) {
                removeItem(player, Items.GRAIN_5607, Container.EQUIPMENT)
                setVarbit(player, grainFromVarbit, 0)
            }
            if (toZoneBorder.insideBorder(player)) {
                removeItem(player, Items.GRAIN_5607, Container.EQUIPMENT)
                setVarbit(player, grainToVarbit, 1)
                checkFinished(player)
            }
            return@onUnequip true
        }

        on(Scenery.FOX_7277, SCENERY, "pick-up") { player, _ ->
            if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                if (fromZoneBorder.insideBorder(player)) {
                    replaceSlot(
                        player,
                        EquipmentSlot.WEAPON.ordinal,
                        Item(Items.FOX_5608),
                        null,
                        Container.EQUIPMENT
                    )
                    setVarbit(player, foxFromVarbit, 1)
                }
                if (toZoneBorder.insideBorder(player)) {
                    replaceSlot(
                        player,
                        EquipmentSlot.WEAPON.ordinal,
                        Item(Items.FOX_5608),
                        null,
                        Container.EQUIPMENT
                    )
                    setVarbit(player, foxToVarbit, 0)
                }
            }
            return@on true
        }

        onUnequip(Items.FOX_5608) { player, _ ->
            if (fromZoneBorder.insideBorder(player)) {
                removeItem(player, Items.FOX_5608, Container.EQUIPMENT)
                setVarbit(player, foxFromVarbit, 0)
            }
            if (toZoneBorder.insideBorder(player)) {
                removeItem(player, Items.FOX_5608, Container.EQUIPMENT)
                setVarbit(player, foxToVarbit, 1)
                checkFinished(player)
            }
            return@onUnequip true
        }

        on(Scenery.CHICKEN_7281, SCENERY, "pick-up") { player, _ ->
            if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                if (fromZoneBorder.insideBorder(player)) {
                    replaceSlot(
                        player,
                        EquipmentSlot.SHIELD.ordinal,
                        Item(Items.CHICKEN_5609),
                        null,
                        Container.EQUIPMENT
                    )
                    setVarbit(player, chickenFromVarbit, 1)
                }
                if (toZoneBorder.insideBorder(player)) {
                    replaceSlot(
                        player,
                        EquipmentSlot.SHIELD.ordinal,
                        Item(Items.CHICKEN_5609),
                        null,
                        Container.EQUIPMENT
                    )
                    setVarbit(player, chickenToVarbit, 0)
                }
            }
            return@on true
        }

        onUnequip(Items.CHICKEN_5609) { player, _ ->
            if (fromZoneBorder.insideBorder(player)) {
                removeItem(player, Items.CHICKEN_5609, Container.EQUIPMENT)
                setVarbit(player, chickenFromVarbit, 0)
            }
            if (toZoneBorder.insideBorder(player)) {
                removeItem(player, Items.CHICKEN_5609, Container.EQUIPMENT)
                setVarbit(player, chickenToVarbit, 1)
                checkFinished(player)
            }
            return@onUnequip true
        }
    }
}

class SirSpishyusDialogueFile(private val dialogueNum: Int = 0) : DialogueBuilderFile() {

    override fun create(b: DialogueBuilder) {
        b.onPredicate { player -> getAttribute(player, RecruitmentDrive.stagePass, false) }
            .npc(FaceAnim.HAPPY, "Excellent work, @name.", "Please step through the portal to your next challenge.")
            .end()

        b.onPredicate { player ->
            dialogueNum == 2 || getAttribute(player, RecruitmentDrive.stageFail, false)
        }
            .betweenStage { _, player, _, _ ->
                setAttribute(player, RecruitmentDrive.stageFail, true)
            }
            .npc(FaceAnim.SAD, "No... I am very sorry.", "You are not up to the challenge.", "Better luck in the future.")
            .endWith { _, player ->
                removeAttribute(player, "quest:recruitmentdrive-donotmove")
                setAttribute(player, RecruitmentDrive.stagePass, false)
                setAttribute(player, RecruitmentDrive.stageFail, false)
                RDUtils.failSequence(player)
            }

        b.onPredicate { true }
            .npcl(FaceAnim.FRIENDLY, "Ah, welcome @name.")
            .playerl(FaceAnim.FRIENDLY, "Hello. What am I supposed to do here?")
            .npcl(FaceAnim.FRIENDLY, "Take the fox, chicken, and grain across the bridge, but be careful!")
            .npcl(FaceAnim.FRIENDLY, "You can only carry one at a time, and leaving the wrong pair alone will result in failure.")
            .playerl(FaceAnim.FRIENDLY, "Got it. I'll see what I can do.")
    }
}
