package content.global.skill.runecrafting

import core.api.*
import core.game.container.impl.EquipmentContainer.SLOT_HAT
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.quest.QuestReq
import core.game.node.entity.player.link.quest.QuestRequirements
import core.game.node.item.Item
import core.game.system.task.Pulse
import shared.consts.Animations

/**
 * Represents interactions with the mysterious ruins in the game.
 */
class MysteriousRuinsPlugin : InteractionListener {
    private val sceneryIDs = allRuins()
    private val stavesIDs = RunecraftingStaff.values().map { it.staffId }.toIntArray()
    private val talismanIDs = RunecraftingStaff.values().map { it.talisman }.toIntArray()

    override fun defineListeners() {

        /*
         * Handles using a talisman with a scenery object.
         */

        onUseWith(IntType.SCENERY, talismanIDs, *sceneryIDs) { player, used, with ->
            return@onUseWith handleTalisman(player, used, with)
        }

        /*
         * Handles interaction with scenery.
         */

        on(sceneryIDs, IntType.SCENERY, "enter", "search") { player, node ->
            if (anyInEquipment(player, *stavesIDs)) {
                handleStaff(player, node)
            } else {
                handleTiara(player, node)
            }
            return@on true
        }
    }

    /**
     * Retrieves all the ids for the mysterious ruins.
     */
    private fun allRuins(): IntArray =
        MysteriousRuins.values().flatMap { ruins -> ruins.sceneryIds.asList() }.toIntArray()

    /**
     * Handles the interaction when a talisman is used with a scenery object.
     */
    private fun handleTalisman(player: Player, used: Node, with: Node): Boolean {
        val ruin = MysteriousRuins.forObject(with.asScenery())
        if (!checkQuestCompletion(player, ruin!!)) {
            return true
        }

        val talisman = Talisman.forItem(used.asItem())
        if (talisman != ruin.talisman && talisman != Talisman.ELEMENTAL) {
            sendMessage(player, "Nothing interesting happens.")
            return false
        }
        if (talisman == Talisman.ELEMENTAL && (ruin.talisman != Talisman.AIR && ruin.talisman != Talisman.WATER && ruin.talisman != Talisman.FIRE && ruin.talisman != Talisman.EARTH)) {
            sendMessage(player, "Nothing interesting happens.")
            return false
        }

        teleportToRuinTalisman(player, used.asItem(), ruin)
        return true
    }

    /**
     * Handles the interaction when the player uses a staff with a scenery object.
     */
    private fun handleStaff(player: Player, node: Node): Boolean {
        val ruin = MysteriousRuins.forObject(node.asScenery())

        if (!checkQuestCompletion(player, ruin!!)) {
            return true
        }

        submitTeleportPulse(player, ruin, 0)
        return true
    }

    /**
     * Handles the interaction when the player uses a tiara with a scenery object.
     */
    private fun handleTiara(player: Player, node: Node): Boolean {
        val ruin = MysteriousRuins.forObject(node.asScenery())

        if (!checkQuestCompletion(player, ruin!!)) {
            return true
        }

        val tiara = player.equipment?.get(SLOT_HAT)?.let { Tiara.forItem(it) }
        if (tiara != ruin.tiara) {
            sendMessage(player, "Nothing interesting happens.")
            return false
        }

        submitTeleportPulse(player, ruin, 0)
        return true
    }

    /**
     * Checks if the player has completed the quest required for interacting with the ruin.
     */
    private fun checkQuestCompletion(player: Player, ruin: MysteriousRuins): Boolean = when (ruin) {
        MysteriousRuins.DEATH -> hasRequirement(player, QuestReq(QuestRequirements.MEP_2), true)
        MysteriousRuins.BLOOD -> hasRequirement(player, QuestReq(QuestRequirements.SEERGAZE), true)
        else -> hasRequirement(player, QuestReq(QuestRequirements.RUNE_MYSTERIES), true)
    }

    /**
     * Teleports the player to the mysterious ruin after using the talisman.
     */
    private fun teleportToRuinTalisman(player: Player, talisman: Item, ruin: MysteriousRuins) {
        lock(player, 4)
        animate(player, Animations.HUMAN_BURYING_BONES_827)
        sendMessage(player, "You hold the ${talisman.name} towards the mysterious ruins.")
        submitTeleportPulse(player, ruin, 3)
    }

    /**
     * Submits a pulse to teleport the player to the ruin after a short delay.
     */
    private fun submitTeleportPulse(player: Player, ruin: MysteriousRuins, delay: Int) {
        sendMessage(player, "You feel a powerful force take hold of you.")
        submitWorldPulse(
            object : Pulse(delay, player) {
                override fun pulse(): Boolean {
                    teleport(player, ruin.end)
                    return true
                }
            },
        )
    }
}
