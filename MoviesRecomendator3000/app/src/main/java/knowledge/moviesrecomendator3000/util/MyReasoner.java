package knowledge.moviesrecomendator3000.util;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.model.OWLOntology;

class MyReasoner extends Reasoner {

    public MyReasoner(OWLOntology rootOntology) {
        super(rootOntology);
    }

    @Override
    public void dispose() {
        // Do nothing
    }
}