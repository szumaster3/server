package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import core.api.event.cureDisease
import core.api.event.curePoison
import core.api.event.isDiseased
import core.api.event.isPoisoned
import core.api.getStatLevel
import core.api.playAudio
import core.api.sendMessage
import core.api.visualize
import core.cache.def.impl.NPCDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.combat.equipment.WeaponInterface
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.ClassScanner.definePlugin
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

@Initializable
class UnicornStallionNPC(
    owner: Player? = null,
    id: Int = NPCs.UNICORN_STALLION_6822,
) : Familiar(owner, id, 5400, Items.UNICORN_STALLION_POUCH_12039, 20, WeaponInterface.STYLE_CONTROLLED) {
    override fun construct(
        owner: Player,
        id: Int,
    ): Familiar {
        return UnicornStallionNPC(owner, id)
    }

    override fun specialMove(special: FamiliarSpecial): Boolean {
        val player = special.node as Player
        playAudio(player, Sounds.HEALING_AURA_4372)
        visualize(Animation.create(8267), Graphics.create(org.rs.consts.Graphics.ELECTRIC_BALL_OVER_HEAD_1356))
        player.getSkills().heal((player.getSkills().maximumLifepoints * 0.15).toInt())
        return true
    }

    override fun configureFamiliar() {
        definePlugin(
            object : OptionHandler() {
                override fun newInstance(arg: Any?): Plugin<Any> {
                    NPCDefinition.forId(NPCs.UNICORN_STALLION_6822).handlers["option:cure"] = this
                    NPCDefinition.forId(NPCs.UNICORN_STALLION_6823).handlers["option:cure"] = this
                    return this
                }

                override fun handle(
                    player: Player,
                    node: Node,
                    option: String,
                ): Boolean {
                    val familiar = node as Familiar
                    if (!player.familiarManager.isOwner(familiar)) {
                        return true
                    }
                    if (getStatLevel(player, Skills.MAGIC) < 2) {
                        sendMessage(player, "You don't have enough summoning points left")
                        return true
                    }
                    if (!isPoisoned(player)) {
                        sendMessage(player, "You are not poisoned.")
                        return true
                    }
                    if (!isDiseased(player)) {
                        sendMessage(player, "You are not diseased.")
                        return true
                    }

                    playAudio(player, Sounds.HEALING_AURA_4372)
                    familiar.visualize(
                        Animation.create(8267),
                        Graphics.create(org.rs.consts.Graphics.ELECTRIC_BALL_OVER_HEAD_1356),
                    )
                    curePoison(player)
                    cureDisease(player)
                    player.getSkills().updateLevel(Skills.SUMMONING, -2, 0)
                    return true
                }
            },
        )
    }

    override fun visualizeSpecialMove() {
        visualize(
            owner,
            Animation.create(Animations.CAST_FAMILIAR_SCROLL_7660),
            Graphics.create(org.rs.consts.Graphics.WHITE_FAMILIAR_GRAPHIC_1298),
        )
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.UNICORN_STALLION_6822, NPCs.UNICORN_STALLION_6823)
    }
}
