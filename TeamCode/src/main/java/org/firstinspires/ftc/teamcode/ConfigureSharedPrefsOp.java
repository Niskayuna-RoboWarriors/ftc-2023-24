package org.firstinspires.ftc.teamcode;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.Arrays;

@TeleOp(name="configure shared prefs", group="TeleOp OpMode")
public class ConfigureSharedPrefsOp extends OpMode {
    final String[][] VALUES = {
            {"STRAFE", "FORWARD_ONLY"}, {"BLUE", "RED"}, {"LEFT", "RIGHT", "MIDDLE"}, {"TOP", "MIDDLE", "BOTTOM"}, {"FAR", "CLOSE"}
    };
    String [] currentValues=new String[VALUES.length];
    final String[] PREF_NAMES = {"movement mode", "alliance color", "parking position", "auton mode", "starting_side"};
    int[] currentIndexes =new int[VALUES.length];
    int currSel=0, dispUntil;
    SharedPreferences sharedPrefs;
    boolean prevUP,prevDOWN,prevLEFT,prevRIGHT;
    @Override
    @SuppressWarnings("deprecation")
    public void init() {
        Arrays.fill(currentIndexes, -1);
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this.hardwareMap.appContext);

        telemetry.addData("Ready to set shared prefs\nPress play to start", null);
        telemetry.update();

        currentValues[0] = sharedPrefs.getString("movement_mode", "ERROR");
        currentValues[1] = sharedPrefs.getString("alliance_color", "ERROR");
        currentValues[2] = sharedPrefs.getString("parking_position", "ERROR");
        currentValues[3] = sharedPrefs.getString("auton_mode", "ERROR");
        currentValues[4] = sharedPrefs.getString("starting_side", "ERROR");
        for (int i=0;i<currentValues.length;i++) {
            for (int j = 0; j < VALUES[i].length; j++) {
                if (currentValues[i].equals(VALUES[i][j])) {
                    currentIndexes[i] = j;
                    break;
                }
            }
        }
    }

    @Override
    public void loop() {
        telemetry.addData("Controls game pad 1:", "D-pad Up/Down changes selected preference. Left/Right changes value. X saves current preferences");

        for (int i = 0; i < VALUES.length; i++) {
            telemetry.addData(PREF_NAMES[i], padding(16 - PREF_NAMES[i].length()) + ((i == currSel) ? "<" : " ") + currentValues[i] + ((i == currSel) ? ">" : ""));
        }

        if (System.nanoTime() / 1000000 < dispUntil){
            telemetry.addData("saved", null);
        }

        telemetry.update();

        if(gamepad1.x){
            savePrefs();
            telemetry.addData("saved", null);
            telemetry.update();
        }
        if(gamepad1.dpad_up && !prevUP){
            currSel--;
            if(currSel==-1)
                currSel=VALUES.length-1;
        }
        if(gamepad1.dpad_down && !prevDOWN){
            currSel++;
            if(currSel==VALUES.length)
                currSel=0;
        }
        if(gamepad1.dpad_left && !prevLEFT){
            currentIndexes[currSel]--;
            if(currentIndexes[currSel]<0){
                currentIndexes[currSel]=VALUES[currSel].length-1;
            }
            currentValues[currSel]=VALUES[currSel][currentIndexes[currSel]];
        }
        if(gamepad1.dpad_right && !prevRIGHT){
            currentIndexes[currSel]++;
            if(currentIndexes[currSel]>=VALUES[currSel].length){
                currentIndexes[currSel]=0;
            }
            currentValues[currSel]=VALUES[currSel][currentIndexes[currSel]];
        }

        prevUP=gamepad1.dpad_up;
        prevDOWN=gamepad1.dpad_down;
        prevLEFT=gamepad1.dpad_left;
        prevRIGHT=gamepad1.dpad_right;
    }

    void savePrefs() {
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("movement_mode", currentValues[0]);
        editor.putString("alliance_color", currentValues[1]);
        editor.putString("parking_position", currentValues[2]);
        editor.putString("auton_mode", currentValues[3]);
        editor.putString("starting_side", currentValues[4]);

        dispUntil = (int)(System.nanoTime() / 1000000) + 1000;
    }

    String padding(int a) {
        StringBuilder p = new StringBuilder();
        if (a > 0) {
            for (int i = 0; i < a; i++) {
                p.append(" ");
            }
        }
        return p.toString();
    }
}