package io.github.shygiants.simplephotopicker.utils;

import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncRequest;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.simplephotopicker.models.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 11..
 */
public class PhotoLoader extends AsyncTaskLoader<List<Photo>> {

    private List<Photo> photos;

    public PhotoLoader(Context context) {
        super(context);
    }

    @Override
    public List<Photo> loadInBackground() {
        String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };

        // TODO: Order to time
        Cursor imageCursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,   // Return all rows
                null,
                null);

        ArrayList<Photo> result = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[1]);

        if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);

                Uri fullImageUri = Uri.parse(filePath);
                Uri thumbnailUri = uriToThumbnail(imageId);
                Photo photo = new Photo(thumbnailUri, fullImageUri);
                result.add(photo);
            } while(imageCursor.moveToNext());
        }
        imageCursor.close();
        return result;
    }

    private Uri uriToThumbnail(String imageId) {
        String[] projection = { MediaStore.Images.Thumbnails.DATA };

        Cursor thumbnailCursor = getContext().getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?",
                new String[]{imageId},
                null);

        if (thumbnailCursor.moveToFirst()) {
            int thumbnailColumnIndex = thumbnailCursor.getColumnIndex(projection[0]);
            // Generate a tiny thumbnail version.
            String thumbnailPath = thumbnailCursor.getString(thumbnailColumnIndex);
            thumbnailCursor.close();
            return Uri.parse(thumbnailPath);
        } else {
            thumbnailCursor.close();
            return null;
        }
    }

    @Override
    public void deliverResult(List<Photo> photos) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (photos != null) {
                onReleaseResources(photos);
            }
        }
        List<Photo> oldPhotos = this.photos;
        this.photos = photos;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(photos);
        }

        // At this point we can release the resources associated with
        // 'oldPhotos' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldPhotos != null) {
            onReleaseResources(oldPhotos);
        }
    }

    @Override
    protected void onStartLoading() {
        if (photos != null) deliverResult(photos);
        else forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    public void onCanceled(List<Photo> photos) {
        super.onCanceled(photos);

        onReleaseResources(photos);
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();

        if (photos != null) {
            onReleaseResources(photos);
            photos = null;
        }
    }

    protected void onReleaseResources(List<Photo> photos) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }
}
