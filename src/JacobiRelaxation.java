
import java.util.ArrayList;


/**
 *
 * @author Binge2
 */

public class JacobiRelaxation 
{
    private int numOfImages;
    private String imageName;
    private String imageExtension;
    private ArrayList<String> screenshots = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<>();
    
    public JacobiRelaxation()
    {
        
    }
    
    private ArrayList<String> getListOfImages(int numOfImages, String imageName) 
    {
        ArrayList<String> list = new ArrayList();
        
        setNumOfImages(numOfImages);
        setImageName(imageName);
        setImageExtension(imageExtension);
        
        for(int i = 0; i < getNumOfImages(); i++)
        {
            list.add(getImageName() + String.valueOf(i + 1));
        }
    
        labels = createLabelList(list);
        
        return list;
    }
    
    private ArrayList<String> createLabelList(ArrayList<String> filenames)
    {
        ArrayList<String> labels = new ArrayList<String>();
        
        for(int i = 0; i < filenames.size(); i++)
        {
            labels.add("JRT Frame Number " + i);
        }
        
        return labels;
    } 

    public int getNumOfImages() {
        return numOfImages;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageExtension() {
        return imageExtension;
    }

    public void setImageExtension(String imageExtension) {
        this.imageExtension = imageExtension;
    }
    
    public void setNumOfImages(int numOfImages) {
        this.numOfImages = numOfImages;
    }
    
    public ArrayList<String> getScreenshots(int numOfImages, String imageName) {
        screenshots = getListOfImages(numOfImages, imageName);
        return screenshots;
    }
    
    public ArrayList<String> getLabels(){
        return labels;
    }
}
