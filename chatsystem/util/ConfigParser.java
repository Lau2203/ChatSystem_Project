package chatsystem.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Hashtable;

class Config {

}

public class ConfigParser {

	private static String configFilePath;
	private static boolean hasBeenInitialized = false;
	private static boolean hasBeenParsed = false;

	private static BufferedReader in;

	private static Hashtable<String, String> configBindings;

	private static void init(String cfp) {
		ConfigParser.configFilePath = cfp;
		ConfigParser.hasBeenInitialized = true;
		ConfigParser.hasBeenParsed = false;
		ConfigParser.configBindings = new Hashtable<String, String>();
	}

	private static void open() throws FileNotFoundException {
		ConfigParser.in	= new BufferedReader(new FileReader(ConfigParser.configFilePath));
	}

	private static void close() throws IOException {
		ConfigParser.in.close();
	}

	public static void parse(String cfp) throws FileNotFoundException, IOException {

		String line;
		String[] config;

		if (!ConfigParser.hasBeenInitialized)
			ConfigParser.init(cfp);

		if (ConfigParser.hasBeenParsed)
			return;

		ConfigParser.open();

		while (true) {

			line = ConfigParser.in.readLine();	

			if (line == null) break;
			
			config = line.split("=");	

			ConfigParser.configBindings.put(config[0], config[1]);
		}

		ConfigParser.close();	
		ConfigParser.hasBeenParsed = true;
	}

	public static String get(String key) { return ConfigParser.configBindings.get(key); }
}


