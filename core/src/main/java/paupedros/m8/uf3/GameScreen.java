package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/** Pantalla de juego principal */
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
    public void show() {}

    private void spawnNewBlock() {
        Rectangle topBlock = blocks.get(blocks.size() - 1); // Obtener el último bloque
        // Calcular el tamaño reducido manteniendo la proporción
        Texture blockTexture;
        if (blocks.isEmpty()) { // Si es el primer bloque
            blockTexture = game.blockTextures[0]; // block_start.png
        } else {
            // Bloques posteriores: seleccionar aleatoriamente entre block_basic.png y block_balcony.png
            blockTexture = game.blockTextures[random.nextInt(2) + 1];
        }
        // Calcular el tamaño reducido manteniendo la proporción
        float originalWidth = blockTexture.getWidth();
        float originalHeight = blockTexture.getHeight();
        float targetWidth = MainGame.WIDTH / 5; // Un tercio del ancho de la pantalla
        float reducedHeight = calculateReducedSize(originalWidth, originalHeight, targetWidth);
        // Centrar el bloque horizontalmente
        float centerX = (MainGame.WIDTH - targetWidth) / 2;
        // Crear el nuevo bloque
        currentBlock = new Rectangle(centerX, topBlock.y + topBlock.height, targetWidth, reducedHeight);
        blockSpeed = 400; // Velocidad inicial ajustada para pantallas más grandes
        movingRight = true;
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

        // Alternar el fondo cuando se alcance una altura específica
        if (score >= 15 && score % 15 == 0) {
            // Alternar entre background_cel.png y background_cel_2.png
            game.backgroundTextures[1] = game.backgroundTextures[1].equals(game.backgroundTextures[2])
                ? game.backgroundTextures[1]
                : game.backgroundTextures[2];
        }

        // Dibujar elementos
        game.batch.begin();

        // Calcular desplazamiento vertical del fondo
        float backgroundYOffset = -(score * 50); // Desplazamiento proporcional a la altura de la torre

        // Dibujar el fondo según la puntuación
        if (score == 0) {
            // Primer fons: background_inicial.png
            game.batch.draw(game.backgroundTextures[0], 0, backgroundYOffset, MainGame.WIDTH, MainGame.HEIGHT);
        } else {
            // Altres fons: background_cel.png o background_cel_2.png
            game.batch.draw(game.backgroundTextures[1], 0, backgroundYOffset, MainGame.WIDTH, MainGame.HEIGHT);
        }

        // Dibuixar tots els blocs apilats
        for (Rectangle block : blocks) {
            game.batch.draw(game.blockTextures[0], block.x, block.y, block.width, block.height);
        }

        // Dibuixar el bloc mòbil actual
        game.batch.draw(game.blockTextures[0], currentBlock.x, currentBlock.y, currentBlock.width, currentBlock.height);

        // Mostrar la puntuació
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
            float blockDropDistance = 50; // Distancia que bajan los bloques
            for (Rectangle block : blocks) {
                block.y -= blockDropDistance;
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
}
