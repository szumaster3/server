package content.global.skill.hunter.pitfall

import content.global.skill.hunter.HunterManager
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Sounds
import java.util.concurrent.TimeUnit

class PitfallListeners : InteractionListener {
    private val knife = Items.KNIFE_946
    private val teasingStick = Items.TEASING_STICK_10029
    private val logs = Items.LOGS_1511

    private val pitIds =
        listOf(
            org.rs.consts.Scenery.PIT_19227,
            org.rs.consts.Scenery.SPIKED_PIT_19228,
            org.rs.consts.Scenery.COLLAPSED_TRAP_19231,
            org.rs.consts.Scenery.COLLAPSED_TRAP_19232,
            org.rs.consts.Scenery.COLLAPSED_TRAP_19233,
        )

    override fun defineListeners() {
        setDest(IntType.SCENERY, pitIds.toIntArray(), "trap", "jump", "dismantle") { player, node ->
            val pit = node as Scenery
            var dst = getBestJumpSpot(player.location, pit)
            return@setDest dst
        }

        on(org.rs.consts.Scenery.PIT_19227, IntType.SCENERY, "trap") { player, node ->
            val pit = node as Scenery
            if (getStatLevel(player, Skills.HUNTER) < 31) {
                sendMessage(player, "You need a hunter level of 31 to set a pitfall trap.")
                return@on true
            }

            val maxTraps = HunterManager.getInstance(player).maximumTraps
            if (getAttribute(player, "pitfall:count", 0) >= maxTraps) {
                sendMessage(player, "You can't set up more than $maxTraps pitfall traps at your hunter level.")
                return@on true
            }
            player.incrementAttribute("pitfall:count", 1)

            if (!inInventory(player, knife) || !removeItem(player, logs)) {
                sendMessage(player, "You need some logs and a knife to set a pitfall trap.")
                return@on true
            }

            setAttribute(player, "pitfall:timestamp:${pit.location.x}:${pit.location.y}", System.currentTimeMillis())
            setPitState(player, pit.location, 1)
            playAudio(player, Sounds.HUNTING_PLACEBRANCHES_2639)

            val collapsePulse =
                object : Pulse(201, player) {
                    override fun pulse(): Boolean {
                        val lastTimestamp =
                            player.getAttribute(
                                "pitfall:timestamp:${pit.location.x}:${pit.location.y}",
                                System.currentTimeMillis(),
                            )
                        if (System.currentTimeMillis() - lastTimestamp >= TimeUnit.MINUTES.toMillis(2)) {
                            sendMessage(player, "Your pitfall trap has collapsed.")
                            setPitState(player, pit.location, 0)
                            player.incrementAttribute("pitfall:count", -1)
                        }
                        return true
                    }
                }
            submitWorldPulse(collapsePulse)
            return@on true
        }

        on(org.rs.consts.Scenery.SPIKED_PIT_19228, IntType.SCENERY, "jump") { player, node ->
            val pit = node as Scenery
            val src = player.location
            val dir = Pitfall.pitJumpSpots(pit.location)!![src]
            if (dir != null) {
                val dst = src.transform(dir, 3)
                ForceMovement.run(
                    player,
                    src,
                    dst,
                    ForceMovement.WALK_ANIMATION,
                    Animation(Animations.JUMP_WEREWOLF_1603),
                    dir,
                    16,
                )
                playAudio(player, Sounds.HUNTING_JUMP_2635)

                val pitfallNpc: Entity? = player.getAttribute("pitfall_npc", null)
                pitfallNpc?.let {
                    handlePitfallNpcJump(player, it, pit, src, dst, dir)
                }
            }
            return@on true
        }

        on(org.rs.consts.Scenery.SPIKED_PIT_19228, IntType.SCENERY, "dismantle") { player, node ->
            dismantlePit(player, node as Scenery)
            return@on true
        }

        handleDismantlePit(
            pitId = org.rs.consts.Scenery.COLLAPSED_TRAP_19232,
            goodFur = Items.LARUPIA_FUR_10095,
            badFur = Items.TATTY_LARUPIA_FUR_10093,
            name = "larupia",
            xp = 180.0,
        )
        handleDismantlePit(
            pitId = org.rs.consts.Scenery.COLLAPSED_TRAP_19231,
            goodFur = Items.GRAAHK_FUR_10099,
            badFur = Items.TATTY_GRAAHK_FUR_10097,
            name = "graahk",
            xp = 240.0,
        )
        handleDismantlePit(
            pitId = org.rs.consts.Scenery.COLLAPSED_TRAP_19233,
            goodFur = Items.KYATT_FUR_10103,
            badFur = Items.TATTY_KYATT_FUR_10101,
            name = "kyatt",
            xp = 300.0,
        )

        on(Pitfall.BEAST_IDS, IntType.NPC, "tease") { player, node ->
            val entity = node as Entity
            val hunterReq = Pitfall.HUNTER_REQS[entity.name] ?: return@on true
            if (getStatLevel(player, Skills.HUNTER) < hunterReq) {
                sendMessage(player, "You need a hunter level of $hunterReq to hunt ${entity.name.lowercase()}s.")
                return@on true
            }
            if (!inInventory(player, teasingStick)) {
                sendMessage(player, "You need a teasing stick to hunt ${entity.name.lowercase()}s.")
                return@on true
            }
            entity.attack(player)
            playAudio(player, Sounds.HUNTING_TEASE_FELINE_2651)
            setAttribute(player, "pitfall_npc", entity)
            return@on true
        }
    }

    private fun getBestJumpSpot(
        src: Location,
        pit: Scenery,
    ): Location {
        val locs = Pitfall.pitJumpSpots(pit.location)
        var dst = pit.location
        locs?.forEach { (loc, _) ->
            if (src.getDistance(loc) <= src.getDistance(dst)) {
                dst = loc
            }
        }
        return dst
    }

    private fun handlePitfallNpcJump(
        player: Player,
        pitfallNpc: Entity,
        pit: Scenery,
        src: Location,
        dst: Location,
        dir: Direction,
    ) {
        if (pitfallNpc.location.getDistance(src) < 3.0) {
            val lastPitLoc: Location? = pitfallNpc.getAttribute("last_pit_loc", null)
            if (lastPitLoc == pit.location) {
                sendMessage(player, "The ${pitfallNpc.name.lowercase()} won't jump the same pit twice in a row.")
                return
            }
            val chance = RandomFunction.getSkillSuccessChance(50.0, 100.0, player.skills.getLevel(Skills.HUNTER))
            if (RandomFunction.random(0.0, 100.0) < chance) {
                teleport(pitfallNpc, pit.location)
                removeAttribute(pitfallNpc, "last_pit_loc")
                playAudio(player, Sounds.HUNTING_PITFALL_COLLAPSE_2638, 0, 1, pit.location, 10)
                playAudio(player, Sounds.PANTHER_DEATH_667, 50, 1, pit.location, 10)
                pitfallNpc.startDeath(null)
                removeAttribute(player, "pitfall:timestamp:${pit.location.x}:${pit.location.y}")
                player.incrementAttribute("pitfall:count", -1)
                setPitState(player, pit.location, 3)
            } else {
                val npcDestination = dst.transform(dir, if (dir == Direction.SOUTH || dir == Direction.WEST) 1 else 0)
                teleport(pitfallNpc, npcDestination)
                animate(pitfallNpc, Animation(5232, Animator.Priority.HIGH))
                playAudio(player, Sounds.HUNTING_BIGCAT_JUMP_2619, 0, 1, pit.location, 10)
                pitfallNpc.attack(player)
                setAttribute(pitfallNpc, "last_pit_loc", pit.location)
            }
        }
    }

    private fun dismantlePit(
        player: Player,
        pit: Scenery,
    ) {
        playAudio(player, Sounds.HUNTING_TAKEBRANCHES_2649)
        removeAttribute(player, "pitfall:timestamp:${pit.location.x}:${pit.location.y}")
        player.incrementAttribute("pitfall:count", -1)
        setPitState(player, pit.location, 0)
    }

    private fun handleDismantlePit(
        pitId: Int,
        goodFur: Int,
        badFur: Int,
        name: String,
        xp: Double,
    ) {
        on(pitId, IntType.SCENERY, "dismantle") { player, node ->
            lootCorpse(player, node as Scenery, xp, goodFur, badFur)
            sendMessage(player, "You've caught a $name!")
            return@on true
        }
    }

    private fun lootCorpse(
        player: Player,
        pit: Scenery,
        xp: Double,
        goodFur: Int,
        badFur: Int,
    ) {
        if (freeSlots(player) < 2) {
            sendMessage(player, "You don't have enough inventory space. You need 2 more free slots.")
            return
        }
        setPitState(player, pit.location, 0)
        rewardXP(player, Skills.HUNTER, xp)
        addItemOrDrop(player, Items.BIG_BONES_532)
        playAudio(player, Sounds.HUNTING_TAKEBRANCHES_2649)

        val chance = RandomFunction.getSkillSuccessChance(50.0, 100.0, getStatLevel(player, Skills.HUNTER))
        val furItem = if (RandomFunction.random(0.0, 100.0) < chance) goodFur else badFur
        addItemOrDrop(player, furItem)
    }

    private fun setPitState(
        player: Player,
        loc: Location,
        state: Int,
    ) {
        val pit = Pitfall.pitVarps[loc] ?: return
        setVarbit(player, pit.varbitId, state)
    }
}
