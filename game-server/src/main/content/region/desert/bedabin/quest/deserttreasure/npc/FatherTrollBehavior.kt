package content.region.desert.bedabin.quest.deserttreasure.npc

import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import org.rs.consts.NPCs

class FatherTrollBehavior : NPCBehavior(NPCs.ICE_TROLL_1943,) {

    override fun canBeAttackedBy(self: NPC, attacker: Entity, style: CombatStyle, shouldSendMessage: Boolean, ): Boolean = attacker is Player

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState, ) {
        if (attacker is Player) {
            self.properties.combatPulse.stop()
            attacker.properties.combatPulse.stop()
            if (state.estimatedHit + Integer.max(state.secondaryHit, 0) >= self.skills.lifepoints) {
                state.estimatedHit = self.skills.lifepoints + 1
                state.secondaryHit = -1
                self.skills.lifepoints = self.skills.maximumLifepoints

                setVarbit(attacker, DesertTreasure.varbitFrozenFather, 1)
                if (getVarbit(attacker, DesertTreasure.varbitFrozenMother) == 1) {
                    setVarbit(attacker, DesertTreasure.varbitChildReunite, 4)
                    queueScript(self, 1, QueueStrength.NORMAL) {
                        openDialogue(attacker,
                            content.region.desert.bedabin.quest.deserttreasure.dialogue.ChatFatherAndMotherTrollDialogueFile()
                        )
                        return@queueScript stopExecuting(self)
                    }
                } else {
                    queueScript(self, 1, QueueStrength.NORMAL) {
                        sendNPCDialogue(
                            attacker,
                            NPCs.TROLL_FATHER_1948,
                            "Oh thank you! It was really cold in there! But please, you must free my wife as well! Our son is depending on us!",
                            FaceAnim.OLD_CALM_TALK2,
                        )
                        return@queueScript stopExecuting(self)
                    }
                }
            }
        }
    }
}
