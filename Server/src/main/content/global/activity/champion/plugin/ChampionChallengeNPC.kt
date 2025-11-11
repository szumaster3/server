package content.global.activity.champion.plugin

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.skill.Skills
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import shared.consts.Components
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Vars

/**
 * Handles Champion challenge NPC.
 */
@Initializable
class ChampionChallengeNPC(id: Int = 0, location: Location? = null) : AbstractNPC(id, location) {

    override fun construct(id: Int, location: Location, vararg objects: Any): AbstractNPC = ChampionChallengeNPC(id, location)

    override fun getIds(): IntArray = styleRules.keys.toIntArray()

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker as? Player ?: return

        val currentTick = GameWorld.ticks
        val lastMsgTick = getAttribute(player, GameAttributes.ACTIVITY_CHAMPIONS_RULE_MSG_TICK, 0)

        if (id == NPCs.IMP_CHAMPION_3062) {
            val specialAttack = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (specialAttack.isSpecialBar) {
                state.neutralizeHits()
                if (currentTick - lastMsgTick > 10) {
                    sendMessage(player, styleRules[id]?.message ?: "", currentTick)
                }
                return
            }
        }

        /*
         * Enforce allowed/banned combat styles.
         */
        styleRules[id]?.let { rule ->
            if (rule.banned?.contains(state.style) == true || (rule.allowed != null && !rule.allowed.contains(state.style))) {
                state.neutralizeHits()
                if (currentTick - lastMsgTick > 10) {
                    sendMessage(player, rule.message, currentTick)
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer !is Player) return
        lock(killer, 2)

        playJingle(killer, 85)
        openInterface(killer, Components.CHAMPIONS_SCROLL_63)

        val config = ChampionDefinition.values().firstOrNull { it.npcId == id } ?: return
        val scrollId = config.scrollId

        /*
         * "Note that if you leave a challenge at any time during the battle, you will not lose your scroll.
         * The only way to lose your scroll is by dying in the area or dropping it.
         * The scroll will automatically disappear once you've defeated the champion, however."
         */

        removeItem(killer, scrollId)

        sendString(killer, "Well done, you defeated the ${getNPCName(id)}!", Components.CHAMPIONS_SCROLL_63, 2)
        sendItemZoomOnInterface(killer, Components.CHAMPIONS_SCROLL_63, 3, config.scrollId, 260)
        sendString(killer, "${config.xp.toInt()} Slayer Xp", Components.CHAMPIONS_SCROLL_63, 6)
        sendString(killer, "${config.xp.toInt()} Hitpoint Xp", Components.CHAMPIONS_SCROLL_63, 7)

        config.varbitId?.let { setVarbit(killer, it, 1, true) }
        rewardXP(killer, Skills.HITPOINTS, config.xp)
        rewardXP(killer, Skills.SLAYER, config.xp)
        if ((Vars.VARBIT_CHAMPIONS_CHALLENGE_EARTH_WARRIOR_1452..Vars.VARBIT_CHAMPIONS_CHALLENGE_ZOMBIE_1461)
                .all { getVarbit(killer, it) == 1 }
        ) {
            setAttribute(killer, GameAttributes.ACTIVITY_CHAMPIONS_CHALLENGE_DEFEAT_ALL, true)
            sendNPCDialogueLines(
                killer, NPCs.LEON_DCOUR_3067, FaceAnim.NEUTRAL, false,
                "You have done well brave adventurer, but I would test",
                "your mettle now. You may arrange the fight with",
                "Larxus at your leisure."
            )
        }
        removeAttributes(killer, GameAttributes.PRAYER_LOCK)
        clearHintIcon(killer)
        clear()
        super.finalizeDeath(killer)
    }

    companion object {
        /**
         * The list of items considered prayer items that may be banned in challenges.
         */
        private val prayerItems = intArrayOf(
            Items.PRAYER_POTION1_143, Items.PRAYER_POTION1_144, Items.PRAYER_POTION2_141, Items.PRAYER_POTION2_142, Items.PRAYER_POTION3_139, Items.PRAYER_POTION3_140, Items.PRAYER_POTION4_2434, Items.PRAYER_POTION4_2435,
            Items.SUPER_RESTORE1_3030, Items.SUPER_RESTORE1_3031, Items.SUPER_RESTORE2_3028, Items.SUPER_RESTORE2_3029, Items.SUPER_RESTORE3_3026, Items.SUPER_RESTORE3_3027, Items.SUPER_RESTORE4_3024, Items.SUPER_RESTORE4_3025,
            Items.PRAYER_CAPE_9759, Items.PRAYER_CAPET_9760, Items.PRAYER_HOOD_9761, Items.PRAYER_CAPE_10643,
            Items.PRAYER_POTION4_14209, Items.PRAYER_POTION4_14210, Items.PRAYER_POTION3_14211, Items.PRAYER_POTION3_14212, Items.PRAYER_POTION2_14213, Items.PRAYER_POTION2_14214, Items.PRAYER_POTION1_14215, Items.PRAYER_POTION1_14216,
            Items.FALADOR_SHIELD_1_14577, Items.FALADOR_SHIELD_2_14578, Items.FALADOR_SHIELD_3_14579,
            Items.PRAYER_MIX1_11467, Items.PRAYER_MIX1_11468, Items.PRAYER_MIX2_11465, Items.PRAYER_MIX2_11466,
            Items.SUP_RESTORE_MIX1_11495, Items.SUP_RESTORE_MIX1_11496, Items.SUP_RESTORE_MIX2_11493, Items.SUP_RESTORE_MIX2_11494
        )

        /**
         * Handles spawn of champion npc.
         *
         * @param player The challenger.
         * @param scrollId The champion scroll id.
         */
        fun spawnChampion(player: Player, scrollId: Int) {
            val entry = ChampionDefinition.fromScroll(scrollId) ?: return
            val champion = ChampionChallengeNPC(entry.npcId).apply {
                location = location(3170, 9758, 0)
                isWalks = true
                isNeverWalks = false
                isRespawn = false
                isAggressive = true
                isActive = false
            }

            if (champion.asNpc() != null && champion.isActive) {
                champion.properties.teleportLocation = champion.properties.spawnLocation
            }

            champion.isActive = true

            if (entry.npcId == NPCs.EARTH_WARRIOR_CHAMPION_3057 && player.hasPrayerItems()) {
                sendNPCDialogue(player, NPCs.LARXUS_3050, "For this fight you're not allowed to use prayers!")
                teleport(player, Location.create(3182, 9758, 0), TeleportManager.TeleportType.INSTANT)
                return
            }

            champion.init()
            registerHintIcon(player, champion)
            champion.attack(player)
        }

        /**
         * Checks if player has any prayer items in inventory or equipment.
         */
        private fun Player.hasPrayerItems(): Boolean =
            inventory.containsAtLeastOneItem(prayerItems) || equipment.containsAtLeastOneItem(prayerItems)

        private val styleRules = mapOf(
            NPCs.GIANT_CHAMPION_3058 to StyleRule(
                setOf(CombatStyle.MELEE),
                setOf(CombatStyle.MAGIC, CombatStyle.RANGE),
                "Larxus said you could use only Melee in this duel."
            ),
            NPCs.GOBLIN_CHAMPION_3060 to StyleRule(
                setOf(CombatStyle.MAGIC),
                setOf(CombatStyle.MELEE, CombatStyle.RANGE),
                "Larxus said you could use only Spells in this duel."
            ),
            NPCs.HOBGOBLIN_CHAMPION_3061 to StyleRule(
                setOf(CombatStyle.MAGIC, CombatStyle.RANGE),
                setOf(CombatStyle.MELEE),
                "Larxus said you couldn't use Melee in this duel."
            ),
            NPCs.IMP_CHAMPION_3062 to StyleRule(
                setOf(CombatStyle.MELEE, CombatStyle.MAGIC, CombatStyle.RANGE),
                null,
                "Larxus said you couldn't use Special Attacks in this duel."
            ),
            NPCs.JOGRE_CHAMPION_3063 to StyleRule(
                setOf(CombatStyle.MAGIC, CombatStyle.MELEE),
                setOf(CombatStyle.RANGE),
                "Larxus said you couldn't use Ranged Weapons."
            ),
            NPCs.ZOMBIES_CHAMPION_3066 to StyleRule(
                setOf(CombatStyle.MELEE, CombatStyle.RANGE),
                setOf(CombatStyle.MAGIC),
                "Larxus said you couldn't use Spells in this duel."
            ),
            NPCs.SKELETON_CHAMPION_3065 to StyleRule(
                setOf(CombatStyle.RANGE),
                setOf(CombatStyle.MAGIC, CombatStyle.MELEE),
                "Larxus said you could use only Ranged Weapons in this duel."
            )
        )
    }
}

private data class StyleRule(
    val allowed: Set<CombatStyle>? = null,
    val banned: Set<CombatStyle>? = null,
    val message: String
)