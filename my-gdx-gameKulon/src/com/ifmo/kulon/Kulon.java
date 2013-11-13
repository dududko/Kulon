package com.ifmo.kulon;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ifmo.kulon.model.Model;
import com.ifmo.kulon.model.Point;
import com.ifmo.kulon.model.Vector;

import java.util.ArrayList;
import java.util.List;

public class Kulon implements ApplicationListener {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    private ShapeRenderer renderer;

    private Model model;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(1, h / w);

        batch = new SpriteBatch();

        renderer = new ShapeRenderer();

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(100, 100,0, 50));
       /* points.add(new Point(100, 100, 0.1, -0.1));
        points.add(new Point(70, 70, 0, -0.1));
        points.add(new Point(90, 70, 0, -0.1));
        points.add(new Point(70, 90, 10, 0));*/

        model = new Model(points,new Vector(100,100),100);
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        model.render(0.1);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(0, 0, 0, 0);
        renderer.circle(100, 100, 100);
        renderer.setColor(255, 0, 0, 0);
        for (Point point : model.getOldState()) {
            renderer.circle((float) point.getX(), (float) point.getY(), 1);
            //System.out.println(point.getX() + "  " + point.getY());
        }

        renderer.end();

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
