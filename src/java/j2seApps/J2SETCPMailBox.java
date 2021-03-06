
import java.io.IOException;
import net.sharkfw.kep.SharkProtocolNotSupportedException;
import net.sharkfw.knowledgeBase.SharkKB;
import net.sharkfw.knowledgeBase.inmemory.InMemoSharkKB;
import net.sharkfw.peer.KnowledgePort;
import net.sharkfw.peer.SharkEngine;
import net.sharkfw.peer.J2SEAndroidSharkEngine;
import net.sharkfw.protocols.Protocols;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Command line tool for starting a simple mailbox kp to act as a tcp-hub.
 * Pass portnumer and timeout value in ms as parameters.
 * 
 * @author mfi
 */
public class J2SETCPMailBox {

  public static void main(String[] args) throws SharkProtocolNotSupportedException, IOException {
    
    String portstring = args[0];
    int port = Integer.parseInt(portstring);

    String connectiontimeoutstring = args[1];
    int conntimeout = Integer.parseInt(connectiontimeoutstring);

    System.out.println("Hub starting with port: " + portstring);
    System.out.println("Hub starting with timeout: " + connectiontimeoutstring);

    SharkKB kb = new InMemoSharkKB();

    J2SEAndroidSharkEngine se = new J2SEAndroidSharkEngine();

    System.out.println("SharkEngine created.");

//    KnowledgePort mailboxkp = new MailBoxKP(se, null);

    System.out.println("MailBoxKP created.");

    se.setConnectionTimeOut(conntimeout);
    se.startTCP(port);

  }

}
