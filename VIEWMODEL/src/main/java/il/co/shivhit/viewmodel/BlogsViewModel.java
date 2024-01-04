package il.co.shivhit.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import il.co.shivhit.model.BlogPost;
import il.co.shivhit.model.BlogPosts;
import il.co.shivhit.repository.BlogsRepository;


public class BlogsViewModel extends ViewModel {
    private MutableLiveData<Boolean> successOperation;
    private MutableLiveData<BlogPosts> blogsRepositoryMutableLiveData;
    private Context context;
    private BlogsRepository repository;

    public BlogsViewModel(Context context) {

        successOperation = new MutableLiveData<>();
        this.context = context;
        repository = new BlogsRepository(context);
        blogsRepositoryMutableLiveData = new MutableLiveData<>();
    }

    public BlogsViewModel() {
        successOperation = new MutableLiveData<>();
    }

    public void add(BlogPost blogPost){
        // Save to DataBase
        repository.add(blogPost).addOnSuccessListener(aBoolean ->
                {successOperation.setValue(true);}).addOnFailureListener(e ->
                {successOperation.setValue(false);});

    }
    public LiveData<BlogPosts> getAll() {
        blogsRepositoryMutableLiveData = repository.getAll();
        return blogsRepositoryMutableLiveData;
    }
    public LiveData<Boolean> getSuccessOperation(){
        return successOperation;
    }
    public void delete(BlogPost blogPost){
        repository.delete(blogPost).addOnSuccessListener(new OnSuccessListener<Boolean>() {
        @Override
        public void onSuccess(Boolean aBoolean) {
        if (aBoolean)
            successOperation.setValue(true);
        else
            successOperation.setValue(false);
            }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        successOperation.setValue(false);
                    }
                });}

    public void update (BlogPost blogPost){
        repository.update(blogPost).addOnSuccessListener(new OnSuccessListener<Boolean>() {
        @Override
        public void onSuccess(Boolean aBoolean) {
        if (aBoolean)
            successOperation.setValue(true);
        else
            successOperation.setValue(false);}}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        successOperation.setValue(false);
                    }
                });}

}

