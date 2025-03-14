package content.global.skill.hunter

import content.global.skill.hunter.NetTrapSetting.NetTrap
import core.api.log
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.Log

enum class Traps(
    @JvmField val settings: TrapSetting,
    vararg nodes: TrapNode,
) {
    BIRD_SNARE(
        TrapSetting(
            10006,
            intArrayOf(19175),
            intArrayOf(),
            "lay",
            19174,
            Animation.create(5208),
            Animation.create(5207),
            1,
        ),
        TrapNode(intArrayOf(5073), 1, 34.0, intArrayOf(19179, 19180), arrayOf(Item(10088, 8), Item(9978), Item(526))),
        TrapNode(intArrayOf(5075), 5, 48.0, intArrayOf(19183, 19184), arrayOf(Item(10090, 8), Item(9978), Item(526))),
        TrapNode(intArrayOf(5076), 9, 61.0, intArrayOf(19185, 19186), arrayOf(Item(10091, 8), Item(9978), Item(526))),
        TrapNode(intArrayOf(5074), 11, 64.7, intArrayOf(19181, 19182), arrayOf(Item(10089, 8), Item(9978), Item(526))),
        TrapNode(intArrayOf(5072), 19, 95.2, intArrayOf(19177, 19178), arrayOf(Item(10087, 8), Item(9978), Item(526))),
        object : TrapNode(
            intArrayOf(7031),
            39,
            167.0,
            intArrayOf(28931, 28930),
            arrayOf(Item(11525, 8), Item(9978), Item(526)),
        ) {
            override fun canCatch(
                wrapper: TrapWrapper,
                npc: NPC,
            ): Boolean {
                return false
            }
        },
    ),
    BOX_TRAP(
        TrapSetting(
            10008,
            intArrayOf(19187),
            intArrayOf(1963, 12579, 1869, 9996, 5972, 12535),
            "lay",
            19192,
            Animation.create(5208),
            Animation(9726),
            27,
        ),
        BoxTrapNode(intArrayOf(5081), 27, 100.0, arrayOf(Item(10092)), 1),
        BoxTrapNode(intArrayOf(6918, 7289, 7290, 7291, 7292), 27, 100.0, arrayOf(Item(12184)), 10),
        BoxTrapNode(intArrayOf(1487), 27, 100.0, arrayOf(Item(4033, 1)), 95),
        BoxTrapNode(intArrayOf(7021, 7022, 7023), 48, 150.0, arrayOf(Item(12551, 1)), 1),
        BoxTrapNode(intArrayOf(5079), 53, 198.0, arrayOf(Item(10033, 1)), 1),
        BoxTrapNode(intArrayOf(5428, 5430, 5449, 5450, 5451), 56, 150.0, arrayOf(Item(12188)), 1),
        BoxTrapNode(intArrayOf(5080), 63, 265.0, arrayOf(Item(10034, 1)), 1),
        BoxTrapNode(intArrayOf(7012, 7014), 66, 400.0, arrayOf(Item(12535)), 1),
        BoxTrapNode(intArrayOf(8654), 73, 315.0, arrayOf(Item(14861)), 1),
        object : BoxTrapNode(intArrayOf(7010, 7011), 77, 0.0, arrayOf(Item(12539, 1)), 1) {
            override fun canCatch(
                wrapper: TrapWrapper,
                npc: NPC,
            ): Boolean {
                wrapper.player.sendMessage(
                    "Note: Giving 0 xp for grenwalls until this area and its requirements are implemented.",
                )
                return super.canCatch(wrapper, npc)
            }
        },
    ),
    RABBIT_SNARE(
        TrapSetting(
            10031,
            intArrayOf(19333),
            intArrayOf(),
            "lay",
            -1,
            Animation.create(5208),
            Animation.create(9726),
            27,
        ),
    ),
    IMP_BOX(
        MagicBoxSetting(),
        TrapNode(intArrayOf(708, 709, 1531), 71, 450.0, intArrayOf(-1, 19226), arrayOf(Item(10027))),
    ),
    DEAD_FALL(
        DeadfallSetting(),
        TrapNode(intArrayOf(5089), 23, 128.0, intArrayOf(19213, 19214, 19218), arrayOf(Item(10113), Item(526))),
        TrapNode(intArrayOf(5088), 33, 168.0, intArrayOf(19211, 19212, 19217), arrayOf(Item(10129), Item(526))),
        TrapNode(intArrayOf(5086), 37, 204.0, intArrayOf(19208, 19208, 19217), arrayOf(Item(10105), Item(526))),
        TrapNode(intArrayOf(7039), 44, 200.0, intArrayOf(28939, 28940, 28941), arrayOf(Item(12567), Item(526))),
        TrapNode(intArrayOf(5087), 51, 200.0, intArrayOf(19209, 19210, 19216), arrayOf(Item(10109), Item(526))),
    ),
    NET_TRAP(
        NetTrapSetting(),
        TrapNode(intArrayOf(5117), 29, 152.0, intArrayOf(), arrayOf(Item(10149))),
        TrapNode(intArrayOf(5114), 47, 224.0, intArrayOf(), arrayOf(Item(10146))),
        TrapNode(intArrayOf(6921), 29, 152.0, intArrayOf(), arrayOf(Item(12130))),
        TrapNode(intArrayOf(5115), 59, 272.0, intArrayOf(), arrayOf(Item(10147))),
        TrapNode(intArrayOf(5116), 67, 304.0, intArrayOf(), arrayOf(Item(10148))),
    ),
    ;

    private val hooks: MutableList<TrapHook> = ArrayList(5)

    @JvmField
    val nodes: Array<TrapNode> = nodes as Array<TrapNode>

    fun create(
        player: Player,
        node: Node,
    ) {
        player.pulseManager.run(TrapCreatePulse(player, node, this))
    }

    fun dismantle(
        player: Player,
        scenery: Scenery,
    ) {
        val instance = HunterManager.getInstance(player)

        if (!instance.isOwner(scenery)) {
            player.sendMessage("This isn't your trap!")
            return
        }
        if (instance.getWrapper(scenery) == null) {
            log(this.javaClass, Log.ERR, "NO WRAPPER (HUNTER DISMANTLE)")
            return
        }
        player.faceLocation(scenery.location)
        player.pulseManager.run(TrapDismantlePulse(player, scenery, instance.getWrapper(scenery)!!))
    }

    fun investigate(
        player: Player,
        scenery: Scenery,
    ) {
        settings.investigate(player, scenery)
    }

    fun catchNpc(
        wrapper: TrapWrapper,
        npc: NPC,
    ) {
        val trapNode = forNpc(npc)
        if (trapNode == null || !trapNode.canCatch(wrapper, npc) || !settings.canCatch(wrapper, npc)) {
            return
        }
        settings.catchNpc(wrapper, trapNode, npc)
    }

    fun addHook(wrapper: TrapWrapper): TrapHook {
        val hook = settings.createHook(wrapper)
        hooks.add(hook)
        return hook
    }

    fun forNpc(npc: NPC): TrapNode? {
        for (node in nodes) {
            for (npcId in node.npcIds) {
                if (npcId == npc.id) {
                    return node
                }
            }
        }
        return null
    }

    fun getByHook(location: Location): TrapWrapper? {
        for (hook in hooks) {
            if (hook.isHooked(location)) {
                return hook.wrapper
            }
        }
        return null
    }

    fun getHooks(): List<TrapHook> {
        return hooks
    }

    companion object {
        @JvmStatic
        fun forNode(node: Node): Traps? {
            for (trap in values()) {
                for (nodeId in trap.settings.nodeIds) {
                    if (node.id == nodeId) {
                        return trap
                    }
                }
                for (objectId in trap.settings.objectIds) {
                    if (objectId == node.id) {
                        return trap
                    }
                }
                for (n in trap.nodes) {
                    for (id in n.objectIds) {
                        if (id == node.id) {
                            return trap
                        }
                    }
                }
                if (trap.settings.failId == node.id) {
                    return trap
                }
                if (trap == NET_TRAP) {
                    for (net in NetTrap.values()) {
                        if (net.original == node.id ||
                            net.failed == node.id ||
                            net.net == node.id ||
                            net.bent == node.id ||
                            net.caught == node.id
                        ) {
                            return trap
                        }
                    }
                }
            }
            return null
        }

        @JvmStatic
        fun getNode(id: Int): Array<Any>? {
            for (trap in values()) {
                for (t in trap.nodes) {
                    for (i in t.npcIds) {
                        if (i == id) {
                            return arrayOf(trap, t)
                        }
                    }
                }
            }
            return null
        }
    }
}
