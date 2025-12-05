package com.gestion.etu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class MainController {

    @FXML
    private TextField idField;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private DatePicker dateEmbauchePicker;
    @FXML
    private TextField posteField;
    @FXML
    private TextField salaireField;
    @FXML
    private TextField serviceField;
    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> filterComboBox;

    @FXML
    private TableView<Employe> employesTable;
    @FXML
    private TableColumn<Employe, Integer> idColumn;
    @FXML
    private TableColumn<Employe, String> nomColumn;
    @FXML
    private TableColumn<Employe, String> prenomColumn;
    @FXML
    private TableColumn<Employe, LocalDate> dateEmbaucheColumn;
    @FXML
    private TableColumn<Employe, String> posteColumn;
    @FXML
    private TableColumn<Employe, Double> salaireColumn;
    @FXML
    private TableColumn<Employe, String> serviceColumn;

    private EmployeDAO employeDAO;
    private ObservableList<Employe> employes;

    @FXML
    public void initialize() {
        employeDAO = new EmployeDAO();

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        dateEmbaucheColumn.setCellValueFactory(new PropertyValueFactory<>("dateEmbauche"));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        salaireColumn.setCellValueFactory(new PropertyValueFactory<>("salaire"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));
        
        filterComboBox.setItems(FXCollections.observableArrayList("Service", "Poste"));

        loadEmployes();
    }

    private void loadEmployes() {
        List<Employe> employeList = employeDAO.getEmployes();
        employes = FXCollections.observableArrayList(employeList);
        employesTable.setItems(employes);
    }

    @FXML
    private void addEmploye() {
        Employe newEmploye = new Employe(
                Integer.parseInt(idField.getText()),
                nomField.getText(),
                prenomField.getText(),
                dateEmbauchePicker.getValue(),
                posteField.getText(),
                Double.parseDouble(salaireField.getText()),
                serviceField.getText()
        );
        employeDAO.addEmploye(newEmploye);
        loadEmployes();
        clearFields();
    }

    @FXML
    private void updateEmploye() {
        Employe selectedEmploye = employesTable.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            selectedEmploye.setNom(nomField.getText());
            selectedEmploye.setPrenom(prenomField.getText());
            selectedEmploye.setDateEmbauche(dateEmbauchePicker.getValue());
            selectedEmploye.setPoste(posteField.getText());
            selectedEmploye.setSalaire(Double.parseDouble(salaireField.getText()));
            selectedEmploye.setService(serviceField.getText());
            employeDAO.updateEmploye(selectedEmploye);
            loadEmployes();
            clearFields();
        }
    }

    @FXML
    private void deleteEmploye() {
        Employe selectedEmploye = employesTable.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            employeDAO.deleteEmploye(selectedEmploye.getId());
            loadEmployes();
            clearFields();
        }
    }

    @FXML
    private void searchEmploye() {
        String searchTerm = searchField.getText();
        if (searchTerm != null && !searchTerm.isEmpty()) {
            List<Employe> searchResult = employeDAO.searchEmploye(searchTerm);
            employes = FXCollections.observableArrayList(searchResult);
            employesTable.setItems(employes);
        } else {
            loadEmployes();
        }
    }

    @FXML
    private void filterEmployes() {
        String filterBy = filterComboBox.getSelectionModel().getSelectedItem();
        String filterValue = searchField.getText(); // Using the same search field for filter value

        if ("Service".equals(filterBy)) {
            List<Employe> filteredList = employeDAO.getEmployesByService(filterValue);
            employes = FXCollections.observableArrayList(filteredList);
            employesTable.setItems(employes);
        } else if ("Poste".equals(filterBy)) {
            List<Employe> filteredList = employeDAO.getEmployesByPoste(filterValue);
            employes = FXCollections.observableArrayList(filteredList);
            employesTable.setItems(employes);
        }
    }

    private void clearFields() {
        idField.clear();
        nomField.clear();
        prenomField.clear();
        dateEmbauchePicker.setValue(null);
        posteField.clear();
        salaireField.clear();
        serviceField.clear();
    }

    @FXML
    private void handleRowSelect() {
        Employe selectedEmploye = employesTable.getSelectionModel().getSelectedItem();
        if (selectedEmploye != null) {
            idField.setText(String.valueOf(selectedEmploye.getId()));
            nomField.setText(selectedEmploye.getNom());
            prenomField.setText(selectedEmploye.getPrenom());
            dateEmbauchePicker.setValue(selectedEmploye.getDateEmbauche());
            posteField.setText(selectedEmploye.getPoste());
            salaireField.setText(String.valueOf(selectedEmploye.getSalaire()));
            serviceField.setText(selectedEmploye.getService());
        }
    }
}
