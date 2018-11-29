package com.ychstudio.builders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.ychstudio.builders.OrthoCamController;

public class MapBuilder{

    private TiledMap map;
    private TiledMapRenderer renderer;
    private OrthographicCamera camera;
    private OrthoCamController cameraController;
    private AssetManager assetManager;
    private Texture tiles;
    private Texture texture;
    private BitmapFont font;
    private SpriteBatch batch;

    public TiledMap create () {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, (w / h) * 470, 470);
        camera.update();

        cameraController = new OrthoCamController(camera);
        Gdx.input.setInputProcessor(cameraController);

        font = new BitmapFont();
        batch = new SpriteBatch();

        {
            tiles = new Texture(Gdx.files.internal("core/assets/map/tiles.png"));
            TextureRegion[][] splitTiles = TextureRegion.split(tiles, 32, 32);
            map = new TiledMap();
            MapLayers layers = map.getLayers();
            for (int l = 0; l < 7; l++) {
                TiledMapTileLayer layer = new TiledMapTileLayer(150, 100, 32, 32);
                for (int x = 0; x < 150; x++) {
                    for (int y = 0; y < 100; y++) {
                        int ty = (int)(Math.random() * splitTiles.length);
                        int tx = (int)(Math.random() * splitTiles[ty].length);
                        Cell cell = new Cell();
                        cell.setTile(new StaticTiledMapTile(splitTiles[ty][tx]));
                        layer.setCell(x, y, cell);
                    }
                }
                layers.add(layer);
            }
        }

        //renderer = new OrthogonalTiledMapRenderer(map);
        return map;

    }

    public void render () {
        Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        renderer.setView(camera);
        renderer.render();
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        batch.end();
    }
}