package puiss4;

import java.io.IOException;
import java.util.HashMap;

public class Puiss4 {

    public static boolean Solve(Jeu jeu) {

        long depart = System.currentTimeMillis();

        //int resultat = Solver.MiniMax(jeu);
        //int resultat = Solver.NegaMax(jeu);
        //int resultat = Solver.AlphaBeta(jeu, -1000000, +1000000);
        //int resultat = Solver.AlphaBetaMemoisation(jeu, -1000000,+1000000, new HashMap<>());
        //int resultat = Solver.AlphaBetaMemoisation2(jeu, -1000000, +1000000, new HashMap<>());
        //int resultat = Solver.AlphaBetaMemoisationProfMax(jeu, -1000000, +1000000, new HashMap<>(), jeu.H * jeu.L);
        //int resultat = Solver.ApproIte(jeu, -1000000, +1000000, new HashMap<>());
        int resultat = Solver.ApproIteNegaScout(jeu,50, new HashMap<>());

        String val = "";
        if (resultat < 0) {
            val = "LOSS";
            System.out.println(val);
        }
        if (resultat == 0) {
            val = "DRAW";
            System.out.println(val);
        }
        if (resultat > 0) {
            val = "WIN";
            System.out.println(val);
        }

        double fin = System.currentTimeMillis();

        System.out.println("Résultat attendu : " + jeu.result);
        System.out.println("Effectué en " + (fin - depart) + "ms");
        System.out.println("");
        return (val.equals(jeu.result));
    }

    public static void main(String[] args) throws IOException {

        boolean ok = true;

        for (int i = 18; i < 40; i += 2) {
            Jeu game = new Jeu("play-" + i);
            ok = ok && Solve(game);
        }

        for (int i = 16; i < 34; i += 2) {
            Jeu game = new Jeu("game-" + i);
            ok = ok && Solve(game);
        }

        for (int i = 16; i < 22; i += 2) {
            Jeu game = new Jeu("jeu-" + i);
            ok = ok && Solve(game);
        }

        for (int i = 14; i < 16; i += 2) {
            Jeu game = new Jeu("partie-" + i);
            ok = ok && Solve(game);
        }

        Jeu game = new Jeu("damned");
        ok = ok && Solve(game);

        game = new Jeu("damned2");
        ok = ok && Solve(game);

        game = new Jeu("win1");
        ok = ok && Solve(game);

        game = new Jeu("ended");
        ok = ok && Solve(game);

        game = new Jeu("trivial");
        ok = ok && Solve(game);

        game = new Jeu("trivial2"); 
        ok = ok && Solve(game);
        
        game = new Jeu("empty-4x4");
        ok = ok && Solve(game);

        if (ok == true) {
            System.out.println("RESULTAT OK");
        } else {
            System.out.println("PROBLEME");
        }

    }

}
