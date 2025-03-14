package core.game.worldevents.events.halloween.randoms

import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.worldevents.events.HolidayRandomEventNPC
import core.game.worldevents.events.HolidayRandoms
import core.game.worldevents.events.ResetHolidayAppearance
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Graphics
import org.rs.consts.NPCs
import org.rs.consts.Sounds

class WitchHolidayRandomNPC : HolidayRandomEventNPC(NPCs.WITCH_611) {
    override fun init() {
        super.init()
        when (RandomFunction.getRandom(4)) {
            0 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Brackium Emendo!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.CONFUSE_CAST_AND_FIRE_119)
                            animate(this, Animations.CAST_SPELL_1978)
                            spawnProjectile(this, player, Graphics.CURSE_PROJECTILE_109)
                            return@queueScript delayScript(this, 2)
                        }

                        1 -> {
                            player.appearance.transformNPC(NPCs.ENAKHRA_3138)
                            playAudio(player, Sounds.SKELETON_RESURRECT_1687)
                            registerTimer(player, ResetHolidayAppearance())
                            return@queueScript delayScript(this, 4)
                        }

                        2 -> {
                            sendChat(this, "That was not right...")
                            visualize(this, Animations.THINK_857, -1)
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }

                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }

            1 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Bombarda!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.CURSE_CAST_AND_FIRE_127)
                            animate(this, Animations.CAST_SPELL_1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }

                        1 -> {
                            playGlobalAudio(player.location, Sounds.EXPLOSION_1487)
                            visualize(player, -1, Graphics.EXPLOSION_659)
                            val hit = if (player.skills.lifepoints < 5) 0 else 2
                            impact(player, hit, ImpactHandler.HitsplatType.NORMAL)
                            return@queueScript delayScript(this, 2)
                        }

                        2 -> {
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }

                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }

            2 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Tarantallegra!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.WEAKEN_CAST_AND_FIRE_3011)
                            animate(this, Animations.CAST_SPELL_1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }

                        1 -> {
                            animate(player, Animations.HUMAN_ZOMBIE_DANCE_3543, true)
                            sendMessage(player, "You suddenly burst into dance.")
                            return@queueScript delayScript(this, 2)
                        }

                        2 -> {
                            visualize(this, Animations.LAUGH_861, -1)
                            playGlobalAudio(this.location, Sounds.HUMAN_LAUGH_1_3071)
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }

                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }

            3 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Vespertilio!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.WEAKEN_CAST_AND_FIRE_3011)
                            animate(this, Animations.CAST_SPELL_1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }

                        1 -> {
                            visualize(player, Animations.HUMAN_TRICK_10530, Graphics.TRICK_1863)
                            playGlobalAudio(player.location, Sounds.VAMPIRE_SUMMON_1899)
                            return@queueScript delayScript(this, 4)
                        }

                        2 -> {
                            player.appearance.transformNPC(NPCs.VAMPIRE_BAT_6835)
                            HolidayRandoms.terminateEventNpc(player)
                            registerTimer(player, ResetHolidayAppearance())
                            return@queueScript stopExecuting(this)
                        }

                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }

            4 -> {
                queueScript(this, 6, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            sendChat(this, "Sella!")
                            this.face(player)
                            playGlobalAudio(this.location, Sounds.WEAKEN_CAST_AND_FIRE_3011)
                            animate(this, Animations.CAST_SPELL_1978)
                            spawnProjectile(this, player, 109)
                            return@queueScript delayScript(this, 2)
                        }

                        1 -> {
                            player.appearance.transformNPC(NPCs.WITCH_3293)
                            playGlobalAudio(player.location, Sounds.KR_JUDGE_HAMMER_3822)
                            registerTimer(player, ResetHolidayAppearance())
                            return@queueScript delayScript(this, 4)
                        }

                        2 -> {
                            HolidayRandoms.terminateEventNpc(player)
                            return@queueScript stopExecuting(this)
                        }

                        else -> return@queueScript stopExecuting(this)
                    }
                }
            }
        }
    }

    override fun talkTo(npc: NPC) {
    }
}
