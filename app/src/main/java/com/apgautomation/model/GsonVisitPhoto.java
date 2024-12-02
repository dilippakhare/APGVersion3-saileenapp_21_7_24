package com.apgautomation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GsonVisitPhoto extends RealmObject {
    public int RecId ;
    @PrimaryKey
    public String VisitToken;
    public String PhotoToken ;
    public String Photo ;
    public String LocalPath;

    public int getRecId() {
        return RecId;
    }

    public void setRecId(int recId) {
        RecId = recId;
    }

    public String getVisitToken() {
        return VisitToken;
    }

    public void setVisitToken(String visitToken) {
        VisitToken = visitToken;
    }

    public String getPhotoToken() {
        return PhotoToken;
    }

    public void setPhotoToken(String photoToken) {
        PhotoToken = photoToken;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getLocalPath() {
        return LocalPath;
    }

    public void setLocalPath(String localPath) {
        LocalPath = localPath;
    }
}
