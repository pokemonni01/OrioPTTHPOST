package app.wachirapong.emschecker;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by wachirapong on 2/6/2016 AD.
 */
public class ShowTrackResultAdapter extends RecyclerView.Adapter<ShowTrackResultAdapter.ViewHolder> {
    final private String CLASSNAME = "ShowTrackResultAdapter";
    private ThaiPostTrackReader thaiPostTrackReader;
    private JSONArray jsonArray;
    private JSONObject bindingTrack;
    public static Context context;
    private String[] dateArray;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private View v;
        public TextView tvDate;
        public TextView tvLocation;
        public TextView tvDescription;
        public TextView tvStatus;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
            //setRecycleView();
            tvDate = (TextView) v.findViewById(R.id.tvDate);
            tvLocation = (TextView) v.findViewById(R.id.tvLocation);
            tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvStatus = (TextView) v.findViewById(R.id.tvStatus);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShowTrackResultAdapter(Context context) {
        this.context = context;
        //mDataset = myDataset;
        thaiPostTrackReader = new ThaiPostTrackReader();
        jsonArray = ThaiPostTrackReader.getTrackJson();
        //setDateAndTime();
    }

    private void setDateAndTime(){
        try {
            dateArray = new String[ jsonArray.length() ];
            bindingTrack = new JSONObject();
            String dateAndTime;
            String[] explodeDate;
            String location;
            String description;
            for(int i=0;i<jsonArray.length();i++){
                dateAndTime = jsonArray.getJSONObject(i).getString("date");
                explodeDate = dateAndTime.split(" ");
                location = jsonArray.getJSONObject(i).getString("location");
                description = jsonArray.getJSONObject(i).getString("description");
                int index = 0;
                String date = "";
                String time = "";
                for( String word:explodeDate ){
                    if(index<4)
                        date += word+" ";
                    else
                        time += word+" ";
                    index++;
                }
                dateArray[i] = date;
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("time",time);
                jsonObj.put("location",location);
                jsonObj.put("description",description);
                bindingTrack.put(date,jsonObj);

            }
            Set<String> dateSet = new HashSet<String>(Arrays.asList( dateArray ));
            dateArray = dateSet.toArray(new String[1]);
            //Log.d(CLASSNAME,bindingTrack.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShowTrackResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_track_rusult_recycle_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        JSONObject jsonObject;
        try {
            jsonObject = jsonArray.getJSONObject(position);
            holder.tvDate.setText( jsonObject.getString("date") );
            holder.tvLocation.setText( jsonObject.getString("location") );
            holder.tvDescription.setText( jsonObject.getString("description") );
            holder.tvStatus.setText( jsonObject.getString("status") );
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return jsonArray.length();
    }


}
