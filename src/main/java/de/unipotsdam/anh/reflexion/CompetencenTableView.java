package de.unipotsdam.anh.reflexion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;
import de.unipotsdam.anh.dto.TableRow;
import de.unipotsdam.anh.util.GraphUtil;

@ManagedBean(name = "competencenTableView")
@ViewScoped
public class CompetencenTableView implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<TableRow> rows;
	
	@PostConstruct
	public void init() {
		rows = new ArrayList<TableRow>();
	}

	public void update(LearningTemplateResultSet learningTemplateResultSet) {
		if(learningTemplateResultSet == null || learningTemplateResultSet.getResultGraph() == null) {
			return;
		}
		
		rows.clear();
		
		final Set<String> catchWords = new HashSet<String>();
		for(Entry<GraphTriple, String[]> entry : learningTemplateResultSet.getCatchwordMap().entrySet()) {
			catchWords.addAll(Arrays.asList(entry.getValue()));
		}
		
		for(String catchword : catchWords) {
			final Graph graph = GraphUtil.getGraphForCatchword(learningTemplateResultSet.getCatchwordMap(), catchword);
			rows.addAll(getTableRowsFromGraph(catchword, graph));
		}
	}
	
	private List<TableRow> getTableRowsFromGraph(String catchword, Graph graph) {
		final List<TableRow> tableRows = new ArrayList<TableRow>();
		final List<Entry<String, Integer>> topoList = GraphUtil.topoSort(graph);
		while(!topoList.isEmpty()) {
			final List<String> competencen = new ArrayList<String>();
			Entry<String, Integer> entry = topoList.get(0);
			topoList.remove(0);
			competencen.add(entry.getKey());
		}
		return tableRows;
	}

	public List<TableRow> getRows() {
		return rows;
	}

	public void setRows(List<TableRow> rows) {
		this.rows = rows;
	}
}
