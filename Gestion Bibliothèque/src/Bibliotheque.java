import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Bibliotheque {
    private ArrayList<Livre> listeLivres;
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL_BD = "jdbc:mysql://localhost:3306/bibliotheque";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "";

    private static final String ISBN = null;

    public Bibliotheque() {
        this.listeLivres = new ArrayList<>();
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void ajouterLivre(Livre livre) {
        listeLivres.add(livre);
        try (Connection connection = DriverManager.getConnection(URL_BD, UTILISATEUR, MOT_DE_PASSE)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO livre (ISBN, Titre, Auteur, AnneePublication) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, livre.getISBN());
            preparedStatement.setString(2, livre.getTitre());
            preparedStatement.setString(3, livre.getAuteur());
            preparedStatement.setInt(4, livre.getAnneePublication());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerLivre(String isbn) {
        try (Connection connection = DriverManager.getConnection(URL_BD, UTILISATEUR, MOT_DE_PASSE);
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM livre WHERE ISBN = ?")) {
            preparedStatement.setString(1, isbn);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void modifierLivre(String nouveauTitre, String nouvelAuteur, int nouvelleAnneePublication) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE livre SET Titre = ?, Auteur = ?, AnneePublication = ? WHERE ISBN = ?");
            preparedStatement.setString(1, nouveauTitre);
            preparedStatement.setString(2, nouvelAuteur);
            preparedStatement.setInt(3, nouvelleAnneePublication);
            preparedStatement.setString(4, ISBN);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 

    public static Livre rechercherLivre(String critere, String valeur) {
        Livre livreTrouve = null;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = null;
            
            if (critere.equalsIgnoreCase("ISBN")) {
                preparedStatement = connection.prepareStatement("SELECT * FROM livre WHERE ISBN = ?");
            } else if (critere.equalsIgnoreCase("Titre")) {
                preparedStatement = connection.prepareStatement("SELECT * FROM livre WHERE Titre = ?");
            } else if (critere.equalsIgnoreCase("Auteur")) {
                preparedStatement = connection.prepareStatement("SELECT * FROM livre WHERE Auteur = ?");
            }
            
            if (preparedStatement != null) {
                preparedStatement.setString(1, valeur);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    livreTrouve = new Livre(resultSet.getString("ISBN"), resultSet.getString("Titre"), resultSet.getString("Auteur"), resultSet.getInt("AnneePublication"));
                }
                preparedStatement.close();
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livreTrouve;
    }

    public boolean verifierEligibilite(int numeroIdentification) {
        boolean estEligible = false;
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM utilisateur WHERE NumeroIdentification = ?");
            preparedStatement.setInt(1, numeroIdentification);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                estEligible = true;
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estEligible;
    }

    public void enregistrerEmprunt(int numeroIdentification, String ISBN) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO emprunt (NumeroIdentification, ISBN, DateEmprunt) VALUES (?, ?, ?)");
            preparedStatement.setInt(1, numeroIdentification);
            preparedStatement.setString(2, ISBN);
            preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public void enregistrerRetour(int numeroIdentification, String ISBN) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bibliotheque", "root", "");
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE emprunt SET DateRetour = ? WHERE NumeroIdentification = ? AND ISBN = ?");
            preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(2, numeroIdentification);
            preparedStatement.setString(3, ISBN);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public int nombreLivresEmpruntes(Utilisateur utilisateur) {
        int nombreEmprunts = 0;
        try {
            Connection connection = DriverManager.getConnection(URL_BD, UTILISATEUR, MOT_DE_PASSE);
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM emprunt WHERE UtilisateurID = ?");
            preparedStatement.setInt(1, utilisateur.getNumeroIdentification());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                nombreEmprunts = resultSet.getInt(1);
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreEmprunts;
    }

    public void afficherStatistiques() {
        try {
            Connection connection = DriverManager.getConnection(URL_BD, UTILISATEUR, MOT_DE_PASSE);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM livre");
            if (resultSet.next()) {
                System.out.println("Nombre total de livres : " + resultSet.getInt(1));
            }
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM emprunt");
            if (resultSet.next()) {
                System.out.println("Nombre total d'emprunts : " + resultSet.getInt(1));
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateur.ajouterUtilisateur();
    }

    public void modifierLivre(Livre livre, String nouveauTitre, String nouvelAuteur, int nouvelleAnnee) {
        throw new UnsupportedOperationException("Unimplemented method 'modifierLivre'");
    }

}
