package core.api.interaction

import core.cache.def.impl.SceneryDefinition

/**
 * Retrieves the name of a scenery id.
 *
 * @param id The id for the scenery.
 * @return The name.
 */
fun getSceneryName(id: Int): String {
    return SceneryDefinition.forId(id).name
}

private class SceneryInteractionAPI
