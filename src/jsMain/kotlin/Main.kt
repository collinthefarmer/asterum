import asterum.Chord
import canvas.JSCanvas
import canvas.JSStrokeStyle

import constellations.Painstar
import constellations.PainstarParameters

import androidx.compose.runtime.*
import canvas.JSParametersControl
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

import org.jetbrains.compose.web.renderComposable
import kotlin.math.PI
import kotlin.math.sign

import kotlin.reflect.*


fun draw(i: Int, chord: Chord): JSStrokeStyle {
    return object : JSStrokeStyle {
        override val width: Double = 1.0
        override val style: String = "#555"
    }
}

fun main() {
    var domainStart by mutableStateOf(0.0)
    var domainEnd by mutableStateOf(PI)



    renderComposable("root") {

        var height by mutableStateOf(1000)
        var width by mutableStateOf(1000)

        var inc = PI / 512

        var alpha by remember { mutableStateOf(0.0) }
        var beta by remember { mutableStateOf(0.0) }
        var gamma by remember { mutableStateOf(0.0) }
        var delta by remember { mutableStateOf(0.0) }
        var epsilon by remember { mutableStateOf(0.0) }
        var zeta by remember { mutableStateOf(0.0) }

        val parameters = PainstarParameters(
            alpha * inc,
            beta * inc,
            gamma * inc,
            delta * inc,
            epsilon * inc,
            zeta * inc
        )

        Div(attrs = {
            style {
                display(DisplayStyle.Flex)
                flexFlow(FlexDirection.Column, FlexWrap.Nowrap)
            }
        }) {
            JSCanvas(
                height,
                width,
                ::draw,
                Painstar(domainStart, domainEnd, parameters)
            ) {
                onWheel {
                    val dir = -it.deltaY.sign
                    gamma += dir
                }
            }
            Div(attrs = {
                style {
                    display(DisplayStyle.Flex)
                    flexFlow(FlexDirection.Column, FlexWrap.Nowrap)
                }
            }) {
                JSParametersControl(alpha) {
                    alpha = it
                }

                JSParametersControl(beta) {
                    beta = it
                }

                JSParametersControl(gamma) {
                    gamma = it
                }

                JSParametersControl(delta) {
                    delta = it
                }

                JSParametersControl(epsilon) {
                    epsilon = it
                }

                JSParametersControl(zeta) {
                    zeta = it
                }
            }
        }
    }
}

