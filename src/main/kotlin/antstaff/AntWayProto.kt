package antstaff

import calculateDistanceInKm
import common.EasyPoint

/*
How it works:
There are two independent pools of AntPoints
Base pool contain AntPoints wit WorkPool inside (Work pool is independent copy for every member of BasePool)
 */

class AntWayProto(private val inputPoints: Array<EasyPoint>) {

    private val basePool: MutableList<AntPoint> = ArrayList<AntPoint>()

    fun makeVoyage(): String {

        // make Base Pool of Points
        inputPoints.forEach { basePool.add(AntPoint(it)) }
        // add first point into the end and make it's Status as Finish
        basePool.add(AntPoint(inputPoints[0]))
        basePool[0].status = AntStatus.START
        basePool[basePool.size - 1].status = AntStatus.FINISH

        // fill Base AntPointPool with independent copies of similar AntPools (avery Point in BasePool will have own WorkPool)
        val sampleCopyOfBasePool = makeIndependentCopy(basePool.toCollection(ArrayList()))
        for (antPoint in basePool) {
            antPoint.deepPool = makeIndependentCopy(sampleCopyOfBasePool)
        }

        // manipulate by Base Pool
        basePool.forEach { it.status = AntStatus.NOT_VISITED }
        singleAntJob(basePool)


        return "stub"
    }

    private fun singleAntJob(basePool: MutableList<AntPoint>) {
        for (basePoint in basePool) {
            basePoint.status = AntStatus.VISITED

            // put basePoint as prepoint to ownPool and calculate distances
            val deepPool = basePoint.deepPool
            for (deepPoint in deepPool) {
                deepPoint.preDistance = calculateDistanceInKm(basePoint.getEasyPoint(), deepPoint.getEasyPoint())
                deepPoint.prePoint = basePoint
            }

            // choose point to walk



        }

    }

    private class AntPoint(private val point: EasyPoint): Comparable<AntPoint> {

        // properties for Base Pool of AntPoints
        var status = AntStatus.NOT_VISITED;
        var deepPool: MutableList<AntPoint> = ArrayList<AntPoint>()

        // Properties for Own Work Pool of AntPoints
        var prePoint: AntPoint = AntPoint(point)
        var preDistance = Double.MAX_VALUE

        var feromonLevel = 0.0
        var affinity = 0.0
        var desirability = 0.0
        var rank: Int = 0


        override fun compareTo(other: AntPoint): Int {
            return this.rank - other.rank
        }

        fun getEasyPoint(): EasyPoint {
            return point
        }

        fun countDistances(): MutableList<AntPoint> {

            return deepPool
        }

        override fun toString(): String {
            return point.toString()
        }

    }

    private data class Line(val node1: EasyPoint, val node2: EasyPoint, val distance: Double) {
        var feromonLevel: Double = 0.0
        var desirability: Double = 0.0
        var name: String = "line"
        init {
            name = if (node1.toString() > node2.toString()) "$node2 ~ $node1"
                    else "$node1 ~ $node2"
        }

        override fun toString() = "$name dist:$distance ferom:$feromonLevel desir:$desirability"

    }

    private data class TempRecord(val left: Double,
                                  val right: Double,
                                  val prePoint: AntPoint,
                                  val workPoint: AntPoint
    ) {
    }

    private enum class AntStatus {
        NOT_VISITED, VISITED, START, FINISH
    }

    private fun makeIndependentCopy(input: ArrayList<AntPoint>): ArrayList<AntPoint> {
        val output: MutableList<AntPoint> = ArrayList<AntPoint>()
        input.forEach { output.add(AntPoint(it.getEasyPoint())) }
        return output.toCollection(ArrayList())
    }

}