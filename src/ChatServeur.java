import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


public class ChatServeur {

	public static void main(String[] args) {
		try{
			LocateRegistry.createRegistry(1099);
			ChatRoomImpl chatRoomImpl = new ChatRoomImpl();
			Naming.rebind("TP0", chatRoomImpl);
		
			System.out.println("Serveur lancé");
		}catch(RemoteException e){
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
