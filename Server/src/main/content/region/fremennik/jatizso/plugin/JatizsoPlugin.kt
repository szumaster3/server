package content.region.fremennik.jatizso

import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Scenery
import shared.consts.Sounds

class JatizsoPlugin : InteractionListener {

    companion object {
        val GUARDS_CHAT = arrayOf(
            arrayOf("YOU WOULDN'T KNOW HOW TO FIGHT A TROLL IF IT CAME UP AND BIT YER ARM OFF", "YAK LOVERS", "CALL THAT A TOWN? I'VE SEEN BETTER HAMLETS!", "AND YOUR FATHER SMELLED OF WINTERBERRIES!", "WOODEN BRIDGES ARE RUBBISH!", "OUR KING'S BETTER THAN YOUR BURGHER!"),
            arrayOf("YOU WOULDN'T KNOW HOW TO SHAVE A YAK IF YOUR LIFE DEPENDED ON IT", "MISERABLE LOSERS", "YOUR MOTHER WAS A HAMSTER!", "AT LEAST WE HAVE SOMETHING OTHER THAN SMELLY FISH!", "AT LEAST WE CAN COOK!")
        )
        val GATES_CLOSED = intArrayOf(Scenery.GATE_21403, Scenery.GATE_21405)
        val TOWER_GUARDS = intArrayOf(NPCs.GUARD_5490, NPCs.GUARD_5489)
        val GUARDS       = intArrayOf(NPCs.GUARD_5491, NPCs.GUARD_5492)
        val KING_CHEST   = intArrayOf(Scenery.CHEST_21299, Scenery.CHEST_21300)
    }

    override fun defineListeners() {
        on(GATES_CLOSED, IntType.SCENERY, "open") { player, node ->
            val scenery = node.asScenery()
            DoorActionHandler.getSecondDoor(scenery)?.let { second ->
                DoorActionHandler.open(scenery, second, scenery.id + 1, second.id + 1, true, 500, false)
            }
            return@on true
        }

        on(NPCs.SLUG_HEMLIGSSEN_5520, IntType.NPC, "talk-to") { player, node ->
            sendNPCDialogue(player, node.id, "Shhh. Go away. I'm not allowed to talk to you.")
            runTask(player,3){
                sendPlayerDialogue(player, "Fine, whatever.")
            }
            return@on true
        }

        on(Scenery.BELL_21394, IntType.SCENERY, "ring-bell") { player, _ ->
            playAudio(player, Sounds.STEEL_15)
            sendMessage(player, "You ring the warning bell, but everyone ignores it!")
            return@on true
        }

        on(KING_CHEST, IntType.SCENERY, "open") { player, _ ->
            sendPlayerDialogue(player, "I probably shouldn't mess with that.", FaceAnim.HALF_THINKING)
            return@on true
        }

        setDest(IntType.NPC, NPCs.MAGNUS_GRAM_5488) { _, _ ->
            return@setDest Location.create(2416, 3801, 0)
        }

        on(GUARDS, IntType.NPC, "talk-to") { player, node ->
            openDialogue(player, JatizsoGuardDialogue() , node.id)
            return@on true
        }

        on(TOWER_GUARDS, IntType.NPC, "watch-shouting") { player, _ ->
            val local = findLocalNPC(player, NPCs.GUARD_5489)
            lock(player, 200)
            face(local!!, Location.create(2371, 3801, 2))
            local.asNpc().isRespawn = false
            submitIndividualPulse(
                player,
                object : Pulse(4) {
                    var id = NPCs.GUARD_5489
                    var counter = 0
                    val other = findLocalNPC(player, getOther(NPCs.GUARD_5489))

                    override fun start() {
                        other?.isRespawn = false
                    }

                    override fun pulse(): Boolean {
                        val npc = findLocalNPC(player, id) ?: return false
                        val index = when (id) {
                            NPCs.GUARD_5489 -> 0
                            else -> 1
                        }
                        if (index == 1 && counter == 5) return true
                        sendChat(npc, GUARDS_CHAT[index][counter])
                        if (npc.id != NPCs.GUARD_5489) counter++
                        id = getOther(id)
                        return false
                    }

                    override fun stop() {
                        unlock(player)
                        other?.isRespawn = true
                        local.asNpc().isRespawn = true
                        super.stop()
                    }

                    fun getOther(npc: Int): Int = when (npc) {
                        NPCs.GUARD_5489 -> NPCs.GUARD_5490
                        NPCs.GUARD_5490 -> NPCs.GUARD_5489
                        else -> NPCs.GUARD_5489
                    }
                },
            )
            return@on true
        }
    }

    inner class JatizsoGuardDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0  -> sendNPCDialogue(player!!, NPCs.GUARD_5491, "Are you all right? Leftie?").also { stage++ }
                1  -> sendNPCDialogue(player!!, NPCs.GUARD_5492, "No, I'm on the left.").also { stage++ }
                2  -> sendNPCDialogue(player!!, NPCs.GUARD_5491, "Only from your perspective. Someone entering the gate should call you Rightie, right Leftie?").also { stage++ }
                3  -> sendNPCDialogue(player!!, NPCs.GUARD_5492, "Right, Rightie. So you'd be Leftie not Rightie, right?").also { stage++ }
                4  -> sendNPCDialogue(player!!, NPCs.GUARD_5491, "That's right Leftie, that's right.").also { stage++ }
                5  -> sendNPCDialogue(player!!, NPCs.GUARD_5492, "Rightie-oh Rightie, or should I call you Leftie?").also { stage++ }
                6  -> sendNPCDialogue(player!!, NPCs.GUARD_5491, "No, Rightie's fine Leftie.").also { stage++ }
                7  -> playerl(FaceAnim.ANGRY, "Aaagh! Enough! If either of you mention left or right in my presence I'll have to scream! Can I come through the gate?").also { stage++ }
                8  -> sendNPCDialogue(player!!, NPCs.GUARD_5492, "Don't let us stop you.").also { stage++ }
                9  -> sendNPCDialogue(player!!, NPCs.GUARD_5491, "Yes, head right on in, sir.").also { stage++ }
                10 -> playerl(FaceAnim.ANGRY, "You said it! You said it! ARRRRRRRRGH!").also { stage++ }
                11 -> end()
            }
        }
    }
}
