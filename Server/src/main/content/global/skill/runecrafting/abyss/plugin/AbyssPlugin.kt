package content.global.skill.runecrafting.abyss.plugin

import content.data.skill.SkillingTool
import content.global.skill.runecrafting.Altar
import content.global.skill.runecrafting.abyss.dialogue.DarkMageDialogue
import content.global.skill.runecrafting.abyss.dialogue.MageOfZamorakDialogue
import content.global.skill.runecrafting.abyss.npc.AbyssalNPC
import core.api.*
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
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.tools.Log
import core.tools.RandomFunction
import core.tools.colorize
import shared.consts.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Handles interactions within the Abyss area.
 * @author lila
 * @author cfunny
 */
class AbyssPlugin : InteractionListener {

    override fun defineListeners() {
        definePlugin(AbyssalNPC())
        definePlugin(DarkMageDialogue())
        definePlugin(MageOfZamorakDialogue())

        /*
         * Handles teleport interaction with the Mage of Zamorak NPC.
         */

        on(NPCs.MAGE_OF_ZAMORAK_2259, IntType.NPC, "teleport") { player, node ->
            teleport(player, node as NPC)
            return@on true
        }

        /*
         * Opens the repair pouches dialogue with the Dark Mage NPC.
         */

        on(NPCs.DARK_MAGE_2262, IntType.NPC, "repair-pouches") { player, node ->
            player.dialogueInterpreter.open(node.id, node, true)
            return@on true
        }

        /*
         * Handles exiting through an altar scenery object.
         */

        on(IntType.SCENERY, "exit-through") { player, node ->
            val altar = Altar.forScenery(node as core.game.node.scenery.Scenery)
            altar?.enterRift(player)
            return@on true
        }

        /*
         * Handles moving through a passage into the inner ring.
         */

        on(Scenery.PASSAGE_7154, IntType.SCENERY, "go-through") { player, node ->
            player.properties.teleportLocation = innerRing(node)
            return@on true
        }

        /*
         * Handles mining a rock obstacle.
         * Requires a pickaxe in the inventory.
         */

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

        /*
         * Handles chopping tendrils obstacle.
         * Requires an axe in the inventory.
         */

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

        /*
         * Handles burning down the boil obstacle.
         * Requires a tinderbox in the inventory.
         */

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

        /*
         * Handles distracting magical eyes using thieving skill.
         */

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

        /*
         * Handles squeezing through a gap obstacle.
         */

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

    private val distractEmotes = intArrayOf(
        Animations.HUMAN_SHAKE_HEAD_YES_855,
        Animations.HUMAN_SHAKE_HEAD_NO_856,
        Animations.HUMAN_THINK_857,
        Animations.BOW_858,
        Animations.ANGRY_859,
        Animations.CRY_860,
        Animations.HUMAN_LAUGH_861,
        Animations.CHEER_862,
        Animations.HUMAN_WAVE_863,
        Animations.BECKON_864,
        Animations.CLAP_865,
        Animations.DANCE_866,
        Animations.HUMAN_SHRUG_2113,
        Animations.HUMAN_JUMP_FOR_JOY_2109,
        Animations.HUMAN_YAWN_2111,
        Animations.HUMAN_JIG_2106,
        Animations.HUMAN_TWIRL_2107,
        Animations.HEADBANG_EMOTE_2108,
        Animations.HUMAN_PANIC_2105,
        Animations.HUMAN_BLOW_RASPBERRY_2110,
        Animations.HUMAN_SALUTE_2112,
        Animations.GOBLIN_BOW_EMOTE_E_2127,
        Animations.GOBLIN_SALUTE_EMOTE_E_2128,
        Animations.GLASS_BOX_EMOTE_1131,
        Animations.CLIMB_ROPE_EMOTE_1130,
        Animations.HUMAN_LEAN_1129,
        Animations.GLASS_WALL_EMOTE_1128,
        Animations.ANGRY_STOMPING_1745,
        Animations.HUMAN_ZOMBIE_WALK_3544,
        Animations.HUMAN_ZOMBIE_DANCE_3543,
        Animations.HUMAN_SCARED_2836
    )

    companion object {

        /**
         * Teleports the player to a random valid Abyss location.
         */
        fun teleport(player: Player, npc: NPC) {
            var teleportLoc = AbyssLocation.randomLocation()
            while (!teleportLoc.isValid()) {
                teleportLoc = teleportLoc.attract()
            }

            player.lock(3)
            npc.visualize(
                Animation(Animations.CAST_SPELL_1979),
                Graphics(shared.consts.Graphics.DELRIGHT_DEFEATED_4),
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
         * Calculates a valid inner Abyss location near a given obstacle.
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
         * Updates the player's view of Abyss obstacles based on their location.
         */
        fun rotateObstacles(player: Player, abyssLoc: AbyssLocation) {
            setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, abyssLoc.getSegment(), true)
        }

        const val MINE_PROGRESS = 12
        const val CHOP_PROGRESS = 14
        const val BURN_PROGRESS = 16
        const val DISTRACT_PROGRESS = 18

        /**
         * Handles an Abyss obstacle interaction.
         */
        fun handleObstacle(obstacle: Node, player: Player, skill: Int, varbitVal: Int?, animation: Animation, messages: Array<String>): Boolean {
            log(this::class.java, Log.FINE, "handled abyss ${obstacle.name}")
            player.lock()
            player.animate(animation)
            GameWorld.Pulser.submit(
                object : Pulse(1, player) {
                    var count = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            1 -> sendMessage(player, messages[0])
                            3 -> return if (RandomFunction.random(100) < getStatLevel(player, skill) + 1) {
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

/**
 * Represents a polar-coordinate location within the Abyss.
 */
class AbyssLocation(val radius: Double, val angle: Double) {
    /**
     * Moves the location closer to the Abyss center.
     *
     * @param steps Steps inward (default 1).
     * @return A new [AbyssLocation] closer to center.
     */
    fun attract(steps: Int = 1): AbyssLocation = AbyssLocation(radius - steps.toDouble(), angle)

    /**
     * Computes the obstacle segment id for this location.
     *
     * @return Segment id (0–11).
     */
    fun getSegment(): Int {
        val segments = 12
        val angleToCircle = angle * segments / (2 * Math.PI)
        val angleSegment = (angleToCircle + 0.5).toInt()

        val normalSegment = (9 - angleSegment).mod(12)
        return normalSegment
    }

    /**
     * Converts to an absolute [Location].
     */
    fun toAbs(): Location {
        val x = (radius * cos(angle)).toInt()
        val y = (radius * sin(angle)).toInt()
        return origin.transform(x, y, 0)
    }

    /**
     * Checks if this location is valid for teleportation.
     */
    fun isValid(): Boolean {
        val abs = toAbs()
        return (RegionManager.isTeleportPermitted(abs) && RegionManager.getObject(abs) == null)
    }

    companion object {
        val origin = Location(3039, 4832, 0)

        const val outerRadius = 25.1

        /**
         * Creates an [AbyssLocation] from an absolute [Location].
         */
        fun fromAbs(loc: Location): AbyssLocation {
            val local = Location.getDelta(origin, loc)
            val radius = Math.sqrt((local.x * local.x + local.y * local.y).toDouble())
            val angle = Math.atan2(local.y.toDouble(), local.x.toDouble())
            return AbyssLocation(radius, angle)
        }

        /**
         * Generates a random outer Abyss [AbyssLocation].
         */
        fun randomLocation(): AbyssLocation {
            val angle = Random.nextDouble() * 2 * Math.PI
            return AbyssLocation(outerRadius, angle)
        }
    }
}