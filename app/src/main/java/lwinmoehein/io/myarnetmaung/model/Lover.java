package lwinmoehein.io.myarnetmaung.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Lover
{
    String uid;
    String name;
    String profilepic;
    String gender;
    String loverid;
    String rsid;

    public Lover(String uid, String name, String profilepic, String gender, String lover, String rsid) {
        this.uid = uid;
        this.name = name;
        this.profilepic = profilepic;
        this.gender = gender;
        this.loverid = lover;
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

    public String getLover() {
        return loverid;
    }

    public void setLover(String lover) {
        this.loverid = lover;
    }

    public String getRsid() {
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("name", name);
        result.put("profilepic", profilepic);
        result.put("gender", gender);
        result.put("loverid", loverid);
        result.put("rsid", rsid);

        return result;
    }


}
