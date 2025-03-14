package core.game.system.task

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object TaskExecutor {
    @JvmStatic
    fun executeSQL(task: () -> Unit) {
        GlobalScope.launch {
            task.invoke()
        }
    }

    @JvmStatic
    fun execute(task: () -> Unit) {
        GlobalScope.launch {
            task.invoke()
        }
    }
}
