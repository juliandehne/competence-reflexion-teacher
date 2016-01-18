package de.unipotsdam.anh.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import uzuzjmd.competence.shared.dto.Graph;
import uzuzjmd.competence.shared.dto.GraphNode;
import uzuzjmd.competence.shared.dto.GraphTriple;
import uzuzjmd.competence.shared.dto.LearningTemplateResultSet;

public class GraphUtil {

	public static List<Entry<String, Integer>> topoSort(Graph graph) {
		final Map<String, Integer> topoMap = new HashMap<String, Integer>();
		int i = 0;
		final  LinkedList<String> tmp = new LinkedList<String>();
		
		for(GraphNode node : graph.nodes) {
			topoMap.put(node.getLabel(), 0);
		}
		
		for(Entry<String, Integer> e : topoMap.entrySet()) {
			for(GraphTriple triple : graph.triples) {
				if(StringUtils.equals(e.getKey(), triple.toNode)) {
					topoMap.put(e.getKey(), e.getValue() + 1);
				}
			}
		}
		
		for(Entry<String, Integer> e : topoMap.entrySet()) {
			if(e.getValue() == 0) {
				tmp.addLast(e.getKey());
			}
		}

		while(!tmp.isEmpty()) {
			String v = tmp.getFirst();
			tmp.removeFirst();
			i++;
			topoMap.put(v, i);
			for(GraphTriple triple : graph.triples) {
				if(StringUtils.equals(v, triple.fromNode)) {
					topoMap.put(triple.toNode, topoMap.get(triple.toNode) - 1);
					if(topoMap.get(triple.toNode) == 0) {
						tmp.addLast(triple.toNode);
					}
				}
			}
		}
		
		return MapUtil.sortByValue(topoMap);
	}

	public static Graph getGraphForCatchword(Map<GraphTriple, String[]> catchwordMap, String catchword) {
		final Graph graph = new Graph();
		for (Entry<GraphTriple, String[]> entry : catchwordMap.entrySet()) {
			if (Arrays.asList(entry.getValue()).contains(catchword)) {
				GraphTriple triple = entry.getKey();
				graph.addTriple(triple.fromNode, triple.toNode, triple.label,
						triple.directed);
			}
		}
		return graph;
	}

	public static void showLearningTemplateResultSet(
			LearningTemplateResultSet result) {
		System.out.println("Result LearningTemplateName: "
				+ result.getNameOfTheLearningTemplate());
		System.out.println("Root graph: " + result.getRoot());
		System.out.println("Result graph: " + result.getResultGraph());
		System.out.println("Catchword Map: ");
		for (Entry<GraphTriple, String[]> e : result.getCatchwordMap()
				.entrySet()) {
			System.out.println("\t Catchword"
					+ Arrays.asList(e.getValue()).toString()
					+ " for Graphtriple: ");
			System.out.println("\t" + e.getKey().toString());
		}
	}
}
