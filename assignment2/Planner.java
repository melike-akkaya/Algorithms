import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Planner {

    public final Task[] taskArray;
    public final Integer[] compatibility;
    public final Double[] maxWeight;
    public final ArrayList<Task> planDynamic;
    public final ArrayList<Task> planGreedy;

    public Planner(Task[] taskArray) {
        this.taskArray = taskArray;
        this.compatibility = new Integer[taskArray.length];
        maxWeight = new Double[taskArray.length];

        this.planDynamic = new ArrayList<>();
        this.planGreedy = new ArrayList<>();
    }

    public int binarySearch(int index) {
        int resultIndex = -1;

        LocalTime startTimeOfProcessTask = LocalTime.parse(taskArray[index].getStartTime());

        int low = 0;
        int high = index - 1;

        while (high >= low) {
            int mid = (low + high) / 2;
            LocalTime finishTimeOfTempTask = LocalTime.parse(taskArray[mid].getFinishTime());
            // If a Task found that is finished before the selected Task starts,
            // it is checked to see if there is any Task which end after the Task already found by setting the low value to mid+1.
            if (finishTimeOfTempTask.isBefore(startTimeOfProcessTask) || finishTimeOfTempTask.equals(startTimeOfProcessTask)) {
                resultIndex = mid;
                low = mid + 1;
            }
            // If the Task that finished before the selected Task has not been reached yet,
            // the Tasks that finished before the reached Task are checked.
            else
                high = mid - 1;
        }

        return resultIndex;
    }

    // Compute compatibility[j] for each j : O(n log n)
    public void calculateCompatibility() {
        for (int index = 0; index < taskArray.length; index++)
            compatibility[index] = binarySearch(index);
    }

    public ArrayList<Task> planDynamic() {
        // to find which task has compatibility with which
        calculateCompatibility();

        System.out.println("Calculating max array\n---------------------");
        calculateMaxWeight(taskArray.length - 1);

        System.out.println("\nCalculating the dynamic solution\n--------------------------------");
        solveDynamic(taskArray.length-1);

        System.out.println("\nDynamic Schedule\n----------------");
        // reversed because the last task is added first.
        Collections.reverse(planDynamic);
        for (Task task : planDynamic) {
            System.out.println("At " + task.getStartTime() + ", " + task.getName() + ".");
        }

        return planDynamic;
    }

    public void solveDynamic(int i) {
        // in order to avoid index out of bounds
        if (i == -1)
            return;

        // writing output
        System.out.println("Called solveDynamic(" + i + ")");

        // The task with 0 index is the first task of program.
        // Which means it is impossible to find a task which is suitable for plan and has more weight
        if (i == 0) {
            planDynamic.add(taskArray[0]);
            return;
        }

        // calculate the weight if we include task
        double weightWithProcessTask = taskArray[i].getWeight();
        if (compatibility[i] != -1)
            weightWithProcessTask += maxWeight[compatibility[i]];
        // the weight if we don't include task
        double weightWithoutProcessTask = maxWeight[i-1];

        // case 1: It is better to include task i in the solution
        if (weightWithProcessTask > weightWithoutProcessTask) {
            planDynamic.add(taskArray[i]);
            solveDynamic(compatibility[i]);
        }
        // case 2: It is worse to include task i in the solution
        else
            solveDynamic(i-1);

    }


    public Double calculateMaxWeight(int i) {
        System.out.println("Called calculateMaxWeight(" + i + ")");

        // If there is no other Task that ends before a Task starts,
        // the compability value for that task is became -1.
        // This if is used to avoid ArrayOutOfBounds error.
        if (i == -1)
            return 0.0;
        // Memoizing is used to reduce complexity.
        // If a value has never been calculated before, this part is used to calculate it.
        else if (maxWeight[i] == null || i == 0) {
            Double w = taskArray[i].getWeight();
            maxWeight[i] = Math.max(w + calculateMaxWeight(compatibility[i]), calculateMaxWeight(i - 1));
        }
        return maxWeight[i];
    }

    /*
     * This function is for generating a plan using the greedy approach.
     * */
    public ArrayList<Task> planGreedy() {
        for (Task task : taskArray) {
            // Initially planGreedy is empty. First task must be added.
            if (planGreedy.size() == 0)
                planGreedy.add(task);
            // Task must be added if it is compatible with planGreedy
            else {
                LocalTime startTime = LocalTime.parse(task.getStartTime());
                Task lastTaskOfList = planGreedy.get(planGreedy.size() - 1);
                LocalTime freeTime = LocalTime.parse(lastTaskOfList.getFinishTime());

                // If task starts after the last task of list had been finished, task is compatible.
                if (freeTime.isBefore(startTime) || freeTime.equals(startTime))
                    planGreedy.add(task);
            }
        }

        // writing output
        System.out.println("Greedy Schedule\n---------------");
        for (Task task : planGreedy) {
            System.out.println("At " + task.getStartTime() + ", " + task.getName() + ".");
        }
        return planGreedy;
    }
}