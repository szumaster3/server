package core.game.world.map.zone.impl

import content.global.plugin.item.equipment.gloves.BrawlingGloves
import core.api.*
import core.game.component.Component
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.NPC
import core.game.node.entity.npc.agg.AggressiveBehavior
import core.game.node.entity.npc.agg.AggressiveHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.info.Rights
import core.game.node.entity.player.link.SkullManager
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.game.world.map.zone.MapZone
import core.game.world.map.zone.ZoneBorders
import core.game.world.map.zone.ZoneRestriction
import core.tools.RandomFunction
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import java.util.*
import kotlin.math.ln
import kotlin.math.pow

/**
 * Represents the Wilderness zone.
 */
class WildernessZone(vararg val borders: ZoneBorders) :
    MapZone("Wilderness", true, ZoneRestriction.RANDOM_EVENTS) {

    override fun configure() {
        borders.forEach { register(it) }
    }

    /**
     * Calculates a custom PvP gear drop rate based on the given combat level.
     *
     * @param combatLevel The combat level of the NPC.
     * @return The calculated drop rate.
     */
    private fun getNewDropRate(combatLevel: Int): Int {
        val x = combatLevel.toDouble()
        val A = 44044.5491
        val B = -7360.19548
        return (A + (B * ln(x))).toInt()
    }

    override fun death(e: Entity, killer: Entity): Boolean {
        if (e is NPC) rollWildernessExclusiveLoot(e, killer)
        return false
    }

    /**
     * Rolls exclusive loot drops for:
     * - [Revenants][content.region.wilderness.npc.revenants.RevenantNPC]
     * - [Chaos Elemental][content.region.wilderness.npc.ChaosElementalNPC]
     *
     * @param e The NPC that died.
     * @param killer The player who killed the NPC.
     */
    private fun rollWildernessExclusiveLoot(e: Entity, killer: Entity) {
        if (killer !is Player || e !is NPC) return
        if (e.id != NPCs.CHAOS_ELEMENTAL_3200 && !e.name.contains("Revenant", ignoreCase = true)) return

        var pvpGearRate = getNewDropRate(e.definition.combatLevel)
        val higherRate = killer.skullManager.isDeepWilderness && killer.getAttribute(
            "deepwild-value-risk", 0L
        ) > SkullManager.DEEP_WILD_DROP_RISK_THRESHOLD

        if (higherRate) pvpGearRate /= 2

        val gloveRate =
            if (e.id == NPCs.CHAOS_ELEMENTAL_3200) 50 else if (higherRate) 100 else ((1.0 / (1.0 - (1.0 - (1.0 / pvpGearRate)).pow(
                16.0
            ))) * 5.0 / 6.0).toInt()

        if (RandomFunction.roll(gloveRate)) {
            val glove = RandomFunction.random(1, 14).toByte()
            val reward = BrawlingGloves.forIndicator(glove.toInt())?.id?.let { Item(it) } ?: return
            GroundItemManager.create(reward, e.dropLocation, killer)
            announceDrop(killer, reward, e.name)
        }

        PVP_GEAR.forEach { gearId ->
            if (RandomFunction.roll(pvpGearRate)) {
                val amount =
                    if (gearId == Items.MORRIGANS_JAVELIN_13879 || gearId == Items.MORRIGANS_THROWING_AXE_13883) RandomFunction.random(
                        15,
                        50
                    ) else 1
                val reward = Item(gearId, amount)
                produceGroundItem(killer, reward.id, reward.amount, e.dropLocation)
                announceDrop(killer, reward, e.name)
            }
        }
    }

    /**
     * Announces a valuable drop globally.
     */
    private fun announceDrop(player: Player, item: Item, npcName: String) {
        sendNews("${player.username} has received a ${item.name.lowercase(Locale.getDefault())} from a $npcName!")
    }

    override fun interact(e: Entity, target: Node, option: Option): Boolean {
        if (target is NPC && target.name.contains("Revenant", ignoreCase = true)) {
            e.asPlayer().properties.combatPulse.attack(target)
            return true
        }
        return super.interact(e, target, option)
    }

    override fun enter(e: Entity): Boolean {
        when (e) {
            is Player -> e.apply {
                if (!isArtificial) {
                    show(this)
                } else skullManager.run {
                    isWilderness = true
                    level = getWilderness(e)
                }
                for (i in 0..6) {
                    if (i != 3 && i != 5 && (attributes.containsKey("overload") || skills.getLevel(i) > 118)) {
                        if (skills.getLevel(i) > skills.getStaticLevel(i)) {
                            skills.setLevel(i, skills.getStaticLevel(i))
                            removeAttribute("overload")
                        }
                    }
                }
                familiarManager.familiar?.takeIf { familiarManager.hasFamiliar() && !familiarManager.hasPet() }
                    ?.transform()
                appearance.sync()
            }

            is NPC -> e.apply {
                if (definition.hasAttackOption() && isAggressive) {
                    aggressiveHandler = AggressiveHandler(this, AggressiveBehavior.WILDERNESS)
                }
            }
        }
        return true
    }

    override fun leave(e: Entity, logout: Boolean): Boolean {
        if (!logout && e is Player) {
            leave(e)
            e.familiarManager.familiar?.takeIf { e.familiarManager.hasFamiliar() && !e.familiarManager.hasPet() && it.isCombatFamiliar }
                ?.reTransform()
            e.appearance.sync()
        }
        return true
    }

    fun leave(p: Player) {
        val overlay = Component(Components.WILDERNESS_OVERLAY_381)
        if (overlay.id == Components.WILDERNESS_OVERLAY_381) {
            closeOverlay(p)
        }
        p.interaction.remove(Option.P_ATTACK)
        p.skullManager.run {
            isWilderness = false
            level = 0
        }
    }

    override fun teleport(e: Entity, type: Int, node: Node?): Boolean {
        if (e is Player) {
            if (e.details.rights == Rights.ADMINISTRATOR) return true

            val limit = when (node) {
                is Item -> if (node.name.contains("glory", true) || node.name.contains("slaying", true)) 30 else 20
                else -> 20
            }
            return checkTeleport(e, limit)
        }
        return true
    }

    override fun continueAttack(e: Entity, target: Node, style: CombatStyle, message: Boolean): Boolean {
        if (e is Player && target is Player) {
            val level = minOf(e.skullManager.level, target.skullManager.level)
            val combat = e.properties.currentCombatLevel
            val targetCombat = target.properties.currentCombatLevel
            if (combat - level > targetCombat || combat + level < targetCombat) {
                if (message) sendMessage(e, "The level difference between you and your opponent is too great.")
                return false
            }
        }
        return true
    }

    override fun locationUpdate(e: Entity, last: Location?) {
        if (e is Player && !e.isArtificial) {
            e.skullManager.level = getWilderness(e)
        }
    }

    companion object {
        private val PVP_GEAR = intArrayOf(
            Items.VESTAS_CHAINBODY_13887,
            Items.VESTAS_PLATESKIRT_13893,
            Items.VESTAS_LONGSWORD_13899,
            Items.VESTAS_SPEAR_13905,
            Items.MORRIGANS_LEATHER_BODY_13870,
            Items.MORRIGANS_LEATHER_CHAPS_13873,
            Items.MORRIGANS_COIF_13876,
            Items.MORRIGANS_JAVELIN_13879,
            Items.MORRIGANS_THROWING_AXE_13883,
            Items.STATIUS_PLATEBODY_13884,
            Items.STATIUS_FULL_HELM_DEG_13898,
            Items.STATIUS_FULL_HELM_13896,
            Items.STATIUS_WARHAMMER_13902,
            Items.ZURIELS_ROBE_TOP_13858,
            Items.ZURIELS_ROBE_BOTTOM_13861,
            Items.ZURIELS_HOOD_13864,
            Items.ZURIELS_STAFF_13867
        )

        val instance = WildernessZone(
            ZoneBorders(2944, 3525, 3400, 3975),
            ZoneBorders(3070, 9924, 3135, 10002),
            ZoneBorders.forRegion(12192),
            ZoneBorders.forRegion(12193),
            ZoneBorders.forRegion(11937)
        )

        /**
         * Show the overlay and sets pvp attack options.
         *
         * @param p The player entering the wilderness.
         */
        @JvmStatic
        fun show(p: Player) {
            if (p.skullManager.isWildernessDisabled) return
            openOverlay(p, Components.WILDERNESS_OVERLAY_381)
            p.skullManager.level = getWilderness(p)
            sendString(p, "Level: ${p.skullManager.level}", Components.WILDERNESS_OVERLAY_381, 1)
            if (settings!!.wild_pvp_enabled) p.interaction.set(Option.P_ATTACK)
            p.skullManager.isWilderness = true
        }
        @JvmStatic
        fun checkTeleport(p: Player, level: Int): Boolean {
            if (p.skullManager.level > level && !p.skullManager.isWildernessDisabled) {
                sendMessage(p, "A mysterious force blocks your teleport spell!")
                sendMessage(p, "You can't use this teleport after level 20 wilderness.")
                return false
            }
            return true
        }

        @JvmStatic
        fun isInZone(e: Entity): Boolean = instance.borders.any { it.insideBorder(e) }

        @JvmStatic
        fun isInZone(l: Location): Boolean = instance.borders.any { it.insideBorder(l) }

        /**
         * Calculates the wilderness level of the given entity based on its location.
         * @see [clientscript,wilderness_level_update]
         */
        @JvmStatic
        fun getWilderness(e: Entity): Int {
            val y = e.location.y
            return if (y > 6400) ((y - 9920) / 8) + 1 else ((y - 3520) / 8) + 1
        }
    }
}