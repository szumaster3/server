package content.global.activity.warriors_guild.plugin.room

import core.api.*
import core.game.interaction.NodeUsageEvent
import core.game.interaction.QueueStrength
import core.game.interaction.UseWithHandler
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.map.Direction
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneBuilder
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Sounds

@Initializable
class AnimatedRoomPlugin : MapZone("wg animation", true), Plugin<Any> {

    /**
     * Represents types of armour sets available to spawn.
     */
    enum class ArmourSet(val npcId: Int, val tokenAmount: Int, val pieces: IntArray) {
        BRONZE(NPCs.ANIMATED_BRONZE_ARMOUR_4278, 5, intArrayOf(Items.BRONZE_FULL_HELM_1155, Items.BRONZE_PLATEBODY_1117, Items.BRONZE_PLATELEGS_1075)),
        IRON(NPCs.ANIMATED_IRON_ARMOUR_4279, 10, intArrayOf(Items.IRON_FULL_HELM_1153, Items.IRON_PLATEBODY_1115, Items.IRON_PLATELEGS_1067)),
        STEEL(NPCs.ANIMATED_STEEL_ARMOUR_4280, 15, intArrayOf(Items.STEEL_FULL_HELM_1157, Items.STEEL_PLATEBODY_1119, Items.STEEL_PLATELEGS_1069)),
        BLACK(NPCs.ANIMATED_BLACK_ARMOUR_4281, 20, intArrayOf(Items.BLACK_FULL_HELM_1165, Items.BLACK_PLATEBODY_1125, Items.BLACK_PLATELEGS_1077)),
        MITHRIL(NPCs.ANIMATED_MITHRIL_ARMOUR_4282, 25, intArrayOf(Items.MITHRIL_FULL_HELM_1159, Items.MITHRIL_PLATEBODY_1121, Items.MITHRIL_PLATELEGS_1071)),
        ADAMANT(NPCs.ANIMATED_ADAMANT_ARMOUR_4283, 30, intArrayOf(Items.ADAMANT_FULL_HELM_1161, Items.ADAMANT_PLATEBODY_1123, Items.ADAMANT_PLATELEGS_1073)),
        RUNE(NPCs.ANIMATED_RUNE_ARMOUR_4284, 40, intArrayOf(Items.RUNE_FULL_HELM_1163, Items.RUNE_PLATEBODY_1127, Items.RUNE_PLATELEGS_1079));
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (e is Player) {
            val npc = e.getAttribute<NPC>("animated_set")
            if (npc != null && npc.isActive) {
                npc.finalizeDeath(null)
            }
        }
        return true
    }

    private fun animateArmour(player: Player, scenery: Scenery, set: ArmourSet) {
        if (!anyInInventory(player, *set.pieces)) {
            sendDialogueLines(
                player,
                "You need a plate body, plate legs and full helm of the same type to",
                "activate the armour animator."
            )
            return
        }

        if (player.getAttribute<NPC>("animated_set") != null) {
            sendMessage(player, "You already have a set animated.")
            return
        }

        player.lock(10)
        player.animate(Animation.create(Animations.HUMAN_BURYING_BONES_827))
        sendPlainDialogue(player, true, "You place your armour on the platform where it", "disappears...")

        queueScript(player, 0, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    set.pieces.forEach { id ->
                        if (!removeItem(player, id)) {
                            sendMessage(player, "You don't have all required items.")
                            return@queueScript stopExecuting(player)
                        }
                    }

                    playAudio(player, Sounds.WARGUILD_ANIMATE_1909)
                    player.logoutListeners["animation-room"] = { p ->
                        set.pieces.forEach { addItem(p, it) }
                    }

                    sendPlainDialogue(
                        player,
                        true,
                        "The animator hums, something appears to be working.",
                        "You stand back..."
                    )
                    return@queueScript delayScript(player, 4)
                }

                1 -> {
                    playAudio(player, Sounds.WARGUILD_ANIMATOR_ACTIVATE_1910)
                    forceMove(player, player.location, player.location.transform(0, 3, 0), 0, 90, Direction.SOUTH)
                    return@queueScript delayScript(player, 3)
                }

                2 -> {
                    val npc = AnimatedArmourNPC(player, scenery.location, set)
                    setAttribute(player, "animated_set", npc)
                    npc.init()
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        val ids = ArmourSet.values().flatMap { it.pieces.toList() }.toIntArray()

        UseWithHandler.addHandler(
            shared.consts.Scenery.MAGICAL_ANIMATOR_15621,
            UseWithHandler.OBJECT_TYPE,
            object : UseWithHandler(*ids) {
                override fun handle(event: NodeUsageEvent): Boolean {
                    val item = event.usedItem
                    val set = ArmourSet.values().firstOrNull { armour ->
                        armour.pieces.any { it == item.id }
                    }
                    if (set != null) {
                        animateArmour(event.player, event.usedWith as Scenery, set)
                    }
                    return true
                }

                override fun newInstance(arg: Any?): Plugin<Any> = this
            }
        )

        ZoneBuilder.configure(this)
        return this
    }

    override fun fireEvent(identifier: String?, vararg args: Any?): Any? = null

    override fun configure() {
        register(ZoneBorders(2849, 3534, 2861, 3545))
    }
}
