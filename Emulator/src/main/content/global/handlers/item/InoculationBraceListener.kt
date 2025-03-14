package content.global.handlers.item

import core.api.*
import core.api.event.applyDisease
import core.api.event.isDiseased
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.command.Privilege
import core.game.system.timer.impl.Disease
import core.game.world.repository.Repository
import org.rs.consts.Items
import kotlin.math.min

class InoculationBraceListener :
    InteractionListener,
    Commands {
    private val maxProtection = 275

    override fun defineListeners() {
        on(Items.INOCULATION_BRACE_11088, IntType.ITEM, "operate") { player, node ->
            val remainingProtection = getCharge(node.asItem())
            sendMessage(player, "Your bracelet will protect you from $remainingProtection points of disease damage.")
            return@on true
        }

        onEquip(Items.INOCULATION_BRACE_11088) { player, node ->
            val charges = getCharge(node)

            if (charges <= 0) {
                sendMessage(player, "Your inoculation bracelet has no remaining protection and crumbles to dust.")
                removeItem(player, node, Container.EQUIPMENT)
                return@onEquip true
            }

            val diseaseTimer = getTimer<Disease>(player)
            if (diseaseTimer != null && isDiseased(player)) {
                val blockedDamage = min(diseaseTimer.hitsLeft, charges)
                val remainingCharges = charges - blockedDamage
                setCharge(node, remainingCharges)

                if (remainingCharges <= 0) {
                    sendMessage(player, "Your bracelet crumbles to dust.")
                    removeItem(player, node, Container.EQUIPMENT)
                }
            }
            return@onEquip true
        }
    }

    override fun defineCommands() {
        define("disease", privilege = Privilege.ADMIN, "Disease for testing purposes.") { player, strings ->
            if (strings.size != 3) {
                sendMessage(player, "Invalid arguments. Use: ::disease username damage")
                return@define
            }

            val targetName = strings[1]
            val damage = strings[2].toIntOrNull()
            val targetPlayer = Repository.getPlayerByName(targetName)

            if (targetPlayer == null) {
                sendMessage(player, "Player $targetName does not exist.")
                return@define
            }

            if (damage == null) {
                sendMessage(player, "Damage must be a valid integer. Use: ::disease username damage")
                return@define
            }

            applyDisease(targetPlayer, targetPlayer, damage)
            sendMessage(player, "Applied $damage disease damage to $targetName.")
        }
    }
}
