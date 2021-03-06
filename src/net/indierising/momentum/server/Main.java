package net.indierising.momentum.server;

import java.io.File;
import java.io.IOException;

import net.indierising.momentum.server.entities.ClassSystem;
import net.indierising.momentum.server.entities.EntityHandler;
import net.indierising.momentum.server.maps.Maps;
import net.indierising.momentum.server.network.Network;
import net.indierising.momentum.server.utils.TagReader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Main extends BasicGame{
	public static final int DIR_DOWN = 0, DIR_UP = 1, DIR_LEFT = 2, DIR_RIGHT = 3;
	
	public Main() {
		super("Momentum Server");
	}

	public void init(GameContainer gc) throws SlickException {
		// init maps
		Maps.initMaps();
		
		// load player classes
		ClassSystem.loadClasses();
		
		// TODO error log in data folder	
		TagReader config = null;
		config = new TagReader(new File("data/config.txt"));
		
		// get the port numbers from our config file
		int tcp_port = Integer.parseInt(config.findData("tcp_port"));
		int udp_port = Integer.parseInt(config.findData("udp_port"));
					
		// load ip address from config and add our parsed port numbers
		try {
			Globals.network = new Network(config.findData("ip"), tcp_port, udp_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// let us know where the server has started
		Globals.log("Server started on: " + Globals.network.ip + " TCP: " + Globals.network.tcpPort + " UDP: " + Globals.network.udpPort); 	
		
	}
	
	public void render(GameContainer gc, Graphics g) throws SlickException {}

	public void update(GameContainer gc, int delta) throws SlickException {
		EntityHandler.update(delta);
	}
	
	public static AppGameContainer app;
	public static void main(String args[]) throws SlickException{
		app = new AppGameContainer(new Main());
	    app.setShowFPS(false);
	    app.setTargetFrameRate(60);
	    app.setMaximumLogicUpdateInterval(10);
		app.setMaximumLogicUpdateInterval(60);
	    app.start();
	}
}
