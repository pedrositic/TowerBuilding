package paupedros.m8.uf3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** Clase principal del juego */
public class MainGame extends Game {
    public static final int WIDTH = 480;  // Ancho
    public static final int HEIGHT = 800; // Alto (formato vertical)

    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new MenuScreen(this)); // Comenzamos en la pantalla inicial
    }

    @Override
    public void dispose() {
        batch.dispose(); // Liberamos recursos
    }
}
