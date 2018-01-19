/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.actions.Destination;

/**
 *
 * @author Markus
 */
public class PathFinder {
    private ArrayList<Node> open;
    private ArrayList<Node> closed;
    private Map map;
    private Node[][] nodeMap;
    
    public PathFinder(Map map)
    {
        this.map = map;
        open = new ArrayList<>();
        closed = new ArrayList<>();
    }
    
    public CopyOnWriteArrayList<Destination> findPath(Destination start, Destination goal)
    {
        //New pathfinding, clear previous paths
        open.clear();
        closed.clear();
        Node current;
        CopyOnWriteArrayList<Destination> finalPath = new CopyOnWriteArrayList<>();
        
        //Create a nodemap and initialize it
        nodeMap = new Node[map.getCells().length][map.getCells()[0].length];
        for(int x = 0; x < nodeMap.length; x++)
        {
            for(int y = 0; y < nodeMap[0].length; y++)
            {
                nodeMap[x][y] = new Node(x, y);
                nodeMap[x][y].heuristic = (Math.abs(x - (int)goal.getX()/Game.CELLSIZE) + Math.abs(y - (int)goal.getY()/Game.CELLSIZE));
            }
        }
        
        //Get start and end node
        Node startNode = nodeMap[(int)start.getX()/Game.CELLSIZE][(int)start.getY()/Game.CELLSIZE];
        Node goalNode = nodeMap[(int)goal.getX()/Game.CELLSIZE][(int)goal.getY()/Game.CELLSIZE];
        
        open.add(startNode);
        
        do
        {
            Collections.sort(open); //Sort so the most likely node is at the top
            current = open.get(0);
            open.remove(current);
            closed.add(current);
            
            if(current == goalNode) //Do this when we've reached the end
            {
                while(current.parent != null)
                {
                    finalPath.add(0, current.getDestination());
                    current = current.parent;
                }
                return finalPath;
            }
            
            for(int x = current.colX - 1; x <= current.colX + 1; x++)
            {
                for(int y = current.colY - 1; y <= current.colY + 1; y++)
                {
                    if(x > 0 && x < nodeMap.length && y > 0 && y < nodeMap[0].length)
                    {
                        int movecost = x != current.colX && y != current.colY ? Node.CORNERCOST : Node.MOVECOST;
                        checkNeighbor(nodeMap[x][y], current, movecost);
                    }
                }
            }
            
        }while(open.size() > 0);
        
        return finalPath;
    }
    
    private void checkNeighbor(Node neighbor, Node current, int moveCost)
    {
        if(closed.stream().anyMatch(node -> node == neighbor) || !map.getCells()[neighbor.colX][neighbor.colY].available)
            return;
        
        int calcCost = neighbor.heuristic + current.totalCost + moveCost;
        if(calcCost < neighbor.totalCost || !open.contains(neighbor))
        {
            neighbor.totalCost = calcCost;
            neighbor.parent = current;
            if(!open.contains(neighbor))
                open.add(neighbor);
        }
    }
    
}

class Node implements Comparable<Node>{
    public static final int MOVECOST = 10;
    public static final int CORNERCOST = 14;
    
    public int heuristic;
    public int totalCost;
    public int colX, colY;
    public Node parent;
    
    
    public Node(int colX, int colY)
    {
        this.colX = colX;
        this.colY = colY;
    }
    
    @Override
    public int compareTo(Node n)
    {
        return Integer.compare(totalCost, n.totalCost);
    }
    
    public Destination getDestination()
    {
        return new Destination(colX * Game.CELLSIZE, colY * Game.CELLSIZE);
    }
}
