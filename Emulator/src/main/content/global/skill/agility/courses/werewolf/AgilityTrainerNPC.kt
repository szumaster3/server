package content.global.skill.agility.courses.werewolf

import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.tools.RandomFunction
import org.rs.consts.NPCs

class AgilityTrainerNPC : NPCBehavior(NPCs.AGILITY_TRAINER_1663, NPCs.AGILITY_TRAINER_1664) {
    override fun tick(self: NPC): Boolean {
        if (RandomFunction.roll(25)) {
            when (self.id) {
                1663 -> sendChat(self, forceChat.random())
                else -> sendChat(self, lastDialogue.random())
            }
        }
        return true
    }

    private val forceChat =
        arrayOf(
            "Remember - a slow wolf is a hungry wolf!!",
            "Get on with it - you need your whiskers perking!!!!",
            "Claws first - think later.",
            "Imagine the smell of blood in your nostrils!!!",
            "I never really wanted to be an agility trainer...",
            "It'll be worth it when you hunt!!",
            "Let's see those powerful backlegs at work!!",
            "Let the bloodlust take you!!",
            "You're the slowest wolf I've ever had the misfortune to witness!!",
            "When you're done there's a human with your name on it!!",
        )
    private val lastDialogue =
        arrayOf(
            "Remember - no stick, no agility bonus!",
            "Bring me the stick when you've finished the course!",
            "Don't forget to give me the stick when you're done!",
        )
}
