package core.game.system.command.sets

import content.global.skill.crafting.spinning.Spinning
import content.global.skill.smithing.smelting.Bar
import content.global.skill.summoning.SummoningPouch
import core.api.addItem
import core.api.quest.finishQuest
import core.api.sendMessage
import core.api.teleport
import core.game.node.item.Item
import core.game.system.command.Privilege
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Quests

@Initializable
class ItemKitsCommandSet : CommandSet(Privilege.ADMIN) {
    override fun defineCommands() {
        /*
         * Provides a set of dyes.
         */

        define(
            name = "dyeskit",
            privilege = Privilege.ADMIN,
            usage = "::dyeskit",
            description = "Provides a set of dyes.",
        ) { player, _ ->
            for (item in dyeKitItems) {
                player.inventory.add(Item(item))
            }
            addItem(player, Items.RED_CAPE_1007, 8)
            return@define
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

        /*
         * Provide all ingredients for the spinning wheel.
         */

        define(
            name = "spinningkit",
            privilege = Privilege.ADMIN,
            usage = "::spinningkit",
            description = "Provides all ingredients for spinning wheel, with product information.",
        ) { player, _ ->
            val spinningWheelLocation = Location.create(3209, 3213, 1)
            for (spinning in Spinning.values()) {
                val needMessage = "Item needed: ${spinning.need} -> Reward: ${spinning.product}"
                sendMessage(player, needMessage)
            }
            player.inventory.addList(Spinning.getAllNeed())
            player.inventory.addList(Spinning.getAllProduct())
            teleport(player, spinningWheelLocation)
            return@define
        }

        /*
         * Provides all bars.
         */

        define(
            name = "barkit",
            privilege = Privilege.ADMIN,
            usage = "::barkit",
            description = "Provides all bars and hammer and teleport to anvil.",
        ) { player, _ ->
            val anvilLocation = Location.create(2919, 3573, 0)
            player.bank.addList(Bar.getAllBars())
            addItem(player, Items.HAMMER_2347)
            teleport(player, anvilLocation)
            return@define
        }

        /*
         * Provides all ores.
         */

        define(
            name = "orekit",
            privilege = Privilege.ADMIN,
            usage = "::orekit",
            description = "Provides all ores to the bank and teleports to the furnace.",
        ) { player, _ ->
            val furnaceLocation = Location.create(3227, 3255, 0)
            player.bank.addList(Bar.getAllOres())
            teleport(player, furnaceLocation)
            return@define
        }

        /*
         * Provides all summoning pouches.
         */

        define(
            name = "summoningkit",
            privilege = Privilege.ADMIN,
            usage = "::summoningkit",
            description = "Provides all pouches to the bank and teleports to the obelisk.",
        ) { player, _ ->
            val obeliskLocation = Location.create(2208, 5344, 0)
            finishQuest(player, Quests.WOLF_WHISTLE)
            player.bank.addList(SummoningPouch.getAllPouchItems())
            teleport(player, obeliskLocation)
            return@define
        }

        /*
         * Provides all required items to making a tea.
         */

        define(
            name = "teakit",
            privilege = Privilege.ADMIN,
            usage = "::teakit",
            description = "Stuff to making cup of tea.",
        ) { player, _ ->

            val itemsToAdd =
                listOf(
                    Items.TEA_LEAVES_7738 to 3,
                    Items.HOT_KETTLE_7691 to 1,
                    Items.TEAPOT_7702 to 1,
                    Items.TEAPOT_7714 to 1,
                    Items.TEAPOT_7726 to 1,
                    Items.HOT_KETTLE_7691 to 1,
                    Items.TEAPOT_WITH_LEAVES_7700 to 1,
                    Items.TEAPOT_WITH_LEAVES_7712 to 1,
                    Items.TEAPOT_WITH_LEAVES_7724 to 1,
                    Items.HOT_KETTLE_7691 to 1,
                    Items.BUCKET_OF_MILK_1927 to 3,
                    Items.EMPTY_CUP_7728 to 1,
                    Items.CUP_OF_TEA_7730 to 1,
                    Items.CUP_OF_TEA_7733 to 2,
                    Items.PORCELAIN_CUP_7732 to 1,
                    Items.CUP_OF_TEA_7736 to 2,
                    Items.PORCELAIN_CUP_7735 to 1,
                )

            itemsToAdd.forEach { (item, amount) ->
                addItem(player, item, amount)
            }
        }
    }

    companion object {
        private val farmKitItems =
            arrayListOf(
                Items.RAKE_5341,
                Items.SPADE_952,
                Items.SEED_DIBBER_5343,
                Items.WATERING_CAN8_5340,
                Items.SECATEURS_5329,
                Items.GARDENING_TROWEL_5325,
            )
        private val runeKitItems =
            arrayListOf(
                Items.AIR_RUNE_556,
                Items.EARTH_RUNE_557,
                Items.FIRE_RUNE_554,
                Items.WATER_RUNE_555,
                Items.MIND_RUNE_558,
                Items.BODY_RUNE_559,
                Items.DEATH_RUNE_560,
                Items.NATURE_RUNE_561,
                Items.CHAOS_RUNE_562,
                Items.LAW_RUNE_563,
                Items.COSMIC_RUNE_564,
                Items.BLOOD_RUNE_565,
                Items.SOUL_RUNE_566,
                Items.ASTRAL_RUNE_9075,
            )
        private val talismanKitItems =
            arrayListOf(
                Items.AIR_TALISMAN_1438,
                Items.EARTH_TALISMAN_1440,
                Items.FIRE_TALISMAN_1442,
                Items.WATER_TALISMAN_1444,
                Items.MIND_TALISMAN_1448,
                Items.BODY_TALISMAN_1446,
                Items.DEATH_TALISMAN_1456,
                Items.NATURE_TALISMAN_1462,
                Items.CHAOS_TALISMAN_1452,
                Items.LAW_TALISMAN_1458,
                Items.COSMIC_TALISMAN_1454,
                Items.BLOOD_TALISMAN_1450,
                Items.SOUL_TALISMAN_1460,
            )
        private val dyeKitItems =
            arrayListOf(
                Items.RED_DYE_1763,
                Items.YELLOW_DYE_1765,
                Items.BLUE_DYE_1767,
                Items.ORANGE_DYE_1769,
                Items.GREEN_DYE_1771,
                Items.PURPLE_DYE_1773,
                Items.BLACK_MUSHROOM_INK_4622,
                Items.PINK_DYE_6955,
            )
    }
}
