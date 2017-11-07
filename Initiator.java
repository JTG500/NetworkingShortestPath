package com.amir;

import com.amir.DVchangeListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Initiator {
	
	int nodesCount = 0;
	int linksCount = 0;
	static List<ElementNode> nodes = new ArrayList<ElementNode>();
	static List<Distance> distances = new ArrayList<Distance>();

	public static void main(String[] args) throws Exception {
		Initiator myInitiator = new Initiator();
		myInitiator.initiate();
	}
	
	private void initiate() throws NumberFormatException, IOException{
		System.out.println("Distance Vector Table Calculation \n**********************************");
		System.out.println("Enter the number of nodes");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		nodesCount = Integer.parseInt(br.readLine());
		
		System.out.println("Print the names of the nodes. \nEx: A or B or C");
		for (int i = 0; i < nodesCount ; i++) {
			String nodeName = br.readLine();
			ElementNode node = new ElementNode(nodeName);
			nodes.add(node);
		}
		
		System.out.println("enter the number of Links");// cost between the neighbour nodes: [ex: a,b,2]");
		linksCount = Integer.parseInt(br.readLine());
		System.out.println("enter the Links(ex: a,b,1)");
		int j = 0;
		while (j < linksCount) {
			
			String neighbourCost = br.readLine();
			if (null != neighbourCost && neighbourCost.length() > 4) {
				String[] cost = neighbourCost.split(",");
				if (cost.length == 3) {
					String s = cost[0];
					String d = cost[1];
					int dis = Integer.parseInt(cost[2]);
					Distance dist = new Distance(s, d, dis);
					distances.add(dist);
					j++;
				}
				else
					System.out.println("Please enter valid data");
			}
		}
		
		for(ElementNode n: nodes) {
			n.nodes = this.nodes;
			n.distances = this.distances;
			n.initialize();
			n.iterateFirst();
		}

		for(ElementNode n: nodes) {
			n.start();
		}	
	}
    
	public synchronized void triggerFunc(List<CostDetails> propList) {
		
        for (ElementNode dvcl : nodes) {
        	dvcl.updateDistanceVector(propList);
	    	try {
				this.wait(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
/*    	try {
			this.wait(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
}
