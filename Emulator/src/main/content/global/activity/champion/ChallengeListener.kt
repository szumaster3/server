package content.global.activity.champion

import content.data.GameAttributes
import content.global.activity.champion.npc.EarthWarriorChampionNPC.Companion.spawnEarthWarriorChampion
import content.global.activity.champion.npc.GhoulChampionNPC.Companion.spawnGhoulChampion
import content.global.activity.champion.npc.GiantChampionNPC.Companion.spawnGiantChampion
import content.global.activity.champion.npc.GoblinChampionNPC.Companion.spawnGoblinChampion
import content.global.activity.champion.npc.HobgoblinChampionNPC.Companion.spawnHobgoblinChampion
import content.global.activity.champion.npc.ImpChampionNPC.Companion.spawnImpChampion
import content.global.activity.champion.npc.JogreChampionNPC.Companion.spawnJogreChampion
import content.global.activity.champion.npc.LeonDCourNPC.Companion.spawnFinalChampion
import content.global.activity.champion.npc.LesserDemonChampionNPC.Companion.spawnLesserDemonChampion
import content.global.activity.champion.npc.SkeletonChampionNPC.Companion.spawnSkeletonChampion
import content.global.activity.champion.npc.ZombieChampionNPC.Companion.spawnZombieChampion
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import org.rs.consts.*

class ChallengeListener :
    InteractionListener,
    MapArea {
    val earthWarriorScroll = Items.CHAMPION_SCROLL_6798
    val ghoulScroll = Items.CHAMPION_SCROLL_6799
    val giantScroll = Items.CHAMPION_SCROLL_6800
    val goblinScroll = Items.CHAMPION_SCROLL_6801
    val hobgoblinScroll = Items.CHAMPION_SCROLL_6802
    val impScroll = Items.CHAMPION_SCROLL_6803
    val jogreScroll = Items.CHAMPION_SCROLL_6804
    val lesserDemonScroll = Items.CHAMPION_SCROLL_6805
    val skeletonScroll = Items.CHAMPION_SCROLL_6806
    val zombieScroll = Items.CHAMPION_SCROLL_6807
    val mummiesScroll = Items.CHAMPION_SCROLL_6808

    val blankScroll = Components.BLANK_SCROLL_222

    private val impScrollContent =
        arrayOf(
            "How about picking on someone your own size? I'll",
            "see you at the Champion's Guild.",
            "",
            "Champion of Imps",
        )
    private val goblinScrollContent =
        arrayOf(
            "Fight me if you think you can human, I'll wait",
            "for you in the Champion's Guild.",
            "",
            "Champion of Goblins",
        )
    private val skeletonScrollContent =
        arrayOf("I'll be waiting at the Champions' Guild to collect", "your bones.", "", "Champion of Skeletons")
    private val zombieScrollContent =
        arrayOf("You come to Champions' Guild, you fight me, I", "squish you, I get brains!", "", "Champion of Zombies")
    private val giantScrollContent =
        arrayOf("Get yourself to the Champions' Guild, if you dare", "to face me puny human.", "", "Champion of Giants")
    private val hobgoblinScrollContent =
        arrayOf(
            "You won't defeat me, though you're welcome to",
            "try at the Champions' Guild.",
            "",
            "Champion of Hobgoblins",
        )
    private val ghoulScrollContent =
        arrayOf(
            "Come and duel me at the Champions' Guild, I'll",
            "make sure nothing goes to waste.",
            "",
            "Champion of Ghouls",
        )
    private val earthWarriorScrollContent =
        arrayOf(
            "I challenge you to a duel, come to the arena",
            "beneath the Champion's Guild and fight me if you",
            "dare.",
            "",
            "Champion of Earth Warriors",
        )
    private val jogreScrollContent =
        arrayOf("You think you can defeat me? Come to the", "Champion's Guild and prove it!", "", "Champion of Jogres")
    private val lesserDemonScrollContent =
        arrayOf("Come to the Champion's Guild so I can banish", "you mortal!", "", "Champion of Lesser Demons")
    private val mummiesScrollContent =
        arrayOf(
            "I challenge you to a fight! Meet me at the",
            "Champions' Guild so we can wrap this up.",
            "",
            "Champion of Mummies",
        )

    private val portcullisScenery = Scenery.PORTCULLIS_10553
    private val ladderID = Scenery.LADDER_10554
    private val championStatue = Scenery.CHAMPION_STATUE_10556
    private val championStatueOpen = Scenery.CHAMPION_STATUE_10557
    private val trapdoorID = Scenery.TRAPDOOR_10558
    private val trapdoorOpen = Scenery.TRAPDOOR_10559
    private val regionID = 12696

    override fun defineListeners() {
        on(ChampionScrollsDropHandler.SCROLLS, IntType.ITEM, "read") { player, node ->
            updateAndReadScroll(player, node.asItem())
            return@on true
        }

        on(trapdoorID, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), trapdoorOpen, 100, node.location)
            return@on true
        }

        onUseWith(IntType.NPC, ChampionScrollsDropHandler.SCROLLS, NPCs.LARXUS_3050) { player, _, _ ->
            openDialogue(player, LarxusDialogueFile(true))
            return@onUseWith true
        }

        on(trapdoorOpen, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), trapdoorID, -1, node.location)
            return@on true
        }

        on(championStatue, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), championStatueOpen, 100, node.location)
            return@on true
        }

        on(ladderID, IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(3185, 9758, 0))
            return@on true
        }

        on(championStatueOpen, IntType.SCENERY, "climb-down") { player, _ ->
            teleport(player, Location.create(3182, 9758, 0))
            return@on true
        }

        on(portcullisScenery, IntType.SCENERY, "open") { player, node ->
            if (player.location == Location.create(3181, 9758, 0)) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                playAudio(player, Sounds.PORTCULLIS_OPEN_83)
                playAudio(player, Sounds.PORTCULLIS_CLOSE_82, 2)
                return@on true
            }
            if (!player.getAttribute("championsarena:start", false)) {
                sendNPCDialogue(
                    player,
                    NPCs.LARXUS_3050,
                    "You need to arrange a challenge with me before you enter the arena.",
                )
            } else {
                lock(player, 3)
                submitWorldPulse(
                    object : Pulse() {
                        private var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                2 -> {
                                    playAudio(player, Sounds.PORTCULLIS_OPEN_83)
                                    playAudio(player, Sounds.PORTCULLIS_CLOSE_82, 2)
                                    DoorActionHandler.handleDoor(player, node.asScenery())
                                    if (!player.musicPlayer.hasUnlocked(Music.VICTORY_IS_MINE_528)) {
                                        player.musicPlayer.unlock(Music.VICTORY_IS_MINE_528, true)
                                    }
                                }

                                3 -> handleChampionSpawn(player)
                            }
                            return false
                        }

                        private fun handleChampionSpawn(player: Player) {
                            when {
                                removeItem(player, impScroll) -> spawnImpChampion(player)
                                removeItem(player, goblinScroll) -> spawnGoblinChampion(player)
                                removeItem(player, skeletonScroll) -> spawnSkeletonChampion(player)
                                removeItem(player, zombieScroll) -> spawnZombieChampion(player)
                                removeItem(player, giantScroll) -> spawnGiantChampion(player)
                                removeItem(player, hobgoblinScroll) -> spawnHobgoblinChampion(player)
                                removeItem(player, ghoulScroll) -> spawnGhoulChampion(player)
                                removeItem(player, earthWarriorScroll) -> {
                                    spawnEarthWarriorChampion(player)
                                    setAttribute(player, GameAttributes.PRAYER_LOCK, true)
                                }

                                removeItem(player, jogreScroll) -> spawnJogreChampion(player)
                                removeItem(player, lesserDemonScroll) -> spawnLesserDemonChampion(player)
                                else -> handleFinalChampionSpawn(player)
                            }
                        }

                        private fun handleFinalChampionSpawn(player: Player) {
                            if (getAttribute(player, "championsarena:boss", false)) {
                                if (freeSlots(player) != 28) {
                                    sendNPCDialogue(
                                        player,
                                        NPCs.LARXUS_3050,
                                        "His special rule is that no items in inventory can be brought to arena, only equipped items are allowed.",
                                    )
                                    return
                                }
                                removeAttribute(player, "championsarena:boss")
                                spawnFinalChampion(player)
                            }
                        }
                    },
                )
            }
            return@on true
        }
    }

    private fun updateAndReadScroll(
        player: Player,
        item: Item,
    ) {
        val id = item.id
        openInterface(player, blankScroll)

        val scrollContentMap =
            mapOf(
                impScroll to impScrollContent,
                goblinScroll to goblinScrollContent,
                skeletonScroll to skeletonScrollContent,
                zombieScroll to zombieScrollContent,
                giantScroll to giantScrollContent,
                hobgoblinScroll to hobgoblinScrollContent,
                ghoulScroll to ghoulScrollContent,
                earthWarriorScroll to earthWarriorScrollContent,
                jogreScroll to jogreScrollContent,
                lesserDemonScroll to lesserDemonScrollContent,
            )

        scrollContentMap[id]?.let { content ->
            sendString(player, content.joinToString("<br>"), blankScroll, 5)
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(trapdoorOpen), "climb-down") { _, _ ->
            return@setDest Location.create(3191, 3355, 0)
        }
    }

    override fun defineAreaBorders(): Array<ZoneBorders> {
        return arrayOf(getRegionBorders(regionID))
    }

    override fun getRestrictions(): Array<ZoneRestriction> {
        return arrayOf(ZoneRestriction.CANNON, ZoneRestriction.FIRES, ZoneRestriction.RANDOM_EVENTS)
    }

    companion object {
        fun isFinalBattle(player: Player) {
            val varbits = (1452..1461).toList()
            val allVarbitsSet = varbits.all { getVarbit(player, it) == 1 }

            if (allVarbitsSet) {
                player.lock(1)
                sendNPCDialogueLines(
                    player,
                    NPCs.LEON_DCOUR_3067,
                    FaceAnim.NEUTRAL,
                    false,
                    "You have done well brave adventurer, but I would test",
                    "your mettle now. You may arrange the fight with",
                    "Larxus at your leisure.",
                )
                setAttribute(player, "/save:championsarena:boss", true)
            }
        }
    }
}
