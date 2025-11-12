package content.global.plugins.item

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.world.repository.Repository
import shared.consts.Items
import kotlin.math.roundToInt

class InoculationBracePlugin : InteractionListener, Commands {

    private val maxProtection = 275.0
    private val maxCharges = 1000
    private val chargePerDamage = maxCharges / maxProtection

    companion object {
        lateinit var instance: InoculationBracePlugin
            private set
    }

    init {
        instance = this
    }

    override fun defineListeners() {
        on(Items.INOCULATION_BRACE_11088, IntType.ITEM, "operate") { player, node ->
            val item = node.asItem()
            val charges = getCharge(item)
            val remainingProtection = (charges / chargePerDamage).coerceAtLeast(0.0).roundToInt()
            sendMessage(
                player,
                "Your bracelet will protect you from $remainingProtection more points of disease damage."
            )
            return@on true
        }

        onEquip(Items.INOCULATION_BRACE_11088) { player, node ->
            val charges = getCharge(node)
            if (charges <= 0) {
                sendMessage(player, "Your inoculation bracelet has no remaining protection and crumbles to dust.")
                removeItem(player, node, Container.EQUIPMENT)
            }
            return@onEquip true
        }
    }

    fun applyDiseaseAbsorption(player: Player, incomingDamage: Int): Int {
        val bracelet = player.equipment.get(EquipmentSlot.HANDS.ordinal) ?: return 0
        if (bracelet.id != Items.INOCULATION_BRACE_11088) return 0

        var charges = getCharge(bracelet)
        if (charges <= 0) {
            sendMessage(player, "Your inoculation bracelet crumbles to dust.")
            removeItem(player, bracelet, Container.EQUIPMENT)
            return 0
        }

        val availableProtection = (charges / chargePerDamage).coerceAtLeast(0.0)
        val blocked = incomingDamage.coerceAtMost(availableProtection.roundToInt())
        val usedCharges = (blocked * chargePerDamage).roundToInt()
        charges -= usedCharges
        setCharge(bracelet, charges.coerceAtLeast(0))

        if (charges <= 0) {
            sendMessage(player, "Your bracelet crumbles to dust after absorbing disease damage.")
            removeItem(player, bracelet, Container.EQUIPMENT)
        } else if (blocked > 0) {
            val remaining = (charges / chargePerDamage).roundToInt()
            sendMessage(player, "Your bracelet absorbs $blocked points of disease damage. Remaining protection: $remaining.")
        }

        return blocked
    }

    override fun defineCommands() {
        define(
            "disease",
            privilege = Privilege.ADMIN,
            description = "Applies disease hits for testing purposes."
        ) { player, args ->
            if (args.size != 3) {
                sendMessage(player, "Usage: ::disease username hits")
                return@define
            }

            val targetName = args[1]
            val damage = args[2].toIntOrNull()
            val targetPlayer = Repository.getPlayerByName(targetName)

            when {
                targetPlayer == null -> sendMessage(player, "Player '$targetName' does not exist.")
                damage == null -> sendMessage(player, "Hits must be a valid number.")
                else -> {
                    applyDisease(player, targetPlayer, damage)
                    sendMessage(player, "Applied $damage disease hits to $targetName.")
                }
            }
        }
    }
}
