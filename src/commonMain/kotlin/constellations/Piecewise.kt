//package constellations
//
//import asterum.Constellation
//import asterum.ConstellationBounds
//
//
//abstract class Piecewise : Constellation() {
//
//    private var _domain: ConstellationBounds? = null
//    override val domain: ConstellationBounds
//        get() {
//            if (_range == null) {
//                this.inputs.takeWhile { true }
//            }
//
//            return _range as ConstellationBounds
//        }
//
//}