package il.co.shivhit.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import il.co.shivhit.model.BlogPost;
import il.co.shivhit.model.BlogPosts;

public class BlogsRepository {
    private FirebaseFirestore db;
    private CollectionReference collection;
    private final MutableLiveData<BlogPosts> blogsLiveData;

    public BlogsRepository(Context context) {
        try {

            db = FirebaseFirestore.getInstance();
        } catch (Exception e) {

            FirebaseInstance instance = FirebaseInstance.instance(context);
            db = FirebaseFirestore.getInstance(FirebaseInstance.app);
        }
        collection = db.collection("Blogs");
        blogsLiveData = new MutableLiveData<>();
    }
    public BlogsRepository() {
        db = FirebaseFirestore.getInstance();
        collection = db.collection("Blogs");
        blogsLiveData = new MutableLiveData<>();
    }
    public Task<Boolean> add(BlogPost blogPost) {
        TaskCompletionSource<Boolean> taskCompletion = new TaskCompletionSource<Boolean>();
        DocumentReference document = collection.document();
        blogPost.setIdFs(document.getId());
        document.set(blogPost).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                taskCompletion.setResult(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                taskCompletion.setResult(false);
            }
        });

        return taskCompletion.getTask();
    }
    public MutableLiveData<BlogPosts> getAll() {

        BlogPosts blogPosts = new BlogPosts();
        collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshots) {
                if (querySnapshots != null && !querySnapshots.isEmpty()) {
                    for (DocumentSnapshot document : querySnapshots) {
                        BlogPost blogPost = document.toObject(BlogPost.class);
                        if (blogPost  != null) {
                            blogPosts.add(blogPost);
                        }
                    }
                }
                blogsLiveData.postValue(blogPosts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                blogsLiveData.postValue(blogPosts);
            }
        });
        return blogsLiveData;
    }

    public Task<Boolean> delete(BlogPost blogPost) {
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();
        DocumentReference document = collection.document(blogPost.getIdFs());
        document.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void unused) { completionSource.setResult(true); }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completionSource.setResult(false);
                        completionSource.setException(e);
                    }});
        return completionSource.getTask();
    }

    public Task<Boolean> update(BlogPost blogPost){
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();
        DocumentReference document = collection.document(blogPost.getIdFs());
        document.set(blogPost).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completionSource.setResult(true);
                    }}).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completionSource.setResult(false);
                        completionSource.setException(e);
                    }
                });

        return completionSource.getTask();
    }

}