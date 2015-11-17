package br.grupointegrado.flappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Thomas on 28/09/2015.
 */
public class TelaJogo extends TelaBase {



    private OrthographicCamera camera; // camera do jogo
    private World mundo; //representa o mundo do box2d
    private Body chao; //corpo do chao
    private Passaro passaro;
    private Array<Obstaculo> obstaculo = new Array<Obstaculo>();
    private int pontuacao = 0;
    private BitmapFont fontePontuacao;
    private Stage palcoInformacoes;
    private Stage palcoInformacaoes;
    private Label lbPontuacao;
    private ImageButton btnPlay;
    private ImageButton btnGameOver;
    private OrthographicCamera cameraInfo;

    private Texture[] texturasPassaro;
    private Texture texturaObstaculoCima;
    private Texture texturaObstaculoBaixo;
    private Texture texturaChao;
    private Texture texturaFundo;
    private Texture texturaPlay;
    private Texture texturaGameOver;

    private boolean jogoIniciado = false;

    private Box2DDebugRenderer debug; // desenha o mundo na tela para ajudar no desenvolvimento

    public TelaJogo(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth() / Util.escala, Gdx.graphics.getHeight() / Util.escala);
        cameraInfo = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        debug = new Box2DDebugRenderer();
        mundo = new World(new Vector2(0,-9.8f), false);
        mundo.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                detectarColisao(contact.getFixtureA(), contact.getFixtureB());
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
        initTexturas();
        initChao();
        initPassaro();
        initFonte();
        initInformacoes();


    }

    private void initTexturas() {
        texturasPassaro = new Texture[3];
        texturasPassaro[0] = new Texture("sprites/bird-1.png");
        texturasPassaro[1] = new Texture("sprites/bird-2.png");
        texturasPassaro[2] = new Texture("sprites/bird-3.png");

        texturaObstaculoCima = new Texture("sprites/toptube.png");
        texturaObstaculoBaixo = new Texture("sprites/bottomtube.png");

        texturaFundo = new Texture("sprites/bg.png");
        texturaChao = new Texture("sprites/ground.png");

        texturaPlay = new Texture("sprites/playbtn.png");
        texturaGameOver = new Texture("sprites/gameover.png");

    }

    private boolean gameOver = false;

    private void detectarColisao(Fixture fixtureA, Fixture fixtureB) {
        if ("passaro".equals(fixtureA.getUserData()) || "passaro".equals(fixtureB.getUserData())){

        }
    }

    private void initFonte() {
        FreeTypeFontGenerator.FreeTypeFontParameter fonteParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fonteParam.size = 56;
        fonteParam.color = Color.WHITE;
        fonteParam.shadowColor = Color.BLACK;
        fonteParam.shadowOffsetX = 4;
        fonteParam.shadowOffsetY = 4;

        FreeTypeFontGenerator gerador =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        fontePontuacao = gerador.generateFont(fonteParam);
    }

    private void initInformacoes() {
        palcoInformacaoes = new Stage(new FillViewport(cameraInfo.viewportWidth,cameraInfo.viewportHeight, cameraInfo));
        Gdx.input.setInputProcessor(palcoInformacaoes);

        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontePontuacao;
        lbPontuacao = new Label("0",estilo);
        palcoInformacaoes.addActor(lbPontuacao);

        //Inicia Botao
        ImageButton.ImageButtonStyle estiloBotao = new ImageButton.ImageButtonStyle();
        estiloBotao.up = new SpriteDrawable(new Sprite(texturaPlay));

        btnPlay = new ImageButton(estiloBotao);
        btnPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                jogoIniciado = true;
            }
        });
        palcoInformacoes.addActor(btnPlay);

        estiloBotao = new ImageButton.ImageButtonStyle();
        estiloBotao.up = new SpriteDrawable(new Sprite(texturaGameOver));

        btnGameOver = new ImageButton(estiloBotao);
        btnGameOver.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reiniciarJogo();
            }
        });
        palcoInformacoes.addActor(btnGameOver);
    }

    private void reiniciarJogo(){
        game.setScreen(new TelaJogo(game));
    }

    private void initChao() {
        chao = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, 0, 0);


    }

    private void initPassaro() {
        passaro = new Passaro(mundo,camera,null);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);// limpa a tela e pinta a cor de fundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);// mantem o buffer de cores

        capturaTeclas();
        atualizar(delta);
        atualizar(delta);
        renderizar(delta);
        debug.render(mundo, camera.combined.cpy().scl(Util.pixel_metro));

    }
    private boolean pulando = false;

    private void capturaTeclas() {
        pulando = false;
        if (Gdx.input.justTouched()){
            pulando = true;
        }

    }


    /**
     * renderizar/desenhar
     * @param delta
     */
    private void renderizar(float delta) {
        palcoInformacaoes.draw();

    }

    /**
     * atualização e calculo dos corpos
     * @param delta
     */

    private void atualizar(float delta) {
        palcoInformacaoes.act(delta);

        passaro.getCorpo().setFixedRotation(!gameOver);
        passaro.atualizar(delta, !gameOver);
        if (jogoIniciado){
            mundo.step(1f / 60f, 6, 2);
            atualizarObstaculo();
        }
        atualizaInformacoes();

        if (!gameOver) {
            atualizarCamera();
            atualizarChao();
        }

        if (pulando && !gameOver){
            passaro.pular();
        }

    }

    private void atualizaInformacoes() {
        lbPontuacao.setText(pontuacao + "");
        lbPontuacao.setPosition(cameraInfo.viewportWidth / 2 - lbPontuacao.getPrefWidth() / 2, cameraInfo.viewportHeight - lbPontuacao.getPrefHeight());

        btnPlay.setPosition(cameraInfo.viewportWidth / 2 - btnPlay.getPrefWidth() / 2, cameraInfo.viewportHeight / 2 - btnPlay.getPrefHeight() / 2);
        btnPlay.setVisible(!jogoIniciado);

        btnPlay.setPosition(cameraInfo.viewportWidth / 2 - btnGameOver.getPrefWidth() / 2, cameraInfo.viewportHeight / 2 - btnGameOver.getPrefHeight() / 2);
        btnGameOver.setVisible(gameOver);
    }

    private void atualizarObstaculo() {
        while (obstaculo.size < 4){
            Obstaculo ultimo = null;
            if (obstaculo.size > 0)
                ultimo = obstaculo.peek();

            Obstaculo o = new Obstaculo(mundo,camera,ultimo);
            obstaculo.add(o);
        }

        for (Obstaculo o : obstaculo){
            float inicioCamera = passaro.getCorpo().getPosition().x - (camera.viewportWidth / 2 / Util.pixel_metro) - o.getLargura();
            if (inicioCamera > o.getPosX()){
                o.remove();
                obstaculo.removeValue(o,true);
            }else if (!o.isPassou()&& o.getPosX() < passaro.getCorpo().getPosition().x ){
                o.setPassou(true);
                pontuacao++;
            }
        }

    }

    private void atualizarCamera() {
        camera.position.x = (passaro.getCorpo().getPosition().x + 34 / Util.pixel_metro) * Util.pixel_metro;
        camera.update();

    }

    /**
     * atualiza a posição do chao para acompanhar o passaro
     */
    private void atualizarChao() {
        Vector2 posicao = passaro.getCorpo().getPosition();

        chao.setTransform(posicao.x, 0, 0);

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width / Util.escala, height / Util.escala);
        camera.update();
        redimensionaChao();
        cameraInfo.setToOrtho(false, width, height);
        cameraInfo.update();
    }

    /**
     * Configura o tamanho do chão de acordo com o tamanho da tela.
     */
    private void redimensionaChao() {
        chao.getFixtureList().clear();
        float largura = camera.viewportWidth / Util.pixel_metro;
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura /2, Util.altura_chao / 2);
        Fixture forma = Util.criarForma(chao, shape, "chao");
        shape.dispose();

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
    debug.dispose();
        mundo.dispose();
        palcoInformacaoes.dispose();
        fontePontuacao.dispose();
        texturasPassaro[0].dispose();
        texturasPassaro[1].dispose();
        texturasPassaro[2].dispose();

        texturaObstaculoCima.dispose();
        texturaObstaculoBaixo.dispose();

        texturaFundo.dispose();
        texturaChao.dispose();

        texturaPlay.dispose();
        texturaGameOver.dispose();
    }
}
