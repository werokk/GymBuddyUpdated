package cw.gymbuddy;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Werokk on 28/03/2018.
 */

public class CustomSwipeAdapter extends PagerAdapter {
    private int[] image_resources ;
    private Context ctx;
    private LayoutInflater layoutInflater;
    public CustomSwipeAdapter(Context ctx){
        this.ctx =ctx;
    }
    ArrayList <Bitmap> imgs = new ArrayList<>();
    @Override
    public int getCount() {
        return imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {

        return (view ==(NestedScrollView)o);
    }

    public void addToArray(ArrayList <Bitmap> b){
        imgs = b;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater)ctx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View item_view = layoutInflater.inflate( R.layout.swipe_layout, container, false );
        ImageView imageView = (ImageView) item_view.findViewById( R.id.img);
        imageView.setImageBitmap( imgs.get( position ) );

        imageView.setAdjustViewBounds( true );
        imageView.setMaxHeight( 50 );
        container.addView( item_view );
        return item_view;

    }
    @Override
    public void destroyItem (ViewGroup container, int position, Object object){
        container.removeView( (NestedScrollView)object );
    }


}

