package util;

import java.io.File;
import java.util.Set;

import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
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

public class OntologyHandler {	
	private String ontologyFilePath;
	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private OWLReasoner reasoner;
	private OWLDataFactory dataFactory;
	
	public OntologyHandler(String ontologyFilePath) throws OWLOntologyCreationException {
		this.ontologyFilePath = ontologyFilePath;
		
		this.createOntologyManager();
		this.createDataFactory();
		
		this.loadOntologyFromFile();
		this.createReasoner();
		
		this.reasoner.precomputeInferences();
		
		Log.debug("Ontology Consistent", "Ontology Inconsistent", this.reasoner.isConsistent());
	}
	
	private void createOntologyManager(){
		this.manager = OWLManager.createOWLOntologyManager();
	}
	
	private void loadOntologyFromFile() throws OWLOntologyCreationException {
		File ontology_file = new File(this.ontologyFilePath);

		IRI docIRI = IRI.create(ontology_file);

		this.ontology = this.manager.loadOntologyFromOntologyDocument(docIRI);
		Log.debug("Ontology Loaded");
	}

	private void createReasoner() {
		OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();

		OWLReasonerConfiguration config;
		if(Log.DEBUG){
			ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
			config = new SimpleConfiguration(progressMonitor);
		} else {
			config = new SimpleConfiguration();
		}

		this.reasoner = reasonerFactory.createReasoner(this.ontology, config);
	}

	private void createDataFactory(){
		this.dataFactory = this.manager.getOWLDataFactory();
	}
	
	private IRI createIRI(String name){
		return IRI.create(this.ontology.getOntologyID().getOntologyIRI().toString() + "#" + name);
	}
	
	public boolean isAnyClassUnsatisfiable() {
		Node<OWLClass> bottomNode = this.reasoner.getUnsatisfiableClasses();
		Set<OWLClass> unsatisfiable = bottomNode.getEntitiesMinusBottom();
		
		if (!unsatisfiable.isEmpty()) {
			Log.debug("The following classes are unsatisfiable: ");
			for (OWLClass cls : unsatisfiable) {
				Log.debug("    " + cls);
			}
			return true;
		} else {
			Log.debug("There are no unsatisfiable classes");
			return false;
		}
	}
	
	public Set<OWLNamedIndividual> getIndividualsOf(String klass) throws Exception{
		if(!this.hasClass(klass))
			throw new Exception("Class " + klass + " does not exist on your ontology");
		IRI iri = this.createIRI(klass);
		OWLClass owlClass = this.dataFactory.getOWLClass(iri);
		
		NodeSet<OWLNamedIndividual> individualsNodeSet = this.reasoner.getInstances(owlClass, false);
		Set<OWLNamedIndividual> individuals = individualsNodeSet.getFlattened();
		
		return individuals;
	}
	
	public OWLIndividual getOneIndividualOf(String klass) throws Exception{
		return this.getIndividualsOf(klass).iterator().next();
	}
	
	public OWLNamedIndividual getIndividual(String name){
		IRI iri = this.createIRI(name);
		return this.dataFactory.getOWLNamedIndividual(iri);
	}
	
	public OWLObjectProperty getObjectProperty(String name){
		IRI iri = this.createIRI(name);
		return this.dataFactory.getOWLObjectProperty(iri);
	}
	
	public void createRelation(OWLObjectProperty relation, OWLIndividual individual1, OWLIndividual individual2){
		OWLObjectPropertyAssertionAxiom propertyAssertion = this.dataFactory.getOWLObjectPropertyAssertionAxiom(
				relation, individual1, individual2);
		AddAxiom newRelation = new AddAxiom(this.ontology, propertyAssertion);
		this.manager.applyChange(newRelation);
	}
	
	public boolean hasClass(String klass){
		IRI iri = this.createIRI(klass);
		return this.ontology.containsClassInSignature(iri);
	}

	public void syncronizeReasoner(){
		this.reasoner.flush();
	}
	
	public OWLOntology getOntology() {
		return ontology;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	
	public OWLReasoner getReasoner() {
		return reasoner;
	}
	

	public OWLDataFactory getFactory() {
		return dataFactory;
	}
	
	
}
