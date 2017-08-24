package system.management.information.itms;

/**
 * Created by janescience on 22/8/2560.
 */

public class History {

    private String Photos;
    private String Header;
    private String Detail;
    private String Date;

    public History(){

    }

    public History(String Photos, String Header, String Detail, String Date){
        this.Photos=Photos;
        this.Header=Header;
        this.Detail=Detail;
        this.Date=Date;
    }

    public String getHeader() {
        return Header;
    }

    public void setHeader(String Header) {
        this.Header = Header;
    }

    public String getDetail() {
        return Detail;
    }

    public void setDetail(String Detail) {
        this.Detail = Detail;
    }

    public String getImage() {
        return Photos;
    }

    public void setImage(String Photos) {
        this.Photos = Photos;

    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }
}
