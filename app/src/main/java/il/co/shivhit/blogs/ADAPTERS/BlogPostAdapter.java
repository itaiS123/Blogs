package il.co.shivhit.blogs.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import il.co.shivhit.blogs.R;
import il.co.shivhit.helper.DateUtil;
import il.co.shivhit.model.BlogPost;
import il.co.shivhit.model.BlogPosts;

public class BlogPostAdapter extends RecyclerView.Adapter<BlogPostAdapter.PostHolder> {
    private int blogLayout;
    private Context context;
    private BlogPosts blogPosts;
    private OnItemClickListener singleClickListener;
    private OnItemLongClickListener longClickListner;

    public BlogPostAdapter(Context context, int blogLayout, BlogPosts blogPosts, OnItemClickListener singleClickListener, OnItemLongClickListener longClickListner) {
        this.context = context;
        this.blogLayout = blogLayout;
        this.blogPosts = blogPosts;
        this.singleClickListener = singleClickListener;
        this.longClickListner = longClickListner;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostHolder(LayoutInflater.from(context).inflate(blogLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        BlogPost blogPost = blogPosts.get(position);
        if(blogPost != null) {
            holder.bind(blogPost, singleClickListener, longClickListner);
        }
    }

    @Override
    public int getItemCount() {
        return (blogPosts != null) ? blogPosts.size() : 0;
    }
    public static class PostHolder extends RecyclerView.ViewHolder {
        public TextView et1;
        public TextView et2;
        public TextView et3;
        public TextView et4;
        public void bind(BlogPost blogPost, OnItemClickListener listener, OnItemLongClickListener longListener) {

            et1.setText(blogPost.getAuthor());
            et2.setText(blogPost.getTitle());
            et3.setText(DateUtil.longDateToString(blogPost.getDate()));
            et4.setText(blogPost.getContent());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(blogPost);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClicked(blogPost);
                    return true;
                }
            });
        }
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            et1 = itemView.findViewById(R.id.et1);
            et2 = itemView.findViewById(R.id.et2);
            et3 = itemView.findViewById(R.id.et3);
            et4 = itemView.findViewById(R.id.et4);
        }
    }
    public void setBlogPosts(BlogPosts blogPosts) {
        this.blogPosts = blogPosts;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        public void onItemClicked(BlogPost blogPost);
    }

    public interface OnItemLongClickListener {
        public boolean onItemLongClicked(BlogPost blogPost);
    }


}