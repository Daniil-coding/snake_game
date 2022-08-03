package com.snake.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartWindow extends Window
{
    Texture background;

    StartWindow(WindowManager windowManager) {
        super(windowManager);
        background = new Texture("startgamebackground.jpg");
        camera.setToOrtho(false, 1000, 1000);
        camera.translate(background.getWidth() / 5, 0);
        camera.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        catchPress();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
            batch.draw(background, 0, 0);
        batch.end();
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void catchPress() {
        if (Gdx.input.isTouched())
            wm.push(new PlayWindow(wm));
    }

    @Override
    public void dispose() {
        background.dispose();
        batch.dispose();
    }
}
