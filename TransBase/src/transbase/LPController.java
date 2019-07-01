/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.ParserConfigurationException;
import netscape.javascript.JSObject;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PrefixSelectionComboBox;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.table.TableFilter.Builder;
import static org.controlsfx.control.table.TableFilter.forTableView;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import transbase.entities.ComandaTransp;
import transbase.entities.FurnizoriClienti;
import transbase.entities.TariISO;
import transbase.entities.controller.ComandaTranspJpaController;
import transbase.entities.controller.FurnizoriClientiJpaController;
import transbase.entities.controller.TariISOJpaController;
import transbase.util.AutoCompleteComboBoxListener;
import transbase.util.GeocodeFetcher;


/**
 * FXML Controller class
 *
 * @author daniel.constantin
 */
public class LPController implements Initializable {
    static String lat;
    static String lon;
    static int webvpage;
    static ComandaTransp selectedLP;
    static int selRow;
     WebEngine we;
   @FXML
    private WebView webview;

    @FXML
    private FilteredTableView<ComandaTransp> tblLP;
     @FXML
    private TableColumn<ComandaTransp, Long> tcolCod;

 //   @FXML
//    private TableColumn<ComandaTransp, String> tcolDenFz0;

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
    @FXML
    private Pane bpMap;
    
    @FXML
    private Label lblNotvalid;
    @FXML
    private Label lbWarehouse;

    @FXML
    private TextField txtWarehause;

    @FXML
    private Label lbAddress;

    @FXML
    private TextField txtAddress;

    @FXML
    private Label lbPostalCode;

    @FXML
    private TextField txtPostalC;
    
     @FXML
    private Label lbCity;

    @FXML
    private TextField txtCity;

    @FXML
    private Label lbCountry;

    @FXML
    private Label lbSupplier;

  //  @FXML
  //  private ComboBox<TariISO> cmbCountry;

   // @FXML
 //   private ComboBox<FurnizoriClienti> cmbSupplier;
    @FXML
    private Button btnFilUpd;
      @FXML
    private Button btnSave;
     @FXML
    private Button btnUpdate;
      @FXML
    private Button btnDelete;
     @FXML
    private PrefixSelectionComboBox<TariISO> cmbCountry;
      @FXML
    private PrefixSelectionComboBox<FurnizoriClienti> cmbSupplier;
    TableFilter<ComandaTransp> tableFilter;
    ObservableList<ComandaTransp> lst; 
    ObservableList<TariISO> lstTari;
    ObservableList<FurnizoriClienti> lstFzCl;
    javax.swing.Timer tmrBanner;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webvpage=2;
       webview.setPrefSize( Double.MAX_VALUE, Double.MAX_VALUE );
         initCol();
     
         bindTrasnportersTable();
         getFurnizoriList();
         getTarilst();
        
            
        we = webview.getEngine();
       
    
        tblLP.setOnMouseClicked((MouseEvent event) -> {
        if (event.getClickCount() > 1) {
        onEdit();
        }else{
        
            try {
                selectedLP = tblLP.getSelectionModel().getSelectedItem();
                if(prepareMap(selectedLP.getStr(),selectedLP.getOras(), selectedLP.getCodOras(),selectedLP.getTara().getTara2L())){
                    
                }else{
                    lblNotvalid.setVisible(true);
                    lblNotvalid.setText("Location can't be Geocoded");
                    webview.setVisible(false);
                }   } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        });    
           
       

        
          ActionListener banner = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                

             //tableFilter.getTableView().getItems().clear();
            
             bindTrasnportersTable();
             
             
            
             System.out.println("bindTransp");
            }
        };
     tmrBanner = new javax.swing.Timer(0, banner);
     
     tmrBanner.start();
     tmrBanner.setDelay(5000);
        
        
    }  
    public void bindTrasnportersTable(){
        selRow = tblLP.getSelectionModel().getSelectedIndex();
        
         ComandaTranspJpaController controllerCT = new ComandaTranspJpaController(emf);
         lst = FXCollections.observableArrayList(controllerCT.findComandaTranspEntities());
          tblLP.getItems().clear();
         tblLP.setItems(lst);
         tblLP.getSelectionModel().select(selRow);
   
      
    }
    public void initCol(){
        tcolCod.setCellValueFactory(new PropertyValueFactory<>("cod"));
        tcolCod.setVisible(false);
     //   tcolDenFz0.setCellValueFactory(new PropertyValueFactory<>("denFurnizor0"));
        tcolDenFz.setCellValueFactory(new PropertyValueFactory<>("denFurnizor"));
        tcolDepozit.setCellValueFactory(new PropertyValueFactory<>("depozit"));
        tcolStr.setCellValueFactory(new PropertyValueFactory<>("str"));
        tcolCodOras.setCellValueFactory(new PropertyValueFactory<>("codOras"));
        tcolOras.setCellValueFactory(new PropertyValueFactory<>("oras"));
   //     tcolTaraISO.setCellValueFactory(new PropertyValueFactory<>("tara"));
        tcolTaraISO.setVisible(false);
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
         TariISOJpaController controller = new TariISOJpaController(emf);
    
         tcolTara.setCellValueFactory(new PropertyValueFactory<>("tara"));
         tcolTara.setCellFactory(ComboBoxTableCell.forTableColumn(controller.findTariISOString()));  
         tcolTaraISO.setCellValueFactory(new PropertyValueFactory<>("tara"));
         tcolTaraISO.setVisible(false);
         
          tcolDenFz.setSortable(false);
         tcolDepozit.setSortable(false);
         tcolStr.setSortable(false);
         tcolDepozit.setSortable(false);
         tcolCodOras.setSortable(false);
         tcolOras.setSortable(false);
         tcolTara.setSortable(false);
    }
    public void getTarilst(){
         
         TariISOJpaController controllerT = new TariISOJpaController(emf);
         
         lstTari =  FXCollections.observableArrayList(controllerT.findTariISOEntities());
         lstTari.sorted();
       //  Object[] tarile = lstTari.toArray();
         cmbCountry.setItems(lstTari);
 
     } 
    public void getFurnizoriList(){
         
         FurnizoriClientiJpaController controllerFC = new FurnizoriClientiJpaController(emf);
         
        lstFzCl =  FXCollections.observableArrayList(controllerFC.findFurnizoriClientiEntities());
        // Object[] FurnizClienti = lstFzCl.toArray();
         cmbSupplier.setItems(lstFzCl);
         
     } 
 
    private boolean prepareMap(String Street, String City, String postalC, String isoCountry) throws IOException{
        if (postalC!=null && !postalC.matches("\\d*$")) {
            postalC=(postalC.replaceAll("[^\\d]", ""));
        }        
         if(getGeolocation(Street, City, postalC, isoCountry)==false){
             
               lblNotvalid.setVisible(true);
               lblNotvalid.setText("Location can't be Geocoded");
               webview.setVisible(false);
           }else{
       
           lblNotvalid.setVisible(false);
           webview.setVisible(true);
    File htmlTemplateFile = new File("src/transbase/resources/maps.html");

    String htmlString = FileUtils.readFileToString(htmlTemplateFile, Charset.forName("UTF-8") );

    htmlString = htmlString.replace("$Lat", lat);
    htmlString = htmlString.replace("$Long", lon);
    URL urlHello;
   
    if(webvpage==2){
    File newHtmlFile = new File("src/transbase/resources/Mnew.html");
    FileUtils.writeStringToFile(newHtmlFile, htmlString,Charset.forName("UTF-8"));
    // urlHello = getClass().getResource("resources/Mnew.html");
    // System.out.println(urlHello.toString());
        we.load(newHtmlFile.toURI().toString());
      //  we.reload();
        webvpage=1;}
    else{
        File newHtmlFile2 = new File("src/transbase/resources/Mnew2.html");
        FileUtils.writeStringToFile(newHtmlFile2, htmlString,Charset.forName("UTF-8"));
      //  urlHello = getClass().getResource("resources/Mnew2.html");
        //System.out.println(urlHello.toString());
          we.load(newHtmlFile2.toURI().toString());
        //  we.reload();
            webvpage=2;
    }
     
           }
    return true;
    }
    public void onEdit() {
    // check the table's selected item and get selected item
    if (tblLP.getSelectionModel().getSelectedItem() != null) {
        selectedLP = tblLP.getSelectionModel().getSelectedItem();
      //  cmbSupplier.getSelectionModel(selectedLP.getCodFurnizor());
        txtCity.setText(selectedLP.getOras());
        txtPostalC.setText(selectedLP.getCodOras());
        txtAddress.setText(selectedLP.getStr());
        cmbSupplier.setValue(selectedLP.getCodFurnizor());
        cmbCountry.setValue(selectedLP.getTara());
        txtWarehause.setText(selectedLP.getDepozit());
    }
}
    private boolean getGeolocation(String Street, String City,String postalC,String isoCountry){
         try {
           String str = GeocodeFetcher.getLoc(Street, City, postalC, isoCountry);
       
           String[] split = str.split(",");
           System.out.println(split[0]);
           System.out.println(split[1]);
           lat = split[0];
           lon = split[1];
           return true;
       } catch (ParserConfigurationException ex) {
           Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
           return false;
       } catch (ParseException ex) {
           Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
           return false;
       }  
    }
    
    public void switchbtnFilUpd(){
        
        
        if(btnFilUpd.getText().equals("Switch to Filtered but NOT Updatable")){
        tableFilter = new TableFilter<>(tblLP);
         tmrBanner.stop();
           tcolDenFz.setSortable(true);
         tcolDepozit.setSortable(true);
         tcolStr.setSortable(true);
         tcolDepozit.setSortable(true);
         tcolCodOras.setSortable(true);
         tcolOras.setSortable(true);
         tcolTara.setSortable(true);
         
         btnFilUpd.setText("Switch to Updatable but NOT Filtered");   
         btnFilUpd.setStyle("-fx-background-color: #909396; -fx-background-radius: 40 40 40 40");
        }else{
         lst.removeAll(tblLP.getItems());
         tableFilter = null;
         tmrBanner.start();
         tmrBanner.setDelay(5000);
         btnFilUpd.setText("Switch to Filtered but NOT Updatable");
         btnFilUpd.setStyle("-fx-background-color: #5f6369; -fx-background-radius: 40 40 40 40");
         tcolDenFz.setSortable(false);
         tcolDepozit.setSortable(false);
         tcolStr.setSortable(false);
         tcolDepozit.setSortable(false);
         tcolCodOras.setSortable(false);
         tcolOras.setSortable(false);
         tcolTara.setSortable(false);
    }

        }
    
    public void ButtonHandler(ActionEvent ae){
        if(ae.getSource()==btnSave){
            if(cmbSupplier.valueProperty().get()==null || cmbCountry.valueProperty().get()==null || 
                    txtAddress.getText().isEmpty()||txtCity.getText().isEmpty() || txtPostalC.getText().isEmpty() ){
                Notifications.create() .title("Invalid Input") .text("All fields must be filled") .showWarning();
            } else{
               ComandaTransp ct= new ComandaTransp();
               ct.setCodFurnizor(cmbSupplier.getValue());
               ct.setCodOras(txtPostalC.getText());
               ct.setDenFurnizor(cmbSupplier.getValue().getDenumire());
               ct.setDepozit(txtWarehause.getText());
               ct.setOras(txtCity.getText());
               ct.setStr(txtAddress.getText());
               ct.setTara(cmbCountry.getValue());
            }
        }
        if(ae.getSource()==btnUpdate){
            
        }
        if(ae.getSource()==btnDelete){
            
        }
    }
    
}