package me.enz0z;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils;

import me.enz0z.instagram.WhoUnfollows;
import me.enz0z.utils.Prop;
import me.enz0z.utils.S;

public class Core {

	public static IGClient client;

	public static void main(String args[]) {
		new Prop();
		try {
			client = IGClient.builder().username(Prop.getString("Username")).password(Prop.getString("Password")).onTwoFactor((client, response) -> {
				return IGChallengeUtils.resolveTwoFactor(client, response, () -> {
					Scanner scanner = new Scanner(System.in);

					System.out.print("Please input code: ");
					return scanner.nextLine();
				});
			}).login();
			S.log("Currently connected as [" + client.getSelfProfile().toString() + "]");
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