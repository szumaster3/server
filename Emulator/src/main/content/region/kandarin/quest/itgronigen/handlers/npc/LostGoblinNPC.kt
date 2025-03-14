package content.region.kandarin.quest.itgronigen.handlers.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class LostGoblinNPC : NPCBehavior(NPCs.GOBLIN_6126) {
    val forceChat =
        arrayOf(
            "Which way should I go?",
            "These dungeons are such a maze.",
            "Where's the exit?!?",
            "This is the fifth time this week. I'm lost!",
            "I've been wandering around down here for hours.",
            "How do you get back to the village?",
            "I hate being so lost!",
            "How could I be so disoriented?",
            "Where am I? I'm so lost.",
            "I know the exit's around here, somewhere.",
        )

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(15) == 5) {
            sendChat(self, forceChat.random())
        }
        return true
    }
}
