package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {
    private MainGame game;
    private ArrayList<Block> blocks; // Lista de bloques con su textura
    private Rectangle currentBlock;
    private Texture currentBlockTexture;
    private float blockSpeed;
    private boolean movingRight;
    private int score;
    private BitmapFont font;
    private boolean blockPlaced = false;
    private Random random = new Random();
    private boolean gameOver = false;
    private boolean isFirstBlock = true;

    public GameScreen(MainGame game) {
        this.game = game;
        blocks = new ArrayList<>();
        spawnNewBlock(); // El primer bloque es móvil
        font = new BitmapFont();
        font.getData().setScale(2);
        gameOver = false;
        this.blockSpeed = 400;
    }

    private void spawnNewBlock() {
        Texture blockTexture;
        float initialY;

        if (isFirstBlock) {
            blockTexture = game.blockTextures[0]; // block_start.png
            initialY = 100;
        } else {
            blockTexture = game.blockTextures[random.nextInt(3) + 1]; // basic o balcony
            Rectangle topBlock = blocks.get(blocks.size() - 1).rect;
            initialY = topBlock.y + topBlock.height;
        }

        float originalWidth = blockTexture.getWidth();
        float originalHeight = blockTexture.getHeight();
        float targetWidth = MainGame.WIDTH / 5;
        float reducedHeight = calculateReducedSize(originalWidth, originalHeight, targetWidth);
        float centerX = (MainGame.WIDTH - targetWidth) / 2;

        currentBlock = new Rectangle(centerX, initialY, targetWidth, reducedHeight);
        currentBlockTexture = blockTexture;
        movingRight = true;
        isFirstBlock = false;
    }

    private float calculateReducedSize(float originalWidth, float originalHeight, float targetWidth) {
        float scale = targetWidth / originalWidth;
        return scale * originalHeight;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if (gameOver) return;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Movimiento del bloque
        if (movingRight) {
            currentBlock.x += blockSpeed * delta;
            if (currentBlock.x + currentBlock.width >= MainGame.WIDTH) {
                currentBlock.x = MainGame.WIDTH - currentBlock.width;
                movingRight = false;
            }
        } else {
            currentBlock.x -= blockSpeed * delta;
            if (currentBlock.x <= 0) {
                currentBlock.x = 0;
                movingRight = true;
            }
        }

        if (Gdx.input.isTouched() && !blockPlaced) {
            placeBlock();
            blockPlaced = true;
        }

        if (!Gdx.input.isTouched()) {
            blockPlaced = false;
        }

        game.batch.begin();

        // Fondo
        if (score < 15) {
            game.batch.draw(game.backgroundTextures[0], 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        } else if (score < 50) {
            game.batch.draw(game.backgroundTextures[1], 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        } else {
            game.batch.draw(game.backgroundTextures[2], 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        }

        // Bloques apilados
        for (Block block : blocks) {
            game.batch.draw(block.texture, block.rect.x, block.rect.y, block.rect.width, block.rect.height);
        }

        // Bloque móvil
        game.batch.draw(currentBlockTexture, currentBlock.x, currentBlock.y, currentBlock.width, currentBlock.height);

        // Altura
        font.draw(game.batch, "Altura: " + score, 10, MainGame.HEIGHT - 10);

        game.batch.end();
    }

    private void placeBlock() {
        if (blocks.isEmpty()) {
            blocks.add(new Block(new Rectangle(currentBlock), currentBlockTexture));
            score++;
            spawnNewBlock();
            return;
        }

        Rectangle topBlock = blocks.get(blocks.size() - 1).rect;

        if (checkHorizontalOverlap(currentBlock, topBlock)) {
            currentBlock.y = topBlock.y + topBlock.height;
            blocks.add(new Block(new Rectangle(currentBlock), currentBlockTexture));
            score++;

            if (score >= 7) {
                float blockDropDistance = currentBlock.height;
                for (Block block : blocks) {
                    block.rect.y -= blockDropDistance;
                }
            }

            blockSpeed *= 1.03f;
            spawnNewBlock();
        } else {
            System.out.println("Jugador ha perdido. Cambiando a GameOverScreen...");
            gameOver = true;
            game.setScreen(new GameOverScreen(game, score));
        }
    }

    private boolean checkHorizontalOverlap(Rectangle a, Rectangle b) {
        return a.x < b.x + b.width && a.x + a.width > b.x;
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
        font.dispose();
    }

    // Clase interna para representar bloques con su textura
    private static class Block {
        public Rectangle rect;
        public Texture texture;

        public Block(Rectangle rect, Texture texture) {
            this.rect = rect;
            this.texture = texture;
        }
    }
}
