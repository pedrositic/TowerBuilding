package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Pantalla de fin de partida con fondo, puntuación centrada, título GAME OVER y dos botones.
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
    private final GlyphLayout layout;

    public GameOverScreen(MainGame game, int score) {
        this.game = game;
        this.score = score;

        background = game.backgroundTextures[0];
        titleTexture = new Texture("Game_over.png");
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(10);
        layout = new GlyphLayout();

        retryButton = new Texture("play_again_button.png");
        exitButton = new Texture("sortir_button.png");
        buttonScale = 1.5f;

        float centerX = MainGame.WIDTH / 2f;
        float titleY = MainGame.HEIGHT * 0.6f;

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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(background, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);

        // Puntuación centrada en la pantalla
        String scoreText = String.valueOf(score);
        layout.setText(scoreFont, scoreText);
        float scoreX = (MainGame.WIDTH - layout.width) / 2f;
        float scoreY = (MainGame.HEIGHT + layout.height) / 2f;
        scoreFont.draw(game.batch, layout, scoreX, scoreY);

        // Título GAME OVER
        float titleX = (MainGame.WIDTH - titleTexture.getWidth()) / 2f;
        float titleY = MainGame.HEIGHT * 0.6f;
        game.batch.draw(titleTexture, titleX, titleY);

        // Botones
        game.batch.draw(retryButton, retryBounds.x, retryBounds.y, retryBounds.width, retryBounds.height);
        game.batch.draw(exitButton, exitBounds.x, exitBounds.y, exitBounds.width, exitBounds.height);

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
