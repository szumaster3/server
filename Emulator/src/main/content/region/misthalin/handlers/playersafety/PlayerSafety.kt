package content.region.misthalin.handlers.playersafety

import core.api.*
import core.game.activity.Cutscene
import core.game.component.Component
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.*

class PlayerSafety :
    MapZone("player-safety", true),
    InteractionListener {
    override fun configure() {
        register(FIRST_FLOOR)
        register(GOBLIN_JAIL)
    }

    override fun locationUpdate(
        e: Entity?,
        last: Location?,
    ) {
        if (e is Player) {
            val player = e.asPlayer()

            if (inBorders(player, GOBLIN_JAIL)) {
                if (!player.musicPlayer.hasUnlocked(Music.INCARCERATION_494)) {
                    player.musicPlayer.unlock(Music.INCARCERATION_494)
                }
                return
            }

            if (inBorders(player, FIRST_FLOOR)) {
                if (!player.musicPlayer.hasUnlocked(Music.SAFETY_IN_NUMBERS_493)) {
                    player.musicPlayer.unlock(Music.SAFETY_IN_NUMBERS_493)
                }
                return
            }

            if (inBorders(player, CLASS_ROOM)) {
                if (!player.musicPlayer.hasUnlocked(Music.EXAM_CONDITIONS_492)) {
                    player.musicPlayer.unlock(Music.EXAM_CONDITIONS_492)
                }
            }
        }
    }

    companion object {
        val instance = this
        var GOBLIN_JAIL = ZoneBorders(3076, 4228, 3089, 4253, 0, true)
        var FIRST_FLOOR = ZoneBorders(3132, 4221, 3175, 4281, 3, true)
        var CLASS_ROOM = ZoneBorders(3076, 3452, 3085, 3458, 0, true)

        private val DTI_MAP =
            mapOf(
                org.rs.consts.Scenery.JAIL_DOOR_29595 to Components.JAIL_PLAQUE_701,
                org.rs.consts.Scenery.JAIL_DOOR_29596 to Components.JAIL_PLAQUE_703,
                org.rs.consts.Scenery.JAIL_DOOR_29597 to Components.JAIL_PLAQUE_711,
                org.rs.consts.Scenery.JAIL_DOOR_29598 to Components.JAIL_PLAQUE_695,
                org.rs.consts.Scenery.JAIL_DOOR_29599 to Components.SAFETY_JAIL_312,
                org.rs.consts.Scenery.JAIL_DOOR_29600 to Components.JAIL_PLAQUE_706,
                org.rs.consts.Scenery.JAIL_DOOR_29601 to Components.JAIL_PLAQUE_698,
            )
        private const val ATTRIBUTE_CLIMB_CREVICE = "player_strong:crevice_climbed"
    }

    override fun defineListeners() {
        on(Items.TEST_PAPER_12626, IntType.ITEM, "take exam") { player, _ ->
            if (player.savedData.globalData.getTestStage() == 2) {
                sendDialogue(player, "You have already completed the test. Hand it in to Professor Henry for marking.")
            } else {
                openInterface(player, Components.PLAYER_SAFETY_EXAM_697)
            }
            return@on true
        }

        on((NPCs.STUDENT_7151..NPCs.STUDENT_7157).toIntArray(), IntType.NPC, "Talk-to") { player, _ ->
            sendDialogue(player, "This student is trying to focus on their work.")
            return@on true
        }

        on(org.rs.consts.Scenery.JAIL_ENTRANCE_29603, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(3082, 4229, 0))
            if (!player.musicPlayer.hasUnlocked(Music.INCARCERATION_494)) {
                player.musicPlayer.unlock(Music.INCARCERATION_494)
            }
            return@on true
        }

        on(org.rs.consts.Scenery.JAIL_ENTRANCE_29602, IntType.SCENERY, "leave") { player, _ ->
            teleport(player, Location.create(3074, 3456, 0))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29589, IntType.SCENERY, "climb-up") { player, _ ->
            if (player.globalData.hasReadPlaques()) {
                if (!player.musicPlayer.hasUnlocked(Music.EXAM_CONDITIONS_492)) {
                    player.musicPlayer.unlock(Music.EXAM_CONDITIONS_492)
                }
                teleport(player, Location.create(3083, 3452, 0))
            } else {
                sendMessage(player, "You need to read the jail plaques before the guard will allow you upstairs.")
            }
            return@on true
        }

        on(org.rs.consts.Scenery.DOOR_29732, IntType.SCENERY, "open") { player, node ->
            if (player.globalData.getTestStage() > 0) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29592, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3086, 4247, 0))
            return@on true
        }

        on(org.rs.consts.Scenery.CREVICE_29728, IntType.SCENERY, "enter") { player, _ ->
            if (getAttribute(player, ATTRIBUTE_CLIMB_CREVICE, false)) {
                teleport(player, Location.create(3159, 4279, 3))
            } else {
                sendMessage(player, "There's no way down.")
            }
            return@on true
        }

        on(org.rs.consts.Scenery.ROPE_29729, IntType.SCENERY, "climb") { player, _ ->
            if (!getAttribute(player, ATTRIBUTE_CLIMB_CREVICE, false)) {
                setAttribute(player, ATTRIBUTE_CLIMB_CREVICE, true)
            }
            teleport(player, Location.create(3077, 3462, 0))
            return@on true
        }

        on(
            (org.rs.consts.Scenery.JAIL_DOOR_29595..org.rs.consts.Scenery.JAIL_DOOR_29601).toIntArray(),
            IntType.SCENERY,
            "Read-plaque on",
        ) { player, node ->
            player.lock()
            read(player, node)
            return@on true
        }

        on(org.rs.consts.Scenery.TUNNEL_29623, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(3077, 4235, 0))
            return@on true
        }

        on(org.rs.consts.Scenery.AN_OLD_LEVER_29730, IntType.SCENERY, "pull") { player, _ ->
            sendMessage(player, "You hear the cogs and gears moving and a distant unlocking sound.")
            setVarp(player, 1203, (1 shl 29) or (1 shl 26), true)
            return@on true
        }

        on(org.rs.consts.Scenery.AN_OLD_LEVER_29731, IntType.SCENERY, "pull") { player, _ ->
            sendMessage(player, "You hear cogs and gears moving and the sound of heavy locks falling into place.")
            setVarp(player, 1203, 1 shl 29, true)
            return@on true
        }

        on(org.rs.consts.Scenery.JAIL_DOOR_29624, IntType.SCENERY, "open") { player, _ ->
            if (getVarp(player, 1203) and (1 shl 26) == 0) {
                sendMessage(player, "The door seems to be locked by some kind of mechanism.")
                return@on true
            }

            if (player.location.z == 2) {
                teleport(player, Location.create(3177, 4266, 0))
            } else if (player.location.z == 1) {
                teleport(player, Location.create(3143, 4270, 0))
            } else {
                if (player.location.x < 3150) {
                    teleport(player, Location.create(3142, 4272, 1))
                } else {
                    teleport(player, Location.create(3177, 4269, 2))
                }
            }
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29667, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3160, 4249, 1))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29668, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(3158, 4250, 2))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29663, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3160, 4246, 1))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29664, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(3158, 4245, 2))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29655, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3146, 4246, 1))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29656, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(3149, 4244, 2))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29659, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3146, 4249, 1))
            return@on true
        }

        on(org.rs.consts.Scenery.STAIRS_29660, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(3148, 4250, 2))
            return@on true
        }

        on(org.rs.consts.Scenery.TREASURE_CHEST_29577, IntType.SCENERY, "open") { player, _ ->
            setVarbit(player, 4499, 1, true)
            return@on true
        }

        on(org.rs.consts.Scenery.TREASURE_CHEST_29578, IntType.SCENERY, "search") { player, _ ->
            if (player.globalData.getTestStage() == 3) {
                if ((freeSlots(player) == 0) or ((freeSlots(player) == 1) and !inInventory(player, Items.COINS_995))) {
                    sendDialogue(player, "You do not have enough inventory space!")
                } else {
                    player.emoteManager.unlock(Emotes.SAFETY_FIRST)
                    addItem(player, Items.COINS_995, 10000)
                    addItem(player, Items.SAFETY_GLOVES_12629)
                    sendItemDialogue(
                        player,
                        Items.SAFETY_GLOVES_12629,
                        "You open the chest to find a large pile of gold, along with a pair of safety gloves. ",
                    )
                    player.globalData.setTestStage(4)
                }
            } else {
                if (hasAnItem(player, Items.SAFETY_GLOVES_12629).exists()) {
                    sendDialogue(player, "The chest is empty")
                } else {
                    if (freeSlots(player) == 0) {
                        sendDialogue(player, "You do not have enough inventory space!")
                    } else {
                        sendItemDialogue(
                            player,
                            Items.SAFETY_GLOVES_12629,
                            "You open the chest to find a pair of safety gloves. ",
                        )
                        addItem(player, Items.SAFETY_GLOVES_12629)
                    }
                }
            }
            return@on true
        }
    }

    fun read(
        player: Player,
        plaque: Node,
    ) {
        if (plaque !is Scenery) return
        player.interfaceManager.openChatbox(DTI_MAP[plaque.id]!!)
    }

    class PlaqueInterfaceListener : InterfaceListener {
        var scene: PlaqueCutscene? = null

        override fun defineInterfaceListeners() {
            for ((index, iface) in DTI_MAP.values.withIndex()) {
                onClose(iface) { player, _ ->
                    scene?.endWithoutFade()
                    player.globalData.readPlaques[index] = true
                    return@onClose true
                }

                onOpen(iface) { player, component ->
                    scene = PlaqueCutscene(player, component)
                    scene?.start(false)
                    return@onOpen true
                }

                on(iface) { player, _, _, buttonID, _, _ ->
                    if (buttonID == 2) {
                        player.unlock()
                        scene?.incrementStage()
                        player.interfaceManager.closeChatbox()
                    }
                    return@on true
                }
            }
        }

        class PlaqueCutscene(
            player: Player,
            val component: Component,
        ) : Cutscene(player) {
            private val rotationMapping =
                mapOf(
                    Components.JAIL_PLAQUE_701 to listOf(-1, 0),
                    Components.JAIL_PLAQUE_703 to listOf(-1, 0),
                    Components.JAIL_PLAQUE_711 to listOf(-1, 0),
                    Components.JAIL_PLAQUE_695 to listOf(0, 1),
                    Components.SAFETY_JAIL_312 to listOf(1, 0),
                    Components.JAIL_PLAQUE_706 to listOf(1, 0),
                    Components.JAIL_PLAQUE_698 to listOf(1, 0),
                )

            override fun setup() {
                setExit(player.location)
            }

            override fun runStage(stage: Int) {
                when (stage) {
                    0 -> {
                        moveCamera(
                            regionX = player.location.localX,
                            regionY = player.location.localY,
                            height = 200,
                            speed = 15,
                        )
                        rotateCamera(
                            regionX = player.location.localX + rotationMapping[component.id]!![0],
                            regionY = player.location.localY + rotationMapping[component.id]!![1],
                            height = 200,
                            speed = 30,
                        )
                    }

                    1 -> resetCamera()
                }
            }
        }
    }
}
