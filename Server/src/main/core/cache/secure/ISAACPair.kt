package core.cache.secure

/**
 * Represents a ISAAC key pair, for both input and output.
 *
 * @author Discardedx2
 */
class ISAACPair(
    val input: ISAACCipher,
    @JvmField val output: ISAACCipher,
)
