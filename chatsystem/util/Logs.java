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

	private static boolean isINFOEnabled;
	private static boolean isWARNEnabled;
	private static boolean isERROEnabled;

	public synchronized static void init(boolean isLogEnabled, String logFilePath) throws IOException {
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

		Logs.isINFOEnabled = true;
		Logs.isWARNEnabled = true;
		Logs.isERROEnabled = true;
	}
	
	public synchronized static void println(String s) { 
		if (Logs.isLogEnabled) {
			try { 
				Logs.out.append(s + "\n"); 
				Logs.out.flush();
			} catch (IOException ioe) {
				System.out.println("[x] Error occured while writing in log file.");
			}
		}
	}

	public synchronized static void printinfo(String s) { if (Logs.isINFOEnabled) { Logs.println("[+] " + s); }}
	public synchronized static void printwarn(String s) { if (Logs.isWARNEnabled) { Logs.println("[!] " + s); }}
	public synchronized static void printerro(String s) { if (Logs.isERROEnabled) { Logs.println("[x] " + s); }}

	public synchronized static void println(String instance, String s) 	{ Logs.println("[+] [" + instance + "] " + s); }
	public synchronized static void printinfo(String instance, String s) 	{ if (Logs.isINFOEnabled) { Logs.println("[+] [" + instance + "] " + s); }}
	public synchronized static void printwarn(String instance, String s) 	{ if (Logs.isWARNEnabled) { Logs.println("[!] [" + instance + "] " + s); }}
	public synchronized static void printerro(String instance, String s) 	{ if (Logs.isERROEnabled) { Logs.println("[x] [" + instance + "] " + s); }}

	public synchronized static void readLogs() {

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

