/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase;



import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import transbase.entities.ComandaTransp;
import transbase.entities.TariISO;
import transbase.entities.controller.ComandaTranspJpaController;
import transbase.entities.controller.TariISOJpaController;


/**
 * FXML Controller class
 *
 * @author daniel.constantin
 */
public class LPController implements Initializable {
   @FXML
    private WebView webview;

    @FXML
    private TableView<ComandaTransp> tblLP;
     @FXML
    private TableColumn<ComandaTransp, Long> tcolCod;

    @FXML
    private TableColumn<ComandaTransp, String> tcolDenFz0;

    @FXML
    private TableColumn<ComandaTransp, String> tcolDenFz;

    @FXML
    private TableColumn<ComandaTransp, String> tcolDepozit;

    @FXML
    private TableColumn<ComandaTransp, String> tcolStr;

    @FXML
    private TableColumn<ComandaTransp, String> tcolCodOras;

    @FXML
    private TableColumn<ComandaTransp, String> tcolOras;
    @FXML
    private TableColumn<ComandaTransp, String> tcolTaraISO;
    @FXML
    private TableColumn<ComandaTransp, String> tcolTara;
    
    ObservableList<ComandaTransp> lst; 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         initCol();
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
         ComandaTranspJpaController controller = new ComandaTranspJpaController(emf);
         lst = FXCollections.observableArrayList(controller.findComandaTranspEntities());
         tblLP.setItems(lst);
         tblLP.setEditable(true);
        
         
         
        WebEngine we = webview.getEngine();
        /*
        we.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
             if (newValue == Worker.State.SUCCEEDED) {
            //JSObject window = (JSObject) we.executeScript("window");
            
            we.executeScript("var map;" +
        "var directionsManager;" +
        "function GetMap()" +
        "map = new Microsoft.Maps.Map('#myMap', {});" +
        "Microsoft.Maps.loadModule('Microsoft.Maps.Directions', function () {" +
        "directionsManager = new Microsoft.Maps.Directions.DirectionsManager(map);" +
        "var seattleWaypoint = new Microsoft.Maps.Directions.Waypoint({ address: 'Seattle, WA' });" +
        "directionsManager.addWaypoint(seattleWaypoint);" +
        "var workWaypoint = new Microsoft.Maps.Directions.Waypoint({ address: 'Work', location: new Microsoft.Maps.Location(47.64, -122.1297) });" +
        "directionsManager.addWaypoint(workWaypoint);" +
        "directionsManager.setRenderOptions({ itineraryContainer: '#directionsItinerary' });" +
        "directionsManager.calculateDirections();" +
        "});" +
        "}");
            
        }
            }
   
});
           
         */
            
        we.load("C:\\Users\\daniel.constantin\\Desktop\\googlemaps.html");
  
        //we.executeScript("setMapStart('"+"Bucharest"+"')");
    }  
    
    public void initCol(){
        tcolCod.setCellValueFactory(new PropertyValueFactory<>("cod"));
        tcolDenFz0.setCellValueFactory(new PropertyValueFactory<>("den_furnizor0"));
        tcolDenFz.setCellValueFactory(new PropertyValueFactory<>("den_furnizor"));
        tcolDepozit.setCellValueFactory(new PropertyValueFactory<>("depozit"));
        tcolStr.setCellValueFactory(new PropertyValueFactory<>("str"));
        tcolCodOras.setCellValueFactory(new PropertyValueFactory<>("cod_oras"));
        tcolOras.setCellValueFactory(new PropertyValueFactory<>("oras"));
        tcolTaraISO.setCellValueFactory(new PropertyValueFactory<>("tara"));
         tcolTaraISO.setVisible(false);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
         TariISOJpaController controller = new TariISOJpaController(emf);
         tcolTara.setCellValueFactory(new PropertyValueFactory<>("tara"));
         tcolTara.setCellFactory(ComboBoxTableCell.forTableColumn(controller.findTariISOString()));
        
    }
     private Object[] getTarilst(){
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
         TariISOJpaController controller = new TariISOJpaController(emf);
         
         ObservableList<TariISO> lstTari =  FXCollections.observableArrayList(controller.findTariISOEntities());
         Object[] tarile = lstTari.toArray();
         return tarile;
     } 
     
    public String getPlace(){
        return "\"Seattle, WA\"";
    }
}
