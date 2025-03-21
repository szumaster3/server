package core.game.dialogue

/**
 * Represents a dialogue topic, which consists of an animation expression, text,
 * and the next stage to transition to after the topic is handled.
 *
 * @param T The type of the next stage to transition to.
 * @property expr The facial animation expression to show during the dialogue.
 * @property text The text that is displayed during the dialogue.
 * @property toStage The next stage of the dialogue to transition to after this topic is handled.
 * @property skipPlayer A flag indicating whether the player should be skipped (default is false).
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
     * Secondary constructor that defaults the facial expression to [FaceAnim.ASKING] if not specified.
     *
     * @param text The text to display during the dialogue.
     * @param toStage The next stage to transition to after this topic.
     * @param skipPlayer A flag indicating whether the player should be skipped (default is false).
     */
    @JvmOverloads
    constructor(text: String, toStage: T, skipPlayer: Boolean = false) : this(
        expr = FaceAnim.ASKING,
        text = text,
        toStage = toStage,
        skipPlayer = skipPlayer,
    )
}

/**
 * Represents a conditional dialogue topic, which consists of an animation expression,
 * text, the next stage to transition to, and a condition determining if the topic should be shown.
 *
 * @param T The type of the next stage to transition to.
 * @property expr The facial animation expression to show during the dialogue.
 * @property text The text that is displayed during the dialogue.
 * @property toStage The next stage of the dialogue to transition to after this topic is handled.
 * @property showCondition A flag indicating whether this topic should be shown based on a condition.
 * @property skipPlayer A flag indicating whether the player should be skipped (default is false).
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
     * Secondary constructor that defaults the facial expression to [FaceAnim.ASKING] if not specified.
     *
     * @param text The text to display during the dialogue.
     * @param toStage The next stage to transition to after this topic.
     * @param showCondition A flag indicating whether this topic should be shown based on a condition.
     * @param skipPlayer A flag indicating whether the player should be skipped (default is false).
     */
    @JvmOverloads
    constructor(text: String, toStage: T, showCondition: Boolean, skipPlayer: Boolean = false) : this(
        expr = FaceAnim.ASKING,
        text = text,
        toStage = toStage,
        showCondition = showCondition,
        skipPlayer = skipPlayer,
    )
}
