import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.lang.Exception

object DataStore {
    val rootPath = File(System.getProperty("user.dir"))
    val saveFile = File(rootPath, "settings.json")

    var LastConfigPath: String = "server\\data\\configs"

    fun parse() {
        if (!saveFile.exists()) return

        try {
            val reader = FileReader(saveFile)
            val parser = JSONParser().parse(reader) as JSONObject
            if (parser.containsKey("lastConfigPath")) {
                val relativePath = parser["lastConfigPath"].toString()
                LastConfigPath = File(rootPath, relativePath).absolutePath
            }
        } catch (ignored: Exception) {}
    }

    fun save() {
        val obj = JSONObject()
        val relative = File(LastConfigPath).toRelativeString(rootPath)
        obj["lastConfigPath"] = relative

        try {
            FileWriter(saveFile).use {
                it.write(obj.toJSONString())
                it.flush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
