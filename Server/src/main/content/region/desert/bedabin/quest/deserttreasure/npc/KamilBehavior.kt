package content.region.desert.bedabin.quest.deserttreasure.npc

import content.region.desert.bedabin.quest.deserttreasure.DTUtils
import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.NPCs

class KamilBehavior : NPCBehavior(NPCs.KAMIL_1913) {
    private var disappearing = false

    override fun canBeAttackedBy(
        self: NPC,
        attacker: Entity,
        style: CombatStyle,
        shouldSendMessage: Boolean,
    ): Boolean {
        if (attacker is Player) {
            if (attacker == getAttribute<Player?>(self, "target", null)) {
                return true
            }
            sendMessage(attacker, "It's not after you...")
        }
        return false
    }

    override fun tick(self: NPC): Boolean {
        if (disappearing) {
            return true
        }
        val player: Player? = getAttribute<Player?>(self, "target", null)
        if (player == null || !self.location.withinDistance(self.properties.spawnLocation, (self.walkRadius*1.5).toInt())) {
            if (player != null && !disappearing) {
                disappearing = true
                sendMessage(player, "Kamil vanishes on an icy wind...")
                removeAttribute(player, DesertTreasure.attributeKamilInstance)
            }
            poofClear(self)
        }
        return true
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (killer is Player) {
            if (DTUtils.getSubStage(killer, DesertTreasure.iceStage) == 2) {
                DTUtils.setSubStage(killer, DesertTreasure.iceStage, 3)
                removeAttribute(killer, DesertTreasure.attributeKamilInstance)
                sendPlayerDialogue(
                    killer,
                    "Well, that must have been the 'bad man' that the troll kid was on about... His parents must be up ahead somewhere.",
                )
            }
        }
    }

    override fun getSwingHandlerOverride(
        self: NPC,
        original: CombatSwingHandler,
    ): CombatSwingHandler = KamilCombatHandler()
}

class KamilCombatHandler :
    MultiSwingHandler(
        SwitchAttack(CombatStyle.MELEE.swingHandler, null),
    ) {
    override fun impact(
        entity: Entity?,
        victim: Entity?,
        state: BattleState?,
    ) {
        if (victim is Player) {
            if (RandomFunction.roll(3) &&
                !hasTimerActive(victim, "frozen") &&
                !hasTimerActive(
                    victim,
                    "frozen:immunity",
                )
            ) {
                sendChat(entity as NPC, "Sallamakar Ro!")
                impact(victim, 5)
                impact(victim, 5)
                registerTimer(victim, spawnTimer("frozen", 7, true))
                sendMessage(victim, "You've been frozen!")
                sendGraphics(539, victim.location)
                victim.properties.combatPulse.stop()
            } else {
                entity?.animate(Animation(440))
            }
        }
        super.impact(entity, victim, state)
    }
}