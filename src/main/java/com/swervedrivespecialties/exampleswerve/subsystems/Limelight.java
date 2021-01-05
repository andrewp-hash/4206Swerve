/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.swervedrivespecialties.exampleswerve.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.swervedrivespecialties.exampleswerve.RobotMap;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Limelight extends SubsystemBase {
  //About: Instance NetworkTable to obtain realtime data from the Limelight
  public NetworkTable ll = NetworkTableInstance.getDefault().getTable("limelight");

  public NetworkTableEntry target = ll.getEntry("tv");
  public NetworkTableEntry horizontalOffset = ll.getEntry("tx");
  public NetworkTableEntry verticalOffset = ll.getEntry("ty");
  public NetworkTableEntry targetArea = ll.getEntry("ta");
  public NetworkTableEntry skew = ll.getEntry("ts");
  public NetworkTableEntry led = ll.getEntry("ledMode");
  public NetworkTableEntry cameraMode = ll.getEntry("camMode");

  private static Limelight instance;

  public static Limelight getInstance() {
    if (instance == null) {
        instance = new Limelight();
    }

    return instance;
}


  public Limelight() {

  }





  //---------------------------Place Getters Here-------------------------------

  //Name: Matthew, Brennan  
  //About: Returns x offset angle of the robot from the target
  public double getHorizontalOffset(){
    return horizontalOffset.getDouble(0.0);
  }
  
  //Name: Matthew, Brennan 
  //About: Returns y offset angle of the robot from the target
  public double getVerticalOffset(){
    return verticalOffset.getDouble(0.0);
  }
  
  //Name: Brennan 
  //About: Returns the angle difference from the robot to the target 
  public double getSkew(){
    return skew.getDouble(0.0);
  }

  //Name: Matthew, Brennan 
  //About: Returns if the limelight has a valid target or not 
  public Boolean validTarget(){
    //Returns true if target is in frame, false is no valid target
    if (target.getDouble(0) == 1) return true;
    else return false;
  }

  //Name: Matthew, Brennan 
  //About: Returns the largest target the limelight can see 
  public double LargestTarget(){
    return targetArea.getDouble(0.0);
  }




  //Name: Brennan, Dante(aka math man)  
  //About: calc the distance to the target
  public double distanceToTarget(){
    double offsetAngle = verticalOffset.getDouble(0);
    return (RobotMap.LimelightConstants.goalHeight-RobotMap.LimelightConstants.cameraHeight)/Math.tan(RobotMap.LimelightConstants.mountAngle + ((offsetAngle*2*Math.PI)/360));
  }


  //---------------------------Place Setters Here-------------------------------
  
  //Name: Matthew, Brennan 
  //About: sets the LED from off to on 
  public void setLED(int mode){
    //Sets LED mode (1: Off, 2: Blink, 3: On)
    switch (mode){
      case 1:
        led.setDouble(1);
        break;
      case 2:
        led.setDouble(2);
        break;
      case 3:
        led.setDouble(3);
        break;
      default:
        led.setDouble(0);
        break;
    }
    return;
  }

  //Name: Matthew 
  //About: Toggles vision processing on the Limelight (0: On, 1: Off with Driver Mode for increased exposure)
  public void camMode(int mode){
    if (mode == 1) cameraMode.setDouble(1);
    else cameraMode.setDouble(0);
    return;
  }

  //---------------------------Place Others Here-------------------------------
  
  //Name: Matthew, Brennan 
  //About: Rotates the robot to the target based on the horizontal offset
  public double rotatetoTarget(double PID){
    //Calculates power necessary to shift drivetrain and align with the target
    double power = horizontalOffset.getDouble(0)*PID;
    return power;
  }





  @Override
  public void periodic() {
    SmartDashboard.putNumber("distanceToTarget", distanceToTarget());      
    SmartDashboard.putNumber("test lime", 2);


  }

}
