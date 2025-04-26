package content.region.misthalin.handlers.abyss

import content.data.items.SkillingTool
import content.global.skill.runecrafting.Altar
import core.api.*
import core.api.skill.getTool
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.system.timer.impl.Skulled
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.Log
import core.tools.RandomFunction
import core.tools.colorize
import org.rs.consts.*

/**
 * Handles interactions within the Abyss area.
 */
class AbyssListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MAGE_OF_ZAMORAK_2259, IntType.NPC, "teleport") { player, node ->
            teleport(player, node as NPC)
            return@on true
        }

        on(NPCs.DARK_MAGE_2262, IntType.NPC, "repair-pouches") { player, node ->
            player.dialogueInterpreter.open(node.id, node, true)
            return@on true
        }

        on(IntType.SCENERY, "exit-through") { player, node ->
            val altar = Altar.forScenery(node as core.game.node.scenery.Scenery)
            altar?.enterRift(player)
            return@on true
        }

        on(Scenery.PASSAGE_7154, IntType.SCENERY, "go-through") { player, node ->
            player.properties.teleportLocation = innerRing(node)
            return@on true
        }

        on(Scenery.ROCK_7158, IntType.SCENERY, "mine") { player, node ->
            val tool: SkillingTool? = getTool(player, true)
            if (tool == null) {
                sendMessage(player, "You need a pickaxe in order to do that.")
                return@on true
            }
            return@on handleObstacle(
                node,
                player,
                Skills.MINING,
                MINE_PROGRESS,
                Animation(tool.animation),
                arrayOf(
                    "You attempt to mine your way through...",
                    "...and manage to break through the rock.",
                    "...but fail to break-up the rock.",
                ),
            )
        }

        on(Scenery.TENDRILS_7161, IntType.SCENERY, "chop") { player, node ->
            val tool: SkillingTool? = getTool(player, false)
            if (tool == null) {
                sendMessage(player, "You need an axe in order to do that.")
                return@on true
            }
            return@on handleObstacle(
                node,
                player,
                Skills.WOODCUTTING,
                CHOP_PROGRESS,
                Animation(tool.animation),
                arrayOf(
                    "You attempt to chop your way through...",
                    "...and manage to chop down the tendrils.",
                    "...but fail to cut through the tendrils.",
                ),
            )
        }

        on(Scenery.BOIL_7165, IntType.SCENERY, "burn-down") { player, node ->
            if (!inInventory(player, Items.TINDERBOX_590)) {
                sendMessage(player, "You don't have a tinderbox to burn it.")
                return@on true
            }
            return@on handleObstacle(
                node,
                player,
                Skills.FIREMAKING,
                BURN_PROGRESS,
                Animation(Animations.HUMAN_LIGHT_FIRE_WITH_TINDERBOX_733),
                arrayOf(
                    "You attempt to burn your way through...",
                    "...and manage to burn it down and get past.",
                    "...but fail to set it on fire.",
                ),
            )
        }

        on(Scenery.EYES_7168, IntType.SCENERY, "distract") { player, node ->
            val distractEmote = Animation(distractEmotes[RandomFunction.random(0, distractEmotes.size)])
            return@on handleObstacle(
                node,
                player,
                Skills.THIEVING,
                DISTRACT_PROGRESS,
                distractEmote,
                arrayOf(
                    "You use your thieving skills to misdirect the eyes...",
                    "...and sneak past while they're not looking.",
                    "...but fail to distract the eyes.",
                ),
            )
        }

        on(Scenery.GAP_7164, IntType.SCENERY, "squeeze-through") { player, node ->
            playAudio(player, Sounds.ABYSSAL_SQUEEZETHROUGH_2709)
            return@on handleObstacle(
                node,
                player,
                Skills.AGILITY,
                null,
                Animation(Animations.HUMAN_SQUEEZE_INTO_GAP_1331),
                arrayOf(
                    "You attempt to squeeze through the narrow gap...",
                    "...and you manage to crawl through.",
                    "...but fail to crawl through.",
                ),
            )
        }
    }

    private val distractEmotes =
        intArrayOf(
            Animations.NOD_HEAD_855,
            Animations.HUMAN_SHAKE_HEAD_NO_856,
            Animations.HUMAN_THINK_857,
            Animations.HUMAN_BOW_858,
            Animations.HUMAN_ANGRY_859,
            Animations.HUMAN_CRY_860,
            Animations.HUMAN_LAUGH_861,
            Animations.HUMAN_CHEER_862,
            Animations.HUMAN_WAVE_863,
            Animations.HUMAN_BECKON_864,
            Animations.HUMAN_CLAP_865,
            Animations.HUMAN_DANCE_866,
            Animations.HUMAN_SHRUG_2113,
            Animations.HUMAN_JUMP_FOR_JOY_2109,
            Animations.HUMAN_YAWN_2111,
            Animations.HUMAN_JIG_2106,
            Animations.HUMAN_TWIRL_2107,
            Animations.HUMAN_HEAD_BANG_2108,
            Animations.HUMAN_PANIC_2105,
            Animations.HUMAN_BLOW_RASPBERRY_2110,
            Animations.HUMAN_SALUTE_2112,
            Animations.HUMAN_GOBLIN_BOW_2127,
            Animations.HUMAN_GOBLIN_SALUTE_2128,
            Animations.HUMAN_GLASS_BOX_1131,
            Animations.HUMAN_CLIMB_ROPE_1130,
            Animations.HUMAN_LEAN_1129,
            Animations.HUMAN_GLASS_WALL_1128,
            Animations.HUMAN_ANGRY_STOMP_1745,
            Animations.HUMAN_ZOMBIE_WALK_3544,
            Animations.HUMAN_ZOMBIE_DANCE_3543,
            Animations.HUMAN_SCARED_2836
        )

    companion object {

        /**
         * Teleports the player to a random location within the Abyss, resetting their skull timer and draining Prayer points.
         *
         * @param player The player being teleported.
         * @param npc The Mage of Zamorak NPC casting the teleport.
         */
        fun teleport(
            player: Player,
            npc: NPC,
        ) {
            var teleportLoc = AbyssLocation.randomLocation()
            while (!teleportLoc.isValid()) {
                teleportLoc = teleportLoc.attract()
            }

            player.lock(3)
            npc.visualize(
                Animation(Animations.CAST_SPELL_1979),
                Graphics(org.rs.consts.Graphics.DELRIGHT_DEFEATED_4),
            )
            npc.sendChat("Veniens! Sallakar! Rinnesset!")
            player.skills.decrementPrayerPoints(100.0)
            removeTimer<Skulled>(player)
            registerTimer(player, spawnTimer<Skulled>(2000))
            GameWorld.Pulser.submit(
                object : Pulse(2, player) {
                    override fun pulse(): Boolean {
                        rotateObstacles(player, teleportLoc)
                        player.properties.teleportLocation = teleportLoc.toAbs()
                        npc.updateMasks.reset()
                        return true
                    }
                },
            )
        }

        /**
         * Calculates a random valid inner Abyss location near an obstacle.
         *
         * @param node The obstacle node the player interacted with.
         * @return A [Location] in the inner Abyss ring.
         */
        fun innerRing(node: Node): Location {
            val obstacleLoc = AbyssLocation.fromAbs(node.location)
            var loc = obstacleLoc.attract(5)
            while (!loc.isValid()) {
                loc = loc.attract()
            }
            return loc.toAbs()
        }

        /**
         * Rotates Abyss obstacle configurations by updating a player-specific varbit.
         *
         * @param player The player whose view of the obstacles will change.
         * @param abyssLoc The Abyss location affecting the new obstacle layout.
         */
        fun rotateObstacles(
            player: Player,
            abyssLoc: AbyssLocation,
        ) {
            setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, abyssLoc.getSegment(), true)
        }

        const val MINE_PROGRESS = 12
        const val CHOP_PROGRESS = 14
        const val BURN_PROGRESS = 16
        const val DISTRACT_PROGRESS = 18

        /**
         * Handles a multi-step obstacle interaction (mining, chopping, burning, distracting, etc.) within the Abyss.
         *
         * @param obstacle The obstacle node being interacted with.
         * @param player The player attempting the interaction.
         * @param skill The skill ID used for success calculation.
         * @param varbitVal Optional varbit to update upon successful obstacle clearing.
         * @param animation The animation to play during interaction.
         * @param messages Messages shown during the interaction stages (attempt, success, failure).
         * @return True if the interaction started successfully, false otherwise.
         */
        fun handleObstacle(
            obstacle: Node,
            player: Player,
            skill: Int,
            varbitVal: Int?,
            animation: Animation,
            messages: Array<String>,
        ): Boolean {
            log(this::class.java, Log.FINE, "handled abyss ${obstacle.name}")
            player.lock()
            player.animate(animation)
            GameWorld.Pulser.submit(
                object : Pulse(1, player) {
                    var count = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            1 -> sendMessage(player, messages[0])
                            3 ->
                                return if (RandomFunction.random(100) < getStatLevel(player, skill) + 1) {
                                    sendMessage(player, colorize("%G${messages[1]}"))
                                    if (varbitVal != null) {
                                        setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, varbitVal)
                                    }
                                    false
                                } else {
                                    sendMessage(player, colorize("%R${messages[2]}"))
                                    player.unlock()
                                    true
                                }

                            5 -> {
                                if (varbitVal != null) {
                                    setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, varbitVal or 1)
                                }
                            }

                            7 -> {
                                player.unlock()
                                player.properties.teleportLocation = innerRing(obstacle)
                                return true
                            }
                        }
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                },
            )
            return true
        }
    }
}
