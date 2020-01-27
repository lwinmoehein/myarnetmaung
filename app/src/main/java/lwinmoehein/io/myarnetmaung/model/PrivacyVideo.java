package lwinmoehein.io.myarnetmaung.model;

public class PrivacyVideo {
    String id,uploaderid,video_url,thumb_url;
    Object upload_date;

    public PrivacyVideo(String id, String uploaderid, String video_url, String thumb_url, Object upload_date) {
        this.id = id;
        this.uploaderid = uploaderid;
        this.video_url = video_url;
        this.thumb_url = thumb_url;
        this.upload_date = upload_date;
    }

    public PrivacyVideo() {
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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public Object getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(Object upload_date) {
        this.upload_date = upload_date;
    }
}
