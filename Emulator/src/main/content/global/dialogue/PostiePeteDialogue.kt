package content.global.dialogue

import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.config.NPCConfigParser
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class PostiePeteDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_NORMAL, "Anyone got Post?")
        sendChat(npc, "Anyone got Post?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (player!!.viewport.region.id) {
            11679 -> handleLaughingMiner()
            14647 -> handlePortPhasmatys()
            12854 -> handleVarrockLibrary()
            12084 -> handleFaladorPartyRoom()
            12086 -> handleIceMountain()
            12089 -> handleWildernessBanditCamp()
            10291 -> handleArdougneZoo()
            9776 -> handleCastleWars()
        }
        return true
    }

    private fun handleLaughingMiner() {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "4 pints of your finest please.").also {
                    sendChat(npc, "4 pints of your finest please.")
                    stage++
                }

            1 ->
                sendNPCDialogue(
                    player,
                    NPCs.BARMAID_2178,
                    "4 pints coming right up. Are you expecting guests?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    findLocalNPC(player, NPCs.BARMAID_2178)?.let {
                        sendChat(it, "4 pints coming right up. Are you expecting guests?")
                    }
                    stage++
                }

            2 ->
                npcl(FaceAnim.OLD_NORMAL, "Just a few old friends.").also {
                    sendChat(npc, "Just a few old friends.")
                    stage++
                }

            3 ->
                sendNPCDialogue(
                    player,
                    NPCs.BARMAID_2178,
                    "You want me to put it on your tab?",
                    FaceAnim.OLD_NORMAL,
                ).also {
                    findLocalNPC(player, NPCs.BARMAID_2178)?.let { sendChat(it, "You want me to put it on your tab?") }
                    stage++
                }

            4 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "That would be great, thanks. Oh, cripes! I've left a fire burning! I've got to go!",
                ).also {
                    sendChat(npc, "That would be great, thanks. Oh, cripes! I've left a fire burning! I've got to go!")
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handlePortPhasmatys() {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "Hey Pete. Any news from the fleet?").also {
                    sendChat(npc, "Hey Pete. Any news from the fleet?")
                    stage++
                }

            1 ->
                sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "No yo ho bruv.").also {
                    findLocalNPC(player, NPCs.PIRATE_PETE_2825)?.let { sendChat(it, "No yo ho bruv.") }
                    stage++
                }

            2 ->
                sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "Tis all quiet on the eastern front.").also {
                    findLocalNPC(
                        player,
                        NPCs.PIRATE_PETE_2825,
                    )?.let { sendChat(it, "Tis all quiet on the eastern front.") }
                    stage++
                }

            3 ->
                npcl(FaceAnim.OLD_NORMAL, "Good good.").also {
                    sendChat(npc, "Good good.")
                    stage++
                }

            4 ->
                npcl(FaceAnim.OLD_NORMAL, "Well you know what to do if something goes wrong.").also {
                    sendChat(npc, "Well you know what to do if something goes wrong.")
                    stage++
                }

            5 ->
                sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "Aye, that I do, bruv.").also {
                    findLocalNPC(player, NPCs.PIRATE_PETE_2825)?.let { sendChat(it, "Aye, that I do, bruv.") }
                    stage++
                }

            6 ->
                sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "Holler like a bosun without a bottle.").also {
                    findLocalNPC(player, NPCs.PIRATE_PETE_2825)?.let {
                        sendChat(it, "Holler like a bosun without a bottle.")
                    }
                    stage++
                }

            7 ->
                npcl(FaceAnim.OLD_NORMAL, "That's it brother, and we'll come a running!").also {
                    sendChat(npc, "That's it brother, and we'll come a running!")
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleVarrockLibrary() {
        when (stage) {
            0 -> player(FaceAnim.LAUGH, "HEY PETE, WHAT ARE YOU DOING HERE?")
            1 -> npc(FaceAnim.OLD_NORMAL, "Shhhh! It's a library you know!")
            2 -> player(FaceAnim.HAPPY, "Oh, sorry! So, what's new?")
            3 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Not much, just doing some research on Dwarvern",
                    "Culture. Did you know that Keldagrim was originally",
                    "constructed by a long lost clan who embraced the power",
                    "of magic, and used it to aid with glorious buildings like",
                )
            4 -> npc(FaceAnim.OLD_NORMAL, "the royal palace?")
            5 -> player(FaceAnim.HAPPY, "Really??")
            6 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Yes. They even stayed down there for 1000 years or",
                    "more, in a time they call 'The age of Kings'.",
                )
            7 -> player(FaceAnim.HAPPY, "WOW!")
            8 ->
                npc(FaceAnim.OLD_NORMAL, "Shhhh! That's enough history for one day I think!").also {
                    stage =
                        END_DIALOGUE
                }
            9 ->
                GameWorld.Pulser.submit(
                    object : Pulse(1, npc) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                0 -> forceWalk(npc, Location.create(3210, 3490, 0), "smart")
                                3 -> {
                                    poofClear(npc)
                                    npc.respawnTick =
                                        GameWorld.ticks +
                                        npc.definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 60)
                                }
                            }
                            return true
                        }
                    },
                )
        }
    }

    private fun handleFaladorPartyRoom() {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "Hey Bro, how's the party business?").also {
                    sendChat(npc, "Hey Bro, how's the party business?")
                    stage++
                }

            1 ->
                sendNPCDialogue(player, NPCs.PARTY_PETE_659, "Great! Thanks!").also {
                    findLocalNPC(player, NPCs.PARTY_PETE_659)?.let { sendChat(it, "Great! Thanks!") }
                    stage++
                }

            2 ->
                sendNPCDialogue(player, NPCs.PARTY_PETE_659, "Celebrating weddings and drop parties...").also {
                    findLocalNPC(player, NPCs.PARTY_PETE_659)?.let {
                        sendChat(it, "Celebrating weddings and drop parties...")
                    }
                    stage++
                }

            3 ->
                sendNPCDialogue(player, NPCs.PARTY_PETE_659, "...and getting paid for it!").also {
                    findLocalNPC(player, NPCs.PARTY_PETE_659)?.let { sendChat(it, "...and getting paid for it!") }
                    stage++
                }

            4 ->
                npcl(FaceAnim.OLD_NORMAL, "Nice. Well I'm here to talk to some white knights.").also {
                    sendChat(npc, "Nice. Well I'm here to talk to some white knights.")
                    stage++
                }

            5 ->
                sendNPCDialogue(player, NPCs.PARTY_PETE_659, "Want to take some cake with you?").also {
                    findLocalNPC(player, NPCs.PARTY_PETE_659)?.let { sendChat(it, "Want to take some cake with you?") }
                    stage++
                }

            6 ->
                npcl(FaceAnim.OLD_NORMAL, "No thanks, I'm still dieting.").also {
                    sendChat(npc, "No thanks, I'm still dieting.")
                    stage++
                }

            7 ->
                npcl(FaceAnim.OLD_NORMAL, "Just seafood for me.").also {
                    sendChat(npc, "Just seafood for me.")
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleIceMountain() {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "Well I'm back. And he said no. Again.").also {
                    sendChat(npc, "Well I'm back. And he said no. Again.")
                    stage++
                }

            1 ->
                sendNPCDialogue(player, NPCs.ORACLE_746, "Lemons? Both of them?").also {
                    findLocalNPC(player, NPCs.ORACLE_746)?.let { sendChat(it, "Lemons? Both of them?") }
                    stage++
                }

            2 ->
                npcl(FaceAnim.OLD_NORMAL, "Lemons? What are you talking about?").also {
                    sendChat(npc, "Lemons? What are you talking about?")
                    stage++
                }

            3 ->
                sendNPCDialogue(player, NPCs.ORACLE_746, "Fragile! Do not bend!").also {
                    findLocalNPC(player, NPCs.ORACLE_746)?.let { sendChat(it, "Fragile! Do not bend!") }
                    stage++
                }

            4 ->
                npcl(FaceAnim.OLD_NORMAL, "I swear you get stranger every time.").also {
                    sendChat(npc, "I swear you get stranger every time.")
                    stage++
                }

            5 ->
                npcl(FaceAnim.OLD_NORMAL, "Ok, I'll ask again.").also {
                    sendChat(npc, "Ok, I'll ask again.")
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleWildernessBanditCamp() {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "Psssst! How is the plan going?").also {
                    sendChat(npc, "Psssst! How is the plan going?")
                    stage++
                }

            1 ->
                sendNPCDialogue(player, NPCs.NOTERAZZO_597, "Perfectly. Everyone is prepared.").also {
                    findLocalNPC(player, NPCs.NOTERAZZO_597)?.let { sendChat(it, "Perfectly. Everyone is prepared.") }
                    stage++
                }

            2 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Great. Now don't forget- nobody does anything until I give the signal.",
                ).also {
                    sendChat(npc, "Great. Now don't forget- nobody does anything until I give the signal.")
                    stage++
                }

            3 ->
                sendNPCDialogue(player, NPCs.NOTERAZZO_597, "Sure no problem. We'll be waiting.").also {
                    findLocalNPC(player, NPCs.NOTERAZZO_597)?.let { sendChat(it, "Sure no problem. We'll be waiting.") }
                    stage++
                }

            4 ->
                npcl(FaceAnim.OLD_NORMAL, "Till next time. Farewell.").also {
                    sendChat(npc, "Till next time. Farewell.")
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleArdougneZoo() {
        when (stage) {
            0 ->
                npcl(FaceAnim.OLD_NORMAL, "Hi Pete!").also {
                    sendChat(npc, "Hi Pete!")
                    stage++
                }

            1 ->
                npcl(FaceAnim.OLD_NORMAL, "Mum wants to know if you're coming round for tea.").also {
                    sendChat(npc, "Mum wants to know if you're coming round for tea.")
                    stage++
                }

            2 ->
                sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "No. I've got another late night here.").also {
                    findLocalNPC(player, NPCs.PARROTY_PETE_1216)?.let {
                        sendChat(
                            it,
                            "No. I've got another late night here.",
                        )
                    }
                    stage++
                }

            3 ->
                sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "Ol' blue beak's got the flu again!").also {
                    findLocalNPC(
                        player,
                        NPCs.PARROTY_PETE_1216,
                    )?.let { sendChat(it, "Ol' blue beak's got the flu again!") }
                    stage++
                }

            4 ->
                sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "Oh dear. Nothing too serious I hope.").also {
                    findLocalNPC(player, NPCs.PARROTY_PETE_1216)?.let {
                        sendChat(
                            it,
                            "Oh dear. Nothing too serious I hope.",
                        )
                    }
                    stage++
                }

            5 ->
                sendNPCDialogue(
                    player,
                    NPCs.PARROTY_PETE_1216,
                    "Well as long as he hasn't caught anything from that evil chicken.",
                ).also {
                    findLocalNPC(player, NPCs.PARROTY_PETE_1216)?.let {
                        sendChat(
                            it,
                            "Well as long as he hasn't caught anything from that evil chicken.",
                        )
                    }
                    stage++
                }

            6 ->
                npcl(FaceAnim.OLD_NORMAL, "Got a taste for parrots now too does he?").also {
                    sendChat(npc, "Got a taste for parrots now too does he?")
                    stage++
                }

            7 ->
                npcl(FaceAnim.OLD_NORMAL, "*Sigh* Will nothing sacred be safe?").also {
                    sendChat(npc, "*Sigh* Will nothing sacred be safe?")
                    stage = END_DIALOGUE
                }
        }
    }

    private fun handleCastleWars() {
        when (stage) {
            0 -> player("Hey Pete, what you doing here?").also { stage++ }
            1 ->
                npc(
                    FaceAnim.OLD_NORMAL,
                    "Just doing a quick survey on famous people and their",
                    "favourite colour. It's a special feature for 'West",
                    "Wyverns Women's Weekly'.",
                ).also {
                    stage++
                }
            2 -> player("Can I get a copy?").also { stage++ }
            3 -> npc(FaceAnim.OLD_NORMAL, "Can you speak Wyvern?").also { stage++ }
            4 -> player("No. Can you?").also { stage++ }
            5 -> npc(FaceAnim.OLD_NORMAL, "Well obviously.").also { stage++ }
            6 -> player("Go on then.").also { stage++ }
            7 ->
                npc(
                    FaceAnim.OLD_SAD,
                    "Here! Are you mad? No, 'fraid I can't do that. Could",
                    "cause all sorts of trouble.",
                ).also {
                    stage++
                }

            8 -> player("You're a bit mad really aren't you?").also { stage++ }
            9 ->
                npc(
                    FaceAnim.OLD_SAD,
                    "So would you be if you could speak over 1000",
                    "languages. I bet it's bliss being ignorant of this world's",
                    "trouble! I'm off!",
                ).also {
                    stage++
                }

            10 -> {
                GameWorld.Pulser.submit(
                    object : Pulse(1, npc) {
                        var counter = 0

                        override fun pulse(): Boolean {
                            when (counter++) {
                                1 -> {
                                    poofClear(npc).also {
                                        npc.respawnTick =
                                            GameWorld.ticks +
                                            npc.definition.getConfiguration(NPCConfigParser.RESPAWN_DELAY, 60)
                                    }
                                }
                            }
                            return true
                        }
                    },
                )
            }
        }
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.POSTIE_PETE_3805)
    }
}
