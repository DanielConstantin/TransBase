/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transbase;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.table.TableFilter;
import org.controlsfx.control.tableview2.FilteredTableView;
import transbase.entities.Contacte;
import transbase.entities.controller.ContacteJpaController;
import transbase.entities.controller.exceptions.NonexistentEntityException;

/**
 * FXML Controller class
 *
 * @author daniel.constantin
 */
public class ContacteController implements Initializable {

    static Contacte isSelected;
    static int selRow;

 
    @FXML
    private FilteredTableView<Contacte> tblContacte;
     @FXML
    private TableColumn<Contacte, Long> tcolId;

 //   @FXML
//    private TableColumn<Contacte, String> tcolCompanie0;

    @FXML
    private TableColumn<Contacte, String> tcolCompanie;

    @FXML
    private TableColumn<Contacte, String> tcolPers;

    @FXML
    private TableColumn<Contacte, String> tcolTel;

    @FXML
    private TableColumn<Contacte, String> tcolFax;
    
    @FXML
    private TableColumn<Contacte, String> tcolEmail;
    @FXML
    private TableColumn<Contacte, Integer> tcolTermenPl;
//    @FXML
//    private TableColumn<Contacte, String> tcolTara;
   
    
//    @FXML
//    private Label lblNotvalid;
    @FXML
    private Label lblPersoana;

    @FXML
    private TextField txtPersoana;

    @FXML
    private Label lbTel;

    @FXML
    private TextField txtTel;

    @FXML
    private Label lblFax;

    @FXML
    private TextField txtFax;
    
     @FXML
    private Label lblEmail;

    @FXML
    private TextField txtEmail;
     @FXML
    private Label lblTermenPl;

    @FXML
    private TextField txtTermenPl;
    @FXML
    private Label lblCompanie;
    @FXML
    private TextField txtCompanie;
//    @FXML
//    private Label lbSupplier;
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
    
    TableFilter<Contacte> tableFilter;
    ObservableList<Contacte> lst; 
    javax.swing.Timer tmrBannerContacte;
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("TransBasePU");
    Stage stageTheLabelBelongs;
    /**
     * Initializes the controller class.
     */
    
    
    public void ButtonHandler(MouseEvent ae){
        ContacteJpaController controllerCT = new ContacteJpaController(emf);
        if(ae.getSource()==btnSave && btnSave.getText().equalsIgnoreCase("Add")){
            if(txtCompanie.getText().isEmpty() || txtPersoana.getText().isEmpty() || 
                    txtTel.getText().isEmpty()||txtEmail.getText().isEmpty() || txtTermenPl.getText().isEmpty() ){
                
                Notifications.create() .title("Invalid Input") .text("All fields must be filled!") .darkStyle().position(Pos.CENTER).showWarning();
            } else{
               Contacte ct= new Contacte();
               ct.setCompanie(txtCompanie.getText());
               ct.setNume(txtPersoana.getText());
               ct.setTel(txtTel.getText());
               ct.setEmail(txtEmail.getText());
               ct.setFax(txtFax.getText());
               ct.setTermenPl(Integer.valueOf(txtTermenPl.getText()));
             
              
                try {
                    controllerCT.create(ct);
                    bindContacteTable();
                } catch (Exception ex) {
                    Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
                    
                    Notifications.create() .title("Error Saving Record") .text("The record was not saved!") .darkStyle().position(Pos.CENTER).showWarning();
                }
            }
             ClearFields();
             
        }
        if(ae.getSource()==btnSave && btnSave.getText().equalsIgnoreCase("Update")){
            try {
               isSelected.setCompanie(txtCompanie.getText());
               isSelected.setNume(txtPersoana.getText());
               isSelected.setTel(txtTel.getText());
               isSelected.setEmail(txtEmail.getText());
               isSelected.setFax(txtFax.getText());
               isSelected.setTermenPl(Integer.valueOf(txtTermenPl.getText()));
                
                controllerCT.edit(isSelected);
                bindContacteTable();
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
                controllerCT.destroy(isSelected.getIdPersoanaCont());
                bindContacteTable();
            } catch(Error e){
                Notifications.create() .title("Error Deleting Record") .text("The record doesn't exist!") .showWarning();
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ContacteController.class.getName()).log(Level.SEVERE, null, ex);
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
        imgTransporters.setEffect(ds);
        
         initCol();
         
         bindContacteTable();
       
      
         btnSwitch.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            
            
        if(newValue==true){
        tableFilter = new TableFilter<>(tblContacte);
         tmrBannerContacte.stop();
         tcolCompanie.setSortable(true);
         tcolPers.setSortable(true);
         tcolTel.setSortable(true);
         tcolTermenPl.setSortable(true);
         tcolFax.setSortable(true);
         tcolEmail.setSortable(true);
         tblContacte.getStylesheets().add(getClass().getResource("resources/tableview 1.css").toExternalForm());

         lblUpdatable.setStyle("-fx-font-weight: normal");
         lblFiltrable.setStyle("-fx-font-weight: bold");

    //     btnFilUpd.setText("Switch to Updatable but NOT Filtered");   
    //     btnFilUpd.setStyle("-fx-background-color: #909396; -fx-background-radius: 40 40 40 40");
        }else {
         lst.removeAll(tblContacte.getItems());
         tableFilter = null;
         bindContacteTable();
         tmrBannerContacte.start();
         tmrBannerContacte.setDelay(20000);

         tcolCompanie.setSortable(false);
         tcolPers.setSortable(false);
         tcolTel.setSortable(false);
         tcolTermenPl.setSortable(false);
         tcolFax.setSortable(false);
         tcolEmail.setSortable(false);
         tblContacte.getStylesheets().remove( tblContacte.getStylesheets().size()-1);
         lblUpdatable.setStyle("-fx-font-weight: bold");
         lblFiltrable.setStyle("-fx-font-weight: normal");
    }
}));
        
            
       
    
        tblContacte.setOnMouseClicked((MouseEvent event) -> {
        if (event.getClickCount() > 1) {
        isSelected=tblContacte.getSelectionModel().getSelectedItem();
        onEdit();
        
        }else{
        
           
        }
        });    
           
       

        
          ActionListener banner = new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                

             //tableFilter.getTableView().getItems().clear();
            
             bindContacteTable();
             
             System.out.println("bindContacte");
            
          //   System.out.println("bindContacte");
            }
        };
     tmrBannerContacte = new javax.swing.Timer(0, banner);
     
     tmrBannerContacte.start();
     tmrBannerContacte.setDelay(20000);
        
        
    }  
    public void bindContacteTable(){
        selRow = tblContacte.getSelectionModel().getSelectedIndex();
        
         ContacteJpaController controllerCT = new ContacteJpaController(emf);
         lst = FXCollections.observableArrayList(controllerCT.findContacteEntities());
          tblContacte.getItems().clear();
         tblContacte.setItems(lst);
         tblContacte.getSelectionModel().select(selRow);
   
      
    }
    public void initCol(){
        tcolId.setCellValueFactory(new PropertyValueFactory<>("id_persoana_cont"));
        tcolId.setVisible(false);
     //   tcolCompanie0.setCellValueFactory(new PropertyValueFactory<>("denFurnizor0"));
        tcolCompanie.setCellValueFactory(new PropertyValueFactory<>("companie"));
        tcolPers.setCellValueFactory(new PropertyValueFactory<>("nume"));
        tcolTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        tcolFax.setCellValueFactory(new PropertyValueFactory<>("fax"));
        tcolEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
       tcolTermenPl.setCellValueFactory(new PropertyValueFactory<>("termenPl"));
     //   tcolTermenPl.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTermenPl()).asObject());
       // tcolTaraISO.setVisible(false);
        
         
         tcolCompanie.setSortable(false);
         tcolPers.setSortable(false);
         tcolTel.setSortable(false);
         tcolPers.setSortable(false);
         tcolFax.setSortable(false);
         tcolEmail.setSortable(false);
         tcolTermenPl.setSortable(false);
         
    }
 
    
    public void onEdit() {
    // check the table's selected item and get selected item
    if (tblContacte.getSelectionModel().getSelectedItem() != null) {
      //  selectedLP = tblContacte.getSelectionModel().getSelectedItem();
      //  cmbSupplier.getSelectionModel(selectedLP.getCodFurnizor());
        txtCompanie.setText(isSelected.getCompanie());
        txtPersoana.setText(isSelected.getNume());
        txtTel.setText(isSelected.getTel());
        txtFax.setText(isSelected.getFax());
        txtEmail.setText(isSelected.getEmail());
        txtTermenPl.setText(String.valueOf(isSelected.getTermenPl()));
        btnSave.setText("Update");
    }
}
   
//    public void switchbtnFilUpd(){
//        
//        
//        if(btnFilUpd.getText().equals("Switch to Filtered but NOT Updatable")){
//        tableFilter = new TableFilter<>(tblContacte);
//         tmrBannerContacte.stop();
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
//         lst.removeAll(tblContacte.getItems());
//         tableFilter = null;
//         tmrBannerContacte.start();
//         tmrBannerContacte.setDelay(20000);
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
        tcolCompanie.setText("");
         tcolPers.setText("");
         tcolTel.setText("");
         tcolTermenPl.setText("");
         tcolFax.setText("");
         tcolEmail.setText("");
        
    }
    @FXML
    public void menuHandler(MouseEvent e){
     System.out.println(this.getClass().getName());        
   stageTheLabelBelongs = (Stage) menLP.getScene().getWindow();
       if((e.getSource()==imgLP || e.getSource()==menLP)&& !this.getClass().getName().equals("transbase.LPController") ){
            try {
                Parent root  = FXMLLoader.load(getClass().getResource("LP.fxml")); 
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerContacte.stop();
                stageTheLabelBelongs.setScene(sc);
                
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       if((e.getSource()==imgTransporters || e.getSource()==menTransporters)&& !this.getClass().getName().equals("transbase.ContacteController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Contacte.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgSuppliers || e.getSource()==menSuppliers)&& !this.getClass().getName().equals("transbase.FurnizoriController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Furnizori.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerContacte.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgSettings || e.getSource()==menSettings)&& !this.getClass().getName().equals("transbase.SettingsController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Settings.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerContacte.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgLoadings || e.getSource()==menLoadings)&& !this.getClass().getName().equals("transbase.LoadingsController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Loadings.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerContacte.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
       if((e.getSource()==imgReports || e.getSource()==menReports)&& !this.getClass().getName().equals("transbase.ReportsController") ){
            try {              
                Parent root  = FXMLLoader.load(getClass().getResource("Reports.fxml"));
               
                Scene sc = new Scene(root, stageTheLabelBelongs.getWidth(), stageTheLabelBelongs.getHeight());
                tmrBannerContacte.stop();
                stageTheLabelBelongs.setScene(sc);
            } catch (IOException ex) {
                Logger.getLogger(LPController.class.getName()).log(Level.SEVERE, null, ex);
            }
       }
    }
}
