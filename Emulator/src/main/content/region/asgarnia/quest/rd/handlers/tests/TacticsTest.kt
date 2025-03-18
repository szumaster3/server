package content.region.asgarnia.quest.rd.handlers.tests

import content.region.asgarnia.quest.rd.RecruitmentDrive
import content.region.asgarnia.quest.rd.handlers.RDUtils.ATTRIBUTE_NPC_SPAWN
import core.api.*
import core.game.dialogue.DialogueBuilder
import core.game.dialogue.DialogueBuilderFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.NPCs

class TacticsTest(
    private val dialogueNum: Int = 0,
) : DialogueBuilderFile() {
    companion object {
        const val spawnSirLeye = "rd:generatedsirleye"
    }

    override fun create(builder: DialogueBuilder) {
        builder.apply {
            onPredicate { player -> getAttribute(player, RecruitmentDrive.stagePass, false) }
                .npc(
                    FaceAnim.HAPPY,
                    "Excellent work, @name!",
                    "Please step through the portal to meet your next",
                    "challenge.",
                ).end()

            onPredicate { true }
                .npc(
                    "Ah, @name, you're finally here.",
                    "Your task for this room is to defeat Sir Leye.",
                    "He has been blessed by Saradomin to be undefeatable",
                    "by any man, so it should be quite the challenge for you.",
                ).npc(
                    "If you are having problems, remember",
                    "A true warrior uses his wits as much as his brawn.",
                    "Fight smarter, not harder.",
                ).endWith { _, player ->
                    SirLeyeNPC.init(player)
                }
        }
    }
}

class SirLeyeNPC : NPCBehavior(NPCs.SIR_LEYE_2285) {
    private var clearTime = 0

    override fun tick(self: NPC): Boolean {
        if (++clearTime > 288) {
            clearTime = 0
            poofClear(self)
        }
        return true
    }

    override fun beforeDamageReceived(
        self: NPC,
        attacker: Entity,
        state: BattleState,
    ) {
        val lifepoints = self.skills.lifepoints
        if (attacker is Player) {
            if (attacker.isMale) {
                if (state.estimatedHit + Integer.max(state.secondaryHit, 0) > lifepoints - 1) {
                    self.skills.lifepoints = self.getSkills().getStaticLevel(Skills.HITPOINTS)
                }
            }
        }
    }

    override fun onDeathFinished(
        self: NPC,
        killer: Entity,
    ) {
        if (killer is Player) {
            clearHintIcon(killer)
            setAttribute(killer, RecruitmentDrive.stagePass, true)
            openDialogue(killer, TacticsTest(1), NPC(NPCs.SIR_KUAM_FERENTSE_2284))
            removeAttribute(killer, TacticsTest.spawnSirLeye)
        }
    }

    override fun getXpMultiplier(
        self: NPC,
        attacker: Entity,
    ): Double = 0.0

    companion object {
        @JvmStatic
        fun init(player: Player) {
            val boss = NPC.create(NPCs.SIR_LEYE_2285, Location.create(2457, 4966, 0))
            boss.isWalks = true
            boss.isAggressive = true
            boss.isActive = false

            if (boss.asNpc() != null && boss.isActive) {
                boss.properties.teleportLocation = boss.properties.spawnLocation
            }
            boss.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, boss) {
                    override fun pulse(): Boolean {
                        boss.init()
                        sendChat(boss, "No man may defeat me!")
                        registerHintIcon(player, boss)
                        boss.attack(player)
                        setAttribute(player, ATTRIBUTE_NPC_SPAWN, true)
                        return true
                    }
                },
            )
        }
    }
}
