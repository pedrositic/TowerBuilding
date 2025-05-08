package paupedros.m8.uf3;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

// En MainGame.java
public class MainGame extends Game {
    public static int WIDTH;  // Ancho de la pantalla
    public static int HEIGHT; // Alto de la pantalla

    public SpriteBatch batch;
    public Texture[] blockTextures; // Array para almacenar las texturas de los bloques
    public Texture[] backgroundTextures; // Array para almacenar las texturas de los fondos

    @Override
    public void create() {
        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();

        batch = new SpriteBatch();

        // Cargar texturas de los bloques
        blockTextures = new Texture[3];
        blockTextures[0] = new Texture("block_start.png"); // Primer bloque
        blockTextures[1] = new Texture("block_basic.png");
        blockTextures[2] = new Texture("block_balcony.png");

        // Cargar texturas de los fondos
        backgroundTextures = new Texture[3];
        backgroundTextures[0] = new Texture("background_inicial.png"); // Fondo inicial
        backgroundTextures[1] = new Texture("background_cel.png");    // Fondo 1
        backgroundTextures[2] = new Texture("background_cel_2.png");   // Fondo 2

        setScreen(new MenuScreen(this)); // Comenzamos en la pantalla inicial
    }

    @Override
    public void dispose() {
        batch.dispose(); // Liberamos recursos
        for (Texture texture : blockTextures) {
            if (texture != null) {
                texture.dispose();
            }
        }
        for (Texture texture : backgroundTextures) {
            if (texture != null) {
                texture.dispose();
            }
        }
    }

    public void setScreen(Screen screen) {
        if (this.screen != null) this.screen.hide();
        this.screen = screen;
        if (this.screen != null) {
            System.out.println("Cambiando a la pantalla: " + screen.getClass().getSimpleName());
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}
