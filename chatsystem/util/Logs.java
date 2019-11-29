package chatsystem.util;

import java.io.IOException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

public class Logs {

	private String filePath;
	private BufferedWriter out;

	public Logs(String logFilePath) throws IOException {
		this.filePath = logFilePath;
		
		this.out = new BufferedWriter(new FileWriter(this.filePath, true));
	}
	
	public void println(String s) { try { this.out.append(s + "\n"); } catch (IOException ioe) { System.out.println("Unable to write log"); } }

	public void printinfo(String s) { try { this.out.append("[+] " + s + "\n"); } catch (IOException ioe) { System.out.println("Unable to write log"); } }
	public void printwarn(String s) { try { this.out.append("[!] " + s + "\n"); } catch (IOException ioe) { System.out.println("Unable to write log"); } }
	public void printerro(String s) { try { this.out.append("[x] " + s + "\n"); } catch (IOException ioe) { System.out.println("Unable to write log"); } }

	public void readLogs() {

		this.writeLogs();
	
		try {
			BufferedReader in = new BufferedReader(new FileReader(this.filePath));	
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

	public void writeLogs() {
		try {
			this.out.flush();
		} catch (IOException ioe) {
			System.out.println("[x] Issue with logs writing process...");
		}
	}
}

