import java.io.*;
import java.util.*;

public class Address {
    private Long address;
    private List<String> functions;
    private Map<Integer, List<String>> tools;
    private int numTools;
    private boolean fullMatch;
    private boolean partialMatch; // if fullMatch true, partialMatch true

    public Address(Long address, int numTools) {
        this.address = address;
        this.numTools = numTools;
        functions = new ArrayList<String>();
        tools = new TreeMap<Integer, List<String>>();

        for (int i = 0; i < numTools; i++) {
            tools.put(i, null);
        }
    }

    public Long getAddress() {
        return address;
    }
    
    public List<String> getFunctions() {
        return functions;
    }

    public Map<Integer, List<String>> getTools() {
        return tools;
    }

    public boolean getFullMatch() {
        return fullMatch;
    }

    public boolean getPartialMatch() {
        return partialMatch;
    }

    public void updateFunctions(List<String> functions) {
        if (this.functions.size() <= 0) {
            this.functions = functions;
            return;
        }

        for (int i = 0; i < functions.size(); i++) {
            if (!this.functions.contains(functions.get(i)))
                this.functions.add(functions.get(i));
        }

        isFullMatch();
    }

    public void updateTools(int tool, List<String> functions) {
        tools.put(tool, functions);

        isFullMatch();
    }

    public void printAddress() {
        System.out.print(Long.toHexString(this.address) + "\t");
        // print all tools
        for (Map.Entry<Integer, List<String>> entry : tools.entrySet()) {
            System.out.print(entry.getValue() + "\t");
        }
    }

    @Override
    public boolean equals(Object o) {
        if(this.address.equals(((Address)o).getAddress()))
            return true;
        
        return false;
    }

    private void isFullMatch() {
        // check if all tool arrays equal the function array
        for (int i = 0; i < functions.size(); i++) {
            for (Map.Entry<Integer, List<String>> entry : tools.entrySet()) {
                if (entry.getValue() == null || entry.getValue().size() <= 0) {
                    partialMatch = false;
                    fullMatch = false;
                    return;
                }
                /*for (int i = 0; i < entry.getValue(); i++) {
                    if (isSynthetic(entry.getValue().get(i))) {

                    }
                }*/
                if (isSynthetic(functions.get(i))) {
                    if (!isSynMatch(entry.getValue(), functions.get(i))) {
                        fullMatch = false; 
                        partialMatch = true;
                        return;
                    }
                } else {
                    if (!entry.getValue().contains(functions.get(i))) {
                        fullMatch = false;
                        partialMatch = true;
                        return;
                    }
                }
            }
        }

        fullMatch = true;
        partialMatch = true;
    }

    private boolean isSynthetic(String name) {
        if (name.contains("targ") || name.contains("FUN")) {
            return true;
        }
        return false;
    }

    private boolean isSynMatch(List<String> functions, String name) {
        for (int i = 0; i < functions.size(); i++) {
            if (isSynthetic(functions.get(i))) 
                return true;
        }
        return false;
    }
}
