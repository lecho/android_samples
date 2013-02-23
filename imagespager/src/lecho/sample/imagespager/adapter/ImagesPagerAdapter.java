package lecho.sample.imagespager.adapter;

import java.util.ArrayList;
import java.util.List;

import lecho.sample.imagespager.R;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Adapter backing up ViewPager acting as images pager.
 * 
 * @author lecho
 * 
 */
public class ImagesPagerAdapter extends PagerAdapter {

    // Instead of resources file paths could be used.
    private List<Integer> mImageResources = new ArrayList<Integer>();
    private Context mContext;

    public ImagesPagerAdapter(Context context, List<Integer> imagesResources) {
        mContext = context;
        mImageResources = imagesResources;
    }

    @Override
    public int getCount() {
        return mImageResources.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // using pages views as key objects so returning page view
        View view = View.inflate(mContext, R.layout.images_pager_page, null);
        ImageView image = (ImageView) view.findViewById(R.id.image);
        image.setImageResource(mImageResources.get(position));
        container.addView(view);
        return view;

    }

}
