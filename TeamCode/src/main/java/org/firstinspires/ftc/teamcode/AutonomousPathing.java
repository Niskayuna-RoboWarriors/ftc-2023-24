package org.firstinspires.ftc.teamcode;

import java.util.ArrayList;

public class AutonomousPathing {

    public AutonomousPathing(RobotManager robotManager, CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide,
                             CenterStageAuton.MovementMode movementMode) {
//        robotManager.robot.telemetry.addData("auton path", path.size());
    }

    /**
     * @param startingSide    where the robot starts in auton mode on the field
     * @param parkingPosition the parking position of the robot during auton mode.
     */
    public void configurePath(CenterStageAuton.StartingSide startingSide, CenterStageAuton.ParkingPosition parkingPosition) {
        transformPath(startingSide);
        //Set parking location
        setParkingLocation(startingSide, parkingPosition);
    }

    /**
     * @param startingSide the starting side of the robot, and what color it is on
     */
    private void transformPath(CenterStageAuton.StartingSide startingSide) {
        //TODO IMPLEMENT
    }

    /**
     * @param startingSide the starting side of the robot, and what color it is on
     * @param parkingPosition the parking position of the robot, and where the robot is parking
     */
    private void setParkingLocation(CenterStageAuton.StartingSide startingSide, CenterStageAuton.ParkingPosition parkingPosition) {
        //TODO IMPLEMENT
    }

    /** Hardcoded paths through the playing field during the Autonomous period.*/
    public static class AutonomousPaths{
        public static final double TILE_SIZE = 24;
        // These two constants aren't used but store the offsets from the center of the tile to the real starting position
        public static final double Y_OFFSET = -0.25;
        public static final double X_OFFSET = -0.15;

        //Junctions
        //Leaving this up to the Autonomous team. Not gonna do anything else beyond this point, since the code from 2022-2023 was all about Auton movement.


    }
}
