package tmc.dotbotandroid_v1;

/**
 * Created by Steven on 4/06/2016.
 */
public class Calculator {

    public int[] calculateMotorInputs(float y_acceleration, float z_acceleration, boolean startButtonPressed) { //calculator

        int power = Math.round(z_acceleration * 10);
        int steering = Math.round(y_acceleration * 5);
        int motorLeft = power + steering;
        int motorRight = power - steering;

        // Limitation to minimum and maximum values for servo motors
        if (motorLeft < -100){motorLeft = -100;}
        if (motorLeft > 100){motorLeft = 100;}
        if (motorRight < -100){motorRight = -100;}
        if (motorRight > 100){motorRight = 100;}

        // Deadband to keep the dotbot steady when the value is close to zero
        if (Math.abs(motorLeft) < 10) {motorLeft = 0;}
        if (Math.abs(motorRight) < 10) {motorRight = 0;}

        //Stop button functionality: the values are zero when the start button is not pressed
        if (!startButtonPressed){
          motorRight = 0;
          motorLeft = 0;
        }
        return new int[]{motorLeft, motorRight, power, steering};
    }
}