/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;
import simplerts.display.Assets;

/**
 *
 * @author Markus
 */
public class MapIO {
    public static Map loadMap(String url)
    {
        return loadMap(new File(File.class.getResource(url).getFile()));
    }
    
    public static Map loadMap(File file)
    {
        Scanner in;
        ArrayList<String> mapData = new ArrayList<>();
        int width = 0, height = 0;
        Map map = new Map(1, 1);
        try {
            in = new Scanner(file);
            while(in.hasNextLine())
            {
                mapData.add(in.nextLine());
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
        }
        
        
        int tileIndex = 0;
        boolean tiles = false;
        for(int i = 0; i < mapData.size(); i++)
        {
            String[] tokens = mapData.get(i).split(",");
            if(i == 0)
            {
                width = Integer.parseInt(tokens[0]);
                height = Integer.parseInt(tokens[1]);
                map = new Map(width, height);
            }
            
            if(tiles)
            {
                for(int j = 0; j < tokens.length; j++)
                {
                    String[] cellInfo = tokens[j].split(":");
                    Cell c = map.getCells()[tileIndex][j];
                    c.setTerrain(Assets.terrains.stream().filter(t -> t.getName().equals(cellInfo[1])).findFirst().get());
                    c.setImage(c.getTerrain().tiles[Integer.parseInt(cellInfo[0])]);
                }
                tileIndex++;
            }
            
            if(tokens[0].equalsIgnoreCase("tiles"))
            {
                System.out.println("tokens is true");
                tiles = true;
            }
        }
        
        return map;
    }
    
    public static void saveMap(File file, Map map) {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file.getAbsolutePath()), "utf-8"))) {
            int width = map.getCells().length;
            int height = map.getCells()[0].length;
            writer.write(width + "," + height + System.getProperty("line.separator"));
            writer.write("tiles," + System.getProperty("line.separator"));
            for(int i = 0; i < width; i++)
            {
                for(int j = 0; j < height; j++)
                {
                    writer.write(map.getCells()[i][j].getTileId() + ":" + map.getCells()[i][j].getTerrain().getName() + ",");
                }
                    System.out.println(width + " " + i);
                writer.write(System.getProperty("line.separator"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
