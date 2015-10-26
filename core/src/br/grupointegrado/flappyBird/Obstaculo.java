package br.grupointegrado.flappyBird;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Obstaculo {

    private World mundo;
    private OrthographicCamera camera;
    private Body corpoCima, corpoBaixo;
    private float posX;
    private float posycima, posyBaixo;

    private float largura, altura;
    private  boolean passou;
    private Obstaculo ultimoObstaculo; // ultimo antes do atual

    public Obstaculo(World mundo, OrthographicCamera camera,Obstaculo ultimoObstaculo){

        this.mundo = mundo;
        this.camera = camera;
        this.ultimoObstaculo = ultimoObstaculo;

        initPosicao();
        InitCorpoCima();
        InitCorpoBaixo();


    }

    private void InitCorpoBaixo() {
        corpoBaixo = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX,posyBaixo);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2,altura / 2);

        Util.criarForma(corpoBaixo, shape, "obstaculo_baixo");
        shape.dispose();
    }

    private void InitCorpoCima() {

        corpoCima = Util.criarCorpo(mundo, BodyDef.BodyType.StaticBody, posX,posycima);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(largura / 2,altura / 2);

        Util.criarForma(corpoCima, shape, "obstaculo_cima");
        shape.dispose();

    }

    public void remove(){
        mundo.destroyBody(corpoBaixo);
        mundo.destroyBody(corpoCima);

    }

    private void initPosicao() {
        largura = 40 / Util.pixel_metro;
        altura = camera.viewportHeight / Util.pixel_metro;

        float xInicial = largura;
        if (ultimoObstaculo != null) {
            xInicial = ultimoObstaculo.getPosX();
        }
        posX = xInicial + 8; // espaço entre os obstaculos

        float parcela = (altura - Util.altura_chao / 6); // tamanho da tela dividido por 6 para encontrar a posiçao y do obstaculo;

        int multiplicador = MathUtils.random(1,3);

        posyBaixo = Util.altura_chao + (parcela * multiplicador) - (altura / 2);

        posycima = posyBaixo + altura + 2f;




    }
    public float getPosX(){
        return posX;
    }

}
