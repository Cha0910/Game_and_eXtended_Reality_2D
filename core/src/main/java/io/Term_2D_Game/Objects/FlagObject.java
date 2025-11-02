package io.Term_2D_Game.Objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class FlagObject extends BlockObject {
    /**
     * @param x X 좌표 (픽셀)
     * @param y Y 좌표 (픽셀)
     * @param box2dWorld Box2D 월드
     */
    public FlagObject(float x, float y, World box2dWorld) {
        super(x, y, "flag_red_a", box2dWorld, true, true);
        body.setUserData(this);
        for (Fixture f : body.getFixtureList()) {
            f.setUserData(this);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        syncSpriteToBody();
        super.draw(batch);
    }

    public void interact() {

    }
}

