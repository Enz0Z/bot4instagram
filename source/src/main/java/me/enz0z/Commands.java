package me.enz0z;

import java.util.List;
import java.util.Scanner;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.user.Profile;

import me.enz0z.utils.Utils;

public class Commands {

	public static void loop() {
		Scanner scanner = new Scanner(System.in);
		IGClient client = Core.getClient();
		
	    System.out.print("shell: ");
		if (scanner.nextLine().equals("followers")) {
			client.getActions().users().findByUsername(client.getSelfProfile().getUsername()).thenAccept(response -> {
				response.followersFeed().forEach(follower -> {
					List<Profile> followers = follower.getUsers();
					
					for (int i = 0; i < followers.size(); i++) {
						Utils.print((i + 1) + ". " + followers.get(i).getUsername() + " follows " + client.getSelfProfile().getUsername());
					}
					Commands.loop();
				});
			});
		} else if (scanner.nextLine().equals("follows")) {
			client.getActions().users().findByUsername(client.getSelfProfile().getUsername()).thenAccept(response -> {
				response.followingFeed().forEach(follow -> {
					List<Profile> follows = follow.getUsers();
					
					for (int i = 0; i < follows.size(); i++) {
						Utils.print((i + 1) + ". " + client.getSelfProfile().getUsername() + " follows " + follows.get(i).getUsername());
					}
					Commands.loop();
				});
			});
		} else {
			Commands.loop();
		}
	}
}