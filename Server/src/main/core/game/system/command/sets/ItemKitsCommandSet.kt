package core.game.system.command.sets

import content.global.skill.runecrafting.Talisman
import core.api.addItem
import core.game.node.entity.combat.spell.Runes
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.plugin.Initializable
import shared.consts.Items

@Initializable
class ItemKitsCommandSet : CommandSet(Privilege.ADMIN) {
    private val farmKit = arrayListOf(Items.RAKE_5341, Items.SPADE_952, Items.SEED_DIBBER_5343, Items.WATERING_CAN8_5340, Items.SECATEURS_5329, Items.GARDENING_TROWEL_5325)
    private val talismanKit = Talisman.values().map { it.item }.toIntArray()
    private val runeKit = Runes.values()
        .filter { "STAFF" !in it.name }
        .map { it.id }
        .toIntArray()

    override fun defineCommands() {

        define(name = "rope", privilege = Privilege.ADMIN, usage = "", description = "") { p, _ ->
            addItem(p, Items.ROPE_954)
        }

        define(name = "spade", privilege = Privilege.ADMIN, usage = "", description = "") { p, _ ->
            addItem(p, Items.SPADE_952)
        }

        define(name = "knife", privilege = Privilege.ADMIN, usage = "", description = "") { p, _ ->
            addItem(p, Items.KNIFE_946)
        }

        define(name = "chisel", privilege = Privilege.ADMIN, usage = "", description = "") { p, _ ->
            addItem(p, Items.CHISEL_1755)
        }

        /*
         * Provide a set of talismans.
         */

        define(
            name = "talismankit",
            privilege = Privilege.ADMIN,
            usage = "::talismankit",
            description = "Provides a set of talisman items.",
        ) { player, _ ->
            for (item in talismanKit) {
                player.inventory.add(Item(item))
            }
            return@define
        }

        /*
         * Provide a kit of various farming equipment.
         */

        define(
            name = "farmkit",
            privilege = Privilege.ADMIN,
            usage = "::farmkit",
            description = "Provides a kit of various farming equipment.",
        ) { player, _ ->
            for (item in farmKit) {
                player.inventory.add(Item(item))
            }
            return@define
        }

        /*
         * Provides 1000 of each rune type to the player.
         */

        define(
            name = "runekit",
            privilege = Privilege.ADMIN,
            usage = "::runekit",
            description = "Gives 1k of each Rune type.",
        ) { player, _ ->
            for (item in runeKit) {
                addItem(player, item, 1000)
            }
            return@define
        }
    }
}
