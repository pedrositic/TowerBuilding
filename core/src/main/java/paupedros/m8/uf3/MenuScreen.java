package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Pantalla de menú inicial con fondo, título y dos botones más grandes y centrados.
 */
public class MenuScreen implements Screen {
    private final MainGame game;
    private final Texture background;
    private final Texture titleTexture;
    private final Texture startButtonTexture;
    private final Texture exitButtonTexture;

    private final Rectangle startBounds;
    private final Rectangle exitBounds;
    private final float buttonScale;

    public MenuScreen(MainGame game) {
        this.game = game;

        // Cargar texturas
        background = new Texture("Background_pantalla_inici.png");
        titleTexture = new Texture("Game_Title.png");
        startButtonTexture = new Texture("Comencar_button.png");
        exitButtonTexture = new Texture("sortir_button.png");

        buttonScale = 2.5f;

        // Calcular posición del título
        float titleX = (MainGame.WIDTH - titleTexture.getWidth()) / 2f;
        float titleY = MainGame.HEIGHT - titleTexture.getHeight() - 100;

        float btnWidth = startButtonTexture.getWidth() * buttonScale;
        float btnHeight = startButtonTexture.getHeight() * buttonScale;

        float btnX = (MainGame.WIDTH - btnWidth) / 2f;

        // Ajustar posiciones con buena separación
        float spacing = btnHeight + 50;
        float firstBtnY = titleY - spacing - 50;

        startBounds = new Rectangle(btnX, firstBtnY, btnWidth, btnHeight);
        exitBounds = new Rectangle(btnX, firstBtnY - spacing, btnWidth, btnHeight);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touch.y = MainGame.HEIGHT - touch.y;

            if (startBounds.contains(touch)) {
                game.setScreen(new GameScreen(game));
                return;
            }
            if (exitBounds.contains(touch)) {
                Gdx.app.exit();
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        game.batch.draw(background, 0, 0, MainGame.WIDTH, MainGame.HEIGHT);

        float titleX = (MainGame.WIDTH - titleTexture.getWidth()) / 2f;
        float titleY = MainGame.HEIGHT - titleTexture.getHeight() - 100;
        game.batch.draw(titleTexture, titleX, titleY);

        game.batch.draw(
            startButtonTexture,
            startBounds.x,
            startBounds.y,
            startBounds.width,
            startBounds.height
        );
        game.batch.draw(
            exitButtonTexture,
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
        startButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
