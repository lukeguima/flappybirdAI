package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoTopo;
	private Texture canoBaixo;
	private Texture gameOver;
	private Random numeroRandomico;
	private float variacao = 0;
	private float deltaTime;
	private float velocidadeDeQueda = 0;
	private float posicaoInicialVertical = 0;
	private float larguraDipositivo;
	private float alturaDispositivo;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float alturaentreOsCanosRandomica;
	private ShapeRenderer shape;
	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	private BitmapFont fonte;
	private int pontuacao = 0;
	private boolean marcouponto = false;

	private int estadoDoJogo = 0; // 0-> jogo não iniciado; 1->Jogo iniciado; 2->Game Over

	@Override
	public void create() {
		batch = new SpriteBatch();
		passaros = new Texture[3];
		fundo = new Texture("fundo.png");
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		larguraDipositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

		posicaoMovimentoCanoHorizontal = larguraDipositivo;
		numeroRandomico = new Random();

		espacoEntreCanos = 300;

		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");

		gameOver = new Texture("game_over.png");

		shape = new ShapeRenderer();
		passaroCirculo = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoTopo = new Rectangle();

		fonte = new BitmapFont();
		fonte.setColor(Color.YELLOW);
		fonte.getData().setScale(6);

		posicaoInicialVertical = Gdx.graphics.getHeight() / 2;
	}

	@Override
	public void render() {
		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 20;
		if (variacao > 2)
			variacao = 0;

		if (Gdx.input.justTouched()) {
			estadoDoJogo = 1;
		}

		if (estadoDoJogo == 1) {
			velocidadeDeQueda++;
			posicaoInicialVertical = posicaoInicialVertical - velocidadeDeQueda;
			posicaoMovimentoCanoHorizontal -= deltaTime * 200;

			float randomCanoBaixo = alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2
					+ alturaentreOsCanosRandomica;
			float randomCanoTopo = alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaentreOsCanosRandomica;

			// Bird jogando sozinho
			float vetorCanoBaixo = randomCanoBaixo;
			float vetorCanoTopo = randomCanoTopo;
			System.out.printf("retanguloCanoBaixo %f \n", vetorCanoBaixo);
			System.out.printf("retanguloCanoTopo %f \n", vetorCanoTopo);
			System.out.printf("posicaoInicialVertical %f \n", posicaoInicialVertical);
			float decidaoBird = posicaoInicialVertical + 80;
			System.out.printf("decidaoBird %f \n", decidaoBird);
			if (decidaoBird < vetorCanoTopo) {
				velocidadeDeQueda = -2;
			} else {
				velocidadeDeQueda = +4;
			}

			// Verifica se os canos sumiram
			if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
				posicaoMovimentoCanoHorizontal = larguraDipositivo;
				alturaentreOsCanosRandomica = numeroRandomico.nextInt(400) - 200;
				marcouponto = false;
			}

			if (posicaoMovimentoCanoHorizontal < 120) {
				if (!marcouponto) {
					pontuacao++;
					marcouponto = true;
				}
			}
		}

		// Similar a um quadro em branco para ser desenhado
		batch.begin();
		batch.draw(fundo, 0, 0, larguraDipositivo, alturaDispositivo);
		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaentreOsCanosRandomica);
		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaentreOsCanosRandomica);
		batch.draw(passaros[(int) variacao], 150, posicaoInicialVertical);

		fonte.draw(batch, String.valueOf(pontuacao), larguraDipositivo / 2, alturaDispositivo - 100);

		if (estadoDoJogo == 2) {
			batch.draw(gameOver, larguraDipositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
		}
		batch.end();

		passaroCirculo.set(150 + passaros[0].getWidth() / 2, posicaoInicialVertical + passaros[0].getHeight() / 2,
				passaros[0].getWidth() / 2);

		retanguloCanoBaixo = new Rectangle(
				posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaentreOsCanosRandomica,
				canoBaixo.getWidth(), canoBaixo.getHeight());

		retanguloCanoTopo = new Rectangle(
				posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaentreOsCanosRandomica,
				canoTopo.getWidth(), canoTopo.getHeight());

		// Teste de colisão
		if (Intersector.overlaps(passaroCirculo, retanguloCanoBaixo)
				|| Intersector.overlaps(passaroCirculo, retanguloCanoTopo) || posicaoInicialVertical <= 0
				|| posicaoInicialVertical >= alturaDispositivo) {
			estadoDoJogo = 2;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		passaros[0].dispose();
		passaros[1].dispose();
		passaros[2].dispose();
		fundo.dispose();
	}
}
