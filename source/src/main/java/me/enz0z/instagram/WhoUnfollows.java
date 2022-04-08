package me.enz0z.instagram;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.github.instagram4j.instagram4j.models.user.Profile;

import me.enz0z.Core;

public class WhoUnfollows {

	private static List<Profile> cached = new ArrayList<Profile>();

	public static void loop() {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				Core.client.getActions().users().findByUsername(Core.client.getSelfProfile().getUsername()).whenCompleteAsync((response, exception) -> {
					if (exception != null) {
						System.out.println(exception);
						try {
							DiscordWebhook webhook = new DiscordWebhook(Prop.getString("Webhook"));

							webhook.setContent("```\n" + ExceptionUtils.getStackTrace(exception) + "\n```");
							webhook.execute();
						} catch (Exception e) {}
					} else {
						List<Profile> followers = Core.getFollowers(response);

						if (cached.size() == 0) {
							cached = followers;
						}
						for (Profile profile : cached) {
							boolean contains = false;

							for (Profile cache : followers) {
								if (profile.getPk().equals(cache.getPk())) {
									contains = true;
								}
							}
							if (!contains) {
								System.out.println("[" + Clock.systemUTC().instant().toString() + "] " + profile.getUsername() + " unfollowed your account.");
								try {
									DiscordWebhook webhook = new DiscordWebhook(Prop.getString("Webhook"));

									webhook.setContent("https://instagram.com/" + profile.getUsername() + " unfollowed your account.");
									webhook.execute();
								} catch (Exception e) {}
							}
						}
						System.out.println("[" + Clock.systemUTC().instant().toString() + "] Cached " + followers.size() + " followers.");
						cached = followers;
					}
					WhoUnfollows.loop();
				});
			}
		}, cached.size() == 0 ? 0 : (new Random().nextInt(7200000 - 3600000) + 3600000));
	}
}