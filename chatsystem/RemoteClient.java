package chatsystem;


public class RemoteClient extends Client {

	public RemoteClient() {
		super();
		this.netmanager.doWorkRemotelyOnly(true);
	}

	public RemoteClient(String configFilePath) {
		super(configFilePath);
		this.netmanager.doWorkRemotelyOnly(true);
	}

	public static void main(String[] args) {
		if (args.length != 0) {
			(new RemoteClient(args[0])).run();
		} else {
			(new RemoteClient()).run();
		}
	}
}
