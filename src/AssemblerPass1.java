import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by omar_swidan on 18/03/18.
 */
public class AssemblerPass1 {

    private Outcomes outcomes;

    public AssemblerPass1(Outcomes outcomes) {
        this.outcomes = outcomes;
    }

    public void pass() throws Exception {

        outcomes.setSourceFile(readSource());
        outcomes.setOpTable(readOpTable());
        new LineAnalyser(outcomes).analyse();


        new AssemblyFileWriter(outcomes.getInterFile(),"InterFile").write();
        new AssemblerPass2(outcomes).pass();
        for (String s :
                outcomes.getSymbTable().keySet()) {
            System.out.println(s+" "+outcomes.getSymbTable().get(s));
        }
    }


    public List<String> readSource() throws IOException {

        Path path= Paths.get("/home/omar_swidan/IdeaProjects/SICAssembler/SourceCode");
        return Files.readAllLines(path);
    }
    public  Map<String,String> readOpTable() throws IOException {

        Path path=Paths.get("/home/omar_swidan/IdeaProjects/SICAssembler/Optable");
        return listToMap(Files.readAllLines(path));

    }
    public Map<String,String> listToMap(List<String> list){

        Map<String,String> map=new HashMap<>();
        for (String t :
                list) {
            String[] s = t.split("\\s");
            map.put(s[0],s[1]);

        }
        return map;
    }

    public static void main(String[] args) {
        try {
            new AssemblerPass1(new Outcomes()).pass();


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
