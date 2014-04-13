led.on();
while(true) {

  var distance = sonar.ping();
  if (distance == 0) {
    var leftDist = 0;
    var rightDist = 0;

    robot.rotateLeft();

    leftDist = sonar.ping();

    robot.rotateRight();
    robot.rotateRight();

    rightDist = sonar.ping();

    robot.rotateLeft();

    if (leftDist > rightDist) {
      robot.rotateLeft();
    } else {
      robot.rotateRight();
    }

  } else {
    for(var i = 0; i < distance; i++) {
      robot.moveForward();
    }
  }
}