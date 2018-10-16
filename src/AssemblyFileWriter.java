import java.io.*;
import java.util.List;

/**
 * Created by omar_swidan on 20/03/18.
 */
public class AssemblyFileWriter {

    private List<String> File;
    private String name;

    public AssemblyFileWriter(List<String> File,String name) {
        this.File = File;
        this.name=name;
    }

    public void write() throws IOException {

    PrintWriter writer=new PrintWriter(new BufferedWriter(new FileWriter("/home/omar_swidan/IdeaProjects/SICAssembler/"+name)));


        for (String s :
                File) {
            writer.print(s);
        }
        writer.close();

    }
}
