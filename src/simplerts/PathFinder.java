/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import simplerts.entities.Building;
import simplerts.entities.Unit;

/**
 *
 * @author Markus
 */
public class PathFinder {
    private final ArrayList<Node> open;
    private final ArrayList<Node> closed;
    private final Map map;
    private boolean accountForUnits;
    private Node[][] nodeMap;
    
    public PathFinder(Map map)
    {
        this.map = map;
        open = new ArrayList<>();
        closed = new ArrayList<>();
    }
    
    public CopyOnWriteArrayList<Destination> findPath(Destination start, Destination goal, boolean accountForUnits)
    {
        //New pathfinding, clear previous paths
        open.clear();
        closed.clear();
        Node current;
        this.accountForUnits = accountForUnits;
        CopyOnWriteArrayList<Destination> finalPath = new CopyOnWriteArrayList<>();
        
        //Create a nodemap and initialize it
        nodeMap = new Node[map.getCells().length][map.getCells()[0].length];
        for(int x = 0; x < nodeMap.length; x++)
        {
            for(int y = 0; y < nodeMap[0].length; y++)
            {
                nodeMap[x][y] = new Node(x, y);
                nodeMap[x][y].heuristic = (Math.abs(x - (int)goal.getX()) + Math.abs(y - (int)goal.getY()));
            }
        }
        
        //Get start and end node
        Node startNode = nodeMap[(int)start.getX()][(int)start.getY()];
        Node goalNode = nodeMap[(int)goal.getX()][(int)goal.getY()];
        
        if(!map.getCells()[goalNode.gridX][goalNode.gridY].available)
        {
            goalNode = findCloseNode(goalNode, startNode);
        }
        
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
            
            //Check diagonals
//            for(int x = current.gridX - 1; x <= current.gridX + 1; x++)
//            {
//                for(int y = current.gridY - 1; y <= current.gridY + 1; y++)
//                {
//                    if(x > 0 && x < nodeMap.length && y > 0 && y < nodeMap[0].length)
//                    {
//                        int movecost = x != current.gridX && y != current.gridY ? Node.CORNERCOST : Node.MOVECOST;
//                        checkNeighbor(nodeMap[x][y], current, movecost);
//                    }
//                }
//            }
            
            for(int x = current.gridX - 1; x <= current.gridX + 1; x++)
            {
                if(x > 0 && x < nodeMap.length)
                {
                    checkNeighbor(nodeMap[x][current.gridY], current, Node.MOVECOST);
                }
            }
            for(int y = current.gridY - 1; y <= current.gridY + 1; y++)
            {
                if(y > 0 && y < nodeMap[0].length)
                {
                    checkNeighbor(nodeMap[current.gridX][y], current, Node.MOVECOST);
                }
            }
        
        }while(open.size() > 0);
        
        if(finalPath.isEmpty())
            finalPath.add(goalNode.getDestination());
        return finalPath;
    }
    
    private Node findCloseNode(Node goalNode, Node startNode)
    {
        Integer[][] destinations = new Integer[3][3];
        
        for(int x = goalNode.gridX - 1; x <= goalNode.gridX + 1; x++)
        {
            for(int y = goalNode.gridY - 1; y <= goalNode.gridY + 1; y++)
            {
                destinations[x - goalNode.gridX + 1][y - goalNode.gridY + 1] = Math.abs(startNode.gridX - x) + (Math.abs(startNode.gridY - y));
            }
        }
        int indexX = 0, indexY = 0, score = 100;
        for(int x = 0; x < destinations.length; x++)
        {
            for(int y = 0; y < destinations[0].length; y++)
            {
                if(destinations[x][y] < score && !map.checkCollision(goalNode.gridX - 1 + x, goalNode.gridY - 1 + y))
                {
                    score = destinations[x][y];
                    indexX = x;
                    indexY = y;
                } else {
                }
            }
        }
        return nodeMap[goalNode.gridX - 1 + indexX][goalNode.gridY - 1 + indexY];
    }
    
    private void checkNeighbor(Node neighbor, Node current, int moveCost)
    {
        if(closed.stream().anyMatch(node -> node == neighbor) ||
                (map.getCells()[neighbor.gridX][neighbor.gridY].getEntity() != null && (map.getCells()[neighbor.gridX][neighbor.gridY].getEntity() instanceof Building || (accountForUnits && map.getCells()[neighbor.gridX][neighbor.gridY].getEntity() instanceof Unit))) || 
                !map.getCells()[neighbor.gridX][neighbor.gridY].available)
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
    public int gridX, gridY;
    public Node parent;
    
    public boolean occupiedByUnit = false;
    
    
    public Node(int gridX, int gridY)
    {
        this.gridX = gridX;
        this.gridY = gridY;
    }
    
    @Override
    public int compareTo(Node n)
    {
        return Integer.compare(totalCost, n.totalCost);
    }
    
    public Destination getDestination()
    {
        return new Destination(gridX, gridY);
    }
}
