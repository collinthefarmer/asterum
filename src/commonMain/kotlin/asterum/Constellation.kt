package asterum

interface ConstellationBounds {
    val lower: Double
    val upper: Double
}

abstract class Constellation {
    abstract val range: ConstellationBounds
    abstract val domain: ConstellationBounds

    abstract val inputs: Sequence<Double>
    abstract val outputs: Sequence<Double>

    abstract val chords: Sequence<Chord>
}
