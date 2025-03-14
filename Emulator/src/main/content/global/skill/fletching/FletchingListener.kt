package content.global.skill.fletching

import content.global.skill.fletching.items.arrow.*
import content.global.skill.fletching.items.bolts.*
import content.global.skill.fletching.items.bow.StringPulse
import content.global.skill.fletching.items.bow.Strings
import content.global.skill.fletching.items.crossbow.Limb
import content.global.skill.fletching.items.crossbow.LimbPulse
import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Items
import kotlin.math.min

class FletchingListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles attaching a string to an unstrung bow.
         */

        onUseWith(IntType.ITEM, Fletching.stringIds, *Fletching.unstrungBows) { player, string, bow ->
            val enum = Strings.product[bow.id] ?: return@onUseWith false
            if (enum.string != string.id) {
                sendMessage(player, "That's not the right kind of string for this.")
                return@onUseWith true
            }
            sendSkillDialogue(player) {
                withItems(enum.product)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = StringPulse(player, Item(string.id), enum, amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, string.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching feathers to arrow shafts to create headless arrows.
         */

        onUseWith(IntType.ITEM, Fletching.arrowShaftId, *Fletching.featherIds) { player, shaft, feather ->
            sendSkillDialogue(player) {
                withItems(Fletching.fletchedShaftId)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = HeadlessArrowPulse(player, Item(shaft.id), Item(feather.id), amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, shaft.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching arrowheads to headless arrows to create arrows.
         */

        onUseWith(IntType.ITEM, Fletching.fletchedShaftId, *Fletching.unfinishedArrows) { player, shaft, unfinished ->
            val arrowHead = ArrowHead.getByUnfinishedId(unfinished.id) ?: return@onUseWith false
            sendSkillDialogue(player) {
                withItems(arrowHead.finished)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = ArrowHeadPulse(player, Item(shaft.id), arrowHead, amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, shaft.id)
                }
            }

            return@onUseWith true
        }

        /*
         * Handles attaching wolfbone arrow tips to flighted ogre arrows to create ogre arrows.
         */

        onUseWith(IntType.ITEM, Items.WOLFBONE_ARROWTIPS_2861, Items.FLIGHTED_OGRE_ARROW_2865) { player, used, with ->
            fun getMaxAmount(_unused: Int = 0): Int {
                val tips = amountInInventory(player, Items.WOLFBONE_ARROWTIPS_2861)
                val shafts = amountInInventory(player, Items.FLIGHTED_OGRE_ARROW_2865)
                return min(tips, shafts)
            }

            fun process() {
                val amountThisIter = min(6, getMaxAmount())
                if (removeItem(player, Item(used.id, amountThisIter)) &&
                    removeItem(
                        player,
                        Item(with.id, amountThisIter),
                    )
                ) {
                    addItem(player, Items.OGRE_ARROW_2866, amountThisIter)
                    rewardXP(player, Skills.FLETCHING, 6.0)
                    sendMessage(player, "You make $amountThisIter ogre arrows.")
                }
            }

            sendSkillDialogue(player) {
                create { _, amount ->
                    runTask(player, delay = 2, repeatTimes = min(amount, getMaxAmount() / 6 + 1), task = ::process)
                }
                calculateMaxAmount(::getMaxAmount)
                withItems(Item(Items.OGRE_ARROW_2866, 5))
            }
            return@onUseWith true
        }

        /*
         * Handles creating mithril grapple base by attaching mithril bolts to grapple tips.
         */

        onUseWith(IntType.ITEM, Items.MITHRIL_BOLTS_9142, Items.MITH_GRAPPLE_TIP_9416) { player, used, tip ->
            if (getStatLevel(player, Skills.FLETCHING) < 59) {
                sendMessage(player, "You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if (removeItem(player, used.asItem()) && removeItem(player, tip.asItem())) {
                addItem(player, Items.MITH_GRAPPLE_9418)
                sendMessage(player, "You attach the grapple tip to the bolt.")
            } else {
                sendMessage(player, "You don't have the required items.")
            }
            return@onUseWith true
        }

        /*
         * Handles attaching a rope to a mithril grapple base to create a mithril grapple.
         */

        onUseWith(IntType.ITEM, Items.ROPE_954, Items.MITH_GRAPPLE_9418) { player, rope, grapple ->
            if (getStatLevel(player, Skills.FLETCHING) < 59) {
                sendMessage(player, "You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if (removeItem(player, rope.asItem()) && removeItem(player, grapple.asItem())) {
                addItem(player, Items.MITH_GRAPPLE_9419)
                sendMessage(player, "You tie the rope to the grapple.")
            } else {
                sendMessage(player, "You don't have the required items.")
            }
            return@onUseWith true
        }

        /*
         * Handles attaching a crossbow limb to a stock to create an unstrung crossbow.
         */

        onUseWith(IntType.ITEM, Fletching.limbIds, *Fletching.stockIds) { player, limb, stock ->
            val limbEnum = Limb.product[stock.id] ?: return@onUseWith false
            if (limbEnum.limb != limb.id) {
                sendMessage(player, "That's not the right limb to attach to that stock.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(limbEnum.product)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = LimbPulse(player, Item(stock.id), limbEnum, amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, limb.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles chiseling gems into bolt tips.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *Fletching.gemIds) { player, used, with ->
            val gem = GemBolt.product[with.id] ?: return@onUseWith true
            sendSkillDialogue(player) {
                withItems(gem.gem)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = GemBoltCutPulse(player, Item(used.id), gem, amount),
                    )
                }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching gem bolt tips to bolt bases to create gem-tipped bolts.
         */

        onUseWith(IntType.ITEM, Fletching.boltBaseIds, *Fletching.boltTipIds) { player, used, with ->
            val bolt = GemBolt.forId(with.id) ?: return@onUseWith true
            if (used.id != bolt.base || with.id != bolt.tip) return@onUseWith true
            val handler: SkillDialogueHandler =
                object : SkillDialogueHandler(player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(bolt.product)) {
                    override fun create(
                        amount: Int,
                        index: Int,
                    ) {
                        player.pulseManager.run(
                            GemBoltPulse(player, GemBolt.forId(used.id)?.base?.asItem(), bolt, amount),
                        )
                    }

                    override fun getAll(index: Int): Int {
                        return min(amountInInventory(player, used.id), amountInInventory(player, with.id))
                    }
                }
            handler.open()
            return@onUseWith true
        }

        /*
         * Handles attaching kebbit spikes to create kebbit bolts.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *Fletching.kebbitSpikeIds) { player, _, base ->
            sendSkillDialogue(player) {
                withItems(base.id)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = KebbitBoltPulse(player, Item(base.id), KebbitBolt.forId(base.asItem())!!, amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, base.id)
                }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching the barb bolt tips with bronze bolts to create barbed bolts.
         */

        onUseWith(IntType.ITEM, Items.BARB_BOLTTIPS_47, Items.BRONZE_BOLTS_877) { player, used, with ->
            if (getStatLevel(player, Skills.FLETCHING) < 51) {
                sendMessage(player, "You need a fletching level of 51 to do this.")
                return@onUseWith true
            }
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }
            if (!inInventory(player, used.id) || !inInventory(player, with.id)) {
                sendDialogue(player, "You don't have required items in your inventory.")
                return@onUseWith true
            }

            fun getMaxAmount(_unused: Int = 0): Int {
                val tips = amountInInventory(player, used.id)
                val bolts = amountInInventory(player, with.id)
                return min(tips, bolts)
            }

            fun process() {
                val amount = min(10, getMaxAmount())
                if (removeItem(player, Item(used.id, amount)) && removeItem(player, Item(with.id, amount))) {
                    addItem(player, Items.BARBED_BOLTS_881, amount)
                    rewardXP(player, Skills.FLETCHING, 9.5)
                    sendMessage(player, "You attach $amount barbed tips to the bronze bolts.")
                }
            }

            sendSkillDialogue(player) {
                create { _, amount ->
                    runTask(player, delay = 2, repeatTimes = min(amount, getMaxAmount() / 6 + 1), task = ::process)
                }
                calculateMaxAmount(::getMaxAmount)
                withItems(Item(Items.BARBED_BOLTS_881, 10))
            }
            return@onUseWith true
        }

        /*
         * Handles attaching the ogre arrow shafts and feathers to create flighted ogre arrows.
         */

        onUseWith(IntType.ITEM, Items.OGRE_ARROW_SHAFT_2864, *Fletching.featherIds) { player, used, with ->
            if (getStatLevel(player, Skills.FLETCHING) < 5) {
                sendDialogue(player, "You need a fletching level of 5 to do this.")
                return@onUseWith true
            }
            if (freeSlots(player) == 0) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }

            fun getMaxAmount(_unused: Int = 0): Int {
                val tips = amountInInventory(player, Items.OGRE_ARROW_SHAFT_2864)
                val feathers = Fletching.featherIds.sumOf { amountInInventory(player, it) }
                return min(tips, feathers)
            }

            fun process() {
                val amount = min(4, getMaxAmount())
                if (removeItem(player, Item(used.id, amount)) && removeItem(player, Item(with.id, amount))) {
                    addItem(player, Fletching.fligtedOgreArrowId, amount)
                    rewardXP(player, Skills.FLETCHING, 5.4)
                    sendMessage(player, "You attach $amount feathers to the ogre arrow shafts.")
                }
            }

            sendSkillDialogue(player) {
                create { _, amount ->
                    runTask(player, delay = 2, repeatTimes = min(amount, getMaxAmount() / 6 + 1), task = ::process)
                }
                calculateMaxAmount(::getMaxAmount)
                withItems(Item(Fletching.fligtedOgreArrowId, 4))
            }
            return@onUseWith true
        }

        /*
         * Handles attaching nails to arrow shafts to create brutal arrows.
         */

        onUseWith(IntType.ITEM, Fletching.fligtedOgreArrowId, *Fletching.nailIds) { player, used, with ->
            val brutalArrow = BrutalArrow.product[with.id] ?: return@onUseWith false
            sendSkillDialogue(player) {
                withItems(brutalArrow.product)
                create { _, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = BrutalArrowPulse(player, Item(used.id), brutalArrow, amount),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, with.id)
                }
            }
            return@onUseWith true
        }
    }
}
