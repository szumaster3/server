package core.game.node.entity.player.info.login

import core.ServerConstants
import core.game.node.entity.player.Player
import java.io.FileInputStream
import java.io.FileOutputStream

object PlayerParser {
    @JvmStatic
    fun parse(player: Player): PlayerSaveParser? {
        return try {
            PlayerSaveParser(player).apply { parse() }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @JvmStatic
    fun save(player: Player) {
        player.setAttribute("flagged-for-save", true)
    }

    @JvmStatic
    fun saveImmediately(player: Player) {
        PlayerSaver(player).save()
    }

    @JvmStatic
    fun makeFromTemplate(player: Player) {
        val templatePath = "${ServerConstants.PLAYER_SAVE_PATH}template/template.json"
        val playerSavePath = "${ServerConstants.PLAYER_SAVE_PATH}${player.name}.json"

        try {
            FileInputStream(templatePath).use { input ->
                FileOutputStream(playerSavePath).use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
