package de.macbury.botlogic.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 31.03.14.
 */
public class AudioManager implements Disposable {
  public Sound sonar;
  public Sound wallImpact;
  public Sound codeError;
  public Sound slide;
  public Music gameMusic;
  public Sound click;
  public Sound move;
  public Music mainMenuMusic;
  public Sound rotation;

  public AudioManager() {
    this.mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/music/di_gravity.mp3"));
    mainMenuMusic.setLooping(true);
    mainMenuMusic.setVolume(0.6f);

    this.gameMusic     = Gdx.audio.newMusic(Gdx.files.internal("audio/music/cloud_9.mp3"));
    gameMusic.setLooping(true);

    this.rotation           = Gdx.audio.newSound(Gdx.files.internal("audio/effects/rotate.mp3"));
    this.move               = Gdx.audio.newSound(Gdx.files.internal("audio/effects/move.mp3"));
    this.click              = Gdx.audio.newSound(Gdx.files.internal("audio/effects/click.ogg"));
    this.slide              = Gdx.audio.newSound(Gdx.files.internal("audio/effects/slide.ogg"));
    this.sonar              = Gdx.audio.newSound(Gdx.files.internal("audio/effects/sonar.ogg"));
    this.codeError          = Gdx.audio.newSound(Gdx.files.internal("audio/effects/code_error.ogg"));
    this.wallImpact         = Gdx.audio.newSound(Gdx.files.internal("audio/effects/wall_impact.mp3"));
    //rotation.setLooping(1, true);
  }

  @Override
  public void dispose() {
    mainMenuMusic.dispose();
    rotation.dispose();
  }
}
