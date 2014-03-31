package de.macbury.botlogic.core.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by macbury on 31.03.14.
 */
public class AudioManager implements Disposable {
  public Music music;
  public Sound rotation;

  public AudioManager() {
    this.music              = Gdx.audio.newMusic(Gdx.files.internal("audio/code_testing.mp3"));
    music.setLooping(true);
    music.setVolume(0.1f);
    this.rotation           = Gdx.audio.newSound(Gdx.files.internal("audio/rotate_effect.mp3"));
    //rotation.setLooping(1, true);
  }

  @Override
  public void dispose() {
    music.dispose();
    rotation.dispose();
  }
}
