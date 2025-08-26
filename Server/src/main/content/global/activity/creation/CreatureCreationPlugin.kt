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
 * Represents the creature creation combinations.
 */
private enum class CreatureCreation(
    val description: String,
    val npcId: Int,
    val location: Location,
    val materials: Set<Int>
) {
    NEWROOST(
        "Feather of chicken and eye of newt",
        NPCs.NEWTROOST_5597,
        Location(3058, 4410, 0),
        setOf(Items.FEATHER_314, Items.EYE_OF_NEWT_221)
    ),
    UNICOW(
        "Horn of unicorn and hide of cow",
        NPCs.UNICOW_5603,
        Location(3018, 4410, 0),
        setOf(Items.COWHIDE_1739, Items.UNICORN_HORN_237)
    ),
    SPIDINE(
        "Red spiders' eggs and a sardine raw",
        NPCs.SPIDINE_5594,
        Location(3043, 4361, 0),
        setOf(Items.RED_SPIDERS_EGGS_223, Items.RAW_SARDINE_327)
    ),
    SWORDCHICK(
        "Swordfish raw and chicken uncooked",
        NPCs.SWORDCHICK_5595,
        Location(3034, 4361, 0),
        setOf(Items.RAW_SWORDFISH_371, Items.RAW_CHICKEN_2138)
    ),
    JUBSTER(
        "Raw meat of jubbly bird and a lobster raw",
        NPCs.JUBSTER_5596,
        Location(3066, 4380, 0),
        setOf(Items.RAW_JUBBLY_7566, Items.RAW_LOBSTER_377)
    ),
    FROGEEL(
        "Legs of giant frog and a cave eel uncooked",
        NPCs.FROGEEL_5593,
        Location(3012, 4380, 0),
        setOf(Items.GIANT_FROG_LEGS_4517, Items.RAW_CAVE_EEL_5001)
    );

    companion object {
        private val byLocation = values().associateBy { it.location }
        private val byItemId = values().flatMap { c -> c.materials.map { it to c } }.toMap()

        fun forLocation(location: Location): CreatureCreation? = byLocation[location]
        fun forItemId(itemId: Int): CreatureCreation? = byItemId[itemId]

        val UNICOW_SPAWN_BASE = Location(3018, 4410, 0)
        val UNICOW_SPAWN_RANDOM_BASE = Location(3022, 4403, 0)
    }
}

/**
 * Handles interactions related to creature creation.
 */
class CreatureCreationPlugin : InteractionListener {

    private val allMaterialIds = CreatureCreation.values().flatMap { it.materials }.distinct().toIntArray()

    override fun defineListeners() {

        /*
         * Handles open the trapdoor.
         */

        on(Scenery.TRAPDOOR_21921, IntType.SCENERY, "open") { player, _ ->
            if (hasRequirement(player, Quests.TOWER_OF_LIFE, false)) {
                setVarbit(player, Vars.VARBIT_TOL_TRAPDOOR_3372, 1)
                sendMessage(player, "You open the trapdoor.")
            } else {
                sendDialogue(player, "The trapdoor won't open.")
            }
            return@on true
        }

        /*
         * Handles talk to homunculus.
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

        on(Scenery.STAIRS_21871, IntType.SCENERY, "climb-up") { player, _ ->
            val musicId = Music.WORK_WORK_WORK_237
            player.musicPlayer.play(MusicEntry.forId(musicId))
            if (!player.musicPlayer.hasUnlocked(musicId)) {
                player.musicPlayer.unlock(musicId)
            }
            return@on true
        }

        /*
         * Handles inspect the symbol of life.
         */

        on(Scenery.SYMBOL_OF_LIFE_21893, IntType.SCENERY, "inspect") { player, node ->
            CreatureCreation.forLocation(node.location)?.let {
                sendDialogue(player, "You see some text scrolled above the altar on a symbol...")
                addDialogueAction(player) { _, _ ->
                    sendDoubleItemDialogue(
                        player,
                        it.materials.elementAt(0),
                        it.materials.elementAt(1),
                        "${it.description}..."
                    )
                }
            }
            return@on true
        }

        /*
         * Handles add resources to symbol of life.
         */

        onUseWith(IntType.SCENERY, allMaterialIds, Scenery.SYMBOL_OF_LIFE_21893) { player, used, with ->
            val item = used.asItem()
            val symbol = CreatureCreation.forItemId(item.id) ?: return@onUseWith true

            if (with.location != symbol.location) {
                sendMessage(player, "You can't reach.")
                return@onUseWith true
            }

            val key = "${symbol.name}:${item.id}"
            if (getAttribute(player, key, false)) {
                sendMessage(player, "You already placed the ${getItemName(item.id).lowercase()} on the altar!")
            } else {
                player.lock(1)
                removeItem(player, item.id)
                animate(player, Animations.HUMAN_BURYING_BONES_827)
                sendDialogueLines(player, "You place the ${getItemName(item.id).lowercase()} on the altar.")
                setAttribute(player, key, true)
            }
            return@onUseWith true
        }

        /*
         * Handles activate the symbol of life.
         */

        on(Scenery.SYMBOL_OF_LIFE_21893, IntType.SCENERY, "activate") { player, node ->
            val symbol = CreatureCreation.forLocation(node.location)
            if (symbol != null && symbol.materials.all { player.getAttribute("${symbol.name}:$it", false) }) {
                activateAltar(player, symbol, node)
            } else {
                sendNPCDialogue(
                    player,
                    NPCs.HOMUNCULUS_5581,
                    "You no haveee the two materials need.",
                    FaceAnim.OLD_NORMAL
                )
            }
            return@on true
        }

    }

    private fun activateAltar(player: Player, symbol: CreatureCreation, node: Node) {
        sendNPCDialogue(player, NPCs.HOMUNCULUS_5581, "You have the materials needed. Here goes!", FaceAnim.OLD_NORMAL)
        addDialogueAction(player) { _, button ->
            if (button >= 5) {
                replaceScenery(node.asScenery(), node.id + 1, 3)
                spawnCreature(player, symbol)
                symbol.materials.forEach { removeAttributes(player, "${symbol.name}:$it") }
            } else {
                player.sendMessage("Nothing interesting happens.")
            }
        }
    }

    private fun spawnCreature(player: Player, symbol: CreatureCreation) {
        val spawnLocation = if (symbol.location == CreatureCreation.UNICOW_SPAWN_BASE)
            Location.getRandomLocation(CreatureCreation.UNICOW_SPAWN_RANDOM_BASE, 2, true)
        else
            Location.create(symbol.location.x - 1, symbol.location.y - 3, 0)

        val creature = core.game.node.entity.npc.NPC.create(symbol.npcId, spawnLocation)
        runTask(player, 2) {
            playAudio(player, Sounds.TOL_CREATURE_APPEAR_3417)
            creature.init()
            creature.attack(player)
            creature.isRespawn = false
        }
    }
}
