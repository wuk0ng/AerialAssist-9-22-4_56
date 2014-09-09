/*Badger 4/9/2014
This code is for paintrain without the shooter mechanism.
This code will utilize the intake and drive system.
This is based on having all talons
*/
package nerdHerd.robot2014;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.KinectStick;
import nerdHerd.util.NerdyBot;
import nerdHerd.util.NerdyTimer;
import nerdHerd.util.ThreeCimBallShifter;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Main extends NerdyBot{
    Joystick leftJoystick, rightJoystick, articJoystick;
    NerdyTimer autonomousTimer = new NerdyTimer(2.0), autonomousShooterTimer = new NerdyTimer(5.0);
    PainTrain myRobot;
    Timer timer;
    //KinectStick leftStick, rightStick;
    boolean shoot = false;
    
    
    double leftSpeed = 0.0, rightSpeed = 0.0;
    boolean isRunningAutonomous = false;
    

    public void robotInit(){
        myRobot         = new PainTrain();
        leftJoystick    = new Joystick(1);
        rightJoystick   = new Joystick(2);
        articJoystick   = new Joystick(3);
        autonomousTimer.start();
        timer           = new Timer();
        //leftStick = new KinectStick(1);
        //rightStick = new KinectStick(2);
        autonomousShooterTimer.start();

    }

    public void autonomousPeriodic() {
        myRobot.setLeft(-0.5);
        myRobot.setRight(-0.5);
        if(timer.get() >= 2.0){
            myRobot.setLeft(0.0);
            myRobot.setRight(0.0);
            timer.stop();
        }
    }
    
    public void autonomousContinous(){
       myRobot.run();
       report('A');
    }
    
    public void autonomousInit(){
        myRobot.enable();
        myRobot.shift(ThreeCimBallShifter.GearNumber.kFirstGear);
        timer.start();
        timer.reset();
    }
    
    public void teleopInit() {
        myRobot.enable();
        timer.start();
        timer.reset();
    }

    public void teleopPeriodic() {
        leftSpeed           =  leftJoystick.getY();
        rightSpeed          =  rightJoystick.getY();
        boolean shiftUp     =  leftJoystick.getRawButton(3);
        boolean shiftDown   =  leftJoystick.getRawButton(2);
        boolean intakeIn    =  articJoystick.getRawButton(11);
        boolean intakeOut   =  articJoystick.getRawButton(10);
        boolean intake      =  articJoystick.getRawButton(3);
        boolean outTake     =  articJoystick.getRawButton(2);
        boolean shoot1       =  articJoystick.getRawButton(6);
        boolean shoot2      = articJoystick.getRawButton(4);
        boolean shoot3      = articJoystick.getRawButton(1);

        
        if(intakeIn){
            myRobot.retractIntake();
        }else if(intakeOut){
            myRobot.extendIntake();
        }

        if(intake){
            myRobot.startIntake(1.0);
        }else if(outTake){
            myRobot.startIntake(-1.0);
        }else{
            myRobot.stopIntake();
        }
        
        
        if(shiftUp){
            myRobot.shift(ThreeCimBallShifter.GearNumber.kSecondGear);
        }else if(shiftDown){
            myRobot.shift(ThreeCimBallShifter.GearNumber.kFirstGear);
        }
        
        if(shoot1){
            myRobot.shoot(0.2);
        } else if (shoot2) {
            myRobot.shoot(0.4);
        } else if (shoot3) {
            myRobot.shoot(0.6);
        }
        
        myRobot.   setLeft(leftSpeed);
        myRobot.   setRight(rightSpeed);
        
        report('T');
    }
    
    public void teleopContinous(){
        myRobot.run();
    }
    
    public void disabledInit(){
        myRobot.disable();
        
    }
    
    public void disabled(){
        myRobot.run();
        report('D');
    }
    
    private void report(char robotState)
    {                
        double time = timer.get();
        SmartDashboard.putNumber("Encoder Left", myRobot.getLeftGBEncoder());
        SmartDashboard.putNumber("Encoder Right", myRobot.getRightGBEncoder());
        SmartDashboard.putNumber("Encoder Shooter", myRobot.getShooterEncoder());
    }

}
