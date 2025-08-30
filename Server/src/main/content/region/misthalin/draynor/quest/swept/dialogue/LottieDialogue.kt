package content.region.misthalin.draynor.quest.swept.dialogue

import content.data.GameAttributes
import core.api.addItem
import core.api.getAttribute
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs

/**
 * Represents the Lottie dialogue.
 *
 * # Relations
 * - [Swept Away][content.region.misthalin.draynor.quest.swept.SweptAway]
 */
@Initializable
class LottieDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.NEUTRAL, "So, what do you want, then?")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                if (getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LOTTIE, false)) {
                    // ?
                }
                showTopics(
                    Topic<Any?>("Who are you and what are you doing in Betty's basement?", 20, false),
                    Topic<Any?>("I need to retrieve Betty's wand.", 1, false),
                    Topic<Any?>("What is this place?", 25, false),
                    Topic<Any?>("No need to be rude!", 29, false)
                )
            }

            1 -> {
                player(
                    "Betty sent me down here to fetch her wand, but she",
                    "said there might be a small complication?"
                )
                stage++
            }

            2 -> {
                npc("I'll say there are complications! Betty has a penchant", "for intricacy.")
                stage++
            }

            3 -> {
                npc("You're not going to be able to get that wand until you", "get that chest open.")
                stage++
            }

            4 -> {
                npc("That sounds simple enough; what's so complicated about", "opening a little chest?")
                stage++
            }

            5 -> {
                npc(
                    "Well, the chest in which Betty stores her wand is",
                    "magically sealed. I won't confuse you with the details",
                    "right now, but  here's the deal:"
                )
                stage++
            }

            6 -> {
                npc("That chest is simply not going to open until order has", "been restored to Betty's menagerie.")
                stage++
            }

            7 -> {
                player("What do you mean? What menagerie?")
                stage++
            }

            8 -> {
                npc(
                    "Well, if you have a look around, you'll see that there",
                    "are six separate chambers, each of which is designed to",
                    "house a little critter."
                )
                stage++
            }

            9 -> {
                npc(
                    "Each creature has an enclosure that has been made",
                    "especially for it, but, at the moment, several of them are",
                    "out of place."
                )
                stage++
            }

            10 -> {
                npc(
                    "So, what you're saying is that we need to put each",
                    "creature in its proper enclosure in order to open the",
                    "chest?"
                )
                stage++
            }

            11 -> {
                npc(
                    "Exactly. Now, I should warn you - these are very",
                    "sensitive creatures and they really don't like each other."
                )
                stage++
            }

            12 -> {
                player("What does that have to do with anything?")
                stage++
            }

            13 -> {
                npc("Two things. First, you can't carry more than one", "creature at a time.")
                stage++
            }

            14 -> {
                npc(
                    "Second, you can't carry a creature through the door of",
                    "a chamber which contains another creature."
                )
                stage++
            }

            15 -> {
                npc(
                    "You'll have to move the creatures one by one. You can",
                    "use the holding pen in this room to help shift them all",
                    "into the right position."
                )
                stage++
            }

            16 -> {
                npc(
                    "One more thing I should mention: you shouldn't have",
                    "to move the bat and the snail at all - they're already",
                    "exactly where they need to be."
                )
                stage++
            }

            17 -> {
                npc(
                    "Now, if you have any questions or want me to put the",
                    "creatures back to the arrangement they're in now, just",
                    "let me know."
                )
                stage++
            }

            18 -> {
                npc(
                    "Here is a magic slate. It always shows an overview of",
                    "exactly who is where. You can use it for reference, if",
                    "you like. It's not necessary, but I find it helpful."
                )
                addItem(player, Items.MAGIC_SLATE_14069, 1)
                stage++
            }

            19 -> {
                player("Okay, thanks.")
                stage = END_DIALOGUE
            }

            20 -> {
                npcl(FaceAnim.NEUTRAL, "My name is Lottie and it should be rather obvious what I'm doing here.")
                stage++
            }

            21 -> {
                options(
                    "Are you the cleaning lady?",
                    "Are you a wise and powerful witch, here to show Betty the ropes?"
                )
                stage++
            }

            22 -> {
                when (buttonId) {
                    1 -> {
                        npcl(FaceAnim.NEUTRAL, "I most certain am not! I'm learning the ways of witchcraft from Betty.")
                        stage++
                    }

                    2 -> {
                        npcl(FaceAnim.NEUTRAL, "Oh, is my witchly prowess that obvious? How flattering.")
                        stage = 24
                    }
                }
                npcl(
                    FaceAnim.NEUTRAL,
                    "If you knew anything at all, you'd be able to tell from my witchly deportment, and aura of mysticism and enchantment that radiates from my every pore."
                )
                stage = 0
            }

            23 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "If you knew anything at all, you'd be able to tell from my witchly deportment, and aura of mysticism and enchantment that radiates from my every pore."
                )
                stage = 0
            }

            24 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "In actuality, it is I who is the apprentice, but I'm sure it is only a matter of time before I'm a fully licensed witch."
                )
                stage = 0
            }

            25 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "Well, if you'd use your eyes, you could tell it is – I know this is going to be difficult to believe – A basement."
                )
                stage++
            }

            26 -> {
                playerl(
                    FaceAnim.THINKING,
                    "Oh, very helpful. I can see it's a basement, but what are these cages doing down here?"
                )
                stage++
            }

            27 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "I see you don't know your witchcraft very well. This is Betty's menagerie. Many witches keep some creatures on hand for their spells and incantations."
                )
                stage++
            }

            28 -> {
                npcl(FaceAnim.NEUTRAL, "Not to mention the company.")
                stage++
            }

            29 -> {
                npcl(FaceAnim.NEUTRAL, "Sorry, it's just that I'm very busy.")
                stage++
            }

            30 -> {
                npcl(
                    FaceAnim.NEUTRAL,
                    "I've lots of studying to do and all of these creatures to look after. It's not easy to keep track of everything and make casual conversation as well."
                )
                stage++
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return LottieDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.LOTTIE_8206)
    }
}
