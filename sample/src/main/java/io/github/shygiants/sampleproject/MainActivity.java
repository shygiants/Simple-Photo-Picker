package io.github.shygiants.sampleproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import io.github.shygiants.simplephotopicker.activities.PhotoPickerActivity;
import io.github.shygiants.simplephotopicker.models.Photo;
import io.github.shygiants.simplephotopicker.utils.PhotoPickerResultResolver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQ_PICK_SINGLE_PHOTO = 1;
    private static final int REQ_PICK_MULTIPLE_PHOTOS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button pickMultiplePhotos = (Button) findViewById(R.id.pick_multiple_photos);
        Button pickSinglePhoto = (Button) findViewById(R.id.pick_single_photo);
        pickMultiplePhotos.setOnClickListener(this);
        pickSinglePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pick_multiple_photos:
                startPhotoPickerActivity(true);
                break;
            case R.id.pick_single_photo:
                startPhotoPickerActivity(false);
                break;
        }
    }

    private void startPhotoPickerActivity(boolean isMultiple) {
        Intent intent = new Intent(this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.ARG_IS_MULTIPLE, isMultiple);
        startActivityForResult(intent, (isMultiple)? REQ_PICK_MULTIPLE_PHOTOS : REQ_PICK_SINGLE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        List<Photo> photos;
        switch (requestCode) {
            case REQ_PICK_MULTIPLE_PHOTOS:
                photos = PhotoPickerResultResolver.resolve(data);
                break;
            case REQ_PICK_SINGLE_PHOTO:
                photos = PhotoPickerResultResolver.resolve(data);
                break;
            default:
                return;
        }

        for (Photo photo : photos) {
            Log.i("Photo", photo.getImageUri().toString());
        }
    }
}
