package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

/**
 * Pantalla de juego principal
 */
public class GameScreen implements Screen {
    private MainGame game;
    private ArrayList<Rectangle> blocks; // Lista de bloques apilados
    private Rectangle currentBlock; // Bloque actual que cae
    private float blockSpeed; // Velocidad horizontal del bloque
    private boolean movingRight; // Dirección del movimiento
    private int score; // Altura de la torre
    private BitmapFont font;
    private boolean blockPlaced = false; // Bandera para evitar múltiples clics
    private Random random = new Random(); // Generador de números aleatorios

    public GameScreen(MainGame game) {
        this.game = game;
        blocks = new ArrayList<>();
        // Crear el primer bloque (block_start.png)
        float blockWidth = MainGame.WIDTH / 3;
        float centerX = (MainGame.WIDTH - blockWidth) / 2; // Centrar el bloque horizontalmente
        Rectangle firstBlock = new Rectangle(centerX, 100, blockWidth, 50); // Posición inicial
        blocks.add(firstBlock);
        spawnNewBlock(); // Generar el segundo bloque
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void show() {
    }

    private void spawnNewBlock() {
        Rectangle topBlock = blocks.get(blocks.size() - 1); // Obtener el último bloque

        // Determinar la textura del bloc segons si és el primer o no
        Texture blockTexture;
        if (blocks.isEmpty()) { // Si és el primer bloc
            blockTexture = game.blockTextures[0]; // block_start.png
        } else {
            // Seleccionar aleatòriament entre block_basic.png (índex 1) i block_balcony.png (índex 2)
            blockTexture = game.blockTextures[random.nextInt(2) + 1];
        }

        // Calcular el tamaño reducido manteniendo la proporción
        float originalWidth = blockTexture.getWidth();
        float originalHeight = blockTexture.getHeight();
        float targetWidth = MainGame.WIDTH / 5; // Un terç de l'amplada de la pantalla
        float reducedHeight = calculateReducedSize(originalWidth, originalHeight, targetWidth);

        // Centrar el bloc horitzontalment
        float centerX = (MainGame.WIDTH - targetWidth) / 2;

        // Crear el nou bloc
        currentBlock = new Rectangle(centerX, topBlock.y + topBlock.height, targetWidth, reducedHeight);
        blockSpeed = 400; // Velocitat inicial ajustada per pantalles més grans
        movingRight = true;

        System.out.println("Bloc seleccionat: " + (blocks.isEmpty() ? "block_start" : "Aleatori"));
    }

    private float calculateReducedSize(float originalWidth, float originalHeight, float targetWidth) {
        // Calcula el factor de escala para mantener la proporción
        float scale = targetWidth / originalWidth;
        return scale * originalHeight;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Movimiento del bloque actual
        if (movingRight) {
            currentBlock.x += blockSpeed * delta;
            if (currentBlock.x + currentBlock.width >= MainGame.WIDTH) {
                currentBlock.x = MainGame.WIDTH - currentBlock.width; // Ajustar posición exacta
                movingRight = false; // Cambiar dirección
            }
        } else {
            currentBlock.x -= blockSpeed * delta;
            if (currentBlock.x <= 0) {
                currentBlock.x = 0; // Ajustar posición exacta
                movingRight = true; // Cambiar dirección
            }
        }

        // Procesar el clic solo si no se ha colocado un bloque en este ciclo
        if (Gdx.input.isTouched() && !blockPlaced) {
            placeBlock();
            blockPlaced = true; // Marcar que se ha colocado un bloque
        }

        // Reiniciar la bandera cuando el jugador deja de tocar la pantalla
        if (!Gdx.input.isTouched()) {
            blockPlaced = false;
        }

        // Dibujar elementos
        game.batch.begin();

        // Dibujar el fondo según la puntuación
        if (score < 15) {
            // Primeros 15 puntos: background_inicial.png
            game.batch.draw(game.backgroundTextures[0], 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        } else if (score >= 15 && score < 50) {
            // Entre 15 y 49 puntos: background_cel.png
            game.batch.draw(game.backgroundTextures[1], 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        } else {
            // A partir de 50 puntos: background_cel_2.png
            game.batch.draw(game.backgroundTextures[2], 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        }

        // Dibujar todos los bloques apilados
        for (Rectangle block : blocks) {
            game.batch.draw(game.blockTextures[0], block.x, block.y, block.width, block.height);
        }

        // Dibujar el bloque móvil actual
        game.batch.draw(game.blockTextures[0], currentBlock.x, currentBlock.y, currentBlock.width, currentBlock.height);

        // Mostrar la puntuación
        font.draw(game.batch, "Altura: " + score, 10, MainGame.HEIGHT - 10);

        game.batch.end();
    }

    private void placeBlock() {
        Rectangle topBlock = blocks.get(blocks.size() - 1); // Obtener el último bloque

        // Verificar si hay superposición horizontal (ignorando la posición vertical)
        if (checkHorizontalOverlap(currentBlock, topBlock)) {
            // Crear un nuevo bloque en la posición actual
            currentBlock.y = topBlock.y + topBlock.height; // Colocar encima del último bloque
            blocks.add(new Rectangle(currentBlock.x, currentBlock.y, currentBlock.width, currentBlock.height));
            score++; // Incrementar la puntuación

            // Desplazar todos los bloques hacia abajo
            if (score >= 7) {
                float blockDropDistance = currentBlock.height; // Distancia que bajan los bloques
                for (Rectangle block : blocks) {
                    block.y -= blockDropDistance;
                }
            }

            // Aumentar la velocidad del bloque
            blockSpeed *= 1.25f;
        } else {
            // Fin del juego si no hay superposición
            game.setScreen(new GameOverScreen(game, score));
        }

        spawnNewBlock(); // Generar un nuevo bloque
    }

    private boolean checkHorizontalOverlap(Rectangle a, Rectangle b) {
        return a.x < b.x + b.width && a.x + a.width > b.x;
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
        font.dispose();
    }
}
