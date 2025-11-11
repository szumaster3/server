package content.global.activity.creation

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.music.MusicEntry
import core.game.world.map.Location
import shared.consts.*

/**
 * Handles interactions related to creature creation.
 */
class CreatureCreationPlugin : InteractionListener {

    private val allMaterialIds = CreatureCreation.values().flatMap { it.materials }.distinct().toIntArray()

    override fun defineListeners() {

        /*
         * Handles entrance to creature creation area.
         */

        on(Scenery.TRAPDOOR_21921, IntType.SCENERY, "open") { player, _ ->
            if (!hasRequirement(player, Quests.TOWER_OF_LIFE, false)) {
                sendDialogue(player, "The trapdoor won't open.")
                return@on true
            }

            setVarbit(player, Vars.VARBIT_TOL_TRAPDOOR_3372, 1)
            sendMessage(player, "You open the trapdoor.")
            return@on true
        }

        /*
         * Handles closing the trapdoor.
         */

        on(NPCs.HOMUNCULUS_5581, IntType.SCENERY, "talk-to") { player, _ ->
            openDialogue(player, HomunculusDialogue())
            return@on true
        }

        /*
         * Handles close the trapdoor.
         */

        on(Scenery.TRAPDOOR_21922, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, Vars.VARBIT_TOL_TRAPDOOR_3372, 0)
            sendMessage(player, "You close the trapdoor.")
            return@on true
        }

        /*
         * Handles interaction with the symbol of life scenery.
         */

        on(Scenery.SYMBOL_OF_LIFE_21893, IntType.SCENERY, "inspect") { player, node ->
            CreatureCreation.forLocation(node.location)?.let { symbol ->
                val altarCharge = getCharge(node.asScenery())
                val placedItems = symbol.materials.filter { itemId -> (altarCharge and (1 shl itemId)) != 0 }
                if (placedItems.isNotEmpty()) {
                    sendMessage(player, "You already placed the ${placedItems.joinToString(", ") { getItemName(it) }} on the altar!")
                }

                sendDialogue(player, "You see some text scrolled above the altar on a symbol...")
                addDialogueAction(player) { _, _ ->
                    sendDoubleItemDialogue(
                        player,
                        symbol.materials.elementAt(0),
                        symbol.materials.elementAt(1),
                        "${symbol.description}..."
                    )
                }
            }
            return@on true
        }

        /*
         * Handles add the resources to symbol of life
         */
        onUseWith(IntType.SCENERY, allMaterialIds, Scenery.SYMBOL_OF_LIFE_21893) { player, used, with ->
            val item = used.asItem()
            val altar = with.asScenery()
            val symbol = CreatureCreation.forItemId(item.id) ?: return@onUseWith true
            if (altar.location != symbol.location) {
                sendMessage(player, "You can't reach.")
                return@onUseWith true
            }

            val currentCharge = getCharge(altar)
            val materialBit = 1 shl item.id
            val itemName = getItemName(item.id).lowercase()

            if (currentCharge and materialBit != 0) {
                sendMessage(player, "You already placed the $itemName on the altar!")
            } else {
                player.lock(1)
                removeItem(player, item.id)
                animate(player, Animations.HUMAN_BURYING_BONES_827)
                sendDialogueLines(player, "You place the $itemName on the altar.")
                setCharge(altar, currentCharge or materialBit)
            }
            return@onUseWith true
        }

        /*
         * Handles activating the symbol of life scenery.
         */

        on(Scenery.SYMBOL_OF_LIFE_21893, IntType.SCENERY, "activate") { player, node ->
            val altar = node.asScenery()
            val symbol = CreatureCreation.forLocation(altar.location)
            if (symbol != null) {
                val altarCharge = getCharge(altar)
                if (symbol.materials.all { altarCharge and (1 shl it) != 0 }) {
                    activateAltar(player, symbol, node)
                } else {
                    sendNPCDialogue(player, NPCs.HOMUNCULUS_5581, "You no haveee the two materials need.", FaceAnim.OLD_NORMAL)
                }
            }
            return@on true
        }

        /*
         * Handles climb to 2nd floor of tower of life.
         */

        on(Scenery.STAIRS_21871, IntType.SCENERY, "climb-up") { player, _ ->
            val musicId = Music.WORK_WORK_WORK_237
            player.musicPlayer.play(MusicEntry.forId(musicId))
            if (!player.musicPlayer.hasUnlocked(musicId)) {
                player.musicPlayer.unlock(musicId)
            }
            return@on true
        }
    }

    /**
     * Activates the altar with the provided materials.
     */
    private fun activateAltar(player: Player, symbol: CreatureCreation, node: Node) {
        sendNPCDialogue(player, NPCs.HOMUNCULUS_5581, "You have the materials needed. Here goes!", FaceAnim.OLD_NORMAL)
        addDialogueAction(player) { _, button ->
            if (button >= 5) {
                player.lock(2)
                setCharge(node.asScenery(), 0)
                animateScenery(node.asScenery(), 5844)
                spawnCreature(player, symbol)
            } else {
                player.sendMessage("Nothing interesting happens.")
            }
        }
    }

    /**
     * Spawns the npc for the given symbol.
     */
    private fun spawnCreature(player: Player, symbol: CreatureCreation) {
        val spawnLocation = if (symbol.location == CreatureCreation.UNICOW_SPAWN_BASE)
            Location.getRandomLocation(CreatureCreation.UNICOW_SPAWN_RANDOM_BASE, 2, true)
        else
            Location.create(symbol.location.x - 1, symbol.location.y - 3, 0)

        val creature = core.game.node.entity.npc.NPC.create(symbol.npcId, spawnLocation)
        playAudio(player, Sounds.TOL_CREATURE_APPEAR_3417)
        runTask(player, 2) {
            creature.isWalks = true
            creature.isNeverWalks = false
            creature.isAggressive = true
            creature.isRespawn = false
            creature.init()
            creature.attack(player)
        }
    }
}


/**
 * Represents the creature creation combinations.
 */
private enum class CreatureCreation(val npcId: Int, val location: Location, val materials: Set<Int>, val description: String,) {
    NEWROOST(   NPCs.NEWTROOST_5597,  Location(3058, 4410, 0), setOf(Items.FEATHER_314, Items.EYE_OF_NEWT_221), "Feather of chicken and eye of newt"),
    UNICOW(     NPCs.UNICOW_5603,     Location(3018, 4410, 0), setOf(Items.COWHIDE_1739, Items.UNICORN_HORN_237), "Horn of unicorn and hide of cow",),
    SPIDINE(    NPCs.SPIDINE_5594,    Location(3043, 4361, 0), setOf(Items.RED_SPIDERS_EGGS_223, Items.RAW_SARDINE_327), "Red spiders' eggs and a sardine raw",),
    SWORDCHICK( NPCs.SWORDCHICK_5595, Location(3034, 4361, 0), setOf(Items.RAW_SWORDFISH_371, Items.RAW_CHICKEN_2138), "Swordfish raw and chicken uncooked",),
    JUBSTER(    NPCs.JUBSTER_5596,    Location(3066, 4380, 0), setOf(Items.RAW_JUBBLY_7566, Items.RAW_LOBSTER_377), "Raw meat of jubbly bird and a lobster raw"),
    FROGEEL(    NPCs.FROGEEL_5593,    Location(3012, 4380, 0), setOf(Items.GIANT_FROG_LEGS_4517, Items.RAW_CAVE_EEL_5001), "Legs of giant frog and a cave eel uncooked");

    companion object {
        private val byLocation: Map<Location, CreatureCreation>
        private val byItemId: Map<Int, CreatureCreation>

        init {
            val locationMap = mutableMapOf<Location, CreatureCreation>()
            val itemMap = mutableMapOf<Int, CreatureCreation>()

            for (c in values()) {
                locationMap[c.location] = c
                for (item in c.materials) {
                    itemMap[item] = c
                }
            }

            byLocation = locationMap
            byItemId = itemMap
        }

        fun forLocation(location: Location): CreatureCreation? = byLocation[location]
        fun forItemId(itemId: Int): CreatureCreation? = byItemId[itemId]

        val UNICOW_SPAWN_BASE = Location(3018, 4410, 0)
        val UNICOW_SPAWN_RANDOM_BASE = Location(3022, 4403, 0)
    }
}