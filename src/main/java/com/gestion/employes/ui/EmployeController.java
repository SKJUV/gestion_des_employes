package com.gestion.employes.ui;

import com.gestion.employes.dao.EmployeDAO;
import com.gestion.employes.model.Employe;
import com.gestion.employes.model.Role;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

public class EmployeController extends JFrame {

    private final EmployeDAO employeDAO;
    private DefaultTableModel tableModel;
    private JTable table;

    private JTextField nomField;
    private JTextField prenomField;
    private JTextField dateField;
    private JTextField posteField;
    private JTextField salaireField;
    private JTextField serviceField;
    private JComboBox<Role> roleComboBox;

    private JTextField searchNomField;
    private JTextField searchIdField;
    private JTextField filterServiceField;
    private JTextField filterPosteField;

    public EmployeController(EmployeDAO employeDAO) {
        this.employeDAO = employeDAO;
        setTitle("Gestion des employes - Swing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        initComponents();
        refreshRoles();
        loadEmployes(employeDAO.listerTous());
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        initTable();
        initFilters();
        initForm();
    }

    private void initTable() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nom", "Prenom", "Date embauche", "Poste", "Salaire", "Service", "Role"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void initFilters() {
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchNomField = new JTextField(12);
        JButton searchNomBtn = new JButton("Rechercher nom");
        searchNomBtn.addActionListener(e -> handleRechercherParNom());

        searchIdField = new JTextField(5);
        JButton searchIdBtn = new JButton("Rechercher ID");
        searchIdBtn.addActionListener(e -> handleRechercherParId());

        filterServiceField = new JTextField(10);
        JButton filterServiceBtn = new JButton("Filtrer service");
        filterServiceBtn.addActionListener(e -> handleFiltrerService());

        filterPosteField = new JTextField(10);
        JButton filterPosteBtn = new JButton("Filtrer poste");
        filterPosteBtn.addActionListener(e -> handleFiltrerPoste());

        JButton afficherTousBtn = new JButton("Afficher tous");
        afficherTousBtn.addActionListener(e -> loadEmployes(employeDAO.listerTous()));

        filters.add(new JLabel("Nom/Prenom :"));
        filters.add(searchNomField);
        filters.add(searchNomBtn);
        filters.add(new JLabel("ID :"));
        filters.add(searchIdField);
        filters.add(searchIdBtn);
        filters.add(new JLabel("Service :"));
        filters.add(filterServiceField);
        filters.add(filterServiceBtn);
        filters.add(new JLabel("Poste :"));
        filters.add(filterPosteField);
        filters.add(filterPosteBtn);
        filters.add(afficherTousBtn);

        add(filters, BorderLayout.NORTH);
    }

    private void initForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomField = new JTextField(15);
        prenomField = new JTextField(15);
        dateField = new JTextField(10);
        posteField = new JTextField(12);
        salaireField = new JTextField(10);
        serviceField = new JTextField(12);
        roleComboBox = new JComboBox<>();

        addLabeledField(formPanel, gbc, 0, "Nom", nomField);
        addLabeledField(formPanel, gbc, 1, "Prenom", prenomField);
        addLabeledField(formPanel, gbc, 2, "Date embauche (yyyy-MM-dd)", dateField);
        addLabeledField(formPanel, gbc, 3, "Poste", posteField);
        addLabeledField(formPanel, gbc, 4, "Salaire", salaireField);
        addLabeledField(formPanel, gbc, 5, "Service", serviceField);
        addLabeledField(formPanel, gbc, 6, "Role", roleComboBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton ajouterBtn = new JButton("Ajouter");
        ajouterBtn.addActionListener(e -> handleAjouter());
        JButton modifierBtn = new JButton("Modifier");
        modifierBtn.addActionListener(e -> handleModifier());
        JButton supprimerBtn = new JButton("Supprimer");
        supprimerBtn.addActionListener(e -> handleSupprimer());
        JButton assignerBtn = new JButton("Assigner role");
        assignerBtn.addActionListener(e -> handleAssignerRole());
        JButton gererRolesBtn = new JButton("Gerer les roles");
        gererRolesBtn.addActionListener(e -> handleGererRoles());

        buttons.add(ajouterBtn);
        buttons.add(modifierBtn);
        buttons.add(supprimerBtn);
        buttons.add(assignerBtn);
        buttons.add(gererRolesBtn);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        formPanel.add(buttons, gbc);

        add(formPanel, BorderLayout.SOUTH);
    }

    private void addLabeledField(JPanel panel, GridBagConstraints gbc, int row, String label, java.awt.Component component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    private void refreshRoles() {
        roleComboBox.removeAllItems();
        roleComboBox.addItem(null);
        List<Role> roles = employeDAO.listerRoles();
        for (Role role : roles) {
            roleComboBox.addItem(role);
        }
    }

    private void loadEmployes(List<Employe> employes) {
        tableModel.setRowCount(0);
        for (Employe e : employes) {
            tableModel.addRow(new Object[]{
                    e.getId(),
                    e.getNom(),
                    e.getPrenom(),
                    e.getDateEmbauche(),
                    e.getPoste(),
                    e.getSalaire(),
                    e.getService(),
                    e.getRole() != null ? e.getRole().getNom() : ""
            });
        }
    }

    private void populateFormFromSelection() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            return;
        }
        nomField.setText(Objects.toString(tableModel.getValueAt(selected, 1), ""));
        prenomField.setText(Objects.toString(tableModel.getValueAt(selected, 2), ""));
        dateField.setText(Objects.toString(tableModel.getValueAt(selected, 3), ""));
        posteField.setText(Objects.toString(tableModel.getValueAt(selected, 4), ""));
        salaireField.setText(Objects.toString(tableModel.getValueAt(selected, 5), ""));
        serviceField.setText(Objects.toString(tableModel.getValueAt(selected, 6), ""));
        selectRoleInCombo(Objects.toString(tableModel.getValueAt(selected, 7), ""));
    }

    private void selectRoleInCombo(String roleNom) {
        for (int i = 0; i < roleComboBox.getItemCount(); i++) {
            Role role = roleComboBox.getItemAt(i);
            if (role != null && role.getNom().equalsIgnoreCase(roleNom)) {
                roleComboBox.setSelectedIndex(i);
                return;
            }
        }
        roleComboBox.setSelectedIndex(0);
    }

    private void handleAjouter() {
        Employe employe = buildEmployeFromForm();
        if (employe == null) {
            return;
        }
        if (employeDAO.ajouter(employe)) {
            loadEmployes(employeDAO.listerTous());
            clearForm();
            JOptionPane.showMessageDialog(this, "Employe ajoute avec succes");
        } else {
            JOptionPane.showMessageDialog(this, "Echec de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleModifier() {
        int id = getSelectedEmployeId();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Selectionnez un employe a modifier");
            return;
        }
        Employe employe = buildEmployeFromForm();
        if (employe == null) {
            return;
        }
        employe.setId(id);
        if (employeDAO.modifier(employe)) {
            loadEmployes(employeDAO.listerTous());
            JOptionPane.showMessageDialog(this, "Employe modifie avec succes");
        } else {
            JOptionPane.showMessageDialog(this, "Echec de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSupprimer() {
        int id = getSelectedEmployeId();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Selectionnez un employe a supprimer");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Supprimer", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (employeDAO.supprimer(id)) {
                loadEmployes(employeDAO.listerTous());
                clearForm();
                JOptionPane.showMessageDialog(this, "Employe supprime");
            } else {
                JOptionPane.showMessageDialog(this, "Echec de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleAssignerRole() {
        int id = getSelectedEmployeId();
        if (id == -1) {
            JOptionPane.showMessageDialog(this, "Selectionnez un employe a mettre a jour");
            return;
        }
        Role role = (Role) roleComboBox.getSelectedItem();
        int roleId = role != null ? role.getId() : 0;
        if (employeDAO.assignerRole(id, roleId)) {
            loadEmployes(employeDAO.listerTous());
            JOptionPane.showMessageDialog(this, "Role assigne");
        } else {
            JOptionPane.showMessageDialog(this, "Echec de l'assignation", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleRechercherParNom() {
        String query = searchNomField.getText().trim();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Renseignez un nom ou prenom");
            return;
        }
        loadEmployes(employeDAO.rechercherParNom(query));
    }

    private void handleRechercherParId() {
        String idText = searchIdField.getText().trim();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Renseignez un identifiant");
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            Employe employe = employeDAO.rechercherParId(id);
            if (employe != null) {
                loadEmployes(List.of(employe));
            } else {
                JOptionPane.showMessageDialog(this, "Aucun employe trouve");
                tableModel.setRowCount(0);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID invalide");
        }
    }

    private void handleFiltrerService() {
        String service = filterServiceField.getText().trim();
        if (service.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Renseignez un service");
            return;
        }
        loadEmployes(employeDAO.filtrerParService(service));
    }

    private void handleFiltrerPoste() {
        String poste = filterPosteField.getText().trim();
        if (poste.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Renseignez un poste");
            return;
        }
        loadEmployes(employeDAO.filtrerParPoste(poste));
    }

    private void handleGererRoles() {
        JDialog dialog = new RoleDialog(this, employeDAO);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
        refreshRoles();
    }

    private Employe buildEmployeFromForm() {
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String dateStr = dateField.getText().trim();
        String poste = posteField.getText().trim();
        String salaireStr = salaireField.getText().trim();
        String service = serviceField.getText().trim();
        Role role = (Role) roleComboBox.getSelectedItem();

        if (nom.isEmpty() || prenom.isEmpty() || dateStr.isEmpty() || poste.isEmpty() || salaireStr.isEmpty() || service.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tous les champs obligatoires doivent etre remplis");
            return null;
        }

        LocalDate dateEmbauche;
        try {
            dateEmbauche = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Format de date invalide (yyyy-MM-dd)");
            return null;
        }

        double salaire;
        try {
            salaire = Double.parseDouble(salaireStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Salaire invalide");
            return null;
        }

        return new Employe(nom, prenom, dateEmbauche, poste, salaire, service, role);
    }

    private int getSelectedEmployeId() {
        int row = table.getSelectedRow();
        if (row == -1) {
            return -1;
        }
        Object value = tableModel.getValueAt(row, 0);
        if (value == null) {
            return -1;
        }
        return Integer.parseInt(value.toString());
    }

    private void clearForm() {
        nomField.setText("");
        prenomField.setText("");
        dateField.setText("");
        posteField.setText("");
        salaireField.setText("");
        serviceField.setText("");
        roleComboBox.setSelectedIndex(0);
        table.clearSelection();
    }
}
