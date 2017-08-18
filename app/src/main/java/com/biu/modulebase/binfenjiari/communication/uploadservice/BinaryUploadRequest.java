package com.biu.modulebase.binfenjiari.communication.uploadservice;

import android.content.Context;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

/**
 * Binary file upload request.
 *
 * @author cankov
 */
public class BinaryUploadRequest extends HttpUploadRequest {

    private BinaryUploadFile file = null;

    /**
     * Creates a file upload.
     *
     * @param context application context
     * @param uploadId unique ID to assign to this upload request.
     *                 It's used in the broadcast receiver when receiving updates.
     * @param serverUrl URL of the server side script that handles the multipart form upload
     */
    public BinaryUploadRequest(final Context context, final String uploadId, final String serverUrl) {
        super(context, uploadId, serverUrl);
    }

    /**
     * Validates the upload request and throws exceptions if one or more parameters are not
     * properly set.
     *
     * @throws IllegalArgumentException if request protocol or URL are not correctly set o
     * if no file is set
     * @throws MalformedURLException if the provided server URL is not valid
     */
    public void validate() throws IllegalArgumentException, MalformedURLException {
        super.validate();
        if (file == null) {
            throw new IllegalArgumentException("You have to set a file to upload");
        }
    }

    /**
     * Write any upload request data to the intent used to start the upload service.
     *
     * @param intent the intent used to start the upload service
     */
    @Override
    protected void initializeIntent(Intent intent) {
        super.initializeIntent(intent);
        intent.putExtra(UploadService.PARAM_TYPE, UploadService.UPLOAD_BINARY);
        intent.putExtra(UploadService.PARAM_FILE, getFile());
    }

    /**
     * Sets the file used as raw body of the upload request.
     *
     * @param path Absolute path to the file that you want to upload
     * @throws FileNotFoundException if the file to upload does not exist
     */
    public BinaryUploadRequest setFileToUpload(String path) throws FileNotFoundException {
        file = new BinaryUploadFile(path);
        return this;
    }

    @Override
    public BinaryUploadRequest setNotificationConfig(UploadNotificationConfig config) {
        super.setNotificationConfig(config);
        return this;
    }

    @Override
    public BinaryUploadRequest addHeader(String headerName, String headerValue) {
        super.addHeader(headerName, headerValue);
        return this;
    }

    @Override
    public BinaryUploadRequest setMethod(String method) {
        super.setMethod(method);
        return this;
    }

    @Override
    public BinaryUploadRequest setCustomUserAgent(String customUserAgent) {
        super.setCustomUserAgent(customUserAgent);
        return this;
    }

    @Override
    public BinaryUploadRequest setMaxRetries(int maxRetries) {
        super.setMaxRetries(maxRetries);
        return this;
    }

    /**
     * Gets the file used as raw body of the upload request.
     *
     * @return The absolute path of the file that will be used for the upload
     */
    public BinaryUploadFile getFile() {
        return file;
    }
}
