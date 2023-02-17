package common

import calculateDistanceInList
import okr2

class Route(private val routeDistance: Double, private val routePoints: MutableList<WorkPoint>) : Comparable<Route> {

    override fun compareTo(other: Route): Int {
        return if (this.routeDistance - other.routeDistance > 0.0) {
            1
        } else if (this.routeDistance - other.routeDistance < 0.0) {
            -1
        } else {
            0
        }
    }

    fun getRoutePointsSize(): Int {
        return routePoints.size
    }

    fun getRoutePoints(): MutableList<WorkPoint> {
        return routePoints
    }

    fun getDistance(): Double {
        return routeDistance
    }

    fun copyRoutePoints(): MutableList<WorkPoint> {
        val newPoints: MutableList<WorkPoint> = ArrayList(routePoints.size)
        for (p in routePoints) {
            newPoints.add(WorkPoint(p.getEasyPoint()))
        }
        return newPoints
    }

    fun getDeepIndependentCopy(): Route {
        // make copy for all points in List
        val copyOfPoints = copyRoutePoints()
        // recalculate distance
        val recalculatedDistance = calculateDistanceInList(copyOfPoints)
        // create new SillyRoute
        val copyOfThis = Route(recalculatedDistance, copyOfPoints)
        return copyOfThis
    }

    override fun toString(): String {
        return "${okr2(routeDistance)} km. $routePoints"
    }
}