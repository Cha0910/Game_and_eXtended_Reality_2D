package io.Term_2D_Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.Term_2D_Game.Objects.BlockObject;
import io.Term_2D_Game.Objects.BoxObject;
import io.Term_2D_Game.Player.Player;
import io.Term_2D_Game.Player.PlayerContactListener;
import io.Term_2D_Game.Player.PlayerEffect;

public class GameWorld {

    public World box2dWorld; // Box2D 월드
    public List<BlockObject> blocks;

    public static final float PPM = 100f; // 픽셀 → 미터 변환 상수
    public final float WORLD_GRAVITY = -9.8f;
    private float accumulator = 0f;
    private final float TIME_STEP = 1/60f;
    public Player player;

    // 레벨 관련
    public Level level;
    public String currentLevel;
    private float fadeEffectTimer = 0f;
    private final float FADE_EFFECT_DURATION = 2.0f;
    private boolean isEnded;

    // 삭제 할 상자 큐
    private List<BoxObject> destroyQueue = new ArrayList<>();

    // 이펙트 관리
    private List<PlayerEffect> playerEffects = new ArrayList<>();

    private List<Icon> inputIcons = new ArrayList<>();

    public GameWorld() {
        box2dWorld = new World(new com.badlogic.gdx.math.Vector2(0, WORLD_GRAVITY), true);
        box2dWorld.setContactListener(new PlayerContactListener());
        level = new Level(this);
        isEnded = false;
        currentLevel = "level_1";
        blocks = new ArrayList<>();
        loadLevel(currentLevel);
        player = new Player(box2dWorld, level.getPlayerStartPosition(), this);
    }

    public void addBlock(BlockObject block) {
        blocks.add(block);
    }

    public void loadLevel(String levelFileName){
        if (levelFileName != null) {
            Array<Body> bodies = new Array<>();
            box2dWorld.getBodies(bodies);

            for (Body body : bodies) {
                box2dWorld.destroyBody(body);
            }

            blocks.clear();
            inputIcons.clear();
            currentLevel = levelFileName;

            level.loadLevel(currentLevel);

            if(player != null) {
                player.respawn(level.getPlayerStartPosition(), box2dWorld);
                player.setIsCleared(false);
            }
        }
    }

    public void update(float delta) {
        if(player.getIsCleared()){
            updateFadeEffectTimer(delta);
            clearedLevel();
            return;
        }

        if(isPlayerOutOfLevel()){
            player.setState(Player.State.DEAD);
            updateFadeEffectTimer(delta);
            if(fadeEffectTimer >= FADE_EFFECT_DURATION){
                fadeEffectTimer = 0f;
                restartCurrentLevel();
            }
        }

        // Box2D 물리 연산
        accumulator += delta;
        while(accumulator >= TIME_STEP) {
            box2dWorld.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }

        for (BoxObject b : destroyQueue) {
            b.destroy(box2dWorld, blocks); // Step 이후에 제거
        }

        // 블록 Sprite 위치 동기화
        for (BlockObject block : blocks) {
            block.syncSpriteToBody();
        }
        player.update(delta);

        // 이펙트 갱신
        for (Iterator<PlayerEffect> it = playerEffects.iterator(); it.hasNext();) {
            PlayerEffect e = it.next();
            e.update(delta);
            if (e.isFinished()) it.remove();
        }
    }

    public void draw(SpriteBatch batch) {
        for (Icon icon : inputIcons){
            icon.draw(batch);
        }
        for (BlockObject block : blocks) {
            block.draw(batch);
        }
        player.draw(batch);

        for (PlayerEffect e : playerEffects) {
            e.render(batch);
        }
    }

    public void dispose() {
        box2dWorld.dispose();
    }

    public Player getPlayer(){
        return player;
    }

    public void addDestroyBox(BoxObject box){
        destroyQueue.add(box);
    }

    public void restartCurrentLevel(){
        loadLevel(currentLevel);
    }

    public void clearedLevel(){
        if(fadeEffectTimer >= FADE_EFFECT_DURATION){
            fadeEffectTimer = 0f;
            if(level.getNextLevel().equals("level_0")){ // 게임 클리어
                isEnded = true;
                currentLevel = "level_1";
            }
            else{
                loadLevel(level.getNextLevel());
            }
        }
    }

    public void addEffect(PlayerEffect effect) {
        playerEffects.add(effect);
    }

    public void addInputIcons(Icon icon){
        inputIcons.add(icon);
    }

    public boolean isPlayerOutOfLevel() {
        Vector2 pos = player.getBody().getPosition();
        Vector2 size = level.getLevelSize();
        float margin = BlockObject.BlockHeight;

        boolean outOfX = pos.x < -margin / PPM || pos.x > (size.x + margin) / PPM;
        boolean outOfY = pos.y < -margin / PPM || pos.y > (size.y + margin) / PPM;

        return outOfX || outOfY;
    }

    public boolean getIsEnded(){
        return this.isEnded;
    }
    public void setIsEnded(boolean ended){
        this.isEnded = ended;
    }

    public void updateFadeEffectTimer(float delta){
        fadeEffectTimer += delta;
        player.addStateTime(delta);
    }

}
