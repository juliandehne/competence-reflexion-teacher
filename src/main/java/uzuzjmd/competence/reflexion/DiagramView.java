package uzuzjmd.competence.reflexion;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;

import uzuzjmd.competence.reflexion.dto.TreeNode;
import uzuzjmd.competence.reflexion.util.GraphUtil;
import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

@ManagedBean(name = "diagramView")
@ViewScoped
public class DiagramView {

	private static final String LABELNAME = "test";
	private DefaultDiagramModel model;

	@PostConstruct
	public void init() {
		model = new DefaultDiagramModel();
		model.setMaxConnections(-1);
		
		createDiagram(testGraph());
	}

	public DefaultDiagramModel getModel() {
		return model;
	}

	public void setModel(DefaultDiagramModel model) {
		this.model = model;
	}

	public void createDiagram(Graph graph) {
		final Map<String, Element> elements = new HashMap<String, Element>();
		int x = 0;
		int y = 0;
		for (Entry<String, Integer> e : GraphUtil.topoSort(graph)) {

			Element element = new Element(e.getKey(), x + "em", y + "em");
			element.addEndPoint(createEndPoint(EndPointAnchor.CONTINUOUS));
			element.addEndPoint(createEndPoint(EndPointAnchor.CONTINUOUS));
			elements.put(e.getKey(), element);
			model.addElement(element);
			
			x = x + 5;
			y = y + 5;
		}

		for (GraphTriple t : graph.triples) {
			model.connect(createConnection(elements.get(t.fromNode)
					.getEndPoints().get(0), elements.get(t.toNode)
					.getEndPoints().get(1), null));
		}
	}

	private EndPoint createEndPoint(EndPointAnchor anchor) {
		BlankEndPoint endPoint = new BlankEndPoint(anchor);
		endPoint.setStyle("{fillStyle:'#404a4e'}");
		endPoint.setHoverStyle("{fillStyle:'#20282b'}");

		return endPoint;
	}

	private Connection createConnection(EndPoint from, EndPoint to, String label) {
		Connection conn = new Connection(from, to);
		conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

		if (label != null) {
			conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
		}

		return conn;
	}
	
	private static Graph testGraph() {
		LearningTemplateResultSet learningTemplateResultSet = new LearningTemplateResultSet();
		learningTemplateResultSet.setNameOfTheLearningTemplate("test");

		final GraphTriple triple1 = new GraphTriple("a1", "a2", LABELNAME, true);
		final GraphTriple triple2 = new GraphTriple("a1", "a7", LABELNAME, true);
		final GraphTriple triple3 = new GraphTriple("a2", "a3", LABELNAME, true);
		final GraphTriple triple4 = new GraphTriple("a3", "a4", LABELNAME, true);
		final GraphTriple triple5 = new GraphTriple("a2", "a5", LABELNAME, true);
		final GraphTriple triple6 = new GraphTriple("a5", "a6", LABELNAME, true);
		
		String catchword = "test";
		learningTemplateResultSet.addTriple(triple1, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple2, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple3, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple4, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple5, new String[] {catchword});
		learningTemplateResultSet.addTriple(triple6, new String[] {catchword});
		
		return learningTemplateResultSet.getResultGraph();
	}
	
	public static void main(String[] args) {
		Graph g = testGraph();
		TreeNode tree = new TreeNode("root");
		for(GraphTriple triple : g.triples) {
			if(tree.getChildrend(triple.fromNode) != null) {
				tree.getChildrend(triple.fromNode).addChildrend(new TreeNode(triple.toNode));
			} else {
				final TreeNode newNode = new TreeNode(triple.fromNode);
				newNode.addChildrend(new TreeNode(triple.toNode));
				tree.addChildrend(newNode);
			}
		}
		
		System.out.println(tree);
	}
}
