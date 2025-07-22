package core.game.worldevents.events.christmas.randoms

import core.api.openDialogue
import core.api.sendChat
import core.game.node.entity.npc.NPC
import core.game.worldevents.events.HolidayRandomEventNPC
import core.tools.RandomFunction
import core.tools.minutesToTicks
import org.rs.consts.NPCs

class SnowmanHolidayRandomNPC : HolidayRandomEventNPC(NPCs.SNOWMAN_6746) {
    private val snowmanLines =
        listOf("@name, are you there?", "Excuse me, @name?", "@name, could I please speak with you?")
    private var hasTalkedWith = false

    override fun init() {
        super.init()
        ticksLeft = minutesToTicks(2)
        sendChat(this, "Happy holidays, ${player.username}!", 2)
    }

    override fun tick() {
        if (RandomFunction.roll(15) && !hasTalkedWith) {
            sendChat(this, snowmanLines.random().replace("@name", player.username))
        }

        super.tick()
    }

    override fun talkTo(npc: NPC) {
        face(player)
        hasTalkedWith = true
        openDialogue(player, SnowmanHolidayRandomDialogue(), npc)
    }
}
