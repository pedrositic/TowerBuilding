package paupedros.m8.uf3;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/** Pantalla de fin de partida */
public class GameOverScreen implements Screen {
    private MainGame game;
    private int score;
    private BitmapFont font;
    private float timeSinceShown = 0f;

    public GameOverScreen(MainGame game, int score) {
        this.game = game;
        this.score = score;
        this.font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        timeSinceShown += delta;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        font.draw(game.batch, "Fin del Juego", 100, MainGame.HEIGHT - 100);
        font.draw(game.batch, "Altura: " + score, 100, MainGame.HEIGHT - 200);
        font.draw(game.batch, "Toca para reiniciar", 100, MainGame.HEIGHT - 300);
        game.batch.end();

        // Esperar 0.5 segundos antes de permitir el reinicio
        if (timeSinceShown > 0.5f && Gdx.input.isTouched()) {
            System.out.println("Reiniciando juego desde GameOverScreen");
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
        font.dispose();
    }
}
