package puiss4;

public class Coup {
    
    int colonne;
    int valeur;
    
    public Coup(int c, int v) {
        colonne = c;
        valeur = v;
    }
    
    @Override
    public String toString() {
        String s =  "Colonne : " + colonne + ". Valeur : " + valeur;
        return s;
    }
    
}
