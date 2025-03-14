package content.region.desert.quest.desertrescue

import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.plugin.ClassScanner.definePlugin
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class RowdySlaveNPC : AbstractNPC {
    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location) {
        this.isAggressive = true
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return RowdySlaveNPC(id, location)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val player = killer
            GroundItemManager.create(Item(Items.BONES_526), getLocation(), player)
            if (!TouristTrap.hasSlaveClothes(player) && !player.equipment.containsItems(*TouristTrap.SLAVE_CLOTHES)) {
                player.packetDispatch.sendMessages(
                    "The slave drops his shirt.",
                    "The slave drops his robe.",
                    "The slave drops his boots.",
                )
                for (i in TouristTrap.SLAVE_CLOTHES) {
                    GroundItemManager.create(i, getLocation(), player)
                }
            }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    NPCDefinition.forId(ids[0]).handlers["option:talk-to"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    (node as NPC).sendChat(CHATS[RandomFunction.random(CHATS.size)])
                    node.attack(player)
                    return true
                }
            },
        )
        return super.newInstance(arg)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.ROWDY_SLAVE_827)
    }

    companion object {
        private val CHATS =
            arrayOf(
                "Oi! Are you looking at me?",
                "I'm going to teach you some respect!",
                "Hey, you're in for a good beating!",
            )
    }
}
