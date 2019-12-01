package chatsystem;

public enum NotifyInformation {

	NEW_CONNECTION,
	END_OF_CONNECTION,

	USERNAME_MODIFICATION,

	/* This notification is raised once a client is open
	 * Once the client open, this latter one inform everyone on the network
	 * (by broadcast) that it is now active. It gives its user's fingerprint 
	 * and address. Then all the already-active clients respond to welcome it
	 * by sending their own fingerprint, address, username AND the number of active client
	 * on the network so that the just-opened client knows how many request it needs to wait for
	 * in order to get a full and valid active users list. Once all the already-active clients
	 * responded, the just-opened client have a valid active users list and can now check the 
	 * availability of its username and ask the user to change it if needed. */
	NEW_ACTIVE_CLIENT,

	NEW_ACTIVE_USER,
	USER_LEFT_NETWORK,

	NEW_MESSAGE,

	READY_TO_CHECK_USERNAME,

	NONE
}

