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
