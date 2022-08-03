package com.snake.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.snake.game.MainSnakeGame.CAMHEIGHT;
import static com.snake.game.MainSnakeGame.CAMWIDTH;

public class Stone
{
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture img;
    public int x, y;

    Stone(int x, int y)
    {
        this.x = x; this.y = y;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMWIDTH,  CAMHEIGHT);
        img = new Texture("stone.png");
    }

    public void draw()
    {
        batch.setProjectionMatrix(camera.combined);
        camera.update();
        batch.begin();
            batch.draw(img, x, y);
        batch.end();
    }

    public void changePosition()
    {
        x = random(0, 35) * 28;
        y = random(0, 35) * 28;
    }

    public void dispose()
    {
        img.dispose();
        batch.dispose();
    }
}
