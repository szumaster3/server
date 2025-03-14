package content.global.skill.magic.spells.teleport

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.playGlobalAudio
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.item.Item
import core.game.world.map.Location
import org.rs.consts.Items
import org.rs.consts.Sounds

class LunarSpellbookTeleport : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.HOME_TELEPORT, NONE) { player, _ ->
            requires(player)
            player.teleporter.send(Location.create(2100, 3914, 0), TeleportManager.TeleportType.HOME)
            setDelay(player, true)
        }

        onCast(LunarSpells.MOONCLAN_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                69,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 1), Item(Items.EARTH_RUNE_557, 2)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 66.0, Location.create(2111, 3916, 0))
        }

        onCast(LunarSpells.OURANIA_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                71,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 1), Item(Items.EARTH_RUNE_557, 6)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 69.0, Location.create(2469, 3247, 0))
        }

        onCast(LunarSpells.WATERBIRTH_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                72,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563), Item(Items.WATER_RUNE_555)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 71.0, Location.create(2527, 3739, 0))
        }

        onCast(LunarSpells.BARBARIAN_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                75,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 2), Item(Items.FIRE_RUNE_554, 3)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 76.0, Location.create(2544, 3572, 0))
        }

        onCast(LunarSpells.KHAZARD_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                78,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 2), Item(Items.LAW_RUNE_563, 2), Item(Items.WATER_RUNE_555, 4)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 80.0, Location.create(2656, 3157, 0))
        }

        onCast(LunarSpells.FISHING_GUILD_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                85,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 3), Item(Items.WATER_RUNE_555, 10)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 89.0, Location.create(2611, 3393, 0))
        }

        onCast(LunarSpells.CATHERBY_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                87,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 3), Item(Items.WATER_RUNE_555, 10)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 92.0, Location.create(2804, 3433, 0))
        }

        onCast(LunarSpells.ICE_PLATEAU_TELEPORT, NONE) { player, _ ->
            requires(
                player,
                89,
                arrayOf(Item(Items.ASTRAL_RUNE_9075, 3), Item(Items.LAW_RUNE_563, 3), Item(Items.WATER_RUNE_555, 8)),
            )
            if (!player.isTeleBlocked) playGlobalAudio(player.location, Sounds.TP_ALL_200)
            sendTeleport(player, 96.0, Location.create(2972, 3873, 0))
        }
    }

    private fun sendTeleport(
        player: Player,
        xp: Double,
        loc: Location,
    ) {
        if (player.teleporter.send(loc, TeleportManager.TeleportType.LUNAR)) {
            addXP(player, xp)
            removeRunes(player)
            setDelay(player, true)
        }
    }
}
