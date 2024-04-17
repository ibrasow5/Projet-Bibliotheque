public class Livre {
    private String ISBN; 
    private String titre; 
    private String auteur; 
    private int anneePublication; 

    // Constructeur
    public Livre(String ISBN, String titre, String auteur, int anneePublication) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
    }

    // Getters
    public String getISBN() {
        return ISBN;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    // Setters
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    // Méthode toString() pour afficher les détails du livre. 
    @Override
    public String toString() {
        return "Titre: " + titre + "\n" +
               "Auteur: " + auteur + "\n" +
               "Année de publication: " + anneePublication + "\n" +
               "ISBN: " + ISBN;
    }
}
