
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
    DigitalOutput led;
    DigitalInput button;
    Joystick stick;
    Joystick stick1;
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    SendableChooser chooser;
    Compressor comp;
    DoubleSolenoid sol;

    public Robot() {
        myRobot = new RobotDrive(1, 0);
        myRobot.setExpiration(0.1);
        stick = new Joystick(0);
        stick1 = new Joystick(1);
        led = new DigitalOutput(0);
        button = new DigitalInput(1);
        comp = new Compressor(0);
        sol = new DoubleSolenoid(1,2);
    }
    
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto modes", chooser);
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
            myRobot.drive(-0.5, 1.0);	// spin at half speed
            Timer.delay(2.0);		//    for 2 seconds
            myRobot.drive(0.0, 0.0);	// stop robot
            break;
    	case defaultAuto:
    	default:
            myRobot.setSafetyEnabled(false);
            myRobot.drive(-0.5, 0.0);	// drive forwards half speed
            Timer.delay(2.0);		//    for 2 seconds
            myRobot.drive(0.0, 0.0);	// stop robot
            break;
    	}
    }

    /**
     * Runs the motors with arcade steering.
     */
    public void operatorControl() {
        myRobot.setSafetyEnabled(true);    
        while (isOperatorControl() && isEnabled()) {
           // myRobot.tankDrive(stick, stick1); // drive with arcade style (use right stick)
            //Timer.delay(0.005);		// wait for a motor update time
        	/*for (int i = 0 ; i < 2 ; i++){
        		led.set(true);
        		Timer.delay(0.5);
        		led.set(false);
        		Timer.delay(0.5); 
        	}
        	Timer.delay(1);
        	led.set(true);
    		Timer.delay(1.5);
    		led.set(false);
    		Timer.delay(1);
    		led.set(true);
    		Timer.delay(0.5);
    		led.set(false);
    		Timer.delay(1);
    		
    		for (int i = 0 ; i < 2 ; i++){
        		led.set(true);
        		Timer.delay(0.5);
        		led.set(false);
        		Timer.delay(0.5); 
    		}
    		led.set(true);
    		Timer.delay(1.5);
    		led.set(false);
    		Timer.delay(1);
    		led.set(true);
    		Timer.delay(0.5);
    		led.set(false);
    		*/
    	//pneumatic
    			//comp.start();
    			//Timer.delay
    		//if(!button.get())
    		//{
    			comp.setClosedLoopControl(true);
    			comp.start();
    			Timer.delay(120);
    			sol.set(DoubleSolenoid.Value.kForward);
    			Timer.delay(.5);
    			sol.set(DoubleSolenoid.Value.kReverse);
    		//}
    		//else
    		// {	comp.setClosedLoopControl(false);
			//	}
    		
        	
        	
        	//if (!button.get()){
        	
        	}

        }
    

    /**
     * Runs during test mode
     */
    public void test() {
    }
}
