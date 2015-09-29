package br.grupointegrado.flappyBird;


import com.badlogic.gdx.Screen;

public abstract class TelaBase implements Screen {

    protected  MainGame game;

    public TelaBase(MainGame game){
        this.game = game;

    }

    public void hide() {
        dispose();
    }
}
