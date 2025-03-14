package content.region.wilderness.handlers.npc

import core.api.ui.setMinimapState
import core.api.utils.BossKillCounter
import core.game.activity.ActivityPlugin
import core.game.activity.CutscenePlugin
import core.game.component.Component
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.ChanceItem
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.GameWorld.ticks
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.net.packet.PacketRepository
import core.net.packet.context.CameraContext
import core.net.packet.out.CameraViewPacket
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import core.tools.RandomFunction
import org.rs.consts.NPCs

@Initializable
class BorkNPC : AbstractNPC {
    private val legions: MutableList<NPC> = ArrayList(20)
    private var spawnedLegion = false
    private var player: Player? = null
    private var cutscene: BorkCutscene? = null

    constructor() : super(-1, null)
    constructor(id: Int, location: Location?) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return BorkNPC(id, location)
    }

    override fun handleTickActions() {
        if (player == null) {
            return
        }
        super.handleTickActions()
        if (!locks.isMovementLocked && player != null) {
            if (!properties.combatPulse.isAttacking) {
                properties.combatPulse.attack(player)
            }
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        BossKillCounter.addToKillCount(killer as Player, this.id)
    }

    override fun commenceDeath(killer: Entity) {
        super.commenceDeath(killer)
        for (l in legions) {
            l.clear()
        }
        player!!.lock()
        cutscene!!.wizard!!.clear()
        cutscene!!.wizard!!.lock()
        player!!.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12)
        setMinimapState(player!!, 2)
        player!!.interfaceManager.open(Component(693))
        if (player!!.dialogueInterpreter.dialogue != null) {
            player!!.dialogueInterpreter.dialogue.end()
        }
        Pulser.submit(
            object : Pulse(10, player) {
                override fun pulse(): Boolean {
                    player!!.unlock()
                    player!!.dialogueInterpreter.sendDialogues(
                        player,
                        FaceAnim.FURIOUS,
                        "That monk - he called to Zamorak for revenge!",
                    )
                    player!!.sendMessage("Something is shaking the whole cavern! You should get out of here quick!")
                    PacketRepository.send(
                        CameraViewPacket::class.java,
                        CameraContext(player!!, CameraContext.CameraType.SHAKE, 3, 2, 2, 2, 2),
                    )
                    player!!.interfaceManager.restoreTabs()
                    setMinimapState(player!!, 0)
                    player!!.interfaceManager.close()
                    return true
                }
            },
        )
    }

    override fun handleDrops(
        p: Player,
        killer: Entity,
    ) {
        if (player!!.getAttribute("first-bork", false)) {
            player!!.removeAttribute("first-bork")
            player!!.getSkills().addExperience(Skills.SLAYER, 5000.0, true)
        } else {
            player!!.getSkills().addExperience(Skills.SLAYER, 1500.0, true)
        }
        val drops =
            if (Item(player!!.equipment.getId(12))
                    .name
                    .lowercase()
                    .contains("ring of wealth")
            ) {
                RING_DROPS
            } else {
                DROPS
            }
        for (i in drops.indices) {
            val item = Item(drops[i].id, RandomFunction.random(drops[i].minimumAmount, drops[i].maximumAmount))
            GroundItemManager.create(item, getLocation(), player!!)
        }
        if (RandomFunction.random(5) == 1) {
            super.handleDrops(p, killer)
        }
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        if (!spawnedLegion && getSkills().lifepoints < (getSkills().getStaticLevel(Skills.HITPOINTS) / 2)) {
            spawnLegion()
        }
    }

    private fun spawnLegion() {
        player!!.interfaceManager.removeTabs(0, 1, 2, 3, 4, 5, 6, 11, 12)
        setMinimapState(player!!, 2)
        spawnedLegion = true
        player!!.lock()
        player!!.impactHandler.disabledTicks = 14
        lock()
        cutscene!!.wizard!!.lock()
        properties.combatPulse.stop()
        player!!.properties.combatPulse.stop()
        animator.forceAnimation(Animation.create(8757))
        Pulser.submit(
            object : Pulse(1, player, this) {
                override fun pulse(): Boolean {
                    animator.forceAnimation(Animation.create(8757))
                    sendChat("Come to my aid, brothers!")
                    player!!.sendMessage("Bork strikes the ground with his axe.")
                    Pulser.submit(
                        object : Pulse(4, player) {
                            override fun pulse(): Boolean {
                                player!!.interfaceManager.open(Component(691))
                                return true
                            }
                        },
                    )
                    Pulser.submit(
                        object : Pulse(13, player) {
                            override fun pulse(): Boolean {
                                player!!.interfaceManager.close()
                                for (i in 0..2) {
                                    val legion =
                                        create(
                                            7135,
                                            getLocation().transform(
                                                RandomFunction.random(1, 3),
                                                RandomFunction.random(1, 3),
                                                0,
                                            ),
                                            player,
                                        )
                                    legion.init()
                                    legion.graphics(Graphics.create(1314))
                                    legion.isAggressive = true
                                    legion.isRespawn = false
                                    legion.attack(player)
                                    legions.add(legion)
                                    legion.sendChat(RandomFunction.getRandomElement(LEGION_CHATS))
                                }
                                player!!.unlock()
                                cutscene!!.wizard!!.unlock()
                                unlock()
                                if (player != null) {
                                    attack(player)
                                    player!!.interfaceManager.restoreTabs()
                                    setMinimapState(player!!, 0)
                                }
                                for (n in legions) {
                                    n.properties.combatPulse.attack(player)
                                }
                                return true
                            }
                        },
                    )
                    return true
                }
            },
        )
    }

    fun setPlayer(player: Player?) {
        this.player = player
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        definePlugin(BorkCutscene())
        definePlugin(DagonDialogue())
        definePlugin(OrkLegion())
        definePlugin(DagonElite())
        return super.newInstance(arg)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BORK_7133)
    }

    inner class OrkLegion : AbstractNPC {
        private var player: Player? = null

        private var lastTalk = ticks + 30

        constructor(id: Int, location: Location?) : super(id, location) {
            super.setAggressive(true)
        }

        constructor() : super(-1, null)

        override fun handleTickActions() {
            if (player == null) {
                return
            }

            if (lastTalk < ticks) {
                sendChat(LEGION_CHATS[RandomFunction.random(LEGION_CHATS.size)])
                lastTalk = ticks + 30
            }
        }

        override fun isIgnoreMultiBoundaries(victim: Entity): Boolean {
            return true
        }

        override fun canAttack(e: Entity): Boolean {
            return true
        }

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            val legion = OrkLegion(id, location)
            legion.player = objects[0] as Player
            return legion
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.ORK_LEGION_7135)
        }
    }

    inner class DagonElite : AbstractNPC {
        private var player: Player? = null

        constructor(id: Int, location: Location?) : super(id, location)

        constructor() : super(-1, null)

        override fun checkImpact(state: BattleState) {
            state.neutralizeHits()
        }

        override fun isIgnoreMultiBoundaries(victim: Entity): Boolean {
            return true
        }

        override fun canAttack(e: Entity): Boolean {
            return true
        }

        override fun isAttackable(
            e: Entity,
            style: CombatStyle,
            message: Boolean,
        ): Boolean {
            return false
        }

        override fun handleTickActions() {
            if (player == null) {
                return
            }
            super.handleTickActions()
            if (!properties.combatPulse.isAttacking) {
                properties.combatPulse.attack(player)
            }
        }

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            val elite = DagonElite(id, location)
            elite.player = objects[0] as Player
            return elite
        }

        override fun getIds(): IntArray {
            return intArrayOf(7137)
        }
    }

    inner class BorkCutscene() : CutscenePlugin("Bork cutscene") {
        private var bork: BorkNPC? = null
        var wizard: NPC? = null

        init {
            this.isMulticombat = true
        }

        constructor(player: Player?) : this() {
            this.player = player
        }

        override fun interact(
            e: Entity,
            target: Node,
            option: Option,
        ): Boolean {
            if (e is Player) {
                when (target.id) {
                    29537 -> {
                        e.asPlayer().graphics(Graphics.create(110))
                        e.asPlayer().teleport(Location.create(3143, 5545, 0))
                        return true
                    }
                }
            }
            return super.interact(e, target, option)
        }

        override fun leave(
            entity: Entity,
            logout: Boolean,
        ): Boolean {
            if (entity is Player) {
                PacketRepository.send(
                    CameraViewPacket::class.java,
                    CameraContext(entity.asPlayer(), CameraContext.CameraType.RESET, 3, 2, 2, 2, 2),
                )
            }
            return super.leave(entity, logout)
        }

        override fun start(
            player: Player,
            login: Boolean,
            vararg args: Any,
        ): Boolean {
            player.lock()
            bork = BorkNPC(7133, base.transform(26, 33, 0))
            bork!!.init()
            bork!!.setPlayer(player)
            bork!!.isRespawn = false
            bork!!.lock()
            bork!!.cutscene = this
            wizard = create(7137, base.transform(38, 29, 0), player)
            wizard!!.init()
            wizard!!.lock()
            wizard!!.faceTemporary(player, 2)
            player.impactHandler.disabledTicks = 5
            player.faceTemporary(wizard, 2)
            return super.start(player, login, *args)
        }

        override fun stop(fade: Boolean) {
            end()
        }

        fun commenceFight() {
            bork!!.unlock()
            wizard!!.unlock()
            player.unlock()
            wizard!!.attack(player)
            bork!!.attack(player)
            wizard!!.isRespawn = false
            player.interfaceManager.restoreTabs()
            setMinimapState(player, 0)
        }

        override fun open() {
            super.open()
            bork!!.cutscene!!.player = player
            player.lock()
            player.interfaceManager.open(Component(692))
            Pulser.submit(
                object : Pulse(13, player) {
                    override fun pulse(): Boolean {
                        commenceFight()
                        player.interfaceManager.close()
                        player.dialogueInterpreter.open("dagon-dialogue", wizard, this@BorkCutscene)
                        return true
                    }
                },
            )
        }

        override fun newInstance(p: Player?): ActivityPlugin {
            return BorkCutscene(player)
        }

        override fun getStartLocation(): Location {
            return base.transform(36, 33, 0)
        }

        override fun getSpawnLocation(): Location? {
            return null
        }

        override fun configure() {
            region = DynamicRegion.create(BORK_REGION)
            region.isMulticombat = true
            setRegionBase()
            registerRegion(region.id)
        }
    }

    inner class DagonDialogue : Dialogue {
        private var cutscene: BorkCutscene? = null

        constructor()

        constructor(player: Player?) : super(player)

        override fun newInstance(player: Player): Dialogue {
            return DagonDialogue(player)
        }

        override fun open(vararg args: Any): Boolean {
            npc = args[0] as NPC
            cutscene = args[1] as BorkCutscene
            npc(
                "Our Lord Zamorak has power over life and death,",
                player.username + "! He has seen fit to resurrect Bork to",
                "continue his great work...and now you will fall before him",
            )
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                0 -> {
                    val played = player.getSavedData().activityData.hasKilledBork()
                    player(if (played) "Uh -oh! Here we go again." else "Oh boy...")
                    stage++
                }

                1 -> {
                    end()
                    cutscene!!.commenceFight()
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(DialogueInterpreter.getDialogueKey("dagon-dialogue"))
        }
    }

    companion object {
        private val BORK_REGION = 12374
        private val LEGION_CHATS =
            arrayOf(
                "For Bork!",
                "Die, human!",
                "Resistance is futile!",
                "We are the collective!",
                "Form a triangle!!",
                "To the attack!",
                "Hup! 2... 3... 4!!",
            )

        private val DROPS =
            arrayOf(
                ChanceItem(532, 1, 1, 0.0),
                ChanceItem(12163, 5, 5, 0.0),
                ChanceItem(12160, 7, 7, 0.0),
                ChanceItem(12159, 2, 2, 0.0),
                ChanceItem(995, 2000, 10000, 0.0),
                ChanceItem(1619, 1, 1, 0.0),
                ChanceItem(1621, 1, 1, 0.0),
                ChanceItem(1623, 1, 1, 0.0),
            )

        private val RING_DROPS =
            arrayOf(
                ChanceItem(532, 1, 1, 0.0),
                ChanceItem(12163, 5, 5, 0.0),
                ChanceItem(12160, 10, 10, 0.0),
                ChanceItem(12159, 3, 3, 0.0),
                ChanceItem(1601, 1, 1, 0.0),
                ChanceItem(995, 2000, 10000, 0.0),
                ChanceItem(1619, 2, 2, 0.0),
                ChanceItem(1621, 3, 3, 0.0),
            )
    }
}
