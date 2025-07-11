package core.game.system.task

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Executes background tasks using coroutines.
 */
object TaskExecutor {

    /**
     * Executes an SQL-related task asynchronously.
     *
     * @param task The task to run.
     */
    @JvmStatic
    fun executeSQL(task: () -> Unit) {
        GlobalScope.launch {
            task.invoke()
        }
    }

    /**
     * Executes a general-purpose task asynchronously.
     *
     * @param task The task to run.
     */
    @JvmStatic
    fun execute(task: () -> Unit) {
        GlobalScope.launch {
            task.invoke()
        }
    }
}