package content.global.skill.slayer.iface

import content.global.skill.slayer.SlayerManager
import content.global.skill.slayer.Tasks
import core.api.*
import core.game.component.Component
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import shared.consts.Components
import shared.consts.Items

class SlayerRewardInterface : InterfaceListener {

    companion object {
        const val ASSIGNMENT = Components.SMKI_ASSIGNMENT_161
        const val LEARN = Components.SMKI_LEARN_163
        const val BUY = Components.SMKI_BUY_164
    }

    override fun defineInterfaceListeners() {
        onOpen(ASSIGNMENT) { player, component ->
            updateInterface(player, component)
            return@onOpen true
        }

        onOpen(LEARN) { player, component ->
            updateInterface(player, component)
            return@onOpen true
        }

        onOpen(BUY) { player, component ->
            updateInterface(player, component)
            return@onOpen true
        }

        on(ASSIGNMENT) { player, _, _, button, _, _ ->
            val mgr = SlayerManager.getInstance(player)
            when (button) {
                // Cancel task (30 points).
                23,
                26 -> {
                    if (!mgr.hasTask()) {
                        sendMessage(player, "You don't have an active task right now.")
                        return@on true
                    }
                    if (purchase(player, 30)) {
                        mgr.clear()
                        sendMessage(player, "You have canceled your current task.")
                    }
                }
                // Block task (100 points).
                24,
                27 -> {
                    if (mgr.task == null) {
                        sendMessage(player, "You don't have a slayer task.")
                        return@on true
                    }

                    if (mgr.removed.size >= 4) {
                        sendMessage(player, "You can't remove anymore tasks.")
                        return@on true
                    }
                    // Quest requirement logic.
                    val qp = player.questRepository.availablePoints
                    val size = mgr.removed.size
                    val requiredQP = arrayOf(50, 100, 150, 200)[size]

                    if (mgr.slayerPoints >= 30 && !player.isAdmin && qp < requiredQP) {
                        sendMessage(
                            player,
                            "You need $requiredQP quest points as a requirement in order to block this task."
                        )
                        return@on true
                    }

                    if (purchase(player, 100)) {
                        mgr.task?.let { mgr.removed.add(it) }
                        mgr.clear()
                        updateInterface(player, Component(ASSIGNMENT))
                    }
                }
                // Unblock task.
                36,
                37,
                38,
                39 -> {
                    val index = 3 - (39 - button)
                    if (index in mgr.removed.indices) {
                        mgr.removed.removeAt(index)
                        updateInterface(player, Component(ASSIGNMENT))
                    }
                }
                15 -> openTab(player, BUY)
                14 -> openTab(player, LEARN)
            }
            return@on true
        }

        on(LEARN) { player, _, _, button, _, _ ->
            val flags = SlayerManager.getInstance(player).flags
            when (button) {
                // Tab switching.
                14 -> openTab(player, ASSIGNMENT)
                15 -> openTab(player, BUY)
                // Unlock Broads (300).
                22,
                29 -> {
                    if (flags.isBroadsUnlocked()) {
                        sendMessage(player, "You don't need to learn this ability again.")
                        return@on true
                    }
                    if (purchase(player, 300)) {
                        flags.unlockBroads()
                        updateInterface(player, Component(LEARN))
                    }
                }
                // Unlock Ring (300).
                23,
                30 -> {
                    if (flags.isRingUnlocked()) {
                        sendMessage(player, "You don't need to learn this ability again.")
                        return@on true
                    }
                    if (purchase(player, 300)) {
                        flags.unlockRing()
                        updateInterface(player, Component(LEARN))
                    }
                }
                // Unlock Helm (400).
                24,
                31 -> {
                    if (flags.isHelmUnlocked()) {
                        sendMessage(player, "You don't need to learn this ability again.")
                        return@on true
                    }
                    if (purchase(player, 400)) {
                        flags.unlockHelm()
                        updateInterface(player, Component(LEARN))
                    }
                }
            }

            return@on true
        }

        on(BUY) { player, _, _, button, _, _ ->
            when (button) {
                // Tab switching.
                16 -> openTab(player, LEARN)
                17 -> openTab(player, ASSIGNMENT)
                // 10k XP (400).
                24,
                32 ->
                    if (purchase(player, 400)) {
                        player.skills.addExperience(
                            core.game.node.entity.skill.Skills.SLAYER,
                            10000.0,
                            false
                        )
                    }
                // Ring of slaying (75).
                26,
                33 -> {
                    if (player.inventory.freeSlots() < 1) {
                        sendMessage(player, "You don't have enough inventory space.")
                        return@on true
                    }
                    if (purchase(player, 75)) {
                        player.inventory.add(Item(Items.RING_OF_SLAYING8_13281))
                    }
                }
                // Runes pack (35).
                28,
                36 ->
                    if (purchase(player, 35)) {
                        player.inventory.add(Item(Items.MIND_RUNE_558, 1000))
                        player.inventory.add(Item(Items.DEATH_RUNE_560, 250))
                    }
                // Broad bolts (35).
                34,
                37 ->
                    if (purchase(player, 35)) {
                        player.inventory.add(Item(Items.BROAD_TIPPED_BOLTS_13280, 250))
                    }
                // Broad arrows (35).
                35,
                39 ->
                    if (purchase(player, 35)) {
                        player.inventory.add(Item(Items.BROAD_ARROWS_4172, 250))
                    }
            }
            return@on true
        }
    }

    private fun purchase(player: Player, amount: Int): Boolean {
        val mgr = SlayerManager.getInstance(player)

        if (mgr.slayerPoints < amount) {
            sendMessage(player, "You need $amount Slayer points in order to purchase this reward.")
            return false
        }

        mgr.slayerPoints -= amount
        updateInterface(player, player.interfaceManager.opened)
        return true
    }

    private fun openTab(player: Player, tab: Int) {
        player.interfaceManager.open(Component(tab))
        updateInterface(player, Component(tab))
    }

    private fun updateInterface(player: Player, comp: Component?) {
        if (comp == null) return
        val mgr = SlayerManager.getInstance(player)
        val points = mgr.slayerPoints
        val spacing = " ".repeat(points.toString().length)
        when (comp.id) {
            Components.SMKI_ASSIGNMENT_161 -> {
                val childs = intArrayOf(35, 30, 31, 32)
                val letters = arrayOf("A", "B", "C", "D")

                for (i in 0 until 4) {
                    val task: Tasks? = if (i < mgr.removed.size) mgr.removed[i] else null
                    sendString(player, task?.name ?: letters[i], comp.id, childs[i])
                }

                sendString(player, spacing + points, comp.id, 19)
            }
            Components.SMKI_LEARN_163 -> {
                val flags = mgr.flags
                sendInterfaceConfig(player, comp.id, 25, !flags.isBroadsUnlocked())
                sendInterfaceConfig(player, comp.id, 26, !flags.isRingUnlocked())
                sendInterfaceConfig(player, comp.id, 27, !flags.isHelmUnlocked())
                sendString(player, spacing + points, comp.id, 18)
            }
            Components.SMKI_BUY_164 -> {
                sendString(player, spacing + points, comp.id, 20)
            }
        }
    }
}
