package me.enz0z.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Timer;

public class S {
	
	public static Timer T = new Timer();
	public static Random R = new Random();

	public static Long currentMillis() {
		return System.currentTimeMillis();
	}

	public static Long currentSeconds() {
		return System.currentTimeMillis() / 1000;
	}

	public static Long elapsedMillis(Long from) {
		return from - System.currentTimeMillis();
	}

	public static Long elapsedSeconds(Long from) {
		return from - (System.currentTimeMillis() / 1000);
	}

	public static String format(Long seconds) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		return df.format(new Date(seconds * 1000));
	}
	
	public static String argsToString(Integer startArg, String[] args) {
		String message = "";
		
		for (int i = startArg; i < args.length; i++) {
			message = message + args[i] + " ";
		}
		return message.trim();
	}
	
	public static String onlyNumbers(String text) {
		String numbers = "";

		for (int i = 0; i < text.length(); i++) {
			try {
				Integer.parseInt(text.charAt(i) + "");
				numbers = numbers + text.charAt(i);
			} catch (NumberFormatException e) {}
		}
		return (numbers.equals("") ? "0" : numbers);
	}

	public static Boolean isInteger(String integer) {
		Boolean is = false;
		
		try {
			Integer.parseInt(integer);
			is = true;
		} catch (NumberFormatException e) {
			is = false;
		}
		return is;
	}
	
	public static void log(Object text) {
		System.out.print(text.toString() + "\n");
	}
}