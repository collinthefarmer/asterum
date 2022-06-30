package constellations

import androidx.compose.runtime.*
import asterum.Constellation
import asterum.ImplementationCanvas

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin


data class PainstarParameters(
    var alpha: Double = 0.0,
    var beta: Double = 0.0,
    var gamma: Double = 0.0,
    var delta: Double = 0.0,
    var epsilon: Double = 0.0,
    var zeta: Double = 0.0
)

@Composable
fun Painstar(
    domainStart: Double,
    domainEnd: Double,
    params: PainstarParameters
): Constellation {
    val count = 10000

    val nx: ContinuousTransform = {
        params.beta.pow(params.gamma) * sin(it * params.beta * params.alpha)
    }
    val dx: ContinuousTransform = {
        1 +
                cos(PI * it * params.delta * params.zeta) *
                sin(PI * it * params.delta * params.epsilon) *
                params.epsilon *
                params.zeta
    }
    val fx: ContinuousTransform = {
        nx(it) / dx(it)
    }

    class Painstar : ContinuousPolar(domainStart, domainEnd, count) {
        override val f = fx
    }

    return Painstar()
}