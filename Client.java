import java.io.*;
import java.net.*;

class Client {
  Socket socket;
  BufferedReader br;
  BufferedReader br_server;

  DataOutputStream dout;
  DataInputStream din_server;

  public Client() throws IOException {
    System.out.println("Client Started ");
    socket = new Socket("localhost", 3333);

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
        while (!socket.isClosed()) {
          if (str.equals("stop")) {
            System.out.println("Client Stopped :");
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

        while (!socket.isClosed()) {
          if (str.equals("stop")) {
            System.out.println("Stopped..");
            return;
          }
          str = din_server.readUTF();
          System.out.println("Server says: " + str);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    };
    new Thread(r2).start();
  }

  public static void main(String args[]) throws IOException {
    new Client();
  }
}
