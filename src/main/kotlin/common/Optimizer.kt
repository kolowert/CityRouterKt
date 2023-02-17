package common

import calculateDistanceInList

/*
this optimizer works on Johnson-Trotter algorithm
 */
class Optimizer {

    fun optimizeSector(inputRoute: Route, sectorSize: Int): Route {

        val qPoints = inputRoute.getRoutePointsSize()
        val trialRoutesSize = 3
        val trialRoutes: MutableList<Route> = ArrayList(trialRoutesSize)
        val firstRoute = inputRoute.getDeepIndependentCopy()
        trialRoutes.add(firstRoute)

        // generate Permutations by Steinhaus–Johnson–Trotter algorithm

        // cycle for sectors
        for (i in (0..qPoints - (1 + sectorSize + 1))) {

            val leftInd = i + 1
            val rightInd = i + sectorSize

            val sidePoints = inputRoute.copyRoutePoints()

            var n = 0
            sidePoints.forEach { it.rank = n++ }
            sidePoints.forEach { it.direction = -1 }

            while(true) {
                val biggestMovableIndex = findBiggestMovableIndex(sidePoints, leftInd, rightInd)
                //print(" ~~ optimizer; left:$leftInd right:$rightInd biggestMovableIndex:$biggestMovableIndex ~~ ") //||||||||||||
                if (biggestMovableIndex == -1) { /*println(">>\n");*/ break }
                val replaceElementIndex = biggestMovableIndex + 1 * sidePoints[biggestMovableIndex].direction
                val currentMovableRank = sidePoints[biggestMovableIndex].rank
                swap(sidePoints, biggestMovableIndex, replaceElementIndex)
                normalizeDirections(sidePoints, currentMovableRank, leftInd, rightInd)
                val someDistance = calculateDistanceInList(sidePoints)
                // make copy of sidePoints when creating new route!
                val someRoute = Route(someDistance, makeShallowCopyOfWorkPoints(sidePoints))
                // println("*** ${calculateDistanceInList(someRoute.getRoutePoints())} $someRoute") //||||||||||||
                trialRoutes.add(someRoute)
                trialRoutes.sort()
                while (trialRoutes.size > trialRoutesSize) { trialRoutes.removeAt(trialRoutes.size - 1) }
            }

        }

        trialRoutes.sort()
        println("> > > opt res: ${trialRoutes[0]}") //|||||||||||||
        return trialRoutes[0]
    }

    private fun findBiggestMovableIndex(points: List<WorkPoint>, leftInd: Int, rightInd: Int): Int {
        // description: https://neerc.ifmo.ru/wiki/index.php?title=%D0%9A%D0%BE%D0%B4%D1%8B_%D0%93%D1%80%D0%B5%D1%8F_%D0%B4%D0%BB%D1%8F_%D0%BF%D0%B5%D1%80%D0%B5%D1%81%D1%82%D0%B0%D0%BD%D0%BE%D0%B2%D0%BE%D0%BA

        var biggestMovableRank = -1
        var biggestMovableRankIndex = -1

        for (j in (leftInd..rightInd)) {
            //print("rnk:${points[j].rank}(${points[j].direction}) ") //|||||||
            if (points[j].rank > biggestMovableRank) {
                // check movability
                if (points[j].direction == -1 && j > leftInd && points[j-1].rank < points[j].rank) {
                    biggestMovableRank = points[j].rank
                    biggestMovableRankIndex = j
                }
                if (points[j].direction == 1 && j < rightInd && points[j].rank > points[j+1].rank) {
                    biggestMovableRank = points[j].rank
                    biggestMovableRankIndex = j
                }
            }

        }

        return biggestMovableRankIndex
    }

    private fun normalizeDirections(points: MutableList<WorkPoint>, currentMovableRank: Int, leftInd: Int, rightInd: Int) {
        // after swapping change direction for all elements with bigger rank
        for (j in (leftInd..rightInd)) {
            if (points[j].rank > currentMovableRank) {
                points[j].direction *= -1
            }
        }
    }

    private fun makeShallowCopyOfWorkPoints(inputPoints: MutableList<WorkPoint>): MutableList<WorkPoint> {
        val result: MutableList<WorkPoint> = ArrayList(inputPoints.size)
        for (p in inputPoints) {
            result.add(p)
        }
        return result
    }

    private fun swap(points: MutableList<WorkPoint>, ind1: Int, ind2: Int) {
        val tmpElement = points[ind1]
        points[ind1] = points[ind2]
        points[ind2] = tmpElement
    }

}