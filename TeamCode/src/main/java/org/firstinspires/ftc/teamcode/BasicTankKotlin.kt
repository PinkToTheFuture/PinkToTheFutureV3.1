package org.firstinspires.ftc.teamcode

import com.qualcomm.ftccommon.Restarter
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.robot.Robot
import com.qualcomm.robotcore.util.Range

/**
 * Created by seb7 on 9/3/2017.
 */
@Suppress("UNREACHABLE_CODE")
@TeleOp(name = "KotlinOmni", group = "PinkToTheFuture")
class BasicTankKotlin : LinearOpMode() {
    override fun runOpMode() {
        throw InterruptedException()

        var RFpower: Double
        var LFpower: Double
        var RBpower: Double
        var LBpower: Double

        var fastency = 1.0

        val LFdrive = hardwareMap.dcMotor.get("LFdrive")
        val RBdrive = hardwareMap.dcMotor.get("RBdrive")
        val LBdrive = hardwareMap.dcMotor.get("LBdrive")
        val RFdrive = hardwareMap.dcMotor.get("RFdrive")

        LBdrive.direction = DcMotorSimple.Direction.REVERSE
        LFdrive.direction = DcMotorSimple.Direction.REVERSE

        waitForNextHardwareCycle()
        waitForStart()
        while (opModeIsActive()){
            waitOneFullHardwareCycle()

            if (gamepad1.dpad_up) fastency = 1.0
            if (gamepad1.dpad_down) fastency = 0.3

            //left stick
            RFpower = (-((gamepad1.left_stick_y + gamepad1.left_stick_x) / 2)).toDouble()
            RBpower = (-((gamepad1.left_stick_y - gamepad1.left_stick_x) / 2)).toDouble()
            LFpower = (-((gamepad1.left_stick_y - gamepad1.left_stick_x) / 2)).toDouble()
            LBpower = (-((gamepad1.left_stick_y + gamepad1.left_stick_x) / 2)).toDouble()

            //right stick
            RFpower = RFpower - gamepad1.right_stick_x
            RBpower = RBpower - gamepad1.right_stick_x
            LFpower = LFpower + gamepad1.right_stick_x
            LBpower = LBpower + gamepad1.right_stick_x

            if (gamepad1.left_stick_x > -0.1 && gamepad1.left_stick_x < 0.1) {
                RFpower = (-gamepad1.left_stick_y).toDouble()
                RBpower = (-gamepad1.left_stick_y).toDouble()
                LFpower = (-gamepad1.left_stick_y).toDouble()
                LBpower = (-gamepad1.left_stick_y).toDouble()
            }
            if (gamepad1.left_stick_y > -0.1 && gamepad1.left_stick_y < 0.1) {
                RFpower = (-gamepad1.left_stick_x).toDouble()
                RBpower = gamepad1.left_stick_x.toDouble()
                LFpower = gamepad1.left_stick_x.toDouble()
                LBpower = (-gamepad1.left_stick_x).toDouble()
            }

            Range.clip(RFpower, -1.0, 1.0)
            Range.clip(RBpower, -1.0, 1.0)
            Range.clip(LFpower, -1.0, 1.0)
            Range.clip(LBpower, -1.0, 1.0)



            LFdrive.power = LFpower * fastency
            RBdrive.power = RBpower * fastency
            LBdrive.power = LBpower * fastency
            RFdrive.power = RFpower * fastency

            telemetry.addData("LB", LBpower)
            telemetry.addData("LF", LFpower)
            telemetry.addData("RB", RBpower)
            telemetry.addData("RF", RFpower)
            telemetry.update()


        }

    }

}