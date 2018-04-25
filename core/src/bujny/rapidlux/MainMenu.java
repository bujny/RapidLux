package bujny.rapidlux;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

import sun.rmi.runtime.Log;

/**
 * Created by Mateusz Bujnowicz on 3/13/2018.
 */

public class MainMenu implements Screen {
    final Start game;
    SpriteBatch batch;

    float width;
    float height;
    float logoWidth;
    float logoHeight;
    float buttonHeight;
    float buttonHeight1;
    float buttonWidth;
    float buttonWidth1;
    float space;
    float defenceHeight;
    float textWidth;
    float textHeight;

    Texture logo;
    Texture play;
    Texture high;
    Texture two_boxes;
    Texture defence;
    Texture memory;

    Music music;

    int menu=0;
    int highScore0;
    int highScore1;
    int highScore2;

    BitmapFont font;


    public MainMenu(Start game) {
        this.game = game;
    }



    @Override
    public void show() {
        batch = new SpriteBatch();
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        logoWidth = width*4/5;
        logoHeight = height*0.3f;
        space = height*0.035f;
        buttonHeight=height*0.1f;
        buttonWidth = width*0.5f;
        buttonWidth1=width*0.6f;
        buttonHeight1=height*0.15f;
        defenceHeight=height*0.08f;
        logo = new Texture("title.png");
        play = new Texture("play1.png");
        high = new Texture("high1.png");
        memory = new Texture("memory.png");
        two_boxes = new Texture("two_boxes.png");
        defence = new Texture("defence.png");
         music = Gdx.audio.newMusic(Gdx.files.internal("main.mp3"));
        music.setVolume(0.3f);music.setLooping(true);music.play();
        highScore0 = Start.database.getHighScore(0);
        highScore1 = Start.database.getHighScore(1);
        highScore2 = Start.database.getHighScore(2);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(height/250);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        GlyphLayout text= new GlyphLayout(font,Integer.toString(666));
        textWidth = text.width;
        textHeight=text.height;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(logo,width/2-logoWidth/2,height*0.95f-logoHeight,logoWidth,logoHeight);
        if(menu==0) {
            Gdx.input.setCatchBackKey(false);
            batch.draw(play, width / 2 - buttonWidth / 2, height * 0.95f - logoHeight - buttonHeight - space, buttonWidth, buttonHeight);
            batch.draw(high, width / 2 - buttonWidth1 / 2, height * 0.95f - logoHeight - buttonHeight - buttonHeight1 - space * 2, buttonWidth1, buttonHeight1);
            if(Gdx.input.justTouched()){
                int x=Gdx.input.getX();
                int y=(int)height-Gdx.input.getY();
                if(x>(width-buttonWidth)/2 && x<buttonWidth+(width-buttonWidth)/2 &&
                       y>height * 0.95f - logoHeight - buttonHeight - space
                               && y< height * 0.95f - logoHeight - space) {
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            menu=1;
                        }
                    },0.01f );
                }
                if(x>(width-buttonWidth1)/2 && x<buttonWidth1+(width-buttonWidth1)/2 &&
                        y>height * 0.95f - logoHeight - buttonHeight - space*2 - buttonHeight1
                        && y< height * 0.95f - logoHeight - space*2 - buttonHeight) menu=2;

            }
        }
        if(menu==1){
            batch.draw(two_boxes, width / 2 - buttonWidth1 / 2, height * 0.95f - logoHeight - buttonHeight - space, buttonWidth1, buttonHeight);
            batch.draw(defence, width / 2 - buttonWidth / 2, height * 0.95f - logoHeight - buttonHeight - defenceHeight - space * 2, buttonWidth, defenceHeight);
            batch.draw(memory,width/2-buttonWidth/2,height*0.95f-logoHeight-buttonHeight-defenceHeight*2-space*3,buttonWidth,defenceHeight);
            Gdx.input.setCatchBackKey(true);
            if(Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                menu=0;
            }
            if(Gdx.input.justTouched()){
                int x=Gdx.input.getX();
                int y=(int)height-Gdx.input.getY();
                if(x>(width-buttonWidth1)/2 && x<buttonWidth1+(width-buttonWidth1)/2 &&
                        y>height * 0.95f - logoHeight - buttonHeight - space
                        && y< height * 0.95f - logoHeight - space)
                {
                    dispose();
                    game.setScreen(new TwoBoxes(game));
                }
                if(x>(width-buttonWidth)/2 && x<buttonWidth+(width-buttonWidth)/2 &&
                        y>height * 0.95f - logoHeight - buttonHeight - space*2 - defenceHeight
                        && y< height * 0.95f - logoHeight - space*2 - buttonHeight) {
                    dispose();
                    game.setScreen(new Defence(game));
                }
                if(x>(width-buttonWidth)/2 && x<buttonWidth+(width-buttonWidth)/2 &&
                        y>height*0.95f-logoHeight-buttonHeight-defenceHeight*2-space*3
                        && y< height*0.95f-logoHeight-buttonHeight-defenceHeight-space*3) {
                    dispose();
                    game.setScreen(new Memory(game));
                }
            }

        }
        if(menu==2){
            Gdx.input.setCatchBackKey(true);
            if(Gdx.input.isKeyPressed(Input.Keys.BACK)) menu=0;
            //HIGH SCORES SECTION
            batch.draw(two_boxes, width / 20, height * 0.95f - logoHeight - buttonHeight - space, buttonWidth1, buttonHeight);
            font.draw(batch,Integer.toString(highScore0),width-textWidth-width/40,height * 0.95f - logoHeight -  buttonHeight*0.30f -  space);
            batch.draw(defence, width / 20, height * 0.95f - logoHeight - buttonHeight - defenceHeight - space * 2, buttonWidth, defenceHeight);
            font.draw(batch,Integer.toString(highScore1),width-textWidth-width/40,height * 0.95f - logoHeight - buttonHeight - defenceHeight*0.26f  - space * 2);
            batch.draw(memory,width/20,height*0.95f-logoHeight-buttonHeight-defenceHeight*2-space*3,buttonWidth,defenceHeight);
            font.draw(batch,Integer.toString(highScore2),width-textWidth-width/40,height*0.95f-logoHeight-buttonHeight-defenceHeight - defenceHeight*0.2f -space*3);
        }
        batch.end();//
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
         logo.dispose();
         play.dispose();
         high.dispose();
         two_boxes.dispose();
         defence.dispose();
         music.dispose();
         memory.dispose();


    }
}
