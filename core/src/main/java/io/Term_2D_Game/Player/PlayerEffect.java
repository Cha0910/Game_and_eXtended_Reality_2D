package io.Term_2D_Game.Player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class PlayerEffect {
    protected Vector2 pos;
    protected float elapsed;
    protected float lifeTime;
    protected boolean finished;

    public PlayerEffect(Vector2 pos, float lifeTime) {
        this.pos = pos;
        this.lifeTime = lifeTime;
    }

    public void update(float delta) {
        elapsed += delta;
        if (elapsed >= lifeTime) finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public abstract void render(SpriteBatch batch);
}

