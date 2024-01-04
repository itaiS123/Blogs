package il.co.shivhit.blogs.ACTIVITIES;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import il.co.shivhit.blogs.ADAPTERS.BlogPostAdapter;
import il.co.shivhit.blogs.R;
import il.co.shivhit.model.BlogPost;
import il.co.shivhit.model.BlogPosts;
import il.co.shivhit.viewmodel.BlogsViewModel;
import il.co.shivhit.viewmodel.GenericViewModelFactory;

public class BlogPostsActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private BlogPostAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private BlogPosts blogPosts;
    private BlogsViewModel blogsViewModel;
    private BlogPost oldBlogPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_posts);
        initializeViews();
        setObservers();
        setListeners();
        setRecyclerView();
    }
    private void setObservers() {
        GenericViewModelFactory<BlogsViewModel> factory = new GenericViewModelFactory<>(getApplication(), BlogsViewModel::new);
        blogsViewModel = new ViewModelProvider(this, factory).get(BlogsViewModel.class);
        blogsViewModel.getAll().observe(this, new Observer<BlogPosts>() {

            @Override
            public void onChanged(BlogPosts observedBlogPosts) {
                blogPosts = observedBlogPosts;
                adapter.setBlogPosts(blogPosts);
            }
        });
    }

    public void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        blogPosts = new BlogPosts();
    }

    @Override
    public void setListeners() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BlogPostActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void setRecyclerView(){
        BlogPostAdapter.OnItemClickListener singleClickListener = new BlogPostAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(BlogPost blogPost) {
                oldBlogPost = blogPost;
                Intent update = new Intent(BlogPostsActivity.this, BlogPostActivity.class);
                update.putExtra("POST", blogPost);
                startActivityForResult(update, 2);
            }
        };

        BlogPostAdapter.OnItemLongClickListener longClickListener = new BlogPostAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(BlogPost blogPost) {
                if(blogPost != null) {
                    deleteBlogPost(blogPost);
                    return true;
                }
                return false;
            }
        };

        adapter = new BlogPostAdapter(this, R.layout.blog_layout, blogPosts, singleClickListener, longClickListener);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void deleteBlogPost(BlogPost blogPost){
        blogsViewModel.delete(blogPost);
        blogPosts.remove(blogPost);
        adapter.notifyDataSetChanged();
    }
}