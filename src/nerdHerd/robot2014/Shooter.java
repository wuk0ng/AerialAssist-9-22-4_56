/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package nerdHerd.robot2014;


import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
/**
 * The VM is configured to automatically run this class,and to call the
 * functions corresponding to each mode,as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project,you must also update the manifest file in the resource
 * directory.
 */
public class Shooter {
    Victor motor1,motor2,motor3;
    Timer time, shootTime, retractTime;
    Encoder encoder;
    boolean shooting = false;
    boolean retracting = false;
    double m_timeToShoot;
    double desired = 75;
    double error = 0;
    double p = .2;
    double can1Volt,can1Current,can2Volt,can2Current,can3Volt,can3Current;
    double currentTime;
    
    public Shooter(int encoderA, int encoderB, int victor1, int victor2, int victor3) {
      
        encoder = new Encoder(encoderA, encoderB);
        encoder.start();
        encoder.reset();
        motor1 = new Victor(victor1);//2
        motor2 = new Victor(victor2);//5
        motor3 = new Victor(victor3);//4
      
      time = new Timer();
      shootTime = new Timer();
      retractTime = new Timer();
      
      time.start();
      time.reset();
    }

    public void run() {
        double power;
        if (shooting == false && retracting == false) {
            power = 0.0;
        } else if (shooting == false && retracting == true) {
            power = m_retract();
        } else if (shooting == true && retracting == false) {
            power = m_shoot();
        } else { // shooting == true && retracting == true
            power = 0.0;
        }

        motor1.set(power);
        motor2.set(power);
        motor3.set(power);
    }
    
    public void shoot(double timeToShoot){
        m_timeToShoot = timeToShoot;
        if(false == shooting && false == retracting) {
            shooting = true;
            retracting = false;
            shootTime.start();
            shootTime.reset();
        }
    }
    
    public void disable(){
        shooting = false;
        retracting = false;
    }
    
    private double m_shoot(){        
        if(encoder.get()>= desired || shootTime.get() > m_timeToShoot){
            shooting = false;
            retracting = true;
            shootTime.stop();
            retractTime.start();
            retractTime.reset();
            return .1;       
        }else{  // encoder < desired && shootTime < 0.2
            error = desired-encoder.get();
            return -1;
        }
    }
    private double threshold(double value){
        if(value > 1.0){
            return 1.0;
        }else if(value<-1){
            return -.5;
        }else{
            return value;
        }
    }
    
    private double m_retract( ){
        if(encoder.get()<=0  || retractTime.get() >= 1.0){
            shooting = false;
            retracting = false;
            return 0;
        } else {  // encoder > 0 && retractTime < 1.0
           return .1;           
        }
    }
    
    public int getEncoder(){
        return encoder.get();
    }
    
    public double getShootTime(){
        return shootTime.get();
    }
    
    
    public double getRetractTime(){
        return retractTime.get();
    }
//    public void safety(){
//        if (shootTime >= number ){
//            retracting = true;
//        }else if (rettoo long){
//            disable
//        }
//    }
    
    public void timecheck(){
        boolean isShooterLagging = true;
        boolean isRetractLagging = true;
    }
    
//    public boolean isShooterOut(){
//         return encoder.get() >=0; 
//    }
}