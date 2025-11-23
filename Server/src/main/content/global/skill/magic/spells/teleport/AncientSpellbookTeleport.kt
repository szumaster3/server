package content.global.skill.magic.spells.teleport

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.AncientSpells
import core.api.finishDiaryTask
import core.api.sendMessage
import core.game.component.Component
import core.game.node.entity.combat.spell.Runes
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.Item
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import shared.consts.Components

class AncientSpellbookTeleport : SpellListener("ancient") {

    private var castRunes: Array<Item> = emptyArray()
    private var spellId: Int = -1

    override fun defineListeners() {
        onCast(AncientSpells.EDGEVILLE_TELEPORT, NONE) { player, _ ->
            val runes = emptyArray<Item>()
            requires(player, 0, runes)
            sendTeleport(player, 0.0, "Edgeville", Location.create(3095, 3513, 0), runes, isHome = true)
        }

        onCast(AncientSpells.PADDEWWA_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.AIR_RUNE.id, 1), Item(Runes.FIRE_RUNE.id, 1), Item(Runes.LAW_RUNE.id, 2))
            requires(player, 54, runes)
            sendTeleport(player, 64.0, "Paddewwa", Location.create(3098, 9883, 0), runes)
        }

        onCast(AncientSpells.SENNTISTEN_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.LAW_RUNE.id, 2), Item(Runes.SOUL_RUNE.id, 1))
            requires(player, 60, runes)
            sendTeleport(player, 70.0, "Senntisten", Location.create(3321, 3336, 0), runes)
        }

        onCast(AncientSpells.KHARYRLL_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.BLOOD_RUNE.id, 1), Item(Runes.LAW_RUNE.id, 2))
            requires(player, 66, runes)
            sendTeleport(player, 76.0, "Kharyrll", Location.create(3494, 3471, 0), runes)
        }

        onCast(AncientSpells.LASSAR_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.WATER_RUNE.id, 4), Item(Runes.LAW_RUNE.id, 2))
            requires(player, 72, runes)
            sendTeleport(player, 82.0, "Lassar", Location.create(3005, 3473, 0), runes)
        }

        onCast(AncientSpells.DAREEYAK_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.AIR_RUNE.id, 2), Item(Runes.FIRE_RUNE.id, 3), Item(Runes.LAW_RUNE.id, 2))
            requires(player, 78, runes)
            sendTeleport(player, 88.0, "Dareeyak", Location.create(2965, 3695, 0), runes)
        }

        onCast(AncientSpells.CARRALLANGER_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.LAW_RUNE.id, 2), Item(Runes.SOUL_RUNE.id, 2))
            requires(player, 84, runes)
            sendTeleport(player, 94.0, "Carrallanger", Location.create(3218, 3678, 0), runes)
        }

        onCast(AncientSpells.ANNAKARL_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.BLOOD_RUNE.id, 2), Item(Runes.LAW_RUNE.id, 2))
            requires(player, 90, runes)
            sendTeleport(player, 100.0, "Annakarl", Location.create(3288, 3886, 0), runes)
        }

        onCast(AncientSpells.GHORROCK_TELEPORT, NONE) { player, _ ->
            val runes = arrayOf(Item(Runes.WATER_RUNE.id, 8), Item(Runes.LAW_RUNE.id, 2))
            requires(player, 96, runes)
            sendTeleport(player, 106.0, "Ghorrock", Location.create(2975, 3877, 0), runes)
        }
    }

    private fun sendTeleport(player: Player, xp: Double, destName: String, loc: Location, runes: Array<Item>, isHome: Boolean = false) {
        if (player.isTeleBlocked) {
            sendMessage(player, "A magical force has stopped you from teleporting.")
            return
        }

        castRunes = runes
        spellId = if (isHome) 28 else 0

        val dest = loc.transform(0, RandomFunction.random(3), 0)
        val teleportType = if (isHome) TeleportManager.TeleportType.HOME else TeleportManager.TeleportType.ANCIENT

        RegionManager.getLocalPlayers(player, 1).forEach {
            if (it == player || it.isTeleBlocked || !it.isActive || !it.settings.isAcceptAid || it.ironmanManager.isIronman) return@forEach
            it.setAttribute("t-o_location", dest)
            it.interfaceManager.open(Component(Components.TP_OTHER_326))
            it.packetDispatch.sendString(player.username, Components.TP_OTHER_326, 1)
            it.packetDispatch.sendString(destName, Components.TP_OTHER_326, 3)
        }

        if (player.teleporter.send(dest, teleportType)) {
            if (loc == Location.create(3087, 3495, 0)) {
                finishDiaryTask(player, DiaryType.VARROCK, 2, 11)
            }

            player.setAttribute("teleport:items", runes)
            player.setAttribute("magic-delay", GameWorld.ticks + 5)
            addXP(player, xp)
            removeRunes(player)
            setDelay(player, true)
        }
    }
}
