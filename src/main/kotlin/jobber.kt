import common.EasyPoint
import common.WorkPoint
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

/*
Here services functions are collected
 */

fun makeEasyPoint(notice: String): EasyPoint {
    val stub = EasyPoint(name = "stub", latitude = 50.0, longitude = 24.0, isOk = false)
    var result = stub
    val parts = notice.split(",")

    when (parts.size) {
        3 -> {
            try {
                val point = EasyPoint(name = parts[0], latitude = parts[1].toDouble(), longitude = parts[2].toDouble())
                result = point
            } catch (e: Exception) {
                try {
                    val point =
                        EasyPoint(name = parts[2], latitude = parts[0].toDouble(), longitude = parts[1].toDouble())
                    result = point
                } catch (e: Exception) {
                    // do nothing
                }
            }
        }
        5 -> {
            try {
                val point = EasyPoint(name = parts[0], locationName = parts[1], latitude = parts[2].toDouble(),
                    longitude = parts[3].toDouble(), elevation = parts[4].toDouble())
                result = point
            } catch (e: Exception) {
                // do nothing
            }
        }
    }

    return result
}

fun calculateDistanceInKm(p1: EasyPoint, p2: EasyPoint): Double {

    val merydianOffset_grad = abs(p1.latitude - p2.latitude)
    val elevationOffset_grad = (p1.elevation ?: 0.0) + (p2.elevation ?: 0.0)
    val mainRadius_km = 6371.3 + 0.001 * 0.5 * elevationOffset_grad
    val meridianLength_km = 2.0 * Math.PI * mainRadius_km
    val merydianOffset_km = merydianOffset_grad * meridianLength_km / 360.0

    val parallelOffset_grad = abs(p1.longitude - p2.longitude)
    val parallel1Radius_km = cos((p1.latitude * Math.PI / 180.0)) * mainRadius_km
    val parallel1Length_km = 2.0 * Math.PI * parallel1Radius_km
    val parallel1Segment_km = parallelOffset_grad * parallel1Length_km / 360.0
    val parallel2Radius_km = cos((p2.latitude * Math.PI / 180.0)) * mainRadius_km
    val parallel2Length_km = 2.0 * Math.PI * parallel2Radius_km
    val parallel2Segment_km = parallelOffset_grad * parallel2Length_km / 360.0
    val parallelSegmentAverageLength_km = 0.5 * (parallel1Segment_km + parallel2Segment_km)

    val hypotenuse = sqrt(merydianOffset_km.pow(2.0) + parallelSegmentAverageLength_km.pow(2.0))

    val adjustmentCoef = 1.2

    val distance_km = adjustmentCoef * hypotenuse

    return distance_km
}

fun calculateDistanceInList(points: List<WorkPoint>): Double {
    var resultingDistance = 0.0
    for (ind in points.indices) {
        if (ind == 0) { continue }
        val d = calculateDistanceInKm(points[ind-1].getEasyPoint(), points[ind].getEasyPoint())
        points[ind].distanceTo = d
        resultingDistance += d
    }
    return resultingDistance
}

fun copyWorkPoints(inputList: MutableList<WorkPoint>): MutableList<WorkPoint> {

    val copied: MutableList<WorkPoint> = ArrayList(inputList.size)

    for (e in inputList) {
        val p = WorkPoint(e.getEasyPoint())
        p.distanceTo = e.distanceTo
        copied.add(p)
    }

    return copied.toMutableList()
}

fun beautifulInt(n: Int): String {
    val sb = StringBuilder()
    val s = n.toString()
    val len = s.length
    var counter = 0
    for (ind in (len - 1 downTo 0)) {
        if (counter % 3 == 0 && counter > 0) {
            sb.append("_")
        }
        counter++
        sb.append(s[ind])
    }
    return sb.toString().reversed()
}

fun beautifulDouble(n: Double): String {
    val left = n.toInt()
    val right = n - left
    val niceRight = right.toString().subSequence(2, 4)
    return "${beautifulInt(left)}.$niceRight"
}

fun reportDistance(p1: EasyPoint, p2: EasyPoint): String {
    val d = calculateDistanceInKm(p1, p2)
    return "${okr1(d)} km. ($p1 - $p2)"
}

fun okr1(n: Double): Double {
    return (n * 10.0 + 0.5).toInt() / 10.0
}
fun okr2(n: Double): Double {
    return (n * 100.0 + 0.5).toInt() / 100.0
}
