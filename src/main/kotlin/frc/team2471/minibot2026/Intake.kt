package frc.team2471.minibot2026

import com.ctre.phoenix6.controls.DutyCycleOut
import com.ctre.phoenix6.controls.NeutralOut
import com.ctre.phoenix6.hardware.TalonFX
import edu.wpi.first.wpilibj2.command.SubsystemBase
import edu.wpi.first.networktables.NetworkTableInstance
import org.littletonrobotics.junction.AutoLogOutput
import org.team2471.frc.lib.hardware.ctre.applyConfiguration
import org.team2471.frc.lib.hardware.ctre.coastMode
import org.team2471.frc.lib.hardware.ctre.currentLimits
import org.team2471.frc.lib.hardware.ctre.inverted
import org.team2471.frc.lib.hardware.ctre.statorCurrentLimit

object Intake: SubsystemBase("Intake") {
   val table = NetworkTableInstance.getDefault().getTable("Intake")

   val motor = TalonFX(Talons.INTAKE)

   @get:AutoLogOutput(key = "Intake/Current State")
   var currentState = State.OFF

   init {
      motor.applyConfiguration {
         currentLimits(7.0, 10.0, 1.0)
         statorCurrentLimit(15.0)
         inverted(true)
         coastMode()
      }

   }

   override fun periodic() {
      when (currentState) {
         State.OFF -> {
            motor.setControl(NeutralOut())
         }
         State.INTAKING -> {
            motor.setControl(DutyCycleOut(0.3))
         }
         State.SPITTING -> {
            motor.setControl(DutyCycleOut(-0.3))
         }
      }
   }


   enum class State {
      OFF,
      INTAKING,
      SPITTING
   }
}