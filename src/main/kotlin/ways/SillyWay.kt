package ways

import common.EasyPoint
import calculateDistanceInList
import common.Optimizer
import common.Route
import common.WorkPoint
import copyWorkPoints
import okr1
import okr2

class SillyWay {

    fun makeVoyage(iterations: Int = 1_000, vararg points: EasyPoint, adjustmentCoef: Double): String {

        //inner params
        val letExpoTmpResults = false
        val letExpoBestRoutesSet = true
        val qBestRoutes = 9
        val letOptimize = true
        val optimizingSectorSize = 9
        val repeatOptimization = 6

        val firstList: MutableList<WorkPoint> = ArrayList(points.size)
        // var bestList: MutableList<staff.WorkPoint> = firstList
        // var bestDistance = Double.MAX_VALUE

        val routes: MutableList<Route> = ArrayList(qBestRoutes + 1)

        for (p in points) {
            firstList.add(WorkPoint(p))
        }

        // main cycle
        for (i in 1..iterations) {
            val workList = firstList.toMutableList()
            val trialSequenceOfPoints: MutableList<WorkPoint> = ArrayList(points.size)
            val firstPoint = workList.removeAt(0)
            trialSequenceOfPoints.add(firstPoint)

            // put random rank to workPoints
            for (p in workList) {
                val randomRank = (1..Int.MAX_VALUE).random()
                p.rank = randomRank
            }

            workList.sort()
            for (p in workList) {
                trialSequenceOfPoints.add(p)
            }
            // add first point to the end to make loop
            trialSequenceOfPoints.add(WorkPoint(firstPoint.getEasyPoint()))

            // count distance in List
            val trialDistance = calculateDistanceInList(trialSequenceOfPoints)
            // println("tryDistance: ${okr(tryDistance)}\n ~ ~ ~") //|||||||||

            // log routes
            val someSequence = copyWorkPoints(trialSequenceOfPoints)
            routes.add(Route(trialDistance, someSequence))
            routes.sort()
            while (routes.size > qBestRoutes) {
                routes.removeAt(routes.size - 1)
            }

            /*/ check best distance
            if (bestDistance >= tryDistance) {
                bestDistance = tryDistance
                bestList = copyListElements(tryList)
            }*/

            if (letExpoTmpResults) {
                val tmpResult = "$i) tryDistance: ${okr1(trialDistance)} km; try rout: $trialSequenceOfPoints"
                println(tmpResult)
            }
        } // main cycle

        if (letExpoBestRoutesSet) {
            println("~ best routes BEFORE optimization ~")
            for (r in routes) {
                println(r)
            }
        }

        // todo: move it to Optimizer
        if (letOptimize && routes[0].getRoutePointsSize() > 1 + optimizingSectorSize + 1) {
            for (repetition in (1..repeatOptimization)) {
                val sector = optimizingSectorSize - (repeatOptimization - repetition)
                println("sector:$sector")
                for (index in routes.indices) {
                    val optimized = Optimizer().optimizeSector(routes[index], sector)
                    routes[index] = optimized
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
        var optimInfo = " <SillyWay> ~~ not optimized"
        if (letOptimize) {
            optimInfo = " <SillyWay> ~~ optimized by sector of $optimizingSectorSize, repeated $repeatOptimization times"
        }

        val recalculatedDistance = okr2(calculateDistanceInList(routes[0].getRoutePoints()))
        return "($recalculatedDistance) ${routes[0]} $optimInfo"

    } // makeVoyage

}