package content.region.misthalin.quest.fluffs.handlers

import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class LumberKittenNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    private var hidden = true
    private var nextSpeak = 0
    private var hideDelay = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return LumberKittenNPC(id, location)
    }

    override fun init() {
        isWalks = false
        super.init()
    }

    override fun tick() {
        if (nextSpeak < ticks) {
            hidden = false
            nextSpeak = ticks + RandomFunction.random(10, 40)
            hideDelay = ticks + 4
            sendChat("Mew!")
        } else if (hideDelay < ticks) {
            hidden = true
            val rand = RandomFunction.random(20, 40)
            hideDelay = ticks + rand
            nextSpeak = ticks + rand
        }
        super.tick()
    }

    override fun isHidden(player: Player): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.GERTRUDES_CAT)
        return hidden || quest.getStage(player) < 20 || quest.getStage(player) > 50
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.CRATE_767)
    }
}
