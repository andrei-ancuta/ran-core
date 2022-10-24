package ro.uti.ran.core.utils;

/**
 * Created by adrian.boldisor on 3/25/2016.
 */
public enum AllowedImagesFormat {
    GIF("image/gif"), JPEG("image/jpeg"), PNG("image/png"), PSD("image/psd"), BMP("image/bmp");

    private String type;

    AllowedImagesFormat(String type) {
        this.type= type;
    }


    public String getType() {
        return type;
    }
}
