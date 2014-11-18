package controller;

import java.io.File;
import java.util.Set;

import model.Movie;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

import util.Log;

public class Controller {
	private static final String ONTOLOGY_PATH = "data/movies_recomendation.owl";

	public static Movie recomend(String mood, String companion) {
		OWLOntologyManager manager = Controller.createOntologyManager();
		
		OWLOntology ont = Controller
				.loadOntologyFromFile(Controller.ONTOLOGY_PATH, manager);
		
		OWLReasoner reasoner = Controller.createReasoner(ont);
		reasoner.precomputeInferences();
		
		Log.debug("Ontology Consistent", "Ontology Inconsistent",
				reasoner.isConsistent());

		Controller.checkForUnsatisfiableClasses(reasoner);
		
		
		OWLDataFactory fac = manager.getOWLDataFactory();
		OWLClass country = fac.getOWLClass(IRI.create("http://www.semanticweb.org/matheus/ontologies/2014/10/untitled-ontology-13#Recommended"));
		
		NodeSet<OWLNamedIndividual> individualsNodeSet = reasoner.getInstances(
                country, true);
		Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
        System.out.println("Instances of pet: ");
        for (OWLNamedIndividual ind : individuals) {
            System.out.println("    " + ind);
        }

		Movie a = new Movie();
		a.setTitle("Oi");
		return a;
	}

	private static OWLOntologyManager createOntologyManager(){
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		return manager;
	}
	
	private static OWLOntology loadOntologyFromFile(String filepath, OWLOntologyManager manager) {
		File ontology_file = new File(filepath);

		IRI docIRI = IRI.create(ontology_file);

		OWLOntology ontology = null;

		try {
			ontology = manager.loadOntologyFromOntologyDocument(docIRI);
			Log.debug("Ontology Loaded");
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
			Log.debug("Fail to load ontology");
		}

		return ontology;
	}

	private static OWLReasoner createReasoner(OWLOntology ontology) {
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();

		ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
		OWLReasonerConfiguration config = new SimpleConfiguration(
				progressMonitor);

		OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);

		return reasoner;
	}

	private static void checkForUnsatisfiableClasses(OWLReasoner reasoner) {
		Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
		Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
		if (!unsatisfiable.isEmpty()) {
			Log.debug("The following classes are unsatisfiable: ");
			for (OWLClass cls : unsatisfiable) {
				Log.debug("    " + cls);
			}
		} else {
			Log.debug("There are no unsatisfiable classes");
		}

	}

}
