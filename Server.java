import java.io.*;
import java.net.*;
// Gui
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

class Server extends JFrame{
  ServerSocket server;
  Socket socket;

  BufferedReader br;
  PrintWriter out;

  // GUI
  public JLabel heading = new JLabel("Server Area");
  public JTextArea messageArea = new JTextArea();
  public JTextField messageInput = new JTextField();
  public Font font = new Font("Roboto", Font.PLAIN, 20);

  public Server() throws IOException {

    server = new ServerSocket(3333);
    socket = server.accept();

    br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    out = new PrintWriter(socket.getOutputStream());

    try{
      gui();
      handleEvents();
      // sendData();
      getData();
    }
    catch(Exception err){
      System.out.println("Error");
    }
  }

  
  public void gui() {
    this.setTitle("Server Messager");
    this.setSize(300, 400);

    this.setLocation(new Point(100,200));
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
    
    Runnable r1 = () -> {
      while (true) {
        try {        
        String content = messageInput.getText();
        out.println(content);
        out.flush();
        } catch (Exception e) {
          e.printStackTrace();
        }
        // System.out.println("Sending Data");
      }
    };

    new Thread(r1).start();
  }

  void getData() {
    Runnable r2 = () -> {
      try {
        while (true) {
          String msg = br.readLine();

          if (msg.equals("exit")) {
            System.out.println("Stopped..");
            JOptionPane.showMessageDialog(this,"Server Ended");
            socket.close();
            break;
          }
          // System.out.println("Client says: " + msg);
          messageArea.append("Client : " + msg + "\n");
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
