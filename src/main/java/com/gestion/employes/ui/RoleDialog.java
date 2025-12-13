package com.gestion.employes.ui;

import com.gestion.employes.dao.EmployeDAO;
import com.gestion.employes.model.Role;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

class RoleDialog extends JDialog {
    private final EmployeDAO employeDAO;
    private final DefaultListModel<Role> listModel = new DefaultListModel<>();
    private JList<Role> roleJList;
    private JTextField nomField;
    private JTextField descriptionField;

    RoleDialog(JFrame parent, EmployeDAO employeDAO) {
        super(parent, "Gerer les roles", true);
        this.employeDAO = employeDAO;
        setSize(500, 400);
        setLayout(new BorderLayout());
        initComponents();
        loadRoles();
    }

    private void initComponents() {
        roleJList = new JList<>(listModel);
        roleJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFields();
            }
        });
        add(new JScrollPane(roleJList), BorderLayout.WEST);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nomField = new JTextField(15);
        descriptionField = new JTextField(15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nom"), gbc);
        gbc.gridx = 1;
        formPanel.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Description"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Ajouter");
        addBtn.addActionListener(e -> handleAjouter());
        JButton editBtn = new JButton("Modifier");
        editBtn.addActionListener(e -> handleModifier());
        JButton deleteBtn = new JButton("Supprimer");
        deleteBtn.addActionListener(e -> handleSupprimer());
        buttons.add(addBtn);
        buttons.add(editBtn);
        buttons.add(deleteBtn);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(buttons, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    private void populateFields() {
        Role selected = roleJList.getSelectedValue();
        if (selected == null) {
            nomField.setText("");
            descriptionField.setText("");
            return;
        }
        nomField.setText(selected.getNom());
        descriptionField.setText(selected.getDescription());
    }

    private void loadRoles() {
        listModel.clear();
        List<Role> roles = employeDAO.listerRoles();
        for (Role role : roles) {
            listModel.addElement(role);
        }
    }

    private void handleAjouter() {
        String nom = nomField.getText().trim();
        String description = descriptionField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom obligatoire");
            return;
        }
        Role role = new Role(nom, description);
        if (employeDAO.ajouterRole(role)) {
            loadRoles();
            roleJList.setSelectedValue(role, true);
            JOptionPane.showMessageDialog(this, "Role ajoute");
        } else {
            JOptionPane.showMessageDialog(this, "Echec de l'ajout", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleModifier() {
        Role selected = roleJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selectionnez un role");
            return;
        }
        String nom = nomField.getText().trim();
        String description = descriptionField.getText().trim();
        if (nom.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nom obligatoire");
            return;
        }
        selected.setNom(nom);
        selected.setDescription(description);
        if (employeDAO.modifierRole(selected)) {
            loadRoles();
            JOptionPane.showMessageDialog(this, "Role modifie");
        } else {
            JOptionPane.showMessageDialog(this, "Echec de la modification", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSupprimer() {
        Role selected = roleJList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Selectionnez un role");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Supprimer le role ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (employeDAO.supprimerRole(selected.getId())) {
                loadRoles();
                JOptionPane.showMessageDialog(this, "Role supprime");
            } else {
                JOptionPane.showMessageDialog(this, "Echec de la suppression", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
