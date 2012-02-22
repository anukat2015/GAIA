package linqs.gaia.graph.generator.decorator;

import java.util.Iterator;
import java.util.Random;

import linqs.gaia.configurable.BaseConfigurable;
import linqs.gaia.exception.ConfigurationException;
import linqs.gaia.feature.explicit.ExplicitString;
import linqs.gaia.feature.schema.Schema;
import linqs.gaia.feature.values.StringValue;
import linqs.gaia.graph.Graph;
import linqs.gaia.graph.Node;
import linqs.gaia.log.Log;
import linqs.gaia.util.SimpleTimer;

/**
 * This decorator creates attribute "labels" for nodes based on edges, for use
 * in generating attributes indicative of link prediction.  The intuition
 * is that edges between nodes indicate they are likely to have the same
 * labels, edges between nodes with "many labels" in common are more
 * like to have an edge between them.
 * <p>
 * This generator uses the RattiganTR07Labeler
 * to create the labels.  The RattiganTR07Labeler will be called repeatedly,
 * with various seeds and targetfeatureids until the requested number
 * of labels are generated.
 * <p>
 * Required Parameters:
 * <UL>
 * <LI> nodeschemaid-Schema ID of nodes to add into
 * </UL>
 * <p>
 * Optional Parameters:
 * <UL>
 * <LI> numattributes-Number of attributes to generate.  Default is 100.
 * <LI> attrprefix-Prefix of attributes ids where the attribute ids will
 * be attrprefix0,...,attrprefixN.
 * <LI> numlabels-Number of labels to use for Rattigan labeler.  Default is 2.
 * <LI> numrandomperlabel-Number random per label to use for Rattigan labeler.  Default is 1.
 * <LI> aggrfid-If specified, also create a string attribute which concatenates all the
 * values generated by this into one string stored with the given feature id.
 * <LI> seed-Seed for use in random number generator.  Default is 0.
 * </UL>
 * 
 * @author namatag
 *
 */
public class HomophilyBasedAttributes extends BaseConfigurable implements Decorator {
	
	public void decorate(Graph g) {
		// Specify number of attributes
		int numattributes = 100;
		if(this.hasParameter("numattributes")) {
			numattributes = (int) this.getDoubleParameter("numattributes");
		}
		
		if(numattributes <= 0) {
			throw new ConfigurationException("Invalid number of attributes: "+numattributes);
		}
		
		// Get prefix to use for attributes
		String attrprefix = "lpw-";
		if(this.hasParameter("attrprefix")) {
			attrprefix = this.getStringParameter("attrprefix");
		}
		
		/************************************/
		
		// Specify Rattigan parameters
		String nodeschemaid = this.getStringParameter("nodeschemaid");
		
		int numlabels = 1;
		if(this.hasParameter("numlabels")) {
			numlabels = (int) this.getDoubleParameter("numlabels");
		}
		
		int numrandomperlabel = 2;
		if(this.hasParameter("numrandomperlabel")) {
			numrandomperlabel = (int) this.getDoubleParameter("numrandomperlabel");
		}
		
		Random rand = null;
		if(this.hasParameter("seed")) {
			rand = new Random((int) this.getDoubleParameter("seed"));
		} else {
			rand = new Random(0);
		}
		
		/************************************/
		
		// Generate attributes
		for(int i=0; i<numattributes; i++) {
			String fid = attrprefix+i;
			RattiganTR07Labeler d = new RattiganTR07Labeler();
			
			SimpleTimer timer = new SimpleTimer();
			Log.INFO("Generating: "+fid);
			
			// Decorate graph using this decorator
			d.decorate(g, nodeschemaid, fid, numlabels, numrandomperlabel, false, rand.nextInt());
			
			Log.INFO("Generated: "+fid+" "+timer.timeLapse(true));
		}
		
		// Save all attributes as one string
		if(this.hasParameter("aggrfid")) {
			String aggrfid = this.getStringParameter("aggrfid");
			Schema schema = g.getSchema(nodeschemaid);
			schema.addFeature(aggrfid, new ExplicitString());
			g.updateSchema(nodeschemaid, schema);
			
			Iterator<Node> nitr = g.getNodes(nodeschemaid);
			while(nitr.hasNext()) {
				Node n = nitr.next();
				String currstring = "";
				for(int i=0; i<numattributes; i++) {
					String fid = attrprefix+i;
					currstring += n.getFeatureValue(fid).getStringValue();
				}
				
				n.setFeatureValue(aggrfid, new StringValue(currstring));
			}
		}
	}
}
