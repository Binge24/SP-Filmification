
import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author ryuusei
 */
public class MultistageNetwork {
    private ArrayList<String> screenshots = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    private Random rnd = new Random();
    private String ins = new String();
    
    public static void main(String[] args) {
        MultistageNetwork t = new MultistageNetwork();
        ArrayList<String> y = new ArrayList<String>();
        ArrayList<String> z = new ArrayList<String>();
        y = t.getListOfImages();
        z = t.getLabels();
        for(int i = 0; i < y.size(); i++){
            System.out.println(y.get(i));
            System.out.println(z.get(i));
        }
    }

    public ArrayList<String> getScreenshots() {
        screenshots = getListOfImages();
        return screenshots;
    }
    
    public ArrayList<String> getLabels(){
        return labels;
    }
    
    private int createRowStart() {
        return rnd.nextInt(4);
    }

    private void createInstruction() {
        ins = Integer.toString(rnd.nextInt(2)) + Integer.toString(rnd.nextInt(2));
    }
    
    private ArrayList<String> getListOfImages() {
        ArrayList<String> list = new ArrayList();
        int start = createRowStart();
        createInstruction();

        list.add("base");
        list.add("input0" + Integer.toString(start));
        list.add("decide0" + Integer.toString(start));

        if (ins.charAt(0) == '0') {
            list.add("input1" + Integer.toString(start));
            list.add("decide1" + Integer.toString(start));
            if (ins.charAt(1) == '0') {
                list.add("input2" + Integer.toString(start));
            } else {
                switch (start) {
                    case 0:
                        list.add("input21");
                        break;
                    case 1:
                        list.add("input20");
                        break;
                    case 2:
                        list.add("input23");
                        break;
                    case 3:
                        list.add("input22");
                        break;
                }
            }
        } else {
            if (start < 2) {
                list.add("input1" + Integer.toString(start + 2));
                list.add("decide1" + Integer.toString(start + 2));
                if (ins.charAt(1) == '0') {
                    list.add("input2" + Integer.toString(start + 2));
                } else {
                    if (start == 1) {
                        list.add("input22");
                    } else {
                        list.add("input23");
                    }
                }
            } else {
                list.add("input1" + Integer.toString(start - 2));
                list.add("decide1" + Integer.toString(start - 2));
                if (ins.charAt(1) == '0') {
                    list.add("input2" + Integer.toString(start - 2));
                } else {
                    if (start == 3) {
                        list.add("input20");
                    } else {
                        list.add("input21");
                    }
                }
            }
        }
        labels = createLabelList(list);
        return list;
    }
    
    private ArrayList<String> createLabelList(ArrayList<String> filenames){
        ArrayList<String> labels = new ArrayList<String>();
        for(int i = 0; i < filenames.size(); i++){
            if (i == 0){
                labels.add("Start - Starting Row: " + 
                        (Integer.parseInt("" + filenames.get(i + 1).charAt(6)) + 1) + 
                        " Instruction: " + ins);
            } else if (i == filenames.size() - 1){
                labels.add("Multistage Network film " + i + " - End");
            } else if ((i + 1)% 2 == 0){
                labels.add("Multistage Network film " + i);
            } else if ((i + 1) % 2 == 1){
                labels.add("Multistage Network film " + i + " - Instruction: " + ins.charAt((i / 2) - 1));
            }
        }
        return labels;
    } 
}
