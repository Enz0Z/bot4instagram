package me.enz0z.instagram;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;

import me.enz0z.utils.DiscordWebhook;
import me.enz0z.utils.Prop;

public class M {

	public static List<Profile> getFollowers(UserAction response) {
		List<Profile> followers = new ArrayList<Profile>();
		
		for (FeedUsersResponse follower : response.followersFeed()) {
			for (Profile profile : follower.getUsers()) {
				followers.add(profile);
			}
		}
		return followers;
	}
	
	public static void sendFastWebhook(String content) {
		DiscordWebhook webhook = new DiscordWebhook(Prop.getString("Webhook"));
		
		webhook.setUsername("EVENT >> Error");
		webhook.setContent(content);
		try {
			webhook.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}