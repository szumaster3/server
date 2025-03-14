package content.region.kandarin.quest.itgronigen.handlers.npc

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class GoblinDungeonNPC : NPCBehavior(NPCs.WAGCHIN_6124, NPCs.NAGHEAD_6123) {
    val wagchinChat =
        arrayOf(
            "Weren't you going to tell me about the rumour?",
            "Carry on.",
            "That joined our horde?",
            "Oh my! Do tell.",
            "Yes...",
            "I'm listening.",
            "Where?",
            "We are?",
            "Oh my!",
            "A goblin folk?",
            "Bizarre creatures for sure.",
            "Why?",
            "For sure, my dear.",
            "What a horrid thought!",
            "What should we do?",
            "Let's!",
            "Yes?",
        )

    val nagheadChat =
        arrayOf(
            "Oh yeah, that was it!",
            "It's about the new family.",
            "The very same!",
            "I were hangin' out me washin'.",
            "And I looked over at-",
            "Wait, we're being watched!",
            "Yes, a person over there.",
            "No! Don't make eye contact.",
            "They're listening to us.",
            "Oh no! It's one of those tall people.",
            "Makes my skin crawl to see them.",
            "So tall and neat.",
            "I hope it doesn't come to speak with us.",
            "Horrid indeed, my dear.",
            "Let us carry on our conversation.",
            "So...",
            "What was I talking about, now?",
        )

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.random(5) == 1) {
            if (self.id == NPCs.WAGCHIN_6124) {
                sendChat(self, wagchinChat.random())
                    .also {
                        sendChat(self, nagheadChat.random())
                    }
            }
        }
        return true
    }
}
