var game;

function loop() {
    if (game.robotRotations() > 10) {
       game.stop();
    }
}

function onEnd() {
  game.fail();
}