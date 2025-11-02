package io.Term_2D_Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import io.Term_2D_Game.Objects.*;

public class Level {

    private GameWorld gameWorld;
    private Vector2 playerStartPosition;
    private String nextLevel;
    private Vector2 levelSize;

    public Level(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.playerStartPosition = new Vector2();
        this.levelSize = new Vector2();
    }

    public void loadLevel(String filePath) {
        Json json = new Json();
        JsonValue root = json.fromJson(null, Gdx.files.internal("levels/" + filePath + ".json"));

        if (root.has("playerStart")) {
            JsonValue playerStart = root.get("playerStart");
            playerStartPosition.x = playerStart.getFloat("x");
            playerStartPosition.y = playerStart.getFloat("y");
        }

        if (root.has("levelSize")) {
            JsonValue levelSize = root.get("levelSize");
            this.levelSize.x = levelSize.getFloat("x");
            this.levelSize.y = levelSize.getFloat("y");
        }

        JsonValue blocks = root.get("blocks");
        for (JsonValue blockData : blocks) {
            float x = blockData.getFloat("x") * BlockObject.BlockWidth;
            float y = blockData.getFloat("y") * BlockObject.BlockHeight;
            float length = blockData.getFloat("length");
            String type = blockData.getString("type");
            String spriteName = blockData.getString("sprite");

            for (int i=0; i<length; i++){
                BlockObject block = null;
                switch (type.toLowerCase()) {
                    case "sand":
                        block = new SandObject(x, y, spriteName, gameWorld.box2dWorld);
                        break;
                    case "stone":
                        block = new StoneObject(x , y, spriteName, gameWorld.box2dWorld);
                        break;
                    case "box":
                        block = new BoxObject(x, y, gameWorld.box2dWorld);
                        break;
                    case "flag":
                        block = new FlagObject(x, y, gameWorld.box2dWorld);
                        nextLevel = blockData.has("nextLevel") ? blockData.getString("nextLevel") : null;
                        break;
                    case "sign":
                        String message = blockData.has("message") ? blockData.getString("message") : null;
                        block = new SignObject(x, y, gameWorld.box2dWorld, message);
                        break;

                }

                if (block != null) {
                    gameWorld.addBlock(block);
                    x += BlockObject.BlockWidth;
                }
            }
        }

        JsonValue icons = root.get("icons");
        if(icons != null){
            for (JsonValue iconData : icons) {
                float x = iconData.getFloat("x");
                float y = iconData.getFloat("y");
                String spriteName = iconData.getString("sprite");
                Icon inputIcon = new Icon(x, y, Assets.get(spriteName));
                gameWorld.addInputIcons(inputIcon);
            }
        }
    }

    public Vector2 getPlayerStartPosition(){
        return playerStartPosition;
    }

    public String getNextLevel(){
        return nextLevel;
    }

    public Vector2 getLevelSize(){
        return this.levelSize;
    }
}


