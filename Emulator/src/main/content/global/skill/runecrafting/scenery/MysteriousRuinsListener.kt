package content.global.skill.runecrafting.scenery

import content.global.skill.runecrafting.items.Staves
import content.global.skill.runecrafting.items.Talisman
import content.global.skill.runecrafting.items.Tiara
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
import org.rs.consts.Animations

class MysteriousRuinsListener : InteractionListener {
    private val sceneryIDs = allRuins()
    private val stavesIDs = Staves.values().map { it.item }.toIntArray()
    private val talismanIDs =
        arrayOf(
            1438,
            1448,
            1444,
            1440,
            1442,
            5516,
            1446,
            1454,
            1452,
            1462,
            1458,
            1456,
            1450,
            1460,
        ).toIntArray()

    override fun defineListeners() {
        onUseWith(IntType.SCENERY, talismanIDs, *sceneryIDs) { player, used, with ->
            return@onUseWith handleTalisman(player, used, with)
        }

        on(sceneryIDs, IntType.SCENERY, "enter", "search") { player, node ->
            if (anyInEquipment(player, *stavesIDs)) {
                handleStaff(player, node)
            } else {
                handleTiara(player, node)
            }
            return@on true
        }
    }

    private fun allRuins(): IntArray {
        return MysteriousRuins
            .values()
            .flatMap { ruins -> ruins.`object`.asList() }
            .toIntArray()
    }

    private fun handleTalisman(
        player: Player,
        used: Node,
        with: Node,
    ): Boolean {
        val ruin =
            MysteriousRuins.forObject(with.asScenery())
        if (!checkQuestCompletion(player, ruin!!)) {
            return true
        }

        val talisman = Talisman.forItem(used.asItem())
        if (talisman != ruin.talisman && talisman != Talisman.ELEMENTAL) {
            sendMessage(player, "Nothing interesting happens.")
            return false
        }
        if (talisman == Talisman.ELEMENTAL &&
            (
                ruin.talisman != Talisman.AIR &&
                    ruin.talisman != Talisman.WATER &&
                    ruin.talisman != Talisman.FIRE &&
                    ruin.talisman != Talisman.EARTH
            )
        ) {
            sendMessage(player, "Nothing interesting happens.")
            return false
        }

        teleportToRuinTalisman(player, used.asItem(), ruin)
        return true
    }

    private fun handleStaff(
        player: Player,
        node: Node,
    ): Boolean {
        val ruin =
            MysteriousRuins.forObject(node.asScenery())

        if (!checkQuestCompletion(player, ruin!!)) {
            return true
        }

        submitTeleportPulse(player, ruin, 0)
        return true
    }

    private fun handleTiara(
        player: Player,
        node: Node,
    ): Boolean {
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

    private fun checkQuestCompletion(
        player: Player,
        ruin: MysteriousRuins,
    ): Boolean {
        return when (ruin) {
            MysteriousRuins.DEATH -> hasRequirement(player, QuestReq(QuestRequirements.MEP_2), true)
            MysteriousRuins.BLOOD -> hasRequirement(player, QuestReq(QuestRequirements.SEERGAZE), true)
            else -> hasRequirement(player, QuestReq(QuestRequirements.RUNE_MYSTERIES), true)
        }
    }

    private fun teleportToRuinTalisman(
        player: Player,
        talisman: Item,
        ruin: MysteriousRuins,
    ) {
        lock(player, 4)
        animate(player, Animations.MULTI_BEND_OVER_827)
        sendMessage(player, "You hold the ${talisman.name} towards the mysterious ruins.")
        submitTeleportPulse(player, ruin, 3)
    }

    private fun submitTeleportPulse(
        player: Player,
        ruin: MysteriousRuins,
        delay: Int,
    ) {
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
