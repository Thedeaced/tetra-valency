package com.td.game.map;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.td.game.utils.Constants;

public class Tile {

    private final TileType type;
    private final ModelInstance modelInstance;

    public Tile(int gridX, int gridZ, TileType type, Model model) {
        this.type = type;

        this.modelInstance = new ModelInstance(model);

        float worldX = gridX * Constants.TILE_SIZE;
        float worldZ = gridZ * Constants.TILE_SIZE;
        modelInstance.transform.setToTranslation(worldX, 0, worldZ);
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    public TileType getType() {
        return type;
    }
}

