/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 *
 * @author acer
 */
public class FXMLDocumentController implements Runnable {
    
    
    @FXML
    private Text title;
    @FXML
    private Button connect;
  
    @FXML
    public TextArea sshow;
   
    public void settext(String text)
    {
        sshow.appendText(text);
    }
    
    @FXML
    private void connect(ActionEvent event) throws IOException {
        new Thread(this).start();
    }

    @Override
    public void run() {
        
        int id = 1;
        sshow.appendText("Connected\n");
        
        ServerSocket welcomeSocket = null;
        try {
            welcomeSocket = new ServerSocket(6781);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while(true)
        {
            Socket connectionSocket = null;
            try {
                connectionSocket = welcomeSocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            WorkerThread wt = new WorkerThread(connectionSocket,id);
            Thread t = new Thread(wt);
            t.start();
            
            String text="Client [" + Integer.toString(id) + "] is now connected."+"\n";
            sshow.appendText(text);
            id++;
        }
    }
    
    class WorkerThread implements Runnable
    {
        private Socket connectionSocket;
        private int id;
        public WorkerThread(Socket ConnectionSocket, int id) 
        {
            this.connectionSocket=ConnectionSocket;
            this.id=id;
        }
        public void run()
        {
            String clientSentence;
            String capitalizedSentence;
            boolean flag=false;
            while(true)
            {
                try
                {
                    if (flag==true)break;
                   
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));    
                    DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                    clientSentence = inFromClient.readLine();
                    String text="Client [" + Integer.toString(id) + "] said: "+clientSentence+"\n";
                    sshow.appendText(text);
                    capitalizedSentence = clientSentence.toUpperCase();
                    outToClient.writeBytes(capitalizedSentence + '\n');

                }
                catch(Exception e)
                {
                    sshow.appendText("Client ["+Integer.toString(id)+"] has left\n");
                    flag=true;
                }
            }
        }
    }
};

