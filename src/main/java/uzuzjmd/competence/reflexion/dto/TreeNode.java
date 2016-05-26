package uzuzjmd.competence.reflexion.dto;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {

	private String label;
	private final List<TreeNode> childrends = new ArrayList<TreeNode>();
	private int xPos = 0;
	private int yPos = 0;
	
	public TreeNode(String label) {
		this.label = label;
	}
	
	public void addChildrend(TreeNode node) {
		if(getChildrend(node.getLabel()) != null)
			return;

		node.setxPos(this.xPos + 1);
		int yTmp = 0;
		for(TreeNode n : childrends) {
			if(n.getyPos() >= yTmp) {
				yTmp = n.getyPos();
			}
		}
		
		node.setyPos(yTmp + 1);
		childrends.add(node);
	}
	
	public TreeNode getChildrend(String label) {
		if(this.label.equals(label))
			return this;
		TreeNode node = null;
		for(TreeNode n : childrends) {
			if(n.getChildrend(label) != null){
				node = n.getChildrend(label);
			}
		}
		return node;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	@Override
	public String toString() {
		return "TreeNode [label=" + label + ", childrends=" + childrends
				+ ", xPos=" + xPos + ", yPos=" + yPos + "]";
	}
}
