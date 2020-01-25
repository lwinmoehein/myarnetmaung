package lwinmoehein.io.myarnetmaung.model;

public class ChatMessage {
    private String message_id;
    private boolean is_seen;
    private Object date;
    private MessageType type;
    private String sender_id,sendername,senderprofile;
    private String message;
    private String imgurl;



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getSender_id() {
        return sender_id;
    }

    public ChatMessage() {
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getSendername() {
        return sendername;
    }

    public void setSendername(String sendername) {
        this.sendername = sendername;
    }

    public String getSenderprofile() {
        return senderprofile;
    }

    public void setSenderprofile(String senderprofile) {
        this.senderprofile = senderprofile;
    }

    public boolean isIs_seen() {
        return is_seen;
    }

    public void setIs_seen(boolean is_seen) {
        this.is_seen = is_seen;
    }

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }

    public ChatMessage(String message_id, boolean is_seen, Object date, MessageType type, String sender_id, String message, String imgurl,String sendername,String senderprofile) {
        this.message_id = message_id;
        this.is_seen = is_seen;
        this.type=type;
        this.date = date;
        this.sender_id = sender_id;
        this.message=message;
        this.imgurl=imgurl;
        this.sendername=sendername;
        this.senderprofile=senderprofile;
    }
}
