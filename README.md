# Gestion des employes - Swing

Application Swing conforme au cahier des charges : CRUD employes, recherche/filtre, assignation et gestion des roles, persistance MariaDB/MySQL.

## Prerequis
- JDK 17+
- Maven 3.9+
- MariaDB/MySQL accessible (schema `gestion_employes`)

## Installation base de donnees
```sql
CREATE DATABASE IF NOT EXISTS gestion_employes;
USE gestion_employes;

CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE employe (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    date_embauche DATE NOT NULL,
    poste VARCHAR(100) NOT NULL,
    salaire DOUBLE NOT NULL,
    service VARCHAR(100) NOT NULL,
    role_id INT,
    CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES role(id)
);
```

## Configuration
Renseigner vos identifiants MariaDB/MySQL dans `src/main/resources/db.properties` :
```
jdbc.url=jdbc:mariadb://localhost:3306/gestion_employes
jdbc.user=<utilisateur>
jdbc.password=<motdepasse>
```

## Lancement
```bash
mvn clean package
mvn exec:java
```

## Fonctionnalites cibles (docs/rapport.md)
- Ajouter / modifier / supprimer un employe avec controle des champs obligatoires et rafraichissement de la liste.
- Consulter la liste et rafraichissement au chargement.
- Recherche par nom/prenom ou par identifiant.
- Filtres par service et par poste.
- Assignation de role depuis la fiche employe.
- Gestion complete des roles (ajout, modification, suppression) avec mise a jour des listes deroulantes.

## Structure
- `src/main/java/com/gestion/employes/model` : entites `Employe`, `Role`.
- `src/main/java/com/gestion/employes/dao` : `DBConnection`, `EmployeDAO` (CRUD employes + roles).
- `src/main/java/com/gestion/employes/ui` : IHM Swing `EmployeController`, dialogue `RoleDialog`.
- `pom.xml` : dependance `mysql-connector-j` et compilation Java 17.

## Flux IHM (alignes sur la doc)
- **Ajouter** : bouton Ajouter -> validation des champs -> insertion MySQL -> rafraichissement table -> confirmation.
- **Modifier** : selection dans la table -> formulaire pre-rempli -> validation -> update -> rafraichissement -> confirmation.
- **Supprimer** : selection -> confirmation -> suppression -> rafraichissement -> confirmation.
- **Recherche/filtre** : champs en haut de l'ecran pour nom/id/service/poste -> requete DAO cible -> affichage restreint.
- **Assigner un role** : selection employe -> choix dans la liste deroulante -> bouton Assigner role -> update -> rafraichissement -> confirmation.
- **Gerer les roles** : bouton dedie -> dialogue CRUD roles -> rafraichissement de la combo role en sortie.

## Notes
- L'application utilise le driver MySQL, assurez-vous que Maven peut telecharger les dependances.
- Les dates se saisissent au format ISO `yyyy-MM-dd`.
- Les champs obligatoires vides ou formats invalides sont bloques avec un message d'erreur.
