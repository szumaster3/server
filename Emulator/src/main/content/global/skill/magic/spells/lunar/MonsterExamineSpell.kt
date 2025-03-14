package content.global.skill.magic.spells.lunar

import content.global.skill.magic.SpellListener
import content.global.skill.magic.spells.LunarSpells
import core.api.faceLocation
import core.api.sendMessage
import core.api.sendString
import core.api.ui.openSingleTab
import core.game.node.item.Item
import core.game.system.config.NPCConfigParser
import org.rs.consts.*

class MonsterExamineSpell : SpellListener("lunar") {
    override fun defineListeners() {
        onCast(LunarSpells.MONSTER_EXAMINE, NPC) { player, node ->
            val npc = node!!.asNpc()
            requires(
                player,
                66,
                arrayOf(Item(Items.ASTRAL_RUNE_9075), Item(Items.MIND_RUNE_558), Item(Items.COSMIC_RUNE_564)),
            )
            if (!npc.location.withinDistance(player.location)) {
                sendMessage(player, "You must get closer to use this spell.")
                return@onCast
            }

            faceLocation(player, npc.location)
            visualizeSpell(
                player,
                Animations.LUNAR_MONSTER_EXAMINE_6293,
                Graphics.STAT_SPY_GFX_1060,
                soundID = Sounds.LUNAR_STAT_SPY_3620,
            )
            removeRunes(player)
            addXP(player, 66.0)
            setDelay(player, false)

            openSingleTab(player, Components.DREAM_MONSTER_STAT_522)
            sendString(player, "Monster name : " + npc.definition.name, Components.DREAM_MONSTER_STAT_522, 0)
            sendString(player, "Combat Level : ${npc.definition.combatLevel}", Components.DREAM_MONSTER_STAT_522, 1)
            sendString(
                player,
                "Hitpoints : ${npc.definition.handlers[NPCConfigParser.LIFEPOINTS] ?: 0}",
                Components.DREAM_MONSTER_STAT_522,
                2,
            )
            sendString(
                player,
                "Max hit : ${npc.getSwingHandler(false).calculateHit(npc, player, 1.0)}",
                Components.DREAM_MONSTER_STAT_522,
                3,
            )

            val poisonStatus =
                if (npc.definition.handlers.getOrDefault(NPCConfigParser.POISON_IMMUNE, false) == true) {
                    "This creature is immune to poison."
                } else {
                    "This creature is not immune to poison."
                }

            sendString(player, poisonStatus, Components.DREAM_MONSTER_STAT_522, 4)
        }
    }
}
