package chatsystem;

public class LocalClient extends Client {

	public LocalClient() {
		super();
		this.netmanager.doWorkRemotelyOnly(false);
	}

	public LocalClient(String configFilePath) {
		super(configFilePath);
		this.netmanager.doWorkRemotelyOnly(false);
	}

	/* Update the GUI */
	public static void main(String[] args) {
		if (args.length != 0) {
			(new LocalClient(args[0])).run();
		} else {
			(new LocalClient()).run();
		}
	}
}
