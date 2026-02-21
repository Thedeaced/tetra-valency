package com.td.game.map;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.utils.Disposable;
import com.td.game.utils.Constants;
import com.td.game.utils.ModelFactory;

public class GameMap implements Disposable {
    public enum MapType {
        ELEMENTAL_CASTLE,
        DESERT_OASIS
    }

    private Tile[][] tiles;
    private int width;
    private int height;
    private final MapType mapType;

    
    private Model grassModel;
    private Model pathModel;

    public GameMap(ModelFactory modelFactory) {
        this(modelFactory, MapType.ELEMENTAL_CASTLE);
    }

    public GameMap(ModelFactory modelFactory, MapType mapType) {
        this.width = Constants.MAP_WIDTH;
        this.height = Constants.MAP_HEIGHT;
        this.tiles = new Tile[width][height];
        this.mapType = mapType == null ? MapType.ELEMENTAL_CASTLE : mapType;

        TileType groundType = this.mapType == MapType.DESERT_OASIS ? TileType.DESERT_GROUND : TileType.GRASS;
        TileType pathType = this.mapType == MapType.DESERT_OASIS ? TileType.DESERT_PATH : TileType.PATH;
        this.grassModel = modelFactory.createTileModel(groundType);
        this.pathModel = modelFactory.createTileModel(pathType);

        initializeMap();
    }

    private void initializeMap() {
        TileType groundType = mapType == MapType.DESERT_OASIS ? TileType.DESERT_GROUND : TileType.GRASS;
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                tiles[x][z] = new Tile(x, z, groundType, grassModel);
            }
        }
        paintPathTiles(getWaypointsForMap());
    }

    public void render(ModelBatch modelBatch, Environment environment) {
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                if (tiles[x][z] != null) {
                    modelBatch.render(tiles[x][z].getModelInstance(), environment);
                }
            }
        }
    }

    public boolean isPathAt(float x, float z) {
        int gridX = Math.round(x / Constants.TILE_SIZE);
        int gridZ = Math.round(z / Constants.TILE_SIZE);
        if (gridX >= 0 && gridX < width && gridZ >= 0 && gridZ < height) {
            TileType type = tiles[gridX][gridZ].getType();
            return type == TileType.PATH || type == TileType.DESERT_PATH;
        }
        return false;
    }

    public int[][] getWaypointsForMap() {
        if (mapType == MapType.DESERT_OASIS) {
            return new int[][] {
                    { 0, 6 }, { 2, 6 }, { 2, 2 }, { 9, 2 }, { 9, 9 }, { 4, 9 }, { 4, 4 }, { 12, 4 }, { 12, 10 },
                    { 15, 10 }
            };
        }

        return new int[][] {
                { 0, 5 }, { 5, 5 }, { 5, 2 }, { 10, 2 }, { 10, 8 }, { 5, 8 }, { 5, 10 }, { 15, 10 }
        };
    }

    private void paintPathTiles(int[][] waypoints) {
        if (waypoints == null || waypoints.length == 0) {
            return;
        }
        for (int i = 0; i < waypoints.length - 1; i++) {
            int x = waypoints[i][0];
            int z = waypoints[i][1];
            int targetX = waypoints[i + 1][0];
            int targetZ = waypoints[i + 1][1];
            while (x != targetX || z != targetZ) {
                setPathTile(x, z);
                if (x < targetX) {
                    x++;
                } else if (x > targetX) {
                    x--;
                } else if (z < targetZ) {
                    z++;
                } else if (z > targetZ) {
                    z--;
                }
            }
        }
        int endX = waypoints[waypoints.length - 1][0];
        int endZ = waypoints[waypoints.length - 1][1];
        setPathTile(endX, endZ);
    }

    private void setPathTile(int x, int z) {
        if (x >= 0 && x < width && z >= 0 && z < height) {
            TileType pathType = mapType == MapType.DESERT_OASIS ? TileType.DESERT_PATH : TileType.PATH;
            tiles[x][z] = new Tile(x, z, pathType, pathModel);
        }
    }

    @Override
    public void dispose() {
        if (grassModel != null)
            grassModel.dispose();
        if (pathModel != null)
            pathModel.dispose();
    }
}

