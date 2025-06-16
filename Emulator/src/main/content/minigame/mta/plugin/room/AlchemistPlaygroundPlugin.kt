package content.minigame.mta.plugin.room

import content.minigame.mta.plugin.MTAType
import content.minigame.mta.plugin.MTAZone
import core.api.removeAttribute
import core.api.sendDialogue
import core.api.sendString
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.settings
import core.game.world.map.Location
import core.game.world.map.RegionManager.getNpc
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.Music
import java.util.*

class AlchemistPlaygroundPlugin :
    MTAZone(
        "Alchemists' Playground",
        arrayOf(
            Item(Items.COINS_8890),
            Item(Items.LEATHER_BOOTS_6893),
            Item(Items.ADAMANT_KITESHIELD_6894),
            Item(Items.ADAMANT_MED_HELM_6895),
            Item(Items.EMERALD_6896),
            Item(Items.RUNE_LONGSWORD_6897),
        ),
    ) {
    override fun leave(
        entity: Entity,
        logout: Boolean,
    ): Boolean {
        if (entity is Player && PLAYERS.remove(entity)) {
            if (logout && entity.inventory.containsItem(COINS)) {
                val deposit = entity.inventory.getAmount(COINS)
                val earn = (deposit / 100).coerceAtLeast(0)
                incrementPoints(entity, MTAType.ALCHEMISTS.ordinal, earn)
                entity.inventory.remove(COINS)
            }
            removeAttribute(entity, "alchemist-session")
        }
        return super.leave(entity, logout)
    }

    override fun update(player: Player?) {
        sendString(
            player!!,
            player
                .getSavedData()
                .activityData
                .getPizazzPoints(type!!.ordinal)
                .toString(),
            type!!.overlay.id,
            3,
        )
    }

    override fun enter(entity: Entity): Boolean {
        if (guardian == null) guardian = getNpc(Location(3363, 9627, 2), 3099, 20)

        if (entity is Player && PLAYERS.add(entity)) {
            if (!PULSE.isRunning) {
                PULSE.restart()
                PULSE.start()
                Pulser.submit(PULSE)
            }
            removeAttribute(entity, "alch-earn")
            setSession(entity)
            updateInterface(entity)
            if (!entity.musicPlayer.hasUnlocked(Music.GOLDEN_TOUCH_535)) {
                entity.musicPlayer.unlock(Music.GOLDEN_TOUCH_535)
            }
        }
        return super.enter(entity)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        if (e is Player) {
            return when {
                target.id == 10734 -> {
                    deposit(e)
                    true
                }

                target.id in 6893..6897 && option.name !in listOf("drop", "take") -> {
                    sendDialogue(e, "This item isn't yours to wield, it belongs to the arena!")
                    true
                }

                target.name == "Cupboard" -> {
                    search(e, target.asScenery())
                    true
                }

                else -> super.interact(e, target, option)
            }
        }
        return super.interact(e, target, option)
    }

    private fun search(
        player: Player,
        scenery: Scenery,
    ) {
        val session = getSession(player)
        val item = session.getItem(scenery.id)
        if (scenery.id % 2 != 0) {
            player.animate(Animation.create(3032))
            SceneryBuilder.replace(scenery, scenery.transform(scenery.id + 1), 35)
        }
        if (item == null) {
            player.sendMessage("The cupboard is empty.")
            return
        }
        if (player.inventory.freeSlots() < 1) {
            player.sendMessage("You have no free space to hold any more items.")
            return
        }
        player.lock(1)
        player.inventory.add(item.item)
        player.sendMessage("You found: " + item.item.name)
    }

    private fun deposit(player: Player) {
        if (!player.inventory.containsItem(COINS)) {
            player.dialogueInterpreter.sendDialogue("You don't have any coins to deposit.")
            return
        }
        val deposit = player.inventory.getAmount(COINS)
        if (deposit >= 12000) {
            player.teleport(Location(3363, 3302, 0))
            player.dialogueInterpreter.sendDialogue(
                "You have been ejected from the arena! You were warned",
                "not to deposit more than 12000 coins at once.",
            )
            return
        }
        if (player.inventory.remove(Item(COINS.id, deposit))) {
            val `val` = deposit / 100
            val earn = if (`val` < 1) 0 else `val`
            val exp = deposit * 2
            var taking = player.getAttribute("alch-earn", 0)
            val add = (if (`val` > 0) (deposit / 100) * 10 else 0)
            if (add != 0) {
                taking += add
            }
            if (earn != 0) {
                incrementPoints(player, MTAType.ALCHEMISTS.ordinal, earn)
            }
            if (taking != 0) {
                player.setAttribute("alch-earn", taking)
            }
            player.getSkills().addExperience(Skills.MAGIC, exp.toDouble(), true)
            player.dialogueInterpreter.sendDialogue(
                "You've just deposited $deposit coins, earning you $earn Alchemist Pizazz",
                "Points and $exp magic XP. So far you're taking $taking coins as a",
                "a reward when you leave!",
            )
        }
    }

    override fun configure() {
        shufflePrices()
        PULSE.stop()
        register(ZoneBorders(3341, 9618, 3393, 9654, 2))
    }

    class AlchemistSession(
        val player: Player,
    ) {
        private var indexer = 0

        init {
            shuffleObjects()
        }

        fun getItem(id: Int): AlchemistItem? {
            val ids = intArrayOf(10797, 10795, 10793, 10791, 10789, 10787, 10785, 10783)
            var index = 0
            for (i in ids.indices) {
                if (ids[i] == id || (ids[i] + 1) == id) {
                    index = i
                    break
                }
            }
            var alchIndex = indexer + index
            if (indexer != 0) {
                if (indexer >= 4 && index < 4) {
                    if (indexer == 4 && indexer - index < 4) {
                        return null
                    }
                    if (indexer == 4) {
                        return AlchemistItem.LEATHER_BOOTS
                    } else {
                        if (indexer == 6 && index == 0) {
                            return AlchemistItem.ADAMANT_HELM
                        } else if (indexer == 6 && index == 1) {
                            return AlchemistItem.ADAMANT_KITESHIELD
                        } else if (indexer == 6 && index == 2) {
                            return AlchemistItem.LEATHER_BOOTS
                        } else if (indexer == 7 && index == 0) {
                            return AlchemistItem.EMERALD
                        } else if (indexer == 7 && index == 1) {
                            return AlchemistItem.ADAMANT_HELM
                        } else if (indexer == 7 && index == 2) {
                            return AlchemistItem.ADAMANT_KITESHIELD
                        } else if (indexer == 7 && index == 3) {
                            return AlchemistItem.LEATHER_BOOTS
                        } else if (indexer == 5 && index == 0) {
                            return AlchemistItem.ADAMANT_KITESHIELD
                        } else if (indexer == 5 && index == 1) {
                            return AlchemistItem.LEATHER_BOOTS
                        }
                    }
                    return null
                } else {
                    alchIndex -= (indexer + indexer)
                }
            }
            val finalIndex = (AlchemistItem.values().size - 1) - alchIndex
            if (finalIndex > AlchemistItem.values().size - 1 || finalIndex < 0) {
                return null
            }
            return AlchemistItem.values()[finalIndex]
        }

        fun shuffleObjects() {
            indexer++
            if (indexer > 7) {
                indexer = 0
            }
        }
    }

    enum class AlchemistItem(
        val item: Item,
    ) {
        LEATHER_BOOTS(Item(Items.LEATHER_BOOTS_6893)),
        ADAMANT_KITESHIELD(Item(Items.ADAMANT_KITESHIELD_6894)),
        ADAMANT_HELM(Item(Items.ADAMANT_MED_HELM_6895)),
        EMERALD(Item(Items.EMERALD_6896)),
        RUNE_LONGSWORD(Item(Items.RUNE_LONGSWORD_6897)),
        ;

        var cost = 0
        val child: Int get() = 14 + ordinal

        companion object {
            fun forItem(id: Int) = values().find { it.item.id == id }
        }
    }

    companion object {
        var ZONE: AlchemistPlaygroundPlugin = AlchemistPlaygroundPlugin()

        val COINS: Item = Item(8890)

        private val PLAYERS: MutableList<Player> = ArrayList(20)

        private var guardian: NPC? = null

        var freeConvert: AlchemistItem? = null

        private val PULSE: Pulse =
            object : Pulse(if (settings!!.isDevMode) 15 else 53) {
                override fun pulse(): Boolean {
                    if (PLAYERS.isEmpty()) {
                        return true
                    }
                    shufflePrices()
                    var forceChat = "The costs are changing!"
                    if (freeConvert == null && RandomFunction.random(3) < 3) {
                        freeConvert = RandomFunction.getRandomElement(AlchemistItem.values())
                        forceChat =
                            "The " + freeConvert!!.item.name.lowercase(Locale.getDefault()) + " " +
                            (if (freeConvert == AlchemistItem.LEATHER_BOOTS) "are" else "is") +
                            " free to convert!"
                    } else if (freeConvert != null) {
                        freeConvert = null
                    }
                    guardian!!.sendChat(forceChat)
                    for (p in PLAYERS) {
                        if (p == null || !p.isActive) {
                            continue
                        }
                        getSession(p).shuffleObjects()
                        updateInterface(p)
                    }
                    return false
                }
            }

        fun shufflePrices() {
            val list: List<Int> = mutableListOf(1, 5, 8, 10, 15, 20, 30)
            Collections.shuffle(list)
            for (i in AlchemistItem.values().indices) {
                AlchemistItem.values()[i].cost = list[i]
            }
        }

        fun updateInterface(player: Player) {
            for (i in AlchemistItem.values()) {
                player.packetDispatch.sendInterfaceConfig(
                    194,
                    i.child,
                    if (freeConvert == null) {
                        true
                    } else if (freeConvert == i) {
                        false
                    } else {
                        true
                    },
                )
                player.packetDispatch.sendString(i.cost.toString() + "", 194, 9 + i.ordinal)
            }
        }

        fun setSession(player: Player): AlchemistSession {
            val session = AlchemistSession(player)
            player.setAttribute("alchemist-session", session)
            return session
        }

        fun getSession(player: Player): AlchemistSession {
            var session = player.getAttribute<AlchemistSession>("alchemist-session", null)
            if (session == null) {
                session = setSession(player)
            }
            return session
        }
    }
}
