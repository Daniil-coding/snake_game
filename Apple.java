package com.snake.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.snake.game.MainSnakeGame.CAMHEIGHT;
import static com.snake.game.MainSnakeGame.CAMWIDTH;

public class Apple
{
    private SpriteBatch batch;
    private Texture img;
    public int x, y;
    OrthographicCamera camera;

    Apple(int x, int y)
    {
        batch = new SpriteBatch();
        img = new Texture("apple.png");
        this.x = x * 28; this.y = y * 28;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMWIDTH,  CAMHEIGHT);
    }

    public void changePosition()
    {
        x = random(5, 20) * 28;
        y = random(5, 20) * 28;
    }

    public void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        batch.begin();
            batch.draw(img, x, y);
        batch.end();
    }

    public void dispose()
    {
        img.dispose();
        batch.dispose();
    }
}