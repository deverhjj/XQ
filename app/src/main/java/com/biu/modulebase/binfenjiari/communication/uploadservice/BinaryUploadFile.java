package com.biu.modulebase.binfenjiari.communication.uploadservice;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Binary file to upload.
 *
 * @author cankov
 */
class BinaryUploadFile implements Parcelable {

    protected final File file;

    BinaryUploadFile(String path) throws FileNotFoundException, IllegalArgumentException {
        if (path == null || "".equals(path)) {
            throw new IllegalArgumentException("Please specify a file path! Passed path value is: " + path);
        }
        File file = new File(path);
        if (!file.exists()) throw new FileNotFoundException("Could not find file at path: " + path);
        this.file = file;
    }

    public long length() {
        return file.length();
    }

    public final InputStream getStream() throws FileNotFoundException {
        return new FileInputStream(file);
    }

    @Override
    public void writeToParcel(Parcel parcel, int arg1) {
        parcel.writeString(file.getAbsolutePath());
    }

    // This is used to regenerate the object.
    // All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<BinaryUploadFile> CREATOR =
            new Creator<BinaryUploadFile>() {
        @Override
        public BinaryUploadFile createFromParcel(final Parcel in) {
            return new BinaryUploadFile(in);
        }

        @Override
        public BinaryUploadFile[] newArray(final int size) {
            return new BinaryUploadFile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    protected BinaryUploadFile(Parcel in) {
        file = new File(in.readString());
    }
}
