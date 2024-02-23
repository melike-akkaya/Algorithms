import java.util.*;

public class TrapLocator {
    public List<Colony> colonies;

    public TrapLocator(List<Colony> colonies) {
        this.colonies = colonies;
    }

    //  to determine trap positions for each colony
    public List<List<Integer>> revealTraps() {
        List<List<Integer>> traps = new ArrayList<>();

        int vertexCounter = 0;
        for (Colony colony : colonies)
            vertexCounter += colony.cities.size();

        for (Colony colony : colonies) {
            List<Integer> cities = colony.cities;

            List<Integer> cycleContent = new ArrayList<>();
            boolean[] visited = new boolean[vertexCounter];
            for (int city : cities) {
                Arrays.fill(visited, false);
                if (!visited[city]) {
                    if (cycleContent.size() == 0) {
                        cycleContent = findCycle(visited, city, colony);
                    } 
                    else 
                        break;
                }
            }
            Collections.sort(cycleContent);
            traps.add(cycleContent);
        }
        return traps;
    }

    public List<Integer> findCycle (boolean[] visited, int vertex, Colony colony) {
        visited[vertex] = true;
        List<Integer> stack = new ArrayList<>();
        stack.add(vertex);

        while (stack.size() != 0) {
            List<Integer> adj = colony.roadNetwork.getOrDefault(stack.get(0), new ArrayList<>());
            boolean added = false;
            for (int u : adj) {
                if (!visited[u]) {
                    stack.add(0, u);
                    added = true;
                    visited[u] = true;
                    break;
                }
                else if (stack.contains(u)) {
                    int i = 0;
                    while ((i != stack.size() - 1) && (stack.get(i) != u))
                        i ++;
                    for (int j = stack.size() - 1; j > i; j--)
                        stack.remove(j);

                    return stack;
                }
            }
            if (!added) 
                stack.remove(0);
        }
        return new ArrayList<>();
    }

    // to print the results as the given output format
    public void printTraps(List<List<Integer>> traps) {
        System.out.println("Danger exploration conclusions: "); 
        for (int i = 0; i < traps.size(); i++) {
            List<Integer> cycleContent = traps.get(i);
            String cycle = "[";
            if (cycleContent.size() != 0) {
                for (int j = 0; j < cycleContent.size(); j++) {
                    cycle += cycleContent.get(j) + 1;
                    if (j != cycleContent.size() - 1)
                        cycle += ", ";
                }
                cycle += "]";
                System.out.println("Colony " + (i+1) + ": Dangerous. Cities on the dangerous path: " + cycle);
            }
            else 
                System.out.println("Colony " + (i+1) +": Safe");
        }
    }
}
