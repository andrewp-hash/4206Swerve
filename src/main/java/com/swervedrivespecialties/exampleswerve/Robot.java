package com.swervedrivespecialties.exampleswerve;

import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
import com.swervedrivespecialties.exampleswerve.subsystems.LimelightSubsystem;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Robot extends TimedRobot {

    private static OI oi;

    private static DrivetrainSubsystem drivetrain;
    public static LimelightSubsystem limelight;





    public static OI getOi() {
        return oi;
    }

    @Override
    public void robotInit() {
        oi = new OI();
        drivetrain = DrivetrainSubsystem.getInstance();
        limelight = LimelightSubsystem.getInstance();

    }

    @Override
    public void robotPeriodic() {
        Scheduler.getInstance().run();
    }
}
