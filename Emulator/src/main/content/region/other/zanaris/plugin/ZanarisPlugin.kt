package content.region.other.zanaris.plugin

import content.global.handlers.iface.FairyRingInterface
import content.region.other.keldagrim.dialogue.MagicDoorDialogue
import core.api.*
import core.api.interaction.transformNpc
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.NPCBehavior
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*
import java.lang.Integer.max

class ZanarisPlugin : InteractionListener {

    companion object {
        // Scenery
        private val RINGS = intArrayOf(org.rs.consts.Scenery.FAIRY_RING_12095, org.rs.consts.Scenery.FAIRY_RING_14058, org.rs.consts.Scenery.FAIRY_RING_14061, org.rs.consts.Scenery.FAIRY_RING_14064, org.rs.consts.Scenery.FAIRY_RING_14067, org.rs.consts.Scenery.FAIRY_RING_14070, org.rs.consts.Scenery.FAIRY_RING_14073, org.rs.consts.Scenery.FAIRY_RING_14076, org.rs.consts.Scenery.FAIRY_RING_14079, org.rs.consts.Scenery.FAIRY_RING_14082, org.rs.consts.Scenery.FAIRY_RING_14085, org.rs.consts.Scenery.FAIRY_RING_14088, org.rs.consts.Scenery.FAIRY_RING_14091, org.rs.consts.Scenery.FAIRY_RING_14094, org.rs.consts.Scenery.FAIRY_RING_14097, org.rs.consts.Scenery.FAIRY_RING_14100, org.rs.consts.Scenery.FAIRY_RING_14103, org.rs.consts.Scenery.FAIRY_RING_14106, org.rs.consts.Scenery.FAIRY_RING_14109, org.rs.consts.Scenery.FAIRY_RING_14112, org.rs.consts.Scenery.FAIRY_RING_14115, org.rs.consts.Scenery.FAIRY_RING_14118, org.rs.consts.Scenery.FAIRY_RING_14121, org.rs.consts.Scenery.FAIRY_RING_14124, org.rs.consts.Scenery.FAIRY_RING_14127, org.rs.consts.Scenery.FAIRY_RING_14130, org.rs.consts.Scenery.FAIRY_RING_14133, org.rs.consts.Scenery.FAIRY_RING_14136, org.rs.consts.Scenery.FAIRY_RING_14139, org.rs.consts.Scenery.FAIRY_RING_14142, org.rs.consts.Scenery.FAIRY_RING_14145, org.rs.consts.Scenery.FAIRY_RING_14148, org.rs.consts.Scenery.FAIRY_RING_14151, org.rs.consts.Scenery.FAIRY_RING_14154, org.rs.consts.Scenery.FAIRY_RING_14157, org.rs.consts.Scenery.FAIRY_RING_14160, org.rs.consts.Scenery.FAIRY_RING_16181, org.rs.consts.Scenery.FAIRY_RING_16184, org.rs.consts.Scenery.FAIRY_RING_23047, org.rs.consts.Scenery.FAIRY_RING_27325, org.rs.consts.Scenery.FAIRY_RING_37727)
        private val MAIN_RING = org.rs.consts.Scenery.FAIRY_RING_12128
        private val ENTRY_RING = org.rs.consts.Scenery.FAIRY_RING_12094
        private val MARKETPLACE_RING = org.rs.consts.Scenery.FAIRY_RING_12003

        /**
         * Checks requirements to use rings.
         */
        private fun fairyMagic(player: Player): Boolean {
            if (!core.api.quest.hasRequirement(player, Quests.FAIRYTALE_I_GROWING_PAINS) ||
                !anyInEquipment(player, Items.DRAMEN_STAFF_772, Items.LUNAR_STAFF_9084)
            ) {
                sendMessage(player, "The fairy ring only works for those who weld fairy magic.")
                return false
            }
            return true
        }

        /**
         * Opens the fairy ring interface.
         */
        private fun openFairyRing(player: Player) {
            openInterface(player, FairyRingInterface.RINGS_IFACE)
        }
    }

    override fun defineListeners() {
        /*
         * Handles ring options.
         */

        on(RINGS, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            teleport(player, Location.create(2412, 4434, 0), TeleportManager.TeleportType.FAIRY_RING)
            return@on true
        }

        on(MAIN_RING, IntType.SCENERY, "use") { player, _ ->
            if (!fairyMagic(player)) return@on true
            openFairyRing(player)
            return@on true
        }

        on(ENTRY_RING, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(3203, 3168, 0), TeleportManager.TeleportType.FAIRY_RING)
            return@on true
        }

        on(MARKETPLACE_RING, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location.create(3262, 3167, 0), TeleportManager.TeleportType.FAIRY_RING)
            return@on true
        }

        /*
         * Handles opening the magic doors.
         */

        on(intArrayOf(12045, 12047), IntType.SCENERY, "open") { player, node ->
            val isMagicDoorAAtLocation =
                node.id == org.rs.consts.Scenery.MAGIC_DOOR_12045 && node.location == Location(2469, 4438, 0)

            val isMagicDoorBAtLocation =
                node.id == org.rs.consts.Scenery.MAGIC_DOOR_12047 && node.location == Location(2465, 4434, 0)

            val conditionOne = isMagicDoorAAtLocation && player.location.x >= 2470
            val conditionTwo = player.location.y < 4434 && (isMagicDoorAAtLocation || isMagicDoorBAtLocation)
            val conditionThree = node.id == org.rs.consts.Scenery.MAGIC_DOOR_12047 && player.location.x >= 2470

            if (conditionOne || conditionTwo || conditionThree) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                player.dialogueInterpreter.open(MagicDoorDialogue.NAME, node)
            }
            return@on true
        }

        /*
         * Handles using raw chicken & egg on chicken shrine.
         */

        onUseWith(
            IntType.SCENERY,
            intArrayOf(Items.RAW_CHICKEN_2138, Items.EGG_1944),
            org.rs.consts.Scenery.CHICKEN_SHRINE_12093
        ) { player, used, _ ->
            if (used.id != Items.RAW_CHICKEN_2138) {
                sendMessage(player, "Nice idea, but nothing interesting happens.")
                return@onUseWith false
            }
            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                lock(player, 3)
                queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
                    when (stage) {
                        0 -> {
                            animate(player, Animations.DISAPPEAR_2755)
                            return@queueScript keepRunning(player)
                        }

                        1 -> {
                            teleport(player, Location(2461, 4356, 0))
                            animate(player, Animations.APPEAR_2757)
                            return@queueScript stopExecuting(player)
                        }

                        else -> return@queueScript stopExecuting(player)
                    }
                }
            }
            return@onUseWith true
        }

        /*
         * Handles scenery interactions around Zanaris.
         */

        on(org.rs.consts.Scenery.PORTAL_12260, IntType.SCENERY, "use") { player, _ ->
            teleport(player, Location(2453, 4476, 0))
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ROPE_954, org.rs.consts.Scenery.TUNNEL_ENTRANCE_12253) { player, used, _ ->
            if (!removeItem(player, used.asItem())) {
                sendMessage(player, "Nothing interesting happens.")
            } else {
                replaceScenery(
                    core.game.node.scenery.Scenery(Scenery.TUNNEL_ENTRANCE_12253, location(2455, 4380, 0)),
                    Scenery.TUNNEL_ENTRANCE_12254,
                    80
                )
            }
            return@onUseWith true
        }

        on(org.rs.consts.Scenery.TUNNEL_ENTRANCE_12254, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(2441, 4381, 0))
            return@on true
        }

        on(org.rs.consts.Scenery.ROPE_12255, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, Animation(Animations.MULTI_BEND_OVER_827), Location(2457, 4380, 0))
            return@on true
        }
    }
}

private class ZygomiteNPC : NPCBehavior(NPCs.FUNGI_3344, NPCs.FUNGI_3345, NPCs.ZYGOMITE_3346, NPCs.ZYGOMITE_3347), InteractionListener {

    companion object {
        private const val FIRST_ANIMATION = Animations.CLOSING_CHEST_3335
        private const val SECOND_ANIMATION = 3322
    }

    override fun defineListeners() {
        on(intArrayOf(NPCs.FUNGI_3344, NPCs.FUNGI_3345), IntType.NPC, "pick") { player, node ->
            val fungi = node as NPC
            if (getStatLevel(player, Skills.SLAYER) < 57) {
                sendMessage(player, "Zygomite is Slayer monster that require a Slayer level of 57 to kill.")
                return@on true
            }
            lock(player, 3)
            animate(player, FIRST_ANIMATION)
            submitWorldPulse(
                object : Pulse() {
                    override fun pulse(): Boolean {
                        animate(entity = fungi, anim = SECOND_ANIMATION)
                        transformNpc(fungi, fungi.id + 2, 500)
                        fungi.impactHandler.disabledTicks = 1
                        fungi.attack(player)
                        return true
                    }
                },
            )
            return@on true
        }

        onUseWith(IntType.NPC, (7421..7431).toIntArray(), *ids, handler = ::handleFungicideSpray)
    }

    override fun beforeDamageReceived(self: NPC, attacker: Entity, state: BattleState, ) {
        val lifepoints = self.skills.lifepoints
        if (state.estimatedHit + max(state.secondaryHit, 0) > lifepoints - 1) {
            state.estimatedHit = lifepoints - 1
            state.secondaryHit = -1
            setAttribute(self, "lock-damage", true)
        }
    }

    override fun tick(self: NPC): Boolean {
        if (getAttribute(self, "lock-damage", false)) {
            self.properties.combatPulse.stop()
            removeAttribute(self, "lock-damage")
        }
        return true
    }

    override fun onDeathFinished(self: NPC, killer: Entity, ) {
        super.onDeathFinished(self, killer)
        self.reTransform()
    }

    override fun shouldIgnoreMultiRestrictions(self: NPC, victim: Entity, ): Boolean = true

    private fun handleFungicideSpray(player: Player, used: Node, with: Node, ): Boolean {
        if (with !is NPC) return false
        if (used.id == Items.FUNGICIDE_SPRAY_0_7431) return false
        if (with.skills.lifepoints >= 3) {
            sendMessage(player, "The zygomite isn't weak enough to be affected by the fungicide.")
        } else {
            sendMessage(player, "The zygomite is covered in fungicide. It bubbles away to nothing!")
            replaceSlot(player, used.asItem().slot, Item(used.id + 1))
            with.startDeath(player)
        }
        return true
    }
}