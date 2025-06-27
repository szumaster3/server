package core.game.dialogue

/**
 * Dialogue topic with animation, text, next stage, and optional player skip.
 *
 * @param T Type of the next stage.
 * @property expr Facial animation expression.
 * @property text Dialogue text.
 * @property toStage Next dialogue stage.
 * @property skipPlayer Whether to skip the player (default false).
 */
open class Topic<T>
@JvmOverloads
constructor(
    val expr: FaceAnim,
    val text: String,
    val toStage: T,
    val skipPlayer: Boolean = false,
) {
    /**
     * Defaults [expr] to [FaceAnim.ASKING].
     */
    @JvmOverloads
    constructor(text: String, toStage: T, skipPlayer: Boolean = false) : this(
        FaceAnim.ASKING, text, toStage, skipPlayer
    )
}

/**
 * Conditional dialogue topic shown based on [showCondition].
 *
 * @param T Type of the next stage.
 * @property expr Facial animation expression.
 * @property text Dialogue text.
 * @property toStage Next dialogue stage.
 * @property showCondition Condition to show this topic.
 * @property skipPlayer Whether to skip the player (default false).
 */
class IfTopic<T>
@JvmOverloads
constructor(
    expr: FaceAnim,
    text: String,
    toStage: T,
    val showCondition: Boolean,
    skipPlayer: Boolean = false,
) : Topic<T>(expr, text, toStage, skipPlayer) {
    /**
     * Defaults [expr] to [FaceAnim.ASKING].
     */
    @JvmOverloads
    constructor(text: String, toStage: T, showCondition: Boolean, skipPlayer: Boolean = false) : this(
        FaceAnim.ASKING, text, toStage, showCondition, skipPlayer
    )
}
