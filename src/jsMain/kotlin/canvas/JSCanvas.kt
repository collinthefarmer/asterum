package canvas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.*

import kotlinx.browser.window


import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.CanvasRenderingContext2D
import org.jetbrains.compose.web.attributes.AttrsScope

import org.jetbrains.compose.web.attributes.height
import org.jetbrains.compose.web.attributes.width
import org.jetbrains.compose.web.dom.Canvas

import asterum.Chord

import asterum.Constellation
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.RangeInput
import org.jetbrains.compose.web.dom.Text
import kotlin.reflect.KMutableProperty0

interface JSStrokeStyle {
    val width: Double
    val style: String
}

typealias JSChordStyleFunc = (index: Int, chord: Chord) -> JSStrokeStyle

fun setDimensions(scope: AttrsScope<HTMLCanvasElement>, ht: Int, wdt: Int) {
    scope.height(ht)
    scope.width(wdt)
}

@Composable
fun JSCanvas(
    height: Int,
    width: Int,
    style: JSChordStyleFunc,
    constellation: Constellation,
    attrs: AttrBuilderContext<HTMLCanvasElement>
) {

    var el: HTMLCanvasElement? by remember { mutableStateOf(null) }

    Canvas(
        {
            setDimensions(this, height, width)
            ref {
                el = it
                onDispose {
                    el = null
                }
            }
            attrs(this)
        }
    ) {
    }

    el?.let {
        DisposableEffect(height, width, it, constellation) {
            jsDrawConstellation(it, style, constellation.chords)

            onDispose {
                clearCanvas(it)
            }
        }
    }

}

fun withContext(el: HTMLCanvasElement, with: (ctx: CanvasRenderingContext2D) -> Unit) {
    val context = el.getContext("2d") as CanvasRenderingContext2D
    with(context)
}

fun drawOnCanvas(el: HTMLCanvasElement, draw: (ctx: CanvasRenderingContext2D) -> Unit) {
    window.requestAnimationFrame {
        withContext(el, draw)
    }
}

fun clearCanvas(el: HTMLCanvasElement) {
    return drawOnCanvas(el) {
        it.clearRect(0.0, 0.0, it.canvas.width.toDouble(), it.canvas.height.toDouble())
    }
}

fun jsDrawConstellation(
    el: HTMLCanvasElement,
    style: JSChordStyleFunc,
    chords: Sequence<Chord>
) {
    return drawOnCanvas(el) {
        for ((i, chord) in chords.withIndex()) {
            jsDrawChord(it, chord, style(i, chord))
        }
    }
}

fun jsDrawChord(
    ctx: CanvasRenderingContext2D,
    chord: Chord,
    style: JSStrokeStyle
) {
    ctx.lineWidth = style.width
    ctx.strokeStyle = style.style

    ctx.beginPath()

    ctx.lineTo(
        chord.first.first * ctx.canvas.width + (ctx.canvas.width / 2),
        chord.first.second * ctx.canvas.height + (ctx.canvas.height / 2)
    )

    ctx.lineTo(
        chord.second.first * ctx.canvas.width + (ctx.canvas.width / 2),
        chord.second.second * ctx.canvas.height + (ctx.canvas.height / 2)
    )

    ctx.stroke()
}

@Composable
fun JSParametersControl(value: Double, setter: (newValue: Double) -> Unit) {
    RangeInput(value = value, step = 1, min = -7000, max = 7000) {
        onInput { event ->
            setter((event.value ?: 0).toDouble())
        }
    }

    Text(value.toString())
}