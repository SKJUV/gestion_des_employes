CREATE DATABASE IF NOT EXISTS gestion_employes;

USE gestion_employes;

CREATE TABLE IF NOT EXISTS employes (
    id INT PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    prenom VARCHAR(255) NOT NULL,
    date_embauche DATE NOT NULL,
    poste VARCHAR(255) NOT NULL,
    salaire DOUBLE NOT NULL,
    service VARCHAR(255) NOT NULL
);
