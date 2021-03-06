package uk.org.taverna.scufl2.translator.t2flow;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.net.URL;

import org.junit.Test;

import uk.org.taverna.scufl2.api.container.WorkflowBundle;
import uk.org.taverna.scufl2.api.io.WorkflowBundleIO;

public class EmptyStackIT {

	/* From http://dev.mygrid.org.uk/issues/browse/SCUFL2-121 */

	private static final String WF_REST = "/rest.t2flow";
	private static WorkflowBundleIO workflowBundleIO = new WorkflowBundleIO();
	
	
	@Test
	public void readSimpleWorkflow() throws Exception {
		URL wfResource = getClass().getResource(WF_REST);
		assertNotNull("Could not find workflow " + WF_REST,
				wfResource);

		File bundleFile = File.createTempFile("test", "wfbundle");		
		
		WorkflowBundle wfBundle = workflowBundleIO.readBundle(wfResource, null);		
		workflowBundleIO.writeBundle(wfBundle, bundleFile, "application/vnd.taverna.scufl2.workflow-bundle");
		wfBundle = workflowBundleIO.readBundle(bundleFile, null);
	}
	
}
