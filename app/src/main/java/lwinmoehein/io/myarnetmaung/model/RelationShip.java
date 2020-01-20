package lwinmoehein.io.myarnetmaung.model;

public class RelationShip {
    String rsid,rsmakerid,rsloverid,rscreateddate;

    public RelationShip(String rsid, String rsmakerid, String rsloverid, String rscreateddate) {
        this.rsid = rsid;
        this.rsmakerid = rsmakerid;
        this.rsloverid = rsloverid;
        this.rscreateddate = rscreateddate;
    }
    public RelationShip(){

    }

    public String getRsid() {
        return rsid;
    }

    public void setRsid(String rsid) {
        this.rsid = rsid;
    }

    public String getRsmakerid() {
        return rsmakerid;
    }

    public void setRsmakerid(String rsmakerid) {
        this.rsmakerid = rsmakerid;
    }

    public String getRsloverid() {
        return rsloverid;
    }

    public void setRsloverid(String rsloverid) {
        this.rsloverid = rsloverid;
    }

    public String getRscreateddate() {
        return rscreateddate;
    }

    public void setRscreateddate(String rscreateddate) {
        this.rscreateddate = rscreateddate;
    }
}
