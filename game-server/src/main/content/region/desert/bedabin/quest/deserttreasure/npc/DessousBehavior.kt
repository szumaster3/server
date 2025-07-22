package content.region.desert.bedabin.quest.deserttreasure.npc

import content.region.desert.bedabin.quest.deserttreasure.DTUtils
import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.QueueStrength
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.CombatSwingHandler
import core.game.node.entity.combat.MultiSwingHandler
import core.game.node.entity.combat.equipment.SwitchAttack
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

class DessousBehavior : NPCBehavior(NPCs.DESSOUS_1914, NPCs.DESSOUS_1915) {
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
        if (self.id == NPCs.DESSOUS_1915 && self.properties.combatPulse.isInCombat) {
            animate(self, Animation(1914))
        }

        val player: Player? = getAttribute<Player?>(self, "target", null)
        if (self.id == NPCs.DESSOUS_1914 && player != null && player.prayer.get(PrayerType.PROTECT_FROM_MELEE)) {
            self.transform(NPCs.DESSOUS_1915)
            Graphics.send(
                Graphics(
                    org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                ),
                self.location,
            )
        } else if (self.id == NPCs.DESSOUS_1915 &&
            player != null &&
            (
                player.prayer.get(PrayerType.PROTECT_FROM_MAGIC) ||
                    player.prayer.get(
                        PrayerType.PROTECT_FROM_MISSILES,
                    )
            )
        ) {
            self.transform(NPCs.DESSOUS_1914)
            Graphics.send(
                Graphics(
                    org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                ),
                self.location,
            )
        }
        if (clearTime++ > 800) {
            self.transform(NPCs.DESSOUS_1914)
            clearTime = 0
            if (player != null) {
                removeAttribute(player, DesertTreasure.attributeDessousInstance)
                sendMessage(player, "Dessous returns to his grave, bored of toying with you.")
            }
            poofClear(self)
        }
        return false
    }

    override fun getSwingHandlerOverride(
        self: NPC,
        original: CombatSwingHandler,
    ): CombatSwingHandler {
        if (self.id == NPCs.DESSOUS_1915) {
            return CombatHandler()
        } else {
            return original
        }
    }

    override fun beforeAttackFinalized(
        self: NPC,
        victim: Entity,
        state: BattleState,
    ) {
        if (victim is Player) {
            if (victim.location.getDistance(self.location) >= 5) {
                Graphics.send(
                    Graphics(
                        org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                    ),
                    self.location,
                )
                self.properties.teleportLocation = victim.location
                Graphics.send(
                    Graphics(
                        org.rs.consts.Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86,
                    ),
                    self.location,
                )
            }
        }
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (killer is Player) {
            val player = killer
            if (DTUtils.getSubStage(player, DesertTreasure.bloodStage) == 2) {
                DTUtils.setSubStage(player, DesertTreasure.bloodStage, 3)
            }
            removeAttribute(player, DesertTreasure.attributeDessousInstance)
            openDialogue(
                player,
                object : DialogueFile() {
                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 ->
                                player(
                                    FaceAnim.ANGRY,
                                    "Well that's Dessous dead, but where is the Diamond he",
                                    "was supposed to have?",
                                ).also { stage++ }

                            1 ->
                                playerl(FaceAnim.ANGRY, "If Malak lied to me about it, he is going to pay!").also {
                                    stage = END_DIALOGUE
                                }
                        }
                    }
                },
            )
        }
    }

    class CombatHandler :
        MultiSwingHandler(
            SwitchAttack(CombatStyle.MAGIC.swingHandler, null),
            SwitchAttack(CombatStyle.RANGE.swingHandler, null),
        ) {
        override fun swing(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ): Int {
            if (entity is NPC && victim is Player) {
                val projectile =
                    Projectile.create(
                        victim.location.transform(Location(intArrayOf(3, -3).random(), intArrayOf(3, -3).random())),
                        victim.location,
                        350,
                        0,
                        0,
                        0,
                        60,
                        0,
                        255,
                    )

                state!!.estimatedHit = 5
                state.secondaryHit = 5
                queueScript(entity, 0, QueueStrength.STRONG) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(entity, "Hssssssssssss")
                            projectile.send()
                            return@queueScript delayScript(entity, entity.location.getDistance(victim.location).toInt())
                        }

                        1 -> {
                            return@queueScript stopExecuting(entity)
                        }

                        else -> return@queueScript stopExecuting(entity)
                    }
                }
            }
            return super.swing(entity, victim, state)
        }
    }
}
