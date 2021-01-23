package com.swervedrivespecialties.exampleswerve.commands;

import com.swervedrivespecialties.exampleswerve.Robot;
import com.swervedrivespecialties.exampleswerve.RobotMap;
import com.swervedrivespecialties.exampleswerve.subsystems.DrivetrainSubsystem;
import com.swervedrivespecialties.exampleswerve.subsystems.Limelight;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import org.frcteam2910.common.robot.Utilities;

public class DriveCommand extends Command {



    public DriveCommand() {
        requires(DrivetrainSubsystem.getInstance());
        requires(Limelight.getInstance());
    }


    private void requires(Limelight instance) {
    }

    @Override
    protected void execute() {


        double forward = Robot.getOi().getPrimaryJoystick().getRawAxis(1);
        forward = Utilities.deadband(forward);
        // Square the forward stick
        forward = Math.copySign(Math.pow(forward, 2.0), forward);

        double strafe = Robot.getOi().getPrimaryJoystick().getRawAxis(0);
        strafe = Utilities.deadband(strafe);
        // Square the strafe stick
        strafe = Math.copySign(Math.pow(strafe, 2.0), strafe);

        double rotation;
        if(Robot.getOi().getPrimaryJoystick().getRawButton(1))
        {
            rotation = Robot.limelight.rotatetoTarget(RobotMap.LimelightConstants.LimeLightDrivePID); //add limelight thing
            Robot.limelight.setLED(3);
        }
        else
        {
            rotation = Robot.getOi().getPrimaryJoystick().getRawAxis(4);  
            //If Using flightstick set axis to 2, if using xboxContrller set Axis to 4
            Robot.limelight.setLED(1);
        }
        
        
        rotation = Utilities.deadband(rotation);
        // Square the rotation stick
        rotation = Math.copySign(Math.pow(rotation, 2.0), rotation);

        DrivetrainSubsystem.getInstance().drive(new Translation2d(forward, strafe), rotation, true);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }
}
