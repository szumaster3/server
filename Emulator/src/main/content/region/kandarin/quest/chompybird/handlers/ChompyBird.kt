package content.region.kandarin.quest.chompybird.handlers

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.tools.colorize
import org.rs.consts.Items

enum class ChompyBird(
    val id: Int,
    val kills: Int,
    val rankName: String,
) {
    O_BOWMAN(
        id = Items.CHOMPY_BIRD_HAT_2978,
        kills = 30,
        rankName = "an Ogre Bowman",
    ),
    BOWMAN(
        id = Items.CHOMPY_BIRD_HAT_2979,
        kills = 40,
        rankName = "a Bowman",
    ),
    O_YEOMAN(
        id = Items.CHOMPY_BIRD_HAT_2980,
        kills = 50,
        rankName = "an Ogre Yeoman",
    ),
    YEOMAN(
        id = Items.CHOMPY_BIRD_HAT_2981,
        kills = 70,
        rankName = "a Yeoman",
    ),
    O_MARKSMAN(
        id = Items.CHOMPY_BIRD_HAT_2982,
        kills = 95,
        rankName = "an Ogre Marksman",
    ),
    MARKSMAN(
        id = Items.CHOMPY_BIRD_HAT_2983,
        kills = 125,
        rankName = "a Marksman",
    ),
    O_WOODSMAN(
        id = Items.CHOMPY_BIRD_HAT_2984,
        kills = 170,
        rankName = "an Ogre Woodsman",
    ),
    WOODSMAN(
        id = Items.CHOMPY_BIRD_HAT_2985,
        kills = 225,
        rankName = "a Woodsman",
    ),
    O_FORESTER(
        id = Items.CHOMPY_BIRD_HAT_2986,
        kills = 300,
        rankName = "an Ogre Forester",
    ),
    FORESTER(
        id = Items.CHOMPY_BIRD_HAT_2987,
        kills = 400,
        rankName = "a Forester",
    ),
    O_BOWMASTER(
        id = Items.CHOMPY_BIRD_HAT_2988,
        kills = 550,
        rankName = "an Ogre Bowmaster",
    ),
    BOWMASTER(
        id = Items.CHOMPY_BIRD_HAT_2989,
        kills = 700,
        rankName = "a Bowmaster",
    ),
    O_EXPERT(
        id = Items.CHOMPY_BIRD_HAT_2990,
        kills = 1000,
        rankName = "an Ogre Expert",
    ),
    EXPERT(
        id = Items.CHOMPY_BIRD_HAT_2991,
        kills = 1300,
        rankName = "an Expert",
    ),
    O_DA(
        id = Items.CHOMPY_BIRD_HAT_2992,
        kills = 1700,
        rankName = "an Ogre Dragon Archer",
    ),
    DA(
        id = Items.CHOMPY_BIRD_HAT_2993,
        kills = 2250,
        rankName = "a Dragon Archer",
    ),
    EO_DA(
        id = Items.CHOMPY_BIRD_HAT_2994,
        kills = 3000,
        rankName = "an Expert Ogre Dragon Archer",
    ),
    E_DA(
        id = Items.CHOMPY_BIRD_HAT_2995,
        kills = 4000,
        rankName = "an Expert Dragon Archer",
    ),
    ;

    companion object {
        val killMap = values().map { it.kills to it }.toMap()

        fun checkForNewRank(player: Player) {
            val kills = getAttribute(player, "chompy-kills", 0)
            val newRank = killMap[kills] ?: return

            sendDialogueLines(
                player,
                colorize("%B*** Congratulations! $kills Chompies! ***"),
                colorize("%R~ You're ${newRank.rankName} ~"),
            )
        }

        fun getApplicableHats(player: Player): List<Int> {
            val kills = getAttribute(player, "chompy-kills", 0)
            val hats = ArrayList<Int>()
            for (hat in values()) {
                if (hat.kills > kills) break
                if (inInventory(player, hat.id) || inEquipment(player, hat.id) || inBank(player, hat.id)) continue
                hats.add(hat.id)
            }
            return hats
        }
    }
}

class ChompyEquipmentListener : InteractionListener {
    override fun defineListeners() {
        ChompyBird.values().forEach { hat ->
            onEquip(hat.id) { player, node ->
                val kills = getAttribute(player, "chompy-kills", 0)
                if (kills < hat.kills) {
                    sendItemDialogue(player, node.id, "You haven't earned this!")
                    removeItem(player, node.asItem())
                    return@onEquip false
                }
                return@onEquip true
            }
        }

        on(Items.COMP_OGRE_BOW_4827, IntType.ITEM, "check kills", "operate") { player, _ ->
            val kills = getAttribute(player, "chompy-kills", 0)
            sendDialogue(player, "You kill $kills chompy " + (if (kills == 1) "bird" else "birds") + ".")
            return@on true
        }
    }
}
