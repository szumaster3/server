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

    override fun handle(interfaceId: Int, buttonId: Int, ): Boolean {
        when (player.viewport.region.id) {
            11679 -> when (stage) {
                0 -> npcl(FaceAnim.OLD_NORMAL, "4 pints of your finest please.").also { stage++ }
                1 -> sendNPCDialogue(player, NPCs.BARMAID_2178, "4 pints coming right up. Are you expecting guests?", FaceAnim.OLD_NORMAL).also { stage++ }
                2 -> npcl(FaceAnim.OLD_NORMAL, "Just a few old friends.").also { stage++ }
                3 -> sendNPCDialogue(player, NPCs.BARMAID_2178, "You want me to put it on your tab?", FaceAnim.OLD_NORMAL).also { stage++ }
                4 -> npcl(FaceAnim.OLD_NORMAL, "That would be great, thanks. Oh, cripes! I've left a fire burning! I've got to go!").also { stage = END_DIALOGUE }
            }

            14647 -> when (stage) {
                0 -> npcl(FaceAnim.OLD_NORMAL, "Hey Pete. Any news from the fleet?").also { stage++ }
                1 -> sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "No yo ho bruv.").also { stage++ }
                2 -> sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "Tis all quiet on the eastern front.").also { stage++ }
                3 -> npcl(FaceAnim.OLD_NORMAL, "Good good.").also { stage++ }
                4 -> npcl(FaceAnim.OLD_NORMAL, "Well you know what to do if something goes wrong.").also { stage++ }
                5 -> sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "Aye, that I do, bruv.").also { stage++ }
                6 -> sendNPCDialogue(player, NPCs.PIRATE_PETE_2825, "Holler like a bosun without a bottle.").also { stage++ }
                7 -> npcl(FaceAnim.OLD_NORMAL, "That's it brother, and we'll come a running!").also { stage = END_DIALOGUE }
            }

            12854 -> when (stage) {
                0 -> player(FaceAnim.LAUGH, "HEY PETE,WHAT ARE YOU DOING", "HERE?").also { stage++ }
                1 -> npc(FaceAnim.OLD_NORMAL, "Shhhh! It's a library you know!").also { stage++ }
                2 -> player(FaceAnim.HAPPY, "Oh, sorry! So, what's new?").also { stage++ }
                3 -> npc(FaceAnim.OLD_NORMAL, "Not much, just doing some research on Dwarvern", "Culture. Did you know that Keldagrim was originally", "constructed by a long lost clan who embraced the power", "of magic, and used it to aid with glorious buildings like").also { stage++ }
                4 -> npc(FaceAnim.OLD_NORMAL, "the royal palace?").also { stage++ }
                5 -> player(FaceAnim.HAPPY, "Really??").also { stage++ }
                6 -> npc(FaceAnim.OLD_NORMAL, "Yes. They even stayed down there for 1000 years or", "more, in a time they call 'The age of Kings'.").also { stage++ }
                7 -> player(FaceAnim.HAPPY, "WOW!").also { stage++ }
                8 -> npc(FaceAnim.OLD_NORMAL, "Shhhh! That's enough history for one day I think!").also { stage++ }
                9 -> {
                    end()
                    GameWorld.Pulser.submit(
                        object : Pulse(1, npc) {
                            var counter = 0

                            override fun pulse(): Boolean {
                                when (counter++) {
                                    0 -> forceWalk(findLocalNPC(player, npc.id)!!, Location.create(3210, 3490, 0), "smart")
                                    3 -> {
                                        poofClear(npc).also {
                                            npc.respawnTick = GameWorld.ticks + npc.definition.getConfiguration(
                                                NPCConfigParser.RESPAWN_DELAY, 60
                                            )
                                        }
                                        return true
                                    }
                                }
                                return false
                            }
                        },
                    )
                }
            }

            12084 -> when (stage) {
                0 -> npcl(FaceAnim.OLD_NORMAL, "Hey Bro, how's the party business?").also { stage++ }
                1 -> sendNPCDialogue(player, NPCs.PARTY_PETE_659, "Great! Thanks!").also { stage++ }
                2 -> sendNPCDialogue(player, NPCs.PARTY_PETE_659, "Celebrating weddings and drop parties...").also { stage++ }
                3 -> sendNPCDialogue(player, NPCs.PARTY_PETE_659, "...and getting paid for it!").also { stage++ }
                4 -> npcl(FaceAnim.OLD_NORMAL, "Nice. Well I'm here to talk to some white knights.").also { stage++ }
                5 -> sendNPCDialogue(player, NPCs.PARTY_PETE_659, "Want to take some cake with you?").also { stage++ }
                6 -> npcl(FaceAnim.OLD_NORMAL, "No thanks, I'm still dieting.").also { stage++ }
                7 -> npcl(FaceAnim.OLD_NORMAL, "Just seafood for me.").also { stage = END_DIALOGUE }
            }

            12086 ->  when (stage) {
                0 -> npcl(FaceAnim.OLD_NORMAL, "Well I'm back. And he said no. Again.").also { stage++ }
                1 -> sendNPCDialogue(player, NPCs.ORACLE_746, "Lemons? Both of them?").also { stage++ }
                2 -> npcl(FaceAnim.OLD_NORMAL, "Lemons? What are you talking about?").also { stage++ }
                3 -> sendNPCDialogue(player, NPCs.ORACLE_746, "Fragile! Do not bend!").also { stage++ }
                4 -> npcl(FaceAnim.OLD_NORMAL, "I swear you get stranger every time.").also { stage++ }
                5 -> npcl(FaceAnim.OLD_NORMAL, "Ok, I'll ask again.").also { stage = END_DIALOGUE }
            }

            12089 -> when (stage) {
                0 -> npcl(FaceAnim.OLD_NORMAL, "Psssst! How is the plan going?").also { stage++ }
                1 -> sendNPCDialogue(player, NPCs.NOTERAZZO_597, "Perfectly. Everyone is prepared.").also { stage++ }
                2 -> npcl(FaceAnim.OLD_NORMAL, "Great. Now don't forget- nobody does anything until I give the signal.").also { stage++ }
                3 -> sendNPCDialogue(player, NPCs.NOTERAZZO_597, "Sure no problem. We'll be waiting.").also { stage++ }
                4 -> npcl(FaceAnim.OLD_NORMAL, "Till next time. Farewell.").also { stage = END_DIALOGUE }
            }

            10291 -> when (stage) {
                0 -> npcl(FaceAnim.OLD_NORMAL, "Hi Pete!").also { stage++ }
                1 -> npcl(FaceAnim.OLD_NORMAL, "Mum wants to know if you're coming round for tea.").also { stage++ }
                2 -> sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "No. I've got another late night here.").also { stage++ }
                3 -> sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "Ol' blue beak's got the flu again!").also { stage++ }
                4 -> sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "Oh dear. Nothing too serious I hope.").also { stage++ }
                5 -> sendNPCDialogue(player, NPCs.PARROTY_PETE_1216, "Well as long as he hasn't caught anything from that evil chicken.").also { stage++ }
                6 -> npcl(FaceAnim.OLD_NORMAL, "Got a taste for parrots now too does he?").also { stage++ }
                7 -> npcl(FaceAnim.OLD_NORMAL, "*Sigh* Will nothing sacred be safe?").also { stage = END_DIALOGUE }
            }

            9776 -> when (stage) {
                0 -> player("Hey Pete, what you doing here?").also { stage++ }
                1 -> npc(FaceAnim.OLD_NORMAL, "Just doing a quick survey on famous people and their", "favourite colour. It's a special feature for 'West", "Wyverns Women's Weekly'.").also { stage++ }
                2 -> player("Can I get a copy?").also { stage++ }
                3 -> npc(FaceAnim.OLD_NORMAL, "Can you speak Wyvern?").also { stage++ }
                4 -> player("No. Can you?").also { stage++ }
                5 -> npc(FaceAnim.OLD_NORMAL, "Well obviously.").also { stage++ }
                6 -> player("Go on then.").also { stage++ }
                7 -> npc(FaceAnim.OLD_SAD, "Here! Are you mad? No, 'fraid I can't do that. Could", "cause all sorts of trouble.").also { stage++ }
                8 -> player("You're a bit mad really aren't you?").also { stage++ }
                9 -> npc(FaceAnim.OLD_SAD, "So would you be if you could speak over 1000", "languages. I bet it's bliss being ignorant of this world's", "trouble! I'm off!").also { stage++ }
                10 -> {
                    end()
                    GameWorld.Pulser.submit(
                        object : Pulse(1, npc) {
                            var counter = 0

                            override fun pulse(): Boolean {
                                when (counter++) {
                                    1 -> {
                                        poofClear(npc).also {
                                            npc.respawnTick = GameWorld.ticks + npc.definition.getConfiguration(
                                                NPCConfigParser.RESPAWN_DELAY, 60
                                            )
                                        }
                                        return true
                                    }
                                }
                                return false
                            }
                        },
                    )
                }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.POSTIE_PETE_3805)
}
