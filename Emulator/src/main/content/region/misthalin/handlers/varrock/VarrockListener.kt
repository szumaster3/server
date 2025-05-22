package content.region.misthalin.handlers.varrock

import content.data.GameAttributes
import content.region.kandarin.quest.biohazard.dialogue.GuidorsWifeDialogue
import content.region.misc.handlers.MinecartTravel
import content.region.misthalin.dialogue.varrock.KnockatDoorDialogue
import content.region.misthalin.dialogue.varrock.SawmillOperatorDialogue
import core.GlobalStatistics
import core.api.*
import core.api.interaction.openNpcShop
import core.api.quest.getQuestPoints
import core.api.quest.getQuestStage
import core.api.ui.closeDialogue
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
import core.game.node.item.GroundItem
import core.game.node.item.GroundItemManager
import core.game.system.task.Pulse
import core.game.world.GameWorld
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
        /*
         * Handles talking to Thessalia.
         */

        on(NPCs.THESSALIA_548, IntType.NPC, "change-clothes") { player, _ ->
            openDialogue(player, NPCs.THESSALIA_548, true, true, true)
            return@on true
        }

        /*
         * Handles searching the broken cart.
         */

        on(Scenery.BROKEN_CART_23055, IntType.SCENERY, "search") { player, _ ->
            sendDialogueLines(
                player,
                "You search the cart but are surprised to find very little there. It's a",
                "little odd for a travelling trader not to have anything to trade.",
            )
            return@on true
        }

        /*
         * Handles opening the Hill Giant doors.
         */

        on(Scenery.DOOR_1804, IntType.SCENERY, "open") { player, node ->
            if (player.location == Location.create(3115, 3449, 0) && !inInventory(player, Items.BRASS_KEY_983)) {
                sendMessage(player, "This door is locked.")
                return@on true
            }
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }

        /*
         * Handles using the brass key on the Hill Giant doors.
         */

        onUseWith(IntType.SCENERY, Items.BRASS_KEY_983, Scenery.DOOR_1804) { player, used, with ->
            if (player.location == Location.create(3115, 3449, 0) && used.id != Items.BRASS_KEY_983) {
                sendMessage(player, "This door is locked.")
                return@onUseWith false
            }
            DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            return@onUseWith true
        }

        /*
         * Handles opening Guidor's house doors.
         */

        on(Scenery.BEDROOM_DOOR_2032, IntType.SCENERY, "open") { player, node ->
            val questStage = getQuestStage(player, Quests.BIOHAZARD)
            val hasGownTop = inEquipment(player, Items.PRIEST_GOWN_426)
            val hasGownBottom = inEquipment(player, Items.PRIEST_GOWN_428)
            val hasAnyGown = hasGownTop || hasGownBottom

            if (questStage < 11) {
                sendNPCDialogue(
                    player,
                    NPCs.GUIDORS_WIFE_342,
                    "Please leave my husband alone. He's very sick, and I don't want anyone bothering him.",
                    FaceAnim.SAD
                )
            } else {
                when {
                    !hasAnyGown -> openDialogue(player, GuidorsWifeDialogue())
                    hasGownTop && !hasGownBottom -> {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        if (player.location.x == 3282) sendMessage(player, "Guidor's wife allows you to go in.")
                    }
                    else -> sendNPCDialogue(
                        player,
                        NPCs.GUIDORS_WIFE_342,
                        "Please leave my husband alone. He's very sick, and I don't want anyone bothering him.",
                        FaceAnim.SAD
                    )
                }
            }
            return@on true
        }

        /*
         * Handles opening the Champion's Guild doors.
         */

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

        /*
         * Handles opening the Champion's Guild chest and the Varrock Chuck clue chest.
         */

        on(Scenery.CLOSED_CHEST_24203, IntType.SCENERY, "open") { player, node ->
            val inGuild = inBorders(player, getRegionBorders(CHAMPIONS_GUILD_REGION))
            val hasClue = inInventory(player, Items.CLUE_SCROLL_10222, 1)
            val hasKey = inInventory(player, Items.KEY_2834, 1)

            when {
                inGuild -> {
                    replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_24204, -1, node.location)
                }
                hasClue && hasKey -> {
                    if (removeItem(player, Items.KEY_2834)) {
                        replaceScenery(node.asScenery(), Scenery.OPEN_CHEST_24204, 80, node.location)
                    } else {
                        sendMessage(player, "The chest is locked.")
                    }
                }
                else -> sendMessage(player, "The chest is locked.")
            }
            return@on true
        }

        /*
         * Handles closing the chest.
         */

        on(Scenery.OPEN_CHEST_24204, IntType.SCENERY, "shut") { _, node ->
            replaceScenery(node.asScenery(), Scenery.CLOSED_CHEST_24203, -1, node.location)
            return@on true
        }

        /*
         * Handles searching the Champion's Guild chest.
         */

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

        /*
         * Handles knocking at the door.
         */

        on(Scenery.DOOR_24389, IntType.SCENERY, "knock-at") { player, node ->
            openDialogue(player, KnockatDoorDialogue(), node.asScenery())
            return@on true
        }

        /*
         * Handles opening the Cook's Guild doors.
         */
        on(intArrayOf(Scenery.DOOR_2712, Scenery.DOOR_26810), IntType.SCENERY, "open") { player, node ->
            val requiredItems = anyInEquipment(
                player,
                Items.CHEFS_HAT_1949,
                Items.COOKING_CAPE_9801,
                Items.COOKING_CAPET_9802,
                Items.VARROCK_ARMOUR_3_11758,
            )

            when (node.id) {
                26810 -> {
                    val hasVarrockArmour = inEquipment(player, Items.VARROCK_ARMOUR_3_11758)
                    val cookingLevel = getStatLevel(player, Skills.COOKING)
                    val isMembers = GameWorld.settings?.isMembers ?: true

                    if (player.location.x <= 3143 && (!hasVarrockArmour || cookingLevel < 99)) {
                        if (!isMembers) {
                            sendNPCDialogue(player, NPCs.HEAD_CHEF_847, "The bank's closed. You just can't get the staff these days.")
                        } else {
                            sendNPCDialogue(player, NPCs.HEAD_CHEF_847, "You need to have completed the hard Varrock diary and have 99 Cooking to enter.")
                        }
                    } else {
                        DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    }
                }

                2712 -> {
                    val cookingLevel = getStatLevel(player, Skills.COOKING)
                    val hasVarrockArmour = inEquipment(player, Items.VARROCK_ARMOUR_3_11758)
                    val yPos = player.location.y

                    when {
                        cookingLevel < 32 -> {
                            sendNPCDialogue(player, NPCs.HEAD_CHEF_847,
                                if (requiredItems)
                                    "Sorry. Only the finest chefs are allowed in here. Get your cooking level up to 32."
                                else
                                    "Sorry. Only the finest chefs are allowed in here. Get your cooking level up to 32 and come back wearing a chef's hat."
                            )
                        }

                        !requiredItems && yPos <= 3443 -> {
                            sendNPCDialogueLines(
                                player,
                                NPCs.HEAD_CHEF_847,
                                FaceAnim.NEUTRAL,
                                false,
                                "You can't come in here unless you're wearing a chef's",
                                "hat, or something like that."
                            )
                        }

                        else -> {
                            if (hasVarrockArmour) {
                                sendNPCDialogue(player, NPCs.HEAD_CHEF_847, "My word! A master explorer of Varrock! Come in, come in! You are more than welcome in here, my friend!")
                            }
                            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                        }
                    }
                }
            }

            return@on true
        }

        /*
         * Handles picking berries from bushes.
         */

        on(BERRIES, IntType.SCENERY, "pick-from") { player, node ->
            val emptyBushes = setOf(Scenery.REDBERRY_BUSH_23630, Scenery.CADAVA_BUSH_23627)
            val redBerryBushes = setOf(Scenery.REDBERRY_BUSH_23628, Scenery.REDBERRY_BUSH_23629)


            if (node.id in emptyBushes) {
                sendMessage(player, "There are no berries left on this bush.")
                sendMessage(player, "More berries will grow soon.")
                return@on true
            }

            if (freeSlots(player) == 0) {
                sendMessage(player, "Your inventory is too full to pick the berries from the bush.")
                return@on true
            }

            player.lock(1)
            runTask(player, 0) {
                animate(player, Animations.PICK_SOMETHING_UP_FROM_GROUND_2282)
                val berriesId = if (node.id in redBerryBushes) Items.REDBERRIES_1951 else Items.CADAVA_BERRIES_753
                addItem(player, berriesId)

                if (COUNTER == 2) {
                    val newSceneryId =
                        if (node.id in redBerryBushes) Scenery.REDBERRY_BUSH_23630 else Scenery.CADAVA_BUSH_23627
                    replaceScenery(node.asScenery(), newSceneryId, 30)
                    COUNTER = 0
                } else {
                    COUNTER++
                }
            }
            return@on true
        }

        /*
         * Zone for Varrock Guards to track pickpocket attempts.
         */

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

        /*
         * Handles reading the signpost about Varrock guards.
         */

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

        /*
         * Handles climbing down the trapdoor.
         */

        on(Scenery.TRAPDOOR_17985, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(
                player,
                ClimbActionHandler.CLIMB_DOWN,
                Location(3204, 9910),
                "You enter the murky sewers.",
            )
            return@on true
        }

        /*
         * Handles using the portal.
         */

        on(Scenery.PORTAL_28780, IntType.SCENERY, "use") { player, _ ->
            visualize(player, -1, Graphics.CURSE_IMPACT_110)
            queueScript(player, 1, QueueStrength.SOFT) {
                teleport(player, Location(3326, 5469, 0))
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles reading the plaque.
         */

        on(Scenery.PLAQUE_23636, IntType.SCENERY, "read") { player, _ ->
            openInterface(player, Components.SOA_PLAQUE_531)
            return@on true
        }

        /*
         * Handles climbing down the ladder.
         */

        on(Scenery.LADDER_1749, IntType.SCENERY, "climb-down") { player, _ ->
            val location = if (player.location.z == 2) Location.create(3097, 3432, 1) else Location.create(3096, 3432, 0)
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), location)
            return@on true
        }

        /*
         * Handles opening the drawers.
         */

        on(Scenery.DRAWERS_17466, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.DRAWERS_24322, -1)
            return@on true
        }

        /*
         * Handles closing the drawers.
         */

        on(Scenery.DRAWERS_24322, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.DRAWERS_17466, -1)
            return@on true
        }

        /*
         * Handles opening the hidden trapdoor at GE.
         */

        on(HIDDEN_TRAPDOOR, IntType.SCENERY, "open") { player, _ ->
            val visited = getAttribute(player, GameAttributes.MINECART_TRAVEL_UNLOCK, false)

            if (!visited) {
                sendDialogue(player, "You must visit Keldagrim to use this shortcut.")
            } else {
                sendDialogueLines(
                    player,
                    "This trapdoor leads to a small dwarven mine cart station. The mine",
                    "cart will take you to Keldagrim."
                )

                addDialogueAction(player) { _, _ ->
                    sendDialogueOptions(player, "Select an option", "Travel to Keldagrim.", "Stay here.")
                    addDialogueAction(player) { _, option ->
                        when (option) {
                            2 -> {
                                closeDialogue(player)
                                MinecartTravel.goToKeldagrim(player)
                            }
                            else -> closeDialogue(player)
                        }
                    }
                }
            }

            return@on true
        }

        /*
         * Handles talking to Sawmill Operator.
         */

        on(SAWMILL_OPERATOR, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, SawmillOperatorDialogue())
            return@on true
        }

        /*
         * Handles buy planks interaction with Sawmill Operator.
         */

        on(SAWMILL_OPERATOR, IntType.NPC, "buy-plank") { player, _ ->
            openInterface(player, Components.POH_SAWMILL_403)
            return@on true
        }

        /*
         * Handles trade interaction with Sawmill Operator.
         */

        on(SAWMILL_OPERATOR, IntType.NPC, "trade") { player, _ ->
            openNpcShop(player, SAWMILL_OPERATOR)
            return@on true
        }

        /*
         * Set destination for all Sawmill Operator interactions
         */

        setDest(IntType.NPC, intArrayOf(SAWMILL_OPERATOR), "talk-to", "buy-plank", "trade") { _, _ ->
            return@setDest Location.create(3302, 3491, 0)
        }

        /*
         * Handles reading the Varrock Census book
         */

        on(Scenery.VARROCK_CENSUS_37209, IntType.SCENERY, "read") { player, _ ->
            sendPlayerDialogue(player, "Hmm. The Varrock Census - year 160. That means it's nine years out of date.")
            addDialogueAction(player) { _, buttonID ->
                if (buttonID == 6) {
                    openInterface(player, Components.VM_LECTERN_794)
                }
            }
            return@on true
        }

        /*
         * Handles picking up a cup of tea from the ground.
         */

        on(Items.CUP_OF_TEA_712, IntType.GROUND_ITEM, "take") { player, node ->
            val teaCup = node as GroundItem

            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space for that.")
                return@on true
            }

            val loc = node.location
            val restricted = loc == Location(3272, 3409, 0) || loc == Location(3271, 3413, 0)

            if (restricted) {
                animate(player, Animations.HUMAN_MULTI_USE_832)
                sendNPCDialogue(
                    player,
                    NPCs.TEA_SELLER_595,
                    "Hey! Put that back! Those are for display only!",
                    FaceAnim.ANNOYED,
                )
            } else {
                if (GroundItemManager.destroy(teaCup) != null) {
                    addItem(player, node.id, 1)
                }
            }

            return@on true
        }

        /*
         * Handles trading with the Tea Seller NPC.
         */

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
        private const val HIDDEN_TRAPDOOR = Scenery.HIDDEN_TRAPDOOR_28094
        private const val SAWMILL_OPERATOR = NPCs.SAWMILL_OPERATOR_4250
        private const val CHAMPIONS_GUILD_REGION = 12596
    }
}
