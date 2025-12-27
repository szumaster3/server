package core.game.node.entity.player.info.login

import content.data.GameAttributes
import content.data.GlobalStore
import core.ServerConstants
import core.api.*
import core.game.component.Component
import core.game.interaction.InteractionListeners
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.GameWorld
import core.game.world.map.RegionManager
import core.game.world.repository.Repository
import core.game.world.update.UpdateSequence
import core.net.packet.PacketRepository
import core.net.packet.context.InterfaceContext
import core.net.packet.out.Interface
import core.plugin.Plugin
import core.tools.Log
import core.tools.colorize
import shared.consts.Components
import shared.consts.Quests
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream

/**
 * Handles login configuration, including lobby setup and game world initialization.
 */
object LoginConfiguration {
    /**
     * List of login-related plugins.
     */
    var loginPlugins = mutableListOf<Plugin<Any>>()
    private val lobbyPane = Component(Components.TOPLEVEL_FULL_549)
    private val lobbyInterface = Component(Components.WELCOME_SCREEN_378)

    /**
     * Lobby components for UI elements.
     */
    private val lobbyComponents =
        intArrayOf(
            Components.MESSAGE_OF_THE_WEEK_17,
            Components.MESSAGE_OF_THE_WEEK_18,
            Components.MESSAGE_OF_THE_WEEK_19,
            Components.MESSAGE_OF_THE_WEEK_20,
            Components.BANNER_PADLOCK_KEYS_15,
            Components.BANNER_ANTI_VIRUS_16,
            Components.BANNER_SCAMMING_21,
            Components.BANNER_SECURITY_22,
            Components.BANNER_XMAS_23,
            Components.BANNER_POH_405,
            Components.BANNER_CHATHEADS_447,
            Components.BANNER_GROUP_622,
            Components.BANNER_GROUP_ASSIST_623,
            Components.BANNER_SUMMONING_679,
            Components.BANNER_EASTER08_715,
            Components.BANNER_HALLOWEEN_800,
        )

    /**
     * Configures the player's lobby or game world based on their status.
     * @param player The player to configure.
     */
    @JvmStatic
    fun configureLobby(player: Player) {
        //val details = PlayerDetails.getDetails(player.username)
        //PlayerDetails.setAccountTimer(player, details)

        player.updateSceneGraph(true)
        if (isTutorialCompleted(player) && player.isNotReconnecting()) {
            sendLobbyScreen(player)
        } else {
            configureGameWorld(player)
        }
    }

    /**
     * Displays the lobby screen for the player.
     * @param player The player to show the lobby screen to.
     */
    @JvmStatic
    fun sendLobbyScreen(player: Player) {
        val selectedMessageModel = autoSelectMessageModel()
        Repository.lobbyPlayers.removeIf { it.name == player.name }
        Repository.lobbyPlayers.add(player)

        // May 2009 message appear after login to PvP world:
        // sendMessage(player, "This is a Bounty World, so you've been given a copy of the Bounty World manual.")
        // addItem(player, Items.BOUNTY_GUIDE_1)

        sendString(player, "Welcome to ${GameWorld.settings?.name}", lobbyInterface.id, 115)
        sendString(player, getLastLogin(player), lobbyInterface.id, 116)

        player.interfaceManager.openWindowsPane(lobbyPane)
        player.interfaceManager.opened = lobbyInterface

        // rectangle on the left
        sendString(player, "0 unread message", lobbyInterface.id, 37)
        sendString(player, "0", lobbyInterface.id, 39)

        // center rectangle
        if(!GameWorld.settings!!.isMembers) {
            sendString(player, "0", lobbyInterface.id, 96)
            sendString(player, "Not a Member", lobbyInterface.id, 93)
            sendString(player, "You are not a member. Members get loads of extra benefits and features. Click here to become a member.", lobbyInterface.id, 94)
        } else {
            sendString(player, "0", lobbyInterface.id, 96)
            sendString(player, "Never expires", lobbyInterface.id, 94)
        }

        PacketRepository.send(Interface::class.java, InterfaceContext(player, lobbyPane.id, 2, Components.WELCOME_SCREEN_378, true))
        PacketRepository.send(Interface::class.java, InterfaceContext(player, lobbyPane.id, 3, selectedMessageModel, true))
        sendString(player, GameWorld.settings?.message_string ?: "", selectedMessageModel, getMessageChild(selectedMessageModel))
    }

    /**
     * Configures the game world for the player.
     * @param player The player to configure the game world for.
     */
    @JvmStatic
    fun configureGameWorld(player: Player) {
        sendGameConfiguration(player)

        Repository.lobbyPlayers.remove(player)
        player.isPlaying = true

        UpdateSequence.renderablePlayers.add(player)
        RegionManager.move(player)
        player.musicPlayer.init()
        player.updateAppearance()
        player.playerFlags.lastSceneGraph = player.location
        player.packetDispatch.sendInterfaceConfig(226, 1, true)

        checkEmotes(player)
        setupItems(player)
        setupSpellBook(player)
        setConfigs(player)
    }

    /**
     * Sends initial game configuration settings to the player.
     * @param player The player to configure.
     */
    @JvmStatic
    fun sendGameConfiguration(player: Player) {
        player.interfaceManager.openWindowsPane(Component(if (player.interfaceManager.isResizable) Components.TOPLEVEL_FULLSCREEN_746 else Components.TOPLEVEL_548))
        player.interfaceManager.openChatbox(Components.CHATDEFAULT_137)
        player.interfaceManager.openDefaultTabs()
        player.interfaceManager.openInfoBars()
        welcome(player)
        config(player)

        loginPlugins.forEach { plugin ->
            try {
                plugin.newInstance(player)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        refreshAppearance(player)
    }

    @JvmStatic
    fun welcome(player: Player) {
        if (player.isArtificial) return
        if(isTutorialCompleted(player)){
            sendMessage(player, "Welcome to ${ServerConstants.SERVER_NAME}.")
        }
        GlobalStore.check(player)

        if (player.details.isMuted) {
            sendMessage(player, "You are muted.")
            sendMessage(player, "To prevent further mutes, please read the rules.")
            openInterface(player, Components.QUICKCHAT_LOCKED_801)
        }
    }

    @JvmStatic
    fun config(player: Player) {
        if (!player.isArtificial) {
            log(LoginConfiguration::class.java, Log.INFO, "Configuring player ${player.username}")
        }
        player.interfaceManager.openDefaultTabs()
        player.interfaceManager.openInfoBars()
        player.inventory.refresh()
        player.equipment.refresh()
        player.skills.refresh()
        player.skills.configure()
        player.settings.update()
        player.interaction.setDefault()
        player.packetDispatch.sendRunEnergy()
        player.familiarManager.login()

        sendString(
            player,
            "Friends List - ${ServerConstants.SERVER_NAME} ${GameWorld.settings?.worldId}",
            Components.FRIENDS2_550,
            3,
        )
        sendString(
            player,
            "When you have finished playing ${ServerConstants.SERVER_NAME}, always use the button below to logout safely.",
            Components.LOGOUT_182,
            0,
        )

        player.questRepository.syncronizeTab(player)
        player.interfaceManager.close()
        player.emoteManager.refresh()
        player.interfaceManager.openInfoBars()
        if (!player.isArtificial) {
            log(LoginConfiguration::class.java, Log.INFO, "Finished configuring player ${player.username}")
        }
    }

    private fun getMessageChild(interfaceId: Int): Int =
        when (interfaceId) {
            Components.BANNER_GROUP_622 -> 8
            Components.BANNER_ANTI_VIRUS_16 -> 6
            Components.MESSAGE_OF_THE_WEEK_20,
            Components.BANNER_GROUP_ASSIST_623,
            -> 5

            Components.BANNER_PADLOCK_KEYS_15,
            Components.MESSAGE_OF_THE_WEEK_18,
            Components.MESSAGE_OF_THE_WEEK_19,
            Components.BANNER_SCAMMING_21,
            Components.BANNER_SECURITY_22,
            Components.BANNER_CHATHEADS_447,
            Components.BANNER_POH_405,
            -> 4

            Components.MESSAGE_OF_THE_WEEK_17,
            Components.BANNER_XMAS_23, Components.BANNER_HALLOWEEN_800,
            -> 3

            Components.BANNER_EASTER08_715 -> 2
            Components.BANNER_SUMMONING_679 -> 1
            else -> 0
        }

    /**
     * Sends a welcome message.
     */
    private fun autoSelectMessageModel(): Int =
        if (IntStream.of(lobbyComponents.size).anyMatch { it == GameWorld.settings?.message_model }) {
            GameWorld.settings?.message_model ?: lobbyComponents.random()
        } else {
            lobbyComponents.random()
        }

    /**
     * Gets the last login message for a player.
     */
    @JvmStatic
    fun getLastLogin(player: Player): String {
        var lastIp =
            player.details.accountInfo.lastUsedIp
                .ifEmpty { player.details.ipAddress }
        player.details.accountInfo.lastUsedIp = player.details.ipAddress
        val timeAgo = calculateTimeAgo(player.details.lastLogin)
        return "You last logged in $timeAgo from: $lastIp"
    }

    /**
     * Calculates the time difference from the last login to the current time.
     * @param lastLoginTime The timestamp of the last login.
     * @return A formatted string representing how long ago the login occurred.
     */
    private fun calculateTimeAgo(lastLoginTime: Long): String {
        val lastLogin = Date(lastLoginTime)
        val current = Date()
        val diff = current.time - lastLogin.time
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
        return when {
            days < 1 -> "earlier today"
            days == 1 -> "yesterday"
            else -> "$days days ago"
        }
    }

    /**
     * Determines if the player has completed the tutorial.
     * @param player The player to check.
     * @return True if the tutorial is completed, otherwise false.
     */
    private fun isTutorialCompleted(player: Player): Boolean = player.getAttribute(GameAttributes.TUTORIAL_COMPLETE, false)

    private fun Player.isNotReconnecting(): Boolean = getAttribute("login_type", LoginType.NORMAL_LOGIN) != LoginType.RECONNECT_TYPE

    private fun checkEmotes(player: Player) {
        if (player.globalData.getTestStage() == 3 && !hasEmote(player, Emotes.SAFETY_FIRST)) {
            unlockEmote(player, Emotes.SAFETY_FIRST)
        }
    }

    private fun setupItems(player: Player) {
        player.equipment.toArray().forEach { item ->
            if (item != null) {
                player.equipment.remove(item)
                if (!InteractionListeners.run(item.id, player, item, true) ||
                    !player.equipment.add(
                        item,
                        true,
                        false,
                    )
                ) {
                    sendMessage(player,
                        colorize(
                            "%RYou had items equipped in the wrong slots. They were moved out into your inventory.",
                        ),
                    )
                    addItemOrBank(player, item.id, item.amount)
                }
            }
        }
    }

    private fun setupSpellBook(player: Player) {
        val manager = player.spellBookManager
        val currentBook = SpellBookManager.SpellBook.forInterface(manager.spellBook) ?: SpellBookManager.SpellBook.MODERN

        when {
            currentBook == SpellBookManager.SpellBook.ANCIENT && !isQuestComplete(player, Quests.DESERT_TREASURE) -> {
                sendMessage(player, colorize("%RAs you can no longer use Ancient Magic, you have been set back to Modern."))
                manager.setSpellBook(SpellBookManager.SpellBook.MODERN)
            }
            currentBook == SpellBookManager.SpellBook.LUNAR && !hasRequirement(player, Quests.LUNAR_DIPLOMACY) -> {
                sendMessage(player, colorize("%RAs you can no longer use Lunar Magic, you have been set back to Modern."))
                manager.setSpellBook(SpellBookManager.SpellBook.MODERN)
            }
        }

        manager.update(player)
    }


    private fun setConfigs(player: Player) {
        setVarbit(player, 4322, 1)


        if (getAttribute(player, GameAttributes.KW_COMPLETE, false)) {
            setVarbit(player, 3909, 8)
        } else {
            setVarbit(player, 3909, 0)
        }

        if(hasRequirement(player, Quests.EAGLES_PEAK, false))
            // setVarbit(player, 2780, 15)
            setVarbit(player, 3109, 3)
            // setVarbit(player, 4056, 199)
    }
}
