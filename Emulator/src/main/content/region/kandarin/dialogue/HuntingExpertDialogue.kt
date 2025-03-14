package content.region.kandarin.dialogue

import core.api.getStatLevel
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class HuntingExpertDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HALF_GUILTY,
            "Whoah there, stranger, careful where you're walking. I",
            "almost clobbered you there; thought you were a larupia",
            "or something.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "What are you doing here?",
                    "What's a larupia?",
                    "What is that cool cape you're wearing?",
                    "Ahh, leave me alone you crazy killing person.",
                ).also {
                    stage++
                }

            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "What are you doing here?").also { stage = 10 }
                    2 -> player(FaceAnim.HALF_GUILTY, "What's a larupia?").also { stage = 117 }
                    3 -> player(FaceAnim.HALF_GUILTY, "What is that cool cape you're wearing?").also { stage = 121 }
                    4 ->
                        player(FaceAnim.HALF_GUILTY, "Ahh, leave me alone you crazy killing person.").also {
                            stage =
                                115
                        }
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Me? Oh, I'm just having some fun, capturing wild",
                    "animals, living on the edge, stuff like that.",
                ).also {
                    stage++
                }
            11 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Hey, want me to teach you how to catch the critters",
                    "around here? Ain't come across anything yet that I",
                    "couldn't capture.",
                ).also {
                    stage++
                }
            12 ->
                options(
                    "Okay, where's a good place to start?",
                    "What sort of things are there to catch around here?",
                    "How can I improve my chances of making a capture?",
                    "How can I get clothes and equipment like yours?",
                    "I think I'll give it a miss right now.",
                ).also {
                    stage++
                }
            13 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Ok, where's a good place to start?").also { stage = 100 }
                    2 ->
                        player(FaceAnim.HALF_GUILTY, "What sort of things are there to catch around here?").also {
                            stage =
                                110
                        }
                    3 ->
                        player(FaceAnim.HALF_GUILTY, "How can I improve my chances of making a capture?").also {
                            stage =
                                111
                        }
                    4 ->
                        player(FaceAnim.HALF_GUILTY, "Where can I get clothes and equipment like yours?").also {
                            stage =
                                112
                        }
                    5 ->
                        player(FaceAnim.HALF_GUILTY, "I think I'll give it a miss right now, thanks.").also {
                            stage =
                                117
                        }
                }
            100 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Your easiest bet around here is probably to go for some",
                    "of the birds. Go for crimson swifts; they're the",
                    "bright red ones that hang around near the coast.",
                    "They're as obliging as can be, or maybe they're just",
                ).also {
                    stage++
                }
            101 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "plain dumb, but either way they just seem to throw",
                    "themselves into traps half the time.",
                ).also {
                    stage++
                }
            102 -> player(FaceAnim.HALF_GUILTY, "What sort of trap should I use?").also { stage++ }
            103 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Just use a standard bird snare and you'll be fine. You",
                    "can get them in any decent shop that sells Hunter",
                    "gear.",
                ).also {
                    stage++
                }
            104 -> player(FaceAnim.HALF_GUILTY, "How do they work, then?").also { stage++ }
            105 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, they're kind of sneaky, really. You've got a tall",
                    "post with what appears to be a little perch sticking out",
                    "the side. Now, that the bird will see this as somewhere to sit",
                    "but the perch is actually rigged such that when the bird",
                ).also {
                    stage++
                }
            106 -> npc(FaceAnim.HALF_GUILTY, "lands on it, it'll drop away.").also { stage++ }
            107 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Further, as the perch drops, it releases a noose that",
                    "tightens around the bird's feet and captures it. Neat",
                    "huh?",
                ).also {
                    stage++
                }
            110 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "What're you interested in? You've got anything from",
                    "birds for beginners up to larupias for the pros, not to",
                    "mention weasels, butterflies, barb-tails and chinchompas.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            111 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "There are four key factors that will affect your",
                    "chances: what bait you use, how close to the trap you",
                    "are, your appearance and your smell.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            112 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, the equipment isn't too hard to get hold of. You've",
                    "got specialist shops like Aleck's in Yanille that'll see you",
                    "right. He's a bit pricey, mind, but it's pretty decent kit.",
                ).also {
                    stage++
                }
            113 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "The clothing is a tad trickier to get hold of, the",
                    "materials required being rather difficult to separate from",
                    "their reluctant owners.",
                ).also {
                    stage++
                }
            114 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "But, if you can get the furs, there are shops like the",
                    "tailor's in east Varrock that can help you.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            115 -> npc(FaceAnim.HALF_GUILTY, "Heh, well I guess it's not everyone's thing.").also { stage++ }
            116 -> npc(FaceAnim.HALF_GUILTY, "Suit yourself!").also { stage = END_DIALOGUE }
            117 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "What's a larupia? You don't know? You mean you",
                    "really don't recognise the significance of these clothes",
                    "I'm wearing?",
                ).also {
                    stage++
                }
            118 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "Well, they're certainly decorative...what about them?",
                ).also { stage++ }
            119 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Well, these are the furs from a larupia. Caught the",
                    "creature myself I did, and these make me blend in",
                    "better, see?",
                ).also {
                    stage++
                }
            120 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "If you want to be successful as a hunter you've got to",
                    "be as stealthy as a kyatt, as quiet as a mouse and blend",
                    "in like a, well like a larupia, I guess!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            121 ->
                if (getStatLevel(player, Skills.HUNTER) < 99) {
                    npc(
                        FaceAnim.HALF_GUILTY,
                        "This? It's a Hunter's Skillcape. It shows that I am a",
                        "true master of the art of Hunting. If you ever manage",
                        "to train your Hunter skill to it maximum level then I",
                        "could sell you one.",
                    )
                    stage = 31
                } else {
                    npc(
                        "An adventurer whom possesses the same level",
                        "of Hunter as me, would you be interested in buying",
                        "a skillcape of Hunter for 99,000 gold coins?",
                    )
                    stage = 123
                }
            122 -> end()
            123 -> options("Yes, please.", "No, thank you.").also { stage++ }
            124 ->
                when (buttonId) {
                    1 -> player("Yes, please.").also { stage++ }
                    2 -> player("No, thank you.").also { stage = END_DIALOGUE }
                }

            125 -> {
                end()
                if (!player.inventory.contains(Items.COINS_995, 99000)) {
                    player("Sorry, I don't have that amount of money.")
                    stage = 99
                } else {
                    if (player.inventory.freeSlots() < 2) {
                        player("Sorry, I don't seem to have enough inventory space.")
                        stage = 99
                    }
                    if (!player.inventory.containsItem(COINS)) {
                        end()
                        return true
                    }
                    if (player.inventory.remove(COINS)) {
                        player.inventory.add(
                            if (player.getSkills().masteredSkills > 1) ITEMS[1] else ITEMS[0],
                            ITEMS[2],
                        )
                        npc("There you go! Enjoy adventurer.")
                    }
                }
            }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue {
        return HuntingExpertDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.HUNTING_EXPERT_5113)
    }

    companion object {
        private val ITEMS =
            arrayOf(Item(Items.HUNTER_CAPE_9948), Item(Items.HUNTER_CAPET_9949), Item(Items.HUNTER_HOOD_9950))
        private val COINS = Item(Items.COINS_995, 99000)
    }
}
