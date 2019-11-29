package chatsystem.util;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

public class Logs {

	private static String filePath;
	private static BufferedWriter out;

	private static boolean isLogEnabled;
	private static boolean isLogFileOpen;

	public static void init(boolean isLogEnabled, String logFilePath) throws IOException {
		Logs.filePath = logFilePath;

		Logs.isLogFileOpen = false;

		if (isLogEnabled) {
			try {	
				Logs.out = new BufferedWriter(new FileWriter(Logs.filePath, true));
				Logs.isLogFileOpen = true;

			} catch (IOException ioe) {
				Logs.isLogEnabled = false;
				throw ioe;
			}
		}

		Logs.isLogEnabled = isLogEnabled && isLogFileOpen;
	}
	
	public static void println(String s) { 
		if (Logs.isLogEnabled) {
			try { 
				Logs.out.append(s + "\n"); 
				Logs.out.flush();
			} catch (IOException ioe) {
				System.out.println("[x] Error occured while writing in log file.");
			}
		}
	}

	public static void printinfo(String s) { Logs.println("[+] " + s); }
	public static void printwarn(String s) { Logs.println("[!] " + s); }
	public static void printerro(String s) { Logs.println("[x] " + s); }

	public static void println(String instance, String s) 	{ Logs.println("[+] [" + instance + "] " + s); }
	public static void printinfo(String instance, String s) { Logs.println("[+] [" + instance + "] " + s); }
	public static void printwarn(String instance, String s) { Logs.println("[!] [" + instance + "] " + s); }
	public static void printerro(String instance, String s) { Logs.println("[x] [" + instance + "] " + s); }

	public static void readLogs() {

		if (Logs.isLogEnabled) {
			try {
				BufferedReader in = new BufferedReader(new FileReader(Logs.filePath));	
				String line;
				
				line = in.readLine();
				while (line != null) {
					System.out.println(line);
					line = in.readLine();
				}

				in.close();
			} catch (IOException ioe) {
				System.out.println("[x] Issue with logs reading process...");
			}
		}
	}
}

