package core.tools

import core.game.world.map.Location

class Vector3d {
    var x: Double

    var y: Double

    var z: Double

    constructor(x: Double, y: Double, z: Double) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(t: DoubleArray) {
        this.x = t[0]
        this.y = t[1]
        this.z = t[2]
    }

    constructor(v: Vector3d) {
        this.x = v.x
        this.y = v.y
        this.z = v.z
    }

    constructor(l: Location) {
        this.x = l.x.toDouble()
        this.y = l.y.toDouble()
        this.z = l.z.toDouble()
    }

    constructor() {
        this.x = 0.0
        this.y = 0.0
        this.z = 0.0
    }

    override fun toString(): String {
        return "($x, $y, $z)"
    }

    fun add(
        v1: Vector3d,
        v2: Vector3d,
    ): Vector3d {
        this.x = v1.x + v2.x
        this.y = v1.y + v2.y
        this.z = v1.z + v2.z
        return this
    }

    fun add(v1: Vector3d): Vector3d {
        this.x += v1.x
        this.y += v1.y
        this.z += v1.z
        return this
    }

    fun sub(
        v1: Vector3d,
        v2: Vector3d,
    ): Vector3d {
        this.x = v1.x - v2.x
        this.y = v1.y - v2.y
        this.z = v1.z - v2.z
        return this
    }

    fun sub(v1: Vector3d): Vector3d {
        this.x -= v1.x
        this.y -= v1.y
        this.z -= v1.z
        return this
    }

    fun negate(v1: Vector3d): Vector3d {
        this.x = -v1.x
        this.y = -v1.y
        this.z = -v1.z
        return this
    }

    fun negate(): Vector3d {
        this.x = -this.x
        this.y = -this.y
        this.z = -this.z
        return this
    }

    fun cross(v2: Vector3d): Vector3d {
        this.cross(Vector3d(this), v2)
        return this
    }

    fun cross(
        v1: Vector3d,
        v2: Vector3d,
    ): Vector3d {
        this.x = v1.y * v2.z - v1.z * v2.y
        this.y = v1.z * v2.x - v1.x * v2.z
        this.z = v1.x * v2.y - v1.y * v2.x
        return this
    }

    fun dot(v2: Vector3d): Double {
        return (this.x * v2.x + this.y * v2.y + this.z * v2.z)
    }

    fun l2norm(): Double {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z)
    }

    fun normalize(): Vector3d {
        val norm = this.l2norm()
        this.x /= norm
        this.y /= norm
        this.z /= norm
        return this
    }

    companion object {
        fun signedAngle(
            v1: Vector3d,
            v2: Vector3d,
            n: Vector3d,
        ): Double {
            return Math.atan2(Vector3d().cross(v1, v2).dot(n), v1.dot(v2))
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Vector3d) return false
        return (this.x == other.x && this.y == other.y && this.z == other.z)
    }

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    fun epsilonEquals(
        v1: Vector3d,
        epsilon: Double,
    ): Boolean {
        var diff: Double

        diff = this.x - v1.x
        if (diff.isNaN()) return false
        if (Math.abs(diff) > epsilon) return false

        diff = this.y - v1.y
        if (diff.isNaN()) return false
        if (Math.abs(diff) > epsilon) return false

        diff = this.z - v1.z
        if (diff.isNaN()) return false
        if (Math.abs(diff) > epsilon) return false

        return true
    }
}
