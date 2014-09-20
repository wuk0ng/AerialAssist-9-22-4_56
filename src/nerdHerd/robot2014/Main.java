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
import edu.wpi.first.wpilibj.DriverStation;
import nerdHerd.util.Thresholder;

public class Main extends NerdyBot{
    Joystick leftJoystick, rightJoystick, articJoystick;
    NerdyTimer autonomousTimer = new NerdyTimer(2.0), autonomousShooterTimer = new NerdyTimer(5.0);
    PainTrain myRobot;
    Timer timer;
    double shotValue, holdValue, thresholdValue;
    
    
    
    double leftSpeed = 0.0, rightSpeed = 0.0;
    boolean isRunningAutonomous = false;

        thresholdValue = (DriverStation.getInstance().getAnalogIn(3)) * 100;
        if(thresholdValue > 1){
            thresholdValue = 1
        }
        
        System.out.println("thresholdValue: " + m_thresholdValue);
        shotValue = (DriverStation.getInstance().getAnalogIn(1)) * 100;
        System.out.println("Shot: " + shotValue);
        holdValue = (DriverStation.getInstance().getAnalogIn(2)) * 100;
        System.out.println("Hold: " + holdValue);
    

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
//        filer           = new Filer();

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
//        filer.connect();
//        filer.println("Time , EncoderA, EncoderB, ShootEncoder, Shoot, Retract");
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
        boolean shootButton =  articJoystick.getRawButton(1);
        boolean holdButton  =  articJoystick.getRawButton(5);

        
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
        
       if(shootButton){
           myRobot.shoot(shootButton);
       }else if(holdButton){
           myRobot.holdPosition(holdButton);
        }
        
        myRobot.   setLeft(leftSpeed);
        myRobot.   setRight(rightSpeed);
        
          SmartDashboard.putNumber("Encoder", m_encode.get());
        SmartDashboard.putNumber("Shoot Timer", m_shootTimer.get());
        SmartDashboard.putNumber("Retract Timer", m_retractTimer.get());
        SmartDashboard.putNumber("shootTime", m_shootTime);
        SmartDashboard.putNumber("retractTime", m_retractTime);
        SmartDashboard.putNumber("thresholdValue: ", m_thresholdValue);
        SmartDashboard.putNumber("shotValue", shotValue);
        SmartDashboard.putNumber("holdValue", holdValue);
        
//        report('T');
    }
    
    public void teleopContinous(){
        myRobot.run();
    }
    
    public void disabledInit(){
        myRobot.disable();
        
    }
    
    public void disabled(){
        myRobot.run();
//        report('D');
//        filer.close();
    }
    
//    private void report(char robotState)
//    {                
//        double time = timer.get();
//        filer.println(time + "," +robotState + ","
//                + myRobot.getLeftGBEncoder() + "," 
//                + myRobot.getRightGBEncoder() + "," 
//                + myRobot.getShooterEncoder() + ","
//                + myRobot.getShootTime() + ","
//                + myRobot.getRetractTime() + ","
//        );
//        SmartDashboard.putDouble("Encoder Left", myRobot.getLeftGBEncoder());
//        SmartDashboard.putDouble("Encoder Right", myRobot.getRightGBEncoder());
//        SmartDashboard.putDouble("Encoder Shooter", myRobot.getShooterEncoder());
    }

}
