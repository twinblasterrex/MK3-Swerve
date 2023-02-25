package frc.robot;

import com.pathplanner.lib.PathPlanner;
import com.pathplanner.lib.PathPlannerTrajectory;
import com.pathplanner.lib.auto.SwerveAutoBuilder;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.DrivetrainConstants;
import frc.robot.commands.ScoreCommand;
import frc.robot.subsystems.*;

import java.util.HashMap;

public class Autos {
    private final Drivetrain m_drivetrain;
    private final Intake m_intake;
    private final Conveyor m_conveyor;
    private final Arm m_arm;
    private final Gripper m_gripper;
    private Limelight limelight;
    private ScoreCommand score;
    private final SwerveAutoBuilder m_AUTO_BUILDER;



    public Autos(Drivetrain drivetrain, Intake intake, Conveyor conveyor, Arm arm, Gripper gripper, Limelight limelight) {
        m_drivetrain = drivetrain;
        m_intake = intake;
        m_conveyor = conveyor;
        m_arm = arm;
        m_gripper = gripper;
        this.limelight = limelight;

        score = new ScoreCommand(arm, drivetrain, gripper, limelight, conveyor,2);

//        addMethods();


        // Auto Variables
        m_AUTO_BUILDER = new SwerveAutoBuilder(
            m_drivetrain::getPose,
            m_drivetrain::resetPose,
            DrivetrainConstants.KINEMATICS,
            DrivetrainConstants.Auto.XY_CONSTANTS,
            DrivetrainConstants.Auto.THETA_CONSTANTS,
            m_drivetrain::setStates,
            this.eventMap,
            false,
        m_drivetrain);
    }

    // Auto Variables
    // This contains the methods we run during auto
    private final HashMap<String, Command> eventMap = new HashMap<>();

    public void addMethods() {

        // intake methodsWADWQWADSXCX6Y
        // eventMap.put("Actuate Intake", new InstantCommand(m_intake::actuateIntake));
        eventMap.put("Intake In", new WaitCommand(3));
        eventMap.put("Intake Out",new WaitCommand(3));
        eventMap.put("Stop Intake", new InstantCommand(() -> m_intake.runIntake(0)));

        // arm methods
//        eventMap.put("Rest Arm", new InstantCommand(m_arm::rest));
//        eventMap.put("Set Arm to Middle Goal", new InstantCommand(m_arm::middleGoalCone));
//        eventMap.put("Set Arm to High Goal", new InstantCommand(m_arm::highGoalCone));
//
//        // gripper methods
//        eventMap.put("Rest Gripper", new InstantCommand(m_gripper::rest));
//        eventMap.put("Grip Cone", new InstantCommand(m_gripper::gripCone));
//        eventMap.put("Grip Cube", new InstantCommand(m_gripper::gripCube));

        eventMap.put("Score", score);
    }

    public Command genPath(String path)
    {

        return m_AUTO_BUILDER.fullAuto(PathPlanner.loadPathGroup(path, Constants.DrivetrainConstants.Auto.PATH_CONSTRAINTS));
    }
    public Command gnerateCommand(PathPlannerTrajectory trajectory)
    {
        return m_AUTO_BUILDER.followPath(trajectory);
    }
}
