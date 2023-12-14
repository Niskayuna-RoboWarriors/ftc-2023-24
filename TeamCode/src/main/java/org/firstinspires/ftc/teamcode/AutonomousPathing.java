package org.firstinspires.ftc.teamcode;

import android.service.quicksettings.Tile;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonomousPathing {
    public ArrayList<Position> path; //List of positions that the robot will go into WHEN IT IS IN AUTOMOTOUS MODE.
    public static final double TILE_SIZE = 24;
    // These two constants aren't used but store the offsets from the center of the tile to the real starting position
    public static final double Y_OFFSET = -0.25;
    public static final double X_OFFSET = -0.15;

    public AutonomousPathing(RobotManager robotManager, CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide,
                             CenterStageAuton.MovementMode movementMode, CenterStageAuton.PixelPosition pixelPosition,
                             CenterStageAuton.ParkingPosition parkingPosition, CenterStageAuton.AutonMode autonMode) {
        path = new ArrayList<>(Arrays.asList(

        ));
//        robotManager.robot.telemetry.addData("auton path", path.size());
    }

    /**
     * @param allianceColor    where the robot starts in auton mode on the field
     * @param parkingPosition the parking position of the robot during auton mode.
     */
    public void configurePath(CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide,
                              CenterStageAuton.PixelPosition pixelPosition, CenterStageAuton.AutonMode autonMode,
                              CenterStageAuton.ParkingPosition parkingPosition) {
        if (startingSide == CenterStageAuton.StartingSide.LEFT) {

        }
        else {

        }
        transformPath(allianceColor, startingSide);
        //Set parking location
        setParkingLocation(allianceColor, parkingPosition);
    }

    /**
     * Transforms the path from the default of BLUE side alliance color if necessary
     * @param allianceColor the starting side of the robot, and what color it is on
     */
    private void transformPath(CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide) {
        for (int i = 0; i < path.size(); i++) {
            Position pos = path.get(i);
            Position copy = new Position(pos.getX(),pos.getY(), pos.getName(),pos.getAction(),pos.getStrafePower(),pos.getRotatePower(),pos.getRotation());
//            if (allianceColor == CenterStageAuton.AllianceColor.RED) {
//                copy.setY(-copy.getY());
                copy.setRotation(-copy.getRotation());
//            }
//            if (allianceColor == CenterStageAuton.AllianceColor.RED) {
//                copy.setX(-copy.getX());
//                copy.setRotation(-copy.getRotation());
//            }
            if (startingSide == CenterStageAuton.StartingSide.RIGHT) {
                // Adjust left-wards on X axis to account for perspective that's further to the right
                copy.setX(-2*TILE_SIZE + copy.getY());
            }
            path.set(i, copy);
        }
    }


    /**
     * @param allianceColor the starting side of the robot, and what color it is on
     * @param parkingPosition the parking position of the robot, and where the robot is parking
     */
    private void setParkingLocation(CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.ParkingPosition parkingPosition) {
        // TO DO: Add paths for three different paths
        //Parks in a signal parking spot to have a chance for 20 points
        if (parkingPosition == CenterStageAuton.ParkingPosition.LEFT) {
            path.add(AutonomousPaths.leftBottomParking); //No transformation occurs on this position so it will be the same
        } else if (parkingPosition == CenterStageAuton.ParkingPosition.CENTER) {
            path.add(AutonomousPaths.leftCenterParking);
        }
        else {
            path.add(AutonomousPaths.leftTopParking);
        }
    }
    public void runAutonPath( RobotManager robotManager, Robot robot, CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide,
                             CenterStageAuton.PixelPosition pixelPosition, CenterStageAuton.AutonMode autonMode,
                             CenterStageAuton.ParkingPosition parkingPosition) {
        //TODO IMPLEMENT
//        if (startingSide == CenterStageAuton.StartingSide.LEFT) {
//            // Dropping the pixel
//            NavigationAuton.travelToPOI(pixelLeftJunction, robot);
//            if (pixelPosition == CenterStageAuton.PixelPosition == CenterStageAuton.PixelPosition.LEFT) {
//                rotate(Math.PI, 1, robot);
//                robotManager.mechanismDriving.dropPixel(robot);
//                rotate(Math.PI/2, 1, robot);
//            }
//            else if (pixelPosition == CenterStageAuton.PixelPosition == CenterStageAuton.PixelPosition.CENTER) {
//                MechanismDriving.dropPixel(robot);
//            }
//            else {
//                rotate(0, 1, robot);
//                MechanismDriving.dropPixel(robot);
//                rotate(Math.PI/2, 1, robot);
//            }
//            // Moving to backdrop
//            if (autonMode == CenterStageAuton.StartingSide.TOP) {
//                NavigationAuton.traveltoPOI();
//                rotate(Math.PI, 1, robot);
//                NavigationAuton.traveltoPOI();
//                NavigationAuton.traveltoPOI();
//                MechanismDriving.dropPixel(robot);
//            }
//            else if (autonMode == CenterStageAuton.StartingSide.MIDDLE) {
//                NavigationAuton.traveltoPOI();
//                MechanismDriving.dropPixel(robot);
//            }
//            else {
//                NavigationAuton.traveltoPOI();
//                rotate(Math.PI, 1, robot);
//                NavigationAuton.traveltoPOI();
//                NavigationAuton.traveltoPOI();
//                MechanismDriving.dropPixel(robot);
//            }
//            // Parking
//            if (parkingPosition == CenterStageAuton.ParkingPosition.LEFT) {
//                NavigationAuton.traveltoPOI(leftBottomParking);
//            }
//            if (parkingPosition == CenterStageAuton.ParkingPosition.CENTER) {
//                NavigationAuton.traveltoPOI(leftCenterParking);
//            }
//            else {
//                NavigationAuton.traveltoPOI(leftTopParking);
//            }
//        }
//        else {
//            // Dropping the pixel
//            NavigationAuton.travelToPOI(pixelLeftJunction, robot);
//            if (pixelPosition == CenterStageAuton.PixelPosition == CenterStageAuton.PixelPosition.LEFT) {
//                rotate(Math.PI, 1, robot);
//                MechanismDriving.dropPixel(robot);
//                rotate(Math.PI/2, 1, robot);
//            }
//            else if (pixelPosition == CenterStageAuton.PixelPosition == CenterStageAuton.PixelPosition.CENTER) {
//                MechanismDriving.dropPixel(robot);
//            }
//            else {
//                rotate(0, 1, robot);
//                MechanismDriving.dropPixel(robot);
//                rotate(Math.PI/2, 1, robot);
//            }
//            // Moving to backdrop
//            if (autonMode == CenterStageAuton.StartingSide.TOP) {
//                NavigationAuton.traveltoPOI();
//                rotate(Math.PI, 1, robot);
//                NavigationAuton.traveltoPOI();
//                NavigationAuton.traveltoPOI();
//                MechanismDriving.dropPixel(robot);
//            }
//            else if (autonMode == CenterStageAuton.StartingSide.MIDDLE) {
//                NavigationAuton.traveltoPOI();
//                MechanismDriving.dropPixel(robot);
//            }
//            else {
//                NavigationAuton.traveltoPOI();
//                rotate(Math.PI, 1, robot);
//                NavigationAuton.traveltoPOI();
//                NavigationAuton.traveltoPOI();
//                MechanismDriving.dropPixel(robot);
//            }
//            // Parking
//            if (parkingPosition == CenterStageAuton.ParkingPosition.LEFT) {
//                NavigationAuton.traveltoPOI(leftBottomParking);
//            }
//            else if (parkingPosition == CenterStageAuton.ParkingPosition.CENTER) {
//                NavigationAuton.traveltoPOI(leftCenterParking);
//            }
//            else {
//                NavigationAuton.traveltoPOI(leftTopParking);
//            }
//        }
    }

    /** Hardcoded paths through the playing field during the Autonomous period.*/
    public static class AutonomousPaths{
        // From the perspective of a Left Starting Side and a Blue Alliance Color

        //Junctions
        //Leaving this up to the Autonomous team. Not gonna do anything else beyond this point, since the code from 2022-2023 was all about Auton movement.
        public static Position pixelLeftJunction = new Position(0*TILE_SIZE, 1*TILE_SIZE, 0, "pixelLeftJunction");
        public static Position pixelRightJunction = new Position(2*TILE_SIZE, 1*TILE_SIZE, 0, "pixelRightJunction");

        // TODO! Change these so that they reflect the full possible 6*3 intermediate locations
        //Intermediate Locations. Since these values could be transformed, inner refers to the middle of the entire field, center
        // to the center of the left or right, and outer refers to the very edges of the field next to either side wall
        // Back refers to the tiles closest to the wall, front refers to the tiles furthest away from the wall/drivers
        public static Position intermediateInnerBack = new Position(0.85*TILE_SIZE, -0.25*TILE_SIZE, 0, "intermediateInnerBack");
        public static Position intermediateCenterBack = new Position(-0.15*TILE_SIZE, -0.25*TILE_SIZE, 0, "intermediateCenterBack");
        public static Position intermediateOuterBack = new Position(-1.15*TILE_SIZE, -0.25*TILE_SIZE, 0, "intermediateOuterBack");
        public static Position intermediateInnerMiddle = new Position(0.85*TILE_SIZE, -1.25*TILE_SIZE, 0, "intermediateInnerMiddle");
        public static Position intermediateCenterMiddle = new Position(-0.15*TILE_SIZE, -1.25*TILE_SIZE, 0, "intermediateCenterMiddle");
        public static Position intermediateOuterMiddle = new Position(-1.15*TILE_SIZE, -1.25*TILE_SIZE, 0, "intermediateOuterMiddle");
        public static Position intermediateInnerFront = new Position(0.85*TILE_SIZE, -2.25*TILE_SIZE, -Math.PI/2, "intermediateInnerFront");
        public static Position intermediateCenterFront = new Position(-0.15*TILE_SIZE, -2.25*TILE_SIZE, -Math.PI/2, "intermediateCenterFront");
        public static Position intermediateOuterFront = new Position(-1.15*TILE_SIZE,-2.25*TILE_SIZE, 0, "intermediateOuterFront");

        // Parking
        public static Position leftBottomParking = new Position(-1.5*TILE_SIZE, 0, 0, "leftBottomParking");
        public static Position leftCenterParking = new Position(-1.5*TILE_SIZE, 1*TILE_SIZE, 0, "leftCenterParking");
        public static Position leftTopParking = new Position(-1.5*TILE_SIZE, 2* TILE_SIZE, 0, "leftTopParking");
        public static Position rightBottomParking = new Position(1.5*TILE_SIZE, 0, 0, "rightBottomParking");
        public static Position rightCenterParking = new Position(1.5*TILE_SIZE, 1*TILE_SIZE, 0, "rightCenterParking");
        public static Position rightTopParking = new Position(1.5*TILE_SIZE, 2* TILE_SIZE, 0, "rightTopParking");
    }
}
