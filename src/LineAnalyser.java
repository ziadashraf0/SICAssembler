import java.util.Arrays;
import java.util.Set;

/**
 * Created by omar_swidan on 19/03/18.
 */
public class LineAnalyser {

    private Outcomes outcomes;
   private boolean errorFlag=false;

    public LineAnalyser(Outcomes outcomes) {
         this.outcomes=outcomes;
    }

    public void analyse() throws Exception {

        String[] s=outcomes.getSourceFile().get(0).trim().split("\\s+");
        analyseFirstLine(s);

        analyseLines();


    }

    public void analyseFirstLine(String[] s){

        if (s[1].compareToIgnoreCase("start") == 0) {
            outcomes.setStartAdd( s[2]);

           outcomes.setLOCCR( Integer.parseInt(s[2],16));

            addToInterList(outcomes.getLOCCR(),s);

        } else outcomes.setLOCCR(0);

    }
    public void analyseLines() throws Exception {

        int i=1;
        String[] s=outcomes.getSourceFile().get(i).trim().split("\\s+");

        while (s[0].compareTo("END")!=0){

            if (checkComment(s[0]))
            {

               // addToInterList(-1,s);
                i++;
                s= outcomes.getSourceFile().get(i).trim().split("\\s+");
                continue;
            }

           if (s.length==3)if (outcomes.getSymbTable().get(s[0]) == null) {
                outcomes.getSymbTable().put(s[0], Integer.toHexString(outcomes.getLOCCR()));
            }else {

               System.out.println();
                throw new Exception("duplicate symbol "+s[0]+" "+outcomes.getSymbTable().get(s[0]));
            }
            addToInterList(outcomes.getLOCCR(),s);
            dealWithOpCode(s);
            i++;
            s= outcomes.getSourceFile().get(i).trim().split("\\s+");

        }


        outcomes.setEndAdd(Integer.toHexString(outcomes.getLOCCR()-1));
        addToInterList(-1,s);




    }
    public boolean checkComment(String s){

        if(s.equals(".")) {

            return true;
        }
        return false;
    }
    public void dealWithOpCode(String[] s) throws Exception {

        String opCode="";
        String operand="";

        if (s.length!=1) {
            opCode= s.length == 3 ? s[1] : s[0];
            operand = s.length == 3 ? s[2] : s[1];
        }
        else {
            opCode="RSUB";
            operand="0";
        }

        int LOCCR=outcomes.getLOCCR();
        if (outcomes.getOpTable().get(opCode)!=null||opCode.compareTo("WORD")==0) {
            LOCCR+= 3;

        } else if (opCode.compareTo("RESW")==0) {
            LOCCR += 3 * Integer.parseInt(operand);
        } else if(opCode.compareTo("RESB")==0) {
            LOCCR += Integer.parseInt(operand);

        } else if (opCode.compareTo("BYTE")==0) {
            LOCCR += operand.length()/2;
        } else{
            errorFlag=true;

           throw new Exception("invalid opcode = "+opCode);
        }
        outcomes.setLOCCR(LOCCR);

    }
    public void addToInterList(int LOCCR,String []s){
        // TODO: 24/03/18 End has a space before it, remove it
        StringBuilder sb=new StringBuilder();
        if (LOCCR!=-1)sb.append(Integer.toHexString(LOCCR));

        for (String t: s)
            if (LOCCR!=-1)
                sb.append(" ").append(t);
             else sb.append(t).append(" ");
        sb.append("\n");
       outcomes.getInterFile().add(sb.toString());

    }
}
