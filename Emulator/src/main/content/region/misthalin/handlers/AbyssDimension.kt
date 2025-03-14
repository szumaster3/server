package content.region.misthalin.handlers

import content.data.items.SkillingTool
import content.global.skill.runecrafting.pouch.PouchManager.Pouches
import content.global.skill.runecrafting.pouch.RunePouch
import content.global.skill.runecrafting.scenery.Altar
import core.api.*
import core.api.quest.isQuestComplete
import core.api.skill.getTool
import core.game.dialogue.Dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.system.timer.impl.Skulled
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import core.tools.Log
import core.tools.RandomFunction
import core.tools.colorize
import org.rs.consts.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class AbyssDimension : InteractionListener {
    override fun defineListeners() {
        on(NPCs.MAGE_OF_ZAMORAK_2259, IntType.NPC, "teleport") { player, node ->
            teleport(player, node as NPC)
            return@on true
        }

        on(NPCs.DARK_MAGE_2262, IntType.NPC, "repair-pouches") { player, node ->
            player.dialogueInterpreter.open(node.id, node, true)
            return@on true
        }

        on(IntType.SCENERY, "exit-through") { player, node ->
            val altar = Altar.forScenery(node as core.game.node.scenery.Scenery)
            altar?.enterRift(player)
            return@on true
        }

        on(Scenery.PASSAGE_7154, IntType.SCENERY, "go-through") { player, node ->
            player.properties.teleportLocation = innerRing(node)
            return@on true
        }

        on(Scenery.ROCK_7158, IntType.SCENERY, "mine") { player, node ->
            val tool: SkillingTool? = getTool(player, true)
            if (tool == null) {
                sendMessage(player, "You need a pickaxe in order to do that.")
                return@on true
            }
            return@on handleObstacle(
                node,
                player,
                Skills.MINING,
                MINE_PROGRESS,
                Animation(tool.animation),
                arrayOf(
                    "You attempt to mine your way through...",
                    "...and manage to break through the rock.",
                    "...but fail to break-up the rock.",
                ),
            )
        }

        on(Scenery.TENDRILS_7161, IntType.SCENERY, "chop") { player, node ->
            val tool: SkillingTool? = getTool(player, false)
            if (tool == null) {
                sendMessage(player, "You need an axe in order to do that.")
                return@on true
            }
            return@on handleObstacle(
                node,
                player,
                Skills.WOODCUTTING,
                CHOP_PROGRESS,
                Animation(tool.animation),
                arrayOf(
                    "You attempt to chop your way through...",
                    "...and manage to chop down the tendrils.",
                    "...but fail to cut through the tendrils.",
                ),
            )
        }

        on(Scenery.BOIL_7165, IntType.SCENERY, "burn-down") { player, node ->
            if (!inInventory(player, Items.TINDERBOX_590)) {
                sendMessage(player, "You don't have a tinderbox to burn it.")
                return@on true
            }
            return@on handleObstacle(
                node,
                player,
                Skills.FIREMAKING,
                BURN_PROGRESS,
                Animation(Animations.HUMAN_LIGHT_FIRE_WITH_TINDERBOX_733),
                arrayOf(
                    "You attempt to burn your way through...",
                    "...and manage to burn it down and get past.",
                    "...but fail to set it on fire.",
                ),
            )
        }

        on(Scenery.EYES_7168, IntType.SCENERY, "distract") { player, node ->
            val distractEmote = Animation(distractEmotes[RandomFunction.random(0, distractEmotes.size)])
            return@on handleObstacle(
                node,
                player,
                Skills.THIEVING,
                DISTRACT_PROGRESS,
                distractEmote,
                arrayOf(
                    "You use your thieving skills to misdirect the eyes...",
                    "...and sneak past while they're not looking.",
                    "...but fail to distract the eyes.",
                ),
            )
        }

        on(Scenery.GAP_7164, IntType.SCENERY, "squeeze-through") { player, node ->
            return@on handleObstacle(
                node,
                player,
                Skills.AGILITY,
                null,
                Animation(Animations.HUMAN_SQUEEZE_INTO_GAP_1331),
                arrayOf(
                    "You attempt to squeeze through the narrow gap...",
                    "...and you manage to crawl through.",
                    "...but fail to crawl through.",
                ),
            )
        }
    }

    private val distractEmotes =
        intArrayOf(
            855,
            856,
            857,
            858,
            859,
            860,
            861,
            862,
            863,
            864,
            865,
            866,
            2113,
            2109,
            2111,
            2106,
            2107,
            2108,
            0x558,
            2105,
            2110,
            2112,
            0x84F,
            0x850,
            1131,
            1130,
            1129,
            1128,
            1745,
            3544,
            3543,
            2836,
        )

    companion object {
        const val ABYSS_OBSTACLES = 18
        const val VARP_SCENERY_ABYSS = Vars.VARP_SCENERY_ABYSS_491

        fun teleport(
            player: Player,
            npc: NPC,
        ) {
            var teleportLoc = AbyssLoc.randomLoc()
            while (!teleportLoc.isValid()) {
                teleportLoc = teleportLoc.attract()
            }

            player.lock(3)
            npc.visualize(
                Animation(Animations.CAST_SPELL_1979),
                Graphics(org.rs.consts.Graphics.DELRIGHT_DEFEATED_4),
            )
            npc.sendChat("Veniens! Sallakar! Rinnesset!")
            player.skills.decrementPrayerPoints(100.0)
            removeTimer<Skulled>(player)
            registerTimer(player, spawnTimer<Skulled>(2000))
            GameWorld.Pulser.submit(
                object : Pulse(2, player) {
                    override fun pulse(): Boolean {
                        rotateObstacles(player, teleportLoc)
                        player.properties.teleportLocation = teleportLoc.toAbs()
                        npc.updateMasks.reset()
                        return true
                    }
                },
            )
        }

        fun innerRing(node: Node): Location {
            val obstacleLoc = AbyssLoc.fromAbs(node.location)
            var loc = obstacleLoc.attract(5)
            while (!loc.isValid()) {
                loc = loc.attract()
            }
            return loc.toAbs()
        }

        fun rotateObstacles(
            player: Player,
            abyssLoc: AbyssLoc,
        ) {
            setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, abyssLoc.getSegment(), true)
        }

        const val MINE_PROGRESS = 12
        const val CHOP_PROGRESS = 14
        const val BURN_PROGRESS = 16
        const val DISTRACT_PROGRESS = 18

        fun handleObstacle(
            obstacle: Node,
            player: Player,
            skill: Int,
            varbitVal: Int?,
            animation: Animation,
            messages: Array<String>,
        ): Boolean {
            log(this::class.java, Log.FINE, "handled abyss ${obstacle.name}")
            player.lock()
            player.animate(animation)
            GameWorld.Pulser.submit(
                object : Pulse(1, player) {
                    var count = 0

                    override fun pulse(): Boolean {
                        when (count++) {
                            1 -> sendMessage(player, messages[0])
                            3 ->
                                return if (RandomFunction.random(100) < getStatLevel(player, skill) + 1) {
                                    sendMessage(player, colorize("%G${messages[1]}"))
                                    if (varbitVal != null) {
                                        setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, varbitVal)
                                    }
                                    false
                                } else {
                                    sendMessage(player, colorize("%R${messages[2]}"))
                                    player.unlock()
                                    true
                                }

                            5 -> {
                                if (varbitVal != null) {
                                    setVarbit(player, Vars.VARBIT_SCENERY_ABYSS_OBSTACLES_625, varbitVal or 1)
                                }
                            }

                            7 -> {
                                player.unlock()
                                player.properties.teleportLocation = innerRing(obstacle)
                                return true
                            }
                        }
                        return false
                    }

                    override fun stop() {
                        super.stop()
                        player.animate(Animation(-1, Animator.Priority.HIGH))
                    }
                },
            )
            return true
        }
    }
}

class AbyssLoc(
    val radius: Double,
    val angle: Double,
) {
    fun attract(steps: Int = 1): AbyssLoc {
        return AbyssLoc(radius - steps.toDouble(), angle)
    }

    fun getSegment(): Int {
        val segments = 12
        val angleToCircle = angle * segments / (2 * Math.PI)
        val angleSegment = (angleToCircle + 0.5).toInt()

        val normalSegment = (9 - angleSegment).mod(12)
        return normalSegment
    }

    fun toAbs(): Location {
        val x = (radius * cos(angle)).toInt()
        val y = (radius * sin(angle)).toInt()
        return origin.transform(x, y, 0)
    }

    fun isValid(): Boolean {
        val abs = toAbs()
        return (RegionManager.isTeleportPermitted(abs) && RegionManager.getObject(abs) == null)
    }

    companion object {
        val origin = Location(3039, 4832, 0)

        const val outerRadius = 25.1

        fun fromAbs(loc: Location): AbyssLoc {
            val local = Location.getDelta(origin, loc)
            val radius = Math.sqrt((local.x * local.x + local.y * local.y).toDouble())
            val angle = Math.atan2(local.y.toDouble(), local.x.toDouble())
            return AbyssLoc(radius, angle)
        }

        fun randomLoc(): AbyssLoc {
            val angle = Random.nextDouble() * 2 * Math.PI
            return AbyssLoc(outerRadius, angle)
        }
    }

    class AbyssalNPC : AbstractNPC {
        constructor() : super(0, null, true) {
            isAggressive = true
        }

        constructor(id: Int, location: Location?) : super(id, location, true)

        override fun construct(
            id: Int,
            location: Location,
            vararg objects: Any,
        ): AbstractNPC {
            return AbyssalNPC(id, location)
        }

        override fun init() {
            super.init()
            setDefaultBehavior()
        }

        override fun handleTickActions() {
            super.handleTickActions()
        }

        override fun finalizeDeath(killer: Entity) {
            super.finalizeDeath(killer)
            if (killer is Player) {
                val p = killer.asPlayer()
                if (RandomFunction.random(750) < 12) {
                    val pouch = getPouch(p)
                    if (pouch != null) {
                        definition.dropTables.createDrop(pouch.asItem(), p, this, getLocation())
                    }
                }
            }
        }

        private fun getPouch(player: Player): Int? {
            val small =
                hasAnItem(player, RunePouch.SMALL.pouch).container != null ||
                    hasAnItem(player, RunePouch.SMALL.decayedPouch.id).container != null
            val medium =
                hasAnItem(player, RunePouch.MEDIUM.pouch).container != null ||
                    hasAnItem(player, RunePouch.MEDIUM.decayedPouch.id).container != null
            val large =
                hasAnItem(player, RunePouch.LARGE.pouch).container != null ||
                    hasAnItem(player, RunePouch.LARGE.decayedPouch.id).container != null
            val giant =
                hasAnItem(player, RunePouch.GIANT.pouch).container != null ||
                    hasAnItem(player, RunePouch.GIANT.decayedPouch.id).container != null

            if (!small) {
                return RunePouch.SMALL.pouch
            }
            if (!medium) {
                return RunePouch.MEDIUM.pouch
            }
            if (!large) {
                return RunePouch.LARGE.pouch
            }
            return if (!giant) {
                RunePouch.GIANT.pouch
            } else {
                null
            }
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.ABYSSAL_LEECH_2263, NPCs.ABYSSAL_GUARDIAN_2264, NPCs.ABYSSAL_WALKER_2265)
        }
    }

    @Initializable
    class DarkMageDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            if (args.size >= 2) {
                if (repair()) {
                    npc("There, I have repaired your pouches.", "Now leave me alone. I'm concentrating.").also {
                        stage = 30
                    }
                    return true
                } else {
                    npc("You don't seem to have any pouches in need of repair.", "Leave me alone.").also { stage = 30 }
                    return true
                }
            }
            player("Hello there.")
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            when (stage) {
                0 -> npc("Quiet!", "You must not break my concentration!").also { stage++ }
                1 ->
                    options(
                        "Why not?",
                        "What are you doing here?",
                        "Can you repair my pouches?",
                        "Ok, Sorry",
                    ).also { stage++ }

                2 ->
                    when (buttonId) {
                        1 -> player("Why not?").also { stage = 10 }
                        2 -> player("What are you doing here?").also { stage = 20 }
                        3 -> player("Can you repair my pouches, please?").also { stage = 50 }
                        4 -> player("Ok, sorry.").also { stage = END_DIALOGUE }
                    }

                10 ->
                    npc(
                        "Well, if my concentration is broken while keeping this",
                        "gate open, then if we are lucky, everyone within a one",
                        "mile radius will either have their heads explode, or will be",
                        "consumed internally by the creatures of the Abyss.",
                    ).also {
                        stage++
                    }

                11 -> player("Erm...", "And if we are unlucky?").also { stage++ }
                12 ->
                    npc(
                        "If we are unlucky, then the entire universe will begin",
                        "to fold in upon itself, and all reality as we know it will",
                        "be annihilated in a single stroke.",
                    ).also {
                        stage++
                    }

                13 -> npc("So leave me alone!").also { stage = END_DIALOGUE }
                20 ->
                    npc(
                        "Do you mean what am I doing here in Abyssal space,",
                        "Or are you asking me what I consider my ultimate role",
                        "to be in this voyage that we call life?",
                    ).also {
                        stage++
                    }

                21 -> player("Um... the first one.").also { stage++ }
                22 ->
                    npc(
                        "By remaining here and holding this portal open, I am",
                        "providing a permanent link between normal space and",
                        "this strange dimension that we call Abyssal space.",
                    ).also {
                        stage++
                    }

                23 ->
                    npc(
                        "As long as this spell remains in effect, we have the",
                        "capability to teleport into abyssal space at will.",
                    ).also {
                        stage++
                    }

                24 -> npc("Now leave me be!", "I can afford no distraction in my task!").also { stage = END_DIALOGUE }
                50 -> npc("Fine, fine! Give them here.").also { stage++ }
                51 -> {
                    repair()
                    npc("There, I've repaired them all.", "Now get out of my sight!").also { stage = END_DIALOGUE }
                }
            }
            return true
        }

        fun repair(): Boolean {
            player.pouchManager.pouches.forEach { (id: Int, pouch: Pouches) ->
                pouch.currentCap = pouch.capacity
                val newCharges: Int =
                    when (id) {
                        Items.MEDIUM_POUCH_5510 -> 264
                        Items.LARGE_POUCH_5512 -> 186
                        Items.GIANT_POUCH_5514 -> 140
                        else -> 3
                    }
                pouch.charges = newCharges
                var essence: Item? = null
                if (!pouch.container.isEmpty) {
                    val ess = pouch.container[0].id
                    val amount = pouch.container.getAmount(ess)
                    essence = Item(ess, amount)
                }
                pouch.remakeContainer()
                if (essence != null) {
                    pouch.container.add(essence)
                }
                if (id != 5509) {
                    if (player.inventory.contains(id + 1, 1)) {
                        player.inventory.remove(Item(id + 1, 1))
                        player.inventory.add(Item(id, 1))
                    }
                    if (player.bank.contains(id + 1, 1)) {
                        player.bank.remove(Item(id + 1, 1))
                        player.bank.add(Item(id, 1))
                    }
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(NPCs.DARK_MAGE_2262)
        }
    }

    @Initializable
    class MageOfZamorakDialogue(
        player: Player? = null,
    ) : Dialogue(player) {
        private var varrockMage = false

        override fun open(vararg args: Any?): Boolean {
            npc = args[0] as NPC
            varrockMage = npc.id == 2261 || npc.id == 2260
            if (!isQuestComplete(player, Quests.RUNE_MYSTERIES)) {
                end()
                sendMessage(player, "The mage doesn't seem interested in talking to you.")
                return true
            }
            if (!varrockMage) {
                when (getStage()) {
                    0 -> npc("Meet me in Varrock's Chaos Temple.", "Here is not the place to talk.")
                    1, 2, 3 -> npc("I already told you!", "meet me in the Varrock Chaos Temple!")
                    4 -> npc("This is no place to talk!", "Meet me at the Varrock Chaos Temple!")
                }
            } else {
                when (getStage()) {
                    0 -> npc("I am in no mood to talk to you", "stranger!")
                    1 ->
                        npc(
                            "Ah, you again.",
                            "What was it you wanted?",
                            "The wilderness is hardly the appropriate place for a",
                            "conversation now, is it?",
                        )
                    2 -> npc("Well?", "Have you managed to use my scrying orb to obtain the", "information yet?")
                    3 -> player("So... that's my end of the deal upheld.", "What do I get in return?")
                    4 ->
                        options(
                            "So what is this 'abyss' stuff?",
                            "Is this abyss dangerous?",
                            "Can you teleport me there now?",
                        )
                }
            }
            return true
        }

        override fun handle(
            interfaceId: Int,
            buttonId: Int,
        ): Boolean {
            if (!varrockMage) {
                when (getStage()) {
                    0 -> {
                        setStage(1)
                        end()
                    }

                    1, 2, 3, 4 -> end()
                }
            } else {
                when (getStage()) {
                    0 -> end()
                    1 ->
                        when (stage) {
                            0 ->
                                options(
                                    "I'd like to buy some runes!",
                                    "Where do you get your runes from?",
                                    "All hail Zamorak!",
                                    "Nothing, thanks.",
                                ).also {
                                    stage++
                                }
                            1 ->
                                when (buttonId) {
                                    1 -> player("I'd like to buy some runes!").also { stage = 10 }
                                    2 ->
                                        player(
                                            "Where do you get your runes from?",
                                            "No offence, but people around here don't exactly like",
                                            "'your type'.",
                                        ).also {
                                            stage =
                                                20
                                        }
                                    3 ->
                                        player(
                                            "All hail Zamorak!",
                                            "He's the man!",
                                            "If he can't do it, maybe some other guy can!",
                                        ).also {
                                            stage =
                                                30
                                        }
                                    4 ->
                                        player(
                                            "I didn't really want anything, thanks.",
                                            "I just like talking to random people I meet around the",
                                            "world.",
                                        ).also {
                                            stage =
                                                40
                                        }
                                }
                            10 ->
                                npc(
                                    "I do not conduct business in this pathetic city.",
                                    "Speak to me in the wilderness if you desire to purchase",
                                    "runes from me.",
                                ).also {
                                    stage =
                                        END_DIALOGUE
                                }
                            20 -> npc("My 'type' Explain.").also { stage++ }
                            21 ->
                                player(
                                    "You know...",
                                    "Scary bearded men in dark clothing with unhealthy",
                                    "obsessions with destruction and stuff.",
                                ).also {
                                    stage++
                                }
                            22 ->
                                npc(
                                    "Hmmm.",
                                    "Well, you may be right, the foolish Saradominists that",
                                    "own this pathetic city don't appreciate loyal Zamorakians,",
                                    "it is true.",
                                ).also {
                                    stage++
                                }
                            23 ->
                                player(
                                    "So you can't be getting your runes anywhere around",
                                    "here...",
                                ).also { stage++ }
                            24 ->
                                npc(
                                    "That is correct stranger.",
                                    "My mysteries of manufacturing Runes is a closely",
                                    "guarded secret of the Zamorakian brotherhood.",
                                ).also {
                                    stage++
                                }
                            25 ->
                                player(
                                    "Oh, you mean the whole teleporting to the Rune",
                                    "essence mine, mining some essence, then using the",
                                    "talismans to locate the Rune Temples, then binding",
                                    "runes there?",
                                ).also {
                                    stage++
                                }
                            26 -> player("I know all about it...").also { stage++ }
                            27 -> npc("WHAT?", "I... but... you...").also { stage++ }
                            28 ->
                                npc(
                                    "Tell me, this is important:",
                                    "You know of the ancient temples?",
                                    "You have been to a place where this 'rune essence'",
                                    "material is freely available?",
                                ).also {
                                    stage++
                                }
                            29 -> npc("How did you get to such place?").also { stage = 200 }
                            200 ->
                                player(
                                    "Well, I helped deliver some research notes to Sedridor",
                                    "at the Wizards Tower, and he teleported me to a huge",
                                    "mine he said was hidden off to the North somewhere",
                                    "where I could mine essence.",
                                ).also {
                                    stage++
                                }

                            201 ->
                                npc(
                                    "And there is an abundant supply of this 'essence' there",
                                    "you say?",
                                ).also { stage++ }
                            202 ->
                                player(
                                    "Yes, but I thought you said that you knew how to make",
                                    "runes?",
                                    "All this stuff is fairly basic knowledge I thought.",
                                ).also {
                                    stage++
                                }

                            203 -> npc("No.", "No, not at all.").also { stage++ }
                            204 ->
                                npc(
                                    "We occasionally manage to plunder small samples of this",
                                    "'essence' and we have recently discovered these temples",
                                    "you speak of, but I have never ever heard of these talismans",
                                    "before, and I was certainly not aware that this 'essence'",
                                ).also {
                                    stage++
                                }

                            205 ->
                                npc(
                                    "substance is a heavily stockpiled resource at the Wizards",
                                    "Tower.",
                                ).also { stage++ }
                            206 -> npc("This changes everything.").also { stage++ }
                            207 -> player("How do you mean?").also { stage++ }
                            208 ->
                                npc(
                                    "For many years there has been a struggle for power",
                                    "on this world.",
                                    "You may dispute the morality of each side as you wish,",
                                    "but the stalemate that exists between my Lord Zamorak",
                                ).also {
                                    stage++
                                }

                            209 ->
                                npc(
                                    "and that pathetic meddling fool Saradomin has meant",
                                    "that our struggle have become more secretive.",
                                    "We exist in a 'cold war' if you will, each side fearful of",
                                    "letting the other gain too much power, and each side",
                                ).also {
                                    stage++
                                }

                            210 ->
                                npc(
                                    "equally fearful of entering into open warfare for fear of",
                                    "bringing our struggles to the attention of... other",
                                    "beings.",
                                ).also {
                                    stage++
                                }

                            211 -> player("You mean Guthix?").also { stage++ }
                            212 ->
                                npc(
                                    "Indeed.",
                                    "Amongst others.",
                                    "But you now tell me that the Saradominist Wizards",
                                    "have the capability to mass produce runes, I can only",
                                ).also {
                                    stage++
                                }

                            213 ->
                                npc(
                                    "conclude that they have been doing so secretly for some",
                                    "time now.",
                                ).also { stage++ }
                            214 ->
                                npc(
                                    "I implore you adventurer, you may or may not agree",
                                    "with my aims, but you cannot allow such a one-sided",
                                    "shift in the balance of power to occur.",
                                ).also {
                                    stage++
                                }

                            215 ->
                                npc(
                                    "Will you help me and my fellow Zamorakians to access",
                                    "this 'essence' mine?",
                                    "In return I will share with you the research we have",
                                    "gathered.",
                                ).also {
                                    stage++
                                }

                            216 -> player("Okay, I'll help you.", "What can I do?").also { stage++ }
                            217 ->
                                npc(
                                    "All I need from you is the spell that will teleport me to",
                                    "this essence mine.",
                                    "That should be sufficient for the armies of Zamorak to",
                                    "once more begin stockpiling magic for war.",
                                ).also {
                                    stage++
                                }

                            218 -> player("Oh.", "Erm...", "I don't actually know that spell.").also { stage++ }
                            219 -> npc("What?", "Then how do you access this location?").also { stage++ }
                            220 ->
                                player(
                                    "Oh, well, people who do know the spell teleport me there",
                                    "directly.",
                                    "Apparently they wouldn't teach it to me to try and keep",
                                    "the location secret.",
                                ).also {
                                    stage++
                                }

                            221 ->
                                npc(
                                    "Hmmm.",
                                    "Yes, yes I see.",
                                    "Very well then, you may still assist us in finding this",
                                    "mysterious essence mine.",
                                ).also {
                                    stage++
                                }

                            222 -> player("How would I do that?").also { stage++ }
                            223 -> {
                                end()
                                setStage(2)
                                player.inventory.add(ORBS[0], player)
                                npc(
                                    "Here, take this scrying orb.",
                                    "I have cast a standard cypher spell upon it, so that it",
                                    "will absorb mystical energies that it is exposed to.",
                                )
                            }

                            30 -> end()
                            40 ->
                                npc(
                                    "...I see.",
                                    "Well, in the future, do not waste my time, or you will",
                                    "feel the wrath of Zamorak upon you.",
                                ).also {
                                    stage++
                                }

                            41 -> end()
                        }

                    2 ->
                        when (stage) {
                            0 -> {
                                if (!player.hasItem(ORBS[0]) && !player.inventory.containsItem(ORBS[1])) {
                                    player(
                                        "Uh...",
                                        "No...",
                                        "I kinda lost that orb thingy that you gave me.",
                                    ).also { stage++ }
                                }
                                if (!player.inventory.containsItem(ORBS[1])) {
                                    player(
                                        "No...",
                                        "Actually, I had something I wanted to ask you...",
                                    ).also { stage = 3 }
                                } else {
                                    player("Yes I have! I've got it right here!").also { stage = 50 }
                                }
                            }

                            1 -> {
                                end()
                                player.inventory.add(ORBS[0], player)
                                npc(
                                    "What?",
                                    "Incompetent fool. Take this.",
                                    "And do not make me regret allying myself with you.",
                                )
                            }

                            3 ->
                                npc(
                                    "I assume the task to be self-explanatory.",
                                    "What is it you wish to know?",
                                ).also { stage++ }
                            4 ->
                                player(
                                    "Please excuse me, I have a very bad short term",
                                    "memory.",
                                    "What exactly am I supposed to be doing again?",
                                ).also {
                                    stage++
                                }
                            5 ->
                                npc(
                                    "All I wish for you to do is to teleport to this 'rune",
                                    "essence' location from three different locations while",
                                    "carrying the scrying orb I gave you.",
                                    "It will collect the data as you teleport.",
                                ).also {
                                    stage =
                                        END_DIALOGUE
                                }

                            224 ->
                                npc(
                                    "Bring it with you and teleport to the rune essence",
                                    "location, and it will absorb the mechanics of the spell and",
                                    "allow us to reverse-engineer the magic behind it.",
                                ).also {
                                    stage++
                                }
                            225 ->
                                npc(
                                    "The important part is that you must teleport to the",
                                    "essence location from three entirely separate locations.",
                                ).also {
                                    stage++
                                }
                            226 ->
                                npc(
                                    "More than three may be helpful to us, but we need a",
                                    "minimum of three in order to triangulate the position of",
                                    "this essence mine.",
                                ).also {
                                    stage++
                                }
                            227 -> npc("Is that all clear, stranger?").also { stage++ }
                            228 -> player("Yeah, I think so.").also { stage++ }
                            229 ->
                                npc("Good.", "If you encounter any difficulties speak to me again.").also {
                                    stage =
                                        END_DIALOGUE
                                }

                            50 ->
                                npc(
                                    "Excellent.",
                                    "Give it here, and I shall examine the findings.",
                                    "Speak to me in a small while.",
                                ).also {
                                    stage++
                                }

                            51 -> {
                                setStage(3)
                                player.inventory.remove(ORBS[1])
                                end()
                            }
                        }

                    3 ->
                        when (stage) {
                            0 -> npc("Indeed, a deal is always a deal.").also { stage++ }
                            1 ->
                                npc(
                                    "I offer you three things as a reward for your efforts on",
                                    "behalf of my Lord Zamorak;",
                                ).also { stage++ }
                            2 ->
                                npc(
                                    "The first is knowledge.",
                                    "I offer you my collected research on the abyss.",
                                    "I also offer you 1000 points of experience in",
                                    "RuneCrafting for your trouble.",
                                ).also {
                                    stage++
                                }
                            3 ->
                                npc(
                                    "Your second gift is convenience.",
                                    "Here you may take this pouch I discovered amidst my",
                                    "research.",
                                    "You will find it to have certain... interesting properties.",
                                ).also {
                                    stage++
                                }
                            4 ->
                                npc(
                                    "Your final gift is that of movement",
                                    "I will from now on offer you a teleport to the abyss",
                                    "whenever you should require it.",
                                ).also {
                                    stage++
                                }

                            5 -> {
                                setStage(4)
                                addItem(player, Items.ABYSSAL_BOOK_5520)
                                addItem(player, Items.SMALL_POUCH_5509)
                                rewardXP(player, Skills.RUNECRAFTING, 1000.0)
                                player(
                                    "Huh?",
                                    "Abyss?",
                                    "What are you talking about?",
                                    "You told me that you would help me with",
                                )
                                stage++
                            }
                        }

                    4 ->
                        when (stage) {
                            0 ->
                                when (buttonId) {
                                    1 ->
                                        player(
                                            "Uh...",
                                            "I really don't see how this talk about an 'abyss' relates",
                                            "to RuneCrafting in the slightest...",
                                        ).also {
                                            stage =
                                                10
                                        }

                                    2 ->
                                        player(
                                            "So...",
                                            "This 'abyss' place...",
                                            "Is it dangerous?",
                                        ).also { stage = 20 }
                                    3 ->
                                        player(
                                            "Well, I reckon I'm prepared to go there now.",
                                            "Beam me there, or whatever it is that you do!",
                                        ).also {
                                            stage =
                                                30
                                        }
                                }
                            10 ->
                                npc(
                                    "My primary research responsibility was not towards the",
                                    "manufacture of runes, this is true.",
                                ).also {
                                    stage =
                                        END_DIALOGUE
                                }
                            20 ->
                                npc(
                                    "Well, the creatures there ARE particularly offensive...",
                                ).also { stage = END_DIALOGUE }
                            30 ->
                                npc(
                                    "No, not from here.",
                                    "The use of my Lord Zamorak magic in this land will",
                                    "draw too much attention to myself.",
                                ).also {
                                    stage =
                                        8
                                }
                            6 -> player("RuneCrafting!").also { stage++ }
                            7 ->
                                npc(
                                    "And so I have done.",
                                    "Read my research notes, they may enlighten you",
                                    "somewhat.",
                                ).also { stage++ }
                            8 -> end()
                        }
                }
            }
            return true
        }

        override fun getIds(): IntArray {
            return intArrayOf(
                2257,
                NPCs.MAGE_OF_ZAMORAK_2258,
                NPCs.MAGE_OF_ZAMORAK_2259,
                NPCs.MAGE_OF_ZAMORAK_2260,
                NPCs.MAGE_OF_ZAMORAK_2261,
            )
        }

        override fun setStage(stage: Int) {
            setVarp(player, 492, stage, true)
        }

        fun getStage(): Int {
            return getVarp(player, 492)
        }

        companion object {
            private val ORBS = arrayOf(Item(Items.SCRYING_ORB_5519), Item(Items.SCRYING_ORB_5518))
        }
    }
}
