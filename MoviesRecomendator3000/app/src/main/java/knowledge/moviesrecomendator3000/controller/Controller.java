package knowledge.moviesrecomendator3000.controller;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import knowledge.moviesrecomendator3000.model.Movie;
import knowledge.moviesrecomendator3000.util.OntologyHandler;

public class Controller {

	public static Movie recomend(String mood, String companion, InputStream fileIS) {
		OntologyHandler ontologyHandler = null;
		try {
			ontologyHandler = new OntologyHandler(fileIS);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (IOException e) {
            e.printStackTrace();
        }

        try {
			OWLIndividual user = ontologyHandler.getIndividual("Default");
			OWLIndividual family = ontologyHandler.getIndividual(companion);
			OWLIndividual happy = ontologyHandler.getIndividual(mood);

			OWLObjectProperty feels = ontologyHandler.getObjectProperty("feels");
			OWLObjectProperty relatedTo = ontologyHandler.getObjectProperty("relatedTo");
			
			ontologyHandler.relateIndividuals(relatedTo, user, family);
			ontologyHandler.relateIndividuals(feels, user, happy);
			ontologyHandler.syncronizeReasoner();
			
			Set<OWLNamedIndividual> recommendeds = ontologyHandler.getIndividualsOf("Recommended");
			for (OWLNamedIndividual owlNamedIndividual : recommendeds) {
				Log.i("result", "" + owlNamedIndividual);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		Movie a = new Movie();
		a.setTitle("Oi");
		return a;
	}
}
