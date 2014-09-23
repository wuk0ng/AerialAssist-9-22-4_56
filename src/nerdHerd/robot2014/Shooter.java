/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/**
 * @author Mr. Mallory
 * @version 20140922
 */
package nerdHerd.robot2014;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import nerdHerd.util.Thresholder;
import nerdHerd.util.InputValues;

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
    boolean m_shooting = false;
    boolean m_retracting = false;
    boolean m_holding;
    double power = 0;
    double m_shotValue, m_holdValue = 0;
    double error = 0;
    double m_pHold = .025;
    double m_pShoot = 0.2;
    double m_shootTime = 0.5;
    double m_retractTime = 2.0;
    double m_thresholdValue; // max power, not used
    boolean m_shootButton, m_holdButton;
    final double retractPower = 0.2;

    public Shooter(int encoderA, int encoderB, int victor1, int victor2, int victor3) {
        m_encode = new Encoder(encoderA, encoderB);
        m_encode.start();
        m_encode.reset();
        m_motor1 = new Victor(victor1);
        m_motor2 = new Victor(victor2);
        m_motor3 = new Victor(victor3);
        m_shootTimer = new Timer();
        m_retractTimer = new Timer();

    }

    public void run() {

        if (false == m_shooting && false == m_retracting && false == m_holding) {
            if (m_shootButton) {
                m_shooting = true;
                m_shootTimer.reset();
                m_shootTimer.start();
                Shoot();
            } else {
                m_holdValue = 0;
                holdPosition();
            }
        } else if (false == m_shooting && false == m_retracting && true == m_holding) {
            if (m_shootButton){
                m_shooting = true;
                m_shootTimer.reset();
                m_shootTimer.start();
                Shoot();
            } else {
                m_holdValue = InputValues.holdVal();
                holdPosition();
            } 
        } else if (false == m_shooting && true == m_retracting && false == m_holding) {
            m_holdValue = 0;
            Retract();
        } else if (false == m_shooting && true == m_retracting && true == m_holding) {
            m_holdValue = InputValues.holdVal();
            Retract();
        } else if (true == m_shooting && false == m_retracting && false == m_holding ){
            Shoot();
        } else if (true == m_shooting && false == m_retracting && true == m_holding){
            m_holdValue = InputValues.holdVal();
            Shoot();
        } else{
            System.out.println("WHAT IS HAPPENING");
            SmartDashboard.putString("Shooter Error Message", "WHAT IS HAPPENING");
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
    
    private void Shoot() {

        if (m_encode.get() >= m_shotValue || m_shootTimer.get() > m_shootTime) {
            m_shooting = false;
            m_retracting = true;
            m_shootTimer.stop();
            m_shootTimer.reset();
            m_retractTimer.start();
            m_retractTimer.reset();
            Retract();
        } else {
            error = m_shotValue - m_encode.get();
            power = -Thresholder.threshold(m_pShoot * error);
        }
    }

    private void Retract() {
        if (m_encode.get() <= m_holdValue || m_retractTimer.get() > m_retractTime) {
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
        error = m_holdValue - m_encode.get();
        power = Thresholder.threshold(m_pHold * error);
    }

    public void disable() {
        m_motor1.set(0.0);
        m_motor2.set(0.0);
        m_motor3.set(0.0);
    }
}
