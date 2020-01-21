package lwinmoehein.io.myarnetmaung.model;

public class Lover
{
    String uid;
    String name;
    String profilepic;
    String gender;
    Lover lover;
    String rsid;

    public Lover(String uid, String name, String profilepic, String gender, Lover lover, String rsid) {
        this.uid = uid;
        this.name = name;
        this.profilepic = profilepic;
        this.gender = gender;
        this.lover = lover;
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

    public Lover getLover() {
        return lover;
    }

    public void setLover(Lover lover) {
        this.lover = lover;
    }

    public String getRsid() {
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

}
