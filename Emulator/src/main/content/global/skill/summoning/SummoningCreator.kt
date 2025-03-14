package content.global.skill.summoning

import core.api.*
import core.api.quest.isQuestComplete
import core.cache.def.impl.CS2Mapping
import core.game.component.Component
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager.getObject
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Sounds

object SummoningCreator {
    private val POUCH_PARAMS =
        arrayOf(
            "List<col=FF9040>",
            "Infuse-X<col=FF9040>",
            "Infuse-All<col=FF9040>",
            "Infuse-10<col=FF9040>",
            "Infuse-5<col=FF9040>",
            "Infuse<col=FF9040>",
            20,
            4,
            669 shl 16 or 15,
        )

    private val SCROLL_PARAMS =
        arrayOf(
            "Transform-X<col=ff9040>",
            "Transform-All<col=ff9040>",
            "Transform-10<col=ff9040>",
            "Transform-5<col=ff9040>",
            "Transform<col=ff9040>",
            20,
            4,
            673 shl 16 or 15,
        )

    private val SUMMONING_COMPONENT = Component(669)

    private val SCROLL_COMPONENT = Component(673)

    @JvmStatic
    fun open(
        player: Player,
        pouch: Boolean,
    ) = configure(player, pouch)

    @JvmStatic
    fun configure(
        player: Player,
        pouch: Boolean,
    ) {
        val component = if (pouch) SUMMONING_COMPONENT else SCROLL_COMPONENT
        val scriptParams = if (pouch) POUCH_PARAMS else SCROLL_PARAMS
        val ifaceId = if (pouch) 190 else 126
        val componentId = if (pouch) 669 else 673

        player.interfaceManager.open(component)
        player.packetDispatch.sendRunScript(
            if (pouch) 757 else 765,
            if (pouch) "Iiissssss" else "Iiisssss",
            *scriptParams,
        )
        player.packetDispatch.sendIfaceSettings(ifaceId, 15, componentId, 0, 78)
    }

    @JvmStatic
    fun create(
        player: Player,
        amount: Int,
        node: Any?,
    ) {
        node?.let {
            player.pulseManager.run(CreatePulse(player, SummoningNode.parse(node), amount))
        }
    }

    @JvmStatic
    fun list(
        player: Player,
        pouch: SummoningPouch,
    ) {
        player.packetDispatch.sendMessage(CS2Mapping.forId(1186)?.map?.get(pouch.pouchId) as? String)
    }

    class CreatePulse(
        player: Player?,
        private val type: SummoningNode,
        private val amount: Int,
    ) : SkillPulse<Item?>(player, null) {
        private val objectIDs = getObject(Location(2209, 5344, 0))

        override fun checkRequirements(): Boolean {
            closeInterface(player)
            return when {
                getStatLevel(player, Skills.SUMMONING) < type.level -> {
                    sendMessage(player, "You need a Summoning level of at least ${type.level} to do this.")
                    false
                }

                type.isPouch &&
                    type.product.id == Items.PHOENIX_POUCH_14624 &&
                    !isQuestComplete(player, Quests.IN_PYRE_NEED) -> {
                    sendMessage(player, "You must complete In Pyre Need to infuse phoenix pouches.")
                    false
                }

                amount == 0 || !type.required.all { inInventory(player, it.id) } -> {
                    sendMessage(player, "You don't have the required items to make this.")
                    false
                }

                else -> true
            }
        }

        override fun animate() {
            lock(player, 3)
            animate(player, Animations.INFUSE_SUMMONING_POUCH_8500)
        }

        override fun stop() {
            super.stop()
            animateScenery(player, objectIDs!!, 8510, true)
        }

        override fun reward(): Boolean {
            if (delay == 1) {
                delay = 4
                animateScenery(player, objectIDs!!, 8509, true)
                playAudio(player, Sounds.CRAFT_POUCH_4164)
                return false
            }

            // "You should speak to Pikkupstix. He will tell you what to do with the scrolls and the"
            // "spirit wolf pouch."

            // You should speak to Pikkupstix to get your reward.

            animateScenery(player, objectIDs!!, 8510, true)
            repeat(amount) {
                if (type.required.all { anyInInventory(player, it.id) } && player.inventory.remove(*type.required)) {
                    player.inventory.add(type.product)
                    rewardXP(player, Skills.SUMMONING, type.experience)
                }
            }
            return true
        }
    }

    class SummoningNode(
        val base: Any,
        val required: Array<Item>,
        val product: Item,
        val experience: Double,
        val level: Int,
    ) {
        val isPouch: Boolean get() = base is SummoningPouch

        companion object {
            fun parse(node: Any): SummoningNode =
                when (node) {
                    is SummoningPouch ->
                        SummoningNode(
                            base = node,
                            required = node.items,
                            product = Item(node.pouchId, 1),
                            experience = node.createExperience,
                            level = node.requiredLevel,
                        )

                    is SummoningScroll ->
                        SummoningNode(
                            base = node,
                            required = node.items.map { Item(it, 1) }.toTypedArray(),
                            product = Item(node.itemId, 10),
                            experience = node.experience,
                            level = node.level,
                        )

                    else -> throw IllegalArgumentException("Invalid node type: [${node::class.simpleName}]")
                }
        }
    }
}
