import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatUser extends Remote{
	public void displayMessage(String message) throws RemoteException;
}
