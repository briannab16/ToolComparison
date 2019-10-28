import java.io.*;
import java.util.*;

public class ReportBuilder {
    private List<Map<Long, List<String>>> mapList;
    private Map<Long, Address> addrMap;
    private int numTools;

    public ReportBuilder(List<Map<Long, List<String>>> mapList, int numTools) {
        this.mapList = mapList;
        this.numTools = numTools;
        addrMap = new TreeMap<Long, Address>();
        
        findAddresses();
    }

    public void printAllAddresses(ArrayList<String> tools) {
        System.out.print("Address\t");

        for (int i = 0; i < numTools; i++) {
            System.out.print(tools.get(i) + " Symbols\t");
        }

        System.out.println("Full Match\tPartial Match");

        for(Map.Entry<Long, Address> entry : addrMap.entrySet()) {
            entry.getValue().printAddress();
        }
    }

    public void printFullMatches() {
        int count = 0;

        System.out.println("Addresses found by all tools with %100 matches between symbols found: ");

        for(Map.Entry<Long, Address> entry : addrMap.entrySet()) {
            if (entry.getValue().getFullMatch()) {
                System.out.print(Long.toHexString(entry.getValue().getAddress()) + "\t");
                System.out.println(entry.getValue().getFunctions());
                count++;
            }
        }

        System.out.println("Amount of addresses with %100 matches: " + count);
    }

    public void printPartialMatches(ArrayList<String> tools) {
        int count = 0;

        System.out.println("Addresses found by all tools with differences between symbols: ");
        for(Map.Entry<Long, Address> entry : addrMap.entrySet()) {
                if (!entry.getValue().getFullMatch() && entry.getValue().getPartialMatch()) {
                    System.out.print(Long.toHexString(entry.getValue().getAddress()) + "\t");

                    for (int i = 0; i < numTools; i++) {
                        System.out.print(tools.get(i) + ": ");
                        System.out.print(entry.getValue().getTools().get(i) + "\t");
                    }
                    System.out.println("");
                    count++;
                }
        }

        System.out.println("Amount of addresses with partial matches: " + count);
    }

    public void printUniqueMatches(ArrayList<String> tools) {
        for (int i = 0; i < numTools; i++) {
            int count = 0;

            System.out.println("Addresses unique to tool: " + tools.get(i));
            for(Map.Entry<Long, Address> entry : addrMap.entrySet()) {
                if (!entry.getValue().getFullMatch() && !entry.getValue().getPartialMatch() 
                        && entry.getValue().getTools().get(i) != null) {
                    System.out.print(Long.toHexString(entry.getValue().getAddress()) + "\t");
                    System.out.println(entry.getValue().getTools().get(i));
                    count++;
                }
            }
            System.out.println("Amount of addresses unique to tool: " + count);
            System.out.println("");
        }
    }

    private void findAddresses() {
        Address address;

        // add all addresses to addrList
        for (int i = 0; i < mapList.size(); i++) {
            for (Map.Entry<Long, List<String>> entry : mapList.get(i).entrySet()) {
                if(addrMap.containsKey(entry.getKey())) {
                    address = addrMap.get(entry.getKey());

                    address.updateTools(i, entry.getValue());
                    address.updateFunctions(entry.getValue());
                } else {
                    address = new Address(entry.getKey(), numTools);
                    address.updateFunctions(entry.getValue());
                    address.updateTools(i, entry.getValue());
                    
                    addrMap.put(entry.getKey(), address);
                }
            }
        }
    }
}
