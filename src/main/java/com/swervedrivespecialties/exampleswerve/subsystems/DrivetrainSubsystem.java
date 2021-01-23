package com.swervedrivespecialties.exampleswerve.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.swervedrivespecialties.exampleswerve.RobotMap;
import com.swervedrivespecialties.exampleswerve.commands.DriveCommand;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frcteam2910.common.drivers.Gyroscope;
import org.frcteam2910.common.drivers.SwerveModule;
import org.frcteam2910.common.math.Vector2;
import org.frcteam2910.common.robot.drivers.Mk2SwerveModuleBuilder;
import org.frcteam2910.common.robot.drivers.NavX;

public class DrivetrainSubsystem extends Subsystem {
    private static final double TRACKWIDTH = 24.5;  
    //Length between the wheels width wise in inches
    private static final double WHEELBASE = 24.5;     
    //Length between the wheels length wise in inches


    //determines the angle for each motor so the will drive straight
    //look at README on the left side bar for how to set up (steps 3-9)
    private static final double FRONT_LEFT_ANGLE_OFFSET = -Math.toRadians(176.4); //123
    private static final double FRONT_RIGHT_ANGLE_OFFSET =  -Math.toRadians(32.4);  //88.1
    private static final double BACK_LEFT_ANGLE_OFFSET = -Math.toRadians(352.4); //171
    private static final double BACK_RIGHT_ANGLE_OFFSET = -Math.toRadians(287); //110.4
    

    private static DrivetrainSubsystem instance;


    //:/GodMode = True 
    //:/AndrewSucks,LikeREALLLLLYSucks = VERY TRUE


    //The setup for each indiviual swerve module, first it gets the frame size and position, next it gets the encoder and motors for that module
    private final SwerveModule frontLeftModule = new Mk2SwerveModuleBuilder(
            new Vector2(TRACKWIDTH / 2.0, WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.CANConstants.DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER), FRONT_LEFT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();
    private final SwerveModule frontRightModule = new Mk2SwerveModuleBuilder(
            new Vector2(TRACKWIDTH / 2.0, -WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.CANConstants.DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER), FRONT_RIGHT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();
    private final SwerveModule backLeftModule = new Mk2SwerveModuleBuilder(
            new Vector2(-TRACKWIDTH / 2.0, WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.CANConstants.DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER), BACK_LEFT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();
    private final SwerveModule backRightModule = new Mk2SwerveModuleBuilder(
            new Vector2(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0))
            .angleEncoder(new AnalogInput(RobotMap.CANConstants.DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER), BACK_RIGHT_ANGLE_OFFSET)
            .angleMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .driveMotor(new CANSparkMax(RobotMap.CANConstants.DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless),
                    Mk2SwerveModuleBuilder.MotorType.NEO)
            .build();


    //sets up the kitamatics and position for all four modules
    private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
            new Translation2d(TRACKWIDTH / 2.0, WHEELBASE / 2.0),
            new Translation2d(TRACKWIDTH / 2.0, -WHEELBASE / 2.0),
            new Translation2d(-TRACKWIDTH / 2.0, WHEELBASE / 2.0),
            new Translation2d(-TRACKWIDTH / 2.0, -WHEELBASE / 2.0)
    );


    private final Gyroscope gyroscope = new NavX(SPI.Port.kMXP);


    public DrivetrainSubsystem() {
        gyroscope.calibrate();
        gyroscope.setInverted(true); 
        // You might not need to invert the gyro, we do


        //sets name for easy calling in smartDashboard
        frontLeftModule.setName("Front Left");
        frontRightModule.setName("Front Right");
        backLeftModule.setName("Back Left");
        backRightModule.setName("Back Right");
    }


        //allows easy calling for commands
        public static DrivetrainSubsystem getInstance() {
        if (instance == null) {
            instance = new DrivetrainSubsystem();
        }

        return instance;
    }



 
    @Override
    public void periodic() {
        frontLeftModule.updateSensors();
        frontRightModule.updateSensors();
        backLeftModule.updateSensors();
        backRightModule.updateSensors();

        SmartDashboard.putNumber("Front Left Module Angle", Math.toDegrees(frontLeftModule.getCurrentAngle()));
        SmartDashboard.putNumber("Front Right Module Angle", Math.toDegrees(frontRightModule.getCurrentAngle()));
        SmartDashboard.putNumber("Back Left Module Angle", Math.toDegrees(backLeftModule.getCurrentAngle()));
        SmartDashboard.putNumber("Back Right Module Angle", Math.toDegrees(backRightModule.getCurrentAngle()));


        SmartDashboard.putNumber("Gyroscope Angle", gyroscope.getAngle().toDegrees());


       // SmartDashboard.putNumber("Turn Motor Current", frontRightTurnMotor.getOutputCurrent());
       // SmartDashboard.putNumber("Drive Motor Currrent", frontRightDriveMotor.getOutputCurrent());


        frontLeftModule.updateState(TimedRobot.kDefaultPeriod);
        frontRightModule.updateState(TimedRobot.kDefaultPeriod);
        backLeftModule.updateState(TimedRobot.kDefaultPeriod);
        backRightModule.updateState(TimedRobot.kDefaultPeriod);
    }

    public void drive(Translation2d translation, double rotation, boolean fieldOriented) {
        rotation *= 2.0 / Math.hypot(WHEELBASE, TRACKWIDTH);
        ChassisSpeeds speeds;
        if (fieldOriented) {
            speeds = ChassisSpeeds.fromFieldRelativeSpeeds(translation.getX(), translation.getY(), rotation,
                    Rotation2d.fromDegrees(gyroscope.getAngle().toDegrees()));
        } else {
            speeds = new ChassisSpeeds(translation.getX(), translation.getY(), rotation);
        }

        SwerveModuleState[] states = kinematics.toSwerveModuleStates(speeds);
        frontLeftModule.setTargetVelocity(states[0].speedMetersPerSecond, states[0].angle.getRadians());
        frontRightModule.setTargetVelocity(states[1].speedMetersPerSecond, states[1].angle.getRadians());
        backLeftModule.setTargetVelocity(states[2].speedMetersPerSecond, states[2].angle.getRadians());
        backRightModule.setTargetVelocity(states[3].speedMetersPerSecond, states[3].angle.getRadians());
    }

    public void resetGyroscope() {
        gyroscope.setAdjustmentAngle(gyroscope.getUnadjustedAngle());
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new DriveCommand());
    }
}                  