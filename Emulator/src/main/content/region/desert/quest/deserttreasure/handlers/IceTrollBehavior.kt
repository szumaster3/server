package content.region.desert.quest.deserttreasure.handlers

import content.region.desert.quest.deserttreasure.DesertTreasure
import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.sendMessage
import core.api.setAttribute
import core.api.setVarbit
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import org.rs.consts.NPCs
import org.rs.consts.Quests

class IceTrollBehavior : NPCBehavior(*iceTrollIds) {
    companion object {
        private val iceTrollIds =
            intArrayOf(
                NPCs.ICE_TROLL_1936,
                NPCs.ICE_TROLL_1937,
                NPCs.ICE_TROLL_1938,
                NPCs.ICE_TROLL_1939,
                NPCs.ICE_TROLL_1940,
                NPCs.ICE_TROLL_1941,
                NPCs.ICE_TROLL_1942,
            )
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (killer is Player) {
            if (getQuestStage(killer, Quests.DESERT_TREASURE) >= 9) {
                val currentIceTrollKill = getAttribute(killer, DesertTreasure.attributeTrollKillCount, 0)
                if (currentIceTrollKill < 5) {
                    setAttribute(killer, DesertTreasure.attributeTrollKillCount, currentIceTrollKill + 1)
                    setVarbit(
                        killer,
                        DesertTreasure.varbitCaveEntrance,
                        getAttribute(
                            killer,
                            DesertTreasure.attributeTrollKillCount,
                            0,
                        ),
                    )
                    sendMessage(killer, "A chunk of ice falls away from the cave entrance...")
                }
            }
        }
    }
}
