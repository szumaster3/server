package content.global.skill.fletching

import core.api.*
import core.game.dialogue.SkillDialogueHandler
import core.game.dialogue.SkillDialogueHandler.SkillDialogue
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import content.global.skill.fletching.arrows.ArrowHead
import content.global.skill.fletching.arrows.BrutalArrow
import content.global.skill.fletching.bolts.GemBolt
import content.global.skill.fletching.bolts.KebbitBolt
import content.global.skill.fletching.bow.Strings
import content.global.skill.fletching.crossbow.Limb
import content.global.skill.slayer.SlayerManager.Companion.getInstance
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds
import core.tools.StringUtils
import core.tools.RandomFunction
import kotlin.math.min

class FletchingListener : InteractionListener {
    override fun defineListeners() {

        /*
         * Handles fletch logs using knife.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, *FLETCH_LOGS) { player, _, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val items = Fletching.getItems(with.id) ?: return@onUseWith true
            val dialogueType =
                when (items.size) {
                    2 -> SkillDialogue.TWO_OPTION
                    3 -> SkillDialogue.THREE_OPTION
                    4 -> SkillDialogue.FOUR_OPTION
                    else -> SkillDialogue.ONE_OPTION
                }

            val handler =
                object : SkillDialogueHandler(player, dialogueType, *items) {
                    override fun create(amount: Int, index: Int) {
                        val fletch = Fletching.getEntries(with.id)?.get(index) ?: return
                        var remaining = amount
                        queueScript(player, 0, QueueStrength.WEAK) {
                            if (remaining <= 0) return@queueScript stopExecuting(player)

                            if (getStatLevel(player, Skills.FLETCHING) < fletch.level) {
                                val name = getItemName(fletch.id).replace("(u)", "").trim()
                                sendDialogue(
                                    player,
                                    "You need a fletching skill of ${fletch.level} or above to make " +
                                            (if (StringUtils.isPlusN(name)) "an" else "a") +
                                            " $name"
                                )
                                return@queueScript stopExecuting(player)
                            }

                            if (amountInInventory(player, with.id) <= 0) {
                                sendMessage(player, "You have run out of logs.")
                                return@queueScript stopExecuting(player)
                            }

                            val animationId =
                                if (with.id == Items.MAGIC_LOGS_1513) Animation(Animations.CUT_MAGIC_LOGS_7211)
                                else Animation(Animations.FLETCH_LOGS_1248)
                            player.animate(animationId)

                            if (!player.inventory.remove(Item(with.id))) return@queueScript stopExecuting(player)

                            val item = Item(fletch.id, fletch.amount)
                            when (fletch.id) {
                                Items.OGRE_ARROW_SHAFT_2864 -> {
                                    val finalAmount = RandomFunction.random(2, 6)
                                    item.amount = finalAmount
                                    sendMessage(player, "You carefully cut the logs into $finalAmount arrow shafts.")
                                }
                                Items.COMP_OGRE_BOW_4827 -> {
                                    if (!player.inventory.contains(Items.WOLF_BONES_2859, 1))
                                        return@queueScript stopExecuting(player)
                                    player.inventory.remove(Item(Items.WOLF_BONES_2859))
                                    sendMessage(player, "You carefully cut the logs into composite ogre bow.")
                                }
                                else -> {
                                    val name = getItemName(fletch.id).replace("(u)", "").trim()
                                    sendMessage(
                                        player,
                                        "You carefully cut the logs into ${if (StringUtils.isPlusN(name)) "an" else "a"} $name."
                                    )
                                }
                            }

                            player.inventory.add(item)
                            player.skills.addExperience(Skills.FLETCHING, fletch.exp, true)

                            val bankZone = ZoneBorders(2721, 3493, 2730, 3487)
                            if (bankZone.insideBorder(player) && fletch.id == Items.MAGIC_SHORTBOW_U_72) {
                                finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 2, 2)
                            }

                            remaining--
                            if (remaining <= 0 || amountInInventory(player, with.id) <= 0)
                                return@queueScript stopExecuting(player)

                            delayScript(player, 2)
                        }
                    }

                    override fun getAll(index: Int): Int {
                        return amountInInventory(player, with.id)
                    }
                }

            if (items.size == 1) {
                handler.create(handler.getAll(0), 0)
            } else {
                handler.open()
            }

            return@onUseWith true
        }

        /*
         * Handles attaching a string to an unstrung bow.
         */

        onUseWith(IntType.ITEM, Fletching.stringIds, *Fletching.unstrungBows) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val enum = Strings.product[with.id] ?: return@onUseWith false
            if (enum.string != used.id) {
                sendMessage(player, "That's not the right kind of string for this.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(enum.product)

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0) return@queueScript stopExecuting(player)

                        if (getStatLevel(player, Skills.FLETCHING) < enum.level) {
                            sendDialogue(player, "You need a Fletching level of ${enum.level} to string this bow.")
                            return@queueScript stopExecuting(player)
                        }

                        if (!player.inventory.containsItem(Item(enum.unfinished))) {
                            sendDialogue(player, "You have run out of bows to string.")
                            return@queueScript stopExecuting(player)
                        }

                        if (!player.inventory.containsItem(Item(enum.string))) {
                            sendDialogue(player, "You seem to have run out of bow strings.")
                            return@queueScript stopExecuting(player)
                        }

                        player.animate(Animation.create(enum.animation))
                        playAudio(player, Sounds.STRING_BOW_2606)

                        if (player.inventory.remove(Item(enum.unfinished), Item(enum.string))) {
                            player.inventory.add(Item(enum.product))
                            player.getSkills().addExperience(Skills.FLETCHING, enum.experience, true)
                            player.packetDispatch.sendMessage("You add a string to the bow.")

                            // Diary check (Seers)
                            if (
                                enum == Strings.MAGIC_SHORTBOW &&
                                (ZoneBorders(2721, 3489, 2724, 3493, 0).insideBorder(player) ||
                                        ZoneBorders(2727, 3487, 2730, 3490, 0).insideBorder(player)) &&
                                player.getAttribute("diary:seers:fletch-magic-short-bow", false)
                            ) {
                                finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 2, 2)
                            }
                        }

                        remaining--
                        if (
                            remaining <= 0 ||
                            !player.inventory.containsItem(Item(enum.string)) ||
                            !player.inventory.containsItem(Item(enum.unfinished))
                        ) {
                            return@queueScript stopExecuting(player)
                        }

                        delayScript(player, 2)
                    }
                }

                calculateMaxAmount { amountInInventory(player, used.id) }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching feathers to arrow shafts to create headless arrows.
         */

        onUseWith(IntType.ITEM, Fletching.ARROW_SHAFT, *Fletching.featherIds) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val maxSets = min(amountInInventory(player, used.id), amountInInventory(player, with.id))
            if (maxSets <= 0) {
                sendDialogue(player, "You do not have enough materials to make headless arrows.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Item(Fletching.HEADLESS_ARROW))

                calculateMaxAmount { maxSets }

                create { _, amount ->
                    var remaining = amount

                    queueScript(player, 0, QueueStrength.WEAK) { stage ->
                        if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)

                        when (stage) {
                            0 -> {
                                delayClock(player, Clocks.SKILLING, 2)
                                delayScript(player, 2)
                            }
                            else -> {
                                val shaftAmount = player.inventory.getAmount(used.id)
                                val featherAmount = player.inventory.getAmount(with.id)
                                if (shaftAmount <= 0 || featherAmount <= 0) return@queueScript stopExecuting(player)

                                val batch = min(15, min(shaftAmount, featherAmount))

                                val shaftItem = Item(used.id, batch)
                                val featherItem = Item(with.id, batch)

                                if (player.inventory.remove(shaftItem, featherItem)) {
                                    val headless = Item(Fletching.HEADLESS_ARROW, batch)
                                    player.inventory.add(headless)
                                    player.getSkills().addExperience(Skills.FLETCHING, batch.toDouble(), true)

                                    val msg =
                                        if (batch == 1) "You attach a feather to a shaft."
                                        else "You attach feathers to $batch arrow shafts."
                                    sendMessage(player, msg)
                                }

                                remaining -= batch

                                if (remaining > 0) {
                                    setCurrentScriptState(player, 0)
                                    delayScript(player, 2)
                                } else {
                                    stopExecuting(player)
                                }
                            }
                        }
                    }
                }
            }

            return@onUseWith true
        }

        /*
         * Handles attaching arrowheads to headless arrows to create arrows.
         */

        onUseWith(IntType.ITEM, Fletching.HEADLESS_ARROW, *Fletching.unfinishedArrows) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val arrowHead = ArrowHead.getByUnfinishedId(with.id) ?: return@onUseWith false
            val maxSets = min(amountInInventory(player, used.id), amountInInventory(player, arrowHead.unfinished))
            if (arrowHead.unfinished == Items.BROAD_BOLTS_UNF_13279) {
                if (!getInstance(player).flags.isBroadsUnlocked()) {
                    sendDialogue(player, "You need to unlock the ability to create broad bolts.")
                    return@onUseWith true
                }
            }

            if (maxSets <= 0) {
                sendDialogue(player, "You do not have enough materials to make arrows.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Item(arrowHead.finished))

                calculateMaxAmount { maxSets }

                create { _, amount ->
                    var remaining = amount

                    queueScript(player, 0, QueueStrength.WEAK) { stage ->
                        if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)

                        when (stage) {
                            0 -> {
                                delayClock(player, Clocks.SKILLING, 2)
                                delayScript(player, 2)
                            }
                            else -> {
                                val tipAmount = player.inventory.getAmount(arrowHead.unfinished)
                                val shaftAmount = player.inventory.getAmount(Fletching.HEADLESS_ARROW)
                                if (tipAmount <= 0 || shaftAmount <= 0) return@queueScript stopExecuting(player)

                                val batch = min(15, min(tipAmount, shaftAmount))

                                val tip = Item(arrowHead.unfinished, batch)
                                val shaftBatch = Item(Fletching.HEADLESS_ARROW, batch)

                                if (player.inventory.remove(shaftBatch, tip)) {
                                    val product = Item(arrowHead.finished, batch)
                                    player.inventory.add(product)
                                    player.getSkills().addExperience(Skills.FLETCHING, arrowHead.experience * batch, true)

                                    val msg =
                                        if (batch == 1) "You attach an arrow head to an arrow shaft."
                                        else "You attach arrow heads to $batch arrow shafts."
                                    sendMessage(player, msg)
                                }

                                remaining -= batch

                                if (remaining > 0) {
                                    setCurrentScriptState(player, 0)
                                    delayScript(player, 2)
                                } else {
                                    stopExecuting(player)
                                }
                            }
                        }
                    }
                }
            }

            return@onUseWith true
        }

        /*
         * Handles attaching wolfbone arrow tips to flighted ogre arrows to create ogre arrows.
         */

        onUseWith(IntType.ITEM, Items.WOLFBONE_ARROWTIPS_2861, Items.FLIGHTED_OGRE_ARROW_2865) { player, _, _ ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            if (freeSlots(player) == 0) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }

            val maxAmount =
                min(
                    amountInInventory(player, Items.WOLFBONE_ARROWTIPS_2861),
                    amountInInventory(player, Items.FLIGHTED_OGRE_ARROW_2865)
                )

            if (maxAmount <= 0) {
                sendDialogue(player, "You do not have enough materials to make ogre arrows.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(Item(Items.OGRE_ARROW_2866, 5))

                calculateMaxAmount { maxAmount }

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) { stage ->
                        if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)

                        when (stage) {
                            0 -> {
                                delayClock(player, Clocks.SKILLING, 2)
                                delayScript(player, 2)
                            }
                            else -> {
                                val batch =
                                    min(6, min(
                                        amountInInventory(player, Items.WOLFBONE_ARROWTIPS_2861),
                                        amountInInventory(player, Items.FLIGHTED_OGRE_ARROW_2865)
                                    ))
                                if (batch <= 0) return@queueScript stopExecuting(player)

                                removeItem(player, Item(Items.WOLFBONE_ARROWTIPS_2861, batch))
                                removeItem(player, Item(Items.FLIGHTED_OGRE_ARROW_2865, batch))
                                addItem(player, Items.OGRE_ARROW_2866, batch)
                                rewardXP(player, Skills.FLETCHING, 6.0 * batch)
                                sendMessage(player, "You make $batch ogre arrows.")

                                remaining -= batch

                                if (remaining > 0) {
                                    setCurrentScriptState(player, 0)
                                    delayScript(player, 2)
                                } else {
                                    stopExecuting(player)
                                }
                            }
                        }
                    }
                }
            }

            return@onUseWith true
        }

        /*
         * Handles creating mithril grapple base by attaching mithril bolts to grapple tips.
         */

        onUseWith(IntType.ITEM, Items.MITHRIL_BOLTS_9142, Items.MITH_GRAPPLE_TIP_9416) { player, used, with ->
            if (getStatLevel(player, Skills.FLETCHING) < 59) {
                sendMessage(player, "You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.MITH_GRAPPLE_9418, 1)
                sendMessage(player, "You attach the grapple tip to the bolt.")
            } else {
                sendMessage(player, "You don't have the required items.")
            }
            return@onUseWith true
        }

        /*
         * Handles attaching a rope to a mithril grapple base to create a mithril grapple.
         */

        onUseWith(IntType.ITEM, Items.ROPE_954, Items.MITH_GRAPPLE_9418) { player, used, with ->
            if (getStatLevel(player, Skills.FLETCHING) < 59) {
                sendMessage(player, "You need a fletching level of 59 to make this.")
                return@onUseWith true
            }
            if (removeItem(player, used.asItem()) && removeItem(player, with.asItem())) {
                addItem(player, Items.MITH_GRAPPLE_9419, 1)
                sendMessage(player, "You tie the rope to the grapple.")
            } else {
                sendMessage(player, "You don't have the required items.")
            }
            return@onUseWith true
        }

        /*
         * Handles attaching a crossbow limb to a stock to create an unstrung crossbow.
         */

        onUseWith(IntType.ITEM, Fletching.limbIds, *Fletching.stockIds) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val limbEnum = Limb.product[with.id] ?: return@onUseWith true
            if (limbEnum.limb != used.id) {
                sendMessage(player, "That's not the right limb to attach to that stock.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(limbEnum.product)

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0) return@queueScript stopExecuting(player)

                        if (getStatLevel(player, Skills.FLETCHING) < limbEnum.level) {
                            sendDialogue(player, "You need a fletching level of ${limbEnum.level} to attach these limbs.")
                            return@queueScript stopExecuting(player)
                        }

                        if (
                            !player.inventory.containsItem(Item(limbEnum.limb)) ||
                            !player.inventory.containsItem(Item(limbEnum.stock))
                        ) {
                            sendMessage(player, "You do not have the required stock or limb.")
                            return@queueScript stopExecuting(player)
                        }

                        player.animate(Animation.create(limbEnum.animation))
                        playAudio(player, Sounds.STRING_CROSSBOW_2924)

                        if (player.inventory.remove(Item(limbEnum.stock), Item(limbEnum.limb))) {
                            player.inventory.add(Item(limbEnum.product))
                            player.skills.addExperience(Skills.FLETCHING, limbEnum.experience, true)
                            sendMessage(player, "You attach the metal limbs to the stock.")
                        }

                        remaining--
                        if (
                            remaining <= 0 ||
                            !player.inventory.containsItem(Item(limbEnum.limb)) ||
                            !player.inventory.containsItem(Item(limbEnum.stock))
                        )
                            return@queueScript stopExecuting(player)

                        delayScript(player, 2)
                    }
                }

                calculateMaxAmount { amountInInventory(player, used.id) }
            }

            return@onUseWith true
        }

        /*
         * Handles chiseling gems into bolt tips.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *Fletching.gemIds) { player, _, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val gem = GemBolt.gemToBolt[with.id] ?: return@onUseWith true
            val maxAmount = amountInInventory(player, with.id)
            if (maxAmount <= 0) {
                sendMessage(player, "You don't have any of that gem to cut.")
                return@onUseWith true
            }

            sendString(player, "How many gems would you like to cut into bolt tips?", Components.SKILL_MULTI1_309, 7)

            sendSkillDialogue(player) {
                withItems(Item(gem.tip))

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) { stage ->
                        if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(
                            player
                        )

                        when (stage) {
                            0 -> {
                                animate(player, gem.animation)
                                playAudio(player, Sounds.CHISEL_2586)
                                delayScript(player, 5)
                            }

                            else -> {
                                delayClock(player, Clocks.SKILLING, 5)
                                val rewardAmount =
                                    when (gem.gem) {
                                        Items.OYSTER_PEARLS_413,
                                        Items.ONYX_6573 -> 24
                                        Items.OYSTER_PEARL_411 -> 6
                                        else -> 12
                                    }

                                if (player.inventory.remove(Item(gem.gem))) {
                                    player.inventory.add(Item(gem.tip, rewardAmount))
                                    player.skills.addExperience(Skills.FLETCHING, gem.experience, true)
                                    sendMessage(player, "You use your chisel to fetch small bolt tips.")
                                    remaining--
                                }

                                if (remaining > 0) {
                                    setCurrentScriptState(player, 0)
                                    delayScript(player, 5)
                                } else {
                                    stopExecuting(player)
                                }
                            }
                        }
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching gem bolt tips to bolt bases to create gem-tipped bolts.
         */

        onUseWith(IntType.ITEM, Fletching.boltBaseIds, *Fletching.boltTipIds) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            val bolt = GemBolt.forId(with.id) ?: return@onUseWith true
            if (used.id != bolt.base || with.id != bolt.tip) return@onUseWith true

            sendSkillDialogue(player) {
                withItems(Item(bolt.product))

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0) return@queueScript stopExecuting(player)

                        if (getStatLevel(player, Skills.FLETCHING) < bolt.level) {
                            sendDialogue(player, "You need a Fletching level of ${bolt.level} or above to do that.")
                            return@queueScript stopExecuting(player)
                        }

                        val baseAmount = player.inventory.getAmount(bolt.base)
                        val tipAmount = player.inventory.getAmount(bolt.tip)
                        if (baseAmount <= 0 || tipAmount <= 0) {
                            val missing = if (baseAmount <= 0) bolt.base else bolt.tip
                            sendMessage(player, "You do not have any more $missing to fletch.")
                            return@queueScript stopExecuting(player)
                        }

                        val batchAmount = min(10, min(baseAmount, tipAmount))
                        val baseItem = Item(bolt.base, batchAmount)
                        val tipItem = Item(bolt.tip, batchAmount)
                        val productItem = Item(bolt.product, batchAmount)

                        if (player.inventory.remove(baseItem, tipItem)) {
                            player.inventory.add(productItem)
                            player.skills.addExperience(Skills.FLETCHING, bolt.experience * batchAmount, true)
                            sendMessage(
                                player,
                                if (batchAmount == 1) "You attach the tip to the bolt." else "You fletch $batchAmount bolts."
                            )
                        }

                        remaining--

                        if (remaining <= 0 ||
                            !player.inventory.containsItem(Item(bolt.base)) ||
                            !player.inventory.containsItem(Item(bolt.tip))
                        ) return@queueScript stopExecuting(player)

                        delayScript(player, 2)
                    }
                }

                calculateMaxAmount {
                    min(amountInInventory(player, used.id), amountInInventory(player, with.id))
                }
            }

            return@onUseWith true
        }

        /*
         * Handles attaching kebbit spikes to create kebbit bolts.
         */

        onUseWith(IntType.ITEM, Items.CHISEL_1755, *Fletching.kebbitSpikeIds) { player, _, base ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            val kebbit = KebbitBolt.forId(base.asItem()) ?: return@onUseWith true
            if (!hasSpaceFor(player, Item(kebbit.product))) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }
            if (!inInventory(player, kebbit.base)) {
                sendDialogue(player, "You don't have the required items in your inventory.")
                return@onUseWith true
            }

            var remaining = amountInInventory(player, base.id)

            sendSkillDialogue(player) {
                withItems(Item(kebbit.product))

                create { _, amount ->
                    var remaining = amount

                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0 || freeSlots(player) == 0) return@queueScript false

                        if (getStatLevel(player, Skills.FLETCHING) < kebbit.level) {
                            sendDialogue(player, "You need a Fletching level of ${kebbit.level} to do this.")
                            return@queueScript stopExecuting(player)
                        }

                        animate(player, Animations.FLETCH_LOGS_4433)
                        val baseItem = Item(kebbit.base, 1)
                        if (removeItem(player, baseItem)) {
                            addItem(player, kebbit.product, 6)
                            rewardXP(player, Skills.FLETCHING, kebbit.experience)
                            sendMessage(player, "You fletch 6 ${getItemName(kebbit.product).lowercase()}s.")
                        }

                        remaining--
                        delayScript(player, 2)
                        true
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles attaching the barb bolt tips with bronze bolts to create barbed bolts.
         */

        onUseWith(IntType.ITEM, Items.BARB_BOLTTIPS_47, Items.BRONZE_BOLTS_877) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            fun getMaxAmount(): Int {
                val tips = amountInInventory(player, used.id)
                val bolts = amountInInventory(player, with.id)
                return min(tips, bolts)
            }

            sendSkillDialogue(player) {
                withItems(Items.BARBED_BOLTS_881)

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0 || freeSlots(player) == 0) return@queueScript false
                        if (getStatLevel(player, Skills.FLETCHING) < 51) {
                            sendMessage(player, "You need a fletching level of 51 to do this.")
                            return@queueScript stopExecuting(player)
                        }
                        if (freeSlots(player) == 0) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return@queueScript stopExecuting(player)
                        }
                        if (!inInventory(player, used.id) || !inInventory(player, with.id)) {
                            sendDialogue(player, "You don't have required items in your inventory.")
                            return@queueScript stopExecuting(player)
                        }

                        val currentAmount = min(10, getMaxAmount())
                        if (currentAmount <= 0) return@queueScript false

                        if (
                            removeItem(player, Item(used.id, currentAmount)) &&
                            removeItem(player, Item(with.id, currentAmount))
                        ) {
                            addItem(player, Items.BARBED_BOLTS_881, currentAmount)
                            rewardXP(player, Skills.FLETCHING, 9.5)
                            sendMessage(player, "You attach $currentAmount barbed tips to the bronze bolts.")
                        }

                        remaining--
                        if (remaining > 0) {
                            delayScript(player, 2)
                            true
                        } else {
                            false
                        }
                    }
                }

                calculateMaxAmount { getMaxAmount() }
            }

            return@onUseWith true
        }

        /*
         * Handles attaching the ogre arrow shafts and feathers to create flighted ogre arrows.
         */

        onUseWith(IntType.ITEM, Items.OGRE_ARROW_SHAFT_2864, *Fletching.featherIds) { player, used, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            fun getMaxAmount(): Int {
                val tips = amountInInventory(player, Items.OGRE_ARROW_SHAFT_2864)
                val feathers = Fletching.featherIds.sumOf { amountInInventory(player, it) }
                return min(tips, feathers)
            }

            sendSkillDialogue(player) {
                withItems(Fletching.FLIGHTED_OGRE_ARROW)

                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0 || freeSlots(player) == 0) return@queueScript stopExecuting(player)
                        if (getStatLevel(player, Skills.FLETCHING) < 5) {
                            sendDialogue(player, "You need a fletching level of 5 to do this.")
                            return@queueScript stopExecuting(player)
                        }
                        if (freeSlots(player) == 0) {
                            sendDialogue(player, "You do not have enough inventory space.")
                            return@queueScript stopExecuting(player)
                        }

                        val currentAmount = min(4, getMaxAmount())
                        if (currentAmount <= 0) return@queueScript stopExecuting(player)

                        if (
                            removeItem(player, Item(used.id, currentAmount)) &&
                            removeItem(player, Item(with.id, currentAmount))
                        ) {
                            addItem(player, Fletching.FLIGHTED_OGRE_ARROW, currentAmount)
                            rewardXP(player, Skills.FLETCHING, 5.4)
                            sendMessage(player, "You attach $currentAmount feathers to the ogre arrow shafts.")
                        }

                        remaining--
                        if (remaining > 0) {
                            delayScript(player, 2)
                            true
                        } else {
                            false
                        }
                    }
                }

                calculateMaxAmount { getMaxAmount() }
            }

            return@onUseWith true
        }

        /*
         * Handles attaching nails to arrow shafts to create brutal arrows.
         */

        onUseWith(IntType.ITEM, Fletching.FLIGHTED_OGRE_ARROW, *Fletching.nailIds) { player, _, with ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true
            val brutalArrow = BrutalArrow.product[with.id] ?: return@onUseWith true
            val baseId = Items.FLIGHTED_OGRE_ARROW_2865
            val nailId = with.id
            val maxAmount = min(amountInInventory(player, baseId), amountInInventory(player, nailId))

            if (!inInventory(player, Items.HAMMER_2347)) {
                sendMessage(player, "You need a hammer to attach nails to these arrows.")
                return@onUseWith true
            }

            if (!hasSpaceFor(player, Item(brutalArrow.product))) {
                sendDialogue(player, "You do not have enough inventory space.")
                return@onUseWith true
            }

            sendSkillDialogue(player) {
                withItems(brutalArrow.product)

                create { _, amount ->
                    var remaining = amount

                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0 || freeSlots(player) == 0) return@queueScript false
                        if (getStatLevel(player, Skills.FLETCHING) < brutalArrow.level) {
                            sendDialogue(player, "You need a fletching level of ${brutalArrow.level} to do this.")
                            return@queueScript stopExecuting(player)
                        }

                        val baseAmount = min(6, amountInInventory(player, baseId))
                        val nailAmount = min(6, amountInInventory(player, nailId))
                        val batchAmount = min(baseAmount, nailAmount)

                        val baseItem = Item(baseId, batchAmount)
                        val nailItem = Item(nailId, batchAmount)
                        val productItem = Item(brutalArrow.product, batchAmount)

                        if (removeItem(player, baseItem) && removeItem(player, nailItem)) {
                            addItem(player, productItem.id)
                            rewardXP(player, Skills.FLETCHING, brutalArrow.experience * batchAmount)
                            val message = if (batchAmount == 1) {
                                "You attach the ${getItemName(nailId).lowercase()} to the flighted ogre arrow."
                            } else {
                                "You fletch $batchAmount ${getItemName(brutalArrow.product).lowercase()} arrows."
                            }

                            sendMessage(player, message)
                        }

                        remaining -= batchAmount
                        delayScript(player, 2)
                        true
                    }
                }

                calculateMaxAmount { maxAmount }
            }

            return@onUseWith true
        }
    }

    companion object {
        val FLETCH_LOGS =
            intArrayOf(
                Items.LOGS_1511,
                Items.OAK_LOGS_1521,
                Items.WILLOW_LOGS_1519,
                Items.MAPLE_LOGS_1517,
                Items.YEW_LOGS_1515,
                Items.MAGIC_LOGS_1513,
                Items.ACHEY_TREE_LOGS_2862,
                Items.MAHOGANY_LOGS_6332,
                Items.TEAK_LOGS_6333
            )
    }
}
