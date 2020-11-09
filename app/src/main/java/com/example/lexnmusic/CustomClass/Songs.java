package com.example.lexnmusic.CustomClass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;


public class Songs implements Parcelable {

    Long songId;
    String songTitle;
    String songArtist;
    String songData;
    Long dateAdded;

    public Songs(Long songId, String songTitle, String songArtist, String songData, Long dateAdded) {
        this.songId = songId;
        this.songTitle = songTitle;
        this.songArtist = songArtist;
        this.songData = songData;
        this.dateAdded = dateAdded;
    }

    protected Songs(Parcel in) {
        if (in.readByte() == 0) {
            songId = null;
        } else {
            songId = in.readLong();
        }
        songTitle = in.readString();
        songArtist = in.readString();
        songData = in.readString();
        if (in.readByte() == 0) {
            dateAdded = null;
        } else {
            dateAdded = in.readLong();
        }
    }


    public Long getSongId() {
        return songId;
    }

    public void setSongId(Long songId) {
        this.songId = songId;
    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongData() {
        return songData;
    }

    public void setSongData(String songData) {
        this.songData = songData;
    }

    public Long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Long dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (songId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(songId);
        }
        dest.writeString(songTitle);
        dest.writeString(songArtist);
        dest.writeString(songData);
        if (dateAdded == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(dateAdded);
        }
    }

    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
        @Override
        public Songs createFromParcel(Parcel in) {
            return new Songs(in);
        }

        @Override
        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };

    public static Comparator<Songs> nameComparator = new Comparator<Songs>() {
        @Override
        public int compare(Songs o1, Songs o2) {
            String songOne = o1.songTitle.toUpperCase();
            String songTwo = o2.songTitle.toUpperCase();
            return songOne.compareTo(songTwo);
        }
    };
    public static Comparator<Songs> dateComparator = new Comparator<Songs>() {
        @Override
        public int compare(Songs o1, Songs o2) {
            Double songOne = o1.dateAdded.doubleValue();
            Double songTwo = o2.dateAdded.doubleValue();
            return songTwo.compareTo(songOne);
        }
    };


    /** @Override public int describeContents() {
    return 0;
    }

     @Override public void writeToParcel(Parcel dest, int flags) {
     dest.writeLong(songId);
     dest.writeString(songTitle);
     dest.writeString(songArtist);
     dest.writeString(songData);
     dest.writeLong(dateAdded);
     }

     public Songs(Parcel parcel) {
     songId= parcel.readLong();
     songTitle= parcel.readString();
     songArtist=parcel.readString();
     songData=parcel.readString();
     dateAdded=parcel.readLong();
     }


     public static final Parcelable.Creator<Songs> CREATOR
     = new Parcelable.Creator<Songs>() {

     public Songs createFromParcel(Parcel parcel) {
     return new Songs(parcel);
     }

     public Songs[] newArray(int size) {
     return new Songs[0];
     }
     };*/
}
