/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nerdHerd.util;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author M0nkey
 */
public class InputValues {
    
    public static double shotVal(){
        return (DriverStation.getInstance().getAnalogIn(1)) * 100;
    }
    
    public static double holdVal(){
        return (DriverStation.getInstance().getAnalogIn(2)) * 100;
    }
    
}

//
///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//package nerdHerd.util;
//
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import edu.wpi.first.wpilibj.DriverStation;
//
///**
// *
// * @author M0nkey
// */
//public class InputValues {
//    
//    public InputValues(){ 
//    }
//    
//    public double shotVal(){
//        return (DriverStation.getInstance().getAnalogIn(1)) * 100;
//    }
//    
//    public double holdVal(){
//        return (DriverStation.getInstance().getAnalogIn(2)) * 100;
//    }
//    
//}
