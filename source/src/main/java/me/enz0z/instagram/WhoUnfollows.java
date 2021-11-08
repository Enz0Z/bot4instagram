package me.enz0z.instagram;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.github.instagram4j.instagram4j.models.user.Profile;

import me.enz0z.Core;
import me.enz0z.utils.DiscordWebhook;
import me.enz0z.utils.Prop;
import me.enz0z.utils.S;

public class WhoUnfollows {

	private static List<Profile> cached = new ArrayList<Profile>();

	public static void loop() {
		S.T.schedule(new TimerTask() {
			@Override
			public void run() {
				Core.client.getActions().users().findByUsername(Core.client.getSelfProfile().getUsername()).thenAccept(response -> {
					if (cached.size() == 0) {
						cached = Core.getFollowers(response);

						S.log("[" + S.format(S.currentSeconds()) + "] WhoUnfollows >> Cached " + cached.size() + " followers.");
					} else {
						List<Profile> followers = Core.getFollowers(response);

						for (Profile profile : cached) {
							Boolean still = false;

							for (Profile cache : followers) {
								if (profile.getPk().equals(cache.getPk())) still = true;
							}
							if (!still) {
								DiscordWebhook webhook = new DiscordWebhook(Prop.getString("Webhook"));

								webhook.setContent("`WhoUnfollows >>` https://instagram.com/" + profile.getUsername() + " unfollowed your account.");
								try {
									webhook.execute();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						S.log("[" + S.format(S.currentSeconds()) + "] WhoUnfollows >> Cached " + followers.size() + " followers.");
						cached = followers;
					}
					WhoUnfollows.loop();
				}).exceptionally(ex -> {
					DiscordWebhook webhook = new DiscordWebhook(Prop.getString("Webhook"));

					webhook.setContent("`WhoUnfollows >>` " + ex.getMessage());
					try {
						webhook.execute();
					} catch (Exception e) {
						e.printStackTrace();
					}
					return null;
				});
			}
		}, cached.size() == 0 ? 0 : (S.R.nextInt(7200000 - 3600000) + 3600000));
	}
}