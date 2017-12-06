package org.usfirst.frc.team3206.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {
//	RobotDrive myRobot = new RobotDrive(0, 1);
	Joystick stick = new Joystick(0);
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();
	Compressor c = new Compressor(0);
	Solenoid Sole0 = new Solenoid(0);
	Solenoid Sole1 = new Solenoid(1);
	Solenoid Sole2 = new Solenoid(2);
	Solenoid Sole3 = new Solenoid(3);
	Solenoid Sole4 = new Solenoid(4);
	Servo servo = new Servo(2);
	
	public Robot() {
//		myRobot.setExpiration(0.1);
	}

	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);
		SmartDashboard.putData("Auto modes", chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * if-else structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomous() {
//		String autoSelected = chooser.getSelected();
//		// String autoSelected = SmartDashboard.getString("Auto Selector",
//		// defaultAuto);
//		System.out.println("Auto selected: " + autoSelected);
//
//		switch (autoSelected) {
//		case customAuto:
//			myRobot.setSafetyEnabled(false);
//			myRobot.drive(-0.5, 1.0); // spin at half speed
//			Timer.delay(2.0); // for 2 seconds
//			myRobot.drive(0.0, 0.0); // stop robot
//			break;
//		case defaultAuto:
//		default:
//			myRobot.setSafetyEnabled(false);
//			myRobot.drive(-0.5, 0.0); // drive forwards half speed
//			Timer.delay(2.0); // for 2 seconds
//			myRobot.drive(0.0, 0.0); // stop robot
//			break;
//		}
	}

	/**
	 * Runs the motors with arcade steering.
	 */
	@Override
	public void operatorControl() {
//		myRobot.setSafetyEnabled(true);
		int Sole = 0;
		c.setClosedLoopControl(true);
		while (isOperatorControl() && isEnabled()) {
//			myRobot.arcadeDrive(stick); // drive with arcade style (use right
//										// stick)
//			Timer.delay(0.005); // wait for a motor update time
			if(stick.getRawButton(4)) {
				c.setClosedLoopControl(true);
			} else if(stick.getRawButton(5)) {
				c.setClosedLoopControl(false);
			}
			
			SmartDashboard.putBoolean("enabled", c.enabled());
			SmartDashboard.putBoolean("PressureSwitchValue", c.getPressureSwitchValue());
			SmartDashboard.putDouble("CompressorCurrent", c.getCompressorCurrent());
			
			if(stick.getRawButton(6)) {
				Sole = 0;
			} else if(stick.getRawButton(7)) {
				Sole = 1;
			} else if(stick.getRawButton(8)) {
				Sole = 2;
			} else if(stick.getRawButton(9)) {
				Sole = 3;
			} else if(stick.getRawButton(10)) {
//				Sole = 4;
			}
			
			switch(Sole) {
			case 0: Sole0.set(stick.getRawButton(1)); break;
			case 1: Sole1.set(stick.getRawButton(1)); break;
			case 2: Sole2.set(stick.getRawButton(1)); break;
			case 3: Sole3.set(stick.getRawButton(1)); break;
			case 4: Sole4.set(stick.getRawButton(1)); break;
			default:
				Sole0.set(false);
				Sole1.set(false);
				Sole2.set(false);
				Sole3.set(false);
				Sole4.set(false);
				break;
			}
			
			servo.set(stick.getZ());
			
			
		}
		c.setClosedLoopControl(false);
	}

	/**
	 * Runs during test mode
	 */
	@Override
	public void test() {
	}
}
