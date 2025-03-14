package content.global.handlers.player

import core.api.*
import core.game.activity.ActivityManager
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.SystemManager
import core.game.world.GameWorld
import core.plugin.Initializable
import core.plugin.Plugin
import core.plugin.PluginManifest
import core.plugin.PluginType
import org.rs.consts.Items
import org.rs.consts.Music
import java.util.concurrent.TimeUnit

@Initializable
@PluginManifest(type = PluginType.LOGIN)
class LoginValidation : Plugin<Player> {
    private val QUEST_ITEMS = arrayOf(Item(Items.QUEST_POINT_CAPE_9813), Item(Items.QUEST_POINT_HOOD_9814))

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }

    override fun newInstance(player: Player): Plugin<Player> {
        player.unlock()
        if (player.isArtificial) {
            return this
        }
        if (!SystemManager.systemConfig.validLogin(player)) {
            return this
        }
        if (GameWorld.settings!!.isDevMode) {
            player.toggleDebug()
        }
        if (player.getAttribute("fc_wave", -1) > -1) {
            ActivityManager.start(player, "fight caves", true)
        }
        if (player.getAttribute("falconry", false)) {
            ActivityManager.start(player, "falconry", true)
        }
        if (player.getSavedData().questData.getDragonSlayerAttribute("repaired")) {
            setVarp(player, 177, 1967876)
        }
        if (player.getSavedData().globalData.getLootShareDelay() < System.currentTimeMillis() &&
            player.getSavedData().globalData.getLootShareDelay() != 0L
        ) {
            player.globalData.setLootSharePoints(
                (
                    player.globalData.getLootSharePoints() -
                        player.globalData.getLootSharePoints() * 0.10
                ).toInt(),
            )
        } else {
            player.getSavedData().globalData.setLootShareDelay(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1))
        }

        val musicTracks =
            listOf(
                Music.ADVENTURE_177,
                Music.BITTERSWEET_BUNNY_502,
                Music.THE_DANCE_OF_THE_SNOW_QUEEN_593,
                Music.DIANGOS_LITTLE_HELPERS_532,
                Music.LAND_OF_SNOW_189,
                Music.FUNNY_BUNNIES_603,
                Music.HIGH_SPIRITS_205,
                Music.GRIMLY_FIENDISH_432,
                Music.EASTER_JIG_273,
                Music.SEA_SHANTY_XMAS_210,
                Music.JUNGLE_BELLS_209,
                Music.JUNGLE_ISLAND_XMAS_208,
                Music.SCAPE_MAIN_16,
                Music.HOMESCAPE_621,
                Music.SCAPE_HUNTER_207,
                Music.SCAPE_ORIGINAL_400,
                Music.SCAPE_SUMMON_457,
                Music.SCAPE_SANTA_547,
                Music.SCAPE_SCARED_321,
                Music.GROUND_SCAPE_466,
            )

        musicTracks.forEach { track ->
            if (!player.musicPlayer.hasUnlocked(track)) {
                player.musicPlayer.unlock(track)
            }
        }

        checkQuestPointsItems(player)
        return this
    }

    private fun checkQuestPointsItems(player: Player) {
        if (!player.getQuestRepository().hasCompletedAll() &&
            anyInEquipment(player, Items.QUEST_POINT_CAPE_9813, Items.QUEST_POINT_HOOD_9814)
        ) {
            var location1: String? = null
            var location2: String? = null
            var item1 = 0
            var item2 = 0
            var amt = 0
            for (i in QUEST_ITEMS) {
                if (removeItem(player, i, Container.EQUIPMENT)) {
                    amt++
                    var location: String
                    if (addItem(player, i.id, i.amount, Container.INVENTORY)) {
                        location = "your inventory"
                    } else if (addItem(player, i.id, i.amount, Container.BANK)) {
                        location = "your bank"
                    } else {
                        location = "the Wise Old Man"
                        if (i.id == Items.QUEST_POINT_CAPE_9813) {
                            setAttribute(player, "/save:reclaim-qp-cape", true)
                        } else {
                            setAttribute(player, "/save:reclaim-qp-hood", true)
                        }
                    }
                    if (amt == 1) {
                        item1 = i.id
                        location1 = location
                    }
                    if (amt == 2) {
                        item2 = i.id
                        location2 = location
                    }
                }
            }
            if (amt == 2) {
                sendDoubleItemDialogue(
                    player,
                    item1,
                    item2,
                    "As you no longer have completed all the quests, your " + getItemName(item1) +
                        " unequips itself to " +
                        location1 +
                        " and your " +
                        getItemName(item2) +
                        " unequips itself to " +
                        location2 +
                        "!",
                )
            } else {
                sendItemDialogue(
                    player,
                    item1,
                    "As you no longer have completed all the quests, your " + getItemName(item1) +
                        " unequips itself to " +
                        location1 +
                        "!",
                )
            }
        }
    }
}
