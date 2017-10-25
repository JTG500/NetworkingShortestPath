package com.amir;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElementNode extends Thread implements DVchangeListener {
	
	private String name;
	private boolean isTableUpdated = false;
	List<ElementNode> nodes = new ArrayList<ElementNode>();
	List<Distance> distances = new ArrayList<Distance>();
	Map<String, Integer> costMap = new HashMap<String, Integer> ();
	List<CostDetails> costs = new ArrayList<CostDetails> ();
	
	StringBuffer nodesHeading = new StringBuffer("Nodes\t\t\t");
	
	public ElementNode(String name) {
		this.name = name;
	}
	
	public void initialize() {
		
		System.out.println("\n\nInitialization at Node: "+ name);
		System.out.println("*****************************");
		
		StringBuffer costToNodes = new StringBuffer("Cost \t\t\t");
		StringBuffer nextHop = new StringBuffer("nHop \t\t\t");
		StringBuffer updateTime = new StringBuffer("Update \t\t\t");
		
		for(ElementNode eachNode: nodes) {
			nodesHeading = nodesHeading.append(eachNode.name + "\t\t");
			CostDetails cd = new CostDetails();
			cd.source = this.name;
			cd.dest = eachNode.name;
			if (this.name.equalsIgnoreCase(eachNode.name)) {
				
				cd.cost = 0;
				cd.via = "-";
				cd.updTime = "00:00:00";
			}
				
			else {
				cd.cost = 999;
				cd.via = "-";	
				cd.updTime = "00:00:00";
			}
			
			if (cd.cost == 999)
				costToNodes = costToNodes.append( "\u00a4\t\t");
			else
				costToNodes = costToNodes.append( cd.cost + "\t\t");

			costs.add(cd);
			nextHop.append(cd.via + "\t\t");
			updateTime.append(cd.updTime + "\t");
		}
		
		System.out.println(nodesHeading.toString());
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println(costToNodes.toString());	
		System.out.println(nextHop.toString());
		System.out.println(updateTime.toString());
	}
	
	public void iterateFirst() {
		
		System.out.println("\n\nIteration at Node: "+ name);
		System.out.println("*****************************");
		
		System.out.println(nodesHeading.toString());
		System.out.println("------------------------------------------------------------------------------------");
		
		StringBuffer costToNodes = new StringBuffer("Cost \t\t\t");
		StringBuffer nextHop = new StringBuffer("nHop \t\t\t");
		StringBuffer updateTime = new StringBuffer("Update \t\t\t");
		
		for(CostDetails cd: costs) {
			
			if (! cd.dest.equalsIgnoreCase(this.name)) {
				
				for (Distance d:distances){
					
					if (d.source.equalsIgnoreCase(this.name) && d.destination.equalsIgnoreCase(cd.dest)) {
						if (d.distance < cd.cost)
							cd.cost = d.distance;
						cd.via = d.destination;
						cd.updTime = giveTime();
						isTableUpdated = true;
					}
					if (d.destination.equalsIgnoreCase(this.name) && d.source.equalsIgnoreCase(cd.dest)) {
						if (d.distance < cd.cost)
							cd.cost = d.distance;
						cd.via = d.source;
						cd.updTime = giveTime();
						isTableUpdated = true;
					}
				}
			}
			if (cd.cost == 999)
				costToNodes = costToNodes.append( "\u00a4\t\t");
			else
				costToNodes = costToNodes.append( cd.cost + "\t\t");
			nextHop.append(cd.via + "\t\t");
			updateTime.append(cd.updTime + "\t");
		}
		
		System.out.println(costToNodes.toString());	
		System.out.println(nextHop.toString());
		System.out.println(updateTime.toString());
	}
	public void run() {
		sendVectorToNeighbours();
		while(true) {
			try {
				this.wait(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			periodicCheck();
		}
//		periodicCheck()
	}
	
	public void sendVectorToNeighbours() {

		List<CostDetails> propList = new ArrayList<CostDetails> ();
		for(CostDetails cd : costs) {
			if (cd.cost != 999) {
				propList.add(cd);
			}
		}
		new Initiator().triggerFunc(propList);
	}
    @Override
    public synchronized void updateDistanceVector(List<CostDetails> propList) {

    	int costToNeighbour = 0;
    	String propSource = propList.get(0).source;
    	boolean isNeighbour = false;
    	if (propSource.equalsIgnoreCase(this.name))
    		return;
    	for (CostDetails myCd: costs) {
    		if (myCd.dest.equalsIgnoreCase(propSource) && myCd.cost != 999 && myCd.via.equalsIgnoreCase(propSource)){
    			isNeighbour = true;
    			costToNeighbour = myCd.cost;
    			break;
    		}
    	}
    	
    	if (isNeighbour) {
    		for (CostDetails myCd: costs) {
    			for (CostDetails propCd: propList) {		
    				if (myCd.dest.equalsIgnoreCase(propCd.dest)) {

    					if (myCd.cost > (costToNeighbour + propCd.cost) ) {
    						myCd.cost = costToNeighbour + propCd.cost;
    						myCd.via = propCd.source;
    					}
    				}
    			}
    		}
    	}
    	
		System.out.println("\n\nIteration at Node: "+ name);
		System.out.println("*****************************");
		
		System.out.println(nodesHeading.toString());
		System.out.println("------------------------------------------------------------------------------------");
		
		StringBuffer costToNodes = new StringBuffer("Cost \t\t\t");
		StringBuffer nextHop = new StringBuffer("nHop \t\t\t");
		StringBuffer updateTime = new StringBuffer("Update \t\t\t");
		
		//loop these
		for (CostDetails myCd: costs) {
			if (myCd.cost == 999)
				costToNodes = costToNodes.append( "\u00a4\t\t");
			else
				costToNodes = costToNodes.append( myCd.cost + "\t\t");
			nextHop.append(myCd.via + "\t\t");
			updateTime.append(giveTime() + "\t");//myCd.updTime 
		}

		//done loop
		
		System.out.println(costToNodes.toString());	
		System.out.println(nextHop.toString());
		System.out.println(updateTime.toString());
		
    }
    
    public void periodicCheck() {
    	if (isTableUpdated) {
    		isTableUpdated = false;
    		sendVectorToNeighbours();
    	}
    }
    
   public String giveTime() {
	   Date now = new Date();		      
	      SimpleDateFormat dateFormatter = new SimpleDateFormat("hh:mm:ss");
	      return dateFormatter.format(now);
   }
}
