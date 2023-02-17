package common

import kotlin.math.abs

data class EasyPoint(val continent: String = "unnamed",
                     val country: String = "unnamed",
                     val locationName: String = "unnamed",
                     val name: String = "unnamed",
                     val latitude: Double, val longitude: Double,
                     val elevation: Double? = null,
                     val isOk: Boolean = true) {

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is EasyPoint -> {
                (
                    this.continent == other.continent
                    && this.country == other.country
                    && this.name == other.name
                    && abs(this.latitude - other.latitude) < 0.000001
                    && abs(this.longitude - other.longitude) < 0.000001
                )
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        var result = continent.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }

    override fun toString(): String {
        return if (country != "unnamed") {
            "$name ($country)"
        } else {
            ":$name"
        }
    }

}
