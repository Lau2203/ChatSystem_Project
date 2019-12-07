package chatsystem.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;

import java.util.Hashtable;
import java.util.Enumeration;

class Config {

}

public class ConfigParser {

	private static String configFilePath;
	private static boolean hasBeenInitialized = false;
	private static boolean hasBeenParsed = false;

	private static BufferedReader in;

	private static Hashtable<String, String> configBindings;

	private static void init(String cfp) {
		ConfigParser.configFilePath 	= cfp;
		ConfigParser.hasBeenInitialized = true;
		ConfigParser.hasBeenParsed 	= false;
		ConfigParser.configBindings 	= new Hashtable<String, String>();
	}

	private static void open() throws FileNotFoundException {
		ConfigParser.in	= new BufferedReader(new FileReader(ConfigParser.configFilePath));
	}

	private static void close() throws IOException {
		ConfigParser.in.close();
	}

	public synchronized static void parse(String cfp) throws FileNotFoundException, IOException {

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

			if (config.length == 2)
				ConfigParser.configBindings.put(config[0], config[1]);
		}

		ConfigParser.close();	
		ConfigParser.hasBeenParsed = true;
	}

	public synchronized static String get(String key) { return ConfigParser.configBindings.get(key); }

	private synchronized static void writeNewSetting(String key, String value) {

		PrintWriter out;

		if (!ConfigParser.hasBeenInitialized)
			return;

		try { 
			out = new PrintWriter(new FileWriter(ConfigParser.configFilePath, true));
		} catch (IOException ioe) { return; }

		out.println(key + "=" + value);
		out.flush();
		out.close();
	}

	public synchronized static void updateSetting(String key, String value) {
		if (!ConfigParser.hasBeenInitialized)
			return;

		/* If the key is not set in the configuration file, we just append it at the end of the config file */
		if (ConfigParser.get(key) == null) {
			ConfigParser.writeNewSetting(key, value);
		}
		/* Otherwise, we need to rewrite the whole config file */
		else {
			PrintWriter out;

			try { 
				out = new PrintWriter(new FileWriter(ConfigParser.configFilePath));
			} catch (IOException ioe) { return; }

			ConfigParser.configBindings.put(key, value);

			Enumeration<String> keys = ConfigParser.configBindings.keys();
			String _key;
			
			while (keys.hasMoreElements()) {
				_key = keys.nextElement();

				out.println(_key + "=" + ConfigParser.get(_key));
			}

			out.flush();
			out.close();
		}
	}
}


