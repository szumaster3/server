package content.activity.blastfurnace

import content.global.skill.smithing.Bar
import content.minigame.blastfurnace.plugin.BFOreContainer
import content.minigame.blastfurnace.plugin.BlastUtils
import core.api.getVarbit
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import shared.consts.Items

class BlastFurnaceOreContainerTests {

    @Test
    fun shouldBeAbleToAddCoal() {
        TestUtils.getMockPlayer("bf-add-coal").use { p ->
            val cont = BFOreContainer()
            cont.addCoal(p, 15)
            Assertions.assertEquals(15, cont.coalAmount(p))
        }
    }

    @Test
    fun addCoalShouldReturnExtraAmountIfAddingMoreThanPossible() {
        TestUtils.getMockPlayer("bf-coal-limit").use { p ->
            val cont = BFOreContainer()
            val limit = getVarbit(p, BlastUtils.COAL_LIMIT)

            cont.addCoal(p, limit - 26)
            Assertions.assertEquals(2, cont.addCoal(p, 28))
        }
    }

    @Test
    fun shouldBeAbleToAddOres() {
        TestUtils.getMockPlayer("bf-add-ores").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, 20)
            Assertions.assertEquals(20, cont.getOreAmount(p, Items.IRON_ORE_440))
        }
    }

    @Test
    fun addOreShouldReturnExtraAmountIfAddingMoreThanPossible() {
        TestUtils.getMockPlayer("bf-ore-limit").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, BlastUtils.ORE_LIMIT - 2)
            Assertions.assertEquals(3, cont.addOre(p, Items.IRON_ORE_440, 5))
        }
    }

    @Test
    fun addOreShouldReturnExtraAmountWhenAddingMoreCopperOrTinThanPossible() {
        TestUtils.getMockPlayer("bf-tin-copper-limit").use { p ->
            val contTin = BFOreContainer()
            Assertions.assertEquals(28, contTin.addOre(p, Items.TIN_ORE_438, 56))

            val contCopper = BFOreContainer()
            Assertions.assertEquals(28, contCopper.addOre(p, Items.COPPER_ORE_436, 56))
        }
    }

    @Test
    fun convertToBarsShouldYieldExpectedResults() {
        data class Data(
            val coalAmount: Int,
            val oreAmount: Array<Pair<Int, Int>>,
            val expectedOreAmounts: Array<Pair<Int, Int>>,
            val expectedCoalAmount: Int,
            val expectedBarResult: Array<Pair<Bar, Int>>,
        )

        val testData =
            arrayOf(
                Data(
                    0,
                    arrayOf(Pair(Items.IRON_ORE_440, 20)),
                    arrayOf(Pair(Items.IRON_ORE_440, 0)),
                    0,
                    arrayOf(Pair(Bar.IRON, 20)),
                ),
                Data(
                    10,
                    arrayOf(Pair(Items.IRON_ORE_440, 20)),
                    arrayOf(Pair(Items.IRON_ORE_440, 0)),
                    0,
                    arrayOf(Pair(Bar.STEEL, 10), Pair(Bar.IRON, 10)),
                ),
                Data(
                    20,
                    arrayOf(Pair(Items.MITHRIL_ORE_447, 10)),
                    arrayOf(Pair(Items.MITHRIL_ORE_447, 0)),
                    0,
                    arrayOf(Pair(Bar.MITHRIL, 10)),
                ),
                Data(
                    30,
                    arrayOf(Pair(Items.ADAMANTITE_ORE_449, 10)),
                    arrayOf(Pair(Items.ADAMANTITE_ORE_449, 0)),
                    0,
                    arrayOf(Pair(Bar.ADAMANT, 10)),
                ),
                Data(
                    40,
                    arrayOf(Pair(Items.RUNITE_ORE_451, 10)),
                    arrayOf(Pair(Items.RUNITE_ORE_451, 0)),
                    0,
                    arrayOf(Pair(Bar.RUNITE, 10)),
                ),
                Data(
                    150,
                    arrayOf(Pair(Items.GOLD_ORE_444, 28)),
                    arrayOf(Pair(Items.GOLD_ORE_444, 0)),
                    150,
                    arrayOf(Pair(Bar.GOLD, 28)),
                ),
                Data(
                    150,
                    arrayOf(Pair(Items.PERFECT_GOLD_ORE_446, 28)),
                    arrayOf(Pair(Items.PERFECT_GOLD_ORE_446, 0)),
                    150,
                    arrayOf(Pair(Bar.PERFECT_GOLD, 28)),
                ),
                Data(
                    58,
                    arrayOf(Pair(Items.SILVER_ORE_442, 18), Pair(Items.RUNITE_ORE_451, 10)),
                    arrayOf(Pair(Items.SILVER_ORE_442, 0), Pair(Items.RUNITE_ORE_451, 0)),
                    18,
                    arrayOf(Pair(Bar.SILVER, 18), Pair(Bar.RUNITE, 10)),
                ),
                Data(
                    20,
                    arrayOf(Pair(Items.RUNITE_ORE_451, 10)),
                    arrayOf(Pair(Items.RUNITE_ORE_451, 5)),
                    0,
                    arrayOf(Pair(Bar.RUNITE, 5)),
                ),
                Data(
                    0,
                    arrayOf(Pair(Items.COPPER_ORE_436, 28), Pair(Items.TIN_ORE_438, 28)),
                    arrayOf(Pair(Items.COPPER_ORE_436, 0), Pair(Items.TIN_ORE_438, 0)),
                    0,
                    arrayOf(Pair(Bar.BRONZE, 28)),
                ),
                Data(
                    0,
                    arrayOf(Pair(Items.COPPER_ORE_436, 10)),
                    arrayOf(Pair(Items.COPPER_ORE_436, 10)),
                    0,
                    arrayOf(Pair(Bar.BRONZE, 0)),
                ),
                Data(
                    0,
                    arrayOf(Pair(Items.COPPER_ORE_436, 14), Pair(Items.TIN_ORE_438, 5)),
                    arrayOf(Pair(Items.COPPER_ORE_436, 9), Pair(Items.TIN_ORE_438, 0)),
                    0,
                    arrayOf(Pair(Bar.BRONZE, 5)),
                ),
            )

        var index = 0
        TestUtils.getMockPlayer("bf-convert-bars").use { p ->
            for ((initialCoal, initialOres, expectedOres, expectedCoal, expectedBars) in testData) {
                val cont = BFOreContainer()
                cont.addCoal(p, initialCoal)
                for ((ore, amount) in initialOres) cont.addOre(p, ore, amount)
                cont.convertToBars(p)

                for ((ore, amount) in expectedOres) {
                    Assertions.assertEquals(amount, cont.getOreAmount(p,ore), "Problem testcase was $index - Missing $ore")
                }
                for ((bar, amount) in expectedBars) {
                    Assertions.assertEquals(amount, cont.getBarAmount(bar), "Problem testcase was $index - Missing ${bar.name}")
                }
                Assertions.assertEquals(expectedCoal, cont.coalAmount(p), "Problem testcase was $index")
                index++
            }
        }
    }

    @Test
    fun convertToBarsShouldNotConsumeMaterialsForAlreadyFilledBarType() {
        TestUtils.getMockPlayer("bf-convert-double").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, 28)
            cont.convertToBars(p)
            Assertions.assertEquals(28, cont.getBarAmount(Bar.IRON))

            cont.addOre(p, Items.IRON_ORE_440, 28)
            cont.convertToBars(p)
            Assertions.assertEquals(28, cont.getBarAmount(Bar.IRON))
            Assertions.assertEquals(28, cont.getOreAmount(p,Items.IRON_ORE_440))
        }
    }

    @Test
    fun oreContainerShouldCleanlySerializeAndDeserializeFromJson() {
        TestUtils.getMockPlayer("bf-json").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, 28)
            cont.convertToBars(p)

            cont.addOre(p, Items.RUNITE_ORE_451, 15)
            cont.addOre(p, Items.MITHRIL_ORE_447, 13)
            cont.addCoal(p, 150)

            val json = cont.toJson()
            val deserialized = BFOreContainer.fromJson(json)

            Assertions.assertEquals(28, deserialized.getBarAmount(Bar.IRON))
            Assertions.assertEquals(15, cont.getOreAmount(p,Items.RUNITE_ORE_451))
            Assertions.assertEquals(13, cont.getOreAmount(p,Items.MITHRIL_ORE_447))
            Assertions.assertEquals(150, cont.coalAmount(p))
        }
    }

    @Test
    fun shouldBeAbleToRemoveBars() {
        TestUtils.getMockPlayer("bf-remove-bars").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, 28)
            cont.convertToBars(p)

            val bars = cont.takeBars(Bar.IRON, 15)
            Assertions.assertEquals(15, bars?.amount)
            Assertions.assertEquals(13, cont.getBarAmount(Bar.IRON))
        }
    }

    @Test
    fun shouldNotBeAbleToRemoveMoreBarsThanPossible() {
        TestUtils.getMockPlayer("bf-remove-too-many").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, 28)
            cont.convertToBars(p)

            val bars = cont.takeBars(Bar.IRON, 50)
            Assertions.assertEquals(28, bars?.amount)
            Assertions.assertEquals(0, cont.getBarAmount(Bar.IRON))
        }
    }

    @Test
    fun convertToBarsShouldReturnXPReward() {
        TestUtils.getMockPlayer("bf-xp-reward").use { p ->
            val cont = BFOreContainer()
            cont.addOre(p, Items.IRON_ORE_440, 28)

            Assertions.assertEquals(350.0, cont.convertToBars(p))
        }
    }

    @Test
    fun removingBarsWithNoStockReturnsNull() {
        val cont = BFOreContainer()
        Assertions.assertEquals(null, cont.takeBars(Bar.RUNITE, 1))
    }
}
