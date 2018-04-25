package bujny.rapidlux;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by Mateusz Bujnowicz on 3/14/2018.
 */

public class Score implements Screen {
    final Start game;
    int score;
    int gameIndex;
    int lastHighScore;

    SpriteBatch batch;

    Texture new_high;
    Texture your_score;

    float width;
    float height;
    float logoWidth;
    float logoHeight;
    float textWidth;
    float textHeight;
    float space;

    BitmapFont font;

    boolean firstRun=true;
    boolean hasTimePassed=false;

    Music ending;



    public Score(Start game,int gameIndex,int score) {
        this.game = game;
        this.score = score;
        this.gameIndex = gameIndex;
    }

    @Override
    public void show() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        logoWidth = width*4/5;
        logoHeight = height*0.3f;
        new_high = new Texture("new_high.png");
        your_score = new Texture("score.png");
        ending = Gdx.audio.newMusic(Gdx.files.internal("lost.mp3"));
        //end = Gdx.audio.newSound(Gdx.files.internal("end.mp3"));
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(width/70);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        GlyphLayout text= new GlyphLayout(font,Integer.toString(score));
        textWidth = text.width;
        textHeight=text.height;
        space=height*0.035f;
        lastHighScore = Start.database.getHighScore(gameIndex);
        firstRun=true;
        //end.play();
        ending.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if(score>lastHighScore) {
            batch.draw(new_high, width / 2 - logoWidth / 2, height * 0.95f - logoHeight, logoWidth, logoHeight);
            Start.database.updateHighScore(gameIndex, score);
        }
        else{
            batch.draw(your_score, width / 2 - logoWidth / 2, height * 0.95f - logoHeight, logoWidth, logoHeight);
        }
        font.draw(batch,Integer.toString(score),width/2-textWidth/2,height*0.95f-logoHeight-space-textHeight);
        if(firstRun) {
            firstRun=false;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hasTimePassed=true;
                }
            },1f);}
        if(Gdx.input.justTouched()&&hasTimePassed) {
            if(gameIndex==0) game.setScreen(new TwoBoxes(game));
            else if(gameIndex==1) game.setScreen(new Defence(game));
            else if(gameIndex==2) game.setScreen(new Memory(game));
            dispose();
        }
        batch.end();

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new MainMenu(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        new_high.dispose();
        your_score.dispose();
        ending.dispose();
        font.dispose();
    }
}
