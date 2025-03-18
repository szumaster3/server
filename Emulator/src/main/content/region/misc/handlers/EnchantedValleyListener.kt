package content.region.misc.handlers

import content.data.items.SkillingTool
import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import org.rs.consts.*
import kotlin.math.ceil

class EnchantedValleyListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles spawn of River troll.
         * https://www.youtube.com/watch?v=QOBX51i2UZ8
         */

        on(ENCHANTED_V_FISH, IntType.SCENERY, "net") { player, _ ->
            if (!inInventory(player, Items.SMALL_FISHING_NET_303)) {
                sendDialogue(
                    player,
                    "You need a small net to catch these fish.",
                )
                return@on true
            }
            if (player.viewport.region.id == 12102) {
                player.pulseManager.run(
                    object : Pulse() {
                        var counter = 0
                        val t = getTroll(player)

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> animate(player, Animations.NET_FISHING_621)
                                3 -> {
                                    visualize(t, -1, Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86)
                                    when {
                                        hasRequirement(
                                            player,
                                            Quests.SWAN_SONG,
                                        ) -> sendChat(t, "You killed da Sea Troll Queen - you die now!")
                                        // "Leave dem fishies alone!"
                                        else -> sendChat(t, "Fishies be mine, leave dem fishies!")
                                    }
                                    t.location = player.location
                                    t.init()
                                    t.moveStep()
                                    t.isRespawn = false
                                }

                                4 -> t.attack(player)
                            }
                            return false
                        }
                    },
                )
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles spawn of Rock golem.
         */

        on(ENCHANTED_V_ROCK, IntType.SCENERY, "mine", "prospect") { player, _ ->
            val tool: SkillingTool? = SkillingTool.getPickaxe(player)
            tool ?: sendMessage(
                player,
                "You lack an pickaxe which you have the Mining level to use.",
            ).also { return@on true }
            if (inBorders(player, 3023, 4491, 3029, 4494)) {
                player.pulseManager.run(
                    object : Pulse() {
                        var counter = 0
                        val g = getGolem(player)

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> animate(player, tool?.animation)
                                3 -> {
                                    visualize(g, -1, Graphics.RANDOM_EVENT_PUFF_OF_SMOKE_86)
                                    sendChat(g, "Gerroff da rock!")
                                    g.location = player.location
                                    g.init()
                                    g.moveStep()
                                    g.isRespawn = false
                                }

                                4 -> g.attack(player)
                            }
                            return false
                        }
                    },
                )
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@on true
        }

        /*
         * Handles spawn of Tree spirit.
         */

        on(ENCHANTED_V_TREE, IntType.SCENERY, "chop-down") { player, _ ->
            val tool: SkillingTool? = SkillingTool.getHatchet(player)
            tool ?: sendMessage(
                player,
                "You lack an axe which you have the Woodcutting level to use.",
            ).also { return@on true }
            player.pulseManager.run(
                object : Pulse() {
                    var counter = 0
                    val n = getSpirit(player)

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> animate(player, tool?.animation)
                            3 -> {
                                n.location = player.location
                                n.init()
                                n.moveStep()
                                n.isRespawn = false
                            }

                            4 -> n.attack(player)
                        }
                        return false
                    }
                },
            )
            return@on true
        }
    }

    fun getSpirit(player: Player): NPC {
        val level = player.properties.currentCombatLevel
        var index = ceil(level / 20.0).toInt()
        if (index >= TREE_SPIRIT_IDS.size) index = TREE_SPIRIT_IDS.size - 1
        return NPC(TREE_SPIRIT_IDS[index])
    }

    fun getGolem(player: Player): NPC {
        val level = player.properties.currentCombatLevel
        var index = ceil(level / 20.0).toInt()
        if (index >= ROCK_GOLEM_IDS.size) index = ROCK_GOLEM_IDS.size - 1
        return NPC(ROCK_GOLEM_IDS[index])
    }

    fun getTroll(player: Player): NPC {
        val level = player.properties.currentCombatLevel
        var index = ceil(level / 20.0).toInt()
        if (index >= RIVER_TROLL_IDS.size) index = RIVER_TROLL_IDS.size - 1
        return NPC(RIVER_TROLL_IDS[index])
    }

    companion object {
        private const val ENCHANTED_V_TREE = Scenery.TREE_16265
        private const val ENCHANTED_V_ROCK = Scenery.ROCKS_31060
        private const val ENCHANTED_V_FISH = Scenery.FISHING_SPOT_1175

        private val TREE_SPIRIT_IDS =
            intArrayOf(
                NPCs.TREE_SPIRIT_438,
                NPCs.TREE_SPIRIT_439,
                NPCs.TREE_SPIRIT_440,
                NPCs.TREE_SPIRIT_441,
                NPCs.TREE_SPIRIT_442,
                NPCs.TREE_SPIRIT_443,
            )

        private val ROCK_GOLEM_IDS =
            intArrayOf(
                NPCs.ROCK_GOLEM_413,
                NPCs.ROCK_GOLEM_414,
                NPCs.ROCK_GOLEM_415,
                NPCs.ROCK_GOLEM_416,
                NPCs.ROCK_GOLEM_417,
                NPCs.ROCK_GOLEM_418,
            )

        private val RIVER_TROLL_IDS =
            intArrayOf(
                NPCs.RIVER_TROLL_391,
                NPCs.RIVER_TROLL_392,
                NPCs.RIVER_TROLL_393,
                NPCs.RIVER_TROLL_394,
                NPCs.RIVER_TROLL_395,
                NPCs.RIVER_TROLL_396,
            )
    }
}
