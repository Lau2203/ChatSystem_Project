package chatsystem;

import chatsystem.LocalClient;

public class Application {

	private LocalClient client;

	public Application() {
		this.client = new LocalClient();
	}

	private void run() {
		this.client.run();
	}

	public static void main(String[] args) {

		Application app = new Application();
		
		app.run();

	}
}

