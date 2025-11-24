package content.global.skill.fletching

import content.global.skill.fletching.bolts.Bolt
import content.global.skill.fletching.bolts.BoltPulse
import content.global.skill.fletching.bolts.GemBolt
import content.global.skill.fletching.darts.Dart
import content.global.skill.fletching.darts.DartPulse
import core.api.amountInInventory
import core.api.clockReady
import core.api.submitIndividualPulse
import core.game.dialogue.SkillDialogueHandler
import core.game.interaction.Clocks
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items
import kotlin.math.min

@Initializable
class FletchingPlugin : UseWithHandler(*BASE) {

    companion object {
        val BASE = intArrayOf(Items.BRONZE_DART_TIP_819, Items.IRON_DART_TIP_820, Items.STEEL_DART_TIP_821, Items.MITHRIL_DART_TIP_822, Items.ADAMANT_DART_TIP_823, Items.RUNE_DART_TIP_824, Items.DRAGON_DART_TIP_11232, Items.BRONZE_BOLTS_UNF_9375, Items.BLURITE_BOLTS_UNF_9376, Items.IRON_BOLTS_UNF_9377, Items.SILVER_BOLTS_UNF_9382, Items.STEEL_BOLTS_UNF_9378, Items.MITHRIL_BOLTS_UNF_9379, Items.ADAMANT_BOLTS_UNF_9380, Items.RUNITE_BOLTS_UNF_9381, Items.BROAD_BOLTS_UNF_13279)
        private val gemMap = mutableMapOf<Int, GemBolt>()
        private val tipMap = mutableMapOf<Int, GemBolt>()

        init {
            GemBolt.values().forEach { gem ->
                gemMap.putIfAbsent(gem.gem, gem)
                tipMap.putIfAbsent(gem.tip, gem)
            }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        val feathers = Fletching.featherIds
        feathers.forEach { addHandler(it, ITEM_TYPE, this) }
        return this
    }

    override fun handle(event: NodeUsageEvent): Boolean = when {
        Dart.isDart(event.usedItem.id) -> handleDart(event)
        Bolt.isBolt(event.usedItem.id) || Bolt.isBolt(event.usedWith.id) -> handleBolt(event)
        else -> false
    }

    private fun handleDart(event: NodeUsageEvent): Boolean {
        if (!clockReady(event.player, Clocks.SKILLING)) return false
        val dart = Dart.product[event.usedItem.id] ?: return false
        val handler = object : SkillDialogueHandler(event.player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(dart.finished)) {
            override fun create(amount: Int, index: Int) {
                submitIndividualPulse(
                    entity = event.player,
                    pulse = DartPulse(event.player, event.usedItem, dart, amount)
                )
            }

            override fun getAll(index: Int): Int = min(
                amountInInventory(player, event.usedItem.id),
                amountInInventory(player, event.usedWith.id),
            )
        }
        handler.open()
        return true
    }

    private fun handleBolt(event: NodeUsageEvent): Boolean {
        if (!clockReady(event.player, Clocks.SKILLING)) return false
        val bolt = Bolt.product[event.usedItem.id] ?: Bolt.product[event.usedWith.id] ?: return false

        val featherId = if (Bolt.isBolt(event.usedItem.id)) event.usedWith.id else event.usedItem.id
        val hasFeather = featherId in listOf(Items.FEATHER_314) || (featherId in Items.STRIPY_FEATHER_10087..Items.ORANGE_FEATHER_10091)

        if (!hasFeather) return false

        val handler = object : SkillDialogueHandler(event.player, SkillDialogue.MAKE_SET_ONE_OPTION, Item(bolt.finished)) {
            override fun create(amount: Int, index: Int) {
                submitIndividualPulse(
                    entity = event.player,
                    pulse = BoltPulse(event.player, event.usedItem, bolt, Item(featherId), amount)
                )
            }

            override fun getAll(index: Int): Int = min(
                amountInInventory(player, event.usedItem.id),
                amountInInventory(player, event.usedWith.id),
            )
        }
        handler.open()
        return true
    }
}
