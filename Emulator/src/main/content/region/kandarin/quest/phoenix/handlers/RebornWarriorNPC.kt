package content.region.kandarin.quest.phoenix.handlers

import core.api.sendChat
import core.game.node.entity.npc.AbstractNPC
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class RebornWarriorNPC : AbstractNPC {
    constructor() : super(0, null)

    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return RebornWarriorNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(
            NPCs.LESSER_REBORN_MAGE_8573,
            NPCs.LESSER_REBORN_RANGER_8571,
            NPCs.LESSER_REBORN_WARRIOR_8569,
        )
    }

    val forceChat =
        arrayOf(
            "The mistress is fading!",
            "You must help the mistress!",
            "Save the mistress!",
            "The mistress is in pain!",
            "Please, help the mistress!",
        )

    override fun handleTickActions() {
        super.handleTickActions()
        if (RandomFunction.random(30) < 3) {
            sendChat(this.asNpc(), forceChat.random())
        }
    }
}
