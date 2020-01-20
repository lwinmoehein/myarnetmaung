package lwinmoehein.io.myarnetmaung.model;

public class Lover
{
    String uid;
    String name;
    String profilepic;
    String gender;
    String loverid;
    String rsid;

    public Lover(String uid, String name, String profilepic, String gender, String loverid, String rsid) {
        this.uid = uid;
        this.name = name;
        this.profilepic = profilepic;
        this.gender = gender;
        this.loverid = loverid;
        this.rsid = rsid;
    }
    public Lover(){}

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLoverid() {
        return loverid;
    }

    public void setLoverid(String loverid) {
        this.loverid = loverid;
    }

    public String getRsid() {
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

}
