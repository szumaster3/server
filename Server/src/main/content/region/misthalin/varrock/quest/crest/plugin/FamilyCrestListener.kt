package content.region.misthalin.varrock.quest.crest.plugin

import core.api.*
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

class FamilyCrestListener : InteractionListener {

    override fun defineListeners() {
        onUseWith(IntType.NPC, POISON_ITEM_IDS, NPCs.JOHNATHON_668) { player, used, with ->
            val npc = with.asNpc()
            val poisonPotionItem = used.asItem()
            val stage = getQuestStage(player, Quests.FAMILY_CREST)

            val index = POISON_ITEM_IDS.indexOf(used.id)
            val returnItem = if (index + 1 == POISON_ITEM_IDS.size) Items.VIAL_229 else POISON_ITEM_IDS[index + 1]

            if (stage == 17 && removeItem(player, poisonPotionItem)) {
                addItem(player, returnItem)
                setQuestStage(player, Quests.FAMILY_CREST, 18)
                openDialogue(player, NPCs.JOHNATHON_668, npc)
            }
            return@onUseWith true
        }

        on(LEVER_IDS, IntType.SCENERY, "pull") { player, node ->
            val baseId = if (node.id % 2 == 0) node.id - 1 else node.id
            if (getQuestStage(player, Quests.FAMILY_CREST) == 0) {
                return@on true
            }

            val oldState = player.getAttribute("family-crest:witchaven-lever:$baseId", false)
            val newState = !oldState
            setAttribute(player, "family-crest:witchaven-lever:$baseId", newState)

            val directionText = if (oldState) "down" else "up"
            sendMessage(player, "You pull the lever $directionText.")
            sendMessage(player, "You hear a clunk.")

            animate(player, if (oldState) PULL_DOWN_LEVER_ANIM else PULL_UP_LEVER_ANIM)

            val downLever = (node as Scenery).transform(baseId)
            val upLever = node.transform(baseId + 1)

            if (oldState) {
                replaceScenery(upLever, downLever.id, -1)
            } else {
                replaceScenery(downLever, upLever.id, -1)
            }

            return@on true
        }

        on(doorsIDs, IntType.SCENERY, "open") { player, node ->
            val northA = player.getAttribute("family-crest:witchaven-lever:$NORTH_LEVER_A", false)
            val northB = player.getAttribute("family-crest:witchaven-lever:$NORTH_LEVER_B", false)
            val south  = player.getAttribute("family-crest:witchaven-lever:$SOUTH_LEVER",   false)

            val questComplete = getQuestStage(player, Quests.FAMILY_CREST) >= 100

            val canPass = when (node.id) {
                // NORTH DOOR (2431)
                // - northA must be OFF
                // - AND (south ON OR northB ON)
                NORTH_DOOR_ID -> !northA && (south || northB)

                // HELLHOUND DOOR (2430)
                // - completed quest OR (northA ON AND northB ON AND south OFF)
                HELLHOUND_DOOR_ID -> questComplete || (northA && northB && !south)

                // SOUTH-WEST DOOR (2427)
                // - northA ON AND south OFF
                SOUTH_WEST_DOOR_ID -> northA && !south

                // SOUTH-EAST DOOR (2429)
                // - northA ON AND south ON
                SOUTH_EAST_DOOR_ID -> northA && south

                else -> false
            }

            if (canPass) {
                sendMessage(player, "The door swings open. You go through the door.")
                DoorActionHandler.handleAutowalkDoor(player, node as Scenery)
            } else {
                sendMessage(player, "The door is locked.")
            }

            return@on true
        }

        onUseWith(IntType.ITEM, CREST_PART_IDS, *CREST_PART_IDS) { player, _, _ ->
            if (!inInventory(player, Items.CREST_PART_779)
                || !inInventory(player, Items.CREST_PART_780)
                || !inInventory(player, Items.CREST_PART_781)
            ) {
                sendMessage(player, "You need all three crest parts to combine them.")
                return@onUseWith true
            }

            removeItem(player, Items.CREST_PART_779)
            removeItem(player, Items.CREST_PART_780)
            removeItem(player, Items.CREST_PART_781)
            addItem(player, Items.FAMILY_CREST_782)
            sendMessage(player, "You combine the three crest pieces into the Family Crest.")
            return@onUseWith true
        }
    }

    companion object {
        private val POISON_ITEM_IDS = intArrayOf(Items.ANTIPOISON4_2446, Items.ANTIPOISON3_175, Items.ANTIPOISON2_177, Items.ANTIPOISON1_179)
        private val PULL_DOWN_LEVER_ANIM = Animation(Animations.PULL_DOWN_LEVER_2140)
        private val PULL_UP_LEVER_ANIM = Animation(Animations.PULL_UP_LEVER_2139)
        private const val NORTH_LEVER_A = shared.consts.Scenery.LEVER_2421
        private const val NORTH_LEVER_B = shared.consts.Scenery.LEVER_2425
        private const val SOUTH_LEVER = shared.consts.Scenery.LEVER_2423
        private val LEVER_IDS = intArrayOf(NORTH_LEVER_A, NORTH_LEVER_A + 1, NORTH_LEVER_B, NORTH_LEVER_B + 1, SOUTH_LEVER, SOUTH_LEVER + 1)
        private const val NORTH_DOOR_ID = shared.consts.Scenery.DOOR_2431
        private const val HELLHOUND_DOOR_ID = shared.consts.Scenery.DOOR_2430
        private const val SOUTH_WEST_DOOR_ID = shared.consts.Scenery.DOOR_2427
        private const val SOUTH_EAST_DOOR_ID = shared.consts.Scenery.DOOR_2429
        private val doorsIDs = intArrayOf(NORTH_DOOR_ID, HELLHOUND_DOOR_ID, SOUTH_WEST_DOOR_ID, SOUTH_EAST_DOOR_ID)
        private val CREST_PART_IDS = intArrayOf(Items.CREST_PART_779, Items.CREST_PART_780, Items.CREST_PART_781)
        private val GAUNTLET_IDS = setOf(Items.COOKING_GAUNTLETS_775, Items.GOLDSMITH_GAUNTLETS_776, Items.CHAOS_GAUNTLETS_777, Items.FAMILY_GAUNTLETS_778)

        @JvmStatic
        fun swapGauntlets(player: Player, givingGauntletsId: Int): String {
            if (givingGauntletsId !in GAUNTLET_IDS) {
                throw IllegalArgumentException("givingGauntletsId not in list of legal gauntlets.")
            }
            if (inInventory(player, givingGauntletsId)) {
                val gauntletString = Item(givingGauntletsId).name
                return "You already have the $gauntletString."
            }
            var otherGauntlets = -1
            val otherPossibleGauntlets = GAUNTLET_IDS.toMutableSet()
            otherPossibleGauntlets.remove(givingGauntletsId)
            for (gauntletId in otherPossibleGauntlets) {
                if (inInventory(player, gauntletId)) {
                    otherGauntlets = gauntletId
                }
            }
            if (otherGauntlets == -1) {
                return "You do not have the gauntlets with you in your inventory."
            }
            val fee = 25000
            val shouldBeFree = getAttribute(player, "family-crest:gauntlets", Items.FAMILY_GAUNTLETS_778) == Items.FAMILY_GAUNTLETS_778
            if (!shouldBeFree && !inInventory(player, Items.COINS_995, fee)) {
                return "You do not have enough coins."
            }
            if ((shouldBeFree || removeItem(player, Item(Items.COINS_995, fee))) && removeItem(player, otherGauntlets)) {
                addItem(player, givingGauntletsId)
                setAttribute(player, "/save:family-crest:gauntlets", givingGauntletsId)
            }
            return ""
        }
    }
}
