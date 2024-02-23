import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Kingdom {
    // Since we don't look at the strongly connected components, Kosaraju's algorithm doesn't work. 
    // The grahp considered as undirected graph to determine if it is connected or not.
    public Map<Integer, ArrayList<Integer>> undirectedCityMap = new HashMap<>();
    // This map is used in part3's solution
    public Map<Integer, ArrayList<Integer>> directedCityMap = new HashMap<>();
    // lineCount variable is equal to vertex count
    int lineCount = 0;

    // to read the txt file and fill adjancecy lists
    public void initializeKingdom(String filename) {
        Path filePath = Paths.get(filename);

        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath.toString()));
            String line;

            try {
                while ((line = br.readLine()) != null) {
                    String[] temp = line.split(" ");

                    for (int i = 0; i < temp.length; i++) {
                        if (temp[i].equals("1")) {
                            ArrayList<Integer> cityAdj = directedCityMap.getOrDefault(lineCount, new ArrayList<>());
                            cityAdj.add(i);
                            directedCityMap.put(lineCount, cityAdj);

                            cityAdj = undirectedCityMap.getOrDefault(lineCount, new ArrayList<>());
                            cityAdj.add(i);
                            undirectedCityMap.put(lineCount, cityAdj);

                            if (i != lineCount) {
                                cityAdj = undirectedCityMap.getOrDefault(i, new ArrayList<>());
                                cityAdj.add(lineCount);
                                undirectedCityMap.put(i, cityAdj);
                            }
                        }
                    }
                    lineCount++;
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }


        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // to determine which city belongs to which colony
    public List<Colony> getColonies() {
        List<Colony> colonies = new ArrayList<>();
        boolean[] visited = new boolean[lineCount];
        Arrays.fill(visited, false);

        // key = colonyID, value = cities
        Map<Integer, List<Integer>> colonyMap = new HashMap<>(); 
        
        int[] count = new int[] {0};
        for (int i = 0; i < lineCount; i++) {
            if (!visited[i]) {
                findConnectedComponents (visited, colonyMap, i, count);
                count[0]++;
            } 
        }

        for (int i = 0; i < count[0]; i++) {
            Colony colony = new Colony();
            colony.cities = colonyMap.getOrDefault(i, new ArrayList<>());
            Collections.sort(colony.cities);

            for (int city : colony.cities) {
                colony.roadNetwork.put(city, directedCityMap.getOrDefault(city, new ArrayList<>()));
            }

            colonies.add(colony);
        }

        return colonies;
    }

    // used depth first search
    public void findConnectedComponents (boolean[] visited, Map<Integer, List<Integer>> colonyMap, int vertex, int[] count) {
        List <Integer> col = new ArrayList<>();
        if (colonyMap.containsKey(count[0]))
            col = colonyMap.get(count[0]);
        col.add(vertex);
        colonyMap.put(count[0], col);

        visited[vertex] = true;
        ArrayList<Integer> neighbours = undirectedCityMap.get(vertex);

        if (neighbours != null) {
            for (int w : neighbours) {
                if (!visited[w]) {
                    findConnectedComponents(visited, colonyMap, w, count);
                }
            }
        }
    }

    // to print the given list of discovered colonies conforming to the given output format.
    public void printColonies(List<Colony> discoveredColonies) {
        System.out.println("Discovered colonies are: ");
        for (int i = 0; i < discoveredColonies.size(); i++) {
            //String temp = (discoveredColonies.get(i).cities).toString();
            
            String temp = "[";
            Colony colony = discoveredColonies.get(i);
            for (int j = 0; j < colony.cities.size(); j++) {
                temp += colony.cities.get(j) + 1;
                if (j != colony.cities.size()-1)
                    temp += ", ";
            }
            temp += "]";

            System.out.println("Colony " + (i+1) + ": " + temp);
        }
    }
}
