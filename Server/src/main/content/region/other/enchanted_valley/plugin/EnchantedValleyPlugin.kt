package content.region.other.enchanted_valley.plugin

import content.data.skill.SkillingTool
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery
import kotlin.math.ceil

/**
 * Handles interaction around Enchanted valley region.
 */
class EnchantedValleyPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles spawn of River troll.
         */

        on(ENCHANTED_V_FISH, IntType.NPC, "lure", "bait") { player, node ->
            val option = getUsedOption(player)

            if (option == "bait" && !inInventory(player, Items.FISHING_ROD_307)) {
                sendDialogue(player, "You need a net to bait these fish.")
                return@on true
            }

            if (option == "lure" && !inInventory(player, Items.SMALL_FISHING_NET_303)) {
                sendDialogue(player, "You need a small net to catch these fish.")
                return@on true
            }

            if (player.getAttribute<Boolean>("enchanted_valley_npc") == true) {
                sendMessage(player, "You already have a creature spawned.")
                return@on true
            }

            sendMessage(player, "You cast out your net...")

            if (player.viewport.region?.id == 12102) {
                val npc = EnchantedValleyNPC.getRandom(RIVER_TROLL_IDS, node.location, player)
                npc.init()
            } else sendMessage(player, "Nothing interesting happens.")

            return@on true
        }

        /*
         * Handles spawn of Rock golem.
         */

        on(ENCHANTED_V_ROCK, IntType.SCENERY, "mine", "prospect") { player, _ ->
            val tool = SkillingTool.getPickaxe(player)
            if (tool == null) {
                sendDialogueLines(
                    player,
                    "You need a pickaxe to mine this rock. You do not have a pickaxe",
                    "which you have the Mining level to use."
                )
                return@on true
            }

            if (getUsedOption(player) != "prospect") {
                sendMessage(player, "You swing your pickaxe at the rock.")
                player.animate(Animation(tool.animation))
            }
            else
                sendMessage(player, "You examine the rock for ores...")

            if (!inBorders(player, 3023, 4491, 3029, 4494)) {
                sendMessage(player, "Nothing interesting happens.")
                return@on true
            }

            if (player.getAttribute<Boolean>("enchanted_valley_npc") == true) {
                sendMessage(player, "You already have a creature spawned.")
                return@on true
            }

            val npc = EnchantedValleyNPC.getRandom(ROCK_GOLEM_IDS, player.location, player)
            npc.init()
            return@on true
        }

        /*
         * Handles spawn of Tree spirit.
         */

        on(ENCHANTED_V_TREE, IntType.SCENERY, "chop-down") { player, _ ->
            val tool = SkillingTool.getAxe(player)
            if (tool == null) {
                sendDialogue(player, "You lack an axe which you have the Woodcutting level to use.")
                return@on true
            }

            sendMessage(player, "You swing your axe at the tree.")
            player.animate(Animation(tool.animation))

            if (player.getAttribute<Boolean>("enchanted_valley_npc") == true) {
                sendMessage(player, "You already have a creature spawned.")
                return@on true
            }

            val npc = EnchantedValleyNPC.getRandom(TREE_SPIRIT_IDS, player.location, player)
            npc.init()
            return@on true
        }
    }

    companion object {
        /**
         * The scenery used for spawning tree spirit.
         */
        private const val ENCHANTED_V_TREE = Scenery.TREE_16265

        /**
         * The scenery used for spawning rock golem.
         */
        private val ENCHANTED_V_ROCK = intArrayOf(Scenery.ROCKS_31060, Scenery.ROCKS_31059)

        /**
         * The scenery used for spawning river troll.
         */
        private const val ENCHANTED_V_FISH = NPCs.FISHING_SPOT_1189

        /**
         * The river troll npc ids.
         */
        val RIVER_TROLL_IDS = intArrayOf(
            NPCs.RIVER_TROLL_391, NPCs.RIVER_TROLL_392, NPCs.RIVER_TROLL_393,
            NPCs.RIVER_TROLL_394, NPCs.RIVER_TROLL_395, NPCs.RIVER_TROLL_396
        )

        /**
         * The rock golem npc ids.
         */
        val ROCK_GOLEM_IDS = intArrayOf(
            NPCs.ROCK_GOLEM_413, NPCs.ROCK_GOLEM_414, NPCs.ROCK_GOLEM_415,
            NPCs.ROCK_GOLEM_416, NPCs.ROCK_GOLEM_417, NPCs.ROCK_GOLEM_418
        )

        /**
         * The tree spirit npc ids.
         */
        val TREE_SPIRIT_IDS = intArrayOf(
            NPCs.TREE_SPIRIT_438, NPCs.TREE_SPIRIT_439, NPCs.TREE_SPIRIT_440,
            NPCs.TREE_SPIRIT_441, NPCs.TREE_SPIRIT_442, NPCs.TREE_SPIRIT_443
        )
    }

    /**
     * Represents the Enchanted valley NPCs.
     */
    class EnchantedValleyNPC(id: Int, spawnLoc: Location? = null, private val p: Player) : AbstractNPC(id, spawnLoc) {

        override fun init() {
            if (p.getAttribute<Boolean>("enchanted_valley_npc") != true) {
                p.setAttribute("enchanted_valley_npc", true)

                p.logoutListeners["enchanted_valley_npc"] = { pl ->
                    if (isActive) clear()
                    pl.removeAttribute("enchanted_valley_npc")
                }

                location = location ?: Location.getRandomLocation(p.location, 1, true)
                isRespawn = false
                isAggressive = true

                initSpawn()
                super.init()
            }
        }

        private fun initSpawn() {
            when (id) {
                in RIVER_TROLL_IDS -> {
                    visualize(this, -1, shared.consts.Graphics.WATER_SPLASH_68)
                    p.animate(Animation(1441, Animator.Priority.HIGH))
                    forceMove(p, p.location, p.location.transform(0, -1, 0), 0, 60, Direction.NORTH)
                    sendChat(this, if (hasRequirement(p, Quests.SWAN_SONG, false))
                        "You killed da Sea Troll Queen - you die now!"
                    else "Fishies be mine, leave dem fishies!")
                }

                in ROCK_GOLEM_IDS -> {
                    visualize(this, -1, shared.consts.Graphics.RE_PUFF_86)
                    sendChat(this, "Gerroff da rock!")
                }

                in TREE_SPIRIT_IDS -> {
                    visualize(this, -1, Graphics(shared.consts.Graphics.BIND_IMPACT_179, 100))
                    sendChat(this, "Leave these woods and never return!")
                }
            }

            this.moveStep()
            attack(p)
        }

        override fun finalizeDeath(killer: Entity) {
            super.finalizeDeath(killer)
            if (killer is Player) {
                p.removeAttribute("enchanted_valley_npc")
            }
        }

        override fun construct(id: Int, location: Location, vararg objects: Any?): AbstractNPC =
            EnchantedValleyNPC(id, location, p)

        override fun getIds(): IntArray = when (id) {
            in RIVER_TROLL_IDS -> RIVER_TROLL_IDS
            in ROCK_GOLEM_IDS -> ROCK_GOLEM_IDS
            in TREE_SPIRIT_IDS -> TREE_SPIRIT_IDS
            else -> intArrayOf(id)
        }

        companion object {
            fun getRandom(ids: IntArray, loc: Location, owner: Player): EnchantedValleyNPC {
                val index = (ceil(owner.properties.currentCombatLevel / 20.0).toInt()).coerceAtMost(ids.lastIndex)
                return EnchantedValleyNPC(ids[index], loc, owner)
            }
        }
    }
}
