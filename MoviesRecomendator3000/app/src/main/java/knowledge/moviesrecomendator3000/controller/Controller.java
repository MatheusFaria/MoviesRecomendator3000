package knowledge.moviesrecomendator3000.controller;

import java.util.Set;

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import knowledge.moviesrecomendator3000.model.Movie;
import knowledge.moviesrecomendator3000.util.Log;
import knowledge.moviesrecomendator3000.util.OntologyHandler;

public class Controller {
	private static final String ONTOLOGY_PATH = "data/movies_recomendation.owl";

	public static Movie recomend(String mood, String companion) {
		OntologyHandler ontologyHandler = null;
		try {
			ontologyHandler = new OntologyHandler(Controller.ONTOLOGY_PATH);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		
		try {
			OWLIndividual user = ontologyHandler.getIndividual("Matheus");
			OWLIndividual family = ontologyHandler.getIndividual("Family");
			OWLIndividual happy = ontologyHandler.getIndividual("Happy");
			OWLObjectProperty feels = ontologyHandler.getObjectProperty("feels");
			OWLObjectProperty relatedTo = ontologyHandler.getObjectProperty("relatedTo");
			
			ontologyHandler.createRelation(relatedTo, user, family);
			ontologyHandler.createRelation(feels, user, happy);
			
			ontologyHandler.syncronizeReasoner();
			
			Set<OWLNamedIndividual> recommendeds = ontologyHandler.getIndividualsOf("Recommended");
			for (OWLNamedIndividual owlNamedIndividual : recommendeds) {
				Log.debug(""+ owlNamedIndividual);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Movie a = new Movie();
		a.setTitle("Oi");
		return a;
	}

}
