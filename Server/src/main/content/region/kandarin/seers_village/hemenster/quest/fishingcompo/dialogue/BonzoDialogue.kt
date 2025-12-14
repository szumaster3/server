package content.region.kandarin.seers_village.hemenster.quest.fishingcompo.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Bonzo dialogue.
 *
 * # Relations
 * - [Fishing Contest][content.region.kandarin.seers_village.hemenster.quest.fishingcompo.FishingContest]
 */
@Initializable
class BonzoDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val init = args.size < 2
        val questStage = getQuestStage(player, Quests.FISHING_CONTEST)
        val hasFishingTrophy = hasAnItem(player, Items.FISHING_TROPHY_26).container != null
        val duringContest = getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, false)
        val hasRod = inInventory(player, Items.FISHING_ROD_307)
        val gender = if (player.isMale) "lad" else "lass"

        if (init) {
            if (hasRod && questStage < 20) {
                npc("Roll up, roll up! Enter the great Hemenster", "Fishing Contest! Only 5gp entrance fee!")
            }
            else if(!hasRod){
                npc("Sorry, $gender, but you need a fishing", "rod to compete.")
                stage = END_DIALOGUE
            } else {
                if(!hasFishingTrophy && getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_WON, false) && !isQuestComplete(player, Quests.FISHING_CONTEST)) {
                    npc(FaceAnim.HAPPY, "Hello champ!")
                    stage = 11
                } else {
                    npc(FaceAnim.HAPPY, "Hello champ! So any hints on how to fish?")
                    stage = 10
                }
            }
            return true
        }

        if (questStage in 20..99 && !hasFishingTrophy) {
            npc(FaceAnim.HAPPY, "Hello champ!")
            stage = 11
            return true
        }

        if (questStage in 20..99 && duringContest) {
            npc(FaceAnim.NEUTRAL,"You've already paid, you don't need to pay me again!")
            stage = END_DIALOGUE
            return true
        }

        if (duringContest && inInventory(player, Items.RAW_GIANT_CARP_338)) {
            npc(FaceAnim.HALF_ASKING,"So how are you doing so far?")
            stage = 15
            return true
        }

        npc(FaceAnim.HAPPY, "Hello champ! So any hints on how to fish?")
        stage = 10
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> options("I'll enter the competition please.", "No thanks, I'll just watch the fun.").also { stage++ }
            1 -> when (buttonId) {
                1 -> player(FaceAnim.FRIENDLY, "I'll enter the competition please.").also { stage++ }
                2 -> player(FaceAnim.NEUTRAL, "No thanks, I'll just watch the fun.").also { stage = END_DIALOGUE }
            }
            2 -> npc(FaceAnim.HAPPY, "Marvelous!").also { stage++ }
            3 -> if (!removeItem(player, Item(Items.COINS_995, 5), Container.INVENTORY)) {
                player(FaceAnim.HALF_GUILTY, "I don't have the 5gp though...")
                stage++
            } else {
                sendItemDialogue(player, Items.COINS_6964, "You pay Bonzo 5 coins.").also { stage = 5 }
            }
            4 -> npc(FaceAnim.NEUTRAL, "No pay, no play.").also { stage = END_DIALOGUE }
            5 -> npc(FaceAnim.HAPPY, "Ok, we've got all the fishermen!").also { stage++ }
            6 -> npc(FaceAnim.HAPPY, "It's time to roll!").also { stage++ }
            7 -> npcl(FaceAnim.NOD_YES, "Ok, nearly everyone is in their place already. You fish in the spot by the willow tree, and the Sinister Stranger, you fish by the pipes.").also { stage++ }
            8 -> sendDialogue(player, "Your fishing competition spot is by the willow tree.").also { stage++ }
            9 -> {
                close()
                setAttribute(player, GameAttributes.RE_PAUSE, true)
                setAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_CONTEST, true)
                lock(player, 3)
                fishingContestPulse(player)
            }
            10 -> player(FaceAnim.FRIENDLY, "I think I'll keep them to myself.").also { stage = END_DIALOGUE }
            11 -> player(FaceAnim.HALF_GUILTY, "I don't feel like a champ...").also { stage++ }
            12 -> npc(FaceAnim.THINKING, "Why not champ?").also { stage++ }
            13 -> player(FaceAnim.SAD, "I lost my fishing trophy...").also { stage++ }
            14 -> {
                end()
                npc(FaceAnim.FRIENDLY, "Is that all chump? Don't worry, I have a spare!")
                addItem(player, Items.FISHING_TROPHY_26)
                stage = END_DIALOGUE
            }
            15 -> showTopics(
                Topic("I have this big fish. Is it enough to win?", 16, true),
                Topic("I think I might be able to find a bigger fish.", END_DIALOGUE)
            )
            16 -> player(FaceAnim.HAPPY, "I have the big fish.").also { stage++ }
            17 -> player(FaceAnim.FRIENDLY, "Is it enough to win?").also { stage++ }
            18 -> npc(FaceAnim.FRIENDLY, "Well, we'll just wait untill time is up.").also { stage++ }
            19 -> sendDialogue(player, "You wait").also { stage = END_DIALOGUE }
        }
        return true
    }

    /**
     * Handles fishing contest competition.
     */
    private fun fishingContestPulse(player: Player) {
        val region: DynamicRegion = DynamicRegion.create(10549)
        val base = region.baseLocation

        val originalLocation = player.location
        registerLogoutListener(player, "during-fishing-contest") { p ->
            p.location = originalLocation
        }

        val playerLocation = base.transform(17, 46, 0)
        player.teleport(playerLocation)

        val bonzo    = NPC.create(NPCs.BONZO_225, base.transform(17, 45, 0))
        val sinister = NPC.create(NPCs.SINISTER_STRANGER_3677, base.transform(15, 50, 0))
        val bigDave  = NPC.create(NPCs.BIG_DAVE_228, base.transform(10, 39, 0))
        val morris   = NPC.create(NPCs.MORRIS_227, base.transform(19, 48, 0))
        val joshua   = NPC.create(NPCs.JOSHUA_229, base.transform(4, 23, 0))

        val firstFishingSpot  = NPC.create(NPCs.FISHING_SPOT_309, base.transform(4, 23, 0))
        val secondFishingSpot = NPC.create(NPCs.FISHING_SPOT_309, base.transform(13, 52, 0))
        val thirdFishingSpot  = NPC.create(NPCs.FISHING_SPOT_309, base.transform(6, 43, 0))

        listOf(
            bonzo, sinister, bigDave, morris, joshua,
            firstFishingSpot, secondFishingSpot, thirdFishingSpot
        ).forEach { it.init() }

        listOf(
            bonzo, sinister, bigDave, morris, joshua,
            firstFishingSpot, secondFishingSpot, thirdFishingSpot
        ).forEach { region.add(it) }

        Pulser.submit(object : Pulse(1, player) {
            var counter = 0
            var newSpot: NPC? = null

            override fun pulse(): Boolean {
                val fishingSpotNPC = firstFishingSpot
                if (!fishingSpotNPC.isActive) {
                    clean(player, originalLocation, region)
                    sendMessage(player, "Unable to complete action - system busy.")
                    return true
                }

                when (counter++) {
                    0 -> {
                        fishingSpotNPC.transform(NPCs.FISHING_SPOT_234)
                    }

                    7 -> {
                        if (getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                            sendNPCDialogue(
                                player, NPCs.SINISTER_STRANGER_3677,
                                "Arrgh! WHAT is THAT GHASTLY smell??? I think I will move over here instead...",
                                FaceAnim.DISGUSTED
                            )
                        }
                    }

                    10 -> {
                        if (getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                            npc("Hmm. You'd better go and take the area by the pipes", "then.")
                            newSpot = listOf(firstFishingSpot, secondFishingSpot, thirdFishingSpot)
                                .firstOrNull { it.id == NPCs.FISHING_SPOT_309 || it.id == NPCs.FISHING_SPOT_233 }
                            newSpot?.transform(NPCs.FISHING_SPOT_233)
                            fishingSpotNPC.reTransform()
                            registerLogoutListener(player, "fishing-contest:fishing_spot:2") {
                                newSpot?.reTransform()
                            }
                        }
                    }

                    13 -> {
                        if (getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_STASH_GARLIC, false)) {
                            sendDialogue(player, "Your fishing competition spot is now beside the pipes.")
                        }
                    }

                    46 -> {
                        face(player, bonzo)
                        npc("Ok folks, time's up!", "Let's see who caught the biggest fish!")
                    }

                    49 -> {
                        lock(player, 16)
                        player.walkingQueue.reset()
                        val loc = base.transform(15,45,0)
                        player.walkingQueue.addPath(loc.x, loc.y)
                    }

                    57 -> {
                        val sardines = amountInInventory(player, Items.RAW_SARDINE_327)
                        val carps = amountInInventory(player, Items.RAW_GIANT_CARP_338)

                        val hasSardines = sardines > 0
                        val hasCarps = carps > 0

                        if (hasSardines || hasCarps) {
                            if (hasSardines) removeAll(player, Item(Items.RAW_SARDINE_327, sardines), Container.INVENTORY)
                            if (hasCarps) {
                                removeAll(player, Item(Items.RAW_GIANT_CARP_338, carps), Container.INVENTORY)
                                setAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_WON, true)
                            }
                            face(player, bonzo)
                            animate(player, Animations.HUMAN_WITHDRAW_833, true)
                            sendDialogue(player, "You hand over your catch.")
                        } else {
                            sendDialogue(player, "You haven't caught anything worth handing in.")
                        }
                    }

                    60 -> npc(FaceAnim.HAPPY, "We have a new winner!")

                    62 -> {
                        if (!getAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_WON, false)) {
                            npc(FaceAnim.HAPPY, "And our winner is... the stranger who", "was fishing over by the pipes!")
                            return true
                        } else {
                            npc(FaceAnim.HAPPY, "The heroic-looking person who was fishing by the pipes", "has caught the biggest carp I've seen since Grandpa", "Jack used to compete!")
                        }
                    }

                    64 -> {
                        clean(player, originalLocation, region)
                        sendItemDialogue(player, Items.FISHING_TROPHY_26, "You are given the Hemenster fishing trophy!")
                        addItemOrDrop(player, Items.FISHING_TROPHY_26, 1)
                        setQuestStage(player, Quests.FISHING_CONTEST, 20)
                        return true
                    }
                }

                return false
            }
        })
    }

    /**
     * Cleans up after fishing contest.
     */
    private fun clean(player: Player, originalLocation : Location, region : DynamicRegion) {
        player.teleport(originalLocation)
        region.flagInactive(true)
        clearLogoutListener(player, "during-fishing-contest")
        removeAttribute(player, GameAttributes.QUEST_FISHINGCOMPO_CONTEST)
        removeAttribute(player, GameAttributes.RE_PAUSE)
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.BONZO_225)
}
