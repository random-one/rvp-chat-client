package ChatClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.*;

import javax.swing.*;

public class ClientView {
    //buttons:
    static private JButton connectBtn = null;
    static private JButton disconnectBtn = null;
    private JButton addFriend = null;
    private JButton sendButton = null;
    private JButton fileButton = null;
    //Panels:
    private JPanel friendPane = null;
    private JScrollPane friendListScroll = null;
    private JPanel buttonsPane = null;
    private JPanel chatPane = null;
    private JScrollPane chatTextScroll = null;
    private JSplitPane chatSplit = null;
    private JPanel connectionPane = null;
    private JPanel mainPane = null;
    private JSplitPane middleSplit = null;
    private JFrame mainFrame = null;
    //Text:
    private JTextArea chatLine = null;
    private JTextArea friendList = null;
    private JTextArea chatText = null;
    private JLabel userLbl = null;
    private JTextField userText = null;
    private JLabel ipLbl = null;
    private JTextField ipText = null;
    private JLabel portLbl = null;
    private NumericTextField portText = null;
    //menu:
    private JMenuBar menuBar = null;
    private JMenu fileMenu = null;
    //other:
    private ClientSide client;
    private MessageHandler messageHandler = null;

    public static void main(String[] args) {
        ClientView view = new ClientView();
        view.GuiInit();
    }

    private void GuiInit(){
        //Friend list Panel
        friendPane = new JPanel(new BorderLayout());
        addFriend = new JButton("Add Friend");
        ActionListener addFriendToList = new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                /*
                 * view list and add friend
                 */
            	//TODO:friendList not visible, can't add clients to list
            	//Temporary solution, friendList is now a class member
            	//TODO:Database row manipulation.
            	//friendList.append(client.getClientName()+ "\n");
            	//Added a manipulator for the friendList textArea
            }
        };
        addFriend.addActionListener(addFriendToList);
        addFriend.setEnabled(true);
        friendList = new JTextArea();
        friendList.setLineWrap(true);
        friendList.setEditable(false);
        friendListScroll = new JScrollPane(friendList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        friendPane.add(addFriend, BorderLayout.NORTH);
        friendPane.add(friendListScroll, BorderLayout.CENTER);
        friendPane.setPreferredSize(new Dimension(200, 300));

        //Chat Text
        chatPane = new JPanel(new BorderLayout());
        chatText = new JTextArea(10,35);
        chatText.setLineWrap(true);
        chatText.setEditable(false);
        chatText.setForeground(Color.blue);
        JScrollPane chatTextScroll = new JScrollPane(chatText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        //Send buttons
        buttonsPane = new JPanel(new GridLayout(1, 2));
        sendButton = new JButton("Send");
        fileButton = new JButton("File send");
        ActionListener sendClick = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                /*
                 * send message
                 */
                 if(client == null)
                 {
                        System.out.println("Client not connected!");
                 }
                 else
                 {
                        if(chatLine.getText() != "")
                        {
                                TextMessage msgToSend = new TextMessage(client.getClientName(),"",chatLine.getText());
                                client.sendMessage(msgToSend);
                                chatText.append("\n" + client.getClientName() + ": " + msgToSend.getContent());
                                chatLine.setText("");
                                //TODO:Need to add receiver address. ChatLine is not visible, content cannot be extracted from the chatline to form a msg.
                                //TODO:See constructor when the msgToSend is formed to make the fix for the content field.
                        }
                        //Message m = client.receiveMessage();
                        //if (m.getType() == Message.msgType.TEXT_MESSAGE) {
                        //       TextMessage tm = (TextMessage) m;
                        //        chatText.setText(chatText.getText() + "\n" + tm.getContent());
                        //}
                }
            }
        };

        ActionListener openFileDialog = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = new JFileChooser();
                //fc.showOpenDialog();
                int returnVal = fc.showOpenDialog(fc);
                //TODO: Decide how to send file.
            }
        };
        sendButton.addActionListener(sendClick);
        sendButton.setEnabled(true);
        fileButton.addActionListener(openFileDialog);
        fileButton.setEnabled(true);

        buttonsPane.add(sendButton);
        buttonsPane.add(fileButton);
        friendPane.add(buttonsPane,BorderLayout.SOUTH);

        //Chat Pane
        chatLine = new JTextArea();
        //chatLine was created here ^, now it is a class member.
        chatSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, chatTextScroll, chatLine);
        chatSplit.setResizeWeight(1.0);
        chatSplit.setOneTouchExpandable(true);
        //chatSplit.setDividerLocation(500);
        //chatPane.add(chatTextScroll, BorderLayout.CENTER);
        //chatPane.add(chatLine, BorderLayout.SOUTH);
        //chatPane.setPreferredSize(new Dimension(500, 200));

        //Menu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");

        menuBar.add(fileMenu);

        messageHandler = new MessageHandler(chatText);

        //Connection & disconnection
        connectionPane = new JPanel(new FlowLayout());
        userLbl = new JLabel("User Name:");
        userText = new JTextField("", 10);
        ipLbl = new JLabel("Server IP:");
        ipText = new JTextField("127.0.0.1", 10);
        portLbl = new JLabel("port:");
        portText = new NumericTextField(5);
        //JFormattedTextField portText = new JFormattedTextField();
        ActionListener connectAction = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if (userText.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Please, enter user name.",  "Error", JOptionPane.ERROR_MESSAGE);
                    userText.requestFocusInWindow();
                    return;
                }
                if (portText.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(mainFrame, "Please, enter server port.",  "Error", JOptionPane.ERROR_MESSAGE);
                    portText.requestFocusInWindow();
                    return;
                }
                client = new ClientSide(ipText.getText(), Integer.parseInt(portText.getText()), userText.getText(), messageHandler);
                if (!client.start())
                    return;
                connectBtn.setEnabled(false);
                disconnectBtn.setEnabled(true);
                /*
                 *  connect to server
                 */
            }
        };
        connectBtn = new JButton("Connect");
        connectBtn.addActionListener(connectAction);
        connectBtn.setEnabled(true);
        ActionListener disconnectAction = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                SystemMessage s = new SystemMessage("", ipText.getText(), userText.getText(), "has disconnected", SystemMessage.systemMsgType.SYSTEM_LOGOUT_MESSAGE);
                client.sendMessage(s);
                client.disconnect();
                System.out.println("Client has disconnected from server (socket is closed)...");
                connectBtn.setEnabled(true);
                disconnectBtn.setEnabled(false);
                /*
                 * disconnect from server
                 */
            }
        };
        disconnectBtn = new JButton("Disconnect");
        disconnectBtn.addActionListener(disconnectAction);
        disconnectBtn.setEnabled(false);

        connectionPane.add(userLbl);
        connectionPane.add(userText);
        connectionPane.add(ipLbl);
        connectionPane.add(ipText);
        connectionPane.add(portLbl);
        connectionPane.add(portText);
        connectionPane.add(connectBtn);
        connectionPane.add(disconnectBtn);

        //Main Pane and connections
        mainPane = new JPanel(new BorderLayout());
        middleSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, chatSplit, friendPane);
        middleSplit.setResizeWeight(1.0);
        middleSplit.setOneTouchExpandable(true);
        //middleSplit.setDividerLocation(500);
        //mainPane.add(chatPane, BorderLayout.WEST);
        mainPane.add(middleSplit, BorderLayout.CENTER);
        mainPane.add(connectionPane, BorderLayout.NORTH);

        mainFrame = new JFrame("RVP Chat Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setContentPane(mainPane);
        mainFrame.setSize(mainFrame.getPreferredSize());
        mainFrame.setMinimumSize(new Dimension(800, 600));
        mainFrame.setJMenuBar(menuBar);

        mainFrame.setVisible(true);

        mainFrame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                if (client != null)
                    client.disconnect();
            }
        });
    }
}

class NumericTextField extends TextField
{
 public NumericTextField (String _initialStr, int _col)
 {
    super (_initialStr, _col);

    this.addKeyListener(new KeyAdapter()
   {
       public void keyTyped (KeyEvent e)
        { 
            char c = e.getKeyChar();

            if (!   ((c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)
                ||  (c == KeyEvent.VK_ENTER)      || (c == KeyEvent.VK_TAB)
                ||  (Character.isDigit(c)) || (c == '.'))) 
            {
               e.consume();
           }
        }
    });
 }

  public NumericTextField (int _col)
  {
    this ("", _col);
  }
}