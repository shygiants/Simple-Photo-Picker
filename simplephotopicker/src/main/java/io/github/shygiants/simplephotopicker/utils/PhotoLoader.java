package io.github.shygiants.simplephotopicker.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

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
        // TODO: Do async thing
        Context context = getContext();

        String[] projection = { MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID };

        // TODO: Order to time
        Cursor thumbnailCursor = context.getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                null);
        // TODO: If cursor is empty(null), handle it

        // Extract the proper column thumbnails
        ArrayList<Photo> result = new ArrayList<>(thumbnailCursor.getCount());
        int thumbnailColumnIndex = thumbnailCursor.getColumnIndex(projection[0]);
        int imageIdColumnIndex = thumbnailCursor.getColumnIndex(projection[1]);

        if (thumbnailCursor.moveToFirst()) {
            do {
                // Generate a tiny thumbnail version.
                String thumbnailPath = thumbnailCursor.getString(thumbnailColumnIndex);
                String imageId = thumbnailCursor.getString(imageIdColumnIndex);
                Uri thumbnailUri = Uri.parse(thumbnailPath);
                Uri fullImageUri = uriToFullImage(imageId);

                // Create the list item.
                Photo photo = new Photo(thumbnailUri, fullImageUri);
                result.add(photo);
            } while (thumbnailCursor.moveToNext());
        }
        thumbnailCursor.close();
        return result;

    }

    private Uri uriToFullImage(String imageId) {
        // Request image related to this thumbnail
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor imageCursor = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                MediaStore.Images.Media._ID + "=?",
                new String[]{imageId},
                null);
        // TODO: If cursor is empty(null), handle it
        if (imageCursor.moveToFirst()) {
            int columnIndex = imageCursor.getColumnIndex(projection[0]);
            String filePath = imageCursor.getString(columnIndex);
            imageCursor.close();
            return Uri.parse(filePath);
        } else {
            imageCursor.close();
            return Uri.parse("");
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
