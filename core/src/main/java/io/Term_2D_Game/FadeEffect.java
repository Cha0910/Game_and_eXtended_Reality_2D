package io.Term_2D_Game;

import static io.Term_2D_Game.GameWorld.PPM;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class FadeEffect {
    private float radius;
    private float maxRadius;
    private float minRadius;
    private float alpha;
    private float speed;
    private boolean fadingOut;
    private boolean fadingIn;
    private boolean pauseOverlay;
    private boolean active;
    private boolean speedFlag;
    private float fadeOutTimer = 0f;
    private final float FADEOUT_DURATION = 0.8f;

    public FadeEffect(float screenWidth, float screenHeight) {
        this.maxRadius = Math.max(screenWidth, screenHeight) * 1.2f;
        this.minRadius = 0f;
        this.radius = maxRadius;
        this.alpha = 1f;
        this.speed = 2000f; // 페이드 속도
        this.active = false;
        this.pauseOverlay = false;
        this.speedFlag = false;
    }

    public void startFadeOut() {
        fadingOut = true;
        fadingIn = false;
        pauseOverlay = false;
        active = true;
        radius = maxRadius;
    }

    public void startFadeIn() {
        fadingOut = false;
        fadingIn = true;
        pauseOverlay = false;
        active = true;
        alpha = 1f;
    }

    public void setPauseOverlay(boolean enabled) {
        pauseOverlay = enabled;
    }

    public void update(float delta) {
        if (!active) return;

        if (fadingOut) {
            radius -= speed * delta;
            if(speedFlag){
                fadeOutTimer += delta;
                if(fadeOutTimer > FADEOUT_DURATION){
                    speed = 3000f;
                    fadeOutTimer = 0f;
                }
            }
            if(radius <= 96f && !speedFlag){
                speed = 0f;
                speedFlag = true;
            }
            if (radius <= minRadius) {
                radius = minRadius;
                speed = 2000f;
                active = false;
                speedFlag = false;
            }
        }
        else if (fadingIn) {
            alpha -= delta;
            if (alpha <= 0f) {
                alpha = 0f;
                active = false;
            }
        }
    }

    public void render(ShapeRenderer shapeRenderer, Vector2 playerPos, OrthographicCamera camera) {
        shapeRenderer.setProjectionMatrix(camera.combined);

        if (fadingOut) {
            Gdx.gl.glEnable(GL20.GL_STENCIL_TEST);

            Gdx.gl.glStencilMask(0xFF);
            Gdx.gl.glClearStencil(0);
            Gdx.gl.glClear(GL20.GL_STENCIL_BUFFER_BIT);

            Gdx.gl.glStencilFunc(GL20.GL_ALWAYS, 1, 0xFF); // 모든 픽셀 통과
            Gdx.gl.glStencilOp(GL20.GL_REPLACE, GL20.GL_REPLACE, GL20.GL_REPLACE); // 스텐실 값 1로 설정

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            Gdx.gl.glColorMask(false, false, false, false);
            shapeRenderer.setColor(0f, 0f, 0f, 0f);
            shapeRenderer.circle(playerPos.x * PPM, playerPos.y * PPM, radius); // 원형 마스크
            shapeRenderer.end();

            Gdx.gl.glStencilMask(0xFF);
            Gdx.gl.glStencilFunc(GL20.GL_NOTEQUAL, 1, 0xFF); // 스텐실 값이 1이 아닌 영역만 그리기
            Gdx.gl.glStencilOp(GL20.GL_KEEP, GL20.GL_KEEP, GL20.GL_KEEP); // 스텐실 값 유지

            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            Gdx.gl.glColorMask(true, true, true, true);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 1f); // alpha = 0~1, 어둡게
            shapeRenderer.rect(
                camera.position.x - Gdx.graphics.getWidth() / 2f,
                camera.position.y - Gdx.graphics.getHeight() / 2f,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
            Gdx.gl.glDisable(GL20.GL_STENCIL_TEST);

        } else if (fadingIn) {
            // 전체 페이드 인
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, alpha);
            shapeRenderer.rect(
                camera.position.x - Gdx.graphics.getWidth() / 2f,
                camera.position.y - Gdx.graphics.getHeight() / 2f,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        if (pauseOverlay) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0f, 0f, 0f, 0.5f); // 원하는 투명도
            shapeRenderer.rect(
                camera.position.x - Gdx.graphics.getWidth() / 2f,
                camera.position.y - Gdx.graphics.getHeight() / 2f,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }
}
