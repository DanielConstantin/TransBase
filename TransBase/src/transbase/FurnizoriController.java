/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
import static transbase.LPController.lat;
import static transbase.LPController.webvpage;
import transbase.entities.ComandaTransp;
import transbase.entities.FurnizoriClienti;
import transbase.entities.Moneda;
import transbase.entities.TariISO;
import transbase.entities.controller.ComandaTranspJpaController;
import transbase.entities.controller.FurnizoriClientiJpaController;
import transbase.entities.controller.MonedaJpaController;
import transbase.entities.controller.TariISOJpaController;
import transbase.entities.controller.exceptions.NonexistentEntityException;
import transbase.util.GeocodeFetcher;

/**
 * FXML Controller class
 *
 * @author daniel.constantin
 */
public class FurnizoriController implements Initializable {

    @FXML
    private HBox hboxTop;
    @FXML
    private Button btnFilUpd;
    @FXML
    private Label lblSearch;
    @FXML
    private TextField txtSearch;
    @FXML
    private ImageView imgSearch;

    @FXML
    private TableColumn<FurnizoriClienti, String> tcolCodScala;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolDenumire;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolPers;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolTara;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolEmailResp;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolEmailPC;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolTransPredef;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolTipPredef;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolTPlata;
    @FXML
    private TableColumn<FurnizoriClienti, String> tcolMoneda;
    @FXML
    private HBox Htextf;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtSupplier;

    @FXML
    private TextField txtPersoana;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTPlata;

    @FXML
    private PrefixSelectionComboBox<Moneda> cmbCurrency;

    @FXML
    private PrefixSelectionComboBox<TariISO> cmbCountry;

    @FXML
    private TextField txtTipPred;

    @FXML
    private TextField txtTranspPredef;

    @FXML
    private TextField txtEmailResp;
 
       @FXML
    private Button btnDelete;        
    @FXML
    private Button btnSave;
    @FXML
    private Button btnClear;
  
    @FXML
    private Label lblNotvalid;
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
    private TableView<FurnizoriClienti> tview;  
    @FXML
    private TableView<ComandaTransp> tblLP;
     @FXML
    private TableColumn<ComandaTransp, Long> tcolCod;

    @FXML
    private TableColumn<ComandaTransp, String> tcolDepozit;

    @FXML
    private TableColumn<ComandaTransp, String> tcolStr;

    @FXML
    private TableColumn<ComandaTransp, String> tcolCodOras;
    
    @FXML
    private TableColumn<ComandaTransp, String> tcolOras;
    @FXML
    private HBox hboxlp;
    @FXML
    private WebView webview;
    Stage stageTheLabelBelongs;
    
    static FurnizoriClienti isSelectedFz;
    static ComandaTransp isSelectedLP;
    static int SelRowFz;
     TableFilter<FurnizoriClienti> tableFilter;
    ObservableList<FurnizoriClienti> lst; 
    ObservableList<ComandaTransp> lstLP; 
    javax.swing.Timer tmrBannerFurnizori;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
    ObservableList<TariISO> lstTari;
    ObservableList<Moneda> lstMoneda;
     static String lat;
    static String lon;
    static int webvpage;
    WebEngine we;
    /**
     * Initializes the controller class.
  */
    
public void ButtonHandler(MouseEvent ae){
        FurnizoriClientiJpaController controllerCT = new FurnizoriClientiJpaController(emf);
        if(ae.getSource()==btnSave && btnSave.getText().equalsIgnoreCase("Add")){
            if(txtSupplier.getText().isEmpty() || txtPersoana.getText().isEmpty() || 
                    txtId.getText().isEmpty()||txtEmail.getText().isEmpty() || txtTPlata.getText().isEmpty() ){
                
                Notifications.create() .title("Invalid Input") .text("All fields must be filled!") .darkStyle().position(Pos.CENTER).showWarning();
            } else{
               FurnizoriClienti ct= new FurnizoriClienti();
               ct.setCodScala(txtId.getText());
               ct.setDenumire(txtSupplier.getText());
               ct.setPersContact(txtPersoana.getText());
               ct.setPersContactemail(txtEmail.getText());
               ct.setPersRespemail(txtEmailResp.getText());
               ct.setTermenPl(Short.valueOf(txtTPlata.getText()));
               ct.setTara(cmbCountry.getValue());
               ct.setMoneda(cmbCurrency.getValue());
               //ct.setTipPredef(txtTranspPredef.getText());
             
              
                try {
                    controllerCT.create(ct);
                    bindFurnizoriClientiTable();
                } catch (Exception ex) {
                    Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
                    
                    Notifications.create() .title("Error Saving Record") .text("The record was not saved!") .darkStyle().position(Pos.CENTER).showWarning();
                }
            }
             ClearFields();
             
        }
        if(ae.getSource()==btnSave && btnSave.getText().equalsIgnoreCase("Update")){
            try {
              
             isSelectedFz.setPersContactemail(txtEmail.getText());
             isSelectedFz.setPersRespemail(txtEmailResp.getText());
             isSelectedFz.setCodScala(txtId.getText());
             isSelectedFz.setPersContact(txtPersoana.getText());
             isSelectedFz.setTermenPl(Short.valueOf(txtTPlata.getText()));
             isSelectedFz.setDenumire(txtSupplier.getText());
             isSelectedFz.setTipPredef(Integer.parseInt(txtTipPred.getText()));
             isSelectedFz.setTransPredef(txtTranspPredef.getText());
             isSelectedFz.setMoneda(cmbCurrency.getValue());
             isSelectedFz.setTara(cmbCountry.getValue());
                
                controllerCT.edit(isSelectedFz);
                bindFurnizoriClientiTable();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(FurnizoriController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(FurnizoriController.class.getName()).log(Level.SEVERE, null, ex);
            }
            ClearFields();
        }
         if(ae.getSource()==btnClear){
            ClearFields();
        }
        if(ae.getSource()==btnDelete){
            try {
                controllerCT.destroy(isSelectedFz.getCodScala());
                bindFurnizoriClientiTable();
            } catch(Error e){
                Notifications.create() .title("Error Deleting Record") .text("The record doesn't exist!") .showWarning();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(FurnizoriController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //    stageTheLabelBelongs = (Stage) menLP.getScene().getWindow();
    //    stageTheLabelBelongs.setMaximized(true);
         DropShadow ds = new DropShadow();
        ds.setColor(Color.DARKBLUE);
        ds.setHeight(40);
        ds.setWidth(120);
        ds.setSpread(.4);
       // menTransporters.setEffect(ds);
        imgSuppliers.setEffect(ds);
        webvpage=2;
        webview.setPrefSize( Double.MAX_VALUE, Double.MAX_VALUE );                  
        we = webview.getEngine();
         initCol();
         initColLP();
//        FurnizoriClientiJpaController controllerCT = new FurnizoriClientiJpaController(emf);
//        lst = FXCollections.observableArrayList(controllerCT.findFurnizoriClientiEntities());
     //   tview.setItems(lst);
        bindFurnizoriClientiTable();
          
//         filterData = new FilteredList<>(lst, e -> true);
//         tview.setItems(filterData);
         getMondeda();
         getTarilst();
         
//          tview.setFixedCellSize(18);
      
        
            
       
    
        tview.setOnMouseClicked((MouseEvent event) -> {
        if (event.getClickCount() > 1) {
        isSelectedFz=tview.getSelectionModel().getSelectedItem();
//        System.out.println(isSelectedFz.getCodScala());
        onEdit();
        
        }else{
        isSelectedFz=tview.getSelectionModel().getSelectedItem();
        bindPlaceByFz();
           
        }
        });    
         tblLP.setOnMouseClicked((MouseEvent ev) -> {
            if (ev.getClickCount() < 2) {
        isSelectedLP=tblLP.getSelectionModel().getSelectedItem();
      try {

                if(prepareMap(isSelectedLP.getStr(),isSelectedLP.getOras(), isSelectedLP.getCodOras(),isSelectedLP.getTara().getTara2L())){
                    
                }else{
              //      lblNotvalid.setVisible(true);
              //      lblNotvalid.setText("Location can't be Geocoded");
                    webview.setVisible(false);
                }   } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        }else{
                
            }
            });    
       

        
          ActionListener banner = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                

             //tableFilter.getTableView().getItems().clear();
            
             bindFurnizoriClientiTable();
             System.out.println("bindFZ");
             
            
          //   System.out.println("bindFurnizoriClienti");
            }
        };
     tmrBannerFurnizori = new javax.swing.Timer(0, banner);
     
     tmrBannerFurnizori.start();
     tmrBannerFurnizori.setDelay(20000);
        
        
    }  
    public void bindFurnizoriClientiTable(){
        if(txtSearch.getText().isEmpty() || txtSearch.equals("")){
          FurnizoriClientiJpaController controllerCT = new FurnizoriClientiJpaController(emf);
          lst = FXCollections.observableArrayList();
        if(lst.size()>0){
        lst = FXCollections.observableArrayList(controllerCT.findFurnizoriClientiEntities());
        SelRowFz = tview.getSelectionModel().getSelectedIndex();  
         tview.getItems().clear();
         filterData = new FilteredList<>(lst, e -> true);
         tview.setItems(filterData);
         tview.getSelectionModel().select(SelRowFz);
        }else{
          lst = FXCollections.observableArrayList(controllerCT.findFurnizoriClientiEntities());
//          tview.getItems().clear();
         filterData = new FilteredList<>(lst, e -> true);
         tview.setItems(filterData); 
        }
        }
    }
    
    public void bindPlaceByFz(){
       ComandaTranspJpaController controllerCT = new ComandaTranspJpaController(emf); 
//        lstLP =  (ObservableList<ComandaTransp>) controllerCT.getEntityManager().createNamedQuery("ComandaTransp.findByDenFurnizor")
//                .setParameter("denFurnizor", isSelectedFz.getDenumire())
//                .getResultList();
         
         lstLP =  FXCollections.observableArrayList(isSelectedFz.getComandaTranspList());
         tblLP.setItems(lstLP);
        
    }
    public void getTarilst(){
         
         TariISOJpaController controllerT = new TariISOJpaController(emf);
         
         lstTari =  FXCollections.observableArrayList(controllerT.findTariISOEntities());
         lstTari.sorted();
       //  Object[] tarile = lstTari.toArray();
         cmbCountry.setItems(lstTari);
     } 
        public void getMondeda(){
         
         MonedaJpaController controllerM = new MonedaJpaController(emf);
         
         lstMoneda =  FXCollections.observableArrayList(controllerM.findMonedaEntities());
         lstMoneda.sorted();
       //  Object[] tarile = lstTari.toArray();
         cmbCurrency.setItems(lstMoneda);
     } 
        public void initColLP(){
        tcolCod.setCellValueFactory(new PropertyValueFactory<>("cod"));
        tcolCod.setVisible(false);
        tcolDepozit.setCellValueFactory(new PropertyValueFactory<>("depozit"));
        tcolStr.setCellValueFactory(new PropertyValueFactory<>("str"));
        tcolCodOras.setCellValueFactory(new PropertyValueFactory<>("codOras"));
        tcolOras.setCellValueFactory(new PropertyValueFactory<>("oras"));
        tcolTara.setCellValueFactory(new PropertyValueFactory<>("tara"));
        tcolTara.setVisible(true);
        }
    public void initCol(){
       tcolCodScala.setCellValueFactory(new PropertyValueFactory<>("codScala"));
        tcolDenumire.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        tcolPers.setCellValueFactory(new PropertyValueFactory<>("persContact"));
        tcolEmailPC.setCellValueFactory(new PropertyValueFactory<>("persContactemail"));
        tcolEmailResp.setCellValueFactory(new PropertyValueFactory<>("persRespemail"));
        tcolMoneda.setCellValueFactory(new PropertyValueFactory<>("moneda"));      
        tcolTPlata.setCellValueFactory(new PropertyValueFactory<>("termenPl"));
        tcolTara.setCellValueFactory(new PropertyValueFactory<>("tara"));
        tcolTipPredef.setCellValueFactory(new PropertyValueFactory<>("tipPredef"));
         tcolTransPredef.setCellValueFactory(new PropertyValueFactory<>("transPredef"));
        
        tcolCodScala.setSortable(false);
        tcolDenumire.setSortable(false);
        tcolPers.setSortable(false);
        tcolEmailPC.setSortable(false);
        tcolEmailResp.setSortable(false);
        tcolMoneda.setSortable(false);
        tcolTPlata.setSortable(false);
        tcolTara.setSortable(false);
        tcolTipPredef.setSortable(false);
        tcolTransPredef.setSortable(false);
    }
 
    
    public void onEdit() {
    // check the table's selected item and get selected item
    if (tview.getSelectionModel().getSelectedItem() != null) {
      txtEmail.setText(isSelectedFz.getPersContactemail());
      txtEmailResp.setText(isSelectedFz.getPersRespemail());
      txtId.setText(isSelectedFz.getCodScala());
      txtPersoana.setText(isSelectedFz.getPersContact());
      txtTPlata.setText(String.valueOf(isSelectedFz.getTermenPl()));
      txtSupplier.setText(isSelectedFz.getDenumire());
      txtTipPred.setText(String.valueOf(isSelectedFz.getTipPredef()));
      txtTranspPredef.setText(isSelectedFz.getTransPredef());
      cmbCurrency.setValue(isSelectedFz.getMoneda());
      cmbCountry.setValue(isSelectedFz.getTara());
        btnSave.setText("Update");
    }
}
    private FilteredList<FurnizoriClienti> filterData;
    public void onSearch(){
        txtSearch.textProperty().addListener((o, ov, nv)->{
         filterData.setPredicate((FurnizoriClienti fz)->{
             String newVal = nv.toLowerCase();
             return fz.getDenumire().toLowerCase().contains(newVal);
                 });
        
        });
        tview.setItems(filterData);
    }
   
//    public void switchbtnFilUpd(){
//        
//        
//        if(btnFilUpd.getText().equals("Switch to Filtered but NOT Updatable")){
//        tableFilter = new TableFilter<>(tview);
//         tmrBannerFurnizori.stop();
//         tcolCompanie.setSortable(true);
//         tcolPers.setSortable(true);
//         tcolTel.setSortable(true);
//         tcolTermenPl.setSortable(true);
//         tcolFax.setSortable(true);
//         tcolEmail.setSortable(true);
//         
//         
//         btnFilUpd.setText("Switch to Updatable but NOT Filtered");   
//         btnFilUpd.setStyle("-fx-background-color: #909396; -fx-background-radius: 40 40 40 40");
//        }else{
//         lst.removeAll(tview.getItems());
//         tableFilter = null;
//         tmrBannerFurnizori.start();
//         tmrBannerFurnizori.setDelay(20000);
//         btnFilUpd.setText("Switch to Filtered but NOT Updatable");
//         btnFilUpd.setStyle("-fx-background-color: #5f6369; -fx-background-radius: 40 40 40 40");
//         tcolCompanie.setSortable(false);
//         tcolPers.setSortable(false);
//         tcolTel.setSortable(false);
//         tcolTermenPl.setSortable(false);
//         tcolFax.setSortable(false);
//         tcolEmail.setSortable(false);
//        
//    }

//        }
    
    
    
    private void ClearFields(){
        btnSave.setText("Add");
       txtEmail.setText("");
      txtEmailResp.setText("");
      txtId.setText("");
      txtPersoana.setText("");
      txtTPlata.setText("");
      txtSupplier.setText("");
      txtTipPred.setText("");
      txtTranspPredef.setText("");
      cmbCurrency.setValue(null);
      cmbCountry.setValue(null);
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
       
//           lblNotvalid.setVisible(false);
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
    
    
    @FXML
    private void menuHandler(MouseEvent e) {
               
        stageTheLabelBelongs = (Stage) menLP.getScene().getWindow();
       if((e.getSource()==imgLP || e.getSource()==menLP)&& !this.getClass().getName().equals("transbase.LPController") ){
            try {
                Parent root  = FXMLLoader.load(getClass().getResource("LP.fxml")); 
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerFurnizori.stop();
                stageTheLabelBelongs.setScene(sc);
                
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       if((e.getSource()==imgTransporters || e.getSource()==menTransporters)&& !this.getClass().getName().equals("transbase.ContacteController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Contacte.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerFurnizori.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgSuppliers || e.getSource()==menSuppliers)&& !this.getClass().getName().equals("transbase.FurnizoriController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Furnizori.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
               
                stageTheLabelBelongs.setScene(sc);
                
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgSettings || e.getSource()==menSettings)&& !this.getClass().getName().equals("transbase.SettingsController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Settings.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerFurnizori.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgLoadings || e.getSource()==menLoadings)&& !this.getClass().getName().equals("transbase.LoadingsController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Loadings.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerFurnizori.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgReports || e.getSource()==menReports)&& !this.getClass().getName().equals("transbase.ReportsController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Reports.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerFurnizori.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
    
}
