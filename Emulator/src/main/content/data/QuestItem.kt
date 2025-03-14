package content.data

import core.api.getItemName
import core.api.getVarbit
import core.api.sendDialogue
import core.game.dialogue.DialogueFile
import core.tools.END_DIALOGUE

/**
 * ```
 *     val configIds = mapOf(
 *         20971715 to 4732, 20971701 to 4744, 20971708 to 4738, 20971625 to 4750,
 *         20971526 to 4733, 20971694 to 4745, 20971534 to 4739, 20971633 to 4751,
 *         20971543 to 4734, 20971559 to 4746, 20971551 to 4740, 20971641 to 4752,
 *         20971567 to 4735, 20971583 to 4747, 20971575 to 4741, 20971649 to 4753,
 *         20971591 to 4736, 20971687 to 4748, 20971599 to 4742, 20971657 to 4754,
 *         20971608 to 4737, 20971680 to 4749, 20971616 to 4743, 20971665 to 4755,
 *         20971708 to 4738, 20971625 to 4750, 20971701 to 4744, 20971673 to 7756
 *     )
 *     return if (configIds[value]?.let { getVarbit(player, it } == true 1 else 0
 * ```
 */
class QuestItem(
    var itemID: Int,
) : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        sendDialogue(
            player!!,
            when (itemID) {
                7464,
                5507,
                5508,
                -> "That's my book! What's it doing in your bank?"

                2399,
                2400,
                2401,
                -> "You've killed Delrith, so you don't need any of the little grey keys that you collected to get Silverlight."

                1536,
                1537,
                1538,
                1535,
                -> {
                    if (getVarbit(player!!, 3741) == 1) {
                        "You've opened a secret passageway to Crandor Island, so you won't need the map that showed you how to get there by sea."
                    } else {
                        "Ned knows the way to Crandor now, so you don't need the map any more. Also, if you go there, look around for secret passages. There might be another way to get back to Crandor."
                    }
                }

                1548,
                1544,
                1545,
                1546,
                1547,
                1543,
                -> "The coloured keys from Melzar's maze are very pretty, but you don't really need to keep them. If you ever go there again, the creatures in the maze will drop more coloured keys for you."

                1542 -> "You don't really need to keep the key to Melzar's Maze. If you ever go there again, you can ask for a new one from the Guildmaster of the Champions' Guild."
                271 -> "Since you've already helped to repair Professor Oddenstein's machine, you can get rid of the pressure gauge."
                274,
                272,
                -> "You really don't need to keep fish food in your bank."

                275 -> "The little key that opens a closet in Draynor Manor? I think you can afford to get rid of it."
                276 -> "You aren't going to need that rubber tube again."
                277 -> "No need to keep that oil can."
                666 -> "That's a nice picture of Sir Vyvin, but you don't really need it."
                668 -> "Blurite ore's handy for making ceremonial swords, but you've already done that quest. Members can make crossbows and bolts out of it, but it has no other use."
                667 -> "You don't really need a copy of Sir Vyvin's sword, although it's a fairly nice weapon."
                432 -> "I see you've got the key to One-Eyed Hector's chest. That's not much use to you."
                433 -> "You've already found the pirate's treasure, so you can get rid of the pirate's message."
                7956 -> "You've already found the pirate's treasure, so you can get rid of the pirate's casket."
                7957 -> "You've already found the pirate's treasure, so you can get rid of the pirate's apron."
                2418 -> "You aren't going to need to get back into Prince Ali's cell."
                2421,
                2419,
                -> "You don't need a wig. Your head looks fine."

                2423 -> "I can't imagine why you've kept the imprint of Lady Keli's key."
                2424 -> "If you want to change your appearance, go to the Makeover Mage. You don't need this skin paste."
                964 -> "I suggest you get rid of the skull. It's unhygienic."
                755 -> "You have a message from Juliet to Romeo. There's no point in keeping that now that they've split up."
                756 -> "A potion made from cadava berries? There's not much you can do with one of those."
                753 -> "If you get rid of those cadava berries, you'll still be able to pick more near Varrock, and you'll save bank space."
                290 -> "Sedridor's research into the mysteries of the runes is very interesting, but you don't need it."
                291 -> "Aubury's notes won't be of any further use to you. You can't even read them!"
                761 -> "You won't need Jonny the Beard's intelligence report now that you've found the Shield of Arrav."
                763,
                765,
                -> "You don't need to keep bits of the Shield of Arrav now that you've completed that quest."

                769 -> "You can get rid of the little certificate from the Museum of Varrock. You've already been rewarded for taking one to the King."
                1549 -> "Now that you've slain Count Draynor, you don't need that stake. It's not much use against other vampyres."
                300 -> "Ugh... A rat's tail! Get rid of it!"
                14062 -> "I don't think you'll need the broom ointment."
                14064 -> "That newt doesn't look very useful."
                14065 -> "You probably won't need the 'Newts' label."
                14066 -> "You probably won't need the 'Toads' label."
                14067 -> "You certainly won't need the 'Newts & Toads' label."
                14068 -> "Betty's wand? I don't think you should be messing around with that."
                14069 -> "There's no point in hanging on to that slate."
                14070 -> "You shouldn't be keeping that reptile in there."
                14071 -> "You shouldn't be keeping that blackbird in there."
                14072 -> "You shouldn't be keeping that bat in there."
                14073 -> "You shouldn't be keeping that spider in there."
                14074 -> "You shouldn't be keeping that rat in there."
                14075 -> "You shouldn't be keeping that snail in there."
                9590,
                9589,
                -> "You don't need to keep the dossier from the White Knight."

                9591 -> "You completed the Black Knights' fortress you don't need this old cauldron anymore."
                524,
                525,
                522,
                523,
                12546,
                -> "You have completed the druidic ritual. You don't need the enchanted meat any longer."

                2409 -> "You don't need the witch's door key any longer."
                2408 -> "You don't need to keep the witch's diary. You can get it from the bookshelf again, if you need to."
                2410 -> "You don't need the magnet. You opened the witch's back door."
                2411 -> "You don't really need the witch's shed key any more."
                11211,
                11210,
                11203,
                11202,
                11204,
                11198,
                11196,
                11197,
                -> "You don't need that grim looking item."

                1584 -> "You've already given the id papers to Grip. This must be a forgery!"
                463,
                462,
                461,
                460,
                459,
                458,
                457,
                456,
                -> "You don't need that scorpion cage anymore."

                774,
                773,
                -> "You've obtained Avan's part of the crest. You don't need the perfect ruby jewellery."

                1856 -> "You've already read from the Ardougne tourist guide."
                1858,
                1857,
                -> "You've finished the Tribal Totem quest. Why do you still have this?"

                90 -> "You've given this blanket to the monk already. Did you steal it back again?"
                83 -> "You already fixed the lever in The Temple of Ikov."
                21,
                20,
                23,
                22,
                -> "You've finished the Clock Tower quest. You don't need that cog."

                15 -> "You completed the Holy Grail quest. You don't need Sir Galahad's table napkin."
                17 -> "You completed the Holy Grail quest. You don't need the grail bell anymore."
                18 -> "You completed the Holy Grail quest. You have no need of King Arthur's golden feather."
                19 -> "You completed the Holy Grail quest. Why do you have the grail?"
                587,
                588,
                -> "You have finished with Tree Gnome Village. You don't need the orbs."

                77 -> "You already got the keys from the lazy guard. You don't need the brew."
                76 -> "You have already used the keys from the lazy guard."
                2403 -> "You already completed the Hazeel Cult Quest no need to hang onto one of those scrolls."
                2404 -> "You have already opened the chest in the Carnillean household."
                1508 -> "You have already made Bravek a hangover cure. You don't need his scrawled note."
                1503 -> "You have no need of a warrant to the plague house."
                1509 -> "You have already given this book to Ted Rehnison. You don't need it."
                1510 -> "You have already shown Elena's picture to Jethick."
                1466 -> "Yuck! A sea slug! Get rid of it!"
                9665 -> "A sea slug torch... Fascinating! You don't need it lit, unlit, smouldering or otherwise."
                292 -> "You have completed the Waterfall quest, so you don't need the book about Baxtorian."
                298 -> "You have completed the Waterfall quest, so you don't need this key."
                296,
                297,
                -> "You have finished the Waterfall quest, so you certainly don't need that urn empty or full."

                423 -> "You've freed Elena already, so you don't need the key."
                425,
                424,
                422,
                -> "You have already distracted the guards with the pigeons."

                420 -> "You've already retreived Elena's distillator."
                415,
                417,
                416,
                419,
                418,
                -> "Guidor has already tested the plague sample. you don't need that any more."

                783 -> "You have given the bark sample to Hazelmere already."
                784 -> "You have already translated the ancient message told to you by Hazelmere."
                785 -> "You have completed the Grand Tree quest. You have no need of Glough's Journal."
                786 -> "You have completed the Grand Tree quest. You have no need of Hazelmere's scroll."
                793 -> "You have completed the Grand Tree quest. You don't need this rock."
                788 -> "You have already searched the Glough's chest. You have no possible use for the key."
                794 -> "You have already given the invasion plans to the King."
                787 -> "You have completed the Grand Tree quest. You don't need to hold on to this lumber order."
                791,
                790,
                789,
                792,
                -> "You have already opened the watchtower entrance you don't need twigs."

                2529,
                1484,
                1483,
                1482,
                1481,
                -> "The orbs are pretty, but you have already found your way down the well in the underground pass."

                1486 -> "Railings... Useful for poking undead in cages, I guess, but of no value otherwise."
                1493,
                1490,
                1488,
                1489,
                1487,
                -> "You have already passed the gate of Zamorak. You don't need this."

                1494 -> "You have killed Iban. You have little use for his journal."
                1500 -> "You have already done all you can with Iban's shadow."
                1502,
                1501,
                1498,
                1499,
                1496,
                1497,
                -> "You have completed the four tasks with the doll."

                603,
                602,
                -> "The professor has already fixed his telescope. You don't need this."

                1852 -> "You have already opened the Captain's chest with the copied key."
                1851 -> "A pineapple! How wonderful... You don't need it."
                1841 -> "You have already returned Ana to the Shantay Pass. You don't need to carry the barrel around."
                1842 -> "You have already returned Ana to the Shantay Pass. You don't need to carry the barrel around... or Ana."
                1849 -> ""
                1855 -> "You don't need that rock unless you plan on being caught by the guards agasin."
                9904 -> "The book on sailing is rather dull."
                2388,
                2387,
                2386,
                2385,
                2384,
                -> "You have already given evidence to the wizard."

                2374,
                2375,
                2373,
                -> "You have already received the ogre relic. You don't need these relic parts."

                2382,
                2383,
                2380,
                2381,
                -> "You have already used the powering crystals."

                2378,
                2379,
                2377,
                2397,
                2395,
                2394,
                2393,
                -> "You have already received the powering crystals from this item."

                0 -> "You have already returned the dwarf remains to Captain Lawgof."
                1 -> "You have already returned the dwarven toolkit to Captain Lawgof."
                1822 -> "You have already solved the murder mystery. You don't need this fingerprint."
                715,
                714,
                -> "You have already presented the Radimus notes."

                750 -> "You have already presented the gilded totem."
                717,
                719,
                718,
                729,
                720,
                -> "You have freed Ungadulu from possession. You don't have any use for the scrawled notes, books, tomes or pictures of dirty old bowls."

                9716 -> "What are you going to do with that rock?"
                2888,
                2889,
                -> "You do not need the Elemental workshop bowl."

                2954,
                2953,
                -> "You have doused the vampyre's coffin already. You don't need this old bucket of stale water."

                2967 -> "You don't need Filliman's Journal."
                3103,
                3102,
                -> "You have already recovered the combination for Denulf."

                3104 -> "You have already recovered the secret way map for Denulf."
                3112,
                3113,
                3110,
                3111,
                3109,
                -> "You don't need those coloured cannonballs"

                3207,
                3206,
                -> "You don't need the king's summons or messages."

                3208 -> "You can get rid of the crystal pendant. It has served it's purpose."
                3267 -> "You don't need that dirty old druid's robe."
                3268 -> "Argh! Is that a man in your backpack? Wait...no! It's a fake man, you crafty so-and-so. Still, you don't need it."
                3395 -> "You have already sold the apothecary this book."
                10830 -> "You've already given King Sorvott's decree to Burgher."
                10835,
                10834,
                10833,
                10832,
                10831,
                -> "You have already collected King Sorvott IV's window taxes."

                10842 -> "You have already handed in the troll's talking head to the Burgher."
                3846,
                3847,
                3845,
                -> "You have completed the Horror from the Deep quest. You probably don't need this book."

                3895,
                3894,
                -> "You have already given the Etceteria anthem to Queen Sigrid."

                3896 -> "You have already given the treaty to King Vargas to sign."
                4073 -> "A damp tinderbox not at all useful for anything."
                4193,
                4192,
                4189,
                4190,
                4191,
                -> "You already retrieved the lightning conductor mould from the chimney. You don't need this brush."

                4204 -> "You don't need the dusty old letter from the clock."
                4206,
                4205,
                -> "You have already planted this seed for Eluned."

                4238 -> "Yuck! An ectoplasm puddle."
                4272,
                4249,
                4248,
                4247,
                -> "You have already given that to the crone."

                4273 -> "You have already made the toy boat. You don't need this key."
                4415 -> "You already have an axe that's been sharpened by Brian. You don't need this blunt one."
                4490 -> "You have already spread enough mud over that poor tree."
                4496 -> "A broken stick. Really? You kept a broken stick?"
                4568 -> "You have already retrieved the schematic from that book."
                4597,
                4598,
                -> "You have already solved the safe combination."

                4606 -> "You have already caught the snake with this basket."
                4615,
                4616,
                -> "You have already found out about the golem. You don't need the note or letter."

                4617 -> "You have already retrieved the statuette from the display ."
                4623 -> "You have already reprogrammed the golem. You don't need this pen."
                4619 -> "You have already opened the golem's head. You don't need this key."
                4684 -> "You don't need more linen for mummification, however much you might like to."
                4686 -> "You don't need the book on embalming."
                4814,
                4815,
                4817,
                -> "You have already shown the portrait to Zavistic Rarve."

                6072 -> "You have already washed the mourner's top."
                6077,
                6079,
                6073,
                6075,
                -> "You have finished the Mourning's End quest. You don't need the mourner's books."

                6104,
                6083,
                -> "You have finished the Mourning's End quest. You don't need that key."

                6546 -> "You have already completed all the chores for Bob."
                6545 -> "You have already completed all the chores for Bob."
                else -> "You should get rid of the " + getItemName(itemID) + "."
            },
        ).also {
            stage = END_DIALOGUE
        }
    }
}
