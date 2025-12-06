## Diagramme de cas d'utilisation

```mermaid
%%{init: {'theme': 'base', 'themeVariables': {'primaryColor': '#0f766e', 'primaryBorderColor': '#0f766e', 'primaryTextColor': '#0a312d', 'lineColor': '#0f766e', 'actorBorder': '#0f766e', 'actorTextColor': '#0a312d', 'tertiaryColor': '#e6f4f1', 'fontFamily': 'Trebuchet MS, Arial'}}}%%
graph TD

classDef actor fill:#0f766e,color:#ffffff,stroke:#0f766e;
classDef usecase fill:#e6f4f1,color:#0a312d,stroke:#0f766e;

RH((Responsable RH)):::actor

subgraph Systeme_de_gestion_des_employes["Systeme de gestion des employes"]
direction TB
Ajouter([Ajouter un employe]):::usecase
Modifier([Modifier un employe]):::usecase
Supprimer([Supprimer un employe]):::usecase
Lister([Consulter la liste des employes]):::usecase
AssignerRole([Assigner un role]):::usecase
GererRoles([Gerer les roles]):::usecase
FiltrerService([Filtrer par service]):::usecase
FiltrerPoste([Filtrer par poste]):::usecase
RechNom([Rechercher par nom]):::usecase
RechId([Rechercher par identifiant]):::usecase
end

RH --> Ajouter
RH --> Modifier
RH --> Supprimer
RH --> Lister
RH --> AssignerRole
RH --> GererRoles

FiltrerService -. extend .-> Lister
FiltrerPoste -. extend .-> Lister
RechNom -. extend .-> Lister
RechId -. extend .-> Lister
AssignerRole -. extend .-> Ajouter
AssignerRole -. extend .-> Modifier
```

- `graph TD` pour rapprocher l'acteur et ses interactions.
- Sous-graphe pour encadrer le systeme de gestion.
- Liens d'extension sur les filtres/recherches pour montrer qu'ils prolongent la consultation.

## Flux UC1 (Ajouter un employe)

```mermaid
graph TD
  A[Cliquer sur Ajouter] --> B[Afficher le formulaire]
  B --> C[Remplir les champs requis]
  C --> D[Valider]
  D --> E{Champs obligatoires remplis ?}
  E -- Non --> F[Afficher un message d'erreur et rester sur le formulaire]
  F --> C
  E -- Oui --> G[Enregistrer l'employe en base MySQL]
  G --> H[Rafraichir la liste affichee]
  H --> I[Afficher un message de confirmation]
```
## Flux UC2 (Modifier un employe)

```mermaid
graph TD
  A[Selectionner un employe dans la liste] --> B[Charger les informations dans le formulaire]
  B --> C[Modifier les champs souhaites]
  C --> D[Valider]
  D --> E{Champs obligatoires remplis ?}
  E -- Non --> F[Afficher un message d'erreur et rester sur le formulaire]
  F --> C
  E -- Oui --> G[Mettre a jour l'employe en base MySQL]
  G --> H[Rafraichir la liste affichee]
  H --> I[Afficher un message de confirmation]
```

## Flux UC3 (Supprimer un employe)

```mermaid
graph TD
  A[Selectionner un employe dans la liste] --> B[Clique sur Supprimer]
  B --> C[Demander confirmation]
  C --> D{Confirme ?}
  D -- Non --> E[Annuler l'action]
  D -- Oui --> F[Supprimer l'employe en base MySQL]
  F --> G[Rafraichir la liste affichee]
  G --> H[Afficher un message de confirmation]
```
## Flux UC4 (Gerer le stock)

```mermaid
graph TD
  A[Choisir entree / sortie / mise a jour] --> B[Saisir produit et quantite]
  B --> C[Valider l'operation]
  C --> D{Donnees valides ?}
  D -- Non --> E[Message d'erreur + rester sur le formulaire]
  E --> B
  D -- Oui --> F[Mettre a jour le stock en base MySQL]
  F --> G[Enregistrer le mouvement]
  G --> H[Rafraichir les indicateurs de stock]
  H --> I[Afficher confirmation]
```

## Flux UC5 (Enregistrer mouvement)

```mermaid
graph TD
  A[Selectionner type de mouvement] --> B[Saisir produit / quantite / motif]
  B --> C[Valider]
  C --> D{Donnees valides ?}
  D -- Non --> E[Afficher erreur et rester sur le formulaire]
  E --> B
  D -- Oui --> F[Creer l'entree mouvement en base MySQL]
  F --> G[Lier au stock concerne]
  G --> H[Afficher confirmation]
```

## Flux UC6 (Consulter l'historique des mouvements)

```mermaid
graph TD
  A[Ouvrir l'historique] --> B[Charger les mouvements depuis MySQL]
  B --> C[Afficher la liste]
  C --> D[Appliquer un filtre : produit / date / type]
  D --> E[Actualiser la liste filtree]
```

## Flux UC7 (Afficher produits par categorie / trier)

```mermaid
graph TD
  A[Ouvrir l'affichage produits] --> B[Charger les produits]
  B --> C[Choisir categorie ou critere de tri]
  C --> D[Appliquer filtre/tri]
  D --> E[Afficher la liste mise a jour]
```

## Flux UC8 (Alertes stock)

```mermaid
graph TD
  A[Surveillance des seuils<br/>batch ou temps reel] --> B{Stock faible ou rupture ?}
  B -- Non --> C[Continuer la surveillance]
  B -- Oui --> D[Generer alerte]
  D --> E[Notifier le gestionnaire]
  E --> F[Proposer actions : commande ou ajustement]
```

## Flux UC9 (Assigner un role a un employe)

```mermaid
graph TD
  A[Selectionner un employe] --> B[Ouvrir la liste des roles]
  B --> C[Choisir un role]
  C --> D[Valider l'assignation]
  D --> E{Role valide ?}
  E -- Non --> F[Afficher un message d'erreur]
  E -- Oui --> G[Mettre a jour le role de l'employe en base MySQL]
  G --> H[Rafraichir la liste / fiche employe]
  H --> I[Afficher un message de confirmation]
```

## Flux UC10 (Gerer les roles)

```mermaid
graph TD
  A[Ouvrir le referentiel des roles] --> B[Choisir ajouter / modifier / supprimer]
  B --> C{Action ?}
  C -- Ajouter --> D[Saisir nom + description]
  C -- Modifier --> E[Selectionner un role et ajuster les champs]
  C -- Supprimer --> F[Selectionner un role et confirmer]
  D --> G[Valider]
  E --> G
  F --> G
  G --> H{Validation OK ?}
  H -- Non --> I[Afficher erreur et rester sur l'ecran]
  H -- Oui --> J[Mettre a jour la table role en base MySQL]
  J --> K[Rafraichir la liste des roles]
  K --> L[Afficher un message de confirmation]
```
