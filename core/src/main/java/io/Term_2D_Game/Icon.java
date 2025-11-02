package io.Term_2D_Game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Icon {
    private float x, y;    // 월드 좌표
    private float width, height;
    private Texture texture;

    public Icon(float x, float y, Texture texture) {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = texture.getWidth() * 2.5f;
        this.height = texture.getHeight() * 2.5f;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }
}
