package content.global.activity.enchkey

import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.system.command.Privilege
import core.game.world.map.Location
import org.rs.consts.Items
import kotlin.random.Random

data class Treasure(
    val rewards: List<Pair<Int, Int>>,
    val completionMessage: String? = null,
)

class EnchKeyListener :
    InteractionListener,
    Commands {
    private var player: Player? = null

    override fun defineListeners() {
        activityListener(EnchKeyTreasure.ENCHANTED_KEY_ATTR) { player -> makingTreasures(player) }
        activityListener(EnchKeyTreasure.ENCHANTED_KEY_2_ATTR) { player -> meetingTreasures(player) }
    }

    private fun activityListener(
        attribute: String,
        treasureProvider: (Player) -> Map<Location, Treasure>,
    ) {
        val treasures = player?.let { treasureProvider(it) }

        treasures?.forEach { (location, treasure) ->
            onDig(location) { player: Player ->
                if (treasures != null) {
                    handleTreasureDig(player, location, treasures, attribute)
                }
            }
        }
    }

    private fun handleTreasureDig(
        player: Player,
        location: Location,
        treasures: Map<Location, Treasure>,
        attribute: String,
    ) {
        val currentProgress = getAttribute(player, attribute, 0)
        val locations = treasures.keys.toList()

        if (currentProgress < locations.size && locations[currentProgress] == location) {
            val treasure = treasures[location] ?: return

            player.incrementAttribute(attribute)

            giveRewards(player, treasure.rewards)
            sendMessage(player, "You found a treasure!")

            if (treasure.completionMessage != null && currentProgress == locations.size - 1) {
                sendMessage(player, treasure.completionMessage)
                finishActivity(player, attribute)
            }
        }
    }

    private fun giveRewards(
        player: Player,
        rewards: List<Pair<Int, Int>>,
    ) {
        rewards.forEach { (item, amount) ->
            addItemOrDrop(player, item, amount)
        }
    }

    private fun finishActivity(
        player: Player,
        attribute: String,
    ) {
        removeItem(player, Items.ENCHANTED_KEY_6754)
        removeAttribute(player, attribute)
        sendMessage(player, "Congratulations! You have completed the Enchanted key mini-quest!")
    }

    private fun makingTreasures(player: Player): Map<Location, Treasure> {
        val initialTreasure =
            mapOf(
                EnchKeyTreasure.rellekkaTreasure to
                    Treasure(
                        listOf(
                            Items.STEEL_ARROW_886 to 20,
                            Items.MITHRIL_ORE_448 to 10,
                            Items.LAW_RUNE_563 to 15,
                        ),
                    ),
            )
        val randomTreasure =
            mapOf(
                EnchKeyTreasure.ardougneTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 36,
                            Items.IRON_ORE_441 to 15,
                            Items.FIRE_RUNE_554 to 30,
                        ),
                    ),
                EnchKeyTreasure.benchTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 40,
                            Items.IRON_ARROWTIPS_40 to 20,
                            Items.FIRE_RUNE_554 to 20,
                        ),
                    ),
                EnchKeyTreasure.gnomeTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 39,
                            Items.IRON_ARROWTIPS_40 to 20,
                            Items.WATER_RUNE_555 to 30,
                        ),
                    ),
                EnchKeyTreasure.altarTreasure to
                    Treasure(
                        listOf(
                            Items.MITHRIL_ORE_448 to 10,
                            Items.IRON_ORE_441 to 15,
                            Items.EARTH_RUNE_557 to 45,
                        ),
                    ),
                EnchKeyTreasure.faladorTreasure to
                    Treasure(
                        listOf(
                            Items.EARTH_RUNE_557 to 15,
                            Items.IRON_ARROW_884 to 20,
                            Items.SARADOMIN_MJOLNIR_6762 to 1,
                        ),
                    ),
                EnchKeyTreasure.mudskipperTreasure to
                    Treasure(
                        listOf(
                            Items.IRON_ORE_441 to 15,
                            Items.MITHRIL_ARROW_888 to 20,
                            Items.DEATH_RUNE_560 to 15,
                        ),
                    ),
                EnchKeyTreasure.swampTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 29,
                            Items.MIND_RUNE_558 to 20,
                            Items.STEEL_ARROW_886 to 20,
                            Items.ZOMBIE_HEAD_6722 to 1,
                        ),
                    ),
                EnchKeyTreasure.alkharidTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 40,
                            Items.MITHRIL_ORE_448 to 10,
                            Items.ZAMORAK_MJOLNIR_6764 to 1,
                        ),
                    ),
                EnchKeyTreasure.examTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 40,
                            Items.IRON_ORE_441 to 15,
                            Items.GUTHIX_MJOLNIR_6760 to 1,
                        ),
                    ),
                EnchKeyTreasure.geTreasure to
                    Treasure(
                        listOf(
                            Items.PURE_ESSENCE_7937 to 39,
                            Items.MITHRIL_ARROW_888 to 10,
                            Items.LAW_RUNE_563 to 15,
                        ),
                    ),
            )

        val shuffledLocation =
            randomTreasure.entries
                .shuffled(Random(player.hashCode()))
                .associate { it.key to it.value }

        return initialTreasure + shuffledLocation
    }

    private fun meetingTreasures(player: Player): Map<Location, Treasure> {
        return mapOf(
            EnchKeyTreasure.gnomeballfieldTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 510,
                        Items.GOLD_CHARM_12158 to 3,
                        Items.LAW_RUNE_563 to 15,
                        Items.MITHRIL_ARROW_888 to 20,
                    ),
                ),
            EnchKeyTreasure.shantaypassTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 530,
                        Items.GOLD_CHARM_12158 to 3,
                        Items.PURE_ESSENCE_7937 to 10,
                        Items.UNCUT_SAPPHIRE_1624 to 3,
                    ),
                ),
            EnchKeyTreasure.brimhavenTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 560,
                        Items.GREEN_CHARM_12159 to 1,
                        Items.COSMIC_RUNE_564 to 5,
                        Items.UNCUT_EMERALD_1622 to 2,
                    ),
                ),
            EnchKeyTreasure.wildernessTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 650,
                        Items.GREEN_CHARM_12159 to 1,
                        Items.PURE_ESSENCE_7937 to 10,
                        Items.UNCUT_RUBY_1620 to 1,
                    ),
                ),
            EnchKeyTreasure.taibwowannaiTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 750,
                        Items.GREEN_CHARM_12159 to 2,
                        Items.COSMIC_RUNE_564 to 10,
                        Items.MITHRIL_ARROW_888 to 30,
                    ),
                ),
            EnchKeyTreasure.feldiphillsTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 800,
                        Items.GOLD_CHARM_12158 to 30,
                        Items.CRIMSON_CHARM_12160 to 1,
                        Items.NATURE_RUNE_561 to 15,
                    ),
                ),
            EnchKeyTreasure.agilitypyramidTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 830,
                        Items.CRIMSON_CHARM_12160 to 1,
                        Items.DEATH_RUNE_560 to 5,
                        Items.UNCUT_RUBY_1620 to 2,
                    ),
                ),
            EnchKeyTreasure.banditcampTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 950,
                        Items.CRIMSON_CHARM_12160 to 2,
                        Items.UNCUT_EMERALD_1621 to 3,
                        Items.CHAOS_RUNE_562 to 15,
                    ),
                ),
            EnchKeyTreasure.daemonheimTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 950,
                        Items.BLUE_CHARM_12163 to 1,
                        Items.PURE_ESSENCE_7937 to 20,
                        Items.GOLD_BAR_2358 to 5,
                    ),
                ),
            EnchKeyTreasure.deathplateauTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 1010,
                        Items.BLUE_CHARM_12163 to 1,
                        Items.PURE_ESSENCE_7937 to 20,
                        Items.BLOOD_RUNE_565 to 10,
                    ),
                ),
            EnchKeyTreasure.scorpionPitTreasure to
                Treasure(
                    listOf(
                        Items.COINS_995 to 1100,
                        Items.BLUE_CHARM_12163 to 2,
                        Items.GOLD_BAR_2358 to 15,
                        Items.DEATH_RUNE_560 to 10,
                    ),
                ),
        )
    }

    override fun defineCommands() {
        define(
            name = "ekstats",
            privilege = Privilege.ADMIN,
            usage = "::ekstats",
            description = "Displays current treasure progress.",
        ) { player, _ ->
            val treasures = makingTreasures(player)
            val progress = getAttribute(player, EnchKeyTreasure.ENCHANTED_KEY_ATTR, 0)
            val message =
                treasures.entries
                    .mapIndexed { index, (location, treasure) ->
                        val status =
                            if (index <
                                progress
                            ) {
                                core.tools.GREEN + "COMPLETED"
                            } else {
                                core.tools.RED + "PENDING"
                            }
                        "Location: (${location.x}, ${location.y}, ${location.z}), Rewards: ${treasure.rewards}, Status: [$status]"
                    }.joinToString("\n")

            sendMessage(player, "Treasures founded:\n$message")
        }

        define(
            name = "rollench",
            privilege = Privilege.ADMIN,
            usage = "::rollench",
            description = "Resets and roll random treasures for the player.",
        ) { player, _ ->
            removeAttribute(player, EnchKeyTreasure.ENCHANTED_KEY_ATTR)
            val treasures = meetingTreasures(player)
            val firstTreasure = treasures.entries.first()

            sendMessage(
                player,
                "[EnchantedKeyTreasureRoll] --> Location=[${firstTreasure.key.x}, ${firstTreasure.key.y}, ${firstTreasure.key.z}]",
            )
        }
    }
}
