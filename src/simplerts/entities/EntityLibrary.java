/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simplerts.entities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Markus
 */
public class EntityLibrary {
    
    public static HashSet<Building> buildings;
    public static HashSet<Unit> units;
    
    public static void setup()
    {
        buildings = new HashSet<>();
        units = new HashSet<>();
        
        try {
            loadLibrary();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EntityLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void loadLibrary() throws FileNotFoundException
    {
        Scanner reader = new Scanner(new FileReader(new File("/entitylibrary.txt")));
        while(reader.hasNextLine())
        {
            String line = reader.nextLine();
            String[] args = line.split("#");
            switch(args[0])
            {
                case "Builder":
                    
                    break;
                case "Footman":
                    
                    break;
            }
        }
    }
    
}
