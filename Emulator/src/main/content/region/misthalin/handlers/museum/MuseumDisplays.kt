package content.region.misthalin.handlers.museum

import content.region.desert.quest.golem.TheGolemListeners
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.world.map.Location
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.Scenery
import org.rs.consts.Vars

class MuseumDisplays :
    InterfaceListener,
    InteractionListener {
    val interfaceModel = "ifaces:534:model"
    val smallInterfaceModel = "small$interfaceModel"
    val displayItems =
        intArrayOf(
            Items.OLD_COIN_11179,
            Items.ANCIENT_COIN_11180,
            Items.ANCIENT_SYMBOL_11181,
            Items.OLD_SYMBOL_11182,
            Items.DISPLAY_CABINET_KEY_4617,
        )

    val description1 =
        arrayOf(
            "5th Age - yr 62",
            "",
            "The barbarian invaders were running out of",
            "steam. They were pretty much forced to make",
            "peace or be wiped out, so they agreed to settle",
            "down peacefully and make a village on the",
            "Misthalin/Asgarnia border, thinking they",
            "could lie low, well positioned to build up strength",
            "and resume their campaign later. This is what is",
            "now called the Barbarian Village.",
        )

    val description2 =
        arrayOf(
            "5th Age - yr 42-62",
            "",
            "The Fremennik Mountain Tribe has always been",
            "opposed to the manufacture of runes, as they",
            "feel this should be the world of the gods. A group ",
            "of the more warmongering of the mountain tribe",
            "broke away, led by a warrior called Gunnar, and",
            "went on a rampage across northern Kandarin",
            "and Asgarnia to put a stop to this",
            "'runecrafting'.",
            "",
            "A group of master smith dwarves, known as the",
            "Imcando, were some of the most unfortunate",
            "during this period. They had been given many",
            "fire and nature runes by the White Knights of",
            "Falador to help with their smithing by use of the",
            "Superheat Item spell. The invading Fremennik",
            "were not happy about all the spellcasting and",
            "launched many attacks, reducing the Imcando to",
            "a near extinction. The invading Fremennik met a",
            "fair amount of resistance and their numbers",
            "were reduced very significantly over the years.",
        )

    val description3 =
        arrayOf(
            "5th Age - yr 70",
            "",
            "Donated by one of our citizens, this historic",
            "tome details the rise and fall of an order of",
            "Zamorakian mages, called the Dagon'hai, ",
            "residing in Varrock. Skilled in the art of chaos",
            "Magic, the Dagon'hai fought continually with the",
            "priests of Saradomin until the day they were",
            "forced out of the city. They were not seen again",
            "until they were discovered recently in a tunnel",
            "near Varrock.",
        )

    val description4 =
        arrayOf(
            "5th Age - yr 8",
            "",
            "The kingdom of Asgarnia grew rapidly. King",
            "Raddallin, who was one of the tribal leaders of",
            "the area, had united many of the smaller tribes",
            "and settlements. However, one group within his",
            "domain is known as the Kinshra, or Black Knights.",
            "They had originally proved cooperative in",
            "helping the expansion of the kingdom of",
            "Asgarnia, and as a result he'd supported them",
            "in building a great fortress on his border to the",
            "Wilderness in the north-east. Another group",
            "known as the White Knights had also proven to",
            "be particularly competent in battle and were now",
            "helping him by being the main military force",
            "defending his capital city of Falador. However, it",
            "turned out that the White Knights and Black",
            "Knights had always been bitter rivals. It has",
            "been a constant political battle ever since for",
            "the kings of Asgarnia to prevent their kingdoms",
            "sliding into an out-and-out, very bloody civil",
            "war.",
        )

    val description5 =
        arrayOf(
            "5th Age - yr 47",
            "",
            "The invading Fremennik had a number of",
            "sympathisers within the kingdom of Misthalin.",
            "There were many warriors skilled in the art of the",
            "sword and bow that take offence to this art of",
            "spellcasting, and taking matters into their own",
            "hands, they destroyed the Mage Training Arena.",
        )

    val description6 =
        arrayOf(
            "4th Age - yr 1937",
            "",
            "Settlers established a town on the River Lum.",
            "Across the river they built a bridge and hence",
            "the town became called Lumbridge. This heralded",
            "more human settlements springing up early in",
            "the next age, where humans began to move into",
            "the desert, establishing Al Kharid, and towns on",
            "both Karamja and Entrana.",
        )

    val description7 =
        arrayOf(
            "5th Age - yr 7",
            "",
            "Using the power of runes, Saradominst humans",
            "in Kandarin, led by the Carnillean family, became",
            "confident and decided to take on the evil",
            "influences resident in the area around what is",
            "now known as Ardougne. From there, they",
            "expanded their territory until Kandarin was the",
            "largest of the human kingdoms.",
        )

    val description8 =
        arrayOf(
            "",
            "4th Age - yr 1-200",
            "",
            "Taverley is the site of the only known example",
            "of a surviving druidic stone circle, but the",
            "druids of Guthix speak of many others",
            "scattered around RuneScape in the past. Our",
            "best dating techniques place them in the 4th",
            "Age. The druids built them to worship Guthix all",
            "across the world. The Druids claim to be keeping",
            "watch for Guthix, keeping balance in the world.",
        )

    val description9 =
        arrayOf(
            "5th Age - yr 132",
            "",
            "King Arthur and the Knights of the Round Table",
            "arrive on Gielinor in year 132 of the 5th Age.",
            "Legend says they will return to Teragard in its time",
            "of greatest need, so they're passing their time",
            "on Gielinor until then. King Ulthas saw them",
            "as good and rightful men who would do much",
            "good in the world, and granted them a large area",
            "of land to set up a new home in north-east",
            "Kandarin. This is a replica of King Arthur's",
            "sword, Excalibur.",
        )

    val description10 =
        arrayOf(
            "5th Age - yr 20",
            "",
            "What little we know of gnomish history is mainly",
            "learned from the glider pilots scattered around",
            "RuneScape. They did mention that an old gnome",
            "engineer called Oaknock used to make model",
            "gliders for his children. Oaknock's son, ",
            "Yewnock, ended up creating this method of",
            "transport with inspiration from his father's",
            "models. Captain Daerkin and Captain Ninto were",
            "said to be test pilots during the initial",
            "prototypes, though enquiries have proved them",
            "to be reticent to give much information.",
            "Previously, we thought that the strange, 'bird- ",
            "like', wood and leather gliders that the gnomes",
            "find so useful would only support a single",
            "gnome in flight. however, due to a brave",
            "adventurer heroically aiding the gnomes to save",
            "their Grand Tree and earning their gratitude, we",
            "find that the gliders indeed transport at",
            "least one human and one gnome. The staff at",
            "the Museum have carefully constructed this",
            "replica model.",
        )

    val description11 =
        arrayOf(
            "5th Age - yr 9",
            "",
            "As the manufacture of runes intensified and",
            "Magic became available to people of a great",
            "variety of ages and backgrounds. It soon",
            "became evident just how dangerous this was, ",
            "with a great many tragic accidents occurring due",
            "to inexperienced wizards. Wizards and victims",
            "alike called for something to be done, but it was",
            "only due to a tragic accident involving one of",
            "the leaders of that time that the Mage Training",
            "Arena was constructed, established with all skill",
            "levels in mind. They even created magic",
            "guardians to run the building.",
        )

    val description12 =
        arrayOf(
            "5th Age - yr 169",
            "",
            "A collection of wizards took it upon themselves",
            "to resurrect the Mage Training Arena along with",
            "all the guardians that were destroyed with it.",
            "These guardians were created out of the very",
            "same essence as runes, embodying the Magic",
            "power and authority needed to oversee the",
            "Arena.",
        )

    val description13 =
        arrayOf(
            "1st Age",
            "",
            "Here, you can see a tablet displaying an artist's",
            "impression of the portal through which humans",
            "first stepped on to RuneScape. It was said that",
            "Guthix himself walked among humans, providing",
            "rune stones, after which man first named this",
            "world. Guthix supposedly created the portal to",
            "bring humans from another world to populate",
            "RuneScape, at a time when he was the only god",
            "interested in developing Gielinor.",
        )

    val description14 =
        arrayOf(
            "5th Age - yr 169",
            "",
            "King Roald of Misthalin renovated a ruined town",
            "in north-west Misthalin. It was a town once known",
            "as Paddewwa, but was destroyed during the god",
            "wars and known to the humans as Ghost",
            "Town. King Roald renamed the town Edgeville.",
        )

    val description15 =
        arrayOf(
            "4th Age - yr 1777",
            "",
            "Temple records show that human settlers came",
            "to the temple on the River Salve looking for new",
            "lands in which to settle. While there were legends",
            "that the temple on the Salve was blocking great",
            "evil, they refused to heed the warnings of the",
            "then custodians of the temple, thinking them",
            "possibly behind the times or reciting old",
            "legends. The lands of Misthalin and Asgarnia",
            "weren't the safest places in the world - the",
            "human tribes of these times were having",
            "constant troubles with goblins, hobgoblins, ",
            "giants, etc., so how much worse could Morytania",
            "have been? So, human settlers passed over the",
            "Salve into Morytania. The Museum recently came",
            "into possession of this torn map showing the",
            "expansion routes of the settlers.",
        )

    val description16 =
        arrayOf(
            "1st - 2nd Ages",
            "",
            "The 1st Age is thought to have been 4,000",
            "years long. The world of RuneScape is said to",
            "have been created by the gods Saradomin, ",
            "Zamorak, and Guthix at the start of the 1st Age.",
            "However, recent evidence from a brave",
            "adventurer suggests that Zamorak was not a",
            "god at this point in time so would not have",
            "been able to create worlds. It's thought that for",
            "much of this time the gods were still in the",
            "process of forming the world and making the",
            "various lands, seas, plants, and animals. This",
            "map is our approximation of the lands at that",
            "time, based upon our existing knowledge of the",
            "world.",
        )

    val description17 =
        arrayOf(
            "5th Age - yr 12",
            "",
            "Scorpius, the early 4th Age astrologer, used",
            "this machine in the Observatory, which he",
            "designed to track the stars and predict the",
            "future, gaining dark knowledge. The plans for his",
            "machine were lost in the early 4th Age. In the",
            "early 5th Age they were rediscovered and from",
            "these plans the Observatory was restored.",
            "Since then, many have learned the ways of the",
            "astrologer.",
        )

    val description18 =
        arrayOf(
            "5th Age - yr 143",
            "",
            "The Phoenix Gang had a big fight amongst",
            "themselves and the Shield of Arrav was broken",
            "in half as a result. Shortly afterwards, some",
            "gang members decided they didn't want to be",
            "part of the Phoenix gang anymore, so split off",
            "to form the Black Arm Gang. On their way out",
            "they looted what treasures they could, which",
            "included one of the halves of the shield. The",
            "phoenix and Black Arm gangs became bitter",
            "rivals, a rivalry that lasts to this day.",
        )

    val description19 =
        arrayOf(
            "4th Age - 2,000 yrs long",
            "",
            "Finds indicate that at the beginning of the 4th",
            "Age, the humans who had survived the god wars",
            "formed nomadic tribes that battled for survival",
            "against not only each other, but also the",
            "dwarves, goblins, ogres, gnomes and many more",
            "races that were competing for land and",
            "resources. Over time, they started to make more",
            "permanent settlements throughout the world, ",
            "but they continued to battle with their",
            "neighbours.",
        )

    val description20 =
        arrayOf(
            "4th Age - yr 1-100",
            "",
            "Around the beginning of the 4th Age, a new",
            "terror was seen in the world: the dragonkin.",
            "Without active gods, the people had to deal with",
            "this problem largely by themselves. Heroes",
            "arose to step up to this challenge, one of them",
            "being Robert the Strong, who helped drive the",
            "dragonkin back to their stronghold where, as far",
            "as anyone knows, they sit and brood to this",
            "very day.",
        )

    val description21 =
        arrayOf(
            "5th Age - currently 169 years old",
            "",
            "In the first years of the 5th Age, human mages",
            "discovered the rune essence rocks, but kept",
            "their locations a closely guarded secret between",
            "a select few of them, so as not to let",
            "the information fall into enemy hands. They used",
            "the power of the essence and the other altars",
            "they knew about to create various runes. Due to",
            "the power of the runes humans started to",
            "become more dominant within the world. The human",
            "kingdoms of Misthalin and Asgarnia quickly grew",
            "to become the RuneScape kingdoms and cities",
            "we know today. The mages who discovered the",
            "rune essence rock (modelled here) set up a",
            "great tower of wizardry in southern Misthalin.",
        )

    val description23 =
        arrayOf(
            "4th Age - yr 1100-1200",
            "",
            "A tide of evil creatures from the east",
            "threatened Avarrocka and border skirmishes",
            "were seen between them and the human",
            "settlers. Seven priestly warriors- ",
            "Iriandul Caistlyn, Sarl Dunegun, Friar Twiblick, Derygull, ",
            "Ivandis Seergaze, Erysail the Pious, and",
            "Essiandar Gar - attempted to drive the evil",
            "creatures back into what we now know as",
            "Morytania. Saradomin blessed the River Salve, ",
            "making it impassable to the foul things lurking in",
            "the swamps, and the brave priests were buried",
            "in the temple above the river.",
        )

    val description24A =
        arrayOf(
            "4th Age - yr 700-800",
            "",
            "Arrav is probably the best known hero from the",
            "4th Age. In his youth, he was found by a tribe",
            "that took him to be a good omen and set up a",
            "camp which they called Avarrocka. That camp, in",
            "later years, became known as our glorious city",
            "of Varrock. Many legends are told of Arrav's",
            "heroics later in his life.",
            "",
            "The shield you see here actually belonged to Arrav. For a",
            "time this priceless exhibit was lost to us, when",
            "a bunch of thieves called the Phoenix Gang",
            "broke into the Varrock Museum and stole it. We",
            "thank the daring adventurer who recently",
            "returned the shield to us. The shield has magical",
            "properties and was recently used to destroy a",
            "horde of zombies that invaded Varrock.",
        )

    val description24B =
        arrayOf(
            "The shield has been removed for",
            "security reasons. So, if you are a hopeful",
            "marauding zombie, the museum exit is on",
            "the ground floor.",
        )

    val description25 =
        arrayOf(
            "5th Age - yr 20",
            "",
            "A striking example of early 5th Age weapon-",
            "smithing, this steel sword has been magically",
            "treated to make it especially powerful against",
            "demons. It originally belonged to a warrior who",
            "defended Varrock from the demon Delrith, and",
            "was recently used to drive off the very same",
            "demon once again. This is a replica; the original",
            "Silverlight is in the hands of a private collector.",
        )

    val description26 =
        arrayOf(
            "5th Age - yr 154",
            "",
            "A strange undead necromancer leads an army of",
            "undead out of the Wilderness in an attack upon",
            "Varrock. Misthalin is strong enough and still has",
            "enough runes that the attack is defeated fairly",
            "quickly.",
        )

    val description27 =
        arrayOf(
            "The Layers of Archaeology",
            "Soil Layers",
            "",
            "O horizon - The top layer of soil is made up",
            "mostly of leaves and decomposed organic",
            "matter.",
            "",
            "A horizon (topsoil) - Plants grow in this dark- ",
            "coloured layer, which is made up of decomposed",
            "organic matter mixed with mineral particles.",
            "",
            "E horizon - This eluviation (leaching) layer has a",
            "light colour and is made up of sand and silt. We",
            "often find significant archaeological artefacts",
            "in this layer.",
            "",
            "B horizon (subsoil) - Contains clay and mineral",
            "deposits it receives from layers above, ",
            "when water drips through.",
            "",
            "C horizon - Regolith consists of slightly broken",
            "up bedrock. Plant roots do not penetrate into",
            "this layer.",
            "",
            "R horizon - The bedrock layer that is beneath all",
            "the other layers.",
        )

    val description28 =
        arrayOf(
            "End of 2nd Age",
            "",
            "This is a replica of the Staff of Armadyl, made",
            "from the descriptions given to us by a brave",
            "adventurer. It was this staff, wielded by",
            "Zamorak, which killed the god Zaros and cursed",
            "all those who aided this treacherous deed. It",
            "would seem that it was around this time that",
            "Zamorak ascended to godhood. Not much is",
            "known of what happened to the staff",
            "afterwards, until it's recent discovery. Armadyl",
            "is said to be a god whom was worshipped during",
            "the 2nd and 3rd Ages, though he does not seem",
            "to have much of a following in modern times.",
        )

    val description29 =
        arrayOf(
            "4th Age - yr 31-60",
            "",
            "A fine example of an incredibly old star chart.",
            "Not much is known about it, but the positions of",
            "the stars indicate that it was made around the",
            "years of 31–60 of the 4th Age.",
        )

    val description30 =
        arrayOf(
            "3rd age - yr 3000-4000",
            "",
            "This statuette was found in an underground",
            "temple in the ruined city of Uzer, which was",
            "destroyed late in the 3rd Age, suddenly, due to",
            "causes unknown. It probably represents one of",
            "the clay golems that the craftsmen of the city",
            "built as warriors and servants. The statuette",
            "was originally part of a mechanism whose",
            "purpose is unknown.",
        )

    val description31 =
        arrayOf(
            "3rd Age - 4,000 years long",
            "",
            "The scorched earth of the Wilderness is a",
            "lasting reminder of the destruction wrought",
            "during the God Wars. The little evidence found",
            "intact from these times would indicate that most",
            "of the mortal races only just survived this",
            "onslaught. Stories handed down over",
            "generations tell of great, powerful entities and",
            "agents of the gods fighting cataclysmic wars.",
            "However, at the end of this 4,000 year long 3rd",
            "Age, it seems that Saradomin, Guthix, and",
            "Zamorak settled for a less direct influence on our",
            "world.",
        )

    val description32 =
        arrayOf(
            "5th Age - yr 23",
            "",
            "An evil vampyre lord started to take control",
            "of northern Morytania and his minions visited",
            "the various human groups in the area",
            "demanding blood tithes, causing widespread",
            "panic. Werewolves founded a settlement near to",
            "the temple. From the few survivors, we learned",
            "that most of the human inhabitants eventually",
            "succumbed and paid the blood tithe imposed by",
            "the vampyres. Only the inhabitants of Castle",
            "Fenkenstrain stood up to this evil influence.",
        )

    val description33 =
        arrayOf(
            "5th Age - yr 162-163",
            "",
            "The old king of Asgarnia, King Vallance, falls",
            "very ill. The White Knights took advantage of",
            "this and began to take control of Falador and",
            "Asgarnia for themselves.",
            "",
            "Year 163, 5th Age - The White Knights declared",
            "that the Black knights were no longer to have",
            "any political power within Asgarnia. Lord Milton",
            "of the Kinshra immediately responds by",
            "declaring his Black Knights as being at a state",
            "of war with Falador. The two sides engaged in a",
            "big battle to the north of Falador - however, ",
            "both sides were fairly evenly matched, and",
            "retreated to their fortresses to build up their",
            "forces and devise plans to crush the other, ",
            "once and for all.",
        )

    val description34 =
        arrayOf(
            "4th Age - yr 500-900",
            "",
            "An early 4th Age map showing the human",
            "civilisations starting to settle into more",
            "permanent villages. After the god wars, all the",
            "the races began to rebuild their settlements.",
        )

    val description35 =
        arrayOf(
            "5th Age - yr 70",
            "",
            "The mages let Zamorakian followers, skilled in",
            "the way of Magic, join their numbers in the 70th",
            "year of the 5th Age. This proved to have been a",
            "disaster when the dark mages betrayed them",
            "and burnt down the tower of magic south of what",
            "we now know as Draynor Village. The location of",
            "the rune essence and the runecrafting altars",
            "was consequently lost. As a result the further",
            "campaigns of the barbarians never happened.",
        )

    val description36 =
        arrayOf(
            "Thought to be an altar decoration, possibly",
            "celebrating the beginning of the 4th Age when",
            "Saranthium was its most vibrant. Lovingly",
            "crafted in silver and sapphire, it is still in good",
            "condition.",
        )

    val description37 =
        arrayOf(
            "This small symbol was used as a decoration on",
            "the Saradominist altars throughout the city. Two",
            "or three examples have been found, but this is",
            "the best. Worked in blue - enamelled bronze, it has",
            "not survived the ages well. We believe this was to be",
            "from the early years when the city was still being",
            "built.",
        )

    val description38 =
        arrayOf(
            "One of the few vases found in good condition.",
            "Its markings show some kind of celebration to",
            "Saradomin.",
        )

    val description39A =
        arrayOf(
            "Two beautiful examples of the many items",
            "of pottery found around the Dig Site. There appear",
            "to be Saradominist markings upon these artefacts, ",
            "indicating that this was at one time a",
            "Saradominist city. This pottery appears to",
            "be dated around the beginning of the 4th Age.",
        )

    val description39B =
        arrayOf(
            "Two distinct types of pottery have been",
            "recovered from the Dig Site, adding evidence to",
            "the two settlements on this site. The red clay",
            "pottery appears to be much older and",
            "occasionally has purple Zarosian symbols, while",
            "the Saradominist artefacts are usually of a",
            "lighter shade with blue decorations.",
        )

    val description40 =
        arrayOf(
            "Fine silver and gold jewellery has been found",
            "concealed in one of the many urns scattered",
            "around the Dig Site. Not much is know about it, ",
            "although most of the items do have",
            "Saradominist markings.",
        )

    val description41 =
        arrayOf(
            "Arrowheads of crude bronze have been found",
            "along with the finds deeper in the Dig Site, which",
            "leads us to believe that the forces occupying the",
            "city before the Saradominists used bows, as well",
            "as other methods of war.",
        )

    val description42 =
        arrayOf(
            "Found at the Dig Site east of Varrock by a",
            "recently qualified archaeologist. It appears to be ",
            "a symbol of the god, Zaros.",
        )

    val description43 =
        arrayOf(
            "Excavated recently from a cavernous temple",
            "below the Dig Site by a recently qualified",
            "archaeologist. It is of great significance, as it",
            "proves the existence of a settlement that",
            "pre-dates the Saradominist city currently being",
            "excavated. The temple appears to be dedicated",
            "to the pagan god, Zaros, and survived the",
            "destruction of the Zarosian city which was",
            "rebuilt by Saradominsts.",
        )

    val description44 =
        arrayOf(
            "A battered and bent coin with Zamorakian",
            "markings. It bears the word 'Senntisten', which",
            "we believe to be the original name of the city, ",
            "Saranthium, before it was rebuilt by those loyal",
            "to Saradomin. Partial numbers on the coin would",
            "indicate that it is from the year 3740, presumably",
            "from the 3rd Age.",
        )

    val description45 =
        arrayOf(
            "A coin in very good condition with Saradominist",
            "markings. It bears the word 'Saranthium', which we",
            "have found to be the name of the city being excavated east of",
            "Varrock. The numbers on the coin would indicate",
            "that it is from the year 3804, presumably from the",
            "3rd Age as the Godwars were coming to an end.",
        )

    val description46 =
        arrayOf(
            "5th Age - yr 98",
            "",
            "One of the survivors of the great battle became",
            "king in year 98 of the 5th Age. His goal was to",
            "listen to the people's views and ensure a fair and",
            "equal life for everyone. He was the first of a line",
            "of kings in Ardougne that continues to this day",
            "under his great grandson, King Lathas of",
            "Ardougne, painted here. The other survivor of",
            "the battle founded the marketplace, allowing",
            "people to trade their skills and wares under",
            "equal rights and opportunities. More information",
            "is available at a small museum north of",
            "Ardougne.",
        )

    val description47 =
        arrayOf(
            "5th Age - yr 136",
            "",
            "King Ulthas of Ardougne dies from an accidental",
            "arrow shot while out on a hunting expedition.",
            "Ardougne is left to his 2 sons, Tyras and Lathas",
            "who decide to split the city between them.",
        )

    val removedForCleaning =
        arrayOf(
            "Item removed for cleaning.",
        )

    override fun defineInterfaceListeners() {
        onOpen(Components.VM_TIMELINE_534) { player, _ ->
            val model = player.getAttribute(interfaceModel, 0)
            player.packetDispatch.sendModelOnInterface(model, Components.VM_TIMELINE_534, 4, 461)
            return@onOpen true
        }

        onClose(Components.VM_TIMELINE_534) { player, _ ->
            removeAttribute(player, interfaceModel)
            removeAttribute(player, smallInterfaceModel)
            return@onClose true
        }

        onOpen(Components.VM_DIGSITE_528) { player, _ ->
            return@onOpen true
        }
    }

    override fun defineListeners() {
        val displayCases =
            mapOf(
                // Barbarian Village map.
                Scenery.DISPLAY_CASE_24645 to description1,
                // Barbarian weapons.
                Scenery.DISPLAY_CASE_24650 to description2,
                // Dagon'hai tome.
                Scenery.DISPLAY_CASE_24663 to description3,
                // Black Knight armour.
                Scenery.DISPLAY_CASE_24647 to description4,
                // Weapons and runes.
                Scenery.DISPLAY_CASE_24651 to description5,
                // Bridge diorama.
                Scenery.DISPLAY_CASE_24641 to description6,
                // Broken arrows.
                Scenery.DISPLAY_CASE_24655 to description7,
                // Stone circle.
                Scenery.DISPLAY_CASE_24629 to description8,
                // Excalibur.
                Scenery.DISPLAY_CASE_24654 to description9,
                // Gnome glider.
                Scenery.DISPLAY_CASE_24660 to description10,
                // Infinity equipment.
                Scenery.DISPLAY_CASE_24648 to description11,
                // Guardian.
                Scenery.DISPLAY_CASE_24659 to description12,
                // Stone tablet.
                Scenery.DISPLAY_CASE_38173 to description13,
                // Edgeville map.
                Scenery.DISPLAY_CASE_24646 to description14,
                // Morytania map.
                Scenery.DISPLAY_CASE_24640 to description15,
                // Gielinor map.
                Scenery.DISPLAY_CASE_24622 to description16,
                // Observatory model.
                Scenery.DISPLAY_CASE_24643 to description17,
                // Phoenix crossbow.
                Scenery.DISPLAY_CASE_24656 to description18,
                // Archaeological finds.
                Scenery.DISPLAY_CASE_24628 to description19,
                // Robert the Strong.
                Scenery.DISPLAY_CASE_24634 to description20,
                // Rune essence rock.
                Scenery.DISPLAY_CASE_24631 to description21,
                // Priestly warrior helms.
                Scenery.DISPLAY_CASE_24637 to description23,
                // Silverlight.
                Scenery.DISPLAY_CASE_24649 to description25,
                // Skeleton.
                Scenery.DISPLAY_CASE_24657 to description26,
                // Soil layers.
                Scenery.DISPLAY_CASE_24621 to description27,
                // Staff of Armadyl.
                Scenery.DISPLAY_CASE_24624 to description28,
                // Star chart.
                Scenery.DISPLAY_CASE_24635 to description29,
                // Golem statuette.
                Scenery.DISPLAY_CASE_24627 to description30,
                // God symbols.
                Scenery.DISPLAY_CASE_24625 to description31,
                // Werewolf.
                Scenery.DISPLAY_CASE_24644 to description32,
                // White Knight armour.
                Scenery.DISPLAY_CASE_24658 to description33,
                // World map.
                Scenery.DISPLAY_CASE_24632 to description34,
                // Zamorakian symbol.
                Scenery.DISPLAY_CASE_24652 to description35,
                // Old symbol.
                Scenery.DISPLAY_CASE_24539 to description36,
                // Ancient symbol.
                Scenery.DISPLAY_CASE_24540 to description37,
                // Saradominist vase.
                Scenery.DISPLAY_CASE_24541 to description38,
                // Pottery A.
                Scenery.DISPLAY_CASE_24542 to description39A,
                // Pottery B | 95 kudos requirements.
                Scenery.DISPLAY_CASE_24543 to description39B,
                // Jewellery.
                Scenery.DISPLAY_CASE_24544 to description40,
                // Arrowheads.
                Scenery.DISPLAY_CASE_24545 to description41,
                // Ancient talisman.
                Scenery.DISPLAY_CASE_24546 to description42,
                // Zaros tablet.
                Scenery.DISPLAY_CASE_24547 to description43,
                // Ancient coin.
                Scenery.DISPLAY_CASE_24548 to description44,
                // Old coin.
                Scenery.DISPLAY_CASE_24549 to description45,
                // King Lathas painting.
                Scenery.PAINTING_24620 to description46,
                // Ardougne map.
                Scenery.DISPLAY_CASE_24661 to description47,
            )

        displayCases.forEach { (scenery, description) ->
            on(scenery, IntType.SCENERY, "study") { player, node ->
                val n = node as core.game.node.scenery.Scenery
                val model = n.definition.modelIds!![0]
                val c = displayCases.keys.indexOf(scenery) + 1

                setAttribute(player, interfaceModel, model)
                openInterface(player, Components.VM_TIMELINE_534)

                sendString(player, description.joinToString("<br>"), Components.VM_TIMELINE_534, 2)
                sendString(player, c.toString(), Components.VM_TIMELINE_534, 85)

                return@on true
            }
        }

        /*
         * Handles study the display case (number 30).
         */

        on(Scenery.DISPLAY_CASE_24550, IntType.SCENERY, "study") { player, node ->
            val n = node as core.game.node.scenery.Scenery
            val model = n.definition.modelIds!![0]
            setAttribute(player, interfaceModel, model)
            openInterface(player, Components.VM_TIMELINE_534)
            sendString(player, removedForCleaning.toString(), Components.VM_TIMELINE_534, 2)
            return@on true
        }

        on(Scenery.DISPLAY_CASE_24627, IntType.SCENERY, "open") { player, _ ->
            if (!player.inventory.containsAtLeastOneItem(Items.DISPLAY_CABINET_KEY_4617)) {
                sendMessage(player, "The cabinet is locked.")
                return@on true
            }
            if (inInventory(player, Items.STATUETTE_4618) ||
                inBank(player, Items.STATUETTE_4618) ||
                player.getAttribute("the-golem:placed-statuette", false)
            ) {
                sendMessage(player, "You have already taken the statuette.")
                return@on true
            }
            addItemOrDrop(player, Items.STATUETTE_4618, 1)
            sendItemDialogue(player, Items.STATUETTE_4618, "You open the cabinet and retrieve the statuette.")
            TheGolemListeners.updateVarps(player)
            return@on true
        }

        /*
         * Handles specimen cleaning items used on display cases.
         */

        onUseWith(IntType.SCENERY, displayItems, Scenery.DISPLAY_CASE_24550) { player, used, with ->
            when (with.location) {
                Location.create(3262, 3448, 0) ->
                    if (used.id == Items.OLD_SYMBOL_11182) {
                        removeItem(player, used.asItem(), Container.INVENTORY)
                        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_37_3646, 1, true)
                    }
                Location.create(3255, 3453, 1) ->
                    if (used.id == Items.DISPLAY_CABINET_KEY_4617) {
                        sendMessage(player, "You have already taken the statuette.")
                    }
                Location.create(3259, 3448, 0) ->
                    if (used.id == Items.OLD_COIN_11179) {
                        removeItem(player, used.asItem(), Container.INVENTORY)
                        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_45_3648, 1, true)
                    }
                Location.create(3259, 3450, 0) ->
                    if (used.id == Items.ANCIENT_COIN_11180) {
                        removeItem(player, used.asItem(), Container.INVENTORY)
                        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_44_3647, 1, true)
                    }
                Location.create(3262, 3450, 0) ->
                    if (used.id == Items.ANCIENT_SYMBOL_11181) {
                        removeItem(player, used.asItem(), Container.INVENTORY)
                        setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_36_3645, 1, true)
                    }
            }
            if (used.id != 4617) {
                sendMessages(
                    player,
                    "You carefully place the ${getItemName(used.id).lowercase()} in the display and update.",
                    "the information.",
                )
            }
            return@onUseWith true
        }

        /*
         * Handles use of Pottery on display case. (number 22)
         */

        onUseWith(IntType.SCENERY, Items.POTTERY_11178, Scenery.DISPLAY_CASE_24543) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                setVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_39_3649, 1, true)
            }
            return@onUseWith true
        }

        /*
         * Handles study the display case (number 24).
         */

        on(
            intArrayOf(Scenery.DISPLAY_CASE_24639, Scenery.DISPLAY_CASE_24551),
            IntType.SCENERY,
            "study",
        ) { player, node ->
            val n = node as core.game.node.scenery.Scenery
            val model = n.definition.modelIds!![0]
            val arrav = getVarbit(player, Vars.VARBIT_SCENERY_MUSEUM_DISPLAY_24_5394)
            setAttribute(player, interfaceModel, model)
            openInterface(player, Components.VM_TIMELINE_534)
            sendString(
                player,
                if (arrav == 1) {
                    description24A.joinToString("<br>")
                } else {
                    description24B.joinToString("<br>")
                },
                Components.VM_TIMELINE_534,
                2,
            )
            sendString(player, "24", Components.VM_TIMELINE_534, 85)
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        /*
         * Handles study the King Lathas painting.
         */
        setDest(IntType.SCENERY, intArrayOf(Scenery.PAINTING_24620), "study") { _, _ ->
            return@setDest Location.create(3257, 3454, 2)
        }
    }
}
