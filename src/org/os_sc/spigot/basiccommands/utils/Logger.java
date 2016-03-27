package org.os_sc.spigot.basiccommands.utils;

import java.util.logging.Level;

public class Logger {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static void init(java.util.logging.Logger logger) {
		Logger.logger = logger;
	}

	private static java.util.logging.Logger logger;

	private static final String tag_d = ANSI_GREEN + "[" + ANSI_CYAN + "BC" + ANSI_GREEN + "] " + ANSI_RESET;
	private static final String tag_i = ANSI_BLUE + "[" + ANSI_GREEN + "BC" + ANSI_BLUE + "] " + ANSI_RESET;
	private static final String tag_e = ANSI_RED + "[" + ANSI_PURPLE + "BC" + ANSI_RED + "] " + ANSI_RESET;

	public static void debug(String msg)
	{
		logger.log(Level.FINE, tag_d + msg);
	}

	public static void info(String msg)
	{
		logger.log(Level.INFO, tag_i + msg);
	}

	public static void error(String msg)
	{
		logger.log(Level.WARNING, tag_e + msg);
	}

	public static void error(String msg, Exception e)
	{
		error(msg);
		e.printStackTrace();
	}
}
