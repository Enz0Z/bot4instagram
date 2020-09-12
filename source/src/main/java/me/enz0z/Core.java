package me.enz0z;

import java.util.Scanner;
import java.util.concurrent.Callable;

import com.github.instagram4j.instagram4j.IGClient;
import com.github.instagram4j.instagram4j.IGClient.Builder.LoginHandler;
import com.github.instagram4j.instagram4j.exceptions.IGLoginException;
import com.github.instagram4j.instagram4j.utils.IGChallengeUtils;

import me.enz0z.utils.Utils;

public class Core {
	
	private static IGClient client;

	public static void main(String args[]) {
		if (args.length > 1) {
			try {
				Scanner scanner = new Scanner(System.in);
				Callable<String> inputCode = () -> {
				    System.out.print("Please input code: ");
				    return scanner.nextLine();
				};
				LoginHandler twoFactorHandler = (client, response) -> {
				    // included utility to resolve two factor
				    // may specify retries. default is 3
				    return IGChallengeUtils.resolveTwoFactor(client, response, inputCode);
				};
				client = IGClient.builder().username(args[0]).password(args[1]).onTwoFactor(twoFactorHandler).login();
				
				Utils.print("Currently connected as " + client.getSelfProfile().toString());
				Commands.loop();
			} catch (IGLoginException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static IGClient getClient() {
		return client;
	}
}