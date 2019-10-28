import java.io.*;
import java.util.*;

public class Parser {
    private BufferedReader reader;
    private Map<Long, List<String>> map;
    private List<String> names;

    public Parser() {
        reader = null;
    }

    public Map<Long, List<String>> parseFile(String filename, String type) {
        map = null;

        try {
            File file = new File(filename);
            reader = new BufferedReader(new FileReader(file));

            if(type.equals("-o")) {
                parseObjdump();
            } else if (type.equals("-g")) {
                parseGhidra();
            } else if (type.equals("-d")) {
                parseDyninst();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: Can not find file");
            System.exit(1);
        } catch(NumberFormatException e) {
            System.out.println("Error: Can not parse file because of formatting issue.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: Can not read lines correctly");
            System.exit(1);
        }

        return map;

    }

    /*
     * Parse files from objdump
     */
    private void parseObjdump() throws NumberFormatException, IOException{
        String line = "";
        String tempName = "";
        Long tempAddr = -1L;

        map = new TreeMap<Long, List<String>>();

        while((line = reader.readLine()) != null) {
            String[] arr = line.split("\\s+");
            if(arr.length >= 6 && arr[2].equals("F")) {
                tempName = arr[5];
                tempAddr = Long.parseLong(arr[0], 16);
                    
                addPair(tempAddr, tempName);
            }
        }
    }

    /*
     * Parse the symbol table from Ghidra.
     */
    private void parseOldGhidra() throws NumberFormatException, IOException{
        String line = "";
        String tempName = "";
        Long tempAddr = -1L;

        map = new TreeMap<Long, List<String>>();

        while((line = reader.readLine()) != null ) {
            String[] arr = line.split("\\s+");
            
            if(arr.length >= 3 && arr[2].equals("Function")) {
                tempName = arr[0];
                tempAddr = Long.parseLong(arr[1], 16) - Long.parseLong("100000", 16);
                  
                addPair(tempAddr, tempName);
            }
        }
    }

    /*
     * Parse the symbol table from Ghidra.
     */
    private void parseGhidra() throws NumberFormatException, IOException{
        String line = "";
        String tempName = "";
        Long tempAddr = -1L;

        map = new TreeMap<Long, List<String>>();

        while((line = reader.readLine()) != null ) {
            String[] arr = line.split("\\s+");
            
            if(arr.length == 6) {
                tempName = arr[2];
                tempAddr = Long.parseLong(arr[4], 16) - Long.parseLong("100000", 16);
                  
                addPair(tempAddr, tempName);
            }
        }
    }

    /*
     * Parse the code dump from Dyninst.
     */
    private void parseDyninst() throws NumberFormatException, IOException{
        String line = "";
        String tempName = "";
        Long tempAddr = -1L;

        map = new TreeMap<Long, List<String>>();

        while((line = reader.readLine()) != null) {
            String[] arr = line.split("\\s+");
            if(arr.length == 2) {
                tempName = arr[1];
                tempAddr = Long.parseLong(arr[0], 16);
                    
                addPair(tempAddr, tempName);
            }
        }
    }

    private void addPair(Long addr, String name) {
        if (!map.containsKey(addr))
            map.put(addr, new ArrayList<String>());

        map.get(addr).add(name);
    }
}
