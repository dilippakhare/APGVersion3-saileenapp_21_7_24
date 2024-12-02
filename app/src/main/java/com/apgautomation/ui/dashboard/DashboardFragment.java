package com.apgautomation.ui.dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.Toast;

import com.apgautomation.HomePage;
import com.apgautomation.R;
import com.apgautomation.model.EmployeeModel;
import com.apgautomation.ui.attendance.EmployeeRecentAttenance;
import com.apgautomation.ui.communication.Communication;
import com.apgautomation.ui.complaint.ComplaintDashboard;
import com.apgautomation.ui.complaint.SelectSearchCustomer;
import com.apgautomation.ui.enquiry.EnquiryList;
import com.apgautomation.ui.installation.InstallationList;
import com.apgautomation.ui.leave.LeaveApplicationsLiist;
import com.apgautomation.ui.notifications.NoticeActivity;
import com.apgautomation.ui.quotation.MuteilaRequestList;
import com.apgautomation.ui.quotation.QuotationList;
import com.apgautomation.ui.salesvisit.SalesVisitMenu;
import com.apgautomation.ui.taskmgmt.ui.TaskMgmt;
import com.apgautomation.ui.visit.VisitMenu;
import com.apgautomation.utility.CommonShare;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

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
    list.add("Attendance");
    list.add("Leave");
    list.add("Pending Complaints");
    list.add("Book Complaint");
    list.add("Service Visit");
    list.add("Sales Visit");
    list.add("Notice");
    list.add("Quotation Hub");
    list.add("To Do");
    list.add("Ask Query");
    list.add("Material Request");

    if(getString(R.string.app_name).toUpperCase().contains("APG"))
       list.add("Enquiry");
    list.add("Sync");
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

    if(name.equalsIgnoreCase("Attendance"))
    {
      holder.img.setImageResource(R.drawable.ic_attendance);
    }
    if(name.equalsIgnoreCase("Leave"))
    {
      holder.img.setImageResource(R.drawable.ic_leave);
    }

    if(name.equalsIgnoreCase("Service Visit"))
    {
      holder.img.setImageResource(R.drawable.servicevisit
      );
    }
    if(name.equalsIgnoreCase("Sales Visit"))
    {
      holder.img.setImageResource(R.drawable.salesvisit);
    }
    if(name.equalsIgnoreCase("Enquiry"))
    {
      holder.img.setImageResource(R.drawable.salesvisit);
    }
    if(name.equalsIgnoreCase("Pending Complaints"))
    {
      holder.img.setImageResource(R.drawable.complaintlist);
    }
    if(name.equalsIgnoreCase("Book Complaint"))
    {
      holder.img.setImageResource(R.drawable.complaint);
    }
    if(name.equalsIgnoreCase("Notice"))
    {
      holder.img.setImageResource(R.drawable.ic_notice);
    }
    if(name.equalsIgnoreCase("Quotation Hub"))
    {
      holder.img.setImageResource(R.drawable.quotation);
    }
    if(name.equalsIgnoreCase("Visit Website"))
    {
      holder.img.setImageResource(R.drawable.websiteicon);
    }
    if(name.equalsIgnoreCase("To Do"))
    {
      holder.img.setImageResource(R.drawable.todo);
    }
    if(name.equalsIgnoreCase("Ask Query"))
    {
      holder.img.setImageResource(R.drawable.feedback);
    }
    if(name.equalsIgnoreCase("Material Request"))
    {
      holder.img.setImageResource(R.drawable.ic_request);
    }
    if(name.equalsIgnoreCase("Sync"))
    {
      holder.img.setImageResource(R.drawable.ic_sync);
    }
    holder.img .setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(name.equalsIgnoreCase("Leave")) {
          context.startActivity(new Intent(context, LeaveApplicationsLiist.class));

        }
        if(name.equalsIgnoreCase("Book Complaint")) {
          context.startActivity(new Intent(context, SelectSearchCustomer.class));

        }
        if(name.equalsIgnoreCase("Pending Complaints")) {
          context.startActivity(new Intent(context, ComplaintDashboard.class));

        }
        if(name.equalsIgnoreCase("Attendance")) {
          context.startActivity(new Intent(context, EmployeeRecentAttenance.class));

        }
        if(name.equalsIgnoreCase("To Do")) {
          context.startActivity(new Intent(context, TaskMgmt.class));
        }
        if(name.equalsIgnoreCase("Quotation Hub")) {
          context.startActivity(new Intent(context, QuotationList.class));

        }
        if(name.equalsIgnoreCase("Installation")) {
          context.startActivity(new Intent(context, InstallationList.class));

        }
        if(name.equalsIgnoreCase("Enquiry")) {
          context.startActivity(new Intent(context, EnquiryList.class));

        }
        if(name.equalsIgnoreCase("Service Visit")) {
          EmployeeModel m = CommonShare.getMyEmployeeModel(context);
          if (m.DeptId == 3)
            context.startActivity(new Intent(context, VisitMenu.class));
          else if (CommonShare.getRole(context).equalsIgnoreCase("admin")) {
            context.startActivity(new Intent(context, VisitMenu.class));
          } else {
            Toast.makeText(context, "This Facility for Service team only", Toast.LENGTH_SHORT).show();

          }
        }
        if(name.equalsIgnoreCase("Sales Visit"))
        {
          EmployeeModel m= CommonShare.getMyEmployeeModel(context);
          if(m.DeptId==2)
            context.startActivity(new Intent(context, SalesVisitMenu.class));
          else if (CommonShare.getRole(context).equalsIgnoreCase("admin")) {
            context.startActivity(new Intent(context, SalesVisitMenu.class));
          }
          else
            Toast.makeText(context, "This Facility for Sales team only", Toast.LENGTH_SHORT).show();
        }

        if(name.equalsIgnoreCase("Notice")) {
          context.startActivity(new Intent(context, NoticeActivity.class));

        }
        if(name.equalsIgnoreCase("Visit WebSite")) {
          // context.startActivity(new Intent(context, NoticeActivity.class));

          if(  context.getString(R.string.app_name).toUpperCase().contains("APG"))
             context.startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://apgautomation.com/")));
          else  if(  context.getString(R.string.app_name).toUpperCase().contains("SAILEEN"))
            context.startActivity(new Intent( Intent.ACTION_VIEW, Uri.parse("http://www.saileen.com/")));

        }
        if(name.equalsIgnoreCase("Ask Query")) {
          context.startActivity(new Intent(context, Communication.class));

        }
        if(name.equalsIgnoreCase("Material Request")) {
         // context.startActivity(new Intent(context, MuteilaRequestList.class));
          context.startActivity(new Intent(context, MuteilaRequestList.class).putExtra("type","Material"));
        //  context.startActivity(new Intent(context, InstallationList.class).putExtra("type","Material"));
        }
        if(name.equalsIgnoreCase("Sync")) {
         // context.startActivity(new Intent(context, Communication.class));
          AlertDialog.Builder alert=new AlertDialog.Builder(context);
          alert.setMessage("Do you want to sync ?");
          alert.setPositiveButton("Sync", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

              try
              {
                HomePage p= (HomePage) context;
                p.sync();
              }
              catch (Exception ex){}
            }
          });
          alert.setNegativeButton("No",null).show();


        }
      }
    });
  }

  // Return the size of your dataset (invoked by the layout manager)
  @Override
  public int getItemCount() {
    return values.size();
  }

}