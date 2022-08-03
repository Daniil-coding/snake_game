package com.snake.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainSnakeGame extends ApplicationAdapter
{

	WindowManager wm;
	SpriteBatch batch;
	public static int CAMWIDTH = 900, CAMHEIGHT = 700;


	@Override
	public void create ()
	{
		wm = new WindowManager();
		batch = new SpriteBatch();
		wm.push(new StartWindow(wm));
	}

	@Override
	public void render()
	{
		Gdx.gl.glClearColor(100, 100, 100,0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		wm.render(batch);
		wm.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {
	    wm.clear();
	}
}
