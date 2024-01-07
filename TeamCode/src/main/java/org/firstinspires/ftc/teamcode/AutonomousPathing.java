package org.firstinspires.ftc.teamcode;



import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Arrays;

public class AutonomousPathing {
    public static final double TILE_SIZE = 24; //24 inches is tile size
    // These two constants aren't used but store the offsets from the center of the tile to the real starting position
    public static final double Y_OFFSET = -0.25;
    public static final double X_OFFSET = -0.15;
    public static final double HALF_ROBOT_TILE_LENGTH = 0; //TODO NEEDS TO BE RECONFIGURED TO THE CORRECT VALUE
    private RobotManager robotManager;

    public AutonomousPathing(RobotManager robotManager, CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide,
                             CenterStageAuton.PixelPosition pixelPosition,
                             CenterStageAuton.ParkingPosition parkingPosition) {
        this.robotManager = robotManager;
        robotManager.navigationAuton.path = new ArrayList<>();

        ArrayList<Position> path = robotManager.navigationAuton.path;
        //generate path here
        generatePath(allianceColor,startingSide,pixelPosition,parkingPosition);

    }

    public void runAutonPath() {
        Robot robot = robotManager.robot;
        NavigationAuton nav = robotManager.navigationAuton;
        while(nav.pathIndex < nav.path.size()){
            Position p = nav.travelToNextPOI(robot);
            if(p.getAction() != NavigationAuton.Action.NONE){ //if the action on the current path index is not null
                handleAction(p); //now lets handle this below...
            }
        }
        robot.telemetry.addData(":","FINISHED PATH");

    }

    private void handleAction(Position position) {
        NavigationAuton.Action action = position.getAction();
        //TODO make the robot place the pixels
        switch(action){
            case DROP_PURPLE:
                //do the thing for purple drop
                //no idea how this happens, but it happens here
                break;
            case DROP_YELLOW:
                //do the thing for yellow drop
                //no idea how this happens, but it happens here
                break;

            default:
                throw new RuntimeException("attempted to execute an action that has not been implemented: "+action);
        }
    }

    /**
     * @param allianceColor The alliance color on what team were on (in this case, it is blue or red)
     * @param startingSide The starting side of the robot (is it further from the backdrop, or closer to the backdrop)
     * @param pixelPosition The randomized position on where to put the pixel (right, center, or left)
     * @param parkingPosition Where the robot is parking from the backboard (parking to the left of the backboard, or the right of the backboard)
     */
    void generatePath(CenterStageAuton.AllianceColor allianceColor, CenterStageAuton.StartingSide startingSide,
                       CenterStageAuton.PixelPosition pixelPosition,
                      CenterStageAuton.ParkingPosition parkingPosition){

        robotManager.navigationAuton.path = new ArrayList<>();
        ArrayList<Position> path = robotManager.navigationAuton.path;
        double desiredAngle =0;
        Position cumulativePosition = new Position();


        //move to center spike mark

        cumulativePosition = Position.add(new Position(0,1.69*TILE_SIZE-HALF_ROBOT_TILE_LENGTH,0,"move to center Spike mark"),cumulativePosition);
        path.add(cumulativePosition);


        switch(pixelPosition){
            case LEFT :
                desiredAngle = Math.PI/4;
                cumulativePosition = Position.add(new Position(-0.259*TILE_SIZE,0,0,"move to pixel spike mark"),cumulativePosition);
                cumulativePosition.setRotation(desiredAngle);
                path.add(cumulativePosition);//move to pixel spike mark
                break;
            case RIGHT:
                desiredAngle = Math.PI/-4;
                cumulativePosition = Position.add(new Position(0.259*TILE_SIZE,0,0,"move to pixel spike mark"),cumulativePosition);
                cumulativePosition.setRotation(desiredAngle);
                path.add(cumulativePosition);//move to pixel spike mark
                break;
            case CENTER:
                //face pixel spike mark//for center do nothing
                desiredAngle = 0;
                cumulativePosition = Position.add(new Position(0,0.259*TILE_SIZE,0,"move to pixel spike mark"),cumulativePosition);
                cumulativePosition.setRotation(desiredAngle);
                path.add(cumulativePosition);//move to pixel spike mark

        }

        //place purple pixel
        cumulativePosition = Position.add(new Position(NavigationAuton.Action.DROP_PURPLE, "Dropping Purple Pixel on Spike Mark"),cumulativePosition);
        path.add(cumulativePosition);

        //return to center spike mark
        switch(pixelPosition){
            case LEFT :
                cumulativePosition = Position.add(new Position(0.259*TILE_SIZE,0,0,"move to pixel spike mark"),cumulativePosition);
                path.add(cumulativePosition);//move to pixel spike mark
                break;
            case RIGHT:
                cumulativePosition = Position.add(new Position(-0.259*TILE_SIZE,0,0,"move to pixel spike mark"),cumulativePosition);
                path.add(cumulativePosition);//move to pixel spike mark
                break;
            case CENTER:
                //face pixel spike mark//for center do nothing
                cumulativePosition = Position.add(new Position(0,-0.259*TILE_SIZE,0,"move to pixel spike mark"),cumulativePosition);
                path.add(cumulativePosition);//move to pixel spike mark
        }
        //movement distances for easy adjustments
        final double AVOID_PIXEL_DISTANCE = 0.25 * TILE_SIZE; //Avoid the pizel distance within interfering with the other team
        final double ADJUSTMENT_TO_BOARD_DISTANCE=1*TILE_SIZE; //the distacne from the place the robot un adjusts to the board
        final double PLACEMENT_TO_PARK_DISTANCE = 1*TILE_SIZE; //the parking distance
        final double NEAR_POST_ADJUST_DISTANCE =1 * TILE_SIZE;
        final double FAR_POST_ADJUST_DISTANCE = 3 * TILE_SIZE;

        if(allianceColor == CenterStageAuton.AllianceColor.BLUE) { //If alliance color is blue (FROM THE FAR SIDE)
            //move to pixel board avoiding placed pixel if necessary
            if (startingSide == CenterStageAuton.StartingSide.FURTHER) { //starting from further side
                cumulativePosition = Position.add(new Position(0, -AVOID_PIXEL_DISTANCE, 0, "AVOID LEFT PIXEL"),cumulativePosition);
                path.add(cumulativePosition);
                cumulativePosition = Position.add(new Position(-FAR_POST_ADJUST_DISTANCE * TILE_SIZE, 0, 0, "move toward board"),cumulativePosition);
                path.add(cumulativePosition);
            }else{ //assume everything is as close as possible...
                cumulativePosition = Position.add(new Position(0, -AVOID_PIXEL_DISTANCE, 0, "AVOID LEFT PIXEL"),cumulativePosition);
                path.add(cumulativePosition);
                cumulativePosition = Position.add(new Position(-NEAR_POST_ADJUST_DISTANCE * TILE_SIZE, 0, 0, "move toward board"),cumulativePosition);
                path.add(cumulativePosition);
            }

            cumulativePosition = Position.add(new Position(0,AVOID_PIXEL_DISTANCE,0,"un avoid pixel"),cumulativePosition);
            desiredAngle = Math.PI/4; // rotate to face board
            cumulativePosition.setRotation(desiredAngle);
            path.add(cumulativePosition);
            cumulativePosition = Position.add(new Position(-ADJUSTMENT_TO_BOARD_DISTANCE,0,0,"move to board"),cumulativePosition);
            path.add(cumulativePosition);

            //place pixel
            cumulativePosition = Position.add(new Position(NavigationAuton.Action.DROP_YELLOW, "Dropping Yellow Pixel on Backdrop"),cumulativePosition);
            path.add(cumulativePosition);

            //move to designated parking position (BLUE)
            cumulativePosition = Position.add(new Position(0.1*TILE_SIZE,0,0,"back up"),cumulativePosition);
            path.add(cumulativePosition);
            if(parkingPosition == CenterStageAuton.ParkingPosition.LEFT){ //parking left
                cumulativePosition = Position.add(new Position(0,-PLACEMENT_TO_PARK_DISTANCE,0,"PARK THE ROBOT. WHY DOES IT MOVE WHERE IT WANTS TO GO?!?!?"),cumulativePosition);
                path.add(cumulativePosition);
            }else{ //assume everything is right, park it to the right
                cumulativePosition = Position.add(new Position(0,PLACEMENT_TO_PARK_DISTANCE,0,"PARK THE ROBOT OMG"),cumulativePosition);
                path.add(cumulativePosition);
            }

        } else { //If alliance color is red (literally just assume it is red, and nothing else)
            //move to pixel board avoiding placed pixel if necessary
            if (startingSide == CenterStageAuton.StartingSide.FURTHER) { //starting from further side
                cumulativePosition = Position.add(new Position(0, -AVOID_PIXEL_DISTANCE, 0, "AVOID LEFT PIXEL"),cumulativePosition);
                path.add(cumulativePosition);
                cumulativePosition = Position.add(new Position(FAR_POST_ADJUST_DISTANCE * TILE_SIZE, 0, 0, "move toward board"),cumulativePosition);
                path.add(cumulativePosition);
            }else{
                cumulativePosition = Position.add(new Position(0, -AVOID_PIXEL_DISTANCE, 0, "AVOID LEFT PIXEL"),cumulativePosition);
                path.add(cumulativePosition);
                cumulativePosition = Position.add(new Position(NEAR_POST_ADJUST_DISTANCE * TILE_SIZE, 0, 0, "move toward board"),cumulativePosition);
                path.add(cumulativePosition);
            }


            cumulativePosition = Position.add(new Position(0,AVOID_PIXEL_DISTANCE,0,"un avoid pixel"),cumulativePosition);
            desiredAngle = -Math.PI/4;//rotate to face board
            cumulativePosition.setRotation(desiredAngle);
            path.add(cumulativePosition);
            cumulativePosition = Position.add(new Position(ADJUSTMENT_TO_BOARD_DISTANCE,0,0,"move to board"),cumulativePosition);
            path.add(cumulativePosition);

            //place pixel
            cumulativePosition = Position.add(new Position(NavigationAuton.Action.DROP_YELLOW, "Dropping Yellow Pixel on Backdrop"),cumulativePosition);
            path.add(cumulativePosition);

            //move to designated parking position
            cumulativePosition = Position.add(new Position(-0.1*TILE_SIZE,0,0,"back up"),cumulativePosition);
            path.add(cumulativePosition);
            if(parkingPosition == CenterStageAuton.ParkingPosition.RIGHT){ //parking right
                cumulativePosition = Position.add(new Position(0,-PLACEMENT_TO_PARK_DISTANCE,0,"PARK THE ROBOT. WHY DOES IT MOVE WHERE IT WANTS TO GO?!?!?"),cumulativePosition);
                path.add(cumulativePosition);
            }else{ //assume everything is left, park it to the right
                cumulativePosition = Position.add(new Position(0,PLACEMENT_TO_PARK_DISTANCE,0,"PARK THE ROBOT OMG"),cumulativePosition);
                path.add(cumulativePosition);
            }
        }

    }
}
