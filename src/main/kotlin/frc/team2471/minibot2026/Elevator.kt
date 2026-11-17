package frc.team2471.minibot2026

import com.ctre.phoenix6.controls.MotionMagicVoltage
import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.wpilibj2.command.SubsystemBase
import org.team2471.frc.lib.hardware.ctre.applyConfiguration
import org.team2471.frc.lib.hardware.ctre.brakeMode
import org.team2471.frc.lib.hardware.ctre.currentLimits
import org.team2471.frc.lib.hardware.ctre.motionMagic
import org.team2471.frc.lib.hardware.ctre.p
import org.team2471.frc.lib.hardware.ctre.sensorToMechanismRatio
import org.team2471.frc.lib.units.asInches
import org.team2471.frc.lib.units.inches

object Elevator: SubsystemBase("Elevator") {
    val motor = TalonFX(Talons.ELEVATOR)

    var heightSetpoint = 0.0.inches
        set(value) {
            field = value.coerceIn(0.0.inches, 30.0.inches)
            motor.setControl(MotionMagicVoltage(field.asInches))
        }

    init {
        motor.applyConfiguration {
            currentLimits(30.0, 40.0, 1.0)
            brakeMode()

            sensorToMechanismRatio(1.0)

            motionMagic(20.0, 10.0)

            p(0.0)
        }
    }
}