import java.time.LocalTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    public String name;
    public String start;
    public int duration;
    public int importance;
    public boolean urgent;

    /*
        Getter methods
     */
    public String getName() {
        return this.name;
    }

    public String getStartTime() {
        return this.start;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getImportance() {
        return this.importance;
    }

    public boolean isUrgent() {
        return this.urgent;
    }


    public String getFinishTime() {
        String[] startTemp = start.split(":");
        // if start time is 12:00, get it as 12.00
        double startDouble = Integer.parseInt(startTemp[0]) + (Double.parseDouble(startTemp[1]) / 100);

        double finishDouble = startDouble + duration;
        // if the minute portion is greater than 60
        while (finishDouble - Math.floor(finishDouble) > 0.6) {
            finishDouble += 0.4; //+1-0.6
        }

        String[] finishTemp = String.valueOf(finishDouble).split("\\.");

        if (Integer.parseInt(finishTemp[0]) < 10)
            finishTemp[0] = "0" + finishTemp[0];
        if (Integer.parseInt(finishTemp[1]) < 10)
            finishTemp[1] = finishTemp[1] + "0";

        return finishTemp[0] + ":" + finishTemp[1];
    }

    public double getWeight() {
        double constant = 1;

        if (urgent)
            constant = 2000;

        return importance * constant / duration;
    }

    @Override
    public int compareTo(Task o) {
        LocalTime time1 = LocalTime.parse(getFinishTime());
        LocalTime time2 = LocalTime.parse(o.getFinishTime());

        if (time1.isAfter(time2) || time1.equals(time2)) {
            return 1;
        }
        return -1;
    }
}