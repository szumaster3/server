package content.region.kandarin.khazard.quest.arena.plugin

import content.region.kandarin.khazard.quest.arena.cutscene.FightOneCutscene
import content.region.kandarin.khazard.quest.arena.cutscene.PrisonCutscene
import content.region.kandarin.khazard.quest.arena.dialogue.*
import content.region.kandarin.khazard.quest.arena.npc.GeneralKhazardNPC
import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.world.map.Location
import shared.consts.*

/**
 * Handles all Fight Arena quest-related interactions.
 */
class FightArenaPlugin : InteractionListener {
    companion object {
        const val GENERAL = NPCs.GENERAL_KHAZARD_258
        const val KELVIN = NPCs.KELVIN_260
        const val JOE = NPCs.JOE_261
        const val FIGHT_SLAVE = NPCs.FIGHTSLAVE_262
        const val HENGRAD = NPCs.HENGRAD_263
        const val JEREMY_A = NPCs.JEREMY_SERVIL_265

        const val CELL_DOOR_1 = Scenery.PRISON_DOOR_79
        const val CELL_DOOR_2 = Scenery.PRISON_DOOR_80
        const val MAIN_DOOR = Scenery.DOOR_81

        const val A_LAZY_GUARD_1 = Scenery.A_LAZY_KHAZARD_GUARD_41494
        const val A_LAZY_GUARD_2 = Scenery.A_LAZY_KHAZARD_GUARD_41496
        const val A_LAZY_GUARD_3 = Scenery.A_LAZY_KHAZARD_GUARD_41497

        const val FULL_ARMOR_STAND = Scenery.A_SUIT_OF_ARMOUR_41490
        val FULL_ARMOR_STAND_1 = getScenery(2619, 3196, 0)

        private const val HELMET = Items.KHAZARD_HELMET_74
        private const val ARMOR = Items.KHAZARD_ARMOUR_75
        private const val CELL_KEY = Items.KHAZARD_CELL_KEYS_76

        val Jeremy = NPC(NPCs.JEREMY_SERVIL_265, Location.create(2616, 3167, 0))
        val General = GeneralKhazardNPC(NPCs.GENERAL_KHAZARD_258, Location.create(2605, 3156, 0))
    }

    init {
        Jeremy.init()
        Jeremy.isWalks = true

        General.init()
        General.isWalks = true
    }

    override fun defineListeners() {

        /*
         * Handles dialogue with Jeremy Servil inside the prison area.
         */

        on(NPCs.JEREMY_SERVIL_266, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, JeremyServilDialogueFile(), npc)
            return@on true
        }

        /*
         * Handles dialogue with General Khazard.
         */

        on(GENERAL, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, GeneralKhazardDialogue(), npc)
            return@on true
        }

        /*
         * Handles dialogue with Hengrad (fellow prisoner).
         */

        on(HENGRAD, IntType.NPC, "talk-to") { player, npc ->
            openDialogue(player, HengradDialogue(), npc)
            return@on true
        }

        /*
         * Handles dialogue with a lazy Khazard guards near the arena area.
         */

        on(A_LAZY_GUARD_1, SCENERY, "talk-to") { player, node ->
            openDialogue(player, ALazyGuardDialogue(), node)
            return@on true
        }
        on(A_LAZY_GUARD_3, SCENERY, "talk-to") { player, node ->
            openDialogue(player, ALazyGuardDialogue(), node)
            return@on true
        }

        /*
         * Handles borrowing Khazard armor from the stand.
         */

        on(FULL_ARMOR_STAND, IntType.SCENERY, "borrow") { player, node ->
            val hasArmor = hasAnItem(player, ARMOR).container != null
            val hasHelmet = hasAnItem(player, HELMET).container != null

            if (freeSlots(player) == 0) {
                sendMessage(player, "You could borrow this suit of armour if you had space in your inventory.")
                return@on true
            }

            val scenery = FULL_ARMOR_STAND_1!!.asScenery()

            animate(player, Animations.HUMAN_MULTI_USE_832)

            if (freeSlots(player) > 1 && !hasArmor && !hasHelmet) {
                replaceScenery(scenery, node.id + 18, 10)
                sendMessage(player, "You borrow the full suit of armour. It looks like it's just your size.")
                addItem(player, ARMOR, 1)
                addItem(player, HELMET, 1)
                return@on true
            }

            if (freeSlots(player) > 0) {
                when {
                    hasArmor && !hasHelmet -> {
                        sendMessage(player, "You borrow the helmet. It looks like it's just your size.")
                        replaceScenery(scenery, node.id + 16, 10)
                        addItem(player, HELMET)
                    }
                    hasHelmet && !hasArmor -> {
                        replaceScenery(scenery, node.id + 17, 10)
                        sendMessage(player, "You borrow the armor. It looks like it's just your size.")
                        addItem(player, ARMOR)
                    }
                }
            }
            return@on true
        }

        /*
         * Handles stealing Khazard cell keys from the sleeping guard table.
         */

        on(A_LAZY_GUARD_2, IntType.SCENERY, "steal-keys") { player, _ ->
            face(player, location(2618, 3144, 0))
            setVarbit(player, 5627, 3, true)
            sendGraphics(Graphics.SLEEPING_ZZZ_1056, Location.create(2617, 3144, 0))
            animate(player, Animations.HUMAN_MULTI_USE_832)
            addItemOrDrop(player, CELL_KEY, 1)
            setQuestStage(player, Quests.FIGHT_ARENA, 68)
            sendMessage(player, "You pick up the keys from the table.")
            return@on true
        }

        /*
         * Handles the main gate interaction inside the arena.
         */

        on(MAIN_DOOR, IntType.SCENERY, "open") { player, node ->
            val hasArmour = allInEquipment(player, HELMET, ARMOR)
            when (player.location.y) {
                3171 -> DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                3172 -> if (hasArmour) {
                    openDialogue(player, DoorSupportDialogue(isWest = false))
                } else {
                    sendPlayerDialogue(player, "This door appears to be locked.")
                }
            }
            when (player.location.x) {
                2585 -> DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                2584 -> if (hasArmour) {
                    openDialogue(player, DoorSupportDialogue(isWest = true))
                } else {
                    sendPlayerDialogue(player, "This door appears to be locked.")
                }
            }
            return@on true
        }

        /*
         * Handles using Khazard cell keys on Jeremy cell door.
         */

        onUseWith(IntType.SCENERY, CELL_KEY, CELL_DOOR_2) { player, _, _ ->
            if (getQuestStage(player, Quests.FIGHT_ARENA) == 88) {
                PrisonCutscene(player).start()
            } else if (getQuestStage(player, Quests.FIGHT_ARENA) >= 68) {
                setAttribute(player, "spawn-ogre", true)
                FightOneCutscene(player).start()
            }
            return@onUseWith true
        }

        /*
         * Handles using Khazard cell keys on the wrong cell.
         */

        onUseWith(IntType.SCENERY, CELL_KEY, CELL_DOOR_1) { player, _, _ ->
            if (getQuestStage(player, Quests.FIGHT_ARENA) >= 68) {
                sendDialogue(player, "I don't want to attract too much attention by freeing all the prisoners. I need to find Jeremy and he's not in this cell.")
            } else {
                sendMessage(player, "The cell gate is securely locked.")
            }
            return@onUseWith false
        }

        /*
         * Handles attempting to open cell doors without keys.
         */

        on(CELL_DOOR_1, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "the cell gate is securely locked.")
            return@on false
        }
        on(CELL_DOOR_2, IntType.SCENERY, "open") { player, _ ->
            sendMessage(player, "the cell gate is securely locked.")
            return@on false
        }
    }

    override fun defineDestinationOverrides() {

        /*
         * Handles destination for talking to Jeremy Servil in his cell.
         */

        setDest(IntType.NPC, intArrayOf(JEREMY_A), "talk-to") { _, _ ->
            return@setDest Location.create(2617, 3167, 0)
        }

        /*
         * Handles destination pathing for fight slave NPCs.
         */

        setDest(IntType.NPC, intArrayOf(FIGHT_SLAVE), "talk-to") { player, _ ->
            if (inBorders(player, 2585, 3139, 2605, 3148)) {
                return@setDest Location.create(2595, 3141, 0)
            } else if (inBorders(player, 2616, 3162, 2617, 3165)) {
                return@setDest Location.create(2617, 3163, 0)
            } else {
                return@setDest Location.create(2617, 3159, 0)
            }
        }

        /*
         * Handles destination for Kelvin NPC interaction.
         */

        setDest(IntType.NPC, intArrayOf(KELVIN), "talk-to") { _, _ ->
            return@setDest Location.create(2589, 3141, 0)
        }

        /*
         * Handles destination for Joe NPC interaction.
         */

        setDest(IntType.NPC, intArrayOf(JOE), "talk-to") { _, _ ->
            return@setDest Location.create(2589, 3141, 0)
        }

        /*
         * Handles destination for stealing keys from the lazy guard table.
         */

        setDest(IntType.SCENERY, intArrayOf(A_LAZY_GUARD_2), "steal-keys") { _, _ ->
            return@setDest Location.create(2619, 3143, 0)
        }
    }
}
