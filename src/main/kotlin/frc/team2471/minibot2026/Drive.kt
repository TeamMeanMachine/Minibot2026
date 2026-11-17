package frc.team2471.minibot2026

import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj2.command.SubsystemBase

import org.team2471.frc.lib.hardware.ctre.applyConfiguration
import org.team2471.frc.lib.hardware.ctre.brakeMode
import org.team2471.frc.lib.hardware.ctre.currentLimits
import org.team2471.frc.lib.hardware.ctre.inverted
import org.team2471.frc.lib.hardware.ctre.modifyConfiguration
import org.team2471.frc.lib.hardware.ctre.statorCurrentLimit
import org.team2471.frc.lib.math.deadband
import kotlin.math.abs
import kotlin.math.pow

object Drive: SubsystemBase("Drive") {
    val table = NetworkTableInstance.getDefault().getTable("Drive")

    private val funModeEntry = table.getEntry("Do Fun Mode")

    val funMode get() = funModeEntry.getBoolean(false)

    val leftMotor = TalonFX(Talons.LEFT_DRIVE)
    val rightMotor = TalonFX(Talons.RIGHT_DRIVE)
    const val STATOR_CURRENT_LIMIT = 45.0

    init {
        if (!funModeEntry.exists()) {
            funModeEntry.setBoolean(funMode)
        }

        leftMotor.applyConfiguration {
            currentLimits(30.0, 40.0, 1.0)
            statorCurrentLimit(STATOR_CURRENT_LIMIT)
            inverted(true)
            brakeMode()
            this.OpenLoopRamps.apply {
                DutyCycleOpenLoopRampPeriod = 1.0
            }

        }

        rightMotor.applyConfiguration {
            currentLimits(30.0, 40.0, 1.0)
            statorCurrentLimit(STATOR_CURRENT_LIMIT)
            inverted(false)
            brakeMode()
            this.OpenLoopRamps.apply {
                DutyCycleOpenLoopRampPeriod = 1.0
            }
        }
    }


    fun updateFunMode() {
        if (funMode) {
            println("fun mode!")
            leftMotor.modifyConfiguration {
                this.CurrentLimits.apply {
                    StatorCurrentLimitEnable = false
                }
                this.OpenLoopRamps.apply {
                    DutyCycleOpenLoopRampPeriod = 0.0
                }
            }
            rightMotor.modifyConfiguration {
                this.CurrentLimits.apply {
                    StatorCurrentLimitEnable = false
                }
                this.OpenLoopRamps.apply {
                    DutyCycleOpenLoopRampPeriod = 0.0
                }
            }
        } else {
            println("normal mode")
            leftMotor.modifyConfiguration {
                statorCurrentLimit(STATOR_CURRENT_LIMIT)
                this.OpenLoopRamps.apply {
                    DutyCycleOpenLoopRampPeriod = 1.0
                }
            }
            rightMotor.modifyConfiguration {
                statorCurrentLimit(STATOR_CURRENT_LIMIT)
                this.OpenLoopRamps.apply {
                    DutyCycleOpenLoopRampPeriod = 1.0
                }
            }
        }
    }

    fun joystickDrive(turnOverride: Double? = null) {
            val forwardStick = -OI.driverController.leftY.deadband(0.1) * if (funMode) 1.0 else 0.8
            val steerStick = OI.driverController.rightX.deadband(0.1).pow(3)

            val turn = turnOverride ?: if (abs(forwardStick) > 0.1) abs(forwardStick) * steerStick else steerStick

            val leftPower = forwardStick + turn
            val rightPower = forwardStick - turn

            leftMotor.setVoltage(leftPower * 12.0)
            rightMotor.setVoltage(rightPower * 12.0)
    }
}