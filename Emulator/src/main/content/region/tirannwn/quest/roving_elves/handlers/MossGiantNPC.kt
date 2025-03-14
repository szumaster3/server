package content.region.tirannwn.quest.roving_elves.handlers

import content.region.tirannwn.quest.roving_elves.RovingElves
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
import org.rs.consts.NPCs
import org.rs.consts.Quests

class MossGiantNPC : AbstractNPC {
    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location) {
        this.isAggressive = true
    }

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return MossGiantNPC(id, location)
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        if (killer is Player) {
            val player = killer
            val quest = player.getQuestRepository().getQuest(Quests.ROVING_ELVES)
            if (quest.getStage(player) == 15 && !player.inventory.contains(RovingElves.CONSECRATION_SEED, 1)) {
                player.packetDispatch.sendMessages("A small grey seed drops on the ground.")
                GroundItemManager.create(Item(RovingElves.CONSECRATION_SEED), getLocation(), player)
            }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    NPCDefinition.forId(ids[0]).handlers["option:attack"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    (node as NPC).attack(player)
                    return true
                }
            },
        )
        return super.newInstance(arg)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MOSS_GIANT_1681)
    }
}
