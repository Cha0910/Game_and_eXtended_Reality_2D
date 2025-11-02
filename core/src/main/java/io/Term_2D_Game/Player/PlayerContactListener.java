package io.Term_2D_Game.Player;

import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.*;

public class PlayerContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        checkContact(contact.getFixtureA(), contact.getFixtureB(), true);
        checkContact(contact.getFixtureB(), contact.getFixtureA(), true);
    }

    @Override
    public void endContact(Contact contact) {
        checkContact(contact.getFixtureA(), contact.getFixtureB(), false);
        checkContact(contact.getFixtureB(), contact.getFixtureA(), false);
    }

    private void checkContact(Fixture sensor, Fixture other, boolean begin) {
        if (sensor == null || other == null) return;
        if (sensor.getUserData() == null) return;

        // 바닥 감지
        if ("footSensor".equals(sensor.getUserData())) {
            Body body = sensor.getBody();
            Object userData = body.getUserData();

            if (userData instanceof Player) {
                Player player = (Player) userData;
                if (begin) player.onBeginContact(other);
                else player.onEndContact(other);
            }
        }

        // 왼쪽 감지
        if("leftSensor".equals(sensor.getUserData())){
            Body body = sensor.getBody();
            Object userData = body.getUserData();
            if (userData instanceof Player) {
                Player player = (Player) userData;
                if (begin) player.onBeginWallContact(other, true);
                else player.onEndWallContact(other, true);
            }
        }

        // 오른쪽 감지
        if("rightSensor".equals(sensor.getUserData())){
            Body body = sensor.getBody();
            Object userData = body.getUserData();
            if (userData instanceof Player) {
                Player player = (Player) userData;
                if (begin) player.onBeginWallContact(other, false);
                else player.onEndWallContact(other, false);
            }
        }

        if("attackSensor".equals(sensor.getUserData())){
            Body body = sensor.getBody();
            Object userData = body.getUserData();
            if (userData instanceof Player) {
                Player player = (Player) userData;
                if (begin) player.onBeginAttackContact(other);
            }
        }
    }

    @Override public void preSolve(Contact contact, Manifold oldManifold) {}
    @Override public void postSolve(Contact contact, ContactImpulse impulse) {}
}
