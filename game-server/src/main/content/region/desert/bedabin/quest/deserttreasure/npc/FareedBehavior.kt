package content.region.desert.bedabin.quest.deserttreasure.npc

import content.global.skill.magic.spells.modern.WaterSpell
import content.region.desert.bedabin.quest.deserttreasure.DTUtils
import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.global.action.EquipHandler
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import org.rs.consts.Items
import org.rs.consts.NPCs

class FareedBehavior : NPCBehavior(NPCs.FAREED_1977) {
    var clearTime = 0

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
        val player: Player? = getAttribute<Player?>(self, "target", null)
        if (clearTime++ > 800) {
            clearTime = 0
            if (player != null) {
                sendMessage(player, "Fareed has lost interest in you, and returned to his flames.")
                removeAttribute(player, DesertTreasure.attributeFareedInstance)
            }
            poofClear(self)
        }
        return true
    }

    override fun beforeDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        if (state.style == CombatStyle.MAGIC && state.spell !is WaterSpell) {
            state.neutralizeHits()
        }
    }

    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        if (victim is Player) {
            if (!inEquipment(victim, Items.ICE_GLOVES_1580)) {
                val weapon = getItemFromEquipment(victim, EquipmentSlot.WEAPON)
                if (weapon != null) {
                    EquipHandler.unequip(victim, EquipmentContainer.SLOT_WEAPON, weapon.id)
                }

                sendMessage(victim, "The heat from the warrior causes you to drop your weapon.")
            }
        }
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (killer is Player) {
            addItemOrDrop(killer, Items.SMOKE_DIAMOND_4672)
            sendMessage(killer, "You take the Diamond of Smoke from the ashes of the warrior.")
            if (DTUtils.getSubStage(killer, DesertTreasure.smokeStage) == 1) {
                DTUtils.setSubStage(killer, DesertTreasure.smokeStage, 100)
                removeAttribute(killer, DesertTreasure.attributeFareedInstance)
            }
        }
    }
}
