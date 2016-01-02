package puiss4;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Solver {

    public static int MiniMax(Jeu jeu) {
        if (jeu.estFini() || jeu.estPlein()) {
            return jeu.eval();
        } else if (jeu.joueur == 2) {

            int min = 1000;
            for (int c = 0; c < jeu.L; c++) {
                if (!jeu.colonnePleine(c)) {
                    jeu.addJeton(c);
                    int val = MiniMax(jeu);

                    if (val <= min) {
                        min = val;
                    }
                    jeu.removeJeton(c);
                }
            }
            return min;
        } else if (jeu.joueur == 1) {

            int max = -10000;
            for (int c = 0; c < jeu.L; c++) {
                if (!jeu.colonnePleine(c)) {
                    jeu.addJeton(c);
                    int val = MiniMax(jeu);

                    if (val >= max) {
                        max = val;
                    }
                    jeu.removeJeton(c);
                }
            }
            return max;
        }
        return 100;
    }


    public static int NegaMax(Jeu jeu) {
        if (jeu.estFini() || jeu.estPlein()) {
            if (jeu.joueur == 1) {
                return jeu.eval();
            } else {
                return (-jeu.eval());
            }
        } else {
            int max = -10000;
            for (int c = 0; c < jeu.L; c++) {
                if (!jeu.colonnePleine(c)) {
                    jeu.addJeton(c);
                    int val = -NegaMax(jeu);
                    if (val >= max) {
                        max = val;
                    }
                    jeu.removeJeton(c);
                }
            }
            return max;
        }
    }


    public static int AlphaBeta(Jeu jeu, int alpha, int beta) {
        if (jeu.estFini() || jeu.estPlein()) {
            if (jeu.joueur == 1) {
                return jeu.eval();
            } else {
                return -jeu.eval();
            }
        } else {
            int max = -1000;
            int a = alpha;
            int b = beta;
            for (int c = 0; c < jeu.L; c++) {
                if (!jeu.colonnePleine(c)) {
                    jeu.addJeton(c);
                    int val = -AlphaBeta(jeu, -b, -a);
                    if (val > max) {
                        max = val;
                    }
                    if (max > a) {
                        a = val;
                    }
                    if (a >= b) {
                        jeu.removeJeton(c);
                        break;
                    }
                    jeu.removeJeton(c);
                }
            }
            return max;
        }
    }


    public static int AlphaBetaMemoisation(Jeu jeu, int alpha, int beta, HashMap<Jeu, TripleValue> hash) {
        if (jeu.estFini()) {
            if (jeu.joueur == 1) {
                return jeu.eval();
            } else {
                return -jeu.eval();
            }
        } else {
            if (hash.containsKey(jeu)) {
                TripleValue x = hash.get(jeu);
                int alph = x.alpha;
                int bet = x.beta;
                int value = x.value;

                if (value <= alph) { // On est dans le cas upper bound
                    beta = Math.min(beta, value);
                } else if (value >= bet) { // cas lower bound
                    alpha = Math.max(alph, value);
                }
                if (alph < value && value < beta) {
                    return value;
                }
            }
            int max = -1000;
            for (int c = 0; c < jeu.L; c++) {
                if (!jeu.colonnePleine(c)) {
                    jeu.addJeton(c);
                    int val = -AlphaBetaMemoisation(jeu, -beta, -alpha, hash);
                    if (val > max) {
                        max = val;
                    }
                    if (max > alpha) {
                        alpha = val;
                    }
                    if (max >= beta) {
                        jeu.removeJeton(c);
                        break;
                    }

                    jeu.removeJeton(c);
                    if (jeu.profondeur <= jeu.H * jeu.L) {
                        TripleValue foo = new TripleValue();
                        foo.alpha = alpha;
                        foo.beta = beta;
                        foo.value = val;

                        hash.put(jeu, foo);
                    }

                }
            }
            return max;
        }
    }


    public static int AlphaBetaMemoisation2(Jeu jeu, int alpha, int beta, HashMap<Jeu, IntervalValue> hash) {
        if (jeu.estFini()) {
            if (jeu.joueur == 1) {
                return jeu.eval();
            } else {
                return -jeu.eval();
            }
        }

        if (hash.containsKey(jeu) && hash.get(jeu).profmax >= jeu.profondeur) {
            IntervalValue x = hash.get(jeu);

            if (x.bound == -1) {
                alpha = Math.max(alpha, x.value);
            } else if (x.bound == 1) {
                beta = Math.min(beta, x.value);
            } else if (x.bound == 0) {
                alpha = x.value;
                beta = x.value;
            }
            if (alpha >= beta) {
                return x.value;
            }
        }

        int max = -1000;
        for (int c = 0; c < jeu.L; c++) {
            if (!jeu.colonnePleine(c)) {
                jeu.addJeton(c);
                int val = -AlphaBetaMemoisation2(jeu, -beta, -alpha, hash);
                if (val > max) {
                    max = val;
                }
                if (max > alpha) {
                    alpha = val;
                }
                if (alpha >= beta) {
                    jeu.removeJeton(c);
                    break;
                }
                jeu.removeJeton(c);
            }
        }

        IntervalValue saved = new IntervalValue();
        if (max <= alpha) {
            saved.bound = 1;

        } else if (max >= beta) {
            saved.bound = -1;
        } else {
            saved.bound = 0;
        }
        saved.profmax = jeu.profondeur;
        saved.value = max;
        hash.put(jeu, saved);
        return max;
    }


    public static int AlphaBetaMemoisationProfMax(Jeu jeu, int alpha, int beta, HashMap<Jeu, IntervalValue> hash, int profmax) {
        if (jeu.estFini() || jeu.profondeur >= profmax) {
            if (jeu.joueur == 1) {
                return jeu.evalHeuristique();
            } else {
                return -jeu.evalHeuristique();
            }
        }
        int a = alpha;
        int b = beta;
        if (hash.containsKey(jeu)) {
            IntervalValue x = hash.get(jeu);

            if (x.value == 1000 || x.value == -1000) {
                return x.value;
            }

            if (hash.get(jeu).profmax == profmax) {
                if (x.bound == -1) {
                    a = Math.max(a, x.value);
                } else if (x.bound == 1) {
                    b = Math.min(b, x.value);
                } else if (x.bound == 0) {
                    a = x.value;
                    b = x.value;
                }
                if (a >= b) {
                    return x.value;
                }
            }

        }
        //LinkedList<Integer> best = new LinkedList<>();
        int max = -1000000;
        int best = -1;
        LinkedList<Integer> moves = jeu.moveOrdering(hash);

        for (int c : moves) {
            if (!jeu.colonnePleine(c)) {
                jeu.addJeton(c);
                int val = -AlphaBetaMemoisationProfMax(jeu, -b, -a, hash, profmax);

                if (val > max) {
                    max = val;
                    best = c;
                }
                if (max > a) {
                    a = val;
                }
                if (a >= b) {
                    jeu.removeJeton(c);
                    best = c;
                    break;
                }
                jeu.removeJeton(c);
            }
        }

        IntervalValue saved = new IntervalValue();
        if (max <= a) {
            saved.bound = 1;
        } else if (max >= b) {
            saved.bound = -1;
        } else {
            saved.bound = 0;
        }
        saved.profmax = profmax;
        saved.value = max;
        saved.bestmove = best;
        hash.put(jeu, saved);
        return max;
    }


    public static int AlphaBetaMemoisationProfMaxSym(Jeu jeu, int alpha, int beta, HashMap<Jeu, IntervalValue> hash, int profmax) {
        if (jeu.estFini() || jeu.profondeur >= profmax) {
            if (jeu.joueur == 1) {
                return jeu.evalHeuristique();
            } else {
                return -jeu.evalHeuristique();
            }
        }
        if (hash.containsKey(jeu)) {
            IntervalValue x = hash.get(jeu);

            if (x.value == 1000 || x.value == -1000) {
                return x.value;
            }

            if (hash.get(jeu).profmax == profmax) {
                if (x.bound == -1) {
                    alpha = Math.max(alpha, x.value);
                } else if (x.bound == 1) {
                    beta = Math.min(beta, x.value);
                } else if (x.bound == 0) {
                    alpha = x.value;
                    beta = x.value;
                }
                if (alpha >= beta) {
                    return x.value;
                }
            }
        }
        jeu.symetrie();
        if (hash.containsKey(jeu)) {
            IntervalValue x = hash.get(jeu);

            if (x.value == 1000 || x.value == -1000) {
                jeu.symetrie();
                return x.value;
            }

            if (hash.get(jeu).profmax == profmax) {
                if (x.bound == -1) {
                    alpha = Math.max(alpha, x.value);
                } else if (x.bound == 1) {
                    beta = Math.min(beta, x.value);
                } else if (x.bound == 0) {
                    alpha = x.value;
                    beta = x.value;
                }
                if (alpha >= beta) {
                    jeu.symetrie();
                    return x.value;
                }
            }
        }
        jeu.symetrie();
        //LinkedList<Integer> best = new LinkedList<>();
        int max = -1000000;
        int best = -1;
        LinkedList<Integer> moves = jeu.moveOrdering(hash);

        for (int c : moves) {
            if (!jeu.colonnePleine(c)) {
                jeu.addJeton(c);
                int val = -AlphaBetaMemoisationProfMaxSym(jeu, -beta, -alpha, hash, profmax);

                if (val > max) {
                    max = val;
                    best = c;
                }
                if (max > alpha) {
                    alpha = val;
                }
                if (alpha >= beta) {
                    jeu.removeJeton(c);
                    best = c;
                    break;
                }
                jeu.removeJeton(c);
            }
        }

        IntervalValue saved = new IntervalValue();
        if (max <= alpha) {
            saved.bound = 1;
        } else if (max >= beta) {
            saved.bound = -1;
        } else {
            saved.bound = 0;
        }
        saved.profmax = profmax;
        saved.value = max;
        saved.bestmove = best;
        hash.put(jeu, saved);
        return max;
    }


    public static int ApproIte(Jeu jeu, int alpha, int beta, HashMap<Jeu, IntervalValue> hash) {
        HashMap<Jeu, IntervalValue> hash2 = new HashMap<>();
        int valeur = 0;
        int prof = jeu.profondeur;
        for (int p = prof; p <= jeu.H * jeu.L; p += 2) {
            valeur = AlphaBetaMemoisationProfMaxSym(jeu, alpha, beta, hash2, p);
            //String s = valeur + " à la profondeur : " + p;
            //System.out.println(s);
            if (valeur == 1000) {
                return valeur;
            }
            if (valeur == -1000) {
                return valeur;
            }
        }
        return 0;
    }


    public static int ApproIteNegaScout(Jeu jeu, int delta, HashMap<Jeu, IntervalValue> hash) {
        int guess = 0;
        int valeur = 0;
        int prof = jeu.profondeur;
        for (int p = prof; p <= jeu.H * jeu.L; p += 2) {
            int alpha = guess - delta;
            int beta = guess + delta;
            valeur = NegaScout(jeu, alpha, beta, delta, hash, p);
            if (valeur > beta) {
                alpha = valeur;
                beta = 1000000;
                valeur = NegaScout(jeu, alpha, beta, delta, hash, p);
            } else if (valeur < alpha) {
                alpha = -1000000;
                beta = valeur;
                valeur = NegaScout(jeu, alpha, beta, delta, hash, p);
            }
            //String s = valeur + " à la profondeur : " + p;
            //System.out.println(s);
            if (valeur == 1000) {
                return valeur;
            }
            if (valeur == -1000) {
                return valeur;
            }
            guess = valeur;
        }
        return 0;
    }


    public static int NegaScout(Jeu jeu, int alpha, int beta, int delta, HashMap<Jeu, IntervalValue> hash, int profmax) {
        if (jeu.profondeur == profmax || jeu.estFini()) {
            if (jeu.joueur == 1) {
                return jeu.evalHeuristique();
            } else {
                return -jeu.evalHeuristique();
            }
        } else {
            LinkedList<Integer> moves = jeu.moveOrdering(hash);
            jeu.addJeton(moves.get(0));
            int valeur = -AlphaBetaMemoisationProfMaxSym(jeu, -beta, -alpha, hash, profmax);
            jeu.removeJeton(moves.get(0));
            if (valeur < beta) {
                int size = moves.indexOf(moves.getLast());
                for (int i = 1; i <= size; i++) {
                    int c = moves.get(i);
                    int lbound = Math.max(valeur, alpha);
                    int ubound = lbound + delta;
                    jeu.addJeton(c);
                    int result = -AlphaBetaMemoisationProfMaxSym(jeu, -ubound, -lbound, hash, profmax);
                    if (result >= ubound && result < beta) {
                        result = -AlphaBetaMemoisationProfMaxSym(jeu, -beta, -result, hash, profmax);
                    }
                    if (result > valeur) {
                        valeur = result;
                    }
                    if (result >= beta) {
                        jeu.removeJeton(c);
                        break;
                    }
                    jeu.removeJeton(c);
                }
            }
            return (valeur);
        }
    }

}
