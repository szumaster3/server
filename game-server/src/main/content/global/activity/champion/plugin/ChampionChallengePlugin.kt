package content.global.activity.champion.plugin

import content.data.GameAttributes
import content.global.activity.champion.dialogue.LarxusDialogueFile
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.*

/**
 * Champion's Challenge plugin.
 */
class ChampionChallengePlugin : InteractionListener, MapArea {

    private val regionID = 12696

    override fun defineAreaBorders(): Array<ZoneBorders> = arrayOf(getRegionBorders(regionID))

    override fun getRestrictions(): Array<ZoneRestriction> =
        arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS)

    override fun areaEnter(entity: Entity) {
        if (entity is Player) {
            registerLogoutListener(entity, "challenge") {
                entity.properties.teleportLocation = Location.create(3182, 9758, 0)
            }
        }
    }

    override fun areaLeave(entity: Entity, logout: Boolean) {
        if (entity is Player) {
            clearLogoutListener(entity, "challenge")
        }
    }

    override fun defineListeners() {

        /*
         * Handles climbing down ladder inside the Champion's Guild.
         */

        on(Scenery.CHAMPION_STATUE_10557, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3182, 9758, 0))
            return@on true
        }

        /*
         * Handles climbing up ladder inside the Champion's Guild.
         */

        on(Scenery.LADDER_10554, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(3185, 9758, 0))
            return@on true
        }

        /*
         * Handles read the champion scroll.
         */

        on(ChampionScrollsDropHandler.SCROLLS, IntType.ITEM, "read") { player, node ->
            displayScroll(player, node.asItem())
            return@on true
        }

        /*
         * Handles opening the trapdoor.
         */

        on(Scenery.TRAPDOOR_10558, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.TRAPDOOR_10559, 100, node.location)
            return@on true
        }

        /*
         * Handles closing the trapdoor.
         */

        on(Scenery.TRAPDOOR_10559, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.TRAPDOOR_10558, -1, node.location)
            return@on true
        }

        /*
         * Handles using champion scroll on Larxus NPC to start dialogue.
         */

        onUseWith(IntType.NPC, ChampionScrollsDropHandler.SCROLLS, NPCs.LARXUS_3050) { player, _, _ ->
            openDialogue(player, LarxusDialogueFile(true))
            return@onUseWith true
        }

        /*
         * Handles opening the champion statue.
         */

        on(Scenery.CHAMPION_STATUE_10556, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CHAMPION_STATUE_10557, 100, node.location)
            return@on true
        }

        /*
         * Handles opening the portcullis gate to start the challenge.
         */

        on(Scenery.PORTCULLIS_10553, IntType.SCENERY, "open") { player, node ->
            getStart(player, node)
            return@on true
        }
    }

    /**
     * Spawns a champion NPC based on the champion scroll.
     */
    private fun spawnChampion(player: Player) {
        val scroll = ChampionScrollsDropHandler.SCROLLS.firstOrNull { removeItem(player, it) }
        if (scroll == null) {
            spawnLeon(player)
            return
        }

        val npcId = ChampionChallengeNPC.NPC_ID + (scroll - ChampionChallengeNPC.SCROLL_ID)
        ChampionChallengeNPC.spawnChampion(player, npcId)
        if (scroll == Items.CHAMPION_SCROLL_6798) {
            setAttribute(player, GameAttributes.PRAYER_LOCK, true)
        }
    }

    /**
     * Spawns boss challenge.
     */
    private fun spawnLeon(player: Player) {
        if (getAttribute(player, GameAttributes.ACTIVITY_CHAMPION_BOSS_CHALLENGE, false)) {
            if (freeSlots(player) != 28) {
                sendNPCDialogue(
                    player,
                    NPCs.LARXUS_3050,
                    "His special rule is that no items in inventory can be brought to arena, only equipped items are allowed."
                )
                return
            }
            removeAttribute(player, GameAttributes.ACTIVITY_CHAMPION_BOSS_CHALLENGE)
            ChampionChallengeNPC.spawnChampion(player, NPCs.LEON_DCOUR_3067)
        }
    }

    /**
     * Handles the player interaction when opening the portcullis gate.
     */
    private fun getStart(player: Player, node: Node) {
        if (player.location.x == 3181 && player.location.y == 9758) {
            DoorActionHandler.handleDoor(player, node.asScenery())
            playAudio(player, Sounds.PORTCULLIS_OPEN_83)
            playAudio(player, Sounds.PORTCULLIS_CLOSE_82, 3)
            return
        }

        if (!player.getAttribute(GameAttributes.ACTIVITY_CHAMPION_CHALLENGE, false)) {
            sendNPCDialogue(
                player, NPCs.LARXUS_3050, "You need to arrange a challenge with me before you enter the arena."
            )
            return
        }

        if (!player.musicPlayer.hasUnlocked(Music.VICTORY_IS_MINE_528)) player.musicPlayer.unlock(
            Music.VICTORY_IS_MINE_528,
            true
        )

        lock(player, 3)
        submitWorldPulse(object : Pulse() {
            var tick = 0
            override fun pulse(): Boolean {
                when (tick++) {
                    2 -> {
                        playAudio(player, Sounds.PORTCULLIS_OPEN_83)
                        playAudio(player, Sounds.PORTCULLIS_CLOSE_82, 2)
                        DoorActionHandler.handleDoor(player, node.asScenery())
                    }

                    3 -> spawnChampion(player)
                }
                return false
            }
        })
    }

    /**
     * Displays the champion scroll content.
     */
    private fun displayScroll(player: Player, item: Item) {
        val content = when (item.id) {
            Items.CHAMPION_SCROLL_6798 -> arrayOf(
                "I challenge you to a duel, come to the arena",
                "beneath the Champion's Guild and fight me if you",
                "dare.",
                "",
                "Champion of Earth Warriors"
            )

            Items.CHAMPION_SCROLL_6799 -> arrayOf(
                "Come and duel me at the Champions' Guild, I'll",
                "make sure nothing goes to waste.",
                "",
                "Champion of Ghouls"
            )

            Items.CHAMPION_SCROLL_6800 -> arrayOf(
                "Get yourself to the Champions' Guild, if you dare", "to face me puny human.", "", "Champion of Giants"
            )

            Items.CHAMPION_SCROLL_6801 -> arrayOf(
                "Fight me if you think you can human, I'll wait",
                "for you in the Champion's Guild.",
                "",
                "Champion of Goblins"
            )

            Items.CHAMPION_SCROLL_6802 -> arrayOf(
                "You won't defeat me, though you're welcome to",
                "try at the Champions' Guild.",
                "",
                "Champion of Hobgoblins"
            )

            Items.CHAMPION_SCROLL_6803 -> arrayOf(
                "How about picking on someone your own size? I'll",
                "see you at the Champion's Guild.",
                "",
                "Champion of Imps"
            )

            Items.CHAMPION_SCROLL_6804 -> arrayOf(
                "You think you can defeat me? Come to the", "Champion's Guild and prove it!", "", "Champion of Jogres"
            )

            Items.CHAMPION_SCROLL_6805 -> arrayOf(
                "Come to the Champion's Guild so I can banish", "you mortal!", "", "Champion of Lesser Demons"
            )

            Items.CHAMPION_SCROLL_6806 -> arrayOf(
                "I'll be waiting at the Champions' Guild to collect", "your bones.", "", "Champion of Skeletons"
            )

            Items.CHAMPION_SCROLL_6807 -> arrayOf(
                "You come to Champions' Guild, you fight me, I", "squish you, I get brains!", "", "Champion of Zombies"
            )

            Items.CHAMPION_SCROLL_6808 -> arrayOf(
                "I challenge you to a fight! Meet me at the",
                "Champions' Guild so we can wrap this up.",
                "",
                "Champion of Mummies"
            )

            else -> return
        }
        openInterface(player, Components.BLANK_SCROLL_222)
        sendString(player, content.joinToString("<br>"), Components.BLANK_SCROLL_222, 5)
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.TRAPDOOR_10559), "climb-down") { _, _ ->
            return@setDest Location(3191, 3355, 0)
        }
    }

    companion object {
        /**
         * Checks if player has completed all prerequisite tasks for the final battle.
         */
        fun isFinalBattle(player: Player) {
            val allSet = (1452..1461).all { getVarbit(player, it) == 1 }
            if (allSet) {
                queueScript(player, 3, QueueStrength.SOFT) {
                    player.lock(1)
                    sendNPCDialogueLines(
                        player,
                        NPCs.LEON_DCOUR_3067,
                        FaceAnim.NEUTRAL,
                        false,
                        "You have done well brave adventurer, but I would test",
                        "your mettle now. You may arrange the fight with",
                        "Larxus at your leisure."
                    )
                    setAttribute(player, GameAttributes.ACTIVITY_CHAMPION_BOSS_CHALLENGE, true)
                    return@queueScript stopExecuting(player)
                }
            }
        }
    }
}
