package custom.guild.warehouse.slot

import custom.guild.GuildRank
import core.game.node.item.Item
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import TestUtils
import shared.consts.Items

class GuildWarehouseSlotTests {

    @Test
    fun testDepositAndWithdrawIntegration() {
        TestUtils.getMockPlayer("Depositor").use { depositor ->
            TestUtils.getMockPlayer("Withdrawer").use { withdrawer ->

                val testItem = Item(Items.IRON_LONGSWORD_1293, 100)

                // Deposit.
                for (slot in WarehouseSlotPermission.values())
                {
                    for (rank in GuildRank.values())
                    {
                        val canDeposit = slot.canDeposit(rank)
                        if (canDeposit)
                        {
                            slot.addItemToSlot(testItem.copy(), rank)
                        }

                        assertEquals(true, canDeposit, "Rank $rank should be able to deposit into $slot")
                    }
                }

                // Withdraw.
                for (slot in WarehouseSlotPermission.values())
                {
                    for (rank in GuildRank.values())
                    {
                        val canWithdraw = slot.canWithdraw(rank)
                        val itemBefore = slot.getItemCount()
                        val withdrawn = slot.withdrawItem(testItem.copy(), rank)
                        val itemAfter = slot.getItemCount()

                        if (canWithdraw)
                        {
                            assertEquals(itemBefore - 100, itemAfter, "Rank $rank should withdraw successfully from $slot")
                            assertEquals(100, withdrawn?.amount ?: 0)
                        }
                        else
                        {
                            assertEquals(itemBefore, itemAfter, "Rank $rank should NOT withdraw from $slot")
                            assertEquals(null, withdrawn)
                        }
                    }
                }
            }
        }
    }
}