package org.firstinspires.ftc.teamcode.RobotStuff.Config.HardwareConfigs

import com.qualcomm.robotcore.hardware.ServoImplEx
import dev.nextftc.ftc.ActiveOpMode
import dev.nextftc.hardware.delegates.Caching
import dev.nextftc.hardware.positionable.Positionable

open class ServoExFullRange(cacheTolerance: Double, servoFactory: () -> ServoImplEx) : Positionable {

    val servo by lazy(servoFactory)

    constructor(servoFactory: () -> ServoImplEx) : this(0.01, servoFactory)

    @JvmOverloads
    constructor(servo: ServoImplEx, cacheTolerance: Double = 0.01) : this(cacheTolerance, { servo })

    @JvmOverloads
    constructor(name: String, cacheTolerance: Double = 0.01) : this(
        cacheTolerance,
        { ActiveOpMode.hardwareMap[name] as ServoImplEx })

    override var position: Double by Caching(cacheTolerance) {
        it?.let { servo.position = it }
    }
}