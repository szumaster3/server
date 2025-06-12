package content.region.kandarin.ardougne.quest.tol.plugin

import content.global.skill.thieving.PickpocketListener
import core.api.*
import core.api.quest.hasRequirement
import core.api.utils.WeightBasedTable
import core.api.utils.WeightedItem
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.*

class TowerOfLifePlugin : InteractionListener {
    override fun defineListeners() {
        on(TOWER_DOORS, IntType.SCENERY, "open") { player, node ->
            if (!hasRequirement(player, Quests.TOWER_OF_LIFE)) return@on true
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            if (player.location.y ==
                3225
            ) {
                sendPlayerDialogue(
                    player,
                    "Wow, this place looks special. Best I look around for something to fix.",
                    FaceAnim.AMAZED,
                )
            }
            return@on true
        }

        on(PLANTS, IntType.SCENERY, "search") { player, _ ->
            val roll = RandomFunction.roll(25)
            lock(player, plantSearch.duration)
            animate(player, plantSearch)
            sendMessage(player, "You search the plant...")
            queueScript(player, plantSearch.duration, QueueStrength.NORMAL) {
                if (roll) {
                    sendDialogue(player, "Aha!, You find some trousers!")
                    sendMessage(player, "Try the beckon emote while wearing an item of builders' clothing!")
                    addItemOrDrop(player, Items.BUILDERS_TROUSERS_10864, 1)
                } else {
                    sendMessage(player, "Nope, nothing here.")
                }
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        on(npcIDs, IntType.NPC, "pickpocket") { player, node ->
            val lootTable =
                PickpocketListener.pickpocketRoll(
                    player,
                    50.0,
                    240.0,
                    WeightBasedTable.create(WeightedItem(Items.TRIANGLE_SANDWICH_6962, 1, 1, 1.0, true)),
                )
            val roll = RandomFunction.roll(25)
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space to do that.")
                return@on true
            }

            lock(player, 2)
            animate(player, Animations.PICK_POCKET_881)
            sendMessage(player, "You attempt to pick the man's pocket.")
            if (lootTable != null) {
                lootTable.forEach {
                    player.inventory.add(it)
                }
                rewardXP(player, Skills.THIEVING, 8.0)
                if (roll && freeSlots(player) > 0 && node.asNpc().id == NPCs.NO_FINGERS_5590) {
                    addItem(player, Items.BUILDERS_BOOTS_10865)
                    sendMessage(player, "You pick the man's pocket...and find some boots!")
                    sendMessage(player, "Try the beckon emote while wearing an item of builders' clothing!")
                } else {
                    sendMessage(player, "You pick the man's pocket.")
                }
            } else {
                animate(node.asNpc(), Animations.PUNCH_422, true)
                stun(player, 5)
                sendChat(node.asNpc(), "What do you think you're doing?")
                sendMessage(player, "You fail to pick the man's pocket.")
            }
            return@on true
        }
    }

    companion object {
        const val TOWER_DOORS = Scenery.TOWER_DOOR_21814
        const val PLANTS = Scenery.PLANT_21924
        val npcIDs = intArrayOf(NPCs.BLACK_EYE_5589, NPCs.NO_FINGERS_5590, NPCs.GUMMY_5591, NPCs.THE_GUNS_5592)
        val plantSearch: Animation = Animation.create(Animations.ON_KNEES_GAME_800)
    }
}
