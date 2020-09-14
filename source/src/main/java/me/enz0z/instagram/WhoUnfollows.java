package me.enz0z.instagram;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.models.user.Profile;

import me.enz0z.Core;
import me.enz0z.utils.DiscordWebhook;
import me.enz0z.utils.Prop;
import me.enz0z.utils.S;

public class WhoUnfollows {
	
	private static Boolean firstRun = true;
	private static List<Profile> cacheFollowers = new ArrayList<Profile>();
	
	public static void loop() {
		IGClient client = Core.getClient();
		
		S.T.schedule(new TimerTask() {
			@Override
			public void run() {
				client.getActions().users().findByUsername(client.getSelfProfile().getUsername()).thenAccept(response -> {
					if (firstRun) {
						firstRun = false;
						cacheFollowers = M.getFollowers(response);
						
						S.log("UNFOLLOWERS >> Cached " + cacheFollowers.size() + " followers.");
					} else {
						List<Profile> currentFollowers = M.getFollowers(response);
						
						for (Profile cache : cacheFollowers) {
							Boolean found = false;
							
							for (Profile profile : currentFollowers) {
								if (cache.getPk().equals(profile.getPk())) found = true;
							}
							if (!found) {
								DiscordWebhook webhook = new DiscordWebhook(Prop.getString("Webhook"));
								webhook.addEmbed(new DiscordWebhook.EmbedObject()
									.setColor(Color.RED)
									.setThumbnail(cache.getProfile_pic_url())
									.setAuthor(cache.getUsername(), "https://instagram.com/" + cache.getUsername(), cache.getProfile_pic_url())
									.setDescription(cache.getExtraProperties().toString())
									.addField("Full Name", cache.getFull_name(), true)
									.addField("PK", cache.getPk().toString(), true)
								);
								try {
									webhook.execute();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						cacheFollowers = currentFollowers;
					}
					WhoUnfollows.loop();
				}).exceptionally(ex -> {
					S.log("ERROR >> " + ex.getMessage());
				    return null;
				});
			}
		}, (S.R.nextInt(3600000-1800000) + 1800000));
	}
}