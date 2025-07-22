package content.global.activity.creation

import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.music.MusicEntry
import core.game.world.map.Location
import org.rs.consts.*

/**
 * Represents the creature creation combinations.
 */
private enum class CreatureCreation(val material: String, val npcId: Int, val location: Location, val firstMaterial: Int, val secondMaterial: Int, ) {
    NEWROOST("Feather of chicken and eye of newt", NPCs.NEWTROOST_5597, Location(3058, 4410, 0), Items.FEATHER_314, Items.EYE_OF_NEWT_221),
    UNICOW("Horn of unicorn and hide of cow", NPCs.UNICOW_5603, Location(3018, 4410, 0), Items.COWHIDE_1739, Items.UNICORN_HORN_237),
    SPIDINE("Red spiders' eggs and a sardine raw", NPCs.SPIDINE_5594, Location(3043, 4361, 0), Items.RED_SPIDERS_EGGS_223, Items.RAW_SARDINE_327),
    SWORDCHICK("Swordfish raw and chicken uncooked", NPCs.SWORDCHICK_5595, Location(3034, 4361, 0), Items.RAW_SWORDFISH_371, Items.RAW_CHICKEN_2138),
    JUBSTER("Raw meat of jubbly bird and a lobster raw", NPCs.JUBSTER_5596, Location(3066, 4380, 0), Items.RAW_JUBBLY_7566, Items.RAW_LOBSTER_377),
    FROGEEL("Legs of giant frog and a cave eel uncooked", NPCs.FROGEEL_5593, Location(3012, 4380, 0), Items.GIANT_FROG_LEGS_4517, Items.RAW_CAVE_EEL_5001);

    /**
     * List of item ids needed to create the creature.
     */
    val materials: List<Int> = listOf(firstMaterial, secondMaterial)

    companion object {
        /**
         * Returns the [CreatureCreation] for the given location, or null if none matches.
         */
        @JvmStatic
        fun forLocation(location: Location): CreatureCreation? =
            values().find { it.location == location }
    }
}

/**
 * Handles interactions related to creature creation.
 */
class CreatureCreationPlugin : InteractionListener {

    val allMaterials = CreatureCreation.values()
        .flatMap { it.materials }
        .toIntArray()

    override fun defineListeners() {
        /*
         * Handles opening the trapdoor if the player meets the quest requirement.
         */

        on(Scenery.TRAPDOOR_21921, IntType.SCENERY, "open") { player, _ ->
            if (hasRequirement(player, Quests.TOWER_OF_LIFE, false)) {
                setVarbit(player, 3372, 1)
                sendMessage(player, "You open the trapdoor.")
            } else {
                sendDialogue(player, "The trapdoor won't open.")
            }
            return@on true
        }

        /*
         * Handles closing the trapdoor.
         */

        on(Scenery.TRAPDOOR_21922, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, 3372, 0)
            sendMessage(player, "You close the trapdoor.")
            return@on true
        }

        /*
         * Handles climbing stairs and unlocking background music.
         */

        on(Scenery.STAIRS_21871, IntType.SCENERY, "climb-up") { player, _ ->
            player.musicPlayer.play(MusicEntry.forId(Music.WORK_WORK_WORK_237))
            if (!player.musicPlayer.hasUnlocked(Music.WORK_WORK_WORK_237)) {
                player.musicPlayer.unlock(Music.WORK_WORK_WORK_237)
            }
            return@on true
        }

        /*
         * Handles inspecting the Symbol of Life altar.
         */

        on(Scenery.SYMBOL_OF_LIFE_21893, IntType.SCENERY, "inspect") { player, node ->
            CreatureCreation.forLocation(node.location)?.let {
                sendDialogue(player, "You see some text scrolled above the altar on a symbol...")
                addDialogueAction(player) { _, _ ->
                    sendDoubleItemDialogue(player, it.firstMaterial, it.secondMaterial, "${it.material}...")
                }
            }
            return@on true
        }

        /*
         * Handles placing items on the altar for creature creation.
         */

        onUseWith(IntType.SCENERY, allMaterials, Scenery.SYMBOL_OF_LIFE_21893) { player, used, with ->
            val item = used.asItem()
            val symbol = CreatureCreation.values().find { it.materials.contains(item.id) }

            if (symbol == null) return@onUseWith true
            if (with.location != symbol.location) {
                sendMessage(player, "You can't reach.")
                return@onUseWith true
            }

            val symbolAttributeName = "${symbol.name}:${item.id}"
            val symbolMaterialName = getItemName(item.id).lowercase()

            if (getAttribute(player, symbolAttributeName, false)) {
                sendMessage(player, "You already placed the $symbolMaterialName on the altar!")
            } else {
                player.lock(1)
                removeItem(player, item.id)
                animate(player, Animations.HUMAN_BURYING_BONES_827)
                sendDialogueLines(player, "You place the ${getItemName(item.id).lowercase()} on the altar.")
                setAttribute(player, symbolAttributeName, true)
            }
            return@onUseWith true
        }

        /*
         * Handles activating the altar if all required materials are placed.
         */

        on(Scenery.SYMBOL_OF_LIFE_21893, IntType.SCENERY, "activate") { player, node ->
            val symbol = CreatureCreation.forLocation(node.location)
            val required = symbol?.materials?.all { player.getAttribute("${symbol.name}:$it", false) }
            if (symbol != null && required == true) {
                activateAltar(player, symbol, node)
            } else {
                sendNPCDialogue(player, NPCs.HOMUNCULUS_5581, "You no haveee the two materials need.", FaceAnim.OLD_NORMAL)
            }
            return@on true
        }

        /*
         * Initiates a dialogue with the Homunculus NPC.
         */

        on(NPCs.HOMUNCULUS_5581, IntType.NPC, "talk-to") { player, node ->
            dialogue(player) {
                player(FaceAnim.HALF_ASKING, "Hi there, you mentioned something about creating monsters...?")
                npc(node.id, FaceAnim.OLD_NORMAL, "Good! I gain know from alchemists and builders. Me make beings.")
                player(FaceAnim.THINKING, "Interesting. Tell me if I'm right.")
                player(FaceAnim.THINKING, "By the alchemists and builders creating you, you have inherited their combined knowledge in much the same way that a child might inherit the looks of their parents.")
                npc(node.id, FaceAnim.OLD_NORMAL, "Yes, you right!")
                player(FaceAnim.HALF_ASKING, "So what do you need me to do?")
                npc(node.id, FaceAnim.OLD_NORMAL, "Inspect symbol of life altars around dungeon. You see item give. Use item on altar. Activate altar to create, you fight.")
                player(FaceAnim.NOD_YES, "Okay. Sounds like a challenge.")
            }
            return@on true
        }
    }

    /**
     * Activates the altar to begin creature creation.
     */
    private fun activateAltar(player: Player, symbol: CreatureCreation, node: Node, ) {
        sendNPCDialogue(player, NPCs.HOMUNCULUS_5581, "You have the materials needed. Here goes!", FaceAnim.OLD_NORMAL)
        addDialogueAction(player) { _, button ->
            if (button >= 5) {
                replaceScenery(node.asScenery(), node.id + 1, 3)
                spawnCreature(player, symbol)
                symbol.materials.forEach {
                    removeAttributes(player, "${symbol.name}:$it")
                }
            } else {
                player.sendMessage("Nothing interesting happens.")
            }
        }
    }

    /**
     * Spawns a creature at the designated location after altar activation.
     */
    private fun spawnCreature(player: Player, symbol: CreatureCreation) {
        val spawnLocation = if (symbol.location == Location(3018, 4410, 0))
            Location.getRandomLocation(Location(3022, 4403, 0), 2, true)
        else
            Location.create(symbol.location.x - 1, symbol.location.y - 3, 0)

        val creature = core.game.node.entity.npc.NPC.create(symbol.npcId, spawnLocation)
        runTask(player, 2) {
            playAudio(player, 3417)
            creature.apply {
                init()
                attack(player)
                isRespawn = false
            }
        }
    }
}
