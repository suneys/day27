package entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/11/13 0013.
 */
public class ImageList {
    private List<ImageEntity> imagePath = Collections.synchronizedList(new ArrayList<>());

    synchronized public void add(ImageEntity imageEntity){
        imagePath.add(imageEntity);
    }

    synchronized public void clear(){
        imagePath.clear();
    }

    synchronized public void remove(ImageEntity imageEntity){
        imagePath.remove(imageEntity);
    }


}
