/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author daniel.constantin
 */
public class MainController implements Initializable {

      @FXML
    private SubScene subscene;

    @FXML
    private ToolBar toolBar;
    @FXML
    private Button btnLoadings;
    @FXML
    private Button btnOrders;
    @FXML
    private Button btnSettings;
    @FXML
    private Button btnLP;
    @FXML
    private Button btnTransporters;
    @FXML
    private Button btnSuppliers;
    @FXML
    private Button btnReports;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
          try {
              Parent  root = FXMLLoader.load(getClass().getResource("LP.fxml"));
              subscene.setRoot(root);
          } catch (IOException ex) {
              Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
          }
       
          
    }    

    @FXML
    private void handleButtonAction(ActionEvent event) {
    }
    
}
