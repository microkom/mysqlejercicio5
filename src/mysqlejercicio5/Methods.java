/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlejercicio5;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author DAW
 */
public class Methods {

    public static String leftAlignFormat() {
        //Distance format for the table
        String leftAlignFormat = "\n\t"
                + "|"
                + " %-" + Integer.toString(50 - 1) + "s"
                + "|"
                + "  %-" + Integer.toString(9 - 1) + "s"
                + "|"
                + "  %-" + Integer.toString(9 - 1) + "s"
                + "|\n";
        return leftAlignFormat;
    }

    public static String removeFileExtension(String fileName) {
        //removes a file extension
        return (fileName.substring(0, fileName.length() - (fileName.length() - fileName.lastIndexOf('.'))));
    }

    public static void readFileThenWriteIt(File fileRead, String destinationFile, String fileRoute) {
        //This method reads from one file and then writes its
        //content into another one without wiping its original content

        //necesario para crear un objeto del mismo tipo
        FileReader fr = null;
        BufferedReader bufferRead = null;

        File fileWritten = null;
        FileWriter fileToWrite = null;
        BufferedWriter bufferWillWrite = null;

        try {

            //creacion de estructura de lectura de un archivo
            fr = new FileReader(fileRead);
            bufferRead = new BufferedReader(fr);

            //creacion de estructura de escritura
            fileWritten = new File(fileRoute, destinationFile);
            fileToWrite = new FileWriter(fileWritten, true); //true: permite agregar info sin borrar el archivo

            bufferWillWrite = new BufferedWriter(fileToWrite);
            try {
                String line;
                while ((line = bufferRead.readLine()) != null) {
                    bufferWillWrite.write(line);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (fileToWrite != null) {
                        bufferWillWrite.close();
                    }
                } catch (Exception er) {
                    System.out.println(er.getMessage());
                }
            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
        } finally {
            try {
                if (fr != null) {
                    bufferRead.close();
                }
            } catch (Exception er) {
                System.out.println(er.getMessage());
            }
        }
    }

    public static String line(int cant, String caracter) {
        //returns a line
        String hyphen = "";
        for (int i = 0; i < cant; i++) {
            hyphen += caracter;
        }
        hyphen += "+";
        return hyphen;
    }

    public static void writeFile(File fileName, String texto) {
        //This method reads from one file and then writes its
        //content into another one without wiping its original content

        //necesario para crear un objeto del mismo tipo       
        FileWriter fileToWrite = null;
        BufferedWriter bufferWillWrite = null;

        try {
            //creacion de estructura de escritura
            //fileWritten = new File(fileName);
            fileToWrite = new FileWriter(fileName); //true: permite agregar info sin borrar el archivo

            bufferWillWrite = new BufferedWriter(fileToWrite);
            try {
                bufferWillWrite.write(texto + "\n");

            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (bufferWillWrite != null) {
                        bufferWillWrite.close();
                    }
                    if (fileToWrite != null) {
                        fileToWrite.close();
                    }
                } catch (Exception er) {
                    System.out.println(er.getMessage());
                }
            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
        }
    }

    public static void wipeFolderContents(File dir) {
        //Deletes a folder content

        try {
            String[] entries = dir.list();
            for (String s : entries) {
                File currentFile = new File(dir.getPath(), s);
                currentFile.delete();
            }
        } catch (SecurityException se) {
            System.out.println(se.getMessage());
        }
    }

    public static void createFolder(File dir) {
        //Creates a folder
        

        if (dir.exists() && dir.isDirectory()) {
            // Directory erase
            wipeFolderContents(dir);
        }
        try {
            dir.mkdir();
        } catch (SecurityException se) {
            System.out.println(se.getMessage());
        }
    }
}
