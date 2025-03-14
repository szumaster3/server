package content.minigame.fishingtrawler

import core.api.*
import core.game.activity.ActivityManager
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.command.sets.FISHING_TRAWLER_LEAKS_PATCHED
import core.game.system.command.sets.STATS_BASE
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import org.rs.consts.Items
import org.rs.consts.Scenery

class FishingTrawlerInteractionHandler : InteractionListener {
    private val entrancePlank = Scenery.GANGPLANK_2178
    private val exitPlank = Scenery.GANGPLANK_2179
    private val leakId = Scenery.LEAK_2167
    private val netIDs = intArrayOf(Scenery.TRAWLER_NET_2164, Scenery.TRAWLER_NET_2165)
    private val rewardNet = Scenery.TRAWLER_NET_2166
    private val barrelIDs = intArrayOf(Scenery.BARREL_2159, Scenery.BARREL_2160)
    private val bailingBucket = Items.BAILING_BUCKET_583
    private val fullBailBucket = Items.BAILING_BUCKET_585

    override fun defineListeners() {
        on(entrancePlank, IntType.SCENERY, "cross") { player, _ ->
            if (getStatLevel(player, Skills.FISHING) < 15) {
                sendDialogue(player, "You need to be at least level 15 fishing to play.")
                return@on true
            }
            player.properties.teleportLocation = Location.create(2672, 3170, 1)
            (ActivityManager.getActivity("fishing trawler") as FishingTrawlerActivity).addPlayer(player)
            return@on true
        }

        on(exitPlank, IntType.SCENERY, "cross") { player, _ ->
            player.properties.teleportLocation = Location.create(2676, 3170, 0)
            (ActivityManager.getActivity("fishing trawler") as FishingTrawlerActivity).removePlayer(player)
            val session: FishingTrawlerSession? = player.getAttribute("ft-session", null)
            session?.players?.remove(player)
            return@on true
        }

        on(leakId, IntType.SCENERY, "fill") { player, node ->
            val session: FishingTrawlerSession? = player.getAttribute("ft-session", null)
            session ?: return@on false
            player.lock()
            player.pulseManager.run(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(Animation(827)).also { player.lock() }
                            1 ->
                                session
                                    .repairHole(player, node.asScenery())
                                    .also {
                                        player.incrementAttribute("/save:$STATS_BASE:$FISHING_TRAWLER_LEAKS_PATCHED")
                                        player.unlock()
                                    }

                            2 -> return true
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(netIDs, IntType.SCENERY, "inspect") { player, _ ->
            player.dialogueInterpreter.open(18237583)
            return@on true
        }

        on(rewardNet, IntType.SCENERY, "inspect") { player, _ ->
            val rolls = player.getAttribute("/save:ft-rolls", 0)
            if (rolls == 0) {
                sendPlayerDialogue(player, "I'd better not go stealing other people's fish.", FaceAnim.HALF_GUILTY)
                return@on true
            }
            player.dialogueInterpreter.open(18237582)
            return@on true
        }

        on(barrelIDs, IntType.SCENERY, "climb-on") { player, _ ->
            player.properties.teleportLocation = Location.create(2672, 3222, 0)
            sendDialogueLines(
                player,
                "You climb onto the floating barrel and begin to kick your way to the",
                "shore.",
                "You make it to the shore tired and weary.",
            )
            player.appearance.setDefaultAnimations()
            player.appearance.sync()
            clearLogoutListener(player, "ft-logout")
            player.locks.unlockTeleport()
            return@on true
        }

        on(fullBailBucket, IntType.ITEM, "empty") { player, node ->
            player.lock()
            player.pulseManager.run(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(Animation(2450))
                            1 -> {
                                if (player.inventory.remove(node.asItem())) {
                                    player.inventory.add(Item(Items.BAILING_BUCKET_583))
                                }
                                player.unlock()
                                return true
                            }
                        }
                        return false
                    }
                },
            )
            return@on true
        }

        on(bailingBucket, IntType.ITEM, "bail-with") { player, node ->
            val session: FishingTrawlerSession? = player.getAttribute("ft-session", null)
            session ?: return@on false
            if (!session.isActive) {
                sendMessage(player, "I don't really need to bail yet.")
                return@on false
            }
            if (player.location.z > 0) {
                player.sendMessage("You can't scoop water out up here.")
                return@on true
            }
            player.lock()
            player.pulseManager.run(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        when (counter++) {
                            0 -> player.animator.animate(Animation(4471))
                            1 ->
                                if (player.inventory.remove(node.asItem())) {
                                    if (session.waterAmount > 0) {
                                        session.waterAmount -= 20
                                        if (session.waterAmount < 0) session.waterAmount = 0
                                        player.inventory.add(Item(Items.BAILING_BUCKET_585))
                                    } else {
                                        player.sendMessage("There's no water to remove.")
                                        player.inventory.add(node.asItem())
                                    }
                                }

                            2 -> player.unlock().also { return true }
                        }
                        return false
                    }
                },
            )
            return@on true
        }
    }
}

@Initializable
class NetLootDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var session: FishingTrawlerSession? = null
    var rolls = 0

    override fun newInstance(player: Player?): Dialogue {
        return NetLootDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        rolls = player.getAttribute("/save:ft-rolls", 0)
        if (rolls == 0) return false
        player.dialogueInterpreter.sendOptions("Skip Junk Items?", "Yes", "No")
        stage = 0
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val level = player.skills.getLevel(Skills.FISHING)
        when (buttonId) {
            1 -> TrawlerLoot.addLootAndMessage(player, level, rolls, true)
            2 -> TrawlerLoot.addLootAndMessage(player, level, rolls, false)
        }
        player.skills.addExperience(
            Skills.FISHING,
            (((0.015 * player.skills.getLevel(Skills.FISHING))) * player.skills.getLevel(Skills.FISHING)) * rolls,
        )
        player.removeAttribute("ft-rolls")
        end()
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(18237582)
    }
}

@Initializable
class NetRepairDialogue(
    player: Player? = null,
) : Dialogue(player) {
    var session: FishingTrawlerSession? = null

    override fun newInstance(player: Player?): Dialogue {
        return NetRepairDialogue(player)
    }

    override fun open(vararg args: Any?): Boolean {
        session = player.getAttribute("ft-session", null)
        if (session!!.netRipped) {
            sendDialogue(player, "The net is ripped and needs repair.")
            stage = 10
        } else {
            sendDialogue(player, "The net is in perfect condition")
            stage = 0
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        session ?: return false
        when (stage++) {
            0 -> end()
            10 -> options("Repair the net?", "Yes", "No")
            11 ->
                when (buttonId) {
                    1 -> {
                        end()
                        session!!.repairNet(player)
                    }

                    else -> {}
                }

            12 -> end()
        }

        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(18237583)
    }
}
