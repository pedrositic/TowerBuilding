package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Pantalla de fin de partida con fondo, título "Game Over", puntuación y dos botones.
 */
public class GameOverScreen implements Screen {
    private final MainGame game;
    private final int score;
    private final Texture background;
    private final Texture titleTexture;
    private final BitmapFont font;
    private final Texture retryButton;
    private final Texture exitButton;
    private final Rectangle retryBounds;
    private final Rectangle exitBounds;

    public GameOverScreen(MainGame game, int score) {
        this.game = game;
        this.score = score;

        // Cargar assets
        background = game.backgroundTextures[0];
        titleTexture = new Texture("Game_over.png");
        font = new BitmapFont();
        font.getData().setScale(2);
        retryButton = new Texture("play_again_button.png");
        exitButton = new Texture("sortir_button.png");

        // Posiciones
        float titleX = (MainGame.WIDTH - titleTexture.getWidth()) / 2f;
        float titleY = MainGame.HEIGHT - titleTexture.getHeight() - 100;

        float scoreY = titleY - titleTexture.getHeight() - 30;

        // Botones centrados uno debajo del otro
        float btnX = (MainGame.WIDTH - retryButton.getWidth()) / 2f;
        float retryY = scoreY - retryButton.getHeight() - 50;
        float exitY = retryY - exitButton.getHeight() - 30;

        retryBounds = new Rectangle(btnX, retryY, retryButton.getWidth(), retryButton.getHeight());
        exitBounds = new Rectangle(btnX, exitY, exitButton.getWidth(), exitButton.getHeight());
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gestión de input
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

        // Dibujar elementos
        game.batch.begin();
        // Fondo
        game.batch.draw(background, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);
        // Título "Game Over"
        float titleX = (MainGame.WIDTH - titleTexture.getWidth()) / 2f;
        float titleY = MainGame.HEIGHT - titleTexture.getHeight() - 100;
        game.batch.draw(titleTexture, titleX, titleY);
        // Puntuación
        String scoreText = "Puntuació: " + score;
        float scoreX = (MainGame.WIDTH - font.getRegion().getRegionWidth()) / 2f; // centrado aproximado
        game.batch.draw(font.getRegion(), 0, 0); // no se dibuja, se usa font.draw abajo
        font.draw(game.batch, scoreText,
            (MainGame.WIDTH - font.getSpaceXadvance() * scoreText.length()) / 2f,
            titleY -  titleTexture.getHeight() - 30);
        // Botones
        game.batch.draw(retryButton, retryBounds.x, retryBounds.y);
        game.batch.draw(exitButton, exitBounds.x, exitBounds.y);
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
        font.dispose();
        retryButton.dispose();
        exitButton.dispose();
    }
}
