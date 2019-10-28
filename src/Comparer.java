import java.io.*;
import java.util.*;

public class Comparer {

	public static void main(String[] args) {
		List<Map<Long, List<String>>> mapList = new ArrayList<Map<Long, List<String>>>();
		Parser parser = new Parser();
		Map<Long, List<String>> tempMap = null;
		boolean reportForm = false;
		int initial = 0;

		if(args.length >= 1 && args[0].equals("-r")) {
			reportForm = true;
			initial = 1;
		}

		for (int i = initial; i < args.length - 1; i += 2) {
			tempMap = null;
			if((tempMap = parser.parseFile(args[i + 1], args[i])) == null) {
				System.out.println("Problem parsing files");
				System.exit(1);
			}
			mapList.add(tempMap);
		}

		/*
		CHECK MAPLIST 
		GOOD

		for(int i =0; i < mapList.size(); i++) {
			for(Map.Entry<Long, List<String>> entry : mapList.get(i).entrySet()) {
				System.out.println("m " + entry.getValue());
			}
		}*/
		
		ReportBuilder rb = new ReportBuilder(mapList, mapList.size());

		ArrayList<String> tools = new ArrayList<String>();

		System.out.print("Comparison of the Reverse Engineering Tools ");
		for (int i = initial; i < args.length - 1; i += 2) {
			if (args[i].equals("-o")) {
				System.out.print("Objdump");
				tools.add("Objdump");
			} else if (args[i].equals("-g")) {
				System.out.print("Ghidra");
				tools.add("Ghidra");
			} else if (args[i].equals("-d")) {
				System.out.print("Dyninst");
				tools.add("Dyninst");
			}

			if (i < args.length - 4)
				System.out.print(", ");
			else if (i < args.length - 2)
				System.out.print(" and ");
		}

		if(reportForm) {
			System.out.println("\n");
			rb.printFullMatches();
			System.out.print("\n");
			rb.printPartialMatches(tools);
			System.out.print("\n");
			rb.printUniqueMatches(tools);
		} else {
			System.out.println("\n");
			rb.printAllAddresses(tools);
		}
	}
}
