package core.game.world.update.flag

import core.ServerConstants
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * Handles registration and lookup of [EFlagProvider] instances by revision, entity type, and flag.
 */
object EntityFlags {
    /**
     * Map of tokens to flag providers.
     */
    private var flagProviders: HashMap<Int, EFlagProvider> = HashMap()

    init {
        registerFlagProviders(PlayerFlags::class)
        registerFlagProviders(NPCFlags::class)
    }

    /**
     * Registers all [EFlagProvider] subclasses from the given sealed class.
     */
    private fun registerFlagProviders(parent: KClass<*>) {
        val clazzes = parent.sealedSubclasses
        for (clazz in clazzes) {
            val p = clazz.primaryConstructor?.call() as? EFlagProvider ?: continue
            flagProviders[getMapToken(p.revision, p.type, p.flag)] = p
        }
    }

    /**
     * Gets the flag provider for the given entity [type] and [flag] (revision 530).
     */
    fun getFlagFor(type: EFlagType, flag: EntityFlag): EFlagProvider? {
        val revision = ServerConstants.REVISION
        return flagProviders[getMapToken(revision, type, flag)]
    }

    /**
     * Gets the ordinal of the flag, or -1 if missing.
     */
    @JvmStatic
    fun getOrdinal(type: EFlagType, flag: EntityFlag): Int = getFlagFor(type, flag)?.ordinal ?: -1

    /**
     * Gets the presence flag bitmask, or 0 if missing.
     */
    @JvmStatic
    fun getPresenceFlag(type: EFlagType, flag: EntityFlag): Int = getFlagFor(type, flag)?.presenceFlag ?: 0

    /**
     * Builds a unique token for revision, type, and flag.
     */
    private fun getMapToken(revision: Int, type: EFlagType, flag: EntityFlag): Int =
        (revision shl 8) + (type.ordinal shl 4) + flag.ordinal
}
