package content.region.misthalin.handlers.surok

import core.api.getVarbit
import core.game.activity.ActivityPlugin
import core.game.activity.CutscenePlugin
import core.game.dialogue.Dialogue
import core.game.node.entity.player.Player
import core.game.world.map.Location
import core.game.world.map.build.DynamicRegion
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Music
import org.rs.consts.NPCs

@Initializable
class SurokMagisDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc(player.username + "! The meddling adventurer.")
        if (getVarbit(player, 3524) >= 1) {
            player.musicPlayer.unlock(Music.THE_WRONG_PATH_488)
        }
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("Surok! What are you doing here?", "How did you-").also { stage++ }
            1 -> npc("Escape from Varrock Palace Library? That cruel", "imprisonment you left me in?").also { stage++ }
            2 -> player("Well...er..yes.").also { stage++ }
            3 ->
                npc(
                    "Bah! A mere trifle for a powerful mage such as myself.",
                    "There were plenty of other foolish people to help with",
                    "my plans, you would do well to stay out of my way.",
                ).also { stage++ }

            4 ->
                player(
                    "Stop, Surok! As a member of the Varrock Palace Secret",
                    "Guard, I arrest you! Again!",
                ).also { stage++ }

            5 -> npc("Ha! I tire of this meaningless drivel. Catch me if you can.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SUROK_MAGIS_7136)
    }

    class SurokCutscene() : CutscenePlugin("Surok Cutscene") {
        var scene: SurokScene? = null

        constructor(player: Player?) : this() {
            this.player = player
        }

        override fun start(
            player: Player,
            login: Boolean,
            vararg args: Any,
        ): Boolean {
            scene = args[0] as SurokScene
            region = DynamicRegion.create(scene!!.regionId)
            setRegionBase()
            registerRegion(region.id)
            return super.start(player, login, *args)
        }

        override fun newInstance(p: Player): ActivityPlugin {
            return SurokCutscene(p)
        }

        override fun getStartLocation(): Location {
            return base.transform(scene!!.startData[0], scene!!.startData[1], 0)
        }

        override fun getSpawnLocation(): Location? {
            return null
        }

        override fun configure() {}

        enum class SurokScene(
            val regionId: Int,
            val startData: IntArray,
        ) {
            ESCAPE(-1, intArrayOf()),
        }
    }
}
