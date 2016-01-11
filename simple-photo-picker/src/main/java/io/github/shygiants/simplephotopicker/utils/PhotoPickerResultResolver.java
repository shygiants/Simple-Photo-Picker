package io.github.shygiants.simplephotopicker.utils;

import android.content.Intent;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import io.github.shygiants.simplephotopicker.activities.PhotoPickerActivity;
import io.github.shygiants.simplephotopicker.models.Photo;

/**
 * Created by SHYBook_Air on 2016. 1. 11..
 */
public class PhotoPickerResultResolver {

    public static List<Photo> resolve(Intent data) {
        Parcelable[] photoPicked = data.getParcelableArrayExtra(PhotoPickerActivity.PICKED_PHOTOS);
        // TODO: Consider intent and return results with callback
        boolean isMultiple = data.getBooleanExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, true);

        List<Photo> photosToReturn = new ArrayList<>();
        for (Parcelable photo : photoPicked)
            photosToReturn.add((Photo)photo);

        return photosToReturn;
    }
}
