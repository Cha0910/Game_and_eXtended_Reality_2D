package io.Term_2D_Game.Objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

public class StoneObject extends BlockObject {

    /**
     * @param x X 좌표 (픽셀)
     * @param y Y 좌표 (픽셀)
     * @param spriteName Assets Map 키
     * @param box2dWorld Box2D 월드
     */
    public StoneObject(float x, float y, String spriteName, World box2dWorld) {
        super(x, y, spriteName, box2dWorld, true, false);
        body.setUserData(this);
        for (Fixture f : body.getFixtureList()) {
            f.setUserData(this);
        }
    }

    public void draw(SpriteBatch batch) {
        syncSpriteToBody();
        super.draw(batch);
    }
}

