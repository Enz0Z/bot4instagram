package me.enz0z;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils;

import me.enz0z.instagram.Prop;
import me.enz0z.instagram.WhoUnfollows;

public class Core {

	public static IGClient client;

	public static void main(String args[]) {
		new Prop();
		try {
			client = IGClient.builder().username(Prop.getString("Username")).password(Prop.getString("Password")).onTwoFactor((client, response) -> {
				return IGChallengeUtils.resolveTwoFactor(client, response, () -> {
					try (Scanner scanner = new Scanner(System.in)) {
						System.out.print("Please input code: ");
						return scanner.nextLine();
					}
				});
			}).login();
			System.out.println("[" + Clock.systemUTC().instant().toString() + "] Currently connected as [" + client.getSelfProfile().toString() + "]");
			WhoUnfollows.loop();
		} catch (IGLoginException e) {
			e.printStackTrace();
		}
	}

	public static List<Profile> getFollowers(UserAction response) {
		List<Profile> followers = new ArrayList<Profile>();

		for (FeedUsersResponse follower : response.followersFeed()) {
			for (Profile profile : follower.getUsers()) {
				followers.add(profile);
			}
		}
		return followers;
	}
}