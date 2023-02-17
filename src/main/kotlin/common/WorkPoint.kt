package common

/*
This contain EarthPoint object and additional information
for manipulating of points when finding rout
*/
class WorkPoint(private val point: EasyPoint): Comparable<WorkPoint> {

    var rank: Int = 0
    var distanceTo: Double = 0.0
    var direction: Int = -1 // for when optimizing to generate Permutations by Steinhaus–Johnson–Trotter algorithm

    override fun compareTo(other: WorkPoint): Int {
        return this.rank - other.rank
    }

    fun getEasyPoint(): EasyPoint {
        return point
    }

    override fun toString(): String {
        return if (distanceTo > 0.0) {
            "-${(distanceTo + 0.5).toInt()}- $point"
        } else {
            "$point"
        }
    }

} // class WorkPoint