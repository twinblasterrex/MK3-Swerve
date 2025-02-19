package frc.robot;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.commands.ChargeStationCommand;
import frc.robot.commands.ScoreCommand;
import frc.robot.subsystems.*;

import java.util.HashMap;

public class Autos {
	private final Drivetrain m_drivetrain;
	private final Intake m_intake;
	private final Conveyor m_conveyor;
	private final HardStopSubsystem m_hardStop;
	private final ScoreCommand score;
	private final SwerveAutoBuilder m_AUTO_BUILDER;
	// Auto Variables
	// This contains the methods we run during auto
	private final HashMap<String, Command> eventMap = new HashMap<>();

	public Autos(Drivetrain drivetrain, Intake intake, Conveyor conveyor, Arm arm, Gripper gripper, HardStopSubsystem hardStop) {
		m_drivetrain = drivetrain;
		m_intake = intake;
		m_conveyor = conveyor;
		m_hardStop = hardStop;

		score = new ScoreCommand(arm, drivetrain, gripper, conveyor, intake, 3, 2, hardStop);


		addMethods();

		// Auto Variables
		m_AUTO_BUILDER = new SwerveAutoBuilder(
				m_drivetrain::getPose,
				m_drivetrain::resetPose,
				DrivetrainConstants.KINEMATICS,
				DrivetrainConstants.Auto.XY_CONSTANTS,
				DrivetrainConstants.Auto.THETA_CONSTANTS,
				m_drivetrain::setStatesAuto,
				this.eventMap,
				false,
				m_drivetrain);
	}

	public void addMethods() {
		// intake methods
		eventMap.put("Run Intake", new InstantCommand(() -> {
			m_intake.runIntake();
			m_conveyor.setEnabled(true);
			m_hardStop.out();
		}));
		eventMap.put("Stop Intake", new InstantCommand(() -> {
			m_intake.stopIntake();
			m_conveyor.setEnabled(false);
			m_hardStop.in();
		}));


		eventMap.put("Balance", new ChargeStationCommand(m_drivetrain));

		eventMap.put("Score", score);
	}

	public Command genPath(String path) {
		return m_AUTO_BUILDER
				.fullAuto(PathPlanner.loadPathGroup(path, Constants.DrivetrainConstants.Auto.PATH_CONSTRAINTS));
	}
}
