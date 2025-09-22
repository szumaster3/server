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
    private val farmKitItems = arrayListOf(Items.RAKE_5341, Items.SPADE_952, Items.SEED_DIBBER_5343, Items.WATERING_CAN8_5340, Items.SECATEURS_5329, Items.GARDENING_TROWEL_5325)
    private val runeKitItems = Runes.values().map { it.id }.toIntArray()
    private val talismanKitItems = Talisman.values().map { it.item.id }.toIntArray()

    override fun defineCommands() {

        /*
         * Provide a set of talismans.
         */

        define(
            name = "talismankit",
            privilege = Privilege.ADMIN,
            usage = "::talismankit",
            description = "Provides a set of talisman items.",
        ) { player, _ ->
            for (item in talismanKitItems) {
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
            for (item in farmKitItems) {
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
            for (item in runeKitItems) {
                addItem(player, item, 1000)
            }
            return@define
        }
    }
}
