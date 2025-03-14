package content.region.kandarin.miniquest.zaros

import content.global.skill.summoning.familiar.BurdenBeast
import core.api.*
import core.game.node.entity.player.Player
import core.game.world.map.Location
import org.rs.consts.NPCs

object CurseOfZaros {
    private val MYSTERIOUS_NPC =
        intArrayOf(
            NPCs.MYSTERIOUS_GHOST_2397,
            NPCs.MYSTERIOUS_GHOST_2398,
            NPCs.MYSTERIOUS_GHOST_2399,
            NPCs.MYSTERIOUS_GHOST_2400,
            NPCs.MYSTERIOUS_GHOST_2401,
            NPCs.MYSTERIOUS_GHOST_2402,
        )

    private val dialogueSequence =
        mapOf(
            NPCs.MYSTERIOUS_GHOST_2397 to
                mapOf(
                    Location.create(3020, 3946, 0) to
                        "Ah, the infamous Rennard... The last I had heard of him, he had sought passage on a ship crewed by none but the most dastardly lowly pirates... I also heard that this ship had been caught in a violent storm, and stranded upon rocks, where the pirates then made their home...",
                    Location.create(3035, 3701, 0) to
                        "Ah, the infamous Rennard... The last I had heard of that vile thief, he had joined a group of bandits in an evil land to the North-east of here, where they had made their home living outside of the reach of the authorities that pursued them...",
                    Location.create(3163, 2982, 0) to
                        "Ah, the infamous Rennard... The last I had heard of that vile thief, he had joined a group of bandits in a barren land to the South-east of here, where they prey upon the unsuspecting visitors to the desert awaiting the return of their dark master...",
                ),
            NPCs.MYSTERIOUS_GHOST_2401 to
                mapOf(
                    Location.create(2851, 3348, 0) to
                        "Well, she was always sickeningly obedient to Saradomin, so I would expect her to have run to some great place of worship of him if she was affected by the curse to try and gain his blessing.",
                    Location.create(2396, 3480, 0) to
                        "Well, we knew little about her or she would have been caught and exposed as a traitor and a spy, but Lucien did mention that he had evidence that she was a great fan of ball games...",
                    Location.create(3041, 3203, 0) to
                        "Well, according to Lucien's intelligence report, she had been uncovered as a spy by her constant use of ships to ferry information... It is entirely possible she would be located somewhere coastal to this day!",
                ),
            NPCs.MYSTERIOUS_GHOST_2400 to
                mapOf(
                    Location.create(2951, 3820, 0) to
                        "Kharrim the messenger... Well, he was always a devoted follower of old General Zamorak, and if I remember rightly Zamorak set up a small base in an old temple near Dareeyak... You might want to check around there.",
                    Location.create(3069, 3859, 0) to
                        "Kharrim the messenger... The last I'd heard of that weasel he was claiming he'd found some underground deposit of runite ore guarded by demons and dragons. I suspect he was pulling some scam or other, but if you know of such a place, that might be a good place to start checking.",
                    Location.create(3217, 3676, 0) to
                        "Kharrim the messenger... Last I'd heard of him, he'd headed off to Carrallagar to seek his fortune. Ya might want to check around there somewhere.",
                ),
            NPCs.MYSTERIOUS_GHOST_2398 to
                mapOf(
                    Location.create(3053, 3378, 1) to
                        "Dhalak? I know not where, but he would try and make the most of his situation if he has been cursed, and find a place to lift his spirits!",
                    Location.create(3112, 3157, 0) to
                        "Dhalak? Well, he was always a knowledgeable mage, so if this curse has befallen him as well, I would suspect he would be researching how to free himself of it. I would look for a library to find him if I were you.",
                    Location.create(3052, 3497, 1) to
                        "Dhalak? He was always a loyal follower of Saradomin... I think he would have found an altar to Saradomin so that he may pray for this curse to be lifted.",
                ),
            NPCs.MYSTERIOUS_GHOST_2402 to
                mapOf(
                    Location.create(3124, 9993, 0) to
                        "Ah, the evil swordsman Viggora... Paddewwa was where he fought many battles, perhaps he has returned to one of his old haunts?",
                    Location.create(3295, 3934, 1) to
                        "Ah, the evil swordsman Viggora... A rogue like him would probably flock to his own kind.",
                    Location.create(3447, 3549, 1) to
                        "Ah, the evil swordsman Viggora... Perhaps he has returned to his castle in the dark lands?",
                ),
        )

    fun tagDialogue(
        player: Player,
        npcId: Int,
    ): Boolean {
        val key = "/save:dialogueTag:$npcId"
        if (getAttribute(player, key, false)) return true

        setAttribute(player, key, true)
        if (MYSTERIOUS_NPC.all { hasTag(player, it) }) {
            setAttribute(player, "/save:zaros:complete", true)
            MYSTERIOUS_NPC.forEach { removeAttribute(player, "/save:dialogueTag:$it") }
            return true
        }
        return false
    }

    fun hasTag(
        player: Player,
        npcId: Int,
    ): Boolean = getAttribute(player, "/save:dialogueTag:$npcId", false)

    fun hasComplete(player: Player): Boolean = getAttribute(player, "/save:zaros:complete", false)

    fun hasItems(
        player: Player,
        item: Int,
    ): Boolean {
        val familiar = player.familiarManager.familiar as? BurdenBeast
        return familiar?.container?.containItems(item) == true ||
            inInventory(player, item) ||
            inEquipment(player, item) ||
            inBank(player, item)
    }

    fun getLocationDialogue(
        npcId: Int,
        location: Location,
    ): Array<String>? = dialogueSequence[npcId]?.get(location)?.let { arrayOf(it) }
}
