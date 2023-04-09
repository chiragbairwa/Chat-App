import java.io.*;
import java.net.*;
import java.util.Scanner;

class Server {
  public static void main(String[] args) throws Exception {
    System.out.println("Server Started :");

    ServerSocket ss = new ServerSocket(3333);
    Socket s = ss.accept();

    DataInputStream din = new DataInputStream(s.getInputStream());
    DataOutputStream dout = new DataOutputStream(s.getOutputStream());
    Scanner sc = new Scanner(System.in);

    String str = "", str2 = "";
    while ( !str.equals("stop") ) {
      str = din.readUTF();
      System.out.println("Client says: " + str);
      
      // For Sending from server
      str2 = sc.nextLine();
      dout.writeUTF(str2);
      System.out.println("Server says: " + str2);
      dout.flush();
    }

    din.close();
    s.close();
    ss.close();
  }
}
