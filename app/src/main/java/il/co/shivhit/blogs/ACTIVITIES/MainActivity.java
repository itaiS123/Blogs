package il.co.shivhit.blogs.ACTIVITIES;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import il.co.shivhit.blogs.R;

public class MainActivity extends BaseActivity {
    private Button btnAddPost;
    private Button btnShowPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }

    @Override
    protected void initializeViews() {
        btnAddPost = findViewById(R.id.btnAddPost);
        btnShowPost = findViewById(R.id.btnShowPost);
        setListeners();
    }

    @Override
    protected void setListeners() {
        btnAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BlogPostActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnShowPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BlogPostsActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
