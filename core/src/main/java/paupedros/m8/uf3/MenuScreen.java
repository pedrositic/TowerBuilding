package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** Pantalla inicial del juego */
public class MenuScreen implements Screen {
    private MainGame game;
    private BitmapFont font;

    public MenuScreen(MainGame game) {
        this.game = game;
        this.font = new BitmapFont(); // Fuente predeterminada
        font.getData().setScale(2); // Escalamos el tamaño del texto
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        font.draw(game.batch, "Tower Building", 100, MainGame.HEIGHT - 100); // Título
        font.draw(game.batch, "Toca para empezar", 100, MainGame.HEIGHT - 200); // Instrucciones
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
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
        font.dispose(); // Liberamos la fuente
    }
}
