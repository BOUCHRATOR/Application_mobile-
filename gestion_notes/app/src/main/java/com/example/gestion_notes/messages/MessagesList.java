package com.example.gestion_notes.messages;

public class MessagesList {
    private String name,mobile,lastMessage,profilePic,chatKey;
    private int unseenMesages;

    public MessagesList(String name,String mobile,String lastMessage,String profilePic,int unseenMesages,String chatKey) {
        this.name = name;
        this.mobile=mobile;
        this.lastMessage=lastMessage;
        this.profilePic=profilePic;
        this.unseenMesages=unseenMesages;
        this.chatKey=chatKey;
    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getMobile() {
        return mobile;
    }

    public int getUnseenMesages() {
        return unseenMesages;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getChatKey() {
        return chatKey;
    }

    public void setChatKey(String chatKey) {
        this.chatKey = chatKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setUnseenMesages(int unseenMesages) {
        this.unseenMesages = unseenMesages;
    }
}
