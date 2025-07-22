package content.region.kandarin.yanille.quest.itwatchtower.dialogue

import content.data.GameAttributes
import core.api.*
import core.api.getQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.global.action.DoorActionHandler
import core.game.interaction.QueueStrength
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.*

/**
 * Represents the Ogre Guards dialogues.
 *
 * # Relations
 * - [Watchtower Quest][content.region.kandarin.yanille.quest.itwatchtower.Watchtower]
 */
@Initializable
class OgreCityGuardDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val stage = getQuestStage(player, Quests.WATCHTOWER)

        if (stage in 10..100) {
            end()
            openDialogue(
                player,
                when (npc.id) {
                    NPCs.OGRE_GUARD_859 -> OgreGuardNorthWestGateDialogue()
                    else -> OgreGuardSouthEastGateDialogue()
                }
            )
        } else {
            npc(FaceAnim.OLD_DEFAULT, "Stop, creature! Only ogres and their friends allowed in", "this city. Show me a sign of companionship, like a lost", "relic or somefing, and you may pass.")
            sendMessage(player, "The guard pushes you back down the hill.")
            handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false)
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = OgreCityGuardDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.OGRE_GUARD_859, NPCs.OGRE_GUARD_858)
}

/**
 * Represents the Ogre Guards near battlement dialogue (extension).
 */
class BattlementDialogue : DialogueFile() {

    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OGRE_GUARD_860)
        when(stage) {
            0 -> {
                face(player!!, npc!!)
                npc(FaceAnim.OLD_DEFAULT,"Oi! Where do you think you are going? You are for", "the cooking pot!").also { stage++ }
            }
            1 -> if(!getAttribute(player!!, GameAttributes.WATCHTOWER_ROCK_CAKE, false)) {
                showTopics(
                    Topic("But I am a friend to ogres...", 2),
                    Topic("Not if I can help it.", 5)
                )
            } else {
                npcl(FaceAnim.OLD_DEFAULT, "Stop, creature! Oh it's you. Well, what have you got for us, then?").also { stage = 13 }
            }
            2 -> npc(FaceAnim.OLD_DEFAULT, "Prove it to us with a gift. Get us something from the", "market.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "Like what?").also { stage++ }
            4 -> npc(FaceAnim.OLD_DEFAULT, "Surprise us...").also {
                setAttribute(player!!, GameAttributes.WATCHTOWER_ROCK_CAKE, true)
                stage = END_DIALOGUE
            }
            5 -> npc(FaceAnim.OLD_DEFAULT, "You can help by being tonight's dinner, or", "you can go away.").also { stage++ }
            6 -> npc(FaceAnim.OLD_NEUTRAL, "Now, which shall it be?").also { stage++ }
            7 -> showTopics(
                Topic("Okay, okay, I'm going.", 8),
                Topic("I tire of ogres, prepare to die!", 12)
            )
            8 -> npc(FaceAnim.OLD_DEFAULT, "Back to whence you came!").also { stage++ }
            9 -> {
                end()
                sendMessage(player!!, "The guard pushes you out of the city.")
                handleGatePassage(player!!, Location.create(2546, 3065), openGate = false)
            }
            10 -> npc(FaceAnim.OLD_DEFAULT, "Well, well, look at this. My favourite: rock cake! Okay,", "we will let it through.").also { stage++ }
            11 -> {
                endFile()
                val destination = player!!.location.transform(2,0,0)
                forceMove(player!!, player!!.location, destination, 0, 90, null, Animations.CLIMB_OVER_THING_5038)
                sendMessage(player!!, "You climb over the low wall.")
            }
            12 -> {
                end()
                npc(FaceAnim.OLD_DEFAULT, "Grrrrr!")
                animate(npc!!, 359)
                impact(player!!, 3, ImpactHandler.HitsplatType.NORMAL)
            }
            13 -> {
                if(!removeItem(player!!, Items.ROCK_CAKE_2379)) {
                    player(FaceAnim.HALF_GUILTY, "I didn't bring anything.").also { stage++ }
                } else {
                    player(FaceAnim.HALF_ASKING, "How about this?").also { stage = 10 }
                }
            }
            14 -> npc(FaceAnim.OLD_DEFAULT, "Didn't bring anything? In that case shove off!").also { stage = 9 }
        }
    }
}

/**
 * Represents the Ogre guard dialogue (North-western gates).
 */
class OgreGuardNorthWestGateDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OGRE_GUARD_859)
        when(stage) {
            0 -> npc(FaceAnim.OLD_DEFAULT, "Well, what proof of friendship did you bring?").also { stage++ }
            1 -> if(inInventory(player!!, Items.OGRE_RELIC_2372)) {
                player(FaceAnim.NEUTRAL, "I have a relic from a chieftain.").also { stage++ }
            } else {
                player(FaceAnim.HALF_GUILTY, "I don't have anything.").also { stage = 4 }
            }

            2 -> npc(FaceAnim.OLD_DEFAULT,"It's got the statue of Dalgroth. Welcome to Gu'Tanoth,", "friend of the ogres.").also { stage++ }
            3 -> {
                end()
                handleGatePassage(player!!, Location.create(2503, 3062, 0), openGate = true)
                setAttribute(player!!, GameAttributes.WATCHTOWER_GATE_UNLOCK, true)
                stage = END_DIALOGUE
            }
            4 -> npc(FaceAnim.OLD_DEFAULT,"Why have you returned with no proof of companionship?", "Back to whence you came!").also { stage++ }
            5 -> {
                end()
                sendMessage(player!!, "The guard pushes you back down the hill.")
                handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false)
                stage = END_DIALOGUE
            }
        }
    }
}

/**
 * Represents the Ogre guard dialogue (South-eastern gates).
 */
class OgreGuardSouthEastGateDialogue : DialogueFile() {
    //https://classic.runescape.wiki/w/Transcript:Ogre_guard#Ogre_guarding_the_south-eastern_gate
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.OGRE_GUARD_858)
        when(stage) {
            0 -> {
                if (getAttribute(player!!, GameAttributes.WATCHTOWER_GOLD_GATE_UNLOCK, false)) {
                    npc(FaceAnim.OLD_DEFAULT, "I know you creature, you may pass")
                    stage = END_DIALOGUE
                    return
                }

                if (getAttribute(player!!, GameAttributes.WATCHTOWER_BRING_GOLD, false)) {
                    npc(FaceAnim.OLD_DEFAULT, "Creature, did you bring me the gold?").also { stage = 6 }
                } else {
                    npc(FaceAnim.OLD_DEFAULT, "Halt!").also { stage++ }
                }
            }
            1 -> npc(FaceAnim.OLD_DEFAULT,"You cannot pass here.").also { stage++ }
            2 -> player(FaceAnim.HALF_WORRIED, "I am a friend to ogres.").also { stage++ }
            3 -> npc(FaceAnim.OLD_DEFAULT,"You will be my friend only with gold.").also { stage++ }
            4 -> npcl(FaceAnim.OLD_DEFAULT, "Bring me a bar of pure gold and i will let you pass.").also { stage++ }
            5 -> {
                end()
                npc(FaceAnim.OLD_ANGRY1,"For now - begone!")
                sendMessage(player!!, "The guard pushes you outside the city.")
                handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false)
                setAttribute(player!!, GameAttributes.WATCHTOWER_BRING_GOLD, true)
            }
            6 -> if(removeItem(player!!, Items.GOLD_BAR_2357)) {
                player("Here it is!").also { stage++ }
            } else {
                player("No I don't have it.").also { stage = 8 }
            }
            7 -> npc(FaceAnim.OLD_NEUTRAL, "It's brought it! On your way.").also {
                setAttribute(player!!, GameAttributes.WATCHTOWER_GOLD_GATE_UNLOCK, true)
                stage = END_DIALOGUE
            }
            8 -> npc(FaceAnim.OLD_DEFAULT, "No gold, no passage!").also { stage++ }
            9 -> npc(FaceAnim.OLD_ANGRY1, "Get out of this city!").also {
                handleGatePassage(player!!, Location.create(2546, 3065, 0), openGate = false)
                stage = END_DIALOGUE
            }
        }

    }
}

/**
 * Handles gate passage.
 *
 * @param openGate boolean.
 */
private fun handleGatePassage(player: Player, loc: Location, openGate: Boolean) {
    teleport(player, loc, TeleportManager.TeleportType.INSTANT)

    if (openGate) {
        val scenery = getScenery(2504, 3063, 0)
        scenery?.asScenery()?.let { DoorActionHandler.handleAutowalkDoor(player, it) }
    } else {
        lock(player, 9)
        openInterface(player, Components.FADE_TO_BLACK_115)
        val max = player.skills.maximumLifepoints
        val damage = (max * 0.05).toInt().coerceAtLeast(1)
        queueScript(player, 1, QueueStrength.SOFT) { stage : Int ->
            when(stage) {
                0 -> {
                    openInterface(player, Components.FADE_FROM_BLACK_170)
                    return@queueScript delayScript(player, 6)
                }
                1 -> {
                    impact(player, damage, ImpactHandler.HitsplatType.NORMAL)
                    sendGraphics(Graphics.STUN_BIRDIES_ABOVE_HEAD_80, player.location)
                    return@queueScript delayScript(player, 3)
                }
                2 -> {
                    sendChat(player, "Urrrrrgh!")
                    return@queueScript stopExecuting(player)
                }
                else ->  return@queueScript stopExecuting(player)
            }
        }
    }
}