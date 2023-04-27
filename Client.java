import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Client extends JFrame {
  Socket socket;
  BufferedReader br;
  PrintWriter out;

  DataOutputStream dout;
  DataInputStream din_server;

  // GUI
  public JLabel heading = new JLabel("Client Area");
  public JTextArea messageArea = new JTextArea();
  // public JTextArea messageAreaUser = new JTextArea();

  public JTextField messageInput = new JTextField();
  public Font font = new Font("Roboto", Font.PLAIN, 20);

  public Client() throws IOException {
    socket = new Socket("localhost", 3333);

    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out = new PrintWriter(socket.getOutputStream());
    try {
      gui();
      handleEvents();
      getData();
      // sendData();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void gui() {
    this.setTitle("Client Messager");
    this.setSize(300, 400);

    // this.setLocationRelativeTo(null);
    this.setLocation(new Point(400,200));

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Components
    heading.setFont(font);

    messageArea.setFont(font);
    messageInput.setFont(font);

    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

    messageArea.setEditable(false);
    this.setLayout(new BorderLayout());

    // Adding comp
    this.add(heading, BorderLayout.NORTH);
    JScrollPane jScrollPane = new JScrollPane(messageArea);
    
    this.add(jScrollPane, BorderLayout.CENTER);
    this.add(messageInput, BorderLayout.SOUTH);

    this.setVisible(true);

  }

  void handleEvents() {
    messageInput.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {

        // System.out.println("Key Typed");
      }

      @Override
      public void keyPressed(KeyEvent e) {
        // System.out.println("Key Pressed");

      }

      @Override
      public void keyReleased(KeyEvent e) {
        // System.out.println("Key Released");
        if (e.getKeyCode() == 10) {
          String msgToSend = messageInput.getText();
          messageArea.append("Me : " + msgToSend + "\n");
          out.println(msgToSend);
          out.flush();

          messageInput.setText("");
          messageInput.requestFocus();
        }
      }

    });
  }

  void sendData() {

    System.out.println("In sendData...");

    Runnable r1 = () -> {
      try {
        while (!socket.isClosed()) {
          String content = br.readLine();
          out.println(content);
          out.flush();
          if (content.equals("exit")) {
            socket.close();
            break;
          }
        }
      } catch (Exception err) {
        System.out.println("connection closed");
      }
    };

    new Thread(r1).start();
  }

  void getData() {
    Runnable r2 =() ->{
      try{

        while(true){

          String content = br.readLine();
          
          if (content.equals("exit")) {
            System.out.println("Stopped..");
            JOptionPane.showMessageDialog(this,"Server Ended");
            socket.close();
            break;
          }
          messageArea.append("Server : " + content + "\n");

        }
      }catch(Exception err){
        System.out.println("Err");
      }
    };
    new Thread(r2).start();
  }
    
  public static void main(String args[]) throws IOException {
    new Client();
  }
}
