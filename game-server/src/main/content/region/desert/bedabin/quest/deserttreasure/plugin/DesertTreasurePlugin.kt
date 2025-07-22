package content.region.desert.bedabin.quest.deserttreasure.plugin

import content.region.desert.bedabin.quest.deserttreasure.DTUtils
import content.region.desert.bedabin.quest.deserttreasure.DesertTreasure
import core.api.*
import core.game.activity.Cutscene
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import org.rs.consts.*

class DesertTreasurePlugin : InteractionListener {
    var temp = 6517

    override fun defineListeners() {
        val ladderMapping =
            mapOf(
                Scenery.LADDER_6497 to Pair(Location(2913, 4954, 3), Location(3233, 2898, 0)),
                Scenery.LADDER_6498 to Pair(Location(2846, 4964, 2), Location(2909, 4963, 3)),
                Scenery.LADDER_6499 to Pair(Location(2782, 4972, 1), Location(2845, 4973, 2)),
                Scenery.LADDER_6500 to Pair(Location(3233, 9293, 0), Location(2783, 4941, 1)),
            )

        ladderMapping.forEach { (ladder, locations) ->
            on(ladder, SCENERY, "climb-down") { player, _ ->
                teleport(player, locations.first)
                return@on true
            }
            on(ladder, SCENERY, "climb-up") { player, _ ->
                teleport(player, locations.second)
                return@on true
            }
        }

        val mirrorCutscenes =
            mapOf(
                Scenery.MYSTICAL_MIRROR_6423 to SouthMirrorLookCutscene::class,
                Scenery.MYSTICAL_MIRROR_6425 to SouthWestMirrorLookCutscene::class,
                Scenery.MYSTICAL_MIRROR_6427 to NorthWestMirrorLookCutscene::class,
                Scenery.MYSTICAL_MIRROR_6429 to NorthMirrorLookCutscene::class,
                Scenery.MYSTICAL_MIRROR_6431 to NorthEastMirrorLookCutscene::class,
                Scenery.MYSTICAL_MIRROR_6433 to SouthEastMirrorLookCutscene::class,
            )

        mirrorCutscenes.forEach { (sceneryId, cutsceneClass) ->
            on(sceneryId, SCENERY, "look-into") { player, _ ->
                cutsceneClass.java
                    .getConstructor(Player::class.java)
                    .newInstance(player)
                    .start()
                sendMessage(player, "You gaze into the mirror...")
                return@on true
            }
        }

        val obeliskMapping =
            mapOf(
                Scenery.OBELISK_6483 to
                    Triple(
                        Items.BLOOD_DIAMOND_4670,
                        DesertTreasure.varbitBloodObelisk,
                        DesertTreasure.bloodDiamond,
                    ),
                Scenery.OBELISK_6486 to
                    Triple(
                        Items.SMOKE_DIAMOND_4672,
                        DesertTreasure.varbitSmokeObelisk,
                        DesertTreasure.smokeDiamond,
                    ),
                Scenery.OBELISK_6489 to
                    Triple(
                        Items.ICE_DIAMOND_4671,
                        DesertTreasure.varbitIceObelisk,
                        DesertTreasure.iceDiamond,
                    ),
                Scenery.OBELISK_6492 to
                    Triple(
                        Items.SHADOW_DIAMOND_4673,
                        DesertTreasure.varbitShadowObelisk,
                        DesertTreasure.shadowDiamond,
                    ),
            )

        obeliskMapping.forEach { (obelisk, diamondData) ->
            val diamond = diamondData.first
            val varbit = diamondData.second
            val attribute = diamondData.third

            onUseWith(IntType.SCENERY, intArrayOf(diamond), obelisk) { player, used, _ ->
                if (getDynLevel(player, Skills.MAGIC) > 50) {
                    if (removeItem(player, used)) {
                        sendMessage(player, "The diamond is absorbed into the pillar.")
                        setVarbit(player, varbit, 1)
                        setAttribute(player, attribute, 1)

                        if (DTUtils.allDiamondsInserted(player)) {
                            sendMessage(player, "The force preventing access to the Pyramid has now vanished.")
                            if (getQuestStage(player, Quests.DESERT_TREASURE) == 9) {
                                setQuestStage(player, Quests.DESERT_TREASURE, 10)
                            }
                        }
                    }
                } else {
                    sendMessage(player, "You are not a powerful enough mage to breach the protective aura.")
                    sendMessage(player, "You need a magic level of at least 50 to enter the Pyramid.")
                }
                return@onUseWith true
            }

            onUseWith(
                IntType.SCENERY,
                intArrayOf(
                    Items.BLOOD_DIAMOND_4670,
                    Items.ICE_DIAMOND_4671,
                    Items.SMOKE_DIAMOND_4672,
                    Items.SHADOW_DIAMOND_4673,
                ),
                obelisk,
            ) { player, _, _ ->
                sendMessage(player, "That doesn't appear to be the correct diamond...")
                return@onUseWith true
            }
        }

        on(intArrayOf(Scenery.PYRAMID_ENTRANCE_6545, Scenery.PYRAMID_ENTRANCE_6547), SCENERY, "open") { player, node ->
            if (DTUtils.allDiamondsInserted(player)) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "A mystical power has sealed this door...")
            }
            return@on true
        }

        on((6512..6517).toIntArray(), SCENERY, "search") { player, _ ->
            sendMessage(player, "You don't find anything interesting.")
            return@on true
        }

        on(Scenery.TUNNEL_6481, SCENERY, "enter") { player, _ ->
            if (isQuestComplete(player, Quests.DESERT_TREASURE)) {
                animate(player, Animations.CRAWL_844)
                player.teleport(Location(3233, 9313, 0), 1)
            } else {
                sendMessage(player, "This passage does not seem to lead anywhere...")
            }
            return@on true
        }

        on(Scenery.PORTAL_6551, SCENERY, "use") { player, _ ->
            teleport(player, Location(3233, 2887, 0))
            return@on true
        }

        val itemData =
            listOf(
                // Magic Logs
                Triple(
                    intArrayOf(Items.MAGIC_LOGS_1513, Items.MAGIC_LOGS_1514),
                    DesertTreasure.magicLogsAmount,
                    12 to "You hand over a magic log.",
                ),
                // Steel Bars
                Triple(
                    intArrayOf(Items.STEEL_BAR_2353, Items.STEEL_BAR_2354),
                    DesertTreasure.steelBarsAmount,
                    6 to "You hand over a steel bar.",
                ),
                // Molten Glass
                Triple(
                    intArrayOf(Items.MOLTEN_GLASS_1775, Items.MOLTEN_GLASS_1776),
                    DesertTreasure.moltenGlassAmount,
                    6 to "You hand over some molten glass.",
                ),
                // Bones
                Triple(
                    intArrayOf(Items.BONES_526, Items.BONES_527),
                    DesertTreasure.bonesAmount,
                    1 to "Thank you, those are enough bones for the spell.",
                ),
                // Ashes
                Triple(
                    intArrayOf(Items.ASHES_592, Items.ASHES_593),
                    DesertTreasure.ashesAmount,
                    1 to "Thank you, that is enough ash for the spell.",
                ),
                // Charcoal
                Triple(
                    intArrayOf(Items.CHARCOAL_973, Items.CHARCOAL_974),
                    DesertTreasure.charcoalAmount,
                    1 to "Thank you, that is enough charcoal for the spell.",
                ),
                // Blood Runes
                Triple(
                    intArrayOf(Items.BLOOD_RUNE_565),
                    DesertTreasure.bloodRunesAmount,
                    1 to "Thank you, that blood rune should be sufficient for the spell.",
                ),
            )

        itemData.forEach { (items, attribute, config) ->
            val (limit, message) = config
            onUseWith(IntType.NPC, items, NPCs.EBLIS_1923) { player, used, _ ->
                if (inInventory(player, used.id)) {
                    val currentAmount = getAttribute(player, attribute, 0)
                    if (currentAmount < limit) {
                        if (removeItem(player, used.id)) {
                            setAttribute(player, attribute, currentAmount + 1)
                            if (message.contains("Thank you")) {
                                sendNPCDialogue(player, NPCs.EBLIS_1923, message)
                            } else {
                                sendMessage(player, message)
                            }
                        }
                    }
                }
                return@onUseWith true
            }
        }
    }
}

class NorthMirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(11322)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                teleport(player, 5, 27)
                moveCamera(5, 27, 1000)
                rotateCamera(6, 27, 1000)
                timedUpdate(1)
            }

            2 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                closeOverlay(player)
                timedUpdate(6)
            }

            3 -> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }

            4 -> {
                end(false) {
                    fadeFromBlack()
                }
            }
        }
    }
}

class NorthEastMirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13878)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                teleport(player, 47, 31)
                moveCamera(47, 31, 1000)
                rotateCamera(43, 31, 1000)
                timedUpdate(1)
            }

            2 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                closeOverlay(player)
                timedUpdate(6)
            }

            3 -> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }

            4 -> {
                end(false) {
                    fadeFromBlack()
                }
            }
        }
    }
}

class SouthEastMirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(13102)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                teleport(player, 36, 20)
                moveCamera(36, 20, 1000)
                rotateCamera(44, 20, 1000)
                timedUpdate(1)
            }

            2 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                closeOverlay(player)
                timedUpdate(6)
            }

            3 -> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }

            4 -> {
                end(false) {
                    fadeFromBlack()
                }
            }
        }
    }
}

class SouthMirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(12845)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                teleport(player, 33, 41)
                moveCamera(33, 41, 1000)
                rotateCamera(33, 39, 990)
                timedUpdate(1)
            }

            2 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                closeOverlay(player)
                timedUpdate(6)
            }

            3 -> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }

            4 -> {
                end(false) {
                    fadeFromBlack()
                }
            }
        }
    }
}

class SouthWestMirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(12590)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                teleport(player, 40, 39)
                moveCamera(40, 39, 1200)
                rotateCamera(10, 39, 1200)
                timedUpdate(1)
            }

            2 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                closeOverlay(player)
                timedUpdate(6)
            }

            3 -> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }

            4 -> {
                end(false) {
                    fadeFromBlack()
                }
            }
        }
    }
}

class NorthWestMirrorLookCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        loadRegion(10037)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(4)
            }

            1 -> {
                teleport(player, 56, 40)
                moveCamera(56, 40, 1400)
                rotateCamera(56, 35, 1000)
                timedUpdate(1)
            }

            2 -> {
                openInterface(player, Components.LEGENDS_MIRROR_155)
                closeOverlay(player)
                timedUpdate(6)
            }

            3 -> {
                closeInterface(player)
                fadeToBlack()
                timedUpdate(4)
            }

            4 -> {
                end(false) {
                    fadeFromBlack()
                }
            }
        }
    }
}
