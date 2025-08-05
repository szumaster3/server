package content.global.activity.champion.plugin

import content.data.GameAttributes
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

/**
 * Handles Champion challenge NPC.
 */
@Initializable
class ChampionChallengeNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    private var clearTicks = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = ChampionChallengeNPC(id, location)

    override fun getIds(): IntArray = styleRules.keys.toIntArray()

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTicks++ > 288) poofClear(this)
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker as? Player ?: return

        if (id == NPCs.IMP_CHAMPION_3062) {
            val specialAttack = player.getExtension<WeaponInterface>(WeaponInterface::class.java)

            if (specialAttack.isSpecialBar) {
                state.neutralizeHits()
                sendMessage(player, styleRules[id]?.message ?: "")
                return
            }
        }

        styleRules[id]?.let { rule ->
            if (rule.banned?.contains(state.style) == true) {
                state.neutralizeHits()
                sendMessage(player, rule.message)
                return
            }
            if (rule.allowed != null && !rule.allowed.contains(state.style)) {
                state.neutralizeHits()
                sendMessage(player, rule.message)
                return
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)
            runTask(killer, 1) {
                playJingle(killer, 85)
                openInterface(killer, Components.CHAMPIONS_SCROLL_63)

                val meta = championMeta[id] ?: return@runTask

                sendString(killer, "Well done, you defeated the ${getNPCName(id)}!", Components.CHAMPIONS_SCROLL_63, 2)
                sendItemZoomOnInterface(killer, Components.CHAMPIONS_SCROLL_63, 3, meta.scrollId, 260)
                sendString(killer, "${meta.xp.toInt()} Slayer Xp", Components.CHAMPIONS_SCROLL_63, 6)
                sendString(killer, "${meta.xp.toInt()} Hitpoint Xp", Components.CHAMPIONS_SCROLL_63, 7)
                meta.varbitId?.let { setVarbit(killer, it, 1, true) }
                rewardXP(killer, Skills.HITPOINTS, meta.xp)
                rewardXP(killer, Skills.SLAYER, meta.xp)
                ChampionChallengePlugin.isFinalBattle(killer)
            }
            clearHintIcon(killer)
            removeAttributes(killer, GameAttributes.ACTIVITY_CHAMPION_CHALLENGE, GameAttributes.PRAYER_LOCK)
        }
        clear()
        super.finalizeDeath(killer)
    }

    companion object {
        const val NPC_ID = NPCs.EARTH_WARRIOR_CHAMPION_3057
        const val SCROLL_ID = Items.CHAMPION_SCROLL_6798
        private const val VARBIT_ID = Vars.VARBIT_SCENERY_CHAMPIONS_CHALLENGE_EARTH_WARRIOR_BANNER_1452

        private val prayerItems = intArrayOf(
            Items.PRAYER_POTION1_143, Items.PRAYER_POTION1_144, Items.PRAYER_POTION2_141, Items.PRAYER_POTION2_142,
            Items.PRAYER_POTION3_139, Items.PRAYER_POTION3_140, Items.PRAYER_POTION4_2434, Items.PRAYER_POTION4_2435,
            Items.SUPER_RESTORE1_3030, Items.SUPER_RESTORE1_3031, Items.SUPER_RESTORE2_3028, Items.SUPER_RESTORE2_3029,
            Items.SUPER_RESTORE3_3026, Items.SUPER_RESTORE3_3027, Items.SUPER_RESTORE4_3024, Items.SUPER_RESTORE4_3025,
            Items.PRAYER_CAPE_9759, Items.PRAYER_CAPET_9760, Items.PRAYER_HOOD_9761, Items.PRAYER_CAPE_10643,
            Items.PRAYER_POTION4_14209, Items.PRAYER_POTION4_14210, Items.PRAYER_POTION3_14211, Items.PRAYER_POTION3_14212,
            Items.PRAYER_POTION2_14213, Items.PRAYER_POTION2_14214, Items.PRAYER_POTION1_14215, Items.PRAYER_POTION1_14216,
            Items.FALADOR_SHIELD_1_14577, Items.FALADOR_SHIELD_2_14578, Items.FALADOR_SHIELD_3_14579,
            Items.PRAYER_MIX1_11467, Items.PRAYER_MIX1_11468, Items.PRAYER_MIX2_11465, Items.PRAYER_MIX2_11466,
            Items.SUP_RESTORE_MIX1_11495, Items.SUP_RESTORE_MIX1_11496, Items.SUP_RESTORE_MIX2_11493, Items.SUP_RESTORE_MIX2_11494
        )

        fun spawnChampion(player: Player, npcId: Int) {
            val champion = ChampionChallengeNPC(npcId).apply {
                location = location(3170, 9758, 0)
                isWalks = true
                isAggressive = true
                isActive = false
            }

            if (champion.asNpc() != null && champion.isActive) {
                champion.properties.teleportLocation = champion.properties.spawnLocation
            }

            champion.isActive = true
            GameWorld.Pulser.submit(object : Pulse(0, champion) {
                override fun pulse(): Boolean {
                    if (npcId == NPC_ID && player.hasPrayerItems()) {
                        sendNPCDialogue(player, NPCs.LARXUS_3050, "For this fight you're not allowed to use prayers!")
                        teleport(player, Location.create(3182, 9758, 0))
                    } else {
                        champion.init()
                        registerHintIcon(player, champion)
                        champion.attack(player)
                    }
                    return true
                }
            })
        }

        private fun Player.hasPrayerItems(): Boolean {
            return inventory.containsAtLeastOneItem(prayerItems) || equipment.containsAtLeastOneItem(prayerItems)
        }

        private val championMeta = (NPC_ID..NPCs.LEON_DCOUR_3067).associateWith { npcId ->
            ChampionMeta(
                xp = when (npcId) {
                    NPCs.EARTH_WARRIOR_CHAMPION_3057 -> 432.0
                    NPCs.GIANT_CHAMPION_3058 -> 280.0
                    NPCs.GHOUL_CHAMPION_3059 -> 400.0
                    NPCs.GOBLIN_CHAMPION_3060 -> 128.0
                    NPCs.HOBGOBLIN_CHAMPION_3061 -> 232.0
                    NPCs.IMP_CHAMPION_3062 -> 160.0
                    NPCs.JOGRE_CHAMPION_3063 -> 480.0
                    NPCs.LESSER_DEMON_CHAMPION_3064 -> 592.0
                    NPCs.SKELETON_CHAMPION_3065 -> 232.0
                    NPCs.ZOMBIES_CHAMPION_3066 -> 240.0
                    NPCs.LEON_DCOUR_3067 -> 492.0
                    else -> 0.0
                },
                scrollId = SCROLL_ID + (npcId - NPC_ID),
                varbitId = if (npcId in NPC_ID..NPCs.ZOMBIES_CHAMPION_3066) VARBIT_ID + (npcId - NPC_ID) else null
            )
        }

        private val styleRules = mapOf(
            NPCs.GIANT_CHAMPION_3058 to StyleRule(setOf(CombatStyle.MELEE), setOf(CombatStyle.MAGIC, CombatStyle.RANGE), "Larxus said you could use only Melee in this duel."),
            NPCs.GOBLIN_CHAMPION_3060 to StyleRule(setOf(CombatStyle.MAGIC), setOf(CombatStyle.MELEE, CombatStyle.RANGE), "Larxus said you could use only Spells in this duel."),
            NPCs.HOBGOBLIN_CHAMPION_3061 to StyleRule(setOf(CombatStyle.MAGIC, CombatStyle.RANGE), setOf(CombatStyle.MELEE), "Larxus said you couldn't use Melee in this duel."),
            NPCs.IMP_CHAMPION_3062 to StyleRule(setOf(CombatStyle.MELEE, CombatStyle.MAGIC, CombatStyle.RANGE), null, "Larxus said you couldn't use Special Attacks in this duel."),
            NPCs.JOGRE_CHAMPION_3063 to StyleRule(setOf(CombatStyle.MAGIC, CombatStyle.MELEE), setOf(CombatStyle.RANGE), "Larxus said you couldn't use Ranged Weapons."),
            NPCs.ZOMBIES_CHAMPION_3066 to StyleRule(setOf(CombatStyle.MELEE, CombatStyle.RANGE), setOf(CombatStyle.MAGIC), "Larxus said you couldn't use Spells in this duel."),
            NPCs.SKELETON_CHAMPION_3065 to StyleRule(setOf(CombatStyle.RANGE), setOf(CombatStyle.MAGIC, CombatStyle.MELEE), "Larxus said you could use only Ranged Weapons in this duel.")
        )
    }
}

private data class StyleRule(val allowed: Set<CombatStyle>? = null, val banned: Set<CombatStyle>? = null, val message: String)

private data class ChampionMeta(val xp: Double, val scrollId: Int, val varbitId: Int?)
