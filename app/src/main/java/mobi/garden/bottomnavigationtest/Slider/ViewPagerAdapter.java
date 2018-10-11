package mobi.garden.bottomnavigationtest.Slider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<String> imageUrls;
    private int pos = 0;

    public ViewPagerAdapter(Context context, List<String> imageUrls){
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        Picasso.with(context)
                .load(imageUrls.get(position))
                .fit()
                .into(imageView);
        container.addView(imageView);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position==0){
                    Toast.makeText(context, "slide 1 clicked", Toast.LENGTH_SHORT).show();
                }
                else if(position==1){
                    Toast.makeText(context, "slide 2 clicked", Toast.LENGTH_SHORT).show();
                }
                else if(position==2){
                    Toast.makeText(context, "slide 3 clicked", Toast.LENGTH_SHORT).show();
                }
                else if(position==3){
                    Toast.makeText(context, "slide 4 clicked", Toast.LENGTH_SHORT).show();
                }
                else if(position==4){
                    Toast.makeText(context, "slide 5 clicked", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
