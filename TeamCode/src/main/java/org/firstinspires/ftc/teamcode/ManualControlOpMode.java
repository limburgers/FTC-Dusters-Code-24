package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

class CurvedGamepad extends Gamepad {
    public double lx;
    public double ly;
    public double rx;

    final double CURVE = 2.5;
    final double TURN_MAX = 0.75;

    public CurvedGamepad(Gamepad gamepad) {
        this.lx = gamepad.left_stick_x < 0 ? -(Math.pow(-gamepad.left_stick_x, CURVE)) : Math.pow(gamepad.left_stick_x, CURVE);
        this.ly = gamepad.left_stick_y < 0 ? -(Math.pow(-gamepad.left_stick_y, CURVE)) : Math.pow(gamepad.left_stick_y, CURVE);
        this.rx = (gamepad.right_stick_x < 0 ? -(Math.pow(-gamepad.right_stick_x, CURVE)) : Math.pow(gamepad.right_stick_x, CURVE)) * TURN_MAX;
    }
}

@TeleOp(name = "Driving OpMode")
public class ManualControlOpMode extends CommonOpMode {
    
    int lPos = 0;
    @Override
    public void runner() {
        CurvedGamepad cgp = new CurvedGamepad(gamepad1);
        CurvedGamepad cgp2 = new CurvedGamepad(gamepad2);

        double denominator = Math.max(Math.abs(cgp.ly) + Math.abs(cgp.lx) + Math.abs(cgp.rx), 1);

        leftFrontMotor.setPower((cgp.ly - cgp.lx - cgp.rx)/denominator);
        leftBackMotor.setPower((cgp.ly + cgp.lx - cgp.rx)/denominator);
        rightFrontMotor.setPower((cgp.ly + cgp.lx + cgp.rx)/denominator);
        rightBackMotor.setPower((cgp.ly - cgp.lx + cgp.rx)/denominator);

        lPos += (int)(gamepad2.left_stick_y * 30);

        if (lPos < -6000)
            lPos = -6000;
        else if (lPos > 0)
            lPos = 0;

        telemetry.addData("lPos", lPos);

        if (gamepad2.left_bumper && gamepad2.right_bumper)
            droneLauncherServo.setPosition(1);

        intakeMotor.setPower(1 * gamepad2.right_stick_y);

        LLiftMotor.setTargetPosition(lPos);
        RLiftMotor.setTargetPosition(lPos);


        if (lPos <= -3000)
            swingServo.setPosition(0.18);
        else
            swingServo.setPosition(0.01);

        if (gamepad2.a && lPos <= -3000)
            releaseServo.setPosition(0.3);
        else
            releaseServo.setPosition(0);
    }
    public void programRunner(double lx1, double ly1, double rx1, boolean a2, double ly2, double ry2, double LFfactor, double LBfactor, double RFfactor, double RBfactor) {
        // Don't judge me riley

        double denominator = Math.max(Math.abs(ly1) + Math.abs(lx1) + Math.abs(rx1), 1);

        leftFrontMotor.setPower((ly1 - lx1 - rx1)/denominator + LFfactor); // Good evening Benefactor, my name is Duke Henry
        leftBackMotor.setPower((ly1 + lx1 - rx1)/denominator + LBfactor);
        rightFrontMotor.setPower((ly1 + lx1 + rx1)/denominator + RFfactor);
        rightBackMotor.setPower((ly1 - lx1 + rx1)/denominator + RBfactor);

        lPos += (int)(ly2 * 30);

        if (lPos < -6000)
            lPos = -6000;
        else if (lPos > 0) {
            lPos = 0;
        }

        LLiftMotor.setTargetPosition(lPos);
        RLiftMotor.setTargetPosition(lPos);


        intakeMotor.setPower(1.0 * ry2);

        if (lPos <= -2000)
            swingServo.setPosition(0.18);
        else
            swingServo.setPosition(0.01);

        if (a2 && lPos <= -2000)
            releaseServo.setPosition(0.3);
        else
            releaseServo.setPosition(0);
    }
    public void replayInit() {}
}
