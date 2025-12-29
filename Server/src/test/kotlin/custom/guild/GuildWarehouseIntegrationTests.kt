package custom.guild.warehouse

import core.game.node.item.Item
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import shared.consts.Items
import custom.guild.Guild
import custom.guild.GuildRank
import custom.guild.warehouse.slot.WarehouseSlotPermission

class GuildWarehouseFullTests {

    private val testPlayer = TestUtils.getMockPlayer("testPlayer")
    private val testPlayer2 = TestUtils.getMockPlayer("testPlayer2")

    private fun createGuild(): Guild {
        return Guild(
            id = 1,
            level = 1,
            gold = 1000,
            members = mutableMapOf(
                testPlayer.details.uid to GuildRank.GUILDMASTER,
                testPlayer2.details.uid to GuildRank.MEMBER
            )
        )
    }

    @Test
    fun `deposit stackable item works`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        val item = Item(Items.IRON_ORE_440, 50)

        val remainder = warehouse.deposit(testPlayer.name, item)
        assertEquals(0, remainder)
        assertEquals(50, warehouse.allItems().first().amount)
    }

    @Test
    fun `deposit returns remainder when full`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        val maxSlots = warehouse.freeSlots()
        for (i in 0 until maxSlots) warehouse.deposit(testPlayer.name, Item(Items.COAL_453, 1))

        val remainder = warehouse.deposit(testPlayer.name, Item(Items.IRON_ORE_440, 10))
        assertEquals(10, remainder)
    }

    @Test
    fun `withdraw respects GuildRank permissions`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        warehouse.deposit(testPlayer.name, Item(Items.IRON_ORE_440, 10))

        // testPlayer2 is MEMBER → cannot withdraw
        val withdrawn = warehouse.withdraw(testPlayer2.name, GuildRank.MEMBER, Items.IRON_ORE_440, 5)
        assertNull(withdrawn)

        // testPlayer (GUILDMASTER) → can withdraw
        val withdrawn2 = warehouse.withdraw(testPlayer.name, GuildRank.GUILDMASTER, Items.IRON_ORE_440, 5)
        assertNotNull(withdrawn2)
        assertEquals(5, withdrawn2!!.amount)
    }

    @Test
    fun `locked slot prevents deposit`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        warehouse.setSlotLocked(testPlayer.name, 0, true)

        val remainder = warehouse.deposit(testPlayer.name, Item(Items.IRON_ORE_440, 10))
        assertEquals(10, remainder)
    }

    @Test
    fun `setting slot permission costs guild gold`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        val initialGold = guild.gold

        val success = warehouse.setSlotPermission(testPlayer.name, 0, WarehouseSlotPermission.WITHDRAW_ONLY)
        assertTrue(success)
        assertTrue(guild.gold < initialGold)
    }

    @Test
    fun `expand increases warehouse capacity`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        val oldSlots = warehouse.freeSlots()
        warehouse.expand()
        assertTrue(warehouse.freeSlots() > oldSlots)
    }

    @Test
    fun `serialization preserves warehouse state`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        warehouse.deposit(testPlayer.name, Item(Items.IRON_ORE_440, 10))
        warehouse.setSlotLocked(testPlayer.name, 0, true)
        warehouse.setSlotPermission(testPlayer.name, 0, WarehouseSlotPermission.WITHDRAW_ONLY)

        val json = warehouse.toJson()
        val loaded = GuildWarehouse.fromJson(guild, json)

        assertEquals(warehouse.allItems().size, loaded.allItems().size)
        assertEquals(warehouse.freeSlots(), loaded.freeSlots())
    }

    @Test
    fun `multi-player deposits and withdrawals`() {
        val guild = createGuild()
        val warehouse = GuildWarehouse(guild)
        warehouse.deposit(testPlayer.name, Item(Items.COAL_453, 20))
        warehouse.deposit(testPlayer2.name, Item(Items.IRON_ORE_440, 10))

        val withdrawn = warehouse.withdraw(testPlayer.name, GuildRank.GUILDMASTER, Items.COAL_453, 10)
        assertEquals(10, withdrawn!!.amount)
        val withdrawn2 = warehouse.withdraw(testPlayer.name, GuildRank.GUILDMASTER, Items.IRON_ORE_440, 5)
        assertEquals(5, withdrawn2!!.amount)
    }
}
