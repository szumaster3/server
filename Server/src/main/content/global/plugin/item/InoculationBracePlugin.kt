package content.global.plugin.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.command.Privilege
import core.game.system.timer.impl.Disease
import core.game.world.repository.Repository
import shared.consts.Items
import kotlin.math.min
import kotlin.math.roundToInt

class InoculationBracePlugin : InteractionListener, Commands {

    private val maxProtection = 275
    private val maxCharges = 1000
    private val chargePerDamage = maxCharges.toDouble() / maxProtection

    override fun defineListeners() {
        on(Items.INOCULATION_BRACE_11088, IntType.ITEM, "operate") { player, node ->
            val charges = getCharge(node.asItem())
            val remainingProtection = ((charges / chargePerDamage).coerceAtLeast(0.0)).roundToInt()
            sendMessage(
                player,
                "Your bracelet will protect you from $remainingProtection more points of disease damage."
            )
            return@on true
        }

        onEquip(Items.INOCULATION_BRACE_11088) { player, node ->
            var charges = getCharge(node)

            if (charges <= 0) {
                sendMessage(player, "Your inoculation bracelet has no remaining protection and crumbles to dust.")
                removeItem(player, node, Container.EQUIPMENT)
                return@onEquip true
            }

            val diseaseTimer = getTimer<Disease>(player)
            if (diseaseTimer != null && isDiseased(player)) {
                val remainingProtection = (charges / chargePerDamage).toInt().coerceAtLeast(0)
                val blockedDamage = min(diseaseTimer.hitsLeft, remainingProtection)
                val usedCharges = (blockedDamage * chargePerDamage).roundToInt()
                charges -= usedCharges
                setCharge(node, charges.coerceAtLeast(0))

                if (charges <= 0) {
                    sendMessage(player, "Your bracelet crumbles to dust after absorbing disease damage.")
                    removeItem(player, node, Container.EQUIPMENT)
                } else {
                    val remaining = ((charges / chargePerDamage).coerceAtLeast(0.0)).roundToInt()
                    sendMessage(
                        player,
                        "Your bracelet absorbs $blockedDamage points of disease damage. Remaining protection: $remaining."
                    )
                }
            }

            return@onEquip true
        }
    }

    override fun defineCommands() {
        define(
            "disease",
            privilege = Privilege.ADMIN,
            description = "Applies disease damage for testing purposes."
        ) { player, args ->

            if (args.size != 3) {
                sendMessage(player, "Usage: ::disease <username> <damage>")
                return@define
            }

            val targetName = args[1]
            val damage = args[2].toIntOrNull()
            val targetPlayer = Repository.getPlayerByName(targetName)

            when {
                targetPlayer == null -> sendMessage(player, "Player '$targetName' does not exist.")
                damage == null -> sendMessage(player, "Damage must be a valid number.")
                else -> {
                    applyDisease(player, targetPlayer, damage)
                    sendMessage(player, "Applied $damage disease damage to $targetName.")
                }
            }
        }
    }
}
