import java.io.*;
import java.net.*;

class Server {
  ServerSocket server;
  Socket socket;
  BufferedReader br_server;

  DataOutputStream dout;

  DataInputStream din_server;

  public Server() throws IOException {
    System.out.println("Server Started :");

    server = new ServerSocket(3333);
    socket = server.accept();
    sendData();
    getData();
  }

  void sendData() {
    System.out.println("In sendData...");
    
    Runnable r1 = () -> {
      try {
        dout = new DataOutputStream(socket.getOutputStream());
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str2 = "";
        while (!server.isClosed()) {
          if (str.equals("stop")) {
            System.out.println("Server Stopped :");
            return;
          }

          // For Sending from server
          str2 = br.readLine();
          dout.writeUTF(str2);

          dout.flush();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("Sending Data");
    };

    new Thread(r1).start();
  }

  void getData() {
    Runnable r2 = () -> {
      System.out.println("In GetData...");

      try {
        din_server = new DataInputStream(socket.getInputStream());
        String str = din_server.readUTF();

        while (!server.isClosed()) {
          if (str.equals("stop")) {
            System.out.println("Stopped..");
            return;
          }
          str = din_server.readUTF();
          System.out.println("Client says: " + str);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    };
    new Thread(r2).start();
  }

  public static void main(String[] args) throws Exception {
    new Server();
  }
}
