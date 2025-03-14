package content.global.skill.crafting.glassblowing.lamps

import core.api.getItemName
import core.api.getStatLevel
import core.api.playAudio
import core.api.sendMessage
import core.game.container.Container
import core.game.event.LitLightSourceEvent
import core.game.interaction.NodeUsageEvent
import core.game.interaction.UseWithHandler
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class LightSourceHandler : UseWithHandler(Items.TINDERBOX_590, Items.CANDLE_36, Items.BLACK_CANDLE_38) {
    override fun newInstance(arg: Any?): Plugin<Any> {
        addHandler(Items.TINDERBOX_590, ITEM_TYPE, this)
        addHandler(Items.UNLIT_TORCH_596, ITEM_TYPE, this)
        addHandler(Items.CANDLE_LANTERN_4529, ITEM_TYPE, this)
        addHandler(Items.CANDLE_LANTERN_4532, ITEM_TYPE, this)
        addHandler(Items.OIL_LAMP_4522, ITEM_TYPE, this)
        addHandler(Items.OIL_LANTERN_4537, ITEM_TYPE, this)
        addHandler(Items.BULLSEYE_LANTERN_4548, ITEM_TYPE, this)
        addHandler(Items.SAPPHIRE_LANTERN_4701, ITEM_TYPE, this)
        addHandler(Items.EMERALD_LANTERN_9064, ITEM_TYPE, this)
        return this
    }

    override fun handle(event: NodeUsageEvent?): Boolean {
        event ?: return false

        val used = if (event.used.id == Items.TINDERBOX_590) event.usedWith.asItem() else event.used.asItem()
        val lightSources = LightSources.forId(used.id)

        lightSources ?: return false

        if (!light(event.player, used, lightSources)) {
            sendMessage(
                event.player,
                "You need a Firemaking level of at least ${lightSources.levelRequired} to light this.",
            )
        }

        return true
    }

    fun Container.replace(
        item: Item,
        with: Item,
    ) {
        if (remove(item)) {
            add(with)
        }
    }

    fun light(
        player: Player,
        item: Item,
        lightSources: LightSources,
    ): Boolean {
        val requiredLevel = lightSources.levelRequired
        val playerLevel = getStatLevel(player, Skills.FIREMAKING)

        if (playerLevel < requiredLevel) return false

        // Making sure that a lit source cannot be ignited again.
        if (item.id != lightSources.litId) {
            playAudio(player, lightSources.sfxId)
            player.inventory.replace(item, Item(lightSources.litId))
            player.dispatch(LitLightSourceEvent(lightSources.litId))
            sendMessage(player, "You light the ${getItemName(LightSources.forId(item.id)!!.litId)}.")
        }
        return true
    }
}
