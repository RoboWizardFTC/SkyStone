/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.Position;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Basic: Linear OpMode Mecanum", group="Linear Opmode")
//@Disabled
public class BasicOpMode_Linear_Mecanum extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor backLeftDrive = null;
    private DcMotor backRightDrive = null;
    private DcMotor armMotor = null;

//    private Servo topservo = null;
    private Servo clawservo = null;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "front_left");
        frontRightDrive = hardwareMap.get(DcMotor.class, "front_right");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "back_left");
        backRightDrive = hardwareMap.get(DcMotor.class, "back_right");
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");

//        topservo = hardwareMap.get(Servo.class, "topservo");
        clawservo = hardwareMap.get(Servo.class, "clawservo");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        frontRightDrive.setDirection(DcMotor.Direction.FORWARD);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backRightDrive.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

//        topservo.setPosition(0);
        clawservo.setPosition(0);


        while (opModeIsActive()) {



//            double magnitude = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
//            double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
//            double rightX = gamepad1.right_stick_x;
//            final double fld = magnitude * Math.cos(robotAngle) + rightX;
//            final double frd = magnitude * Math.sin(robotAngle) - rightX;
//            final double bld = magnitude * Math.sin(robotAngle) + rightX;
//            final double brd = magnitude * Math.cos(robotAngle) - rightX;
//            frontLeftDrive.setPower(fld);
//            frontRightDrive.setPower(frd);
//            backLeftDrive.setPower(bld);
//            backRightDrive.setPower(brd);

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x /** 1.5*/;
            double rx = gamepad1.right_stick_x;

            double frontLeftPower = y + x + rx;
            double frontRightPower = y - x - rx;
            double backLeftPower = y - x + rx;
            double backRightPower = y + x - rx;

            double maxPower = 0.5;

            // Put powers in the range of -1 to 1 only if they aren't already (not
            // checking would cause us to always drive at full speed)
            if (Math.abs(frontLeftPower) > maxPower || Math.abs(backLeftPower) > maxPower ||
                    Math.abs(frontRightPower) > maxPower || Math.abs(backRightPower) > maxPower ) {
                // Find the largest power
                double max = 0;
                max = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
                max = Math.max(Math.abs(frontRightPower), max);
                max = Math.max(Math.abs(backRightPower), max);

                // Divide everything by max (it's positive so we don't need to worry
                // about signs)

                frontLeftPower /= max;
                frontRightPower /= max;
                backLeftPower /= max;
                backRightPower /= max;

                frontLeftPower *= maxPower;
                frontRightPower *= maxPower;
                backLeftPower *= maxPower;
                backRightPower *= maxPower;

            }

            frontLeftDrive.setPower(frontLeftPower);
            frontRightDrive.setPower(frontRightPower);
            backLeftDrive.setPower(backLeftPower);
            backRightDrive.setPower(backRightPower);

            if (gamepad1.y || gamepad1.a) {
                if(gamepad1.a) {
                    armMotor.setPower(0.3);
                } else if (gamepad1.y) {
                    armMotor.setPower(-0.3);
                }
            } else {
                armMotor.setPower(0.0);
            }

            // check to see if we need to move the servo.
            //if(gamepad1.b || gamepad1.x) {
                if(gamepad1.b) {
                    clawservo.setPosition(0.05);
                } else if (gamepad1.x) {
                    clawservo.setPosition(0.4);
                }


//        double gamepadPosition = gamepad1.right_stick_y;
//            double servoPosition = (gamepadPosition + 1.0) / 2.0;
//
//
//            clawservo.setPosition(servoPosition);
            sleep(100);

        // Show the elapsed game time and wheel power.
//        telemetry.addData("Status", "Run Time: " + runtime.toString());
//        telemetry.addData("Servo", "Gamepad (%.2f), Position (%.2f)", gamepadPosition, servoPosition);
//        telemetry.update();

//            if(gamepad1.y) {
//                // move to 0 degrees.
//                topservo.setPosition(0.5);
//            } else if (gamepad1.a) {//                // move to 90 degrees.
//                topservo.setPosition(-0.5);
//            }






        }
    }
}
