package entities;

/**
 * Created by Administrator on 2017/10/15 0015.
 */
public class ImageEntity {

    String lineName;
    String imgPath;
    String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ImageEntity(){

    }

    public ImageEntity(String lineName,String imgPath){
        this.lineName = lineName;
        this.imgPath = imgPath;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
