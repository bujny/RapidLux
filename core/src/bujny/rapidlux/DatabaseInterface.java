package bujny.rapidlux;

/**
 * Created by Mateusz Bujnowicz on 3/14/2018.
 */

public interface DatabaseInterface {
     int getHighScore(int gameIndex);
     void updateHighScore(int gameIndex, int highScore);
}
