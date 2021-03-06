package br.grupointegrado.flappyBird;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.sun.media.sound.UlawCodec;

/**
 * Created by Thomas on 05/10/2015.
 */
public class Passaro {

    private final World mundo;
    private final OrthographicCamera camera;
    private final Texture[] texturas;
    private Body corpo;

    public Passaro(World mundo,OrthographicCamera camera, Texture[] texturas){

        this.mundo = mundo;
        this.camera = camera;
        this.texturas = texturas;

        initCorpo();
    }

    private void initCorpo() {

        float x = (camera.viewportWidth / 2) / Util.pixel_metro;
        float y = (camera.viewportHeight/ 2) / Util.pixel_metro;

        corpo = Util.criarCorpo(mundo, BodyDef.BodyType.DynamicBody, x ,y);

        FixtureDef definicao = new FixtureDef();
        definicao.density = 1; // densidade do corpo
        definicao.friction = 0.04f; // fric��o/atrito entre um corpo e outro
        definicao.restitution = 0.3f; // elasticidadedo corpo

        BodyEditorLoader loader = new BodyEditorLoader(Gdx.files.internal("physics/bird.json"));
        loader.attachFixture(corpo, "bird", definicao, 1, "PASSARO");


    }

    /**
     * atualiza o comportamento do passaro
     * @param delta
     */
    public void atualizar(float delta, boolean movimentar) {
                if (movimentar){
                        atualizarVelocidade();
                        atualizarRotacao();
                }
            }
        private void atualizarRotacao(){
        float velocidadeY = corpo.getLinearVelocity().y;
                float rotacao = 0;

        if (velocidadeY < 0) {
            rotacao = -15;
        }else if (velocidadeY > 0){
            rotacao = 15;
        }else{
            rotacao = 0;
        }
        rotacao = (float) Math.toRadians(rotacao);
        corpo.setTransform(corpo.getPosition(), rotacao);


    }

    private void atualizarVelocidade() {

        corpo.setLinearVelocity(2f,corpo.getLinearVelocity().y);
    }

    /**
     * Aplica uma for�a positiva no y para similar o pulo
     */
    public void pular(){
        corpo.setLinearVelocity(corpo.getLinearVelocity().x, 0);
        corpo.applyForceToCenter(0, 100, false);
    }

    public Body getCorpo(){
        return corpo;
    }
}
