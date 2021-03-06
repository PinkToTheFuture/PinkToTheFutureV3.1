package org.firstinspires.ftc.teamcode;

// Add a gyroscope here. Needed for correcting motor differences.
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Ari on 01-06-17.
 * Credit to gearsincorg for sample code. You can find his original sample here: https://github.com/gearsincorg/FTCVuforiaDemo
 *
 * This file is not an OpMode.
 * This defines all the hardware required for a four wheel omni-bot.
 * It also moves the robot around.
 * To-do list: Add a gyroscope to correct motor differences.
 *
 * This hardware class assumes the following motors have been configured: (The 'front' of the robot is the way the camera is facing.)
 * Top-left motor:
 * Top-right motor:    topRight
 * Bottom-left motor:  bottomLeft
 * Bottom-right motor: bottomRight
 *
 * These motors correspond to four motors that are on a 45* angle, on the corner of a square.
 * Yes, it's a bad description, I'm sorry about that.
 * Each motor is attached to an omni-wheel.
 *
 * Robot motion is defined in three different axis motions:
 * Axial (X axis)   : Forwards / Backwards, with +ve being forward.
 * Lateral (Y axis) : Side to side strafing, with +ve moving to the right.
 * Yaw              : Rotation around the Z axis, with +ve being counter-clockwise.
 */
public class VuforiaOmniSetStraight {

    // Private members.
    private LinearOpMode myOpMode;

    // This is really the best way of defining motors, we should probably do this more often.
    private DcMotor LFdrive   = null;
    private DcMotor RFdrive   = null;
    private DcMotor LBdrive   = null;
    private DcMotor RBdrive   = null;

    private double driveAxial   = 0; // +ve is forward.
    private double driveLateral = 0; // +ve is right.
    private double driveYaw     = 0; // +ve is CCW.

    // Begin code by creating a constructor. Used to create an object from this class in Vuforia_TeleOp.
    public VuforiaOmniSetStraight(){
    }

    /**
     * Init the motors, set them up.
     */
    public void initDrive(LinearOpMode opMode){

        // Save reference to Hardware map.
        myOpMode = opMode;

        // Define and init the motors.
        LFdrive   = myOpMode.hardwareMap.get(DcMotor.class, "LFdrive");
        RFdrive   = myOpMode.hardwareMap.get(DcMotor.class, "RFdrive");
        LBdrive   = myOpMode.hardwareMap.get(DcMotor.class, "LBdrive");
        RBdrive   = myOpMode.hardwareMap.get(DcMotor.class, "RBdrive");

        // Set the direction of all the motors. We DON'T want any motors to be reversed.
        // Positive inputs rotate counter-clockwise.
        LFdrive.setDirection(DcMotorSimple.Direction.FORWARD); // I guess we can use DcMotorSimple here.
        RFdrive.setDirection(DcMotorSimple.Direction.FORWARD);
        LBdrive.setDirection(DcMotorSimple.Direction.FORWARD);
        RBdrive.setDirection(DcMotorSimple.Direction.FORWARD);

        // Assuming we hve encoders installed, use RUN_USING_ENCODER
        // Also, this is actually a void. Just makes things quicker.
        setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        // Stop all robot motion by setting each axis value to zero.
        setMoveRobot( 0,0,0 );
    }

    public void manualDrive()  {
        // Just copy-pasting this, will edit later.
        // In this mode the Left stick moves the robot fwd & back, and Right & Left.
        // The Right stick rotates CCW and CW.

        //  (note: The joystick goes negative when pushed forwards, so negate it)
        setAxial(-myOpMode.gamepad1.left_stick_y);
        setLateral(myOpMode.gamepad1.left_stick_x);
        setYaw(-myOpMode.gamepad1.right_stick_x);
    }

    /**
     *  Set the speed levels to motors based on axes requests.
     *  In the sample code, this is called moveRobot. But because I don't want a headache, I rename it here.
     *  Also, turns out you can name void's as the same thing multiple times by having different params
     */
    public void setMoveRobot(double axial, double lateral, double yaw){
        setAxial(axial);
        setLateral(lateral);
        setYaw(yaw);
        moveRobot();
    }
    public void moveRobot(){
        // Calculate the required motor speeds to achieve axis motions.
        // Remember, motors spin CCW.
        // If you are confused about this, ask me. It's easier to explain visually.
        // To-do list: Add fudge factor, preferably using a Gyro.

        double LFpower = driveYaw - driveAxial -driveLateral; // Top left motor.
        double RFpower = driveYaw + driveAxial - driveLateral; // Top right motor.
        double LBpower = driveYaw - driveAxial + driveLateral; // Bottom left motor.
        double RBpower = driveYaw + driveAxial + driveLateral; // Bottom right power.


        // Normalise all the motor speeds to no values exceed 100% (which is really 1.0)
        // The /= compound operator means divide the left value by the right, and assign that number into the left value.
        // In Vuforia_Navigation, all motors are multiplied by 0.00**, making the value be between 1 and -1.

        double max = Math.max(Math.abs(LFpower), Math.abs(RFpower)); // See which value is larger: topLeft or topRight.
        max = Math.max(max, Math.abs(LBpower)); // See which value is larger: The above max or bottomLeftPower
        max = Math.max(max, Math.abs(RBpower)); // See which value is larger: The above max or bottomRightPower
        if (max > 1.0){ // If any of the values are larger than 1, divide every motor by that number.
            LFpower /= max;
            RFpower /= max;
            LBpower /= max;
            RBpower /= max;
        }

        // Set motor power levels. Will require changes.
        LFdrive.setPower(LFpower);
        RFdrive.setPower(RFpower);
        LBdrive.setPower(LBpower);
        RBdrive.setPower(RBpower);

        // Display telemetry. Requires changing.
        myOpMode.telemetry.addData("Axes  ", "A[%+5.2f], L[%+5.2f], Y[%+5.2f]", driveAxial, driveLateral, driveYaw);
        myOpMode.telemetry.addData("Wheels", "TL[%+5.2f], TR[%+5.2f], BL[%+5.2f], BR[%+5.2f]", LFpower, RFpower, LBpower, RBpower);

    }

    // Create the functions that we use to set variables. Also, sets the range that the variable can be set to.
    public void setAxial(double axial){
        driveAxial = Range.clip(axial, -1, 1);}

    public void setLateral(double lateral){
        driveLateral = Range.clip(lateral, -1, 1); }

    public void setYaw(double yaw){
        driveYaw = Range.clip(yaw, -1, 1); }

    /**
     * Sets all the motors to the same mode.
     */
    public void setMode(DcMotor.RunMode mode ){
        LFdrive.setMode(mode);
        RFdrive.setMode(mode);
        LBdrive.setMode(mode);
        RBdrive.setMode(mode);
    }
}
