package content.region.misthalin.draynor.quest.swept.dialogue

import content.data.GameAttributes
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs

/**
 * Represents the Gus dialogue.
 *
 * # Relations
 * - [Swept Away][content.region.misthalin.draynor.quest.swept.SweptAway]
 */
@Initializable
class GusDialogue(player: Player? = null) : Dialogue(player) {

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                val hasLabels = getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS, false)
                val completeTask = getAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS_COMPLETE, false)

                if (hasLabels) {
                    if (!anyInInventory(player, Items.NEWT_LABEL_14065, Items.TOAD_LABEL_14066, Items.NEWTS_AND_TOADS_LABEL_14067)) {
                        addItemOrDrop(player, Items.NEWT_LABEL_14065, 1)
                        addItemOrDrop(player, Items.TOAD_LABEL_14066, 1)
                        addItemOrDrop(player, Items.NEWTS_AND_TOADS_LABEL_14067, 1)
                    } else {
                        if (!inInventory(player, Items.NEWT_LABEL_14065, 1)) {
                            addItemOrDrop(player, Items.NEWT_LABEL_14065, 1)
                        }
                        if (!inInventory(player, Items.TOAD_LABEL_14066, 1)) {
                            addItemOrDrop(player, Items.TOAD_LABEL_14066, 1)
                        }
                        if (!inInventory(player, Items.NEWTS_AND_TOADS_LABEL_14067, 1)) {
                            addItemOrDrop(player, Items.NEWTS_AND_TOADS_LABEL_14067, 1)
                        }
                    }
                    npc("If you lose your labels or want me to put things back", "the way they were, just let me know.")
                    stage = END_DIALOGUE
                } else if (completeTask) {
                    npc("Thank you so much for sorting out those labels for me!", "I would have been here for weeks trying to sort out", "that mess.")
                    stage = 29
                } else {
                    player("Hello. Are you the delivery, um, ghoul? Hetty told me", "that you could give me a newt for her potion.")
                    stage++
                }
            }

            1 -> {
                npc("Yes, but I'd prefer it if you called me Gus. I make", "deliveries for Numinous Witchery Supplies Ltd.")
                stage++
            }

            2 -> {
                npc(
                    "If it's a newt you're after, you can have one from one",
                    "of these crates...but there's a problem."
                )
                stage++
            }

            3 -> {
                player("What kind of problem?")
                stage++
            }

            4 -> {
                npc(
                    "A newty, toady sort of problem, I'm afraid. You see, the",
                    "labels on the crates have somehow got mixed up."
                )
                stage++
            }

            5 -> {
                npc(
                    "One of these crates contains just newts. One contains",
                    "just toads. One contains a mixture of newts and toads."
                )
                stage++
            }

            6 -> {
                npc(
                    "The problem is, I've put the wrong label on ALL three",
                    "crates! I can't remember which crate contains newts,",
                    "toads, or both newts and toads."
                )
                stage++
            }

            7 -> {
                player("Oh dear, that certainly sounds like quite a problem.")
                stage++
            }

            8 -> {
                npc(
                    "Indeed! Please don't tell Ms Hetty or I'll be tomorrow's",
                    "soup. The other problem is that I can't look in the",
                    "crates myself. You see, I have a terrible fear of newts.",
                    "Will you work out which box contains toads, which one"
                )
                stage++
            }

            9 -> {
                npc("contains newts, and which one has both?")
                stage++
            }

            10 -> {
                npc(
                    "If you could sort out the labels, I'd be happy to give",
                    "you a newt to take to Ms Hetty. Is it a deal?"
                )
                stage++
            }

            11 -> {
                options("It's a deal.", "Can't you just let me take a newt now?", "Sorry, you're on your own.")
                stage++
            }

            12 -> when (buttonId) {
                1 -> {
                    player("It's a deal.")
                    stage++
                }

                2 -> {
                    player("Can't you just let me take a newt now?")
                    stage = 26
                }

                3 -> {
                    player("Sorry, you're on your own.")
                    stage = 28
                }
            }

            13 -> {
                npc("Excellent; I'm so relieved!")
                stage++
            }

            14 -> {
                player("So, what do we need to do to sort out these crates,", "then?")
                stage++
            }

            15 -> {
                npc(
                    "Well, here's the problem. We need to figure out what is",
                    "in each crate. All we know is that I've put the wrong",
                    "label on EACH and EVERY crate. So, not one crate",
                    "has the correct label on it."
                )
                stage++
            }

            16 -> {
                player("That sounds easy enough; I'll just have a look in each", "crate and -")
                stage++
            }

            17 -> {
                npc(
                    "No, no, no! You can't do that! We can't disturb them!",
                    "Don't you know anything about magical frogs and",
                    "newts?"
                )
                stage++
            }

            18 -> {
                npc(
                    "Also, we cannot waste too much stock; Ms Hetty will",
                    "find out if we start taking newts and frogs out of",
                    "crates as we please."
                )
                stage++
            }

            19 -> {
                player("So what do you suggest?")
                stage++
            }

            20 -> {
                npc(
                    "Well, when I was cramming for my crate delivery",
                    "certification exam, I remember reading about situations",
                    "like this."
                )
                stage++
            }

            21 -> {
                npc(
                    "A person can figure out exactly what is in all three",
                    "crates by only taking out ONE item from ONE crate."
                )
                stage++
            }

            22 -> {
                npc(
                    "The problem is, I can't remember how to figure it out.",
                    "I'm pretty sure the key is in what I said earlier:",
                    "that EACH and EVERY crate has a wrong label on it."
                )
                stage++
            }

            23 -> {
                npc("Here are three labels that you can paste on the crates.")
                addItemOrDrop(player, Items.NEWT_LABEL_14065, 1)
                addItemOrDrop(player, Items.TOAD_LABEL_14066, 1)
                addItemOrDrop(player, Items.NEWTS_AND_TOADS_LABEL_14067, 1)
                setAttribute(player, GameAttributes.QUEST_SWEPT_AWAY_LABELS, true)
                stage++
            }

            24 -> {
                npc("If you lose your labels or want me to put things back", "the way they were, just let me know.")
                stage++
            }

            25 -> {
                player("Thanks, Gus.")
                stage = END_DIALOGUE
            }

            26 -> {
                npc("No. I'm afraid that these newts and toads are the property of Numinous Magical Supplies Ltd up until the time that they have been properly delivered.")
                stage++
            }

            27 -> {
                npc(
                    FaceAnim.HALF_ASKING,
                    "So, what do you say? Will you help me in return for a newt that you can take to Ms Hetty?"
                )
                stage = 11
            }

            28 -> {
                npc("Hmmph. Well, I wish you the best of luck finding a quality newt somewhere else!")
                stage = END_DIALOGUE
            }

            29 -> {
                player(
                    "You're very welcome. Now that those crates are sorted,",
                    "could I please have a newt to bring to Hetty?"
                )
                stage++
            }

            30 -> {
                npc("Of course. Just take one from the crate containing", "newts only.")
                stage++
            }

            31 -> {
                player("Okay, thanks.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return GusDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUS_8205)
    }
}
