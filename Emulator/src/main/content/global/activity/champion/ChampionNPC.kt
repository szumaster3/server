package content.global.activity.champion

import content.data.GameAttributes
import core.api.*
import core.api.interaction.getNPCName
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

@Initializable
class ChampionNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {

    private var clearTime = 0

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = ChampionNPC(id, location)

    override fun getIds(): IntArray = intArrayOf(
        NPCs.EARTH_WARRIOR_CHAMPION_3057,
        NPCs.GIANT_CHAMPION_3058,
        NPCs.GHOUL_CHAMPION_3059,
        NPCs.GOBLIN_CHAMPION_3060,
        NPCs.HOBGOBLIN_CHAMPION_3061,
        NPCs.IMP_CHAMPION_3062,
        NPCs.JOGRE_CHAMPION_3063,
        NPCs.LESSER_DEMON_CHAMPION_3064,
        NPCs.SKELETON_CHAMPION_3065,
        NPCs.ZOMBIES_CHAMPION_3066,
        NPCs.LEON_DCOUR_3067
    )

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 288) poofClear(this)
    }

    private val bannedStyleMap = mapOf(
        NPCs.GIANT_CHAMPION_3058 to setOf(CombatStyle.MAGIC, CombatStyle.RANGE),
        NPCs.GOBLIN_CHAMPION_3060 to setOf(CombatStyle.MELEE, CombatStyle.RANGE),
        NPCs.HOBGOBLIN_CHAMPION_3061 to setOf(CombatStyle.MELEE),
        NPCs.JOGRE_CHAMPION_3063 to setOf(CombatStyle.RANGE),
        NPCs.ZOMBIES_CHAMPION_3066 to setOf(CombatStyle.MAGIC),
        NPCs.SKELETON_CHAMPION_3065 to setOf(CombatStyle.MAGIC, CombatStyle.MELEE)
    )

    private val allowedStyleMap = mapOf(
        NPCs.GIANT_CHAMPION_3058 to setOf(CombatStyle.MELEE),
        NPCs.GOBLIN_CHAMPION_3060 to setOf(CombatStyle.MAGIC),
        NPCs.HOBGOBLIN_CHAMPION_3061 to setOf(CombatStyle.MAGIC, CombatStyle.RANGE),
        NPCs.IMP_CHAMPION_3062 to setOf(CombatStyle.MELEE, CombatStyle.MAGIC, CombatStyle.RANGE),
        NPCs.JOGRE_CHAMPION_3063 to setOf(CombatStyle.MAGIC, CombatStyle.MELEE),
        NPCs.ZOMBIES_CHAMPION_3066 to setOf(CombatStyle.MELEE, CombatStyle.RANGE),
        NPCs.SKELETON_CHAMPION_3065 to setOf(CombatStyle.RANGE)
    )

    private val impactMessage = mapOf(
        NPCs.GIANT_CHAMPION_3058 to "You can use only melee in this challenge.",
        NPCs.GOBLIN_CHAMPION_3060 to "You can use only spells in this challenge.",
        NPCs.HOBGOBLIN_CHAMPION_3061 to "You cannot use melee in this challenge.",
        NPCs.IMP_CHAMPION_3062 to "You cannot use special attack in this challenge.",
        NPCs.JOGRE_CHAMPION_3063 to "You cannot use ranged weapons.",
        NPCs.ZOMBIES_CHAMPION_3066 to "You cannot use spells in this challenge.",
        NPCs.SKELETON_CHAMPION_3065 to "You can use only ranged weapons in this challenge."
    )

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker as? Player ?: return

        if (id == NPCs.IMP_CHAMPION_3062) {
            val w = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (w.weaponInterface?.interfaceId == 10) {
                state.estimatedHit = 0
                state.secondaryHit = 0
                sendMessage(player, impactMessage[id] ?: "")
                return
            }
        }

        bannedStyleMap[id]?.let { styles ->
            if (state.style in styles) {
                state.estimatedHit = 0
                state.secondaryHit = 0
                sendMessage(player, impactMessage[id] ?: "")
            }
        }

        allowedStyleMap[id]?.let { allowed ->
            if (state.style in allowed) {
                state.neutralizeHits()
                return
            }
        }
    }

    private val experience = mapOf(
        NPCs.EARTH_WARRIOR_CHAMPION_3057 to 432.0,
        NPCs.GIANT_CHAMPION_3058 to 280.0,
        NPCs.GHOUL_CHAMPION_3059 to 400.0,
        NPCs.GOBLIN_CHAMPION_3060 to 128.0,
        NPCs.HOBGOBLIN_CHAMPION_3061 to 232.0,
        NPCs.IMP_CHAMPION_3062 to 160.0,
        NPCs.JOGRE_CHAMPION_3063 to 480.0,
        NPCs.LESSER_DEMON_CHAMPION_3064 to 592.0,
        NPCs.SKELETON_CHAMPION_3065 to 232.0,
        NPCs.ZOMBIES_CHAMPION_3066 to 240.0,
        NPCs.LEON_DCOUR_3067 to 492.0
    )

    private fun getVarbit(npcId: Int): Int? {
        return if (npcId in NPC_ID..NPCs.ZOMBIES_CHAMPION_3066) {
            VARBIT_ID + (npcId - NPC_ID)
        } else {
            null
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)
            runTask(killer, 1) {
                playJingle(killer, 85)
                openInterface(killer, Components.CHAMPIONS_SCROLL_63)
                val SCROLL_IDS = getScrollId(id)
                val XP_REWARD = experience[id]
                val VARBIT_IDS = getVarbit(id)

                if (SCROLL_IDS != null && XP_REWARD != null) {
                    sendString(
                        killer, "Well done, you defeated the ${getNPCName(id)}!", Components.CHAMPIONS_SCROLL_63, 2
                    )
                    sendItemZoomOnInterface(killer, Components.CHAMPIONS_SCROLL_63, 3, SCROLL_IDS, 260)
                    sendString(killer, "${XP_REWARD.toInt()} Slayer Xp", Components.CHAMPIONS_SCROLL_63, 6)
                    sendString(killer, "${XP_REWARD.toInt()} Hitpoint Xp", Components.CHAMPIONS_SCROLL_63, 7)
                    VARBIT_IDS?.let { setVarbit(killer, it, 1, true) }
                    rewardXP(killer, Skills.HITPOINTS, XP_REWARD)
                    rewardXP(killer, Skills.SLAYER, XP_REWARD)
                }
                ChallengeListener.isFinalBattle(killer)
            }
            clearHintIcon(killer)
            removeAttributes(
                killer, GameAttributes.ACTIVITY_CHAMPION_CHALLENGE, GameAttributes.PRAYER_LOCK
            )
        }
        clear()
        super.finalizeDeath(killer)
    }

    companion object {
        const val NPC_ID = NPCs.EARTH_WARRIOR_CHAMPION_3057
        const val SCROLL_ID = Items.CHAMPION_SCROLL_6798
        private const val VARBIT_ID = Vars.VARBIT_SCENERY_CHAMPIONS_CHALLENGE_EARTH_WARRIOR_BANNER_1452

        private val scrollIds: Map<Int, Int> = (NPC_ID..3067).associateWith { npcId ->
            SCROLL_ID + (npcId - NPC_ID)
        }

        fun getScrollId(npcId: Int): Int? = scrollIds[npcId]

        private val PRAYER_ITEMS = intArrayOf(
            Items.PRAYER_POTION1_143,
            Items.PRAYER_POTION1_144,
            Items.PRAYER_POTION2_141,
            Items.PRAYER_POTION2_142,
            Items.PRAYER_POTION3_139,
            Items.PRAYER_POTION3_140,
            Items.PRAYER_POTION4_2434,
            Items.PRAYER_POTION4_2435,
            Items.SUPER_RESTORE1_3030,
            Items.SUPER_RESTORE1_3031,
            Items.SUPER_RESTORE2_3028,
            Items.SUPER_RESTORE2_3029,
            Items.SUPER_RESTORE3_3026,
            Items.SUPER_RESTORE3_3027,
            Items.SUPER_RESTORE4_3024,
            Items.SUPER_RESTORE4_3025,
            Items.PRAYER_CAPE_9759,
            Items.PRAYER_CAPET_9760,
            Items.PRAYER_HOOD_9761,
            Items.PRAYER_CAPE_10643,
            Items.PRAYER_POTION4_14209,
            Items.PRAYER_POTION4_14210,
            Items.PRAYER_POTION3_14211,
            Items.PRAYER_POTION3_14212,
            Items.PRAYER_POTION2_14213,
            Items.PRAYER_POTION2_14214,
            Items.PRAYER_POTION1_14215,
            Items.PRAYER_POTION1_14216,
            Items.FALADOR_SHIELD_1_14577,
            Items.FALADOR_SHIELD_2_14578,
            Items.FALADOR_SHIELD_3_14579,
            Items.PRAYER_MIX1_11467,
            Items.PRAYER_MIX1_11468,
            Items.PRAYER_MIX2_11465,
            Items.PRAYER_MIX2_11466,
            Items.SUP_RESTORE_MIX1_11495,
            Items.SUP_RESTORE_MIX1_11496,
            Items.SUP_RESTORE_MIX2_11493,
            Items.SUP_RESTORE_MIX2_11494
        )

        @JvmStatic
        fun spawnChampion(player: Player, npcId: Int) {
            val champion = ChampionNPC(npcId)
            champion.location = location(3170, 9758, 0)
            champion.isWalks = true
            champion.isAggressive = true
            champion.isActive = false

            if (champion.asNpc() != null && champion.isActive) {
                champion.properties.teleportLocation = champion.properties.spawnLocation
            }

            champion.isActive = true
            GameWorld.Pulser.submit(object : Pulse(0, champion) {
                override fun pulse(): Boolean {
                    if (npcId == NPCs.EARTH_WARRIOR_CHAMPION_3057 && (player.inventory.containsAtLeastOneItem(PRAYER_ITEMS) || player.equipment.containsAtLeastOneItem(PRAYER_ITEMS))) {
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
    }
}