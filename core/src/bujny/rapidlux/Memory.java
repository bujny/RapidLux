package bujny.rapidlux;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;

import sun.rmi.runtime.Log;

/**
 * Created by Mateusz Bujnowicz on 3/15/2018.
 */

public class Memory implements Screen {
    final Start game;

    SpriteBatch batch;

    float timeInterval = 0.75f;
    int score;
    int currentIndex;

    ArrayList<Integer> order;

    float width;
    float height;
    float boxWidth;
    float boxHeight;

    Texture[] boxes;
    Texture whiteBox;
    Texture greenBox;

    boolean firstRun;
    boolean isPlaying;

    Music backgroundMusic;
    Sound good;
    Sound tada;

    public Memory(Start game) {
        this.game = game;
    }

    void clearBoxes(){
        for(int i=0;i<12;i++) {
            boxes[i] = whiteBox;
        }
    }

    void  drawBoxes(){
        for (int i = 0; i < 12; i++) batch.draw(boxes[i], i % 3 * boxWidth, i / 3 * boxHeight, boxWidth, boxHeight);
    }

    void showBoxes(){
        clearBoxes();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                good.play();
                boxes[order.get(currentIndex)]=greenBox;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        if(score==currentIndex) {
                            isPlaying=true;
                            clearBoxes();
                            currentIndex=0;
                            return;
                        }
                        currentIndex++;
                        showBoxes();
                    }
                },timeInterval);
            }
        },0.3f);
    }

    void generateSequence(){
        isPlaying=false;
        Random random = new Random();
        order.clear();
        for(int i=0;i<score+1;i++){
            order.add(random.nextInt(12));
        }
        currentIndex=0;
        showBoxes();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        boxWidth = width/3;
        boxHeight = height/4;
        boxes = new Texture[12];
        whiteBox = new Texture("box.png");
        greenBox = new Texture("green2.png");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("memory.mp3"));
        good = Gdx.audio.newSound(Gdx.files.internal("good.mp3"));
        tada = Gdx.audio.newSound(Gdx.files.internal("tada.mp3"));
        clearBoxes();
        firstRun=true;
        score=0;
        isPlaying=false;
        order = new ArrayList<Integer>();
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                generateSequence();
            }
        },1f);
    }

    @Override
    public void render(float delta) {
        if(firstRun) {
            firstRun=false;backgroundMusic.play();backgroundMusic.setVolume(0.1f);backgroundMusic.setLooping(true);
        }

        if(Gdx.input.isKeyPressed(Input.Keys.BACK)){
            dispose();
            game.setScreen(new MainMenu(game));
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        drawBoxes();

        if(isPlaying){if(Gdx.input.justTouched()){
            int x=Gdx.input.getX();
            int y=(int)height-Gdx.input.getY();
                if(x>order.get(currentIndex)%3*boxWidth && x<order.get(currentIndex)%3*boxWidth+boxWidth && y>order.get(currentIndex)/3*boxHeight && y<order.get(currentIndex)/3*boxHeight+boxHeight) {
                    boxes[order.get(currentIndex)]=greenBox;
                    final int clearThat=order.get(currentIndex);
                    currentIndex++;
                    if(currentIndex==score+1){
                        tada.play(0.5f);
                        score++;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                generateSequence();
                            }
                        },1.5f);
                    }
                    else good.play();
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            boxes[clearThat]=whiteBox;
                        }
                    },0.3f);
                }
                else {
                    game.setScreen(new Score(game,2,score));
                }
        }}

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
        whiteBox.dispose();
        greenBox.dispose();
        for(int i=0;i<12;i++) boxes[i].dispose();
        backgroundMusic.dispose();
        good.dispose();
        tada.dispose();
    }
}
