package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

/** Pantalla de juego principal */
public class GameScreen implements Screen {
    private MainGame game;
    private Texture blockTexture;
    private ArrayList<Rectangle> blocks; // Lista de bloques apilados
    private Rectangle currentBlock; // Bloque actual que cae
    private float blockSpeed; // Velocidad horizontal del bloque
    private boolean movingRight; // Dirección del movimiento
    private int score; // Altura de la torre
    private BitmapFont font;

    public GameScreen(MainGame game) {
        this.game = game;
        blockTexture = new Texture("block.png");
        blocks = new ArrayList<>();
        blocks.add(new Rectangle(100, 100, 200, 50)); // Bloque base
        spawnNewBlock();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    private void spawnNewBlock() {
        currentBlock = new Rectangle(100, MainGame.HEIGHT, 200, 50); // Nuevo bloque
        blockSpeed = 200; // Velocidad horizontal
        movingRight = true;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Movimiento del bloque actual
        if (movingRight) {
            currentBlock.x += blockSpeed * delta;
            if (currentBlock.x + currentBlock.width >= MainGame.WIDTH) {
                movingRight = false;
            }
        } else {
            currentBlock.x -= blockSpeed * delta;
            if (currentBlock.x <= 0) {
                movingRight = true;
            }
        }

        // Detener el bloque si se toca la pantalla
        if (Gdx.input.isTouched()) {
            placeBlock();
        }

        // Dibujar elementos
        game.batch.begin();
        for (Rectangle block : blocks) {
            game.batch.draw(blockTexture, block.x, block.y, block.width, block.height);
        }
        game.batch.draw(blockTexture, currentBlock.x, currentBlock.y, currentBlock.width, currentBlock.height);
        font.draw(game.batch, "Altura: " + score, 10, MainGame.HEIGHT - 10); // Mostrar puntuación
        game.batch.end();
    }

    private void placeBlock() {
        Rectangle topBlock = blocks.get(blocks.size() - 1);

        // Verificar alineación
        if (currentBlock.overlaps(topBlock)) {
            float overlapLeft = Math.max(topBlock.x, currentBlock.x);
            float overlapRight = Math.min(topBlock.x + topBlock.width, currentBlock.x + currentBlock.width);
            float overlapWidth = overlapRight - overlapLeft;

            // Crear nuevo bloque alineado
            currentBlock.x = overlapLeft;
            currentBlock.width = overlapWidth;
            currentBlock.y = topBlock.y + topBlock.height;
            blocks.add(currentBlock);

            score++;
        } else {
            // Fin del juego si no hay alineación
            game.setScreen(new GameOverScreen(game, score));
        }

        spawnNewBlock();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        blockTexture.dispose();
        font.dispose();
    }
}
