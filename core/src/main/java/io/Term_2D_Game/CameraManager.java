package io.Term_2D_Game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CameraManager {
    private final float CAMERA_WIDTH = 960;
    private final float CAMERA_HEIGHT = 540;
    private OrthographicCamera camera;
    private Viewport viewport;
    private GameWorld gameWorld;
    private SpriteBatch batch;
    private Vector2 cameraPosition;
    public CameraManager(GameWorld gameWorld, SpriteBatch batch){
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        viewport = new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);
        this.gameWorld = gameWorld;
        this.batch = batch;
        cameraPosition = new Vector2();
    }

    public void updateCamera(float delta){
        Vector2 targetPos = gameWorld.getPlayer().getBody().getPosition();
        Vector2 levelSize = gameWorld.level.getLevelSize();

        // Box2D 좌표 → 화면 좌표 변환
        float targetX = targetPos.x * GameWorld.PPM;
        float targetY = targetPos.y * GameWorld.PPM;

        if (camera.position.x == 0 && camera.position.y == 0) {
            camera.position.set(targetX, targetY, 0);
        }

        camera.position.x = targetX;
        camera.position.y = targetY;

        // 맵 경계 자르기
        cameraPosition.x = Math.max(CAMERA_WIDTH / 2f, Math.min(levelSize.x - CAMERA_WIDTH / 2f, camera.position.x));
        cameraPosition.y = Math.max(CAMERA_HEIGHT / 2f, Math.min(levelSize.y - CAMERA_HEIGHT / 2f, camera.position.y));
        camera.position.set(cameraPosition.x, cameraPosition.y, 0);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
    }

    public OrthographicCamera getCamera(){
        return this.camera;
    }
}
