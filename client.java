import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.StringReader;

public class client {

  private String host;
  private int port;

  public static void main(String[] args) throws UnknownHostException, IOException {
    new client("127.0.0.1", 12345).run();
  }

  public client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void run() throws UnknownHostException, IOException {
    // connect client to server
    Socket client = new Socket(host, port);
    System.out.println("Client successfully connected to server!");

    // Get Socket output stream (where the client send her mesg)
    PrintStream output = new PrintStream(client.getOutputStream());

    // ask for a nickname
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter a nickname: ");
    String nickname = sc.nextLine();

    // send nickname to server
    output.println(nickname);

    // create a new thread for server messages handling
    new Thread(new ReceivedMessagesHandler(client.getInputStream())).start();

    // read messages from keyboard and send to server
    System.out.println("Messages: \n");

    // while new messages
    while (sc.hasNextLine()) {
      output.println(sc.nextLine());
    }

    // end ctrl D
    output.close();
    sc.close();
    client.close();
  }
}

class ReceivedMessagesHandler implements Runnable {

  private InputStream server;

  public ReceivedMessagesHandler(InputStream server) {
    this.server = server;
  }

  public void run() {
    // receive server messages and print out to screen
    Scanner s = new Scanner(server);
    String tmp = "";
    while (s.hasNextLine()) {
      tmp = s.nextLine();
      if (tmp.charAt(0) == '[') {
        tmp = tmp.substring(1, tmp.length()-1);
        System.out.println(
            "\nUSERS LIST: " +
            new ArrayList<String>(Arrays.asList(tmp.split(","))) + "\n"//Java ArrayList class uses a dynamic array for storing the elements. It is like an array, but there is no size limit. We can add or remove elements anytime.
            );                                                       //break the string at the separator  split()
      }else{
        try {
          System.out.println("\n" + getTagValue(tmp));
          // System.out.println(tmp);
        } catch(Exception ignore){}
      }
    }
    s.close();
  }

  // I  use a javax.xml.parsers but the goal of Client.java is to keep everything tight and simple
  public static String getTagValue(String xml){
    return  xml.split(">")[2].split("<")[0] + xml.split("<span>")[1].split("</span>")[0];
  }

}


// acceptFriendRequest.java: This servlet add friend request in the friend request list.

// addFriendRequest.java: This servlet adds the friend request to the user in the friend request list when the user sends the friend request.

// AddMessage.java: This servlet adds messages to the message box and shows them to the users.

// ApplicationSetting.java: This servlet implements the Serializable interface and sets the number of notifications.

// Authenticator.java: This servlet is used to authenticating the user when he login to the system. It checks whether the username and password are correct or not.

// CloseAccount.java: This servlet deletes the user account from the database and closes the account.

// FriendList.java: This servlet shows the friend list of a user. In this list all the friends that are being added by accepting the friend request.

// Logout.java: This servlet is used to logout the user from the application.

// MessageList.java: This java file shows the message ists to the user in the message box.

// ProfileUpdater.java: This servlet is used to update the user profile like username, name, and password.

// removeFriend.java: This servlet removes the friend from the friend list.

// RemoveFriendRequest.java: This servlet removes the friend request.

// UserAvatar.java: This servlet fetches the images from the images folder and shows them on the webpage of the user profile.

// userCreator.java: This servlet creates a new user and adds to the database.

// usernameAvailabilityVerifier.java: This servlet checks the username availability at the time of registration. If the user tried to enter the already registered username, then there will be an error message shown that asks the user to choose other usernames.