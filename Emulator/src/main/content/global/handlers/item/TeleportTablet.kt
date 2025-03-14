package content.global.handlers.item

import core.api.*
import core.api.quest.isQuestComplete
import core.cache.def.impl.ItemDefinition
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.quest.QuestReq
import core.game.node.entity.player.link.quest.QuestRequirements
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Quests

enum class TeleportTablet(
    val item: Int,
    val location: Location,
    val exp: Double,
) {
    ADDOUGNE_TELEPORT(
        item = Items.ARDOUGNE_TP_8011,
        location = Location.create(2662, 3307, 0),
        exp = 61.0,
    ),
    AIR_ALTAR_TELEPORT(
        item = Items.AIR_ALTAR_TP_13599,
        location = Location.create(2978, 3296, 0),
        exp = 0.0,
    ),
    ASTRAL_ALTAR_TELEPORT(
        item = Items.ASTRAL_ALTAR_TP_13611,
        location = Location.create(2156, 3862, 0),
        exp = 0.0,
    ),
    BLOOD_ALTAR_TELEPORT(
        item = Items.BLOOD_ALTAR_TP_13610,
        location = Location.create(3559, 9778, 0),
        exp = 0.0,
    ),
    BODY_ALTAR_TELEPORT(
        item = Items.BODY_ALTAR_TP_13604,
        location = Location.create(3055, 3443, 0),
        exp = 0.0,
    ),
    CAMELOT_TELEPORT(
        item = Items.CAMELOT_TP_8010,
        location = Location.create(2757, 3477, 0),
        exp = 55.5,
    ),
    CHAOS_ALTAR_TELEPORT(
        item = Items.CHAOS_ALTAR_TP_13606,
        location = Location.create(3058, 3593, 0),
        exp = 0.0,
    ),
    COSMIC_ALTAR_TELEPORT(
        item = Items.COSMIC_ALTAR_TP_13605,
        location = Location.create(2411, 4380, 0),
        exp = 0.0,
    ),
    DEATH_ALTAR_TELEPORT(
        item = Items.DEATH_ALTAR_TP_13609,
        location = Location.create(1863, 4639, 0),
        exp = 0.0,
    ),
    EARTH_ALTAR_TELEPORT(
        item = Items.EARTH_ALTAR_TP_13602,
        location = Location.create(3304, 3472, 0),
        exp = 0.0,
    ),
    FALADOR_TELEPORT(
        item = Items.FALADOR_TP_8009,
        location = Location.create(2966, 3380, 0),
        exp = 47.0,
    ),
    FIRE_ALTAR_TELEPORT(
        item = Items.FIRE_ALTAR_TP_13603,
        location = Location.create(3311, 3252, 0),
        exp = 0.0,
    ),
    LAW_ALTAR_TELEPORT(
        item = Items.LAW_ALTAR_TP_13608,
        location = Location.create(2857, 3378, 0),
        exp = 0.0,
    ),
    LUMBRIDGE_TELEPORT(
        item = Items.LUMBRIDGE_TP_8008,
        location = Location.create(3222, 3218, 0),
        exp = 41.0,
    ),
    MIND_ALTAR_TELEPORT(
        item = Items.MIND_ALTAR_TP_13600,
        location = Location.create(2979, 3510, 0),
        exp = 0.0,
    ),
    NATURE_ALTAR_TELEPORT(
        item = Items.NATURE_ALTAR_TP_13607,
        location = Location.create(2868, 3013, 0),
        exp = 0.0,
    ),
    VARROCK_TELEPORT(
        item = Items.VARROCK_TP_8007,
        location = Location.create(3212, 3423, 0),
        exp = 35.00,
    ),
    WATCH_TOWER_TELEPORT(
        item = Items.WATCHTOWER_TPORT_8012,
        location = Location.create(2548, 3114, 0),
        exp = 68.00,
    ),
    WATER_ALTAR_TELEPORT(
        item = Items.WATER_ALTAR_TP_13601,
        location = Location.create(3182, 3162, 0),
        exp = 0.0,
    ),
    RUNECRAFTING_GUILD_TELEPORT(
        item = Items.RUNECRAFTING_GUILD_TP_13598,
        location = Location.create(1696, 5463, 2),
        exp = 0.0,
    ),
    ;

    companion object {
        val idMap = values().map { it.item to it }.toMap()

        fun forId(id: Int): TeleportTablet? {
            return idMap[id]
        }
    }
}

class TeleportTabletOption : InteractionListener {
    override fun defineListeners() {
        val tabIDs = TeleportTablet.values().map { it.item }.toIntArray()
        on(tabIDs, IntType.ITEM, "break") { player, node ->
            val tab = node.id
            val tabEnum = TeleportTablet.forId(tab)
            if (tabEnum != null && inInventory(player, tab)) {
                if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                    sendMessage(player, "You need complete the Rune Mysteries quest in order to use this.")
                    return@on true
                }
                val tabloc = tabEnum.location

                if (inInventory(player, tab)) {
                    if (tab == Items.ARDOUGNE_TP_8011 && !isQuestComplete(player, Quests.PLAGUE_CITY)) return@on true
                    if (tab == Items.ASTRAL_ALTAR_TP_13611 &&
                        !hasRequirement(
                            player,
                            QuestReq(QuestRequirements.LUNAR_DIPLOMACY),
                        )
                    ) {
                        return@on true
                    }
                    if (tab == Items.COSMIC_ALTAR_TP_13605 && !isQuestComplete(player, Quests.LOST_CITY)) {
                        return@on true
                    }
                    if (tab == Items.DEATH_ALTAR_TP_13609 &&
                        !hasRequirement(
                            player,
                            QuestReq(QuestRequirements.MEP_2),
                        )
                    ) {
                        return@on true
                    }
                    if (tab == Items.BLOOD_ALTAR_TP_13610 &&
                        !hasRequirement(
                            player,
                            QuestReq(QuestRequirements.SEERGAZE),
                        )
                    ) {
                        return@on true
                    }
                    if (tab == Items.LAW_ALTAR_TP_13608 && !ItemDefinition.canEnterEntrana(player)) {
                        sendMessage(player, "You can't take weapons and armour into the law rift.")
                        return@on false
                    }
                    if (teleport(player, tabloc, TeleportManager.TeleportType.TELETABS)) {
                        removeItem(player, Item(node.id, 1))
                    }
                }
            }
            return@on true
        }
    }
}
