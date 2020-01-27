package lwinmoehein.io.myarnetmaung.model;

public class PrivacyImage {
    String id,uploaderid,img_url;
    Object upload_date;

    public PrivacyImage(String id, String uploaderid, String img_url, Object upload_date) {
        this.id = id;
        this.uploaderid = uploaderid;
        this.img_url = img_url;
        this.upload_date = upload_date;
    }

    public PrivacyImage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUploaderid() {
        return uploaderid;
    }

    public void setUploaderid(String uploaderid) {
        this.uploaderid = uploaderid;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public Object getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Object upload_date) {
        this.upload_date = upload_date;
    }
}
