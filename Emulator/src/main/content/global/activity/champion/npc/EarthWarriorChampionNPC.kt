package content.global.activity.champion.npc

import content.data.GameAttributes
import content.global.activity.champion.ChallengeListener
import core.api.*
import core.game.node.entity.Entity
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
class EarthWarriorChampionNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var clearTime = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return EarthWarriorChampionNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.EARTH_WARRIOR_CHAMPION_3057)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 288) poofClear(this)
    }

    companion object {
        @JvmStatic
        fun spawnEarthWarriorChampion(player: Player) {
            val champion = EarthWarriorChampionNPC(NPCs.EARTH_WARRIOR_CHAMPION_3057)
            champion.location = location(3170, 9758, 0)
            champion.isWalks = true
            champion.isAggressive = true
            champion.isActive = false

            if (champion.asNpc() != null && champion.isActive) {
                champion.properties.teleportLocation = champion.properties.spawnLocation
            }

            champion.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, champion) {
                    override fun pulse(): Boolean {
                        val prayerItems =
                            intArrayOf(
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
                                Items.SUP_RESTORE_MIX2_11494,
                            )
                        if (player.inventory.containsAtLeastOneItem(prayerItems) ||
                            player.equipment.containsAtLeastOneItem(
                                prayerItems,
                            )
                        ) {
                            sendNPCDialogue(
                                player,
                                NPCs.LARXUS_3050,
                                "For this fight you're not allowed to use prayers!",
                            )
                            teleport(player, Location.create(3182, 9758, 0))
                        } else {
                            champion.init()
                            registerHintIcon(player, champion)
                            champion.attack(player)
                        }
                        return true
                    }
                },
            )
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)
            runTask(killer, 1) {
                openInterface(killer, Components.CHAMPIONS_SCROLL_63)
                sendString(
                    killer,
                    "Well done, you defeated the Earth Warrior Champion!",
                    Components.CHAMPIONS_SCROLL_63,
                    2,
                )
                sendItemZoomOnInterface(killer, Components.CHAMPIONS_SCROLL_63, 3, Items.CHAMPION_SCROLL_6798, 260)
                sendString(killer, "432 Slayer Xp", Components.CHAMPIONS_SCROLL_63, 6)
                sendString(killer, "432 Hitpoint Xp", Components.CHAMPIONS_SCROLL_63, 7)
            }
            setVarbit(killer, Vars.VARBIT_SCENERY_CHAMPIONS_CHALLENGE_EARTH_WARRIOR_BANNER_1452, 1, true)
            rewardXP(killer, Skills.HITPOINTS, 432.0)
            rewardXP(killer, Skills.SLAYER, 432.0)
            removeAttributes(killer, "championsarena:start", GameAttributes.PRAYER_LOCK)
            clearHintIcon(killer)
            ChallengeListener.isFinalBattle(killer)
        }
        clear()
        super.finalizeDeath(killer)
    }
}
