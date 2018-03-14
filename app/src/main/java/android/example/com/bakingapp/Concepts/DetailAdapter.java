package android.example.com.bakingapp.Concepts;

import android.app.Activity;
import android.content.Context;
import android.example.com.bakingapp.R;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by florinel on 14.03.2018.
 */

public class DetailAdapter extends ArrayAdapter<DataLoader.Recipe> {

    private Context mContext;
    private int layoutResourceId;
    private ArrayList data = new ArrayList();

    public DetailAdapter(Context context, int layoutResourceId, ArrayList<DataLoader.Recipe> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = context;
        this.data = data;
    }

    static class ViewHolder {
        TextView RecipeTitle;

    }

    @Override
    // Create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        DataLoader.Recipe currentRecipe = getItem(position);

        if (convertView == null) {
            // If it's not recycled, initialize some attributes
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.RecipeTitle = (TextView) convertView.findViewById(R.id.detail_toolbar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();;
        }


        return convertView;
}
}
