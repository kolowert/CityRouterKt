/*
Project description
Project name = CityRouter
Project Purpose = It operates with Cities and Calculates best rout
 */

fun main(args: Array<String>) {

    val startTime = java.util.Calendar.getInstance().timeInMillis

    val player = Player()
    player.play()

    println("~ elapsed ${reportElapsedTime(startTime)}")

}

fun reportElapsedTime(startTime: Long): String {
    val finishTime = java.util.Calendar.getInstance().timeInMillis
    val t = finishTime - startTime
    var sec = round(0.001 * t)
    var min = 0
    if (sec > 60) {
        min = (sec / 60.0).toInt()
        sec -= min * 60
    }
    return "$min min ${okr1(sec)} sec"
}

fun round(d: Double): Double {
    return 0.001 * (1000.0 * d).toInt().toDouble()
}