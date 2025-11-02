package io.Term_2D_Game.Objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import io.Term_2D_Game.GameWorld;

public class BoxObject extends BlockObject {

    /**
     * @param x X 좌표 (픽셀)
     * @param y Y 좌표 (픽셀)
     * @param box2dWorld Box2D 월드
     */
    public BoxObject(float x, float y, World box2dWorld) {
        super(x, y, "block_planks", box2dWorld, true, false);
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

    public void destroy(World world, List<BlockObject> blocks) {
        if (body != null) {
            world.destroyBody(body); // Box2D Body 제거
            body = null;             // 참조 제거
        }
        blocks.remove(this);
    }
}

