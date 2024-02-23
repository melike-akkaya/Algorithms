import java.util.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.annotation.processing.SupportedOptions;

import java.util.regex.Matcher;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class IMECEPathFinder{
	public int[][] grid;
	public int height, width;
	public int maxFlyingHeight;
	public double fuelCostPerUnit, climbingCostPerUnit;

	  public IMECEPathFinder(String filename, int rows, int cols, int maxFlyingHeight, double fuelCostPerUnit, double climbingCostPerUnit){

		  grid = new int[rows][cols];
		  this.height = rows;
		  this.width = cols;
		  this.maxFlyingHeight = maxFlyingHeight;
		  this.fuelCostPerUnit = fuelCostPerUnit;
		  this.climbingCostPerUnit = climbingCostPerUnit;

		String filePath = filename;
		//String filePath = "C:\\Users\\melik\\Desktop\\204assignment4\\ass4\\sample_IO\\sample_1\\" + filename;
		
		//String filePath = "C:\\Users\\melik\\Desktop\\204assignment4\\ass4\\sample_IO\\sample_2\\" + filename;
        int rowRem = rows;
        int columnRem = cols;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern numberPattern = Pattern.compile("\\d+");
            
            while ((line = reader.readLine()) != null) {
                Matcher matcher = numberPattern.matcher(line);
                
                while (matcher.find()) {
                    int number = Integer.parseInt(matcher.group());
					grid[rows-rowRem][cols-columnRem] = number;
					columnRem--;
					if (columnRem == 0)
						break;
                }
                rowRem--;
				columnRem = cols;

				if (rowRem == 0)
					break;
            }
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
	  }


	  /**
	   * Draws the grid using the given Graphics object.
	   * Colors should be grayscale values 0-255, scaled based on min/max elevation values in the grid
	   */
	  public void drawGrayscaleMap(Graphics g){
		int minimum = Integer.MAX_VALUE;
		int maximum = 0;
		for (int[] temp : grid) {
			for (int i : temp) {
				if (i > maximum)
					maximum = i;
				else if (i < minimum)
					minimum = i;
			}
		}
		for (int i = 0; i < grid.length; i++)
		{
			for (int j = 0; j < grid[0].length; j++) {
				//int value = ThreadLocalRandom.current().nextInt(0, 255 + 1);
				int value = grid[i][j];
				value = (value-minimum) * 256 / maximum;
				g.setColor(new Color(value,value,value));
				g.fillRect(j, i, 1, 1);
			}
		}
	}

	public void createOutputFile() {
		int minimum = Integer.MAX_VALUE;
		int maximum = 0;
		for (int[] temp : grid) {
			for (int i : temp) {
				if (i > maximum)
					maximum = i;
				else if (i < minimum)
					minimum = i;
			}
		}
	
		try {
			FileWriter outputFile = new FileWriter("grayscaleMap.dat");
			for (int i = 0; i < grid.length; i++) {
				for (int j = 0; j < grid[0].length; j++) {
					int value = grid[i][j];
					value = (value - minimum) * 256 / (maximum - minimum);
					outputFile.write(Integer.toString(value));
					outputFile.write(' ');
				}
				outputFile.write('\n');
			}
			outputFile.close();
		} 
		catch (IOException e) {}
	}
	
  /**
   * Get the most cost-efficient path from the source Point start to the destination Point end
   * using Dijkstra's algorithm on pixels.
   * @return the List of Points on the most cost-efficient path from start to end
   */
  public List<Point> getMostEfficientPath(Point start, Point end) {

	  List<Point> path = new ArrayList<>();

	  HashMap<String, Point> edgeTo = new HashMap<>();
	  HashMap<String, Double> costTo = new HashMap<>();

	  PriorityQueue<Point> pq = new PriorityQueue<>();
	  start.costTo = 0.0;
	  pq.add(start);
	  edgeTo.put(start.toString(), null);
	  costTo.put(start.toString(), 0.0);

	  while (!pq.isEmpty()) {
		Point point = pq.poll();

		int[][] neigbours = new int[][] {new int[] {point.x-1, point.y}, new int[] {point.x+1, point.y}, new int[] {point.x, point.y-1}, 
										new int[] {point.x, point.y+1}, new int[] {point.x-1, point.y+1}, 
										new int[] {point.x-1, point.y-1}, new int[] {point.x+1, point.y+1}, new int[] {point.x+1, point.y-1}};
		
		for (int[] indexes : neigbours) 
			addPointToQueue (point, edgeTo, costTo, pq, indexes[0], indexes[1],end);

		if (point.equals(end)) {
			path.add(point);
			break;
		}
	  }

	  Point temp = edgeTo.getOrDefault(end.toString(), null);
	  while (temp != null) {
		path.add(temp);
		temp = edgeTo.getOrDefault(temp.toString(), null);
	  }

	  return path;
  }
  
  public void addPointToQueue(Point point, HashMap<String, Point> edgeTo, HashMap<String, Double> costTo, PriorityQueue<Point> pq, int newPointx, int newPointy, Point end) {
	try {
		Point newPoint = new Point (newPointx, newPointy);
		if (grid[newPoint.y][newPoint.x] < maxFlyingHeight) {
			double cost = point.costTo + fuelCostPerUnit * (Math.sqrt((Math.pow(point.x-newPoint.x, 2) + Math.pow(point.y-newPoint.y, 2))));
			if (grid[newPoint.y][newPoint.x] > grid[point.y][point.x])
				cost += climbingCostPerUnit * (grid[newPoint.y][newPoint.x] - grid[point.y][point.x]);
			double oldCost = costTo.getOrDefault(newPoint.toString(), Double.MAX_VALUE);
	
			if (oldCost > cost) {
				newPoint.costTo = cost;
				costTo.put(newPoint.toString(), cost);
				edgeTo.put(newPoint.toString(), point);
	
				if (pq.contains(newPoint)) {
					pq.remove(newPoint);
				}
				pq.add(newPoint);
			}
		}
	}
	catch (ArrayIndexOutOfBoundsException e) {}
  }

  /**
   * Calculate the most cost-efficient path from source to destination.
   * @return the total cost of this most cost-efficient path when traveling from source to destination
   */
  public double getMostEfficientPathCost(List<Point> path){
	  double totalCost = 0.0;
	  totalCost = path.get(0).costTo;

	  return totalCost;
  }


  /**
   * Draw the most cost-efficient path on top of the grayscale map from source to destination.
   */
  public void drawMostEfficientPath(Graphics g, List<Point> path){
	  for (Point p : path) {
		g.setColor(new Color(0,225,0));
		g.fillRect(p.x, p.y, 1, 1);
	  }
  }

  /**
   * Find an escape path from source towards East such that it has the lowest elevation change.
   * Choose a forward step out of 3 possible forward locations, using greedy method described in the assignment instructions.
   * @return the list of Points on the path
   */
  public List<Point> getLowestElevationEscapePath(Point start){
	  List<Point> pathPointsList = new ArrayList<>();

	  pathPointsList.add(start);
	  // to rightmost
	  while (pathPointsList.get(0).x != width-1) {
		Point lastPoint = pathPointsList.get(0);

		int minElevation = Integer.MAX_VALUE;
		int[][] neigbours = new int[][] {new int[] {lastPoint.x+1, lastPoint.y}, new int[] {lastPoint.x+1, lastPoint.y-1}, new int[] {lastPoint.x+1, lastPoint.y+1}};
		
		Point destinationPoint = new Point(-1, -1);
		for (int[] indexes : neigbours) {
			Point tempPoint = new Point(indexes[0], indexes[1]);
				try {
					int elevationDifference = Math.abs(grid[lastPoint.y][lastPoint.x] - grid[indexes[1]][indexes[0]]);
			
					if (elevationDifference < minElevation) {
						destinationPoint = tempPoint;
						minElevation = elevationDifference;
						destinationPoint.highCost = minElevation;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {};
		}

		pathPointsList.add(0, destinationPoint);
	  }

	  Collections.reverse(pathPointsList);

	  return pathPointsList;
  }


  /**
   * Calculate the escape path from source towards East such that it has the lowest elevation change.
   * @return the total change in elevation for the entire path
   */
  public int getLowestElevationEscapePathCost(List<Point> pathPointsList){
	  int totalChange = 0;

	  for (Point point : pathPointsList)
	  	totalChange += point.highCost;

	  return totalChange;
  }


  /**
   * Draw the escape path from source towards East on top of the grayscale map such that it has the lowest elevation change.
   */
  public void drawLowestElevationEscapePath(Graphics g, List<Point> pathPointsList){
	  for (Point p : pathPointsList) {
		g.setColor(new Color(225,225,0));
		g.fillRect(p.x, p.y, 1, 1);
	  }
  }
}
