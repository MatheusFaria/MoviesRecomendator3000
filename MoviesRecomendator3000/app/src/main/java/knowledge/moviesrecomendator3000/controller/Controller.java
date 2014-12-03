package knowledge.moviesrecomendator3000.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import knowledge.moviesrecomendator3000.util.Logger;
import knowledge.moviesrecomendator3000.util.OntologyHandler;

public class Controller {

    private static OntologyHandler ontologyHandler;

    public static ArrayList<String> recommend(String mood, String companion, InputStream fileIS) {

        ArrayList<String> recommendedMovies = new ArrayList();

        try {
            ontologyHandler = new OntologyHandler(fileIS);

            OWLNamedIndividual user = ontologyHandler.getIndividual("Default");
            OWLObjectProperty feels = ontologyHandler.getObjectProperty("feels");
            OWLObjectProperty relatedTo = ontologyHandler.getObjectProperty("isRelatedTo");

			OWLIndividual family = ontologyHandler.getIndividual(companion);
			OWLIndividual happy = ontologyHandler.getIndividual(mood);

			ontologyHandler.relateIndividuals(relatedTo, user, family);
			ontologyHandler.relateIndividuals(feels, user, happy);

			ontologyHandler.synchronizeReasoner();

			Set<OWLNamedIndividual> recommended = ontologyHandler.getIndividualsOf("Recommended");
			for (OWLNamedIndividual owlNamedIndividual : recommended) {
                recommendedMovies.add(owlNamedIndividual.toString());
				Logger.debug(owlNamedIndividual.toString());
			}
		} catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

	    return recommendedMovies;
	}
}
