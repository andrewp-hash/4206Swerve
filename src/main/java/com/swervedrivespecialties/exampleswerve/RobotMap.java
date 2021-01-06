package com.swervedrivespecialties.exampleswerve;

public class RobotMap {

    public static final class CANConstants{

     public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 8; // CAN
     public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER = 3; // Analog
     public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 7; // CAN

     public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 4; // CAN 2
     public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER = 1; // Analog 0
     public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 3; // CAN 1

     public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 6; // CAN
     public static final int DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER = 2; // Analog
     public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 5; // CAN

     public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 2; // CAN 4
     public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER = 0; // Analog 1
     public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 1; // CAN 3

    }



    public static final class CurrentLimit{

      public static final int TurnMotorCurrentLimit = 15; //recomeneded 15
      public static final int DriveMotorCurrentLimit = 50; // recomended 50

    }
    


    public static final class LimelightConstants{

     public static final double LimeLightDrivePID = 0.03;
     public static final double cameraHeight = 0.6096; //Camera height in meters
     public static final double goalHeight = 7.5; //Hatch panel height in meters
     public static final double mountAngle = 0.612706; //26.3258

    }
}
