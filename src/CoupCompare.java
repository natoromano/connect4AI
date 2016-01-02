package puiss4;

import java.util.Comparator;

/** Comparator for 'Coup' objects */
public class CoupCompare implements Comparator<Coup> {

    @Override
    public int compare(Coup o1, Coup o2) {
        return (o1.valeur - o2.valeur);
    }
    
}
