package content.global.skill.magic.spells.teleport

import content.data.GameAttributes
import content.global.skill.magic.SpellListener
import content.global.skill.magic.TeleportMethod
import content.global.skill.magic.spells.ModernSpells
import content.region.misthalin.handlers.VarrockAchievementDiary
import core.ServerConstants
import core.api.*
import core.api.quest.hasRequirement
import core.game.event.TeleportEvent
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Quests

class ModernSpellbookTeleport : SpellListener("modern") {
    override fun defineListeners() {
        onCast(ModernSpells.HOME_TELEPORT, NONE) { player, _ ->
            requires(player)
            if (!getAttribute(player, GameAttributes.TUTORIAL_COMPLETE, false)) {
                player.teleporter.send(Location.create(3233, 3230, 0), TeleportManager.TeleportType.HOME)
            } else {
                player.teleporter.send(ServerConstants.HOME_LOCATION, TeleportManager.TeleportType.HOME)
            }
            setDelay(player, true)
        }

        onCast(ModernSpells.VARROCK_TELEPORT, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 25,
                runes = arrayOf(Item(Items.FIRE_RUNE_554), Item(Items.AIR_RUNE_556, 3), Item(Items.LAW_RUNE_563)),
            )
            val alternateTeleport = getAttribute(player, VarrockAchievementDiary.ATTRIBUTE_VARROCK_ALT_TELE, false)
            val dest = if (alternateTeleport) Location.create(3165, 3472, 0) else Location.create(3213, 3424, 0)
            sendTeleport(
                player = player,
                xp = 35.0,
                location = dest,
            )
        }

        onCast(ModernSpells.LUMBRIDGE_TELEPORT, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 31,
                runes = arrayOf(Item(Items.EARTH_RUNE_557), Item(Items.AIR_RUNE_556, 3), Item(Items.LAW_RUNE_563)),
            )
            sendTeleport(
                player = player,
                xp = 41.0,
                location = Location.create(3221, 3219, 0),
            )
        }

        onCast(ModernSpells.FALADOR_TELEPORT, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 37,
                runes = arrayOf(Item(Items.WATER_RUNE_555), Item(Items.AIR_RUNE_556, 3), Item(Items.LAW_RUNE_563)),
            )
            sendTeleport(
                player = player,
                xp = 47.0,
                location = Location.create(2965, 3378, 0),
            )
        }

        onCast(ModernSpells.CAMELOT_TELEPORT, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 45,
                runes = arrayOf(Item(Items.AIR_RUNE_556, 5), Item(Items.LAW_RUNE_563)),
            )
            // https://runescape.wiki/w/Camelot_Teleport?oldid=1085906
            val alternateTeleport = inEquipment(player, Items.SEERS_HEADBAND_1_14631)
            val dest = if (alternateTeleport) Location.create(2731, 3485, 0) else Location.create(2758, 3478, 0)
            sendTeleport(
                player = player,
                xp = 55.5,
                location = dest,
            )
            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 1, 5)
        }

        onCast(ModernSpells.ARDOUGNE_TELEPORT, NONE) { player, _ ->
            if (!hasRequirement(player, Quests.PLAGUE_CITY)) {
                return@onCast
            }
            requires(
                player = player,
                magicLevel = 51,
                runes = arrayOf(Item(Items.WATER_RUNE_555, 2), Item(Items.LAW_RUNE_563, 2)),
            )
            sendTeleport(
                player = player,
                xp = 61.0,
                location = Location.create(2662, 3307, 0),
            )
        }

        onCast(ModernSpells.WATCHTOWER_TELEPORT, NONE) { player, _ ->
            if (!hasRequirement(player, Quests.WATCHTOWER)) {
                return@onCast
            }
            requires(
                player = player,
                magicLevel = 58,
                runes = arrayOf(Item(Items.EARTH_RUNE_557, 2), Item(Items.LAW_RUNE_563, 2)),
            )
            sendTeleport(
                player = player,
                xp = 68.0,
                location = Location.create(2549, 3112, 0),
            )
        }

        onCast(ModernSpells.TROLLHEIM_TELEPORT, NONE) { player, _ ->
            if (!hasRequirement(player, Quests.EADGARS_RUSE)) {
                return@onCast
            }
            requires(
                player = player,
                magicLevel = 61,
                runes = arrayOf(Item(Items.FIRE_RUNE_554, 2), Item(Items.LAW_RUNE_563, 2)),
            )
            sendTeleport(
                player = player,
                xp = 68.0,
                location = Location.create(2891, 3678, 0),
            )
        }

        onCast(ModernSpells.APE_ATOLL_TELEPORT, NONE) { player, _ ->
            if (!hasRequirement(player, Quests.MONKEY_MADNESS)) {
                return@onCast
            }
            requires(
                player = player,
                magicLevel = 64,
                runes =
                    arrayOf(
                        Item(Items.FIRE_RUNE_554, 2),
                        Item(Items.WATER_RUNE_555, 2),
                        Item(Items.LAW_RUNE_563, 2),
                        Item(
                            Items.BANANA_1963,
                        ),
                    ),
            )
            sendTeleport(player, 74.0, Location.create(2754, 2784, 0))
        }

        onCast(ModernSpells.TELEPORT_TO_HOUSE, NONE) { player, _ ->
            requires(
                player = player,
                magicLevel = 40,
                runes = arrayOf(Item(Items.LAW_RUNE_563), Item(Items.AIR_RUNE_556), Item(Items.EARTH_RUNE_557)),
            )
            attemptHouseTeleport(player)
        }
    }

    private fun sendTeleport(
        player: Player,
        xp: Double,
        location: Location,
    ) {
        if (player.isTeleBlocked) {
            removeAttribute(player, "spell:runes")
            sendMessage(player, "A magical force prevents you from teleporting.")
            return
        }

        val teleType = TeleportManager.TeleportType.NORMAL

        if (player.teleporter.send(location, teleType)) {
            player.dispatch(TeleportEvent(teleType, TeleportMethod.SPELL, -1, location))

            removeRunes(player)
            addXP(player, xp)
            setDelay(player, true)
        }
    }

    private fun attemptHouseTeleport(player: Player) {
        if (player.isTeleBlocked) {
            removeAttribute(player, "spell:runes")
            sendMessage(player, "A magical force prevents you from teleporting.")
            return
        }
        val hasHouse = player.houseManager.location.exitLocation != null
        if (!hasHouse) {
            sendMessage(player, "You must have bought a house before you can use the spell.")
            return
        }

        player.houseManager.preEnter(player, false)
        val teleType = TeleportManager.TeleportType.NORMAL
        val loc = player.houseManager.getEnterLocation()
        player.teleporter.send(loc, teleType)
        removeRunes(player)
        addXP(player, 30.0)
        setDelay(player, true)
        submitWorldPulse(
            object : Pulse(4) {
                override fun pulse(): Boolean {
                    openInterface(player, Components.POH_HOUSE_LOADING_SCREEN_399)
                    player.houseManager.postEnter(player, false)
                    return true
                }
            },
        )
    }
}
