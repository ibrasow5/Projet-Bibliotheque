

public class Livre {
    private String ISBN;
    private String titre;
    private String auteur;
    private int anneePublication;

    public Livre(String ISBN, String titre, String auteur, int anneePublication) {
        this.ISBN = ISBN;
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    @Override
    public String toString() {
        return "Titre: " + titre + "\n" +
               "Auteur: " + auteur + "\n" +
               "Année de publication: " + anneePublication + "\n" +
               "ISBN: " + ISBN;
    }
}