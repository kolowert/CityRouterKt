import common.EasyPoint
import ways.*
import java.io.File

class Player {

    private val filesOfPointSets = arrayOf<String>( "LvivRegion06.csv",   // 0
                                                    "LvivRegion16.csv",   // 1
                                                    "LvivRegion32.csv",   // 2
                                                    "NorthWestUA.csv",    // 3
                                                    "UAPoints.csv",       // 4
                                                    "PolandFromLviv.csv", // 5
                                                    "EU_x.csv",           // 6
                                                    "US_RVParks.csv",     // 7
                                                    "USA_20.csv",         // 8
                                                    "USA_25.csv",         // 9
                                                    "USA_30.csv",         // 10
                                                    "EU_cross_100.csv",   // 11
                                                    "EU_cross_120.csv"    // 12
                                                  )

    private val pointsSet = 12

    fun play() {
        val filePath = "src/main/resources/points/${filesOfPointSets[pointsSet]}"
        val points = makeListOfEasyPointsFromCsvFile(filePath)
        println(java.util.Calendar.getInstance().time)
        print("${points.size} points: "); points.forEach { print(" $it") }; println()

        // val routReport = SillyWay().makeVoyage(sillyIterations, *points)
        val routReport = GreedyWay().makeVoyage(*points)
        // val routReport = AntWayProto(points).makeVoyage()

        println("\n${points.size} points, points list: ${filesOfPointSets[pointsSet]} ")
        println("routeReport: $routReport")

    }

    private fun makeListOfEasyPointsFromCsvFile(fileName: String): Array<EasyPoint> {
        val linesFromFile: List<String> = File(fileName).readLines()
        val points = ArrayList<EasyPoint>()
        linesFromFile.forEach { e -> points.add(makeEasyPoint(e)) }
        return points.toTypedArray()
    }

}