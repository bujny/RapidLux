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

import java.sql.Time;
import java.util.Random;

/**
 * Created by Mateusz Bujnowicz on 3/13/2018.
 */

public class TwoBoxes implements Screen {
    final Start game;

    SpriteBatch batch;

    int gameMode = 1;
    float timeInterval = 1.0f;
    float timeRate = 0.005f;
    int score=0;

    int index1,index2;

    float width;
    float height;
    float boxWidth;
    float boxHeight;
    float textWidth;

    Texture[] boxes;
    Texture whiteBox;
    Texture greenBox;
    Texture greenBox0;
    Texture redBox;
    Texture redBox0;

    boolean wasTapped1;
    boolean wasTapped2;
    boolean firstRun;
    boolean itsTrap;


    Music backgroundMusic;
    Sound good,bad;

    void clearBoxes(){
        for(int i=0;i<12;i++) boxes[i] = whiteBox;
    }

    void  drawBoxes(){
        for (int i = 0; i < 12; i++)
            batch.draw(boxes[i], i % 3 * boxWidth, i / 3 * boxHeight, boxWidth, boxHeight);
    }

    void markBox(){
        clearBoxes();
        Random random = new Random();
        if(random.nextInt(6)==0) itsTrap=true;
        else itsTrap=false;
        index1 = random.nextInt(12);
        do {
            index2 = random.nextInt(12);
        }while(index1==index2);
        if(itsTrap){
            boxes[index1] = redBox0;
            boxes[index2] = redBox0;
        }else {
            boxes[index1] = greenBox0;
            boxes[index2] = greenBox0;
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                if(itsTrap){
                    if(wasTapped1 || wasTapped2)
                    {
                        gameMode=2;
                        firstRun=true;
                    }
                    else{
                        wasTapped2=false;
                        wasTapped1=false;
                        markBox();
                    }
                }
                else{
                    if(!wasTapped1 || !wasTapped2)
                    {
                        firstRun=true;
                        gameMode=2;
                    }
                    else {
                        score++;
                        wasTapped2=false;
                        wasTapped1=false;
                        markBox();
                    }
                }
            }
        },timeInterval-score*timeRate);
    }

    public TwoBoxes(Start game) {
        this.game = game;
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
        greenBox0= new Texture("green1.png");
        redBox0 = new Texture("red.png");
        redBox = new Texture("red0.png");
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        bad = Gdx.audio.newSound(Gdx.files.internal("bad.mp3"));
        good = Gdx.audio.newSound(Gdx.files.internal("good.mp3"));

        clearBoxes();
        gameMode = 1;
        firstRun=true;
        score=0;
        wasTapped2=false;
        wasTapped1=false;
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

        if(gameMode==1) {
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
                if(x>index1%3*boxWidth && x<index1%3*boxWidth+boxWidth && y>index1/3*boxHeight && y<index1/3*boxHeight+boxHeight) {
                    wasTapped1=true;
                    if(!itsTrap){boxes[index1]=greenBox;good.play();}
                    else {boxes[index1]=redBox;bad.play();}
                }
                if(x>index2%3*boxWidth && x<index2%3*boxWidth+boxWidth && y>index2/3*boxHeight && y<index2/3*boxHeight+boxHeight) {
                    wasTapped2=true;
                    if(!itsTrap){boxes[index2]=greenBox;good.play();}
                    else {boxes[index2]=redBox;bad.play();}
                }

            }
        }
        else if(gameMode==2) {
            game.setScreen(new Score(game,0,score));
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
        bad.dispose();
        good.dispose();
    }
}
