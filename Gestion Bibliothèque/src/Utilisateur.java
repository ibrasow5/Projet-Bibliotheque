import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Utilisateur {
    private int numeroIdentification;
    private String nom;
    private ArrayList<Livre> livresEmpruntes; 

    // Constructeur
    public Utilisateur(int numeroIdentification, String nom) {
        this.numeroIdentification = numeroIdentification;
        this.nom = nom;
        this.livresEmpruntes = new ArrayList<>(); // Initialisation de la liste des livres empruntés
    }

    // Getters et setters
    public int getNumeroIdentification() {
        return numeroIdentification;
    }

    public void setNumeroIdentification(int numeroIdentification) {
        this.numeroIdentification = numeroIdentification;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public ArrayList<Livre> getLivresEmpruntes() {
        return livresEmpruntes;
    }

    // Méthode pour ajouter un utilisateur
    public void ajouterUtilisateur() throws LibraryException {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO utilisateur (NumeroIdentification, Nom) VALUES (?, ?)");
            preparedStatement.setInt(1, numeroIdentification);
            preparedStatement.setString(2, nom);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new LibraryException("Erreur lors de l'ajout de l'utilisateur.", e);
        }
    }
    

    // Méthode pour emprunter un livre
    public void emprunterLivre(Livre livre) throws LibraryException {
        int nombreMaxEmprunts = 3;
        if (livresEmpruntes.size() >= nombreMaxEmprunts) {
            throw new LibraryException("Vous avez déjà atteint le nombre maximum d'emprunts autorisés.", null);
        }
        livresEmpruntes.add(livre);
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.enregistrerEmprunt(this.getNumeroIdentification(), livre.getISBN());
        System.out.println("Livre emprunté avec succès !");
    } 
    
    // Méthode pour retourner un livre
    public void retournerLivre(Livre livre) {
        livresEmpruntes.remove(livre);
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.enregistrerRetour(this.getNumeroIdentification(), livre.getISBN());
    }
    
    // Méthode pour afficher les livres empruntés par l'utilisateur
    public void afficherLivresEmpruntes() {
        System.out.println("Livres empruntés par " + nom + " (ID: " + numeroIdentification + "):");
        for (Livre livre : livresEmpruntes) {
            System.out.println("Titre: " + livre.getTitre());
            System.out.println("Auteur: " + livre.getAuteur());
            System.out.println("Année de publication: " + livre.getAnneePublication());
            System.out.println("ISBN: " + livre.getISBN());
        }
    }
}
