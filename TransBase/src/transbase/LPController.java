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
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.PrefixSelectionComboBox;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.tableview2.FilteredTableView;
import org.json.simple.parser.ParseException;
import transbase.entities.ComandaTransp;
import transbase.entities.FurnizoriClienti;
import transbase.entities.TariISO;
import transbase.entities.controller.ComandaTranspJpaController;
import transbase.entities.controller.FurnizoriClientiJpaController;
import transbase.entities.controller.TariISOJpaController;
import transbase.entities.controller.exceptions.IllegalOrphanException;
import transbase.entities.controller.exceptions.NonexistentEntityException;
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
    static ComandaTransp isSelected;
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
//    @FXML
//    private TableColumn<ComandaTransp, String> tcolTaraISO;
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
    @FXML
    private Label lbSearch;
    @FXML
    private TextField txtSearch;
    @FXML
    private ImageView imgSearch;
      @FXML
    private Button btnSave;
     @FXML
    private Button btnClear;
      @FXML
    private Button btnDelete;
     @FXML
    private PrefixSelectionComboBox<TariISO> cmbCountry;
      @FXML
    private PrefixSelectionComboBox<FurnizoriClienti> cmbSupplier;
   @FXML
    private Pane menPane;

    @FXML
    private ImageView imgLoadings;

    @FXML
    private Label menLoadings;

    @FXML
    private ImageView imgSuppliers;

    @FXML
    private Label menSuppliers;

    @FXML
    private ImageView imgLP;

    @FXML
    private Label menLP;

    @FXML
    private ImageView imgTransporters;

    @FXML
    private Label menTransporters;

    @FXML
    private ImageView imgOrders;

    @FXML
    private Label menOrders;

    @FXML
    private ImageView imgSettings;

    @FXML
    private Label menSettings;

    @FXML
    private ImageView imgReports;

    @FXML
    private Label menReports;
    @FXML
    private VBox vbox;
     @FXML
    private ToggleSwitch btnSwitch;
    @FXML
    private Label lblUpdatable;
    @FXML
    private Label lblFiltrable;
    TableFilter<ComandaTransp> tableFilter;
    ObservableList<ComandaTransp> lst; 
    ObservableList<TariISO> lstTari;
    ObservableList<FurnizoriClienti> lstFzCl;
    javax.swing.Timer tmrBannerLP;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
    Stage stageTheLabelBelongs;
    /**
     * Initializes the controller class.
     */
    
    @FXML
    public void ButtonHandler(MouseEvent ae){
        ComandaTranspJpaController controllerCT = new ComandaTranspJpaController(emf);
        if(ae.getSource()==btnSave && btnSave.getText().equalsIgnoreCase("Add")){
            if(cmbSupplier.valueProperty().get()==null || cmbCountry.valueProperty().get()==null || 
                    txtAddress.getText().isEmpty()||txtCity.getText().isEmpty() || txtPostalC.getText().isEmpty() ){
                
                Notifications.create() .title("Invalid Input") .text("All fields must be filled!") .darkStyle().position(Pos.CENTER).showWarning();
            } else{
               ComandaTransp ct= new ComandaTransp();
               ct.setCodFurnizor(cmbSupplier.getValue());
               ct.setCodOras(txtPostalC.getText());
               ct.setDenFurnizor(cmbSupplier.getValue().getDenumire());
               ct.setDepozit(txtWarehause.getText());
               ct.setOras(txtCity.getText());
               ct.setStr(txtAddress.getText());
               ct.setTara(cmbCountry.getValue());
              
                try {
                    controllerCT.create(ct);
                    bindTrasnportersTable();
                } catch (Exception ex) {
                    Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
                    
                    Notifications.create() .title("Error Saving Record") .text("The record was not saved!") .darkStyle().position(Pos.CENTER).showWarning();
                }
            }
             ClearFields();
             
        }
        if(ae.getSource()==btnSave && btnSave.getText().equalsIgnoreCase("Update")){
            try {
                isSelected.setCodFurnizor(cmbSupplier.getValue());
                isSelected.setCodOras(txtPostalC.getText());
               isSelected.setDenFurnizor(cmbSupplier.getValue().getDenumire());
               isSelected.setDepozit(txtWarehause.getText());
               isSelected.setOras(txtCity.getText());
               isSelected.setStr(txtAddress.getText());
               isSelected.setTara(cmbCountry.getValue());
                
                controllerCT.edit(isSelected);
                bindTrasnportersTable();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ClearFields();
        }
         if(ae.getSource()==btnClear){
            ClearFields();
        }
        if(ae.getSource()==btnDelete){
            try {
                controllerCT.destroy(isSelected.getCod());
                bindTrasnportersTable();
            } catch (IllegalOrphanException ex) {
                Notifications.create() .title("Error Deleting Record") .text("The record can't be deleted!") .showWarning();
            } catch (NonexistentEntityException ex) {
                Notifications.create() .title("Error Deleting Record") .text("The record doesn't exist!") .showWarning();
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
  
        DropShadow ds = new DropShadow();
         ds.setColor(Color.DARKBLUE);
        ds.setHeight(40);
        ds.setWidth(120);
        ds.setSpread(.4);
        imgLP.setEffect(ds);
        
        webvpage=2;
        webview.setPrefSize( Double.MAX_VALUE, Double.MAX_VALUE );
         initCol();
        
         bindTrasnportersTable();
         getFurnizoriList();
         getTarilst();
                   
        we = webview.getEngine();
       
        btnSwitch.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            
            
         if(newValue==true) {
        tableFilter = new TableFilter<>(tblLP);
         tmrBannerLP.stop();
           tcolDenFz.setSortable(true);
         tcolDepozit.setSortable(true);
         tcolStr.setSortable(true);
         tcolDepozit.setSortable(true);
         tcolCodOras.setSortable(true);
         tcolOras.setSortable(true);
         tcolTara.setSortable(true);
         tblLP.getStylesheets().add(getClass().getResource("resources/tableview 1.css").toExternalForm());
         lblUpdatable.setStyle("-fx-font-weight: normal");
         lblFiltrable.setStyle("-fx-font-weight: bold");

    //     btnFilUpd.setText("Switch to Updatable but NOT Filtered");   
    //     btnFilUpd.setStyle("-fx-background-color: #909396; -fx-background-radius: 40 40 40 40");
        }if(newValue == false){
         lst.removeAll(tblLP.getItems());
         tableFilter = null;
         bindTrasnportersTable(); 
         tmrBannerLP.start();
         tmrBannerLP.setDelay(5000);
         tcolDenFz.setSortable(false);
         tcolDepozit.setSortable(false);
         tcolStr.setSortable(false);
         tcolDepozit.setSortable(false);
         tcolCodOras.setSortable(false);
         tcolOras.setSortable(false);
         tcolTara.setSortable(false);
         tblLP.getStylesheets().remove( tblLP.getStylesheets().size()-1);
         lblUpdatable.setStyle("-fx-font-weight: bold");
         lblFiltrable.setStyle("-fx-font-weight: normal");
    }
}));
           
        tblLP.setOnMouseClicked((MouseEvent event) -> {
        if (event.getClickCount() > 1) {
        isSelected=tblLP.getSelectionModel().getSelectedItem();
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
            
             System.out.println("bindLP");
            }
        };
     tmrBannerLP = new javax.swing.Timer(0, banner);
     
     tmrBannerLP.start();
     tmrBannerLP.setDelay(8000);
        
        
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
    //    tcolTaraISO.setVisible(false);
         EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
         TariISOJpaController controller = new TariISOJpaController(emf);
    
     
         tcolTara.setCellValueFactory(new PropertyValueFactory<>("tara"));
         tcolTara.setVisible(true);
         
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
      //  selectedLP = tblLP.getSelectionModel().getSelectedItem();
      //  cmbSupplier.getSelectionModel(selectedLP.getCodFurnizor());
        txtCity.setText(isSelected.getOras());
        txtPostalC.setText(isSelected.getCodOras());
        txtAddress.setText(isSelected.getStr());
        cmbSupplier.setValue(isSelected.getCodFurnizor());
        cmbCountry.setValue(isSelected.getTara());
        txtWarehause.setText(isSelected.getDepozit());
        btnSave.setText("Update");
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
    

    
    
    
    private void ClearFields(){
        btnSave.setText("Add");
        cmbSupplier.setValue(null);
        txtAddress.setText("");
        txtPostalC.setText("");
        txtCity.setText("");
        cmbCountry.setValue(null);
        txtWarehause.setText("");
        
    }

   
  
    public void menuHandler(MouseEvent e){
       
        stageTheLabelBelongs = (Stage) menLP.getScene().getWindow();
       if((e.getSource()==imgLP || e.getSource()==menLP)&& !this.getClass().getName().equals("transbase.LPController") ){
            try {
                Parent root  = FXMLLoader.load(getClass().getResource("LP.fxml")); 
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                
                stageTheLabelBelongs.setScene(sc);
                
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       if((e.getSource()==imgTransporters || e.getSource()==menTransporters)&& !this.getClass().getName().equals("transbase.Contacte") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Contacte.fxml"));
               tmrBannerLP.stop();
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgSuppliers || e.getSource()==menSuppliers)&& !this.getClass().getName().equals("transbase.Furnizori") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Furnizori.fxml"));
               tmrBannerLP.stop();
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerLP.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgSettings || e.getSource()==menSettings)&& !this.getClass().getName().equals("transbase.Settings") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Settings.fxml"));
               tmrBannerLP.stop();
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerLP.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgLoadings || e.getSource()==menLoadings)&& !this.getClass().getName().equals("transbase.Loadings") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Loadings.fxml"));
               tmrBannerLP.stop();
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerLP.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgReports || e.getSource()==menReports)&& !this.getClass().getName().equals("transbase.Reports") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Reports.fxml"));
             
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerLP.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
}