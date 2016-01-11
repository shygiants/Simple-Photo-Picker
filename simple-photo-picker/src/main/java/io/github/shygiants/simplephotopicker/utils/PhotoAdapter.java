package io.github.shygiants.simplephotopicker.utils;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import io.github.shygiants.simplephotopicker.R;
import io.github.shygiants.simplephotopicker.activities.PhotoPickerActivity;
import io.github.shygiants.simplephotopicker.models.Photo;
import io.github.shygiants.simplephotopicker.views.PhotoHolder;

/**
 * Created by SHYBook_Air on 2016. 1. 11..
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder>
        implements LoaderManager.LoaderCallbacks<List<Photo>>, PhotoHolder.OnPhotoPickListener {

    private final PhotoPickerActivity activity;
    private final boolean isMultiple;

    private List<Photo> photos;
    // TODO: Consider maximum number of photos to pick
    private List<PhotoHolder> selected = new ArrayList<>();

    public PhotoAdapter(PhotoPickerActivity activity, boolean isMultiple) {
        this.activity = activity;
        this.isMultiple = isMultiple;
    }

    public Photo[] getPicked() {
        Photo[] photos = new Photo[selected.size()];

        int i = 0;
        for (PhotoHolder photoHolder : selected) {
            photos[i] = photoHolder.getPhoto();
            i++;
        }

        return photos;
    }

    @Override
    public void onBindViewHolder(PhotoHolder holder, int position) {
        holder.setPhoto(photos.get(position));
    }

    @Override
    public int getItemCount() {
        return (photos != null)? photos.size() : 0;
    }

    @Override
    public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_photo, parent, false);

        return new PhotoHolder(view, this);
    }

    @Override
    public Loader<List<Photo>> onCreateLoader(int id, Bundle args) {
        return new PhotoLoader(activity);
    }

    @Override
    public void onLoaderReset(Loader<List<Photo>> loader) {
        photos.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onLoadFinished(Loader<List<Photo>> loader, List<Photo> photos) {
        this.photos = new ArrayList<>(photos);
        notifyDataSetChanged();
    }

    @Override
    public void onSelect(PhotoHolder photoHolder) {
        if (!isMultiple) {
            if (!selected.isEmpty()) {
                selected.get(0).setSelected(false, 0);
                selected.clear();
            }
            photoHolder.setSelected(true, 0);
        } else
            photoHolder.setSelected(true, selected.size() + 1);

        selected.add(photoHolder);
        notifyIfCanBeDone();
    }

    @Override
    public void onDeselect(PhotoHolder photoHolder) {
        int index = selected.indexOf(photoHolder);
        ListIterator<PhotoHolder> iter = selected.listIterator(index);
        for (int i = index; iter.hasNext(); i++)
            iter.next().setSelected(true, i);
        selected.remove(index);
        photoHolder.setSelected(false, 0);
        notifyIfCanBeDone();
    }

    private void notifyIfCanBeDone() {
        activity.onSelect(!selected.isEmpty());
    }
}
