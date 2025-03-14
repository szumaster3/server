package content.global.skill.hunter.impling

import content.global.skill.magic.spells.ModernSpells
import core.api.log
import core.api.sendChat
import core.api.sendGraphics
import core.api.sendMessage
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.item.Item
import core.game.world.map.path.ClipMaskSupplier
import core.tools.Log
import core.tools.RandomFunction

class ImplingNPC : NPCBehavior(*Implings.getIds()) {
    override fun onCreation(self: NPC) {
        self.isWalks = true
        self.isNeverWalks = false
    }

    override fun tick(self: NPC): Boolean {
        if (RandomFunction.roll(10)) {
            sendChat(self, "Tee-hee!")
        }
        return true
    }

    override fun onRespawn(self: NPC) {
        if (!isPuroImpling(self)) {
            log(this::class.java, Log.ERR, "Non-puro impling has respawned!")
        }
        sendGraphics(1119, self.properties.teleportLocation)
    }

    override fun canBeAttackedBy(
        self: NPC,
        attacker: Entity,
        style: CombatStyle,
        shouldSendMessage: Boolean,
    ): Boolean {
        if (attacker !is Player) return false

        if (style != CombatStyle.MAGIC) {
            if (shouldSendMessage) {
                sendMessage(attacker, "You can't do that.")
            }
            return false
        }

        val spellBook = attacker.spellBookManager.spellBook
        if (spellBook != SpellBookManager.SpellBook.MODERN.interfaceId) {
            if (shouldSendMessage) {
                sendMessage(attacker, "The impling is too fast for that.")
            }
            return false
        }

        val spellId = attacker.properties.spell.spellId
        if (spellId != ModernSpells.BIND && spellId != ModernSpells.SNARE && spellId != ModernSpells.ENTANGLE) {
            if (shouldSendMessage) {
                sendMessage(attacker, "The impling is immune to that magic.")
            }
            return false
        }

        return true
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (!isPuroImpling(self)) {
            ImplingController.deregister(self)
        } else if (self.originalId != self.id) {
            self.reTransform()

            self.behavior = forId(self.id)
        }
    }

    override fun onDropTableRolled(
        self: NPC,
        killer: Entity,
        drops: ArrayList<Item>,
    ) {
        drops.clear()
    }

    override fun getClippingSupplier(self: NPC): ClipMaskSupplier {
        return ImplingClipper
    }

    private fun isPuroImpling(self: NPC): Boolean {
        return self.id in intArrayOf(6055, 6057, 6058, 6059, 6060, 6063, 6064, 6061, 7846)
    }
}
