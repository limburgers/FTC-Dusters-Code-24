package org.firstinspires.ftc.teamcode;

import android.os.Environment;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.tfod.TfodProcessor;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class ReplayOpMode extends ManualControlOpMode {
    int index = 0;
    int tagID = 4;

    TfodProcessor tfod;
    String ending = "left";
    double fx = 1425.99;
    double fy = 1425.99;
    double cx = 631.634;
    double cy = 306.469;
    double tagsize = 0.508;
    ArrayList<Double> LY1arr = new ArrayList<>();
    ArrayList<Double> LX1arr = new ArrayList<>();
    ArrayList<Double> RX1arr = new ArrayList<>();
    ArrayList<Boolean> A2arr = new ArrayList<>();
    ArrayList<Double> LY2arr = new ArrayList<>();
    ArrayList<Double> RY2arr = new ArrayList<>();
    ArrayList<Integer> LFarr = new ArrayList<>(),
            LBarr = new ArrayList<>(),
            RFarr = new ArrayList<>(),
            RBarr = new ArrayList<>();
    ArrayList<ArrayList> mainArr = new ArrayList<>();

    public void replayInit() {
        tfod = new TfodProcessor.Builder()
                .setModelFileName("/sdcard/meow.tflite") // possible change to Environment.getExternalStorageDirectory().getPath()
                .setModelLabels(new String[]{"Musty", "Dusty"})
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));

        builder.addProcessor(tfod);
        visionPortal = builder.build();

        while (!isStarted() && !isStopRequested()) {
            if (visionPortal.getFps() != 0) {
                List<Recognition> currentRecognitions = tfod.getRecognitions();

                if (currentRecognitions.size() == 0)
                    telemetry.addLine("Detected nothing!");

                for (Recognition recognition : currentRecognitions) {
                    double x = (recognition.getLeft() + recognition.getRight()) / 2;
                    double y = (recognition.getTop() + recognition.getBottom()) / 2;

                    if (x < 300)
                        ending = "center";
                    else
                        ending = "right";

                    telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                    telemetry.addData("Coords", "%.0f / %.0f", x, y);

                    telemetry.addData("position", ending);
                }
                if (currentRecognitions.size() == 0)
                    ending = "left";
            } else
                    telemetry.addLine("Camera is opening...");

            telemetry.update();

            sleep(20);
        }
        telemetry.addLine("Chose to go: " + ending);
        telemetry.update();
        visionPortal.close();

        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/controller_data.ser." + suffix() + "." + ending)));
            mainArr = (ArrayList) ois.readObject();
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        LY1arr = mainArr.get(0);
        LX1arr = mainArr.get(1);
        RX1arr = mainArr.get(2);
        A2arr = mainArr.get(3);
        LY2arr = mainArr.get(4);
        RY2arr = mainArr.get(5);
        LFarr = mainArr.get(6);
        LBarr = mainArr.get(7);
        RFarr = mainArr.get(8);
        RBarr = mainArr.get(9);
    }

    final double FACTOR_DIVISOR = 200; // This value needs to be tweaked
    public void runner() {
        if (index == mainArr.get(0).size())
            requestOpModeStop();

        double LFfactor = (LFarr.get(index) - leftFrontMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double LBfactor = (LBarr.get(index) - leftBackMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double RFfactor = (RFarr.get(index) - rightFrontMotor.getCurrentPosition()) / FACTOR_DIVISOR;
        double RBfactor = (RBarr.get(index) - rightBackMotor.getCurrentPosition()) / FACTOR_DIVISOR;

        programRunner(LX1arr.get(index), LY1arr.get(index), RX1arr.get(index), A2arr.get(index), LY2arr.get(index), RY2arr.get(index), LFfactor, LBfactor, RFfactor, RBfactor);

        index++;
    }

    public abstract String suffix();
}
