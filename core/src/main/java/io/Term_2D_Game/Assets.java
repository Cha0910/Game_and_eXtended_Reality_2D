package io.Term_2D_Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class Assets {
    public static Map<String, Texture> textures = new HashMap<>();
    public static Map<String, Animation<TextureRegion>> animations = new HashMap<>();
    public static Texture whitePixel;
    public static void load(){
        load_objects();
        loadPlayerAnimations();
        loadOnWallBar();
        loadInputIcons();
    }
    public static void load_objects() {
        textures.put("pause", new Texture("pause.png"));

        // Objects
        textures.put("block_planks", new Texture("objects/block_planks.png"));
        textures.put("flag_red_a", new Texture("objects/flag_red_a.png"));
        textures.put("sign", new Texture("objects/sign.png"));

        // Sand
        textures.put("sand", new Texture("objects/terrain_sand_block.png"));
        textures.put("sand_bottom", new Texture("objects/terrain_sand_block_bottom.png"));
        textures.put("sand_bottom_left", new Texture("objects/terrain_sand_block_bottom_left.png"));
        textures.put("sand_bottom_right", new Texture("objects/terrain_sand_block_bottom_right.png"));
        textures.put("sand_center", new Texture("objects/terrain_sand_block_center.png"));
        textures.put("sand_left", new Texture("objects/terrain_sand_block_left.png"));
        textures.put("sand_right", new Texture("objects/terrain_sand_block_right.png"));
        textures.put("sand_top", new Texture("objects/terrain_sand_block_top.png"));
        textures.put("sand_top_left", new Texture("objects/terrain_sand_block_top_left.png"));
        textures.put("sand_top_right", new Texture("objects/terrain_sand_block_top_right.png"));
        textures.put("sand_horizontal_left", new Texture("objects/terrain_sand_horizontal_left.png"));
        textures.put("sand_horizontal_middle", new Texture("objects/terrain_sand_horizontal_middle.png"));
        textures.put("sand_horizontal_right", new Texture("objects/terrain_sand_horizontal_right.png"));

        // Stone
        textures.put("stone", new Texture("objects/terrain_stone_block.png"));
        textures.put("stone_bottom", new Texture("objects/terrain_stone_block_bottom.png"));
        textures.put("stone_bottom_left", new Texture("objects/terrain_stone_block_bottom_left.png"));
        textures.put("stone_bottom_right", new Texture("objects/terrain_stone_block_bottom_right.png"));
        textures.put("stone_center", new Texture("objects/terrain_stone_block_center.png"));
        textures.put("stone_left", new Texture("objects/terrain_stone_block_left.png"));
        textures.put("stone_right", new Texture("objects/terrain_stone_block_right.png"));
        textures.put("stone_top", new Texture("objects/terrain_stone_block_top.png"));
        textures.put("stone_top_left", new Texture("objects/terrain_stone_block_top_left.png"));
        textures.put("stone_top_right", new Texture("objects/terrain_stone_block_top_right.png"));
        textures.put("stone_horizontal_left", new Texture("objects/terrain_stone_horizontal_left.png"));
        textures.put("stone_horizontal_middle", new Texture("objects/terrain_stone_horizontal_middle.png"));
        textures.put("stone_horizontal_right", new Texture("objects/terrain_stone_horizontal_right.png"));
    }

    private static void loadPlayerAnimations() {
        FileHandle dir = Gdx.files.internal("player");

        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("[Assets] player 폴더가 없습니다.");
            return;
        }

        for (FileHandle file : dir.list("png")) {
            Texture sheet = new Texture(file);
            String name = file.nameWithoutExtension();

            int frameHeight = sheet.getHeight();
            int frameWidth = frameHeight;
            int frameCount = sheet.getWidth() / frameWidth;

            TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
            TextureRegion[] frames = new TextureRegion[frameCount];
            for (int i = 0; i < frameCount; i++) {
                frames[i] = tmp[0][i];
            }

            Animation<TextureRegion> anim = new Animation<>(0.2f, frames);
            anim.setPlayMode(Animation.PlayMode.LOOP);
            if(name.equals("player_walk")){
                anim.setFrameDuration(0.15f);
            }
            if(name.equals("player_attack")){
                anim.setFrameDuration(0.1f);
                anim.setPlayMode(Animation.PlayMode.NORMAL);
            }

            animations.put(name, anim);

            System.out.println("[Assets] Loaded animation: " + name + " (" + frameCount + " frames)");
        }
    }

    private static void loadOnWallBar(){
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

    private static void loadInputIcons(){
        FileHandle dir = Gdx.files.internal("inputs");
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("[Assets] inputs 폴더가 없습니다.");
            return;
        }

        for (FileHandle file : dir.list("png")) {
            String name = file.nameWithoutExtension();

            textures.put(name, new Texture(file));
            //System.out.println("[Assets] Loaded icon: " + name + " " + file);
        }
    }

    public static Texture get(String name) {
        return textures.get(name);
    }

    public static void dispose() {
        for (Texture tex : textures.values()) {
            tex.dispose();
        }
    }

    public static Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }
}


