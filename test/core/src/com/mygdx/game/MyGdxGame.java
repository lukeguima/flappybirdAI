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
	private float velocidadeDeQueda[] = new float[10];
	private float posicaoInicialVertical[] = new float[10];
	private float decidaoBird[] = new float[10];
	private float larguraDipositivo;
	private float alturaDispositivo;
	private float posicaoMovimentoCanoHorizontal;
	private float espacoEntreCanos;
	private float alturaentreOsCanosRandomica;
	private ShapeRenderer shape;
	private Circle passaroCirculo[] = new Circle[10];
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
	private BitmapFont fonte;
	private int pontuacao = 0;
	private boolean marcouponto = false;
	private boolean lifeBird[] = new boolean[3];
	private int contBirdLife = 0;

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
		passaroCirculo[0] = new Circle();
		passaroCirculo[1] = new Circle();
		passaroCirculo[2] = new Circle();
		retanguloCanoBaixo = new Rectangle();
		retanguloCanoTopo = new Rectangle();

		fonte = new BitmapFont();
		fonte.setColor(Color.YELLOW);
		fonte.getData().setScale(6);

		posicaoInicialVertical[0] = Gdx.graphics.getHeight() / 2;
		posicaoInicialVertical[1] = Gdx.graphics.getHeight() / 2;
		posicaoInicialVertical[2] = Gdx.graphics.getHeight() / 2;

		lifeBird[0] = true;
		lifeBird[1] = true;
		lifeBird[2] = true;

	}

	@Override
	public void render() {
		// Quantidade de Birds jogando
		contBirdLife = 3;

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 20;
		if (variacao > 2)
			variacao = 0;

		if (Gdx.input.justTouched()) {
			estadoDoJogo = 1;
		}

		if (estadoDoJogo == 1) {
			velocidadeDeQueda[0]++;
			velocidadeDeQueda[1]++;
			velocidadeDeQueda[2]++;
			posicaoInicialVertical[0] = posicaoInicialVertical[0] - velocidadeDeQueda[0];
			posicaoInicialVertical[1] = posicaoInicialVertical[1] - velocidadeDeQueda[1];
			posicaoInicialVertical[2] = posicaoInicialVertical[2] - velocidadeDeQueda[2];
			posicaoMovimentoCanoHorizontal -= deltaTime * 200;

			// Bird jogando sozinho
			float alturaCanoTopo = alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaentreOsCanosRandomica;
			float alturaCanoBaixo = alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2
					+ alturaentreOsCanosRandomica;

			// Bird 1 (Amarelo)
			if (lifeBird[0] == true) {
				decidaoBird[0] = posicaoInicialVertical[0] + 60;
				if (decidaoBird[0] < alturaCanoTopo) {
					velocidadeDeQueda[0] = -2;
				} else {
					velocidadeDeQueda[0] = +4;
				}
			}

			// Bird 2 (Preto)
			if (lifeBird[1] == true) {
				decidaoBird[1] = posicaoInicialVertical[1] + 100;
				if (decidaoBird[1] < alturaCanoTopo) {
					velocidadeDeQueda[1] = -3;
				} else {
					if (alturaCanoBaixo <= -500 || alturaCanoTopo >= 550) {
						velocidadeDeQueda[1] = +20;
					}
					velocidadeDeQueda[1] = +6;
				}
			}

			// Bird 3 (Rosa)
			if (lifeBird[2] == true) {
				decidaoBird[2] = posicaoInicialVertical[2] + 100;
				if (decidaoBird[2] < alturaCanoTopo) {
					velocidadeDeQueda[2] = -5;
				} else {
					velocidadeDeQueda[2] = +5;
				}

			}

			// Verificar se os canos sumiram
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

		// Simulando
		batch.begin();

		batch.draw(fundo, 0, 0, larguraDipositivo, alturaDispositivo);

		batch.draw(canoTopo, posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaentreOsCanosRandomica);

		batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaentreOsCanosRandomica);

		batch.draw(passaros[0], 150, posicaoInicialVertical[0]);
		batch.draw(passaros[1], 150, posicaoInicialVertical[1]);
		batch.draw(passaros[2], 150, posicaoInicialVertical[2]);

		fonte.draw(batch, String.valueOf(pontuacao), larguraDipositivo / 2, alturaDispositivo - 100);

		if (estadoDoJogo == 2) {
			batch.draw(gameOver, larguraDipositivo / 2 - gameOver.getWidth() / 2, alturaDispositivo / 2);
		}
		batch.end();

		// Bird 1
		passaroCirculo[0].set(150 + passaros[0].getWidth() / 2,
				posicaoInicialVertical[0] + passaros[0].getHeight() / 2,
				passaros[0].getWidth() / 2);

		// Bird 2
		passaroCirculo[1].set(150 + passaros[1].getWidth() / 2,
				posicaoInicialVertical[1] + passaros[1].getHeight() / 2,
				passaros[1].getWidth() / 2);

		// Bird 3
		passaroCirculo[2].set(150 + passaros[1].getWidth() / 2,
				posicaoInicialVertical[2] + passaros[2].getHeight() / 2,
				passaros[1].getWidth() / 2);

		retanguloCanoBaixo = new Rectangle(
				posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaentreOsCanosRandomica,
				canoBaixo.getWidth(), canoBaixo.getHeight());

		retanguloCanoTopo = new Rectangle(
				posicaoMovimentoCanoHorizontal,
				alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaentreOsCanosRandomica,
				canoTopo.getWidth(), canoTopo.getHeight());

		// Teste de colisões dos passaros

		// Bird 1
		if (Intersector.overlaps(passaroCirculo[0], retanguloCanoBaixo)
				|| Intersector.overlaps(passaroCirculo[0], retanguloCanoTopo) ||
				posicaoInicialVertical[0] <= 0
				|| posicaoInicialVertical[0] >= alturaDispositivo) {
			lifeBird[0] = false;
			contBirdLife--;
		}

		// Bird 2
		if (Intersector.overlaps(passaroCirculo[1], retanguloCanoBaixo)
				|| Intersector.overlaps(passaroCirculo[1], retanguloCanoTopo) ||
				posicaoInicialVertical[1] <= 0
				|| posicaoInicialVertical[1] >= alturaDispositivo) {
			lifeBird[1] = false;
			contBirdLife--;
		}

		// Bird 3
		if (Intersector.overlaps(passaroCirculo[2], retanguloCanoBaixo)
				|| Intersector.overlaps(passaroCirculo[2], retanguloCanoTopo) ||
				posicaoInicialVertical[2] <= 0
				|| posicaoInicialVertical[2] >= alturaDispositivo) {
			lifeBird[2] = false;
			contBirdLife--;
		}

		// Verificando a quantidade de birds vivos
		if (contBirdLife == 0) {
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
