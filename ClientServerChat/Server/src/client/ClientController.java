import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 *
 * @author acer
 */
public class ClientController implements Initializable {
    
    
    @FXML
    private AnchorPane root;
    @FXML
    private Button disconnect;
    @FXML
    private Text title;
    @FXML
    private Button send;
    @FXML
    private TextArea show;
    @FXML
    private TextField message;
    
    public String sentence;
    public String modifiedSentence;
    public DataOutputStream outToServer;
    public BufferedReader inFromServer;
    public Socket clientSocket;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            clientSocket=new Socket("localhost", 6781);
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void send(ActionEvent event) throws IOException {
        sentence=message.getText();
        outToServer.writeBytes(sentence + '\n');
        modifiedSentence = inFromServer.readLine();
        show.appendText("From Server : "+modifiedSentence+"\n");
    }   

    @FXML
    private void disconnect(ActionEvent event) {
        System.exit(0);
    }
}
