package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Pantalla de fin de partida con fondo, puntuación grande, título GAME OVER y dos botones.
 */
public class GameOverScreen implements Screen {
    private final MainGame game;
    private final int score;
    private final Texture background;
    private final Texture titleTexture;
    private final BitmapFont scoreFont;
    private final Texture retryButton;
    private final Texture exitButton;
    private final Rectangle retryBounds;
    private final Rectangle exitBounds;
    private final float buttonScale;

    public GameOverScreen(MainGame game, int score) {
        this.game = game;
        this.score = score;
        // Cargar texturas
        background = game.backgroundTextures[0];
        titleTexture = new Texture("Game_over.png");
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(4); // tamaño grande para la puntuación
        retryButton = new Texture("play_again_button.png");
        exitButton = new Texture("sortir_button.png");
        // Escala de botones para que sean más grandes
        buttonScale = 2.5f;
        // Calcular posiciones
        float centerX = MainGame.WIDTH / 2f;
        // Puntuación en la parte superior
        // Título GAME OVER debajo de la puntuación
        float titleY = MainGame.HEIGHT * 0.6f;
        // Botones debajo del título
        float btnWidth = retryButton.getWidth() * buttonScale;
        float btnHeight = retryButton.getHeight() * buttonScale;
        float startX = centerX - btnWidth / 2f;
        float retryY = titleY - titleTexture.getHeight() - 80;
        float exitY = retryY - btnHeight - 40;
        retryBounds = new Rectangle(startX, retryY, btnWidth, btnHeight);
        exitBounds = new Rectangle(startX, exitY, btnWidth, btnHeight);
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        // Input
        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touch.y = MainGame.HEIGHT - touch.y;
            if (retryBounds.contains(touch)) {
                game.setScreen(new GameScreen(game));
                return;
            }
            if (exitBounds.contains(touch)) {
                Gdx.app.exit();
                return;
            }
        }
        // Dibujar
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        // Fondo completo
        game.batch.draw(background, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        // Puntuación centrada grande
        String scoreText = String.valueOf(score);
        float scoreWidth = scoreFont.getSpaceXadvance() * scoreText.length();
        scoreFont.draw(game.batch,
            scoreText,
            (MainGame.WIDTH - scoreWidth) / 2f,
            MainGame.HEIGHT - 100
        );
        // Título GAME OVER
        float titleX = (MainGame.WIDTH - titleTexture.getWidth()) / 2f;
        float titleY = MainGame.HEIGHT * 0.6f;
        game.batch.draw(titleTexture, titleX, titleY);
        // Botones escalados
        game.batch.draw(
            retryButton,
            retryBounds.x,
            retryBounds.y,
            retryBounds.width,
            retryBounds.height
        );
        game.batch.draw(
            exitButton,
            exitBounds.x,
            exitBounds.y,
            exitBounds.width,
            exitBounds.height
        );
        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        titleTexture.dispose();
        scoreFont.dispose();
        retryButton.dispose();
        exitButton.dispose();
    }
}
