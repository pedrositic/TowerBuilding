package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Pantalla de preferencias del juego.
 */
public class PreferencesScreen implements Screen {
    private final MainGame game;
    private BitmapFont font;
    private Texture backButtonTexture;
    private Rectangle backBounds;

    public PreferencesScreen(MainGame game) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(2);
        backButtonTexture = new Texture("sortir_button.png");

        float btnX = (MainGame.WIDTH - backButtonTexture.getWidth()) / 2f;
        float btnY = 50;
        backBounds = new Rectangle(btnX, btnY, backButtonTexture.getWidth(), backButtonTexture.getHeight());
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        // Texto de explicación
        String msg = "Aquest joc és suficientment senzill" +
            " com per no oferir opcions de configuració." +
        " Gràcies!";
        float textX = 100;
        float textY = MainGame.HEIGHT - 150;
        font.draw(game.batch, msg, textX, textY);
        // Botón de tornar
        game.batch.draw(backButtonTexture, backBounds.x, backBounds.y);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            Vector2 touch = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touch.y = MainGame.HEIGHT - touch.y;
            if (backBounds.contains(touch)) {
                game.setScreen(new MenuScreen(game));
            }
        }
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
        backButtonTexture.dispose();
    }
}
