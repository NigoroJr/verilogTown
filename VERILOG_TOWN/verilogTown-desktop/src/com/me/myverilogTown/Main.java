package com.me.myverilogTown;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "verilogTown";
		cfg.useGL20 = false;
		cfg.width = 1280;
		cfg.height = 1280;
		
		new LwjglApplication(new verilogTown(), cfg);
	}
}
