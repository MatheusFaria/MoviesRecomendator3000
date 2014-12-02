package knowledge.moviesrecomendator3000.controller;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import knowledge.moviesrecomendator3000.model.Movie;
import knowledge.moviesrecomendator3000.util.OntologyHandler;

public class Controller {

    private static OntologyHandler ontologyHandler;
    private static OWLIndividual user;
    private static OWLObjectProperty feels, relatedTo;

	public static ArrayList<Movie> recomend(String mood, String companion) {

        ArrayList<Movie> recommendedMovies = new ArrayList<Movie>();

        try {
			OWLIndividual family = ontologyHandler.getIndividual(companion);
			OWLIndividual happy = ontologyHandler.getIndividual(mood);
			
			ontologyHandler.relateIndividuals(relatedTo, user, family);
			ontologyHandler.relateIndividuals(feels, user, happy);
			ontologyHandler.syncronizeReasoner();
			
			Set<OWLNamedIndividual> recommended = ontologyHandler.getIndividualsOf("Recommended");
			for (OWLNamedIndividual owlNamedIndividual : recommended) {
                Movie newRecommendedMovie = new Movie();
                newRecommendedMovie.setTitle("" + owlNamedIndividual);

                recommendedMovies.add(newRecommendedMovie);
				Log.i("Result", "" + owlNamedIndividual);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return recommendedMovies;
	}

    public static void initializeOntology(InputStream ontologyFileIS) {
        try {
            ontologyHandler = new OntologyHandler(ontologyFileIS);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            user = ontologyHandler.getIndividual("Default");
            feels = ontologyHandler.getObjectProperty("feels");
            relatedTo = ontologyHandler.getObjectProperty("isRelatedTo");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
