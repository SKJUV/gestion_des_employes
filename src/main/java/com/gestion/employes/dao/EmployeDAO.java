package com.gestion.employes.dao;

import com.gestion.employes.model.Employe;
import com.gestion.employes.model.Role;

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

    private final DBConnection dbConnection;

    public EmployeDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean ajouter(Employe employe) {
        String sql = "INSERT INTO employe (nom, prenom, date_embauche, poste, salaire, service, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fillEmployeStatement(employe, stmt);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employe.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifier(Employe employe) {
        String sql = "UPDATE employe SET nom = ?, prenom = ?, date_embauche = ?, poste = ?, salaire = ?, service = ?, role_id = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            fillEmployeStatement(employe, stmt);
            stmt.setInt(8, employe.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimer(int id) {
        String sql = "DELETE FROM employe WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Employe> listerTous() {
        String sql = baseSelect() + " ORDER BY e.id";
        return fetchEmployes(sql, null);
    }

    public Employe rechercherParId(int id) {
        String sql = baseSelect() + " WHERE e.id = ?";
        List<Employe> results = fetchEmployes(sql, stmt -> stmt.setInt(1, id));
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Employe> rechercherParNom(String nom) {
        String sql = baseSelect() + " WHERE LOWER(e.nom) LIKE ? OR LOWER(e.prenom) LIKE ? ORDER BY e.id";
        String pattern = "%" + nom.toLowerCase() + "%";
        return fetchEmployes(sql, stmt -> {
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
        });
    }

    public List<Employe> filtrerParService(String service) {
        String sql = baseSelect() + " WHERE LOWER(e.service) = LOWER(?) ORDER BY e.id";
        return fetchEmployes(sql, stmt -> stmt.setString(1, service));
    }

    public List<Employe> filtrerParPoste(String poste) {
        String sql = baseSelect() + " WHERE LOWER(e.poste) = LOWER(?) ORDER BY e.id";
        return fetchEmployes(sql, stmt -> stmt.setString(1, poste));
    }

    public boolean assignerRole(int employeId, int roleId) {
        String sql = "UPDATE employe SET role_id = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (roleId > 0) {
                stmt.setInt(1, roleId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setInt(2, employeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Role> listerRoles() {
        List<Role> roles = new ArrayList<>();
        String sql = "SELECT id, nom, description FROM role ORDER BY nom";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                roles.add(mapRole(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public boolean ajouterRole(Role role) {
        String sql = "INSERT INTO role (nom, description) VALUES (?, ?)";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, role.getNom());
            stmt.setString(2, role.getDescription());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    role.setId(generatedKeys.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean supprimerRole(int roleId) {
        String sql = "DELETE FROM role WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean modifierRole(Role role) {
        String sql = "UPDATE role SET nom = ?, description = ? WHERE id = ?";
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getNom());
            stmt.setString(2, role.getDescription());
            stmt.setInt(3, role.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Employe> fetchEmployes(String sql, StatementConfigurer configurer) {
        List<Employe> employes = new ArrayList<>();
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (configurer != null) {
                configurer.configure(stmt);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employes.add(mapEmploye(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employes;
    }

    private void fillEmployeStatement(Employe employe, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, employe.getNom());
        stmt.setString(2, employe.getPrenom());
        LocalDate date = employe.getDateEmbauche();
        stmt.setDate(3, date != null ? Date.valueOf(date) : null);
        stmt.setString(4, employe.getPoste());
        stmt.setDouble(5, employe.getSalaire());
        stmt.setString(6, employe.getService());
        if (employe.getRole() != null) {
            stmt.setInt(7, employe.getRole().getId());
        } else {
            stmt.setNull(7, java.sql.Types.INTEGER);
        }
    }

    private Employe mapEmploye(ResultSet rs) throws SQLException {
        Role role = null;
        int roleId = rs.getInt("role_id");
        if (!rs.wasNull()) {
            role = new Role(roleId, rs.getString("role_nom"), rs.getString("role_description"));
        }
        LocalDate dateEmbauche = rs.getDate("date_embauche").toLocalDate();
        return new Employe(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                dateEmbauche,
                rs.getString("poste"),
                rs.getDouble("salaire"),
                rs.getString("service"),
                role
        );
    }

    private Role mapRole(ResultSet rs) throws SQLException {
        return new Role(rs.getInt("id"), rs.getString("nom"), rs.getString("description"));
    }

    private String baseSelect() {
        return "SELECT e.id, e.nom, e.prenom, e.date_embauche, e.poste, e.salaire, e.service, e.role_id, r.nom AS role_nom, r.description AS role_description " +
                "FROM employe e LEFT JOIN role r ON e.role_id = r.id";
    }

    @FunctionalInterface
    private interface StatementConfigurer {
        void configure(PreparedStatement stmt) throws SQLException;
    }
}
