package me.enz0z;

import java.util.Scanner;
import java.util.concurrent.Callable;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.IGClient.Builder.LoginHandler;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils;

import me.enz0z.instagram.WhoUnfollows;
import me.enz0z.utils.Prop;
import me.enz0z.utils.S;

public class Core {
	
	private static IGClient client;

	public static void main(String args[]) {
		new Prop();
		try {
			Scanner scanner = new Scanner(System.in);
			Callable<String> inputCode = () -> {
			    System.out.print("Please input code: ");
			    return scanner.nextLine();
			};
			LoginHandler twoFactorHandler = (client, response) -> {
			    return IGChallengeUtils.resolveTwoFactor(client, response, inputCode);
			};
			client = IGClient.builder().username(Prop.getString("Username")).password(Prop.getString("Password")).onTwoFactor(twoFactorHandler).login();
			
			S.log("Currently connected as [" + client.getSelfProfile().toString() + "]");
			WhoUnfollows.loop();
		} catch (IGLoginException e) {
			e.printStackTrace();
		}
	}
	
	public static IGClient getClient() {
		return client;
	}
}