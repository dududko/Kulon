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
import java.util.Random;

public class Kulon implements ApplicationListener {
    public static float width;
    public static float height;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture texture;
    private Sprite sprite;
    private ShapeRenderer renderer;
    private Model model;
    private static final  int PH_RADIUS = 100;
    private static final  float POINT_RADIUS = 7;




    @Override
    public void create() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(1, height / width);

        batch = new SpriteBatch();

        renderer = new ShapeRenderer();

        List<Point> points = new ArrayList<Point>();
        /*points.add(new Point(150, 100,0, 20));
        points.add(new Point(100, 100, 50, -50));
        points.add(new Point(70, 70, 0, -15));
        points.add(new Point(90, 70, 0, -24));
        points.add(new Point(70, 90, 100, 0));
        points.add(new Point(20, 90, 100, -50));
        points.add(new Point(100, 180, -100, -10));

        model = new Model(points,new Vector(100,100),100);*/
        /*for (int i = 0; i < 100; i++) {
            double r = 100 * Math.random();
            double angle = 2 * Math.PI * Math.random();
            double x = r*Math.cos(angle);
            double y = r*Math.sin(angle);
            points.add(new Point(x,y, 0, 20));
        }*/
        points.add(new Point(0, 100, 0, -10));
        points.add(new Point(0, -100, 0, 10));
        /*points.add(new Point(-30, -30, 0, -15));
        points.add(new Point(-10, -30, 0, -24));
        points.add(new Point(-30, -10, 100, 0));
        points.add(new Point(-80, -10, 100, -50));
        points.add(new Point(0, 80, -100, -10));*/


        Random random = new Random();
        for (int i = 0; i<30;++i){
            int x = random.nextInt(PH_RADIUS);
            int y = random.nextInt(PH_RADIUS);
            points.add(new Point(x,y,0,0));

        }

        model = new Model(points, new Vector(0, 0), PH_RADIUS);
    }

    @Override
    public void dispose() {
        batch.dispose();
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
        renderer.circle(width / 2, height / 2, PH_RADIUS + POINT_RADIUS);
        renderer.setColor(255, 0, 0, 0);
        for (Point point : model.getOldState()) {
            renderer.circle((float) point.getX() + width / 2, (float) point.getY() + height / 2, POINT_RADIUS);
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
