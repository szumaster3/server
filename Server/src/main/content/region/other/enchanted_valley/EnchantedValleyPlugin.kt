package content.region.other.enchanted_valley

import content.data.skill.SkillingTool
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import shared.consts.*
import kotlin.math.ceil

class EnchantedValleyPlugin : InteractionListener {

    companion object {
        private const val ENCHANTED_V_TREE = Scenery.TREE_16265
        private const val ENCHANTED_V_ROCK = Scenery.ROCKS_31060
        private const val ENCHANTED_V_FISH = Scenery.FISHING_SPOT_1175

        private val TREE_SPIRIT_IDS = intArrayOf(
            NPCs.TREE_SPIRIT_438,
            NPCs.TREE_SPIRIT_439,
            NPCs.TREE_SPIRIT_440,
            NPCs.TREE_SPIRIT_441,
            NPCs.TREE_SPIRIT_442,
            NPCs.TREE_SPIRIT_443
        )
        private val ROCK_GOLEM_IDS = intArrayOf(
            NPCs.ROCK_GOLEM_413,
            NPCs.ROCK_GOLEM_414,
            NPCs.ROCK_GOLEM_415,
            NPCs.ROCK_GOLEM_416,
            NPCs.ROCK_GOLEM_417,
            NPCs.ROCK_GOLEM_418
        )
        private val RIVER_TROLL_IDS = intArrayOf(
            NPCs.RIVER_TROLL_391,
            NPCs.RIVER_TROLL_392,
            NPCs.RIVER_TROLL_393,
            NPCs.RIVER_TROLL_394,
            NPCs.RIVER_TROLL_395,
            NPCs.RIVER_TROLL_396
        )
    }

    override fun defineListeners() {

        /*
         * Handles spawn of River troll.
         * https://www.youtube.com/watch?v=QOBX51i2UZ8
         */

        on(ENCHANTED_V_FISH, IntType.SCENERY, "net") { player, _ ->
            if (!inInventory(player, Items.SMALL_FISHING_NET_303))
                sendDialogue(player, "You need a small net to catch these fish.")
             else
                sendMessage(player, "You cast out your net...")

            if (player.viewport.region!!.id == 12102) {
                player.animate(Animation(Animations.NET_FISHING_621))
                spawnEvent(player, getNpcFor(player, RIVER_TROLL_IDS)) { npc ->
                    visualize(npc, -1, Graphics.RE_PUFF_86)
                    val message = if (hasRequirement(player, Quests.SWAN_SONG)) {
                        "You killed da Sea Troll Queen - you die now!"
                    } else "Fishies be mine, leave dem fishies!"
                    sendChat(npc, message)
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles spawn of Rock golem.
         */

        on(ENCHANTED_V_ROCK, IntType.SCENERY, "mine", "prospect") { player, _ ->
            val tool = SkillingTool.getPickaxe(player)
            if (tool == null) {
                sendMessage(player, "You lack a pickaxe which you have the Mining level to use.")
            } else
                 if(getUsedOption(player) != "prospect") {
                     sendMessage(player, "You swing your pickaxe at the rock.")
                     player.animate(Animation(tool.animation))
                 }
             else
                 sendMessage(player, "You examine the rock for ores...")

            if (inBorders(player, 3023, 4491, 3029, 4494)) {
                spawnEvent(player, getNpcFor(player, ROCK_GOLEM_IDS)) { npc ->
                    visualize(npc, -1, Graphics.RE_PUFF_86)
                    sendChat(npc, "Gerroff da rock!")
                }
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles spawn of Tree spirit.
         */

        on(ENCHANTED_V_TREE, IntType.SCENERY, "chop-down") { player, _ ->
            val tool = SkillingTool.getAxe(player)
            if (tool == null) {
                sendMessage(player, "You lack an axe which you have the Woodcutting level to use.")
                return@on true
            }

            sendMessage(player, "You swing your axe at the tree.")
            player.animate(Animation(tool.animation))

            spawnEvent(player, getNpcFor(player, TREE_SPIRIT_IDS)) { npc ->
                visualize(npc, -1, Graphics.RE_PUFF_86)
            }

            return@on true
        }
    }

    /**
     * Generic NPC event spawner for the Enchanted Valley events.
     */
    private fun spawnEvent(player: Player, npc: NPC, preAttack: ((NPC) -> Unit)? = null) {
        player.pulseManager.run(object : Pulse(1) {
            var counter = 0
            override fun pulse(): Boolean {
                when (counter++) {
                    2 -> {
                        npc.location = player.location
                        npc.init()
                        npc.moveStep()
                        npc.isRespawn = false
                        preAttack?.invoke(npc)
                    }
                    3 -> npc.attack(player)
                }
                return false
            }
        })
    }

    /**
     * Gets the NPC for the player combat level.
     */
    private fun getNpcFor(player: Player, ids: IntArray): NPC {
        val index = (ceil(player.properties.currentCombatLevel / 20.0).toInt()).coerceAtMost(ids.lastIndex)
        return NPC(ids[index])
    }
}