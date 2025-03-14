package core.game.dialogue

open class Topic<T>
    @JvmOverloads
    constructor(
        val expr: FaceAnim,
        val text: String,
        val toStage: T,
        val skipPlayer: Boolean = false,
    ) {
        @JvmOverloads
        constructor(text: String, toStage: T, skipPlayer: Boolean = false) : this(
            expr = FaceAnim.ASKING,
            text = text,
            toStage = toStage,
            skipPlayer = skipPlayer,
        )
    }

class IfTopic<T>
    @JvmOverloads
    constructor(
        expr: FaceAnim,
        text: String,
        toStage: T,
        val showCondition: Boolean,
        skipPlayer: Boolean = false,
    ) : Topic<T>(expr, text, toStage, skipPlayer) {
        @JvmOverloads
        constructor(text: String, toStage: T, showCondition: Boolean, skipPlayer: Boolean = false) : this(
            expr = FaceAnim.ASKING,
            text = text,
            toStage = toStage,
            showCondition = showCondition,
            skipPlayer = skipPlayer,
        )
    }
