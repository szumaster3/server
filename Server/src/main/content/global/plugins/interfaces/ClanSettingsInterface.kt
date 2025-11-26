package content.global.plugins.interfaces

import core.api.openInterface
import core.api.sendInputDialogue
import core.api.sendMessage
import core.api.sendString
import core.game.interaction.InterfaceListener
import core.game.node.entity.player.Player
import core.game.system.communication.ClanRank
import core.game.system.communication.ClanRepository
import core.net.amsc.MSPacketRepository
import core.net.amsc.WorldCommunicator
import core.tools.StringUtils
import shared.consts.Components

/**
 * Represents the clan setup interface interactions for players.
 * @author Vexia
 */
class ClanSettingsInterface : InterfaceListener {
    override fun defineInterfaceListeners() {
        on(Components.CLANJOIN_589) { player, _, _, buttonID, _, _ ->
            if (buttonID == 9) {
                if (player.interfaceManager.opened != null) {
                    sendMessage(player, "Please close the interface you have open before using your 'Clan Setup'")
                } else {
                    openSettings(player)
                }
            }

            if (buttonID == 14) {
                player.communication.toggleLootshare(player)
            }
            return@on true
        }

        on(Components.CLANSETUP_590) { player, _, opcode, buttonID, _, _ ->
            val clan = ClanRepository.get(player.name, true)

            when (buttonID) {
                22 -> {
                    if (opcode == 155) {
                        sendInputDialogue(player, false, "Enter clan prefix:") { value ->
                            val clanName = StringUtils.formatDisplayName(value.toString())

                            if (WorldCommunicator.isEnabled()) {
                                MSPacketRepository.sendClanRename(player, clanName)
                            }

                            if (clan.name == "Chat disabled") {
                                sendMessage(player, "Your clan channel has now been enabled!")
                                sendMessage(
                                    player,
                                    "Join your channel by clicking 'Join Chat' and typing: ${player.username}",
                                )
                            }

                            clan.name = clanName
                            player.communication.clanName = clanName
                            updateSettings(player)
                            clan.update()
                        }
                    }

                    if (opcode == 196) {
                        clan.name = "Chat disabled"
                        player.communication.clanName = ""
                        if (WorldCommunicator.isEnabled()) {
                            MSPacketRepository.sendClanRename(player, player.communication.clanName)
                        }
                        updateSettings(player)
                        clan.delete()
                    }
                }

                23 -> {
                    clan.joinRequirement = getRank(opcode)
                    player.communication.joinRequirement = clan.joinRequirement
                    MSPacketRepository.setClanSetting(player, 0, clan.joinRequirement)
                }

                24 -> {
                    clan.messageRequirement = getRank(opcode)
                    player.communication.messageRequirement = clan.messageRequirement
                    MSPacketRepository.setClanSetting(player, 1, clan.messageRequirement)
                }

                25 -> {
                    clan.kickRequirement = getRank(opcode)
                    player.communication.kickRequirement = clan.kickRequirement
                    MSPacketRepository.setClanSetting(player, 2, clan.kickRequirement)
                }

                26 -> {
                    clan.lootRequirement = if (opcode == 155) ClanRank.ONLY_ME else getRank(opcode)
                    player.communication.lootRequirement = clan.lootRequirement
                    MSPacketRepository.setClanSetting(player, 3, clan.lootRequirement)
                }

                33 -> sendMessage(player, "CoinShare is not available.")
            }

            updateSettings(player)
            clan.update()
            return@on true
        }
    }

    /**
     * Converts an opcode integer to a corresponding [ClanRank].
     *
     * @param opcode The opcode representing a clan rank.
     * @return The corresponding [ClanRank].
     */
    fun getRank(opcode: Int): ClanRank =
        when (opcode) {
            155 -> ClanRank.ANYONE
            196 -> ClanRank.ANY_FRIEND
            124 -> ClanRank.RECRUIT
            199 -> ClanRank.CORPORAL
            234 -> ClanRank.SERGEANT
            168 -> ClanRank.LIEUTENANT
            166 -> ClanRank.CAPTAIN
            64 -> ClanRank.GENERAL
            53 -> ClanRank.ONLY_ME
            else -> ClanRank.NO_ONE
        }

    /**
     * Updates the clan settings UI strings for the specified player.
     *
     * @param player The player whose clan settings should be updated.
     */
    private fun updateSettings(player: Player) {
        val clan = ClanRepository.get(player.name, true)
        sendString(player, clan.name, Components.CLANSETUP_590, 22)
        // player.getPacketDispatch().sendConfig(1083, (isCoinshare() ? 1 : 0) << 18 | (isLootshare() ? 0 : 1));
        sendString(player, clan.joinRequirement.info, Components.CLANSETUP_590, 23)
        sendString(player, clan.messageRequirement.info, Components.CLANSETUP_590, 24)
        sendString(player, clan.kickRequirement.info, Components.CLANSETUP_590, 25)
        sendString(player, clan.lootRequirement.info, Components.CLANSETUP_590, 26)
    }

    /**
     * Opens the clan settings for the player.
     *
     * @param player The player.
     */
    private fun openSettings(player: Player) {
        openInterface(player, Components.CLANSETUP_590)
        updateSettings(player)
    }
}
