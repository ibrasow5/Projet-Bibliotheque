import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {
    @SuppressWarnings("unused")
    private Bibliotheque bibliotheque;

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        bibliotheque = new Bibliotheque();

        Menu gestionUtilisateursMenu = new Menu("Gestion des Utilisateurs");
        Menu gestionLivresMenu = new Menu("Gestion des Livres");
        Menu gestionEmpruntsMenu = new Menu("Gestion des Emprunts");
        Menu statistiquesMenu = new Menu("Statistiques");

        MenuItem afficherUtilisateursItem = new MenuItem("Afficher les utilisateurs");
        MenuItem ajouterUtilisateurItem = new MenuItem("Ajouter un utilisateur");
        MenuItem verifierEligibiliteItem = new MenuItem("Vérifier l'éligibilité");
        gestionUtilisateursMenu.getItems().addAll(afficherUtilisateursItem, ajouterUtilisateurItem, verifierEligibiliteItem);

        MenuItem ajouterLivreItem = new MenuItem("Ajouter un livre");
        MenuItem modifierLivreItem = new MenuItem("Modifier un livre");
        MenuItem supprimerLivreItem = new MenuItem("Supprimer un livre");
        MenuItem rechercherLivreItem = new MenuItem("Rechercher un livre");
        gestionLivresMenu.getItems().addAll(ajouterLivreItem, modifierLivreItem, supprimerLivreItem, rechercherLivreItem);

        MenuItem enregistrerEmpruntItem = new MenuItem("Enregistrer un emprunt");
        MenuItem enregistrerRetourItem = new MenuItem("Enregistrer un retour");
        MenuItem afficherEmpruntsItem = new MenuItem("Afficher les emprunts");
        MenuItem limiterEmpruntsItem = new MenuItem("Limiter les emprunts");
        gestionEmpruntsMenu.getItems().addAll(enregistrerEmpruntItem, enregistrerRetourItem, afficherEmpruntsItem, limiterEmpruntsItem);

        MenuItem afficherStatistiquesItem = new MenuItem("Afficher les statistiques");
        statistiquesMenu.getItems().add(afficherStatistiquesItem);

        afficherUtilisateursItem.setOnAction(e -> {
            ouvrirFenetre(new AffichageUtilisateursPage(), primaryStage);
        });
        
        ajouterUtilisateurItem.setOnAction(e -> {
            ouvrirFenetre(new AjoutUtilisateurPage(), primaryStage);
        });

        verifierEligibiliteItem.setOnAction(e -> {
            ouvrirFenetre(new VerificationEligibilitePage(), primaryStage);
        });

        ajouterLivreItem.setOnAction(e -> {
            ouvrirFenetre(new AjoutLivrePage(), primaryStage);
        });

        modifierLivreItem.setOnAction(e -> {
            ouvrirFenetre(new ModificationLivrePage(), primaryStage);
        });

        supprimerLivreItem.setOnAction(e -> {
            ouvrirFenetre(new SuppressionLivrePage(), primaryStage);
        });

        rechercherLivreItem.setOnAction(e -> {
            ouvrirFenetre(new RechercheLivrePage(), primaryStage);
        });

        enregistrerEmpruntItem.setOnAction(e -> {
            ouvrirFenetre(new EnregistrementEmpruntPage(), primaryStage);
        });

        enregistrerRetourItem.setOnAction(e -> {
            ouvrirFenetre(new EnregistrementRetourPage(), primaryStage);
        });

        afficherEmpruntsItem.setOnAction(e -> {
            ouvrirFenetre(new AffichageEmpruntsPage(), primaryStage);
        });

        limiterEmpruntsItem.setOnAction(e -> {
            ouvrirFenetre(new LimitationEmpruntsPage(), primaryStage);
        });

        afficherStatistiquesItem.setOnAction(e -> {
            ouvrirFenetre(new StatistiquesPage(), primaryStage);
        });

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(gestionUtilisateursMenu, gestionLivresMenu, gestionEmpruntsMenu, statistiquesMenu);

        VBox root = new VBox();
        root.getChildren().addAll(menuBar);

        // Créer un TableView pour afficher les livres
        TableView<Livre> tableView = new TableView<>();
        TableColumn<Livre, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("ISBN"));

        TableColumn<Livre, String> titreCol = new TableColumn<>("Titre");
        titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));

        TableColumn<Livre, String> auteurCol = new TableColumn<>("Auteur");
        auteurCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        TableColumn<Livre, Integer> anneeCol = new TableColumn<>("Année de publication");
        anneeCol.setCellValueFactory(new PropertyValueFactory<>("anneePublication"));

        tableView.getColumns().addAll(isbnCol, titreCol, auteurCol, anneeCol);

        // Charger les livres depuis la base de données et les ajouter au TableView
        chargerLivres(tableView);

        root.getChildren().add(tableView);

        Button actualiserButton = new Button("Actualiser");
        actualiserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Actualiser les données du TableView
                tableView.getItems().clear(); // Efface les données actuelles
                chargerLivres(tableView); // Recharge les données depuis la base de données
            }
        });

        HBox buttonBox = new HBox(actualiserButton);
        buttonBox.setPadding(new Insets((10)));
        root.getChildren().add(buttonBox);

        Scene scene = new Scene(root, 481, 400);

        primaryStage.setTitle("Gestionnaire de Bibliothèque");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void chargerLivres(TableView<Livre> tableView) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM livre")) {

            while (resultSet.next()) {
                String isbn = resultSet.getString("ISBN");
                String titre = resultSet.getString("Titre");
                String auteur = resultSet.getString("Auteur");
                int anneePublication = resultSet.getInt("AnneePublication");

                Livre livre = new Livre(isbn, titre, auteur, anneePublication);
                tableView.getItems().add(livre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ouvrirFenetre(Page page, Stage parentStage) {
        Stage stage = new Stage();
        stage.setTitle(page.getTitle());
        stage.setScene(new Scene(page.getContent(), 220, 200));
        stage.initOwner(parentStage);
        stage.showAndWait(); // Afficher la fenêtre de manière modale
    }

    public static void main(String[] args) {
        launch(args);
    }
}

interface Page {
    String getTitle();
    VBox getContent();
}

class AffichageUtilisateursPage implements Page {
    @Override
    public String getTitle() {
        return "Afficher les utilisateurs";
    }

    @SuppressWarnings("unchecked")
    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TableView<Utilisateur> tableView = new TableView<>();

        // Définir les colonnes du TableView pour les utilisateurs
        TableColumn<Utilisateur, Integer> idCol = new TableColumn<>("ID Utilisateur");
        idCol.setCellValueFactory(new PropertyValueFactory<>("numeroIdentification"));

        TableColumn<Utilisateur, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        tableView.getColumns().addAll(idCol, nomCol);

        // Charger les utilisateurs depuis la base de données et les ajouter au TableView
        chargerUtilisateurs(tableView);

        root.getChildren().addAll(tableView);
        return root;
    }

    private void chargerUtilisateurs(TableView<Utilisateur> tableView) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM utilisateur")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("NumeroIdentification");
                String nom = resultSet.getString("Nom");

                Utilisateur utilisateur = new Utilisateur(id, nom);
                tableView.getItems().add(utilisateur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


class AjoutUtilisateurPage implements Page {
    @Override
    public String getTitle() {
        return "Ajouter un utilisateur";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField nomField = new TextField();
        TextField idField = new TextField();
        Button addButton = new Button("Ajouter");
        addButton.setOnAction(e -> {
            String nom = nomField.getText();
            int id = Integer.parseInt(idField.getText());
            Utilisateur utilisateur = new Utilisateur(id, nom);
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.ajouterUtilisateur(utilisateur);
            System.out.println("Utilisateur ajouté avec succès.");
            Stage stage = (Stage) addButton.getScene().getWindow(); 
            stage.close(); 
        });
        root.getChildren().addAll(new Label("Nom:"), nomField, new Label("Numéro Identification:"), idField, addButton);
        return root;
    }
}

class VerificationEligibilitePage implements Page {
    @Override
    public String getTitle() {
        return "Vérifier l'éligibilité";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField idField = new TextField();
        Button verifyButton = new Button("Vérifier");
        verifyButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            Bibliotheque bibliotheque = new Bibliotheque();
            boolean estEligible = bibliotheque.verifierEligibilite(id);
            if (estEligible) {
                System.out.println("Cet utilisateur est éligible.");
            } else {
                System.out.println("Cet utilisateur est inéligible.");
            }
            Stage stage = (Stage) verifyButton.getScene().getWindow();
            stage.close();
        });
        root.getChildren().addAll(new Label("Numéro Identification:"), idField, verifyButton);
        return root;
    }
}


class AjoutLivrePage implements Page {
    @Override
    public String getTitle() {
        return "Ajouter un livre";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField titreField = new TextField();
        TextField auteurField = new TextField();
        TextField anneeField = new TextField();
        TextField isbnField = new TextField();
        Button addButton = new Button("Ajouter");
        addButton.setOnAction(e -> {
            String titre = titreField.getText();
            String auteur = auteurField.getText();
            int annee = Integer.parseInt(anneeField.getText());
            String isbn = isbnField.getText();
            Livre livre = new Livre(isbn, titre, auteur, annee);
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.ajouterLivre(livre);
            System.out.println("Livre ajouté avec succès.");
            Stage stage = (Stage) addButton.getScene().getWindow(); 
            stage.close();
        });
        root.getChildren().addAll(new Label("Titre:"), titreField, new Label("Auteur:"), auteurField,
                new Label("Année de publication:"), anneeField, new Label("ISBN:"), isbnField, addButton);
        return root;
    }
}

class ModificationLivrePage implements Page {
    @Override
    public String getTitle() {
        return "Modifier un livre";
    }

    @Override
public VBox getContent() {
    VBox root = new VBox();
    TextField isbnField = new TextField();
    TextField titreField = new TextField();
    TextField auteurField = new TextField();
    TextField anneeField = new TextField();
    Button modifyButton = new Button("Modifier");

    modifyButton.setOnAction(e -> {
        String isbn = isbnField.getText();
        Livre livre = Bibliotheque.rechercherLivre("ISBN", isbn);
      
        if (livre != null) {
            String nouveauTitre = titreField.getText();
            String nouvelAuteur = auteurField.getText();
            int nouvelleAnnee = Integer.parseInt(anneeField.getText());
    
            // Instancier un objet Bibliotheque
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.modifierLivre(livre, nouveauTitre, nouvelAuteur, nouvelleAnnee);
            
            System.out.println("Livre modifié avec succès.");
            Stage stage = (Stage) modifyButton.getScene().getWindow(); 
            stage.close();
        } else {
            System.out.println("Aucun livre trouvé avec cet ISBN.");
        }
    });
    
    
    

    root.getChildren().addAll(new Label("ISBN:"), isbnField, new Label("Titre:"), titreField,
            new Label("Auteur:"), auteurField, new Label("Année de publication:"), anneeField, modifyButton);
    return root;
}
}

class SuppressionLivrePage implements Page {
    @Override
    public String getTitle() {
        return "Supprimer un livre";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField isbnField = new TextField();
        Button deleteButton = new Button("Supprimer");
        deleteButton.setOnAction(e -> {
            String isbn = isbnField.getText();
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.supprimerLivre(isbn); // Appeler la méthode supprimerLivre avec l'ISBN saisi
            System.out.println("Livre supprimé avec succès.");
            Stage stage = (Stage) deleteButton.getScene().getWindow();
            stage.close();
        });
        root.getChildren().addAll(new Label("ISBN:"), isbnField, deleteButton);
        return root;
    }
}


class RechercheLivrePage implements Page {
    @Override
    public String getTitle() {
        return "Rechercher un livre";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField searchField = new TextField();
        ComboBox<String> criteriaComboBox = new ComboBox<>();
        criteriaComboBox.getItems().addAll("Titre", "Auteur", "ISBN");
        criteriaComboBox.setValue("Titre");
        Button searchButton = new Button("Rechercher");
        searchButton.setOnAction(e -> {
            String critere = criteriaComboBox.getValue();
            String valeur = searchField.getText();
            Livre livre = Bibliotheque.rechercherLivre(critere, valeur);
            if (livre != null) {
                // Utilisation de la méthode toString() pour afficher les détails du livre
                System.out.println(livre.toString());
            } else {
                System.out.println("Aucun livre trouvé.");
            }
            Stage stage = (Stage) searchButton.getScene().getWindow();
            stage.close();
        });
        root.getChildren().addAll(new Label("Rechercher par titre, auteur ou ISBN:"), searchField, criteriaComboBox, searchButton);
        return root;
    }
}


class EnregistrementEmpruntPage implements Page {
    @Override
    public String getTitle() {
        return "Enregistrer un emprunt";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField userIdField = new TextField();
        TextField isbnField = new TextField();
        Button addButton = new Button("Enregistrer");
        addButton.setOnAction(e -> {
            int userId = Integer.parseInt(userIdField.getText());
            String isbn = isbnField.getText();
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.enregistrerEmprunt(userId, isbn);
            System.out.println("Emprunt enregistré avec succès.");
            Stage stage = (Stage) addButton.getScene().getWindow(); 
            stage.close();
        });
        root.getChildren().addAll(new Label("ID Utilisateur:"), userIdField, new Label("ISBN Livre:"), isbnField, addButton);
        return root;
    }
}


class EnregistrementRetourPage implements Page {
    @Override
    public String getTitle() {
        return "Enregistrer un retour";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField userIdField = new TextField();
        TextField isbnField = new TextField();
        Button returnButton = new Button("Enregistrer");
        returnButton.setOnAction(e -> {
            int userId = Integer.parseInt(userIdField.getText());
            String isbn = isbnField.getText();
            Bibliotheque bibliotheque = new Bibliotheque();
            bibliotheque.enregistrerRetour(userId, isbn);
            System.out.println("Retour enregistré avec succès.");
            Stage stage = (Stage) returnButton.getScene().getWindow(); 
            stage.close();
        });
        root.getChildren().addAll(new Label("ID Utilisateur:"), userIdField, new Label("ISBN Livre:"), isbnField, returnButton);
        return root;
    }
}

class AffichageEmpruntsPage implements Page {
    @Override
    public String getTitle() {
        return "Afficher les emprunts";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();

        // Créer les éléments d'interface utilisateur
        Label instructionLabel = new Label("Numéro d'identification :");
        TextField idTextField = new TextField();
        Button afficherButton = new Button("Afficher");

        // Définir l'action à effectuer lorsque le bouton est cliqué
        afficherButton.setOnAction(event -> {
            // Récupérer le numéro d'identification saisi par l'utilisateur
            int userId = Integer.parseInt(idTextField.getText());
            // Afficher les livres empruntés par l'utilisateur dans le terminal
            afficherLivresEmpruntes(userId);
            Stage stage = (Stage) afficherButton.getScene().getWindow(); 
            stage.close();
        });

        // Ajouter les éléments à la racine
        root.getChildren().addAll(instructionLabel, idTextField, afficherButton);

        return root;
    }

    private void afficherLivresEmpruntes(int numeroIdentification) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT e.ISBN, e.DateEmprunt, l.Titre, l.Auteur, l.AnneePublication " +
                                                                                 "FROM emprunt e " +
                                                                                 "JOIN livre l ON e.ISBN = l.ISBN " +
                                                                                 "WHERE NumeroIdentification = ? AND e.DateRetour IS NULL")) {
    
            preparedStatement.setInt(1, numeroIdentification);
            ResultSet resultSet = preparedStatement.executeQuery();
            
            // Variable pour garder trace s'il y a au moins un livre emprunté
            boolean livreEmprunte = false;
    
            // Afficher les livres empruntés dans le terminal avec leurs informations complètes
            while (resultSet.next()) {
                livreEmprunte = true; // Indiquer qu'au moins un livre a été trouvé
                String isbnLivre = resultSet.getString("ISBN");
                String dateEmprunt = resultSet.getString("DateEmprunt");
                String titreLivre = resultSet.getString("Titre");
                String auteurLivre = resultSet.getString("Auteur");
                int anneePublication = resultSet.getInt("AnneePublication");
    
                Livre livre = new Livre(isbnLivre, titreLivre, auteurLivre, anneePublication);
                System.out.println(livre); // Utilisation de la méthode toString() de Livre pour afficher les informations du livre
                System.out.println("Date Emprunt: " + dateEmprunt);
                System.out.println("Date Retour: (Non retourné)");
                System.out.println(); // Ligne vide pour séparer les livres
            }
    
            // Si aucun livre n'a été emprunté, afficher un message
            if (!livreEmprunte) {
                System.out.println("Aucun livre emprunté et non retourné pour cet utilisateur.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }             
}    


class LimitationEmpruntsPage implements Page {
    @Override
    public String getTitle() {
        return "Limiter les emprunts";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        TextField limiteField = new TextField();
        Button validerButton = new Button("Valider");

        validerButton.setOnAction(e -> {
            try {
                int limite = Integer.parseInt(limiteField.getText());
                Bibliotheque.definirLimiteEmprunts(limite);
                System.out.println("Limite des emprunts définie avec succès : " + limite);
                Stage stage = (Stage) validerButton.getScene().getWindow();
                stage.close();
            } catch (NumberFormatException ex) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        });

        root.getChildren().addAll(new Label("Limite de livres à emprunter:"), limiteField, validerButton);
        return root;
    }
}

class StatistiquesPage implements Page {
    @Override
    public String getTitle() {
        return "Statistiques de la bibliothèque";
    }

    @Override
    public VBox getContent() {
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));

        // Créer les éléments d'interface utilisateur
        Label statistiquesLabel = new Label("Statistiques de la bibliothèque:");
        Label nombreTotalLivresLabel = new Label("Nombre total de livres: " + Bibliotheque.getNombreTotalLivres());
        Label nombreExemplairesEmpruntesLabel = new Label("Nombre d'exemplaires empruntés: " + Bibliotheque.getNombreExemplairesEmpruntes());
        
        // Créer le bouton pour fermer la fenêtre
        Button fermerButton = new Button("Fermer");
        fermerButton.setOnAction(event -> {
            Stage stage = (Stage) fermerButton.getScene().getWindow();
            stage.close();
        });

        // Créer une HBox pour aligner le bouton en bas à droite
        HBox buttonBox = new HBox();
        buttonBox.getChildren().add(fermerButton);
        buttonBox.setAlignment(Pos.BOTTOM_RIGHT);

        // Ajouter les éléments à la racine
        root.getChildren().addAll(statistiquesLabel, nombreTotalLivresLabel, nombreExemplairesEmpruntesLabel, buttonBox);

        return root;
    }
}

