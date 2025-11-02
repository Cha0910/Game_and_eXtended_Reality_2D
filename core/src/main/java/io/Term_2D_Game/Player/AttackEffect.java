package io.Term_2D_Game.Player;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;

import io.Term_2D_Game.Assets;

public class AttackEffect extends PlayerEffect {
    private Animation<TextureRegion> anim;
    private boolean lookingRight;

    public AttackEffect(Vector2 pos, float lifeTime, boolean lookingRight) {
        super(pos, lifeTime);
        this.anim = Assets.getAnimation("attack_effect");
        this.lookingRight = lookingRight;
        anim.setFrameDuration(0.1f);
    }

    @Override
    public void render(SpriteBatch batch) {
        if (finished) return;

        TextureRegion frame = anim.getKeyFrame(elapsed, false);

        // 방향에 따라 반전
        if (!lookingRight && !frame.isFlipX()) frame.flip(true, false);
        else if (lookingRight && frame.isFlipX()) frame.flip(true, false);

        float drawX = pos.x + (lookingRight ? 40.0f : -40.0f);
        float drawY = pos.y;

        float width = frame.getRegionWidth() * 0.2f;
        float height = frame.getRegionHeight() * 0.4f;

        batch.draw(frame, drawX - width / 2f, drawY - height / 2f, width, height);
    }
}

