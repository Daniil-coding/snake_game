package com.snake.game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.snake.game.MainSnakeGame.CAMHEIGHT;
import static com.snake.game.MainSnakeGame.CAMWIDTH;

public abstract class Window
{
    SpriteBatch batch;
    WindowManager wm;
    OrthographicCamera camera;

    Window (WindowManager windowManager) {
        wm = windowManager;
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMWIDTH, CAMHEIGHT);
    }

    public abstract void render(SpriteBatch batch);
    public abstract void update(float deltaTime);
    public abstract void catchPress();
    public abstract void dispose();
}
