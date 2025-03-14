package content.global.skill.hunter

import core.api.addScenery
import core.api.removeScenery
import core.api.sendMessage
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.SceneryBuilder
import core.game.world.update.flag.context.Animation
import org.rs.consts.Scenery

class NetTrapSetting :
    TrapSetting(
        intArrayOf(19652, 19663, 19671, 19679, 28564),
        arrayOf(Item(303), Item(954)),
        NetTrap.ids,
        intArrayOf(10142, 10143, 10144, 10145),
        "set-trap",
        29,
        -1,
        Animation(5215),
        Animation.create(5207),
        true,
    ) {
    override fun hasItems(player: Player): Boolean {
        if (!super.hasItems(player)) {
            sendMessage(player, "You need a net and a rope to set a net trap.")
            return false
        }
        return true
    }

    override fun clear(
        wrapper: TrapWrapper,
        type: Int,
    ): Boolean {
        if (super.clear(wrapper, type)) {
            if (wrapper.secondary != null && wrapper.secondary.isActive) {
                removeScenery(wrapper.secondary)
            } else {
                addScenery(wrapper.getObject().transform(wrapper.netType.original))
            }

            return true
        }
        return false
    }

    override fun returnItems(
        scenery: core.game.node.scenery.Scenery,
        wrapper: TrapWrapper,
        type: Int,
    ) {
        super.returnItems(scenery, wrapper, type)
        if (type == 0) {
            for (i in super.getItems()) {
                createGroundItem(i, scenery.location, wrapper.player)
            }
        }
    }

    override fun reward(
        player: Player,
        node: Node,
        wrapper: TrapWrapper,
    ) {
        val `object` = wrapper.getObject()
        wrapper.netType = NetTrap.forId(node.id)
        var rotation = 0
        var increment = 0
        var x = false
        val netInfo = getNetInfo(player, node)
        rotation = netInfo[0] as Int
        increment = netInfo[1] as Int
        x = netInfo[2] as Boolean
        var secondary: core.game.node.scenery.Scenery? =
            core.game.node.scenery.Scenery(
                wrapper.netType.net,
                `object`.location.transform(if (x) increment else 0, if (!x) increment else 0, 0),
                rotation,
            )
        secondary = SceneryBuilder.add(secondary)
        wrapper.secondary = secondary
        player.moveStep()
        wrapper.addItem(*items)
        player.inventory.remove(*wrapper.type.settings.items)
    }

    override fun handleCatch(
        counter: Int,
        wrapper: TrapWrapper,
        node: TrapNode,
        npc: NPC,
        success: Boolean,
    ) {
        when (counter) {
            2 -> removeScenery(wrapper.secondary)
            3 -> {
                npc.moveStep()
                wrapper.setObject(wrapper.netType.failed)
            }
        }
    }

    override fun buildObject(
        player: Player,
        node: Node,
    ): core.game.node.scenery.Scenery {
        return (node as core.game.node.scenery.Scenery).transform(NetTrap.forId(node.getId())!!.bent)
    }

    override fun createHook(wrapper: TrapWrapper): TrapHook {
        return TrapHook(wrapper, arrayOf(wrapper.secondary.location))
    }

    override fun getTransformId(
        wrapper: TrapWrapper?,
        node: TrapNode?,
    ): Int {
        return wrapper!!.netType.catching
    }

    override fun getFinalId(
        wrapper: TrapWrapper,
        node: TrapNode,
    ): Int {
        return wrapper.netType.caught
    }

    override fun getFailId(
        wrapper: TrapWrapper,
        node: TrapNode,
    ): Int {
        return wrapper.netType.failing
    }

    override fun getTimeUpMessage(): String {
        return "The net trap that you constructed has collapsed."
    }

    private fun getNetInfo(
        player: Player,
        node: Node,
    ): Array<Any> {
        val rotation: Int
        val increment: Int
        var x = false
        if (player.location.x < node.location.x) {
            rotation = 3
            increment = -1
            x = true
        } else if (player.location.x > node.location.x) {
            rotation = 1
            increment = 1
            x = true
        } else if (player.location.y < node.location.y) {
            rotation = 2
            increment = -1
        } else {
            rotation = 0
            increment = 1
        }
        return arrayOf(rotation, increment, x)
    }

    enum class NetTrap(
        val original: Int,
        @JvmField val bent: Int,
        val failing: Int,
        @JvmField val failed: Int,
        val catching: Int,
        @JvmField val caught: Int,
        @JvmField val net: Int,
    ) {
        GREEN(
            original = Scenery.YOUNG_TREE_19679,
            bent = Scenery.YOUNG_TREE_19678,
            failing = Scenery.NET_TRAP_19676,
            failed = Scenery.NET_TRAP_19677,
            catching = Scenery.NET_TRAP_19674,
            caught = Scenery.NET_TRAP_19675,
            net = Scenery.NET_TRAP_19651,
        ),

        SQUIRREL(
            original = Scenery.YOUNG_TREE_28564,
            bent = Scenery.YOUNG_TREE_28563,
            failing = Scenery.NET_TRAP_28752,
            failed = Scenery.NET_TRAP_28753,
            catching = Scenery.NET_TRAP_28750,
            caught = Scenery.NET_TRAP_28751,
            net = Scenery.NET_TRAP_28566,
        ),

        ORANGE(
            original = Scenery.YOUNG_TREE_19652,
            bent = Scenery.YOUNG_TREE_19650,
            failing = Scenery.NET_TRAP_19657,
            failed = Scenery.NET_TRAP_19656,
            catching = Scenery.NET_TRAP_19655,
            caught = Scenery.NET_TRAP_19654,
            net = Scenery.NET_TRAP_19665,
        ),

        RED(
            original = Scenery.YOUNG_TREE_19663,
            bent = Scenery.YOUNG_TREE_19662,
            failing = Scenery.NET_TRAP_19660,
            failed = Scenery.NET_TRAP_19661,
            catching = Scenery.NET_TRAP_19658,
            caught = Scenery.NET_TRAP_19659,
            net = Scenery.NET_TRAP_19673,
        ),

        BLACK(
            original = Scenery.YOUNG_TREE_19671,
            bent = Scenery.YOUNG_TREE_19670,
            failing = Scenery.NET_TRAP_19668,
            failed = Scenery.NET_TRAP_19669,
            catching = Scenery.NET_TRAP_19666,
            caught = Scenery.NET_TRAP_19667,
            net = Scenery.NET_TRAP_19681,
        ),
        ;

        companion object {
            @JvmStatic
            fun forId(id: Int): NetTrap? {
                for (trap in values()) {
                    if (trap.original == id) {
                        return trap
                    }
                }
                return null
            }

            val ids: IntArray
                get() {
                    val ids: MutableList<Int> = ArrayList(10)
                    for (trap in values()) {
                        ids.add(trap.bent)
                        ids.add(trap.caught)
                        ids.add(trap.net)
                        ids.add(trap.original)
                    }
                    val array = IntArray(ids.size)
                    for (i in array.indices) {
                        array[i] = ids[i]
                    }
                    return array
                }
        }
    }
}
