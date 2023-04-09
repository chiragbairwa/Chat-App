import java.io.*;
import java.net.*;

class Server {  
  public static void main(String[] args) {
    System.out.println("Server Started :");
    
    try {
      ServerSocket ss = new ServerSocket(6666);
      Socket s = ss.accept();// establishes connection

      DataInputStream dis = new DataInputStream(s.getInputStream());
      String str = (String) dis.readUTF();
      
      System.out.println("Client = " + str);
      ss.close();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}