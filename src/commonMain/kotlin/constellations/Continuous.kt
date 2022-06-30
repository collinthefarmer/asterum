package constellations

import asterum.Chord
import asterum.Constellation
import asterum.ConstellationBounds
import kotlin.math.*

import kotlin.reflect.KMutableProperty0

typealias ContinuousTransform = (Double) -> Double

typealias SamplingPattern = (Double, Double, Int) -> Sequence<Double>

abstract class Continuous(
    protected val domainStart: Double,
    protected val domainEnd: Double,
    protected val count: Int,
    protected val sampleBetweenWith: SamplingPattern = ::evenDistribution
) : Constellation() {

    protected abstract val f: ContinuousTransform

    private fun trackBoundsTo(
        seq: Sequence<Double>,
        sets: KMutableProperty0<ConstellationBounds?>
    ): Sequence<Double> {
        var min = Double.MAX_VALUE
        var max = Double.MIN_VALUE

        return seq.onEachIndexed { index, v ->
            if (index == this.count) {
                sets.set(object : ConstellationBounds {
                    override val lower = min
                    override val upper = max
                })
            }

            min = if (v < min) v else min
            max = if (v > max) v else max
        }
    }

    private var _range: ConstellationBounds? = null
    override val range: ConstellationBounds
        get() {
            if (_range == null) {
                this.outputs.takeWhile { true }
            }

            return _range as ConstellationBounds
        }

    override val domain: ConstellationBounds
        get() {
            return object : ConstellationBounds {
                override val lower = min(domainStart, domainEnd)
                override val upper = max(domainStart, domainEnd)
            }
        }

    override val inputs: Sequence<Double>
        get() = this.sampleBetweenWith(domainStart, domainEnd, this.count)

    private val outputCache: MutableMap<Double, Double> = mutableMapOf()
    override val outputs: Sequence<Double>
        get() = this.inputs
            .map {
                this.outputCache[it].takeIf { v ->
                    v != null
                } ?: this.f(it).also { v ->
                    outputCache[it] = v
                }
            }
            .also {
                this.trackBoundsTo(it, ::_range)
            }

    override val chords: Sequence<Chord>
        get() = this.inputs.zip(this.outputs).zipWithNext()
}


abstract class ContinuousPolar(
    domainStart: Double,
    domainEnd: Double,
    count: Int,
    sampleBetweenWith: SamplingPattern = ::evenDistribution
) : Continuous(domainStart, domainEnd, count, sampleBetweenWith) {

    override val chords: Sequence<Chord>
        get() = super.chords.map {
            Pair(
                Pair(
                    it.first.second * cos(it.first.first),
                    it.first.second * sin(it.first.first)
                ),
                Pair(
                    it.second.second * cos(it.second.first),
                    it.second.second * sin(it.second.first)
                )
            )
        }

}


fun evenDistribution(start: Double, end: Double, count: Int): Sequence<Double> {
    val magnitude = end - start
    val step = magnitude / count

    return generateSequence(start) {
        (it + step).takeIf { v -> v <= end }
    }}


