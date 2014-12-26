package net.indierising.momentum.entities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import net.indierising.momentum.Main;
import net.indierising.momentum.network.Network.PlayerPacket;
import net.indierising.momentum.utils.TagReader;

public class Handler {
	public static ArrayList<Player> players = new ArrayList<Player>();

	public static void update(int delta){
		for(int i = 0; i < players.size(); i++){
			players.get(i).update(delta);
		}
	}
	
	public static Player getPlayerByID(int connectionID){
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).getConnectionID() == connectionID){
				return players.get(i);
			}
		}
		// if we can't find them sorry.
		return null;
	}

	// check if we have the player saved, otherwise create a new file with their username
	public static void addPlayer(PlayerPacket packet) throws IOException{
		float x = 0, y = 0; String imageLocation = "";
		File userData = new File("data/entities/players/" + packet.username + ".dat");
		
		if(!userData.exists()){
			userData.createNewFile();
			FileWriter fw = new FileWriter(userData.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<name>" + packet.username + "\n");
			bw.write("<x>" + x + "\n");
			bw.write("<y>" + y);
			
			bw.close();
		}
	
		// load our information about the user here.
		TagReader reader;
		try {
			reader = new TagReader(new FileInputStream(userData));
			reader.read();
			x = Float.parseFloat(reader.findData("x"));
			y = Float.parseFloat(reader.findData("y"));
			imageLocation = reader.findData("render");
		} catch (FileNotFoundException e) {
			System.out.println("Data on player not found.");
		}
		players.add(new Player(packet.connectionID,packet.username,x,y,Main.DIRECTION_DOWN,imageLocation));
	}
	
	public static void logout(int connectionID) throws IOException{
		Player player = getPlayerByID(connectionID);
		File userData = new File("data/entities/players/" + player.getUsername() + ".dat");
				
		if(userData.exists()){
			userData.createNewFile();
			FileWriter fw = new FileWriter(userData.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("<name>" + player.getUsername() + "\n");
			bw.write("<x>" + player.getX() + "\n");
			bw.write("<y>" + player.getY() + "\n");
			bw.write("<render>" + player.getImageLocation());
			bw.close();
		}
		players.remove(player);
	}
}