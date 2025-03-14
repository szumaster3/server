package core.api

import core.game.container.Container
import core.game.container.ContainerEvent

interface ContainerListener {
    fun update(
        container: Container?,
        event: ContainerEvent?,
    )

    fun refresh(container: Container?)
}
