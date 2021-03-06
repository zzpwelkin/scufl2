package uk.org.taverna.scufl2.rdfxml;

import static uk.org.taverna.scufl2.rdfxml.RDFXMLReader.APPLICATION_RDF_XML;
import static uk.org.taverna.scufl2.rdfxml.RDFXMLReader.APPLICATION_VND_TAVERNA_SCUFL2_WORKFLOW_BUNDLE;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import javax.xml.bind.JAXBException;

import uk.org.taverna.scufl2.api.annotation.Revision;
import uk.org.taverna.scufl2.api.annotation.Revisioned;
import uk.org.taverna.scufl2.api.common.URITools;
import uk.org.taverna.scufl2.api.container.WorkflowBundle;
import uk.org.taverna.scufl2.api.core.Workflow;
import uk.org.taverna.scufl2.api.io.WorkflowBundleWriter;
import uk.org.taverna.scufl2.api.io.WriterException;
import uk.org.taverna.scufl2.api.profiles.Profile;
import uk.org.taverna.scufl2.ucfpackage.UCFPackage;

public class RDFXMLWriter implements WorkflowBundleWriter {

	private static final String WF = "wf-";
	private static final String REVISIONS = "-revisions";
	protected static final String RDF = ".rdf";
	protected static final String WORKFLOW = "workflow/";
	protected static final String HISTORY = "history/";
	protected static final String PROFILE = "profile/";
	protected static final String WORKFLOW_BUNDLE_RDF = "workflowBundle.rdf";

	private static URITools uriTools = new URITools();
	
	public static final URITools getUriTools() {
		return uriTools;
	}

	public static final void setUriTools(URITools uriTools) {
		RDFXMLWriter.uriTools = uriTools;
	}

	@Override
	public Set<String> getMediaTypes() {
		return Collections
				.singleton(APPLICATION_VND_TAVERNA_SCUFL2_WORKFLOW_BUNDLE);
	}

	@Override
	public void writeBundle(WorkflowBundle wfBundle, File destination,
			String mediaType) throws WriterException, IOException {
		UCFPackage ucfPackage = makeUCFPackage(wfBundle);
		ucfPackage.save(destination);
	}

	protected UCFPackage makeUCFPackage(WorkflowBundle wfBundle)
			throws IOException, WriterException {
		//UCFPackage ucfPackage = new UCFPackage();
		UCFPackage ucfPackage = wfBundle.getResources();		
		if (ucfPackage.getPackageMediaType() == null) {
			ucfPackage
				.setPackageMediaType(APPLICATION_VND_TAVERNA_SCUFL2_WORKFLOW_BUNDLE);
		}

		RDFXMLSerializer serializer = new RDFXMLSerializer(wfBundle);
		
		for (Workflow wf : wfBundle.getWorkflows()) {
			String path = WORKFLOW + uriTools.validFilename(wf.getName()) + RDF;

			OutputStream outputStream = ucfPackage
					.addResourceUsingOutputStream(path, APPLICATION_RDF_XML);
			try {
				serializer.workflowDoc(outputStream, wf, URI.create(path));
			} catch (JAXBException e) {
				throw new WriterException("Can't generate " + path, e);
			} finally {
				outputStream.close();
			}
			
			path = HISTORY + WF +  
					uriTools.validFilename(wf.getName()) + REVISIONS + RDF;
			addRevisions(wf, path, wfBundle);
		}

		for (Profile pf : wfBundle.getProfiles()) {
			String path = PROFILE + uriTools.validFilename(pf.getName()) + RDF;
			OutputStream outputStream = ucfPackage
					.addResourceUsingOutputStream(path, APPLICATION_RDF_XML);
			try {
				serializer.profileDoc(outputStream, pf, URI.create(path));
			} catch (JAXBException e) {
				throw new WriterException("Can't generate " + path, e);
			} finally {
				outputStream.close();
			}
			path = HISTORY + "pf-" +  
					uriTools.validFilename(pf.getName()) + REVISIONS + RDF;
			addRevisions(pf, path, wfBundle);

		}

		OutputStream outputStream = ucfPackage.addResourceUsingOutputStream(
				WORKFLOW_BUNDLE_RDF, APPLICATION_RDF_XML);
		try {
			serializer.workflowBundleDoc(outputStream,
					URI.create(WORKFLOW_BUNDLE_RDF));
		} catch (JAXBException e) {
			throw new WriterException("Can't generate " + WORKFLOW_BUNDLE_RDF,
					e);
		} finally {
			outputStream.close();
		}
		
		if (ucfPackage.getPackageMediaType().equals(APPLICATION_VND_TAVERNA_SCUFL2_WORKFLOW_BUNDLE)) {
			ucfPackage.setRootFile(WORKFLOW_BUNDLE_RDF);
		}

		String path = HISTORY + "wfbundle" + REVISIONS + RDF;
		addRevisions(wfBundle, path, wfBundle);
		
		return ucfPackage;

	}


	protected void addRevisions(Revisioned revisioned, String path, WorkflowBundle wfBundle) throws WriterException {
		URI uriBase = uriTools.uriForBean(wfBundle).resolve(path);		
		Revision currentRevision = revisioned.getCurrentRevision();
		if (currentRevision == null) {
			return;
		}
//		try {
//			wfBundle.getResources()
//					.addResource(visitor.getDoc(), path, APPLICATION_RDF_XML);
//		} catch (IOException e) {
//			throw new WriterException("Can't write revisions to " + path, e);
//		}
	}

	@Override
	public void writeBundle(WorkflowBundle wfBundle, OutputStream output,
			String mediaType) throws WriterException, IOException {
		UCFPackage ucfPackage = makeUCFPackage(wfBundle);
		ucfPackage.save(output);
	}

}
