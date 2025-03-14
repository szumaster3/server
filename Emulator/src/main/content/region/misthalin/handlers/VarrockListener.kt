package content.region.misthalin.handlers

import content.region.kandarin.quest.biohazard.dialogue.GuidorsWifeDialogue
import content.region.misc.handlers.MinecartTravel
import content.region.misthalin.dialogue.varrock.KnockatDoorDialogue
import content.region.misthalin.dialogue.varrock.SawmillOperatorDialogue
import core.GlobalStatistics
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.getQuestPoints
import core.api.quest.getQuestStage
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.Option
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.Log
import core.tools.RandomFunction
import org.rs.consts.*

class VarrockListener : InteractionListener {
    override fun defineListeners() {
        on(NPCs.THESSALIA_548, IntType.NPC, "change-clothes") { player, _ ->
            openDialogue(player, NPCs.THESSALIA_548, true, true, true)
            return@on true
        }

        on(Scenery.BROKEN_CART_23055, IntType.SCENERY, "search") { player, _ ->
            sendDialogueLines(
                player,
                "You search the cart but are surprised to find very little there. It's a",
                "little odd for a travelling trader not to have anything to trade.",
            )
            return@on true
        }

        on(Scenery.DOOR_1804, IntType.SCENERY, "open") { player, node ->
            if (player.location == Location.create(3115, 3449, 0) && !inInventory(player, Items.BRASS_KEY_983)) {
                sendMessage(player, "This door is locked.")
                return@on true
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.BRASS_KEY_983, Scenery.DOOR_1804) { player, used, with ->
            if (player.location == Location.create(3115, 3449, 0) && used.id != Items.BRASS_KEY_983) {
                sendMessage(player, "This door is locked.")
                return@onUseWith false
            }
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        on(Scenery.BEDROOM_DOOR_2032, IntType.SCENERY, "open") { player, node ->
            if (!anyInEquipment(player, Items.PRIEST_GOWN_426, Items.PRIEST_GOWN_428) &&
                getQuestStage(player, Quests.BIOHAZARD) >= 11
            ) {
                openDialogue(player, GuidorsWifeDialogue())
            } else if (inEquipment(player, Items.PRIEST_GOWN_426) &&
                !inEquipment(
                    player,
                    Items.PRIEST_GOWN_428,
                ) &&
                getQuestStage(player, Quests.BIOHAZARD) >= 11
            ) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                if (player.location.x == 3282) sendMessage(player, "Guidor's wife allows you to go in.")
            } else {
                sendNPCDialogue(
                    player,
                    NPCs.GUIDORS_WIFE_342,
                    "Please leave my husband alone. He's very sick, and I don't want anyone bothering him.",
                    FaceAnim.SAD,
                )
            }
            return@on true
        }

        on(Scenery.DOOR_1805, IntType.SCENERY, "open") { player, node ->
            if (getQuestPoints(player) < 32) {
                sendDialogue(player, "You have not proved yourself worthy to enter here yet.")
                sendMessage(player, "The door won't open - you need at least 32 Quest Points.")
            } else {
                when (player.location.y) {
                    3363 -> {
                        sendNPCDialogue(
                            player,
                            NPCs.GUILDMASTER_198,
                            "Greetings bold adventurer. Welcome to the guild of Champions.",
                        ).also {
                            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        }
                    }

                    3362 -> DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }
            }
            return@on true
        }

        on(Scenery.CLOSED_CHEST_24203, IntType.SCENERY, "open") { player, node ->
            if (inBorders(player, getRegionBorders(12596))) {
                replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_24204, -1, node.location)
            } else {
                sendMessage(player, "The chest is locked.")
            }
            return@on true
        }

        on(Scenery.OPEN_CHEST_24204, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CLOSED_CHEST_24203, -1, node.location)
            return@on true
        }

        on(Scenery.OPEN_CHEST_24204, IntType.SCENERY, "search") { player, _ ->
            val randomAmount = RandomFunction.random(5, 20)
            sendMessage(player, "You search the chest...")
            if (getAttribute(player, "champions_guild:chest", false)) {
                player.packetDispatch.sendMessage("...but you find nothing of interest.")
                return@on true
            }
            if (freeSlots(player) < 3 || (freeSlots(player) < 2 && !inInventory(player, Items.COINS_995))) {
                sendMessage(player, "Since you can't carry it, you leave it where it is.")
                return@on true
            }
            sendMessage(player, "...and find a gloves, leather boots and small amount of coins.")
            setAttribute(player, "/save:champions_guild:chest", true)
            addItem(player, Items.LEATHER_GLOVES_1059)
            addItem(player, Items.LEATHER_BOOTS_1061)
            addItem(player, Items.COINS_995, randomAmount)
            return@on true
        }

        on(Scenery.DOOR_24389, IntType.SCENERY, "knock-at") { player, node ->
            openDialogue(player, KnockatDoorDialogue(), node.asScenery())
            return@on true
        }

        on(intArrayOf(Scenery.DOOR_2712, Scenery.DOOR_26810), IntType.SCENERY, "open") { player, node ->
            var requiredItems =
                anyInEquipment(
                    player,
                    Items.CHEFS_HAT_1949,
                    Items.COOKING_CAPE_9801,
                    Items.COOKING_CAPET_9802,
                    Items.VARROCK_ARMOUR_3_11758,
                )

            when (node.id) {
                26810 -> {
                    if (!inEquipment(player, Items.VARROCK_ARMOUR_3_11758) && player.location.x <= 3143) {
                        sendNPCDialogue(
                            player,
                            NPCs.HEAD_CHEF_847,
                            "The bank's closed. You just can't get the staff these days.",
                        )
                    } else if (getStatLevel(player, Skills.COOKING) == 99) {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    } else {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    }
                }

                2712 -> {
                    if (getStatLevel(player, Skills.COOKING) < 32) {
                        if (!requiredItems) {
                            sendNPCDialogue(
                                player,
                                NPCs.HEAD_CHEF_847,
                                "Sorry. Only the finest chefs are allowed in here. Get your cooking level up to 32 and come back wearing a chef's hat.",
                            )
                        } else {
                            sendNPCDialogue(
                                player,
                                NPCs.HEAD_CHEF_847,
                                "Sorry. Only the finest chefs are allowed in here. Get your cooking level up to 32.",
                            )
                        }
                    } else if (!requiredItems && player.location.y <= 3443) {
                        sendNPCDialogue(
                            player,
                            NPCs.HEAD_CHEF_847,
                            "You can't come in here unless you're wearing a chef's hat or something like that.",
                        )
                    } else {
                        if (inEquipment(player, Items.VARROCK_ARMOUR_3_11758)) {
                            sendNPCDialogue(
                                player,
                                NPCs.HEAD_CHEF_847,
                                "My word! A master explorer of Varrock! Come in, come in! You are more than welcome in here, my friend!",
                            )
                        }
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    }
                }
            }

            return@on true
        }

        on(BERRIES, IntType.SCENERY, "pick-from") { player, node ->

            if (node.id == 23630 || node.id == 23627) {
                sendMessage(player, "There are no berries left on this bush.")
                sendMessage(player, "More berries will grow soon.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "Your inventory is too full to pick the berries from the bush.")
                return@on true
            }

            stopWalk(player)
            lock(player, 3)
            animate(player, Animations.PICK_SOMETHING_UP_FROM_GROUND_2282)

            if (node.id == 23628 || node.id == 23629) {
                addItem(player, Items.REDBERRIES_1951)
            } else {
                addItem(player, Items.CADAVA_BERRIES_753)
            }

            if (COUNTER == 2) {
                if (node.id != 23628 || node.id != 23629) {
                    replaceScenery(node.asScenery(), 23630, 30)
                } else {
                    replaceScenery(node.asScenery(), 23627, 30)
                }

                COUNTER = 0
                return@on true
            }
            COUNTER++
            return@on true
        }

        val zone =
            object : MapZone("Varrock Guards", true) {
                override fun interact(
                    e: Entity?,
                    target: Node?,
                    option: Option?,
                ): Boolean {
                    if (option != null &&
                        option.name
                            .lowercase()
                            .contains("pickpocket") &&
                        target != null &&
                        target.name.lowercase().contains("guard")
                    ) {
                        GlobalStatistics.incrementGuardPickpockets()
                    }
                    return false
                }
            }

        registerMapZone(zone, ZoneBorders(3225, 3445, 3198, 3471))
        registerMapZone(zone, ZoneBorders(3222, 3375, 3199, 3387))
        registerMapZone(zone, ZoneBorders(3180, 3420, 3165, 3435))
        registerMapZone(zone, ZoneBorders(3280, 3422, 3266, 3435))

        on(Scenery.SIGNPOST_31298, IntType.SCENERY, "read") { player, _ ->
            val pickpocketCount = GlobalStatistics.getDailyGuardPickpockets()
            log(this::class.java, Log.FINE, "Is equal? ${pickpocketCount == 0}")
            when (pickpocketCount) {
                0 ->
                    sendDialogue(
                        player,
                        "The Varrock Palace guards are pleased to announce that crime is at an all-time low, without a single guard in the palace or at the city gates being pickpocketed today.",
                    )

                1 ->
                    sendDialogue(
                        player,
                        "One of the Varrock Palace guards was pickpocketed today. He was close to tears at having lost his last few gold pieces.",
                    )

                else ->
                    sendDialogue(
                        player,
                        "Guards in the Varrock Palace are on full alert due to increasing levels of pickpocketing. So far today, $pickpocketCount guards have had their money pickpocketed in the palace or at the city gates.",
                    )
            }
            return@on true
        }

        on(17985, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_DOWN,
                Location(3204, 9910),
                "You enter the murky sewers.",
            )
            return@on true
        }

        on(Scenery.PORTAL_28780, IntType.SCENERY, "use") { player, _ ->
            visualize(player, -1, Graphics.CURSE_IMPACT_110)
            queueScript(player, 1, QueueStrength.SOFT) {
                teleport(player, Location(3326, 5469, 0))
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(Scenery.PLAQUE_23636, IntType.SCENERY, "read") { player, _ ->
            openInterface(player, Components.SOA_PLAQUE_531)
            return@on true
        }

        on(Scenery.LADDER_1749, IntType.SCENERY, "climb-down") { player, _ ->
            if (player.location.z == 2) {
                ClimbActionHandler.climb(
                    player,
                    Animation(Animations.MULTI_BEND_OVER_827),
                    Location.create(3097, 3432, 1),
                )
            } else {
                ClimbActionHandler.climb(
                    player,
                    Animation(Animations.MULTI_BEND_OVER_827),
                    Location.create(3096, 3432, 0),
                )
            }
            return@on true
        }

        on(Scenery.DRAWERS_17466, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.DRAWERS_24322, -1)
            return@on true
        }

        on(Scenery.DRAWERS_24322, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.DRAWERS_17466, -1)
            return@on true
        }

        on(HIDDEN_TRAPDOOR, IntType.SCENERY, "open") { player, _ ->
            openDialogue(
                player,
                object : DialogueFile() {
                    val keldagrimVisited = getAttribute(player, "keldagrim-visited", false)

                    override fun handle(
                        componentID: Int,
                        buttonID: Int,
                    ) {
                        when (stage) {
                            0 -> {
                                if (!keldagrimVisited) {
                                    end()
                                    sendMessage(player, "Perhaps I should visit Keldagrim first.")
                                } else {
                                    options("Travel to Keldagrim", "Nevermind.").also { stage++ }
                                }
                            }

                            1 ->
                                when (buttonID) {
                                    1 -> MinecartTravel.goToKeldagrim(player).also { end() }
                                    2 -> end()
                                }
                        }
                    }
                },
            )
            return@on true
        }

        on(SAWMILL_OPERATOR, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, SawmillOperatorDialogue())
            return@on true
        }

        on(SAWMILL_OPERATOR, IntType.NPC, "buy-plank") { player, _ ->
            openInterface(player, Components.POH_SAWMILL_403)
            return@on true
        }

        on(SAWMILL_OPERATOR, IntType.NPC, "trade") { player, _ ->
            openNpcShop(player, SAWMILL_OPERATOR)
            return@on true
        }

        setDest(IntType.NPC, intArrayOf(SAWMILL_OPERATOR), "talk-to", "buy-plank", "trade") { _, _ ->
            return@setDest Location.create(3302, 3491, 0)
        }

        on(Scenery.VARROCK_CENSUS_37209, IntType.SCENERY, "read") { player, _ ->
            sendPlayerDialogue(player, "Hmm. The Varrock Census - year 160. That means it's nine years out of date.")
            addDialogueAction(player) { _, buttonID ->
                if (buttonID == 6) {
                    openInterface(player, Components.VM_LECTERN_794)
                }
            }
            return@on true
        }

        on(Items.CUP_OF_TEA_712, IntType.GROUNDITEM, "take") { player, node ->
            animate(player, Animations.HUMAN_MULTI_USE_832)
            if (node.location == Location.create(3272, 3409, 0) || node.location == Location.create(3271, 3413, 0)) {
                sendNPCDialogue(
                    player,
                    NPCs.TEA_SELLER_595,
                    "Hey! Put that back! Those are for display only!",
                    FaceAnim.ANNOYED,
                )
            }
            return@on true
        }

        on(NPCs.TEA_SELLER_595, IntType.NPC, "trade") { player, node ->
            val npc = node.asNpc()
            if (player.getSavedData().globalData.getTeaSteal() > System.currentTimeMillis()) {
                Pulser.submit(
                    object : Pulse(1) {
                        var count: Int = 0

                        override fun pulse(): Boolean {
                            if (count == 0) sendChat(npc, "You're the one who stole something from me!")
                            if (count == 2) {
                                sendChat(npc, "Guards guards!")
                                return true
                            }
                            count++
                            return false
                        }
                    },
                )
                return@on false
            }
            openNpcShop(player, NPCs.TEA_SELLER_595)
            return@on true
        }
    }

    companion object {
        private var COUNTER = 0
        private val BERRIES =
            intArrayOf(
                Scenery.CADAVA_BUSH_23625,
                Scenery.CADAVA_BUSH_23626,
                Scenery.CADAVA_BUSH_23627,
                Scenery.REDBERRY_BUSH_23628,
                Scenery.REDBERRY_BUSH_23629,
                Scenery.REDBERRY_BUSH_23630,
            )
        private val STRAY_DOGS = intArrayOf(NPCs.STRAY_DOG_5917, NPCs.STRAY_DOG_5918)
        private const val HIDDEN_TRAPDOOR = Scenery.HIDDEN_TRAPDOOR_28094
        private const val SAWMILL_OPERATOR = NPCs.SAWMILL_OPERATOR_4250
        private const val RASPBERRY_ANIMATION = Animations.RASPBERRY_2110
    }
}
