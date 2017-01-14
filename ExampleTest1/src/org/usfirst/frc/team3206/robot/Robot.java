
package org.usfirst.frc.team3206.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;


public class Robot extends SampleRobot {
    RobotDrive myRobot;
    Joystick stick0; // all joysticks will be named as such stickX where X is the position on the Driver Station
    Joystick stick1;
    DigitalOutput dio0; // all dio will be named as such dioX where X is the DIO pin on the roboRIO
    DigitalInput dio1;
    Compressor c;  // all compressors will be named c
    DoubleSolenoid solenoid0-1; // all solenoids will be named as such solenoidX or solenoidX-Y where for single solenoids X is the connection on the PCM and for double solenoids X is the first connection and Y is the second
    SmartDashboard dash;
    CameraServer cs;
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    SendableChooser chooser;

    public Robot() {
        myRobot = new RobotDrive(0, 1);
        myRobot.setExpiration(0.1);
        stick0 = new Joystick(0);
        stick1 = new Joystick(1);
        dio0 = new DigitalOutput(0);
        dash = new SmartDashboard();
        dio1 = new DigitalInput(1);
        c = new Compressor(0);
        cs = new CameraServer();
        solenoid0-1 = new DoubleSolenoid(0,1);
    }
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto modes", chooser);
        cs.
    }

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the if-else structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomous() {
    	
    	String autoSelected = (String) chooser.getSelected();
//		String autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    	
    	switch(autoSelected) {
    	case customAuto:
            myRobot.setSafetyEnabled(false);
            myRobot.tankDrive(1.0, 1.0);
            c.start();
            Timer.delay(5.0);
            myRobot.tankDrive(0.0, 0.0);
            c.stop();
            break;
    	case defaultAuto:
    	default:
            myRobot.setSafetyEnabled(false);
            myRobot.tankDrive(1.0, 1.0);
            c.start();
            Timer.delay(5.0);
            myRobot.tankDrive(0.0, 0.0);
            c.stop();
    		
            break;
    	}
    }
    

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(false);
        while (isOperatorControl() && isEnabled()) {
        	if(stick0.getRawButton(4)) {
        		c.setClosedLoopControl(true); // this method c.setClosedLoopControl(boolean input);
        									//  is what we use to control the compressor
        									// when it is set to true it will run the compressor until the pressure
        									// sensor reads true which is at 120 psi
        	} else if(stick0.getRawButton(5)) {
        		c.setClosedLoopControl(false);
        	}
        	if(stick0.getRawButton(1)) {
        		solenoid0-1.set(DoubleSolenoid.Value.kForward);
        	} else if(stick0.getRawButton(2)) {
        		solenoid0-1.set(DoubleSolenoid.Value.kReverse);
        	} else {
        		solenoid0-1.set(DoubleSolenoid.Value.kOff);
        	}
        	if(!dio1.get()) {
        		myRobot.tankDrive(1.0, 1.0);
        	} else {
        		myRobot.tankDrive(0.0, 0.0);
        	}
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}
