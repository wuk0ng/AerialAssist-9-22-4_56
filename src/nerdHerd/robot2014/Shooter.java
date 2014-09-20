/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/**
 * @author Dietrich Henson
 * @version 20140919
 */
package nerdHerd.robot2014;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import nerdHerd.util.Thresholder;
//import nerdHerd.util.Filer;

/**
 * The VM is configured to automatically run this class,and to call the
 * functions corresponding to each mode,as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project,you must also update the manifest file in the resource
 * directory.
 */
public class Shooter {

    Victor m_motor1, m_motor2, m_motor3;
    Encoder m_encode;
    Timer m_shootTimer, m_retractTimer;
    Thresholder thresh; 
    boolean m_shooting = false;
    boolean m_retracting = false;
    double power = 0;
    double shotValue, holdValue;
    double error = 0;
    double m_p = .025;
    double m_shootTime = 0.5;
    double m_retractTime = 2.0;
    double m_thresholdValue; // max power.
    boolean m_shootButton, m_holdButton, m_holdButton;
    const retractPower = 0.2;

    public Shooter(int encoderA, int encoderB, int victor1, int victor2, int victor3) {
        m_encode = new Encoder(encoderA, encoderB);
        m_encode.start();
        m_encode.reset();
        thresh = new Thresholder();
        m_motor1 = new Victor(victor1);
        m_motor2 = new Victor(victor2);
        m_motor3 = new Victor(victor3);

        m_shootTimer = new Timer();
        m_retractTimer = new Timer();
       
    }

    public void run() {

        holdValue = 0.0;     
        
         if (false == m_shooting && false == m_retracting && false == m_holding) {
            if (m_shootButton) {
                m_shooting = true;
                m_shootTimer.reset();
                m_shootTimer.start();
                Shoot();
            } else {
                holdPosition();
            }
        }
          else if(false == m_shooting && false == m_retracting && true == m_holding){
                   
        }
        } else if (false == m_shooting && true == m_retracting) {
            Retract();
        } else if (true == m_shooting && false == m_retracting) {
            Shoot();
        } else { // if(true == m_shooting && true == m_retracting){
            // should never happen.
            System.out.println("(true == shooting && true == retracting) should never happen");
        }

        m_motor1.set(power);
        m_motor2.set(power);
        m_motor3.set(power);
      
    }

    public void shoot(boolean shootButton) {
        m_shootButton = shootButton;
    }

    public void hold(boolean holdButton) {
        m_holdButton = holdButton;
    }
    
    public void thresh(double value){
        Threshold(value);
    }

    private void Shoot() {

        if (m_encode.get() >= shotValue || m_shootTimer.get() > m_shootTime) {
            m_shooting = false;
            m_retracting = true;
            m_shootTimer.stop();
            m_shootTimer.reset();
            m_retractTimer.start();
            m_retractTimer.reset();
            Retract();
        } else {
            error = shotValue - m_encode.get();
            power = -Thresholder.threshold(m_p * error);
        }
    }


    private void Retract() {
        if (m_encode.get() <= holdValue || m_retractTimer.get() > m_retractTime) {
            power = 0;
            m_shooting = false;
            m_retracting = false;
            m_retractTimer.stop();
            m_retractTimer.reset();
        } else { // m_encode.get > 0 && m_retractTimer.get() < m_retractTime
            power = retractPower;
        }
    }

    private void holdPosition() {
        error = holdValue - m_encode.get();
        power = Thresholder.threshold(m_p * error);
    }

    public void disable() {
        m_motor1.set(0.0);
        m_motor2.set(0.0);
        m_motor3.set(0.0);
    }
}
