import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatUserImpl extends UnicastRemoteObject implements ChatUser {
	private static final long serialVersionUID = 1L;
	private InterfaceGraphique ig;
	
	protected ChatUserImpl() throws RemoteException {
		super();
	}
	
	public void setIg(InterfaceGraphique ig) {
		this.ig = ig;
	}

	@Override
	public void displayMessage(String message) throws RemoteException {
		ig.display(message);
	}

}
