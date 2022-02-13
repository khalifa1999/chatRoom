import java.rmi.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.net.MalformedURLException;

import javax.swing.*;


public class InterfaceGraphique implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title = "Logiciel de discussion en ligne";
    private String pseudo = null;
    private ChatRoom room = null;
    
    private JFrame window = new JFrame(this.title);
    private JTextArea txtOutput = new JTextArea();
    private JTextField txtMessage = new JTextField();
    private JButton btnSend = new JButton("Envoyer");
    ChatUserImpl chatUserImpl;
    
    public InterfaceGraphique() throws RemoteException {
        this.createIHM();
        try {
        	chatUserImpl = new ChatUserImpl();
        	chatUserImpl.setIg(this);
			Remote r = Naming.lookup("TP0");
			this.room = (ChatRoom)r;
			this.requestPseudo();
		} catch (MalformedURLException e) {
			System.out.println("Acces impossible a la salle");
			System.exit(0);
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
        
    }

    public void createIHM() {
        JPanel panel = (JPanel)this.window.getContentPane();
        JScrollPane sclPane = new JScrollPane(txtOutput);
        panel.add(sclPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(this.txtMessage, BorderLayout.CENTER);
        southPanel.add(this.btnSend, BorderLayout.EAST);
        panel.add(southPanel, BorderLayout.SOUTH);

        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
					window_windowClosing(e);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
            }
        });
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnSend_actionPerformed(e);
            }
        });
	txtMessage.addKeyListener(new KeyAdapter() {
		public void keyReleased(KeyEvent event) {
			if (event.getKeyChar() == '\n')
			    btnSend_actionPerformed(null);
		    }
	});
        this.txtOutput.setBackground(new Color(220,220,220));
        this.txtOutput.setEditable(false);
		this.window.setSize(500,400);
        this.window.setVisible(true);
        this.txtMessage.requestFocus();
    }
        public void requestPseudo() throws RemoteException {
        this.pseudo = JOptionPane.showInputDialog(
            this.window, "Entrez votre pseudo : ",
            this.title,  JOptionPane.OK_OPTION
        );
        if (this.pseudo == null) System.exit(0);
        this.room.subscribe(chatUserImpl, this.pseudo);
        
    }
    
    public void window_windowClosing(WindowEvent e) throws RemoteException {
    	this.room.unsubscribe(this.pseudo);
    	System.exit(-1);
    }
    //envoi message
    public void btnSend_actionPerformed(ActionEvent e) {
    	try {
			this.room.postMessage(this.pseudo, this.txtMessage.getText());
		} catch (RemoteException e1) {
			e1.printStackTrace();
			System.out.println("Impossible d envoyer le message");
		}
    	this.txtMessage.setText("");
        this.txtMessage.requestFocus();
    }

    public static void main(String[] args) throws RemoteException {
        new InterfaceGraphique();
    }

	public void display(String message) throws RemoteException {
		this.txtOutput.append(message +" \n");
		this.txtOutput.moveCaretPosition(this.txtOutput.getText().length());
	}
}
