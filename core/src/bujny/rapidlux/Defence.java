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

import java.util.Random;

/**
 * Created by Mateusz Bujnowicz on 3/14/2018.
 */
//13.75 2.5% >30K 35*9 1500  - 20 13
public class Defence implements Screen {
    final Start game;

    SpriteBatch batch;

    int gameMode = 1;
    float timeInterval = 0.4f;
    float timeRate = 0.001f;
    int score=0;

    boolean isMarked[];

    float width;
    float height;
    float boxWidth;
    float boxHeight;

    Texture[] boxes;
    Texture whiteBox;
    Texture greenBox;
    Texture greenBox0;
    Texture redBox;
    Texture redBox0;

    boolean firstRun;

    Music backgroundMusic;
    Sound good;

    public Defence(Start game) {
        this.game = game;
    }

    void clearBoxes(){
        for(int i=0;i<12;i++) {
            boxes[i] = whiteBox;
            isMarked[i]=false;
        }

    }

    void  drawBoxes(){
        for (int i = 0; i < 12; i++) batch.draw(boxes[i], i % 3 * boxWidth, i / 3 * boxHeight, boxWidth, boxHeight);
    }

    float timeInterval(){
        if(score<319) return timeInterval-score*timeRate;
        return timeInterval-timeRate*318;
    }

    boolean isFull(){
        for (int i=0;i<12;i++) if(!isMarked[i]) return false;
        return true;
    }

    void markBox(){
        Random random = new Random();
        int index;
        do {
            index = random.nextInt(12);
        }while (isMarked[index]);
        isMarked[index]=true;
        boxes[index]=redBox;
        if(isFull()) {gameMode=2;firstRun=true;return;}

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                markBox();
                }
        },timeInterval());

    }


    @Override
    public void show() {
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        boxWidth = width/3;
        boxHeight = height/4;
        boxes = new Texture[12];
        isMarked = new boolean[12];
        whiteBox = new Texture("box.png");
        greenBox = new Texture("green2.png");
        greenBox0= new Texture("green1.png");
        redBox0 = new Texture("red.png");
        redBox = new Texture("red0.png");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("def_bg.mp3"));
        good = Gdx.audio.newSound(Gdx.files.internal("good.mp3"));
        clearBoxes();
        gameMode = 1;
        firstRun=true;
        score=0;
    }

    @Override
    public void render(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new MainMenu(game));
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();

        if(gameMode==1){
            drawBoxes();
            if(firstRun) {
                firstRun=false;backgroundMusic.play();backgroundMusic.setVolume(0.5f);backgroundMusic.setLooping(true);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        markBox();
                    }
                },1f);
            }

            if(Gdx.input.justTouched()){
                int x=Gdx.input.getX();
                int y=(int)height-Gdx.input.getY();
                for(int index=0;index<12;index++)
                if(x>index%3*boxWidth && x<index%3*boxWidth+boxWidth && y>index/3*boxHeight && y<index/3*boxHeight+boxHeight && isMarked[index]) {
                    isMarked[index]=false;
                    boxes[index]=whiteBox;
                    score++;
                    good.play();
                }
            }
        }
        if(gameMode==2){
            game.setScreen(new Score(game,1,score));
            dispose();
        }
        batch.end();

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
        redBox.dispose();
        whiteBox.dispose();
        greenBox.dispose();
        greenBox0.dispose();
        redBox0.dispose();
        for(int i=0;i<12;i++) boxes[i].dispose();
        backgroundMusic.dispose();
        good.dispose();
    }
}
