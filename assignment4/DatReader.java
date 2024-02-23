import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatReader {
    
    private String fileContent;

    public DatReader(String filename) {
        Path filePath = Path.of(filename);
        try {
            fileContent = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStringVar(String varName) {
        Pattern pattern = Pattern.compile(varName + "\\s*=\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(fileContent);
        matcher.find();
        return matcher.group(1);
    }

    public Double getDoubleVar(String varName) {
        Pattern pattern = Pattern.compile(varName + "\\s*=\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher matcher = pattern.matcher(fileContent);
        matcher.find();
        return Double.parseDouble(matcher.group(1));
    }

    public int getIntVar(String varName) {
        Pattern pattern = Pattern.compile("[\\t ]*" + varName + "[\\t ]*=[\\t ]*([0-9]+)");
        Matcher matcher = pattern.matcher(fileContent);
        matcher.find();
        return Integer.parseInt(matcher.group(1));
    }
 
    public Point getPointVar(String varName) {
        Point p = new Point(0,0);
        Pattern pattern = Pattern.compile(varName + "\\s*=\\s*\\(\\s*([0-9]+)\\s*,\\s*([0-9]+)\\s*\\)");
        Matcher matcher = pattern.matcher(fileContent);
        if (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            p = new Point(x, y);
        }
        return p;
    }
}
