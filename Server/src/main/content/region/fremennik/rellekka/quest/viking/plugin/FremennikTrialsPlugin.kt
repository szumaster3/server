package content.region.fremennik.rellekka.quest.viking.plugin

import content.data.GameAttributes
import content.global.skill.gather.woodcutting.WoodcuttingPulse
import content.region.fremennik.plugin.LyreTeleport
import content.region.fremennik.rellekka.dialogue.CouncilWorkerDialogue
import content.region.fremennik.rellekka.quest.viking.dialogue.FremennikFishermanDialogue
import content.region.fremennik.rellekka.quest.viking.npc.KoscheiSession
import core.api.*
import core.game.container.impl.EquipmentContainer.SLOT_WEAPON
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.music.MusicEntry
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.config.ItemConfigParser
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import core.tools.secondsToTicks
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery as Objects

class FremennikTrialsPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles interaction with Fisherman.
         */

        on(FISHERMAN, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, FremennikFishermanDialogue(), node)
            return@on true
        }

        /*
         * Handles exchange the beer with cherry bomb.
         */

        onUseWith(IntType.NPC, BEER, WORKER) { player, beer, _ ->
            player.dialogueInterpreter.open(CouncilWorkerDialogue(0, true, beer.id), NPC(WORKER))
            return@onUseWith true
        }

        /*
         * Handles interaction with fossegrimen.
         */

        onUseWith(IntType.SCENERY, FISH, FISH_ALTAR) { player, fish, _ ->
            val hasLyre = anyInInventory(player, Items.LYRE_3689, Items.ENCHANTED_LYRE_3690)

            if (!hasLyre) {
                sendMessage(player, "I should probably have my lyre with me.")
                return@onUseWith true
            }

            if (inInventory(player, Items.RAW_BASS_363)) {
                sendNPCDialogue(player, NPCs.FOSSEGRIMEN_1273, "That's not a bass, it's a very small shark.")
            } else {
                sendMessage(player, "Fossegrimen require a greater offering to enchant your lyre.")
            }

            Pulser.submit(SpiritPulse(player, fish.id))
            return@onUseWith true
        }

        /*
         * Handles mix the alcohol.
         */

        onUseWith(IntType.ITEM, LOW_ALC_KEG, KEG) { player, _, _ ->
            if (getAttribute(player, GameAttributes.QUEST_VIKING_MANI_KEG, false)) {
                return@onUseWith false
            }

            if (!getAttribute(player, GameAttributes.QUEST_VIKING_MANI_BOMB, false)) {
                sendDialogueLines(player, "I can't do this right now. I should create", "a distraction.")
                return@onUseWith true
            }

            if (!inBorders(player, 2655, 3665, 2662, 3681)) {
                sendMessage(player, "You can only use the beerkeg in the Fremennik long hall.")
                return@onUseWith false
            }

            if(removeItem(player, LOW_ALC_KEG)) {
                setAttribute(player, GameAttributes.QUEST_VIKING_MANI_KEG, true)
                sendMessage(player, "You hear a loud bang from outside. It echoes through the drain.")
                sendMessage(player, "You empty the keg and refill it with low alcohol beer.")
                RegionManager.getLocalEntitys(player).forEach { it.sendChat("What was THAT??") }
            }
            return@onUseWith true
        }

        /*
         * Handles lit the cherry bomb.
         */

        onUseWith(IntType.ITEM, TINDERBOX, CHERRY_BOMB) { player, _, _ ->
            if (removeItem(player, CHERRY_BOMB)) {
                addItem(player, LIT_BOMB)
                sendMessage(player, "You light the string of the strange object. It starts to hiss slightly.")
            }

            submitWorldPulse(
                object : Pulse(
                RandomFunction.random(secondsToTicks(70), secondsToTicks(100))
            ){
                override fun pulse(): Boolean {
                    if (inInventory(player, LIT_BOMB))
                    {
                        removeItem(player, LIT_BOMB)
                        val safeDamage = player.skills.lifepoints - 1
                        val calculatedDamage = (player.skills.lifepoints * 0.04).toInt().coerceAtLeast(2)
                        val damage = calculatedDamage.coerceAtMost(safeDamage)

                        impact(player, damage)
                        sendMessage(player, "The strange object you lit earlier explodes in your inventory!")
                        sendChat(player, "Ow!")
                        return true
                    }
                    return false
                }
            })

            return@onUseWith true
        }

        onUseWith(IntType.SCENERY, CHERRY_BOMB, PIPE) { player, _, _ ->
            sendMessage(player, "You should light the strange object first.")
            return@onUseWith true
        }

        /*
         * Handles creating unstrung lyre.
         */

        onUseWith(IntType.ITEM, KNIFE, TREE_BRANCH) { player, _, _ ->
            if (getStatLevel(player, Skills.CRAFTING) < 40) {
                sendDialogue(player, "You need 40 crafting to do this!")
                return@onUseWith true
            }

            if (!inInventory(player, KNIFE)) {
                sendMessage(player, "You need a knife to do this.")
                return@onUseWith true
            }

            if(!hasSpaceFor(player, Item(UNSTRUNG_LYRE))){
                sendMessage(player, "You don't have enough inventory space.")
                return@onUseWith true
            }

            queueScript(player, 1, QueueStrength.WEAK) {
                animate(player, Animation(Animations.FLETCH_LOGS_1248))
                removeItem(player, TREE_BRANCH)
                addItem(player, UNSTRUNG_LYRE)
                delayClock(player, Clocks.SKILLING, 3)
                sendMessage(player, "You craft an unstrung lyre out of the branch.")
                return@queueScript stopExecuting(player)
            }

            return@onUseWith true
        }

        /*
         * Handles adding ingredients for Lallis soup.
         */

        onUseWith(IntType.SCENERY, STEW_INGREDIENT_IDS, LALLIS_STEW) { player, ingredient, _ ->

            val ingredientData = mapOf(
                Items.ONION_1957 to Pair(
                    "You added an onion to the stew.",
                    GameAttributes.QUEST_VIKING_STEW_INGREDIENTS_ONION
                ),
                Items.POTATO_1942 to Pair(
                    "You added a potato to the stew.",
                    GameAttributes.QUEST_VIKING_STEW_INGREDIENTS_POTATO
                ),
                Items.CABBAGE_1965 to Pair(
                    "You added a cabbage to the stew.",
                    GameAttributes.QUEST_VIKING_STEW_INGREDIENTS_CABBAGE
                ),
                Items.PET_ROCK_3695 to Pair(
                    "You added your dear pet rock to the stew.",
                    GameAttributes.QUEST_VIKING_STEW_INGREDIENTS_ROCK
                )
            )

            ingredientData[ingredient.id]?.let { (message, attribute) ->
                sendDialogue(player, message)
                setAttribute(player, attribute, true)
                removeItem(player, ingredient)
            }

            return@onUseWith true
        }

        /*
         * Handles creating lyre.
         */

        onUseWith(IntType.ITEM, UNSTRUNG_LYRE, GOLDEN_WOOL) { player, used, with ->
            if (getStatLevel(player, Skills.FLETCHING) < 25) {
                sendDialogue(player, "You need 25 Fletching level to do this!")
                return@onUseWith false
            }

            val hasItems = removeItem(player, used) && removeItem(player, with)
            if (!hasItems) return@onUseWith false

            animate(player, Animations.FLETCH_LOGS_1248)
            addItem(player, Items.LYRE_3689)
            sendDialogue(player, "You string the Lyre with the Golden Wool.")
            return@onUseWith true
        }

        /*
         * Handles interaction with doors to where we do the concert.
         */

        on(LONGHALL_BACKDOOR, IntType.SCENERY, "open") { player, node ->
            if (player.location == Location(2662, 3692, 0) ||
                player.location == Location(2661, 3692, 0)
            ) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                return@on true
            }
            if (player.location == Location(2621, 3666, 0) ||
                player.location == Location(2622, 3666, 0)
            ) {
                DoorActionHandler.handleDoor(player, node.asScenery())
                return@on true
            }

            when {
                getAttribute(player, GameAttributes.QUEST_VIKING_LYRE, false) -> {
                    sendNPCDialogue(player, NPCs.LONGHALL_BOUNCER_1278, "Yeah you're good to go through. Olaf tells me you're some kind of outerlander bard here on tour. I doubt you're worse than Olaf is.")
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                getAttribute(player, GameAttributes.QUEST_VIKING_OLAF_CONCERT, false) -> {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                }

                else -> {
                    sendNPCDialogue(player, NPCs.LONGHALL_BOUNCER_1278, "I didn't give you permission to go backstage!")
                }
            }
            return@on true
        }

        /*
         * Handles playing the lyre.
         */

        on(LYRE_IDs, IntType.ITEM, "play") { player, lyre ->
            if (getAttribute(player, GameAttributes.QUEST_VIKING_PLAYER_ON_STAGE, false) &&
                !getAttribute(player, GameAttributes.QUEST_VIKING_OLAF_CONCERT, false)
            ) {
                Pulser.submit(LyreConcertPulse(player, lyre.id))
                return@on true
            }

            if (getQuestStage(player, Quests.THE_FREMENNIK_TRIALS) < 20 ||
                !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)
            ) {
                sendMessage(player, "You lack the knowledge to play this.")
                return@on true
            }

            if (LYRE_IDs.isLast(lyre.id)) {
                sendMessage(player, "Your lyre is out of charges!")
                return@on true
            }

            if (hasTimerActive(player, GameAttributes.TELEBLOCK_TIMER)) {
                sendMessage(player, "A magical force has stopped you from teleporting.")
                return@on true
            }

            if (removeItem(player, lyre.asItem())) {
                addItem(player, LYRE_IDs.getNext(lyre.id))
                Pulser.submit(LyreTeleport(player))
            }

            return@on true
        }

        /*
         * Handles interaction with pipe.
         */

        on(PIPE, IntType.SCENERY, "put-inside") { player, _ ->
            if (removeItem(player, LIT_BOMB)) {
                sendMessage(player, "You put the lit strange object into the pipe.")
                setAttribute(player, GameAttributes.QUEST_VIKING_MANI_BOMB, true)
                runTask(player, 1) {
                    sendPlayerDialogue(player, "That is going to make a really loud bang when it goes off! It would be a perfect distraction to help me cheat in the drinking contest!", FaceAnim.HAPPY)
                }
            } else {
                sendPlayerDialogue(player, "I don't have anything I really want to put inside a smelly old drain pipe.", FaceAnim.THINKING)
            }
            return@on true
        }

        /*
         * Handles interaction with swensen room portals.
         */

        on(PORTALIDs, IntType.SCENERY, "use") { player, portal ->
            player.properties?.teleportLocation =
                when (portal.id) {
                    Objects.PORTAL_2273 -> DestRoom(2639, 10012, 2645, 10018).getCenter()
                    Objects.PORTAL_2274 -> DestRoom(2650, 10034, 2656, 10040).getCenter()
                    Objects.PORTAL_2506 -> DestRoom(2662, 10023, 2669, 10029).getCenter()
                    Objects.PORTAL_2507 -> DestRoom(2626, 10023, 2633, 10029).getCenter()
                    Objects.PORTAL_2505 -> DestRoom(2650, 10001, 2656, 10007).getCenter()
                    Objects.PORTAL_2503 -> DestRoom(2662, 10012, 2668, 10018).getCenter()
                    Objects.PORTAL_2504 -> {
                        setAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_MAZE, true)
                        DestRoom(2662, 10034, 2668, 10039).getCenter()
                    }

                    else -> getRandomLocation(player)
                }
            return@on true
        }

        /*
         * Handles interaction with the swensen ladder.
         */

        on(SWENSEN_LADDER, IntType.SCENERY, "climb-down") { player, _ ->
            when {
                !getAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_START, false) -> {
                    sendDialogueLines(player, "You try to open the trapdoor but it won't budge!", "It looks like the trapdoor can only be opened from the other side.")
                    sendMessage(player, "You try to open the trapdoor but it won't budge!")
                    sendMessage(player, "It looks like the trapdoor can only be opened from the other side.")
                }
                getAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_VOTE, false) || isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) -> {
                    sendPlayerDialogue(player, "No way am I doing that maze again!", FaceAnim.SCARED)
                }
                else -> {
                    teleport(player, Location.create(2631, 10006, 0))
                }
            }
            return@on true
        }

        /*
         * Handles enter to the warriors trial.
         */

        on(THORVALD_LADDER, IntType.SCENERY, "climb-down") { player, _ ->
            if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS) ||
                getAttribute(player, GameAttributes.QUEST_VIKING_THORVALD_VOTE, false)
            ) {
                sendMessage(player, "You have no reason to go back down there.")
                return@on true
            }

            if (!getAttribute(player, GameAttributes.QUEST_VIKING_THORVALD_START, false)) {
                sendNPCDialogueLines(player, NPCs.THORVALD_THE_WARRIOR_1289, FaceAnim.THINKING, false, "Outerlander... do not test my patience. I do not take", "kindly to people wandering in here and acting as though", "they own the place.")
                return@on true
            }

            if (hasEquippableItems(player)) {
                sendNPCDialogueLines(player, NPCs.THORVALD_THE_WARRIOR_1289, FaceAnim.THINKING, false, "You may not enter the battleground with any armour", "or weaponry of any kind.")
                addDialogueAction(player) { _, _ ->
                    sendNPCDialogueLines(player, NPCs.THORVALD_THE_WARRIOR_1289, FaceAnim.THINKING, false, "If you need to place your equipment into your bank", "account, I recommend that you speak to the seer. He", "knows a spell that will do that for you.")
                }
                return@on true
            }

            player.getExtension<Any?>(KoscheiSession::class.java)?.let {
                KoscheiSession.getSession(player).close()
            }

            ClimbActionHandler.climb(
                player,
                Animation(Animations.HUMAN_CLIMB_STAIRS_828),
                Location.create(2671, 10099, 2)
            )

            Pulser.submit(KoscheiPulse(player))
            return@on true
        }

        /*
         * Handles exit from warriors trial.
         */

        on(THORVALD_LADDER_LOWER, IntType.SCENERY, "climb-up") { player, _ ->
            if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
                KoscheiSession.getSession(player).close()
            }
            ClimbActionHandler.climb(player, Animation(Animations.HUMAN_CLIMB_STAIRS_828), Location.create(2666, 3694, 0))
            return@on true
        }

        /*
         * Handles interaction with swaying tree.
         */

        on(SWAYING_TREE, IntType.SCENERY, "cut-branch") { player, node ->
            player.pulseManager.run(WoodcuttingPulse(player, node as Scenery))
            return@on true
        }

        /*
         * Handles interaction with shop NPCs.
         */

        on(SHOPNPCS, IntType.NPC, "Trade") { player, npc ->
            if (isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                openNpcShop(player, npc.id)
                return@on true
            }

            val restrictedMessages = mapOf(
                NPCs.THORA_THE_BARKEEP_1300 to "Only Fremenniks may buy drinks here.",
                NPCs.SKULGRIMEN_1303 to "Only Fremenniks may purchase weapons and armour here.",
                NPCs.SIGMUND_THE_MERCHANT_1282 to "Only Fremenniks may trade with this merchant.",
                NPCs.YRSA_1301 to "Only Fremenniks may buy clothes here.",
                NPCs.FISH_MONGER_1315 to "Only Fremenniks may purchase fish here."
            )

            restrictedMessages[npc.id]?.let { msg ->
                sendMessage(player, msg)
            }

            return@on true
        }

        /*
         * Handles exit from swensen maze.
         */

        on(intArrayOf(Objects.LADDER_4159, Objects.ESCAPE_ROPE_4161), IntType.SCENERY, "climb-up") { player, _ ->
            teleport(player, Location.create(2644, 3658, 0), TeleportManager.TeleportType.INSTANT)
            removeAttribute(player, GameAttributes.QUEST_VIKING_SWENSEN_START)
            return@on true
        }

        /*
         * Handles play using lyre.
         */

        on(Items.LYRE_3689, IntType.ITEM, "play") { player, _ ->
            sendMessage(player, "You lack the knowledge to play this.")
            return@on true
        }
    }

    companion object {
        private const val WORKER = NPCs.COUNCIL_WORKMAN_1287
        private const val FISH_ALTAR = Objects.STRANGE_ALTAR_4141
        private const val LOW_ALC_KEG = Items.LOW_ALCOHOL_KEG_3712
        private const val KEG = Items.KEG_OF_BEER_3711
        private const val TINDERBOX = Items.TINDERBOX_590
        private const val CHERRY_BOMB = Items.STRANGE_OBJECT_3713
        private const val LIT_BOMB = Items.LIT_STRANGE_OBJECT_3714
        private const val PIPE = Objects.PIPE_4162
        private const val SWENSEN_LADDER = Objects.LADDER_4158
        private const val SWAYING_TREE = Objects.SWAYING_TREE_4142
        private const val KNIFE = Items.KNIFE_946
        private const val TREE_BRANCH = Items.BRANCH_3692
        private const val THORVALD_LADDER = Objects.LADDER_34286
        private const val THORVALD_LADDER_LOWER = Objects.LADDER_4188
        private const val LALLIS_STEW = Objects.LALLI_S_STEW_4149
        private const val UNSTRUNG_LYRE = Items.UNSTRUNG_LYRE_3688
        private const val GOLDEN_WOOL = Items.GOLDEN_WOOL_3694
        private const val LONGHALL_BACKDOOR = Objects.DOOR_4148
        private const val FISHERMAN = NPCs.FISHERMAN_1302
        private val LYRE_IDs = intArrayOf(Items.ENCHANTED_LYRE6_14591, Items.ENCHANTED_LYRE5_14590, Items.ENCHANTED_LYRE4_6127, Items.ENCHANTED_LYRE3_6126, Items.ENCHANTED_LYRE2_6125, Items.ENCHANTED_LYRE1_3691, Items.ENCHANTED_LYRE_3690)
        private val PORTALIDs = intArrayOf(Objects.PORTAL_2273, Objects.PORTAL_2274, Objects.PORTAL_2506, Objects.PORTAL_2507, Objects.PORTAL_2505, Objects.PORTAL_2503, Objects.PORTAL_2504, Objects.PORTAL_5138)
        private val BEER = intArrayOf(Items.BEER_3803, Items.BEER_1917)
        private val FISH = intArrayOf(Items.RAW_BASS_363, Items.RAW_SHARK_383, Items.RAW_SEA_TURTLE_395, Items.RAW_MANTA_RAY_389)
        private val STEW_INGREDIENT_IDS = intArrayOf(Items.POTATO_1942, Items.ONION_1957, Items.CABBAGE_1965, Items.PET_ROCK_3695)
        private val SHOPNPCS = intArrayOf(NPCs.YRSA_1301, NPCs.SKULGRIMEN_1303, NPCs.THORA_THE_BARKEEP_1300, NPCs.SIGMUND_THE_MERCHANT_1282, NPCs.FISH_MONGER_1315)
    }

    /**
     * Pulse for initiating the Koschei the Deathless combat session.
     */
    private class KoscheiPulse(val player: Player) : Pulse() {
        var counter = 0

        override fun pulse(): Boolean {
            when (counter++) {
                0 -> sendMessage(player, "Explore this battleground and find your foe...")
                20 -> {
                    if (player.getExtension<Any?>(KoscheiSession::class.java) != null) {
                        return true
                    }
                    KoscheiSession.create(player).start().also { return true }
                }
            }
            return false
        }
    }

    /**
     * Handles the pulse for the concert performance and upgrades the lyre.
     */
    private class LyreConcertPulse(private val player: Player, private val lyre: Int) : Pulse() {

        private var counter = 0

        private val questPoints = getQuestPoints(player)
        private val champGuild = player.achievementDiaryManager?.hasCompletedTask(DiaryType.VARROCK, 1, 1) ?: false
        private val legGuild = questPoints >= 111
        private val heroGuild = questPoints >= 5
        private val masteredSkills = player.getSkills()?.masteredSkills ?: 0
        private val skillNames = getMasteredSkillNames(player)

        private val lyrics: Array<String> = when {
            masteredSkills > 0 -> arrayOf("When people speak of training,", "Some people think they're fine.", "But they just all seem jealous that", "My ${skillNames.random()}'s ninety-nine!")
            legGuild -> arrayOf("I cannot even start to list", "The amount of foes I've killed.", "I will simply tell you this:", "I've joined the Legends' Guild!")
            heroGuild -> arrayOf("When it comes to fighting", "I hit my share of zeroes", "But I'm well respected at", "the Guild reserved for Heroes,")
            champGuild -> arrayOf("The thought of lots of questing,", "Leaves some people unfulfilled,", "But I have done my simple best, in", "Entering the Champions Guild.")
            else -> arrayOf("${player.name.replaceFirstChar { it.uppercaseChar() }} is my name,", "I haven't much to say", "But since I have to sing this song.", "I'll just go ahead and play.")
        }

        override fun pulse(): Boolean {
            when (counter++) {
                0 -> {
                    player.lock()
                    animate(player, Animations.HOLD_LYRE_1318, true)
                }

                2 -> playLyreWithMusic(Animations.PLAY_LYRE_1320, 165)

                4, 6, 8, 10 -> {
                    val lineIndex = (counter - 4) / 2
                    playLyreWithMusic(Animations.PLAY_LYRE_1320, 164)
                    sendChat(player, lyrics[lineIndex])
                }

                14 -> {
                    sendMessage(player, "Your lyre is perfectly tuned.")
                    player.musicPlayer.play(MusicEntry.forId(163))
                    setAttribute(player, GameAttributes.QUEST_VIKING_OLAF_CONCERT, true)
                    removeAttribute(player, GameAttributes.QUEST_VIKING_LYRE)
                    if (removeItem(player, lyre)) addItem(player, Items.ENCHANTED_LYRE_3690)
                    player.unlock()
                }
            }
            return false
        }

        private fun playLyreWithMusic(animation: Int, musicId: Int) {
            animate(player, animation, true)
            player.musicPlayer.play(MusicEntry.forId(musicId))
        }
    }

    /**
     * Handles the spirit offering pulse to the Fossegrimen NPC in the Fremennik Trials.
     */
    private class SpiritPulse(private val player: Player, private val fish: Int) : Pulse() {

        private var counter = 0
        private val npc = NPC(NPCs.FOSSEGRIMEN_1273, player.location)

        private val seaBoots = intArrayOf(Items.FREMENNIK_SEA_BOOTS_1_14571, Items.FREMENNIK_SEA_BOOTS_2_14572, Items.FREMENNIK_SEA_BOOTS_3_14573)

        override fun pulse(): Boolean {
            when (counter++) {
                0 -> {
                    npc.init()
                    player.lock()
                    removeItem(player, fish)
                }
                1 -> npc.moveStep()
                2 -> {
                    npc.face(player)
                    player.face(npc)
                }
                3 -> sendNPCDialogue(player, npc.id, "I will kindly accept this offering, and bestow upon you a gift in return.", FaceAnim.HAPPY)
                4 -> if(!removeItem(player,Items.LYRE_3689)) { removeItem(player,Items.ENCHANTED_LYRE_3690) }
                5 -> handleLyreReward()
                6 -> player.unlock()
                10 -> {
                    npc.clear()
                    sendMessage(player, "Fossegrimen has enchanted your lyre so that you may play it.")
                    setAttribute(player, GameAttributes.QUEST_VIKING_LYRE, true)
                    return true
                }
            }
            return false
        }

        private fun handleLyreReward() {
            val hasBoots = player.equipment.containsAtLeastOneItem(seaBoots)
            val hasRing = player.equipment.containsItem(Item(Items.RING_OF_CHAROS_4202))

            val reward = when {
                hasRing && fish == Items.RAW_BASS_363 -> {
                    finishDiaryTask(player, DiaryType.FREMENNIK, 1, 4)
                    if (hasBoots) Items.ENCHANTED_LYRE3_6126 else Items.ENCHANTED_LYRE2_6125
                }

                hasBoots -> when (fish) {
                    Items.RAW_SHARK_383 -> Items.ENCHANTED_LYRE3_6126
                    Items.RAW_MANTA_RAY_389 -> Items.ENCHANTED_LYRE5_14590
                    Items.RAW_SEA_TURTLE_395 -> Items.ENCHANTED_LYRE6_14591
                    else -> null
                }

                else -> when (fish) {
                    Items.RAW_SHARK_383 -> Items.ENCHANTED_LYRE2_6125
                    Items.RAW_MANTA_RAY_389 -> Items.ENCHANTED_LYRE3_6126
                    Items.RAW_SEA_TURTLE_395 -> Items.ENCHANTED_LYRE4_6127
                    else -> null
                }
            }

            reward?.let { addItem(player, it) }
        }
    }

    /**
     * Represents a destination room with rectangular bounds.
     */
    private class DestRoom(val swx: Int, val swy: Int, val nex: Int, val ney: Int)

    /**
     * Calculates the center location of the destination room and slightly shifts it.
     *
     * @return A [Location] representing the center of this room.
     */
    private fun DestRoom.getCenter(): Location = Location((swx + nex) / 2, (swy + ney) / 2).transform(1, 0, 0)

    /**
     * Finds a random location from scenery objects in the player's current viewport,
     * specifically looking for object ID 5138.
     *
     * @param player The player to check viewport data for.
     * @return A random valid [Location] of the specified object.
     */
    fun getRandomLocation(player: Player?): Location{
        var obj: Scenery? = null

        while(obj?.id != Objects.PORTAL_5138) {
            val objects = player?.viewport?.chunks?.random()?.random()?.objects
            obj = objects?.random()?.random()
            if(obj == null || obj.location?.equals(Location(0,0,0))!!){
                continue
            }
        }
        return obj.location
    }

    /**
     * Checks whether the player has any equippable items in inventory or equipment.
     *
     * @param player The player whose items to check.
     * @return `true` if any equippable item is found, `false` otherwise.
     */
    private fun hasEquippableItems(player: Player?): Boolean {
        val container = arrayOf(player!!.inventory, player.equipment)
        for (c in container) {
            for (i in c.toArray()) {
                if (i == null) {
                    continue
                }
                if (i.name.lowercase().contains(" rune")) {
                    return true
                }
                var slot: Int = i.definition.getConfiguration(ItemConfigParser.EQUIP_SLOT, -1)
                if (slot == -1 && i.definition.getConfiguration(ItemConfigParser.WEAPON_INTERFACE, -1) != -1) {
                    slot = SLOT_WEAPON
                }
                if (slot >= 0) {
                    return true
                }
            }
        }
        return false
    }
}