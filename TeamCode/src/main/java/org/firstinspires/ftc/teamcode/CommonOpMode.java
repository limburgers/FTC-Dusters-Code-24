package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public abstract class CommonOpMode extends LinearOpMode {
    protected DcMotor leftFrontMotor, leftBackMotor, rightFrontMotor, rightBackMotor;

    protected DcMotor LLiftMotor, RLiftMotor, intakeMotor;
    protected Servo droneLauncherServo, swingServo, releaseServo;
    protected VisionPortal visionPortal;

    @Override
    public void runOpMode() throws InterruptedException {

        replayInit();

        waitForStart();

        leftFrontMotor = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "rightFrontMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "rightBackMotor");
        LLiftMotor = hardwareMap.get(DcMotor.class, "LLiftMotor");
        RLiftMotor = hardwareMap.get(DcMotor.class, "RLiftMotor");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        droneLauncherServo = hardwareMap.get(Servo.class, "droneLauncherServo");
        swingServo = hardwareMap.get(Servo.class, "swingServo");
        releaseServo = hardwareMap.get(Servo.class, "releaseServo");
        
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        RLiftMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setDirection(DcMotor.Direction.REVERSE);
        swingServo.setDirection(Servo.Direction.REVERSE);
        releaseServo.setDirection(Servo.Direction.REVERSE);

        LLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RLiftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightBackMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        LLiftMotor.setTargetPosition(0);
        RLiftMotor.setTargetPosition(0);

        LLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        RLiftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        LLiftMotor.setPower(1);
        RLiftMotor.setPower(1);

        droneLauncherServo.setPosition(0);
        swingServo.setPosition(0);
        releaseServo.setPosition(0);


        gamepad1.reset();
        gamepad1.rumble(500);

        while(opModeIsActive()) {
            double start = System.currentTimeMillis();
            telemetry.addData("LF encoder", leftFrontMotor.getCurrentPosition());
            telemetry.addData("LB encoder", leftBackMotor.getCurrentPosition());
            telemetry.addData("RF encoder", rightFrontMotor.getCurrentPosition());
            telemetry.addData("RB encoder", rightBackMotor.getCurrentPosition());

            telemetry.update();

            runner();
            while (System.currentTimeMillis() - start < 10) {}
        }
    }

    protected abstract void runner();
    protected abstract void replayInit();
}
