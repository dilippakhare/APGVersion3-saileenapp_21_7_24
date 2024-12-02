package com.apgautomation.ui.customer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apgautomation.R;
import com.apgautomation.ui.complaint.BookComplaint;
import com.apgautomation.ui.complaint.ViewComplaint;
import com.apgautomation.ui.dashboard.DashboardViewModel;
import com.apgautomation.ui.notifications.NoticeActivity;
import com.apgautomation.ui.visit.CustomerVisitListActivity;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    ArrayList<String> list=new ArrayList<>();
    RecyclerView recyclerView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
         recyclerView=root.findViewById(R.id.recyclerView);
         //   CommonShare.alert(getActivity(),"Dash Frag");
         GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
          // gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // set Horizontal Orientation
         recyclerView.setLayoutManager(gridLayoutManager);
         list.add("View Products");
         list.add("Book Complaint");
         list.add("Pending Complaint");
       //  list.add("Recent Visits");
         if(getActivity().getString(R.string.app_name).contains("APG"))
             list.add("Visit Websites");
        if(getActivity().getString(R.string.app_name).contains("Saileen"))
            list.add("Visit Websites");

         list.add("Notifications" );
         list.add("Helpline" );

         MyAdapter adapter=new MyAdapter(list,getActivity());
         recyclerView.setAdapter(adapter);

         return root;
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<String> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView txtHeader;
        public ImageView img;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
           // txtHeader = (TextView) v.findViewById(R.id.firstLine);
          //  txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<String> myDataset,Context context) {
        values = myDataset; this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =inflater.inflate(R.layout.item_gridmenu, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        vh.txtHeader=v.findViewById(R.id.title);
        vh.img=v.findViewById(R.id.imgCamera);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position);
        holder.txtHeader.setText(name);
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //remove(position);
            }
        });

        if(name.equalsIgnoreCase("View Products"))
        {
            holder.img.setImageResource(R.drawable.compressor);
        }
        if(name.equalsIgnoreCase("Book Complaint"))
        {
            holder.img.setImageResource(R.drawable.complaint);
        }
        if(name.equalsIgnoreCase("Pending Complaint"))
        {
            holder.img.setImageResource(R.drawable.complaintlist);
        }
        if(name.equalsIgnoreCase("Notifications"))
        {
            holder.img.setImageResource(R.drawable.ic_notice);
        }
        if(name.equalsIgnoreCase("Visit Websites"))
        {
            holder.img.setImageResource(R.drawable.websiteicon);
        }
        if(name.equalsIgnoreCase("Recent Visits"))
        {
            holder.img.setImageResource(R.drawable.ic_visit);
        }
        if(name.equalsIgnoreCase("Helpline"))
        {
            holder.img.setImageResource(R.drawable.help);
        }
        holder.img .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.equalsIgnoreCase("Visit Websites")) {
                    if(context.getString(R.string.app_name).contains("APG"))
                       context.startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://apgautomation.com/")));
                    if(context.getString(R.string.app_name).contains("Saileen"))
                        context.startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://www.saileen.com/")));

                    //  CommonShare.v
                }
                if(name.equalsIgnoreCase("View Products")) {
                    context.startActivity(new Intent(context, ViewProducts.class));

                }
                if(name.equalsIgnoreCase("Book Complaint")) {
                    context.startActivity(new Intent(context, BookComplaint.class));

                }
                if(name.equalsIgnoreCase("Pending Complaint")) {
                    context.startActivity(new Intent(context, ViewComplaint.class));

                }
                if(name.equalsIgnoreCase("Recent Visits")) {
                  context.startActivity(new Intent(context, CustomerVisitListActivity.class));
                  //  Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                }
                if(name.equalsIgnoreCase("Notifications")) {
                  //  Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, NoticeActivity.class));
                }
                if(name.equalsIgnoreCase("Helpline")) {
                    //  Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, Helpline.class));
                }

                //http://apgautomation.com/about-us/
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}