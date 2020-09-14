package me.enz0z.instagram;

import java.util.ArrayList;
import java.util.List;

import com.github.instagram4j.instagram4j.actions.users.UserAction;
import com.github.instagram4j.instagram4j.models.user.Profile;
import com.github.instagram4j.instagram4j.responses.feed.FeedUsersResponse;

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
}