package com.gestion.employes;

import com.gestion.employes.dao.DBConnection;
import com.gestion.employes.dao.EmployeDAO;
import com.gestion.employes.ui.EmployeController;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DBConnection dbConnection = new DBConnection();
            try (Connection ignored = dbConnection.getConnection()) {
                EmployeDAO employeDAO = new EmployeDAO(dbConnection);
                EmployeController controller = new EmployeController(employeDAO);
                controller.setVisible(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Connexion à la base impossible : " + ex.getMessage() +
                                "\nVérifiez que MariaDB est démarré, le port/host, et les identifiants dans src/main/resources/db.properties",
                        "Erreur de connexion",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
