package io.Term_2D_Game.Player;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

import io.Term_2D_Game.Assets;

public class DashEffect extends PlayerEffect {
    private Animation<TextureRegion> anim;
    private boolean lookingRight;

    public DashEffect(Vector2 pos, float lifeTime, boolean lookingRight) {
        super(pos, lifeTime);
        this.anim = Assets.getAnimation("dash_effect");
        this.lookingRight = lookingRight;
        anim.setFrameDuration(0.05f);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (finished) return;

        TextureRegion frame = anim.getKeyFrame(elapsed, false);

        // 방향에 따라 반전
        if (!lookingRight && !frame.isFlipX()) frame.flip(true, false);
        else if (lookingRight && frame.isFlipX()) frame.flip(true, false);

        float drawX = pos.x + (lookingRight ? -96.0f : 96.0f);
        float drawY = pos.y;

        float width = frame.getRegionWidth() * 1f;
        float height = frame.getRegionHeight() * 1f;

        batch.draw(frame, drawX - width / 2f, drawY - height / 1.6f, width, height);
    }
}

