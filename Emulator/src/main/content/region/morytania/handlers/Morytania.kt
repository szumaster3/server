package content.region.morytania.handlers

import content.region.morytania.dialogue.AbidorCrankDialogue
import core.api.*
import core.api.MapArea
import core.api.quest.isQuestComplete
import core.game.bots.AIPlayer
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.NPCs
import org.rs.consts.Quests

class Morytania : MapArea {
    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(ZoneBorders(3426, 3191, 3715, 3588))
    }

    override fun areaEnter(entity: Entity) {
        if (entity is Player &&
            entity !is AIPlayer &&
            !isQuestComplete(entity, Quests.PRIEST_IN_PERIL) &&
            entity.details.rights != Rights.ADMINISTRATOR
        ) {
            kickThemOut(entity)
        }
    }

    private fun kickThemOut(entity: Player) {
        val watchdog =
            NPC(NPCs.ABIDOR_CRANK_3635).apply {
                isNeverWalks = true
                isWalks = false
                location = entity.location
                init()
            }
        entity.lock()

        runTask(watchdog, 1) {
            watchdog.moveStep()
            watchdog.face(entity)
            openDialogue(entity, AbidorCrankDialogue(), watchdog)
            GameWorld.Pulser.submit(
                object : Pulse() {
                    override fun pulse(): Boolean {
                        if (getAttribute(entity, "teleporting-away", false)) return true
                        if (!entity.isActive) poofClear(watchdog)
                        if (entity.dialogueInterpreter.dialogue == null ||
                            entity.dialogueInterpreter.dialogue.file == null
                        ) {
                            openDialogue(entity, AbidorCrankDialogue(), watchdog)
                        }
                        return !watchdog.isActive || !entity.isActive
                    }
                },
            )
        }
    }
}
