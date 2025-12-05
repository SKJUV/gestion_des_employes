package com.gestion.etu;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {

    public Employe getEmploye(int id) {
        String sql = "SELECT * FROM employes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Date d = rs.getDate("date_embauche");
                    LocalDate date = d != null ? d.toLocalDate() : null;
                    return new Employe(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            date,
                            rs.getString("poste"),
                            rs.getDouble("salaire"),
                            rs.getString("service")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Employe> getEmployes() {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Date d = rs.getDate("date_embauche");
                LocalDate date = d != null ? d.toLocalDate() : null;
                employes.add(new Employe(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        date,
                        rs.getString("poste"),
                        rs.getDouble("salaire"),
                        rs.getString("service")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    public List<Employe> getEmployesByService(String service) {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes WHERE service = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, service);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date d = rs.getDate("date_embauche");
                    LocalDate date = d != null ? d.toLocalDate() : null;
                    employes.add(new Employe(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            date,
                            rs.getString("poste"),
                            rs.getDouble("salaire"),
                            rs.getString("service")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    public List<Employe> getEmployesByPoste(String poste) {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes WHERE poste = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, poste);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date d = rs.getDate("date_embauche");
                    LocalDate date = d != null ? d.toLocalDate() : null;
                    employes.add(new Employe(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            date,
                            rs.getString("poste"),
                            rs.getDouble("salaire"),
                            rs.getString("service")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    public void addEmploye(Employe employe) {
        String sql = "INSERT INTO employes(id, nom, prenom, date_embauche, poste, salaire, service) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employe.getId());
            pstmt.setString(2, employe.getNom());
            pstmt.setString(3, employe.getPrenom());
            if (employe.getDateEmbauche() != null) {
                pstmt.setDate(4, Date.valueOf(employe.getDateEmbauche()));
            } else {
                pstmt.setDate(4, null);
            }
            pstmt.setString(5, employe.getPoste());
            pstmt.setDouble(6, employe.getSalaire());
            pstmt.setString(7, employe.getService());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateEmploye(Employe employe) {
        String sql = "UPDATE employes SET nom = ?, prenom = ?, date_embauche = ?, poste = ?, salaire = ?, service = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employe.getNom());
            pstmt.setString(2, employe.getPrenom());
            if (employe.getDateEmbauche() != null) {
                pstmt.setDate(3, Date.valueOf(employe.getDateEmbauche()));
            } else {
                pstmt.setDate(3, null);
            }
            pstmt.setString(4, employe.getPoste());
            pstmt.setDouble(5, employe.getSalaire());
            pstmt.setString(6, employe.getService());
            pstmt.setInt(7, employe.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmploye(int id) {
        String sql = "DELETE FROM employes WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Employe> searchEmploye(String searchTerm) {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes WHERE nom LIKE ? OR id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchTerm + "%");
            try {
                int id = Integer.parseInt(searchTerm);
                pstmt.setInt(2, id);
            } catch (NumberFormatException e) {
                pstmt.setInt(2, -1);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date d = rs.getDate("date_embauche");
                    LocalDate date = d != null ? d.toLocalDate() : null;
                    employes.add(new Employe(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            date,
                            rs.getString("poste"),
                            rs.getDouble("salaire"),
                            rs.getString("service")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }
}
