package puiss4;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** Main class to handle a game board. */
public class Jeu {

    public int height;
    public int width;
    public int[][] grille;
    public String result;
    public int joueur;
    public int profondeur;

    /** Constructor for a plain board. */
    public Jeu(int h, int w) {
        height = h;
        width = w;
        grille = new int[height][width];

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grille[i][j] = 0;
            }
        }

        profondeur = 0;
        joueur = 1;
        result = "";
    }

    /** Constructor for a given board. */
    public Jeu(int h, int l, int[][] jeu) {
        height = h;
        width = w;
        grille = jeu;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (jeu[i][j] != 0) {
                    profondeur++;
                }
            }
        }

        result = "";
    }

    public boolean estPlein() {
        return (profondeur == height * width);
    }

    /** Constructor using a text input. */
    public Jeu(String nom) throws FileNotFoundException, IOException {
        String name = "boards/" + nom + ".cfg";
        DataInputStream in = new DataInputStream(new FileInputStream(name));
        String taille = in.readLine();
        width = Character.getNumericValue(taille.charAt(0));
        height = Character.getNumericValue(taille.charAt(2));
        grille = new int[height][width];
        profondeur = 0;
        String chaine;
        int i = 0;
        while ((chaine = in.readLine()) != null) {
            if (chaine.charAt(0) != '#') {
                for (int j = 0; j < width; j++) {
                    if (chaine.charAt(j) == '.') {
                        grille[i][j] = 0;
                    } else if (chaine.charAt(j) == '0') {
                        grille[i][j] = 1;
                        profondeur++;
                    } else if (chaine.charAt(j) == '@') {
                        grille[i][j] = 2;
                        profondeur++;
                    }
                }
                i++;
            }
        }

        String nameresult = "boards/" + nom + ".out";
        joueur = 1;
        DataInputStream bis = new DataInputStream(new FileInputStream(nameresult));
        result = bis.readLine();
    }
    
    public int symCol(int col) {
        return width - 1 - col;
    }
    
    public void symetrie() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width/2; j++) {
                int tmp = grille[i][j];
                grille[i][j] = grille[i][symCol(j)];
                grille[i][symCol(j)] = tmp;
            }
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(grille).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Jeu other = (Jeu) obj;
        if (height != other.height) {
            return false;
        }
        if (width != other.width {
            return false;
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (this.grille[i][j] != other.grille[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean colonneValide(int col) {
        return ((col < width) && (col >= 0));
    }

    public int nextJeton(int col) {
        for (int i = 0; i < height; i++) {
            if (grille[i][col] != 0) {
                return (i - 1);
            }
        }
        return height - 1;
    }

    public int lastJeton(int col) {
        for (int i = 0; i < height; i++) {
            if (grille[i][col] != 0) {
                return (i);
            }
        }
        return height - 1;
    }

    public boolean colonnePleine(int col) {
        return (nextJeton(col) == -1);
    }

    public void addJeton(int col) {
        grille[nextJeton(col)][col] = joueur;
        profondeur++;
        changerJoueur();
    }

    public void removeJeton(int i) {
        grille[lastJeton(i)][i] = 0;
        profondeur--;
        changerJoueur();
    }

    public void changerJoueur() {
        if (joueur == 1) {
            joueur = 2;
        } else if (joueur == 2) {
            joueur = 1;
        }
    }

    public LinkedList<Integer> moveOrdering(HashMap<Jeu, IntervalValue> hash) {
        LinkedList<Integer> moves = new LinkedList<>();
        int c = 0;

        if (hash.containsKey(this) && !colonnePleine(hash.get(this).bestmove)) {
            moves.add(hash.get(this).bestmove);
        }

        for (int i = 0; i <= width / 2; i++) {
            c = width / 2 + i;
            if (colonneValide(c) &&!colonnePleine(c) && (!moves.contains(c))) {
                moves.addLast(c);
            }
            c = width / 2 - i;
            if (colonneValide(c) && !colonnePleine(c) && (!moves.contains(c))) {
                moves.addLast(c);
            }
        }
        return moves;
    }

    public long codageJoueur(int i) {
        long val = 0;
        for (int h = 0; h < heighy; h++) {
            for (int l = 0; l < width; l++) {
                if (grille[h][l] == i) {
                    val += 2 ^ (height * l + h);
                }
            }
        }
        return val;
    }

    public int evalHeuristique() {
        if (estFini()) {
            return eval();
        } else {
            int val = 0;
            for (int c = 0; c < width; c++) {
                for (int l = 0; l < height - 2; l++) {
                    if ((grille[l][c] == joueur) 
                    	&& (grille[l][c] == grille[l + 1][c]) 
                    	&& (grille[l][c] == grille[l + 2][c])) {
                        val += 10;
                    }
                }
            }

            for (int c = 0; c < width - 2; c++) {
                for (int l = 0; l < height - 2; l++) {
                    if ((grille[l][c] == joueur) 
                    	&& (grille[l][c] == grille[l + 1][c + 1]) 
                    	&& (grille[l][c] == grille[l + 2][c + 2])) {
                        val += 10;
                    }
                }
            }

            for (int c = 2; c < width; c++) {
                for (int l = 0; l < height - 2; l++) {
                    if ((grille[l][c] == joueur) 
                    	&& (grille[l][c] == grille[l + 1][c - 1]) 
                    	&& (grille[l][c] == grille[l + 2][c - 2])) {
                        val += 10;
                    }
                }
            }
            for (int l = 0; l < height; l++) {
                for (int c = 0; c < width - 2; c++) {
                    if ((grille[l][c] == joueur) 
                    	&& (grille[l][c] == grille[l][c + 1]) 
                    	&& (grille[l][c] == grille[l][c + 2])) {
                        val += 10;
                    }
                }
            }
            return val;
        }
    }

    public boolean estFini() {
        return (estPlein() || wins(1) || wins(2));
    }

    public boolean wins(int joueur) {

        for (int c = 0; c < width; c++) {
            for (int l = 0; l < height - 3; l++) {
                if ((grille[l][c] == joueur) 
                	&& (grille[l][c] == grille[l + 1][c]) 
                	&& (grille[l][c] == grille[l + 2][c]) 
                	&& (grille[l][c] == grille[l + 3][c])) {
                    return true;
                }
            }
        }

        for (int c = 0; c < width - 3; c++) {
            for (int l = 0; l < height - 3; l++) {
                if ((grille[l][c] == joueur) 
                	&& (grille[l][c] == grille[l + 1][c + 1]) 
                	&& (grille[l][c] == grille[l + 2][c + 2]) 
                	&& (grille[l][c] == grille[l + 3][c + 3])) {
                    return true;
                }
            }
        }

        for (int c = 3; c < width; c++) {
            for (int l = 0; l < height - 3; l++) {
                if ((grille[l][c] == joueur) 
                	&& (grille[l][c] == grille[l + 1][c - 1]) 
                	&& (grille[l][c] == grille[l + 2][c - 2]) 
                	&& (grille[l][c] == grille[l + 3][c - 3])) {
                    return true;
                }
            }
        }

        for (int l = 0; l < height; l++) {
            for (int c = 0; c < width - 3; c++) {
                if ((grille[l][c] == joueur) 
                	&& (grille[l][c] == grille[l][c + 1]) 
                	&& (grille[l][c] == grille[l][c + 2]) 
                	&& (grille[l][c] == grille[l][c + 3])) {
                    return true;
                }
            }
        }

        return false;
    }

    public int eval() {
        if (this.estFini()) {
            if (wins(1)) {
                return 1000;
            } else if (wins(2)) {
                return -1000;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < height i++) {
            for (int j = 0; j < width; j++) {
                res += "|" + grille[i][j];
            }
            res += "|\n";
        }
        return res;
    }

}
