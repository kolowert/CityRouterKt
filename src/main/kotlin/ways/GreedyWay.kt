package ways

import beautifulDouble
import beautifulInt
import calculateDistanceInKm
import common.EasyPoint
import common.Optimizer
import calculateDistanceInList
import copyWorkPoints
import okr1
import okr2
import common.Route
import common.WorkPoint
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class GreedyWay {

    fun makeVoyage(vararg points: EasyPoint): String {

        //inner params
        val iterations = 20_000
        val qNearest = 1
        val qBestRoutes = 120
        val letReduceQBestRoutes = true
        val reducingCoefficient = 0.49
        val overReduceTip = 3

        val letOptimize = true
        val optimizingSectorSize = 9
        val repeatOptimization = optimizingSectorSize - 2

        val letExpoTmpResults = false
        val letExpoBestRoutesSet = true

        val firstList: MutableList<WorkPoint> = ArrayList(points.size)
        // var bestList: MutableList<staff.WorkPoint> = firstList
        // var bestDistance = Double.MAX_VALUE

        val routes: MutableList<Route> = ArrayList(qBestRoutes + 1)

        for (p in points) {
            firstList.add(WorkPoint(p))
        }

        // main cycle
        var expoCounter = 0 // ||||||||||||
        for (i in 1..iterations) {
            if (i % 10_000 == 0) { print(":") } // ||||||||||||
            if (i % 100_000 == 0) { print("${++expoCounter}") } // ||||||||||||

            val workList = firstList.toMutableList()
            val trialSequenceOfPoints: MutableList<WorkPoint> = ArrayList(points.size)
            val firstPoint = workList.removeAt(0)
            trialSequenceOfPoints.add(firstPoint)

            // todo: from here it is different from SillyWay-algorithm (do something with it?)
            while (workList.isNotEmpty()) {
                val lastTrialPoint = trialSequenceOfPoints[trialSequenceOfPoints.size-1]
                workList.forEach {
                    it.distanceTo = calculateDistanceInKm(lastTrialPoint.getEasyPoint(), it.getEasyPoint())
                    it.rank = it.distanceTo.toInt()
                }
                workList.sort()
                // chose randomly one of nearest
                var rightEdge = qNearest - 1
                if (rightEdge < 1) { rightEdge = 1 }
                if (rightEdge > workList.size-1) { rightEdge = workList.size-1 }
                val randomIndex = (0 .. rightEdge).random()
                val nextPoint = workList.removeAt(randomIndex)
                trialSequenceOfPoints.add(nextPoint)
            }
            // todo: to here

            // add first point to the end to make loop
            trialSequenceOfPoints.add(WorkPoint(firstPoint.getEasyPoint()))

            val trialDistance = calculateDistanceInList(trialSequenceOfPoints)
            // println("tryDistance: ${okr(tryDistance)}\n ~ ~ ~") //|||||||||

            // log routes
            val someSequence = copyWorkPoints(trialSequenceOfPoints)
            routes.add(Route(trialDistance, someSequence))
            routes.sort()
            while (routes.size > qBestRoutes) {
                routes.removeAt(routes.size - 1)
            }

            if (letExpoTmpResults) {
                val tmpResult = "$i) tryDistance: ${okr1(trialDistance)} km; try rout: $trialSequenceOfPoints"
                println(tmpResult)
            }
        } // main cycle

        if (letExpoBestRoutesSet) {
            println("\n~ best routes BEFORE optimization ~")
            for (r in routes) {
                println(r)
            }
        }

        // OPTIMIZATION
        // todo: move it to Optimizer
        var qBestRoutesReduced = qBestRoutes.absoluteValue
        if (letOptimize && routes[0].getRoutePointsSize() > 1 + optimizingSectorSize + 1) {
            when (letReduceQBestRoutes) {
                false -> {
                    for (repetition in (1..repeatOptimization)) {
                        val sector = optimizingSectorSize - (repeatOptimization - repetition)
                        println("sector:$sector")
                        for (index in routes.indices) {
                           val optimized = Optimizer().optimizeSector(routes[index], sector)
                           routes[index] = optimized
                        }
                    }
                }
                true -> {
                    for (repetition in (1..repeatOptimization)) {
                        val sector = optimizingSectorSize - (repeatOptimization - repetition)
                        println("sector:$sector, qBestRoutes Reduced to:$qBestRoutesReduced")
                        for (index in routes.indices) {
                            val optimized = Optimizer().optimizeSector(routes[index], sector)
                            routes[index] = optimized
                        }
                        //todo: this part integrate to upper block(false -> ...) and terminate block(true -> ...)
                        qBestRoutesReduced = overReduceTip + (reducingCoefficient * qBestRoutesReduced).roundToInt()
                        routes.sort()
                        while (routes.size > qBestRoutesReduced) {
                            routes.removeAt(routes.size - 1)
                        }
                    }
                }
            }
        }

        if (letOptimize && letExpoBestRoutesSet) {
            println("~ best routes AFTER optimization ~")
            routes.sort()
            for (r in routes) {
                println(r)
            }
        }

        // prepare result
        routes.sort()
        val way = "<GreedyWay$qNearest>"
        var optimInfo = "not optimized"
        if (letOptimize) {
            val optimizationInfo = "optimized by sector of $optimizingSectorSize"
            val repetitionInfo = "repeated $repeatOptimization times"
            val resultsOnProcessingInfo = "lines on processing $qBestRoutes"
            val reducingCoeffInfo = if (letReduceQBestRoutes) { "reducing $reducingCoefficient" } else "reducing 1"
            optimInfo = "$optimizationInfo, $repetitionInfo, $resultsOnProcessingInfo, $reducingCoeffInfo"
        }

        val recalculatedDistance = okr2(calculateDistanceInList(routes[0].getRoutePoints()))
        return "(${beautifulDouble(recalculatedDistance)}) ${routes[0]} " +
                "$way iterations: ${beautifulInt(iterations)}; $optimInfo; " +
                "made at ${java.util.Calendar.getInstance().time}"

    } // makeVoyage

}