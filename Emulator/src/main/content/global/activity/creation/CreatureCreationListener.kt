package content.global.activity.creation

import core.api.*
import core.api.quest.hasRequirement
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.music.MusicEntry
import core.game.world.map.Location
import org.rs.consts.*

/**
 * Listener for handling interactions related to creature creation.
 */
class CreatureCreationListener : InteractionListener {
    override fun defineListeners() {
        /*
         * Handles opening the trapdoor if the player meets the quest requirement.
         */

        on(Scenery.TRAPDOOR_21921, IntType.SCENERY, "open") { player, _ ->
            if (hasRequirement(player, Quests.TOWER_OF_LIFE)) {
                setVarbit(player, 3372, 1)
            }
            return@on true
        }

        /*
         * Handles closing the trapdoor.
         */

        on(Scenery.TRAPDOOR_21922, IntType.SCENERY, "close") { player, _ ->
            setVarbit(player, 3372, 0)
            return@on true
        }

        /*
         * Initiates a dialogue with the Homunculus NPC.
         */

        on(NPCs.HOMUNCULUS_5581, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, HomunculusDialogue())
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
                sendDoubleItemDialogue(player, it.firstMaterial, it.secondMaterial, "${it.material}...")
            }
            return@on true
        }

        /*
         * Handles placing items on the altar for creature creation.
         */

        CreatureCreation.values().forEach { symbol ->
            onUseWith(
                IntType.SCENERY,
                symbol.materials.toIntArray(),
                Scenery.SYMBOL_OF_LIFE_21893,
            ) { player, used, with ->
                val item = used.asItem()
                if (with.location == symbol.location) {
                    if (symbol.materials.contains(item.id)) {
                        val attributeKey = "${symbol.name}:${item.id}"
                        if (!getAttribute(player, attributeKey, false)) {
                            player.lock(1)
                            removeItem(player, item.id)
                            animate(player, Animations.HUMAN_BURYING_BONES_827)
                            sendDialogueLines(player, "You place the ${getItemName(item.id).lowercase()} on the altar.")
                            setAttribute(player, attributeKey, true)
                        } else {
                            sendMessage(
                                player,
                                "You already placed the ${getItemName(item.id).lowercase()} on the altar!",
                            )
                        }
                    }
                } else {
                    sendMessage(player, "You can't reach.")
                }
                return@onUseWith true
            }
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
                sendNPCDialogue(player, NPCs.HOMUNCULUS_5581, "You no haveee the two materials need.")
            }
            return@on true
        }
    }

    /**
     * Activates the altar to begin creature creation.
     */
    private fun activateAltar(
        player: Player,
        symbol: CreatureCreation,
        node: Node,
    ) {
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
    private fun spawnCreature(
        player: Player,
        symbol: CreatureCreation,
    ) {
        val spawnLocation =
            if (symbol.location == Location(3018, 4410, 0)) {
                Location.getRandomLocation(Location(3022, 4403, 0), 2, true)
            } else {
                Location.create(symbol.location.x - 1, symbol.location.y - 3, 0)
            }
        val creature =
            core.game.node.entity.npc.NPC
                .create(symbol.npcId, spawnLocation)
        creature.init()
        creature.attack(player)
        creature.isRespawn = false
    }
}
