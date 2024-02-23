import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class TravelMap {

    // Maps a single Id to a single Location.
    public Map<Integer, Location> locationMap = new HashMap<>();

    // List of locations, read in the given order
    public List<Location> locations = new ArrayList<>();

    // List of trails, read in the given order
    public List<Trail> trails = new ArrayList<>();

    // key = location id, value = trails
    public Map<Integer, ArrayList<Trail>> adjacencyMap = new HashMap<>();

    // to read the XML file and fill the locations and trails lists
    public void initializeMap(String filename) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));
            document.getDocumentElement().normalize();

            // to fill locations and locationMap:
            NodeList locationList = document.getElementsByTagName("Location");
            for(int i = 0; i <locationList.getLength(); i++) {
                Node locationNode = locationList.item(i);
                if(locationNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element locationElement = (Element) locationNode;
                    String name = locationElement.getElementsByTagName("Name").item(0).getTextContent();
                    String id = locationElement.getElementsByTagName("Id").item(0).getTextContent();
                    Location location = new Location(name, Integer.parseInt(id));
                    locations.add(location);
                    locationMap.put(location.id, location);
                }
            }

            NodeList trailList = document.getElementsByTagName("Trail");
            for(int i = 0; i <trailList.getLength(); i++) {
                Node trailNode = trailList.item(i);
                if(trailNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element trailElement = (Element) trailNode;
                    String source = trailElement.getElementsByTagName("Source").item(0).getTextContent();
                    Location sourceLoc = locationMap.get(Integer.parseInt(source));
                    String destination = trailElement.getElementsByTagName("Destination").item(0).getTextContent();
                    Location destinationLoc = locationMap.get(Integer.parseInt(destination));
                    String danger = trailElement.getElementsByTagName("Danger").item(0).getTextContent();
                    Trail trail = new Trail(sourceLoc, destinationLoc, Integer.parseInt(danger));
                    trails.add(trail);
                    
                    ArrayList<Trail> adjDes = new ArrayList<>();
                    if (adjacencyMap.containsKey(trail.destination.id)) 
                        adjDes = adjacencyMap.get(trail.destination.id);
                    adjDes.add(trail);
                    adjacencyMap.put(trail.destination.id, adjDes);

                    adjDes = new ArrayList<>();
                    if (adjacencyMap.containsKey(trail.source.id)) 
                        adjDes = adjacencyMap.get(trail.source.id);
                    adjDes.add(trail);
                    adjacencyMap.put(trail.source.id, adjDes);
                }
            }
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
    }

    // to add all the trails a location has to a priority queue
    public void addTrailsToQueue(Location location, PriorityQueue<Trail> queue) {
        ArrayList<Trail> neighbours = adjacencyMap.get(location.id);
        for (Trail t : neighbours)
            queue.offer(t);
    }

    // to fill the safestTrail list by selection the optimal trails from trail list
    // used lazy implementation of Prim's algorithm
    public List<Trail> getSafestTrails() {
        List<Trail> safestTrails = new ArrayList<>();

        // Since Trail implements Comparable, we can directly sort the trails list.
        Collections.sort(trails);
        // hashmap uses id of a location as key to control if it is visited
        Map<Integer, Boolean> visited = new HashMap<>();
        for (Location location : locations)
            visited.put(location.id, false);

        if (trails == null)
            return null;
        
        Trail firstSafeTrail = trails.get(0);
        safestTrails.add(firstSafeTrail);
        
        visited.put(firstSafeTrail.source.id, true);
        visited.put(firstSafeTrail.destination.id, true);

        PriorityQueue<Trail> queue = new PriorityQueue<>();
        addTrailsToQueue(firstSafeTrail.source, queue);
        addTrailsToQueue(firstSafeTrail.destination, queue);

        while (!(queue.isEmpty())) {
            Trail trail = queue.poll();
            Location source = trail.source;
            Location destination = trail.destination;
            
            if (!(visited.get(source.id)) || !(visited.get(destination.id))) {
                visited.put(source.id, true);
                visited.put(destination.id, true);
                addTrailsToQueue(source, queue);
                addTrailsToQueue(destination, queue);
                safestTrails.add(trail);
            }
        }
        return safestTrails;
    }

    // Print the given list of safest trails conforming to the given output format.
    public void printSafestTrails(List<Trail> safestTrails) {
        System.out.println("Safest trails are:");
        int totalDanger = 0;
        for (Trail t : safestTrails) {
            System.out.println("The trail from " + t.source.name + " to " + t.destination.name + " with danger " + t.danger);
            totalDanger += t.danger;
        }
        System.out.println("Total danger: " + totalDanger);
    }
}