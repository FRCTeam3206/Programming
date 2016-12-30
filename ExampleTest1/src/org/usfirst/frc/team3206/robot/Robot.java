
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

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
    RobotDrive myRobot;
    Joystick stick;
    Joystick stick1;
    DigitalOutput dio0;
    DigitalInput dio1;
    Compressor c;
    DoubleSolenoid cylinder0;
    SmartDashboard dash;
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    SendableChooser chooser;

    public Robot() {
        myRobot = new RobotDrive(0, 1);
        myRobot.setExpiration(0.1);
        stick = new Joystick(0);
        stick1 = new Joystick(1);
        dio0 = new DigitalOutput(0);
        dash = new SmartDashboard();
        dio1 = new DigitalInput(1);
        c = new Compressor(0);
        cylinder0 = new DoubleSolenoid(0,1);
    }
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto modes", chooser);
        c.setClosedLoopControl(false);
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
        	if(stick.getRawButton(4)) {
        		c.setClosedLoopControl(true);
        	} else if(stick.getRawButton(5)) {
        		c.setClosedLoopControl(false);
        	}
        	if(stick.getRawButton(1)) {
        		cylinder0.set(DoubleSolenoid.Value.kForward);
        	} else if(stick.getRawButton(2)) {
        		cylinder0.set(DoubleSolenoid.Value.kReverse);
        	} else {
        		cylinder0.set(DoubleSolenoid.Value.kOff);
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
