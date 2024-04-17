import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Utilisateur {
    private int numeroIdentification;
    private String nom;
    private ArrayList<Livre> livresEmpruntes; // Ajout de l'ArrayList pour stocker les livres empruntés

    public Utilisateur(int numeroIdentification, String nom) {
        this.numeroIdentification = numeroIdentification;
        this.nom = nom;
        this.livresEmpruntes = new ArrayList<>(); // Initialisation de l'ArrayList
    }

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

    public void ajouterUtilisateur() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO utilisateur (NumeroIdentification, Nom) VALUES (?, ?)");
            preparedStatement.setInt(1, numeroIdentification);
            preparedStatement.setString(2, nom);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean verifierEligibilite() {
        throw new UnsupportedOperationException("Unimplemented method 'verifierEligibilite'");
    }

    public void emprunterLivre(Livre livre) {
        livresEmpruntes.add(livre);
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.enregistrerEmprunt(this.getNumeroIdentification(), livre.getISBN());
    }
    
    public void retournerLivre(Livre livre) {
        livresEmpruntes.remove(livre);
        Bibliotheque bibliotheque = new Bibliotheque();
        bibliotheque.enregistrerRetour(this.getNumeroIdentification(), livre.getISBN());
    }
    
    public void afficherLivresEmpruntes() {
        System.out.println("Livres empruntés par " + nom + " (ID: " + numeroIdentification + "):");
        for (Livre livre : livresEmpruntes) {
            System.out.println(livre.toString());
            System.out.println(); // Saut de ligne pour séparer les livres
        }
    }
}
