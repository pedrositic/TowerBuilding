package paupedros.m8.uf3;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Block {
    public Rectangle rect;
    public Texture texture;

    public Block(Rectangle rect, Texture texture) {
        this.rect = rect;
        this.texture = texture;
    }
}
