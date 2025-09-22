package content.activity.blastfurnace

import TestUtils
import content.global.skill.smithing.Bar
import content.minigame.blastfurnace.plugin.BFPlayerState
import content.minigame.blastfurnace.plugin.BlastFurnace
import core.api.addItem
import core.api.amountInInventory
import core.api.setVarbit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import shared.consts.Items

class BFPlayerStateTests {
    init {
        TestUtils.preTestSetup()
    }

    @Test
    fun processPerfectGoldOre() {
        TestUtils.getMockPlayer("bf-perfectgold").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            setVarbit(p, BFPlayerState.DISPENSER_STATE, 0, true)

            state.container.addOre(Items.PERFECT_GOLD_ORE_446, 5)
            state.container.addCoal(10)

            Assertions.assertTrue(state.processOresIntoBars())
            Assertions.assertEquals(5, state.container.getBarAmount(Bar.PERFECT_GOLD))

            Assertions.assertFalse(state.processOresIntoBars())
            Assertions.assertEquals(5, state.container.getBarAmount(Bar.PERFECT_GOLD))

            state.coolBars()
            state.container.addOre(Items.PERFECT_GOLD_ORE_446, 3)

            Assertions.assertTrue(state.processOresIntoBars())
            Assertions.assertEquals(8, state.container.getBarAmount(Bar.PERFECT_GOLD))

            state.coolBars()
            Assertions.assertTrue(state.claimBars(Bar.PERFECT_GOLD, 3))
            Assertions.assertEquals(3, amountInInventory(p, Items.PERFECT_GOLD_BAR_2365))
            Assertions.assertEquals(5, state.container.getBarAmount(Bar.PERFECT_GOLD))
        }
    }

    @Test fun processOreIntoBarsShouldDoNothingIfBarsNotCooled() {
        TestUtils.getMockPlayer("bf-barsnotcooled").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            Assertions.assertEquals(28, state.container.getBarAmount(Bar.IRON))

            state.container.addCoal(40)
            state.container.addOre(Items.RUNITE_ORE_451, 10)
            Assertions.assertEquals(false, state.processOresIntoBars())

            Assertions.assertEquals(0, state.container.getBarAmount(Bar.RUNITE))
        }
    }

    @Test fun processOreIntoBarsShouldDoNothingIfBarsUnchecked() {
        TestUtils.getMockPlayer("bf-barsnotchecked").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            state.container.addCoal(40)
            state.container.addOre(Items.RUNITE_ORE_451, 10)
            Assertions.assertEquals(false, state.processOresIntoBars())
            Assertions.assertEquals(0, state.container.getBarAmount(Bar.RUNITE))

            state.checkBars()
            Assertions.assertEquals(true, state.processOresIntoBars())
            Assertions.assertEquals(10, state.container.getBarAmount(Bar.RUNITE))
        }
    }

    @Test fun shouldBeAbleToClaimBars() {
        TestUtils.getMockPlayer("bf-barclaiminig").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            Assertions.assertEquals(true, state.claimBars(Bar.IRON, 5))
            Assertions.assertEquals(5, amountInInventory(p, Items.IRON_BAR_2351))
            Assertions.assertEquals(23, state.container.getBarAmount(Bar.IRON))
        }
    }

    @Test fun shouldNotBeAbleToClaimMoreBarsThanFreeInventorySlots() {
        TestUtils.getMockPlayer("bf-claimbarslessslots").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            addItem(p, Items.ABYSSAL_WHIP_4151, 27)

            Assertions.assertEquals(true, state.claimBars(Bar.IRON, 5))
            Assertions.assertEquals(1, amountInInventory(p, Items.IRON_BAR_2351))
            Assertions.assertEquals(27, state.container.getBarAmount(Bar.IRON))
        }
    }

    @Test fun claimBarsShouldDoNothingAndGrantNoItemIfInventoryFull() {
        TestUtils.getMockPlayer("bf-claimbarsnoslots").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            state.coolBars()

            addItem(p, Items.ABYSSAL_WHIP_4151, 28)

            Assertions.assertEquals(false, state.claimBars(Bar.IRON, 5))
            Assertions.assertEquals(0, amountInInventory(p, Items.IRON_BAR_2351))
            Assertions.assertEquals(28, state.container.getBarAmount(Bar.IRON))
        }
    }

    @Test fun claimBarsShouldDoNothingIfBarsNotCooled() {
        TestUtils.getMockPlayer("bf-claimbarsnotcooled").use { p ->
            val state = BlastFurnace.getPlayerState(p)
            state.container.addOre(Items.IRON_ORE_440, 28)
            Assertions.assertEquals(true, state.processOresIntoBars())
            Assertions.assertEquals(false, state.claimBars(Bar.IRON, 5))
        }
    }
}
