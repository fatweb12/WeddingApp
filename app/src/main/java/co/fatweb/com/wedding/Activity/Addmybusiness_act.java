package co.fatweb.com.wedding.Activity;

import android.app.Dialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import co.fatweb.com.wedding.DataObject.Allergy;
import co.fatweb.com.wedding.Model.Config;
import co.fatweb.com.wedding.R;

public class Addmybusiness_act extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    String serviceprovider;
    LinearLayout back;
    Button btn_addmybusiness;
    Allergy cod;
    ArrayList<Allergy> co = new ArrayList<>();
    ListView lv;
    TextView edt_cat, edt_catid, spinerhint;
    /*Adapter1 adapter1;*/
    EditText edt_businessname;
    private Spinner spinner;

    private ArrayList<String> students;
    String name = "";

    private JSONArray result;
    private TextView textViewName;
    Dialog dialog;
    MaterialBetterSpinner materialBetterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmybusiness_act);


        // edt_catid=findViewById(R.id.auto_complete_edit_textid);
        edt_businessname = findViewById(R.id.edt_businessname);
        back = (LinearLayout) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_addmybusiness = (Button) findViewById(R.id.btn_addbusiness);
        students = new ArrayList<String>();
        textViewName = (TextView) findViewById(R.id.textViewName);
        spinner = (Spinner) findViewById(R.id.spinner);
        materialBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.material_spinner1);

        spinner.setOnItemSelectedListener(this);


        materialBetterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textViewName.setText(getName(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getData();
        btn_addmybusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtbusinessname = edt_businessname.getText().toString();

                if ((edtbusinessname.equals(""))) {
                    Toast.makeText(Addmybusiness_act.this, "Please enter Business name", Toast.LENGTH_SHORT).show();
                } else if (name.equals("0")) {
                        Toast.makeText(Addmybusiness_act.this, "Please Select atleast one category", Toast.LENGTH_SHORT).show();
                    } else {


                        Intent i = new Intent(getApplicationContext(), AddMYBusiness1_act.class);
                        i.putExtra("Cat_id", name);
                        i.putExtra("business_name", edtbusinessname);
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

                }}
            });
        }




    private void getData() {
        //Creating a string request
        Log.e("URL", Config.DATA_URL);
        StringRequest stringRequest = new StringRequest(Config.DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Parsing the fetched Json String to JSON Object

                            Log.e("RESPONSE", response);
                            //Storing the Array of JSON String to our JSON Array
                            result = new JSONArray(response);

                            //Calling method getStudents to get the students from the JSON Array
                            getStudents(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void getStudents(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                students.add(json.getString(Config.TAG_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(Addmybusiness_act.this, android.R.layout.simple_spinner_dropdown_item, students));

        materialBetterSpinner = (MaterialBetterSpinner) findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Addmybusiness_act.this, android.R.layout.simple_dropdown_item_1line, students);

        materialBetterSpinner.setAdapter(adapter);

    }

    //Method to get student name of a particular position
    private String getName(int position) {
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);
            // String str_content = json.getString("id");
            // Toast.makeText(this, ""+str_content, Toast.LENGTH_SHORT).show();

            Log.e("result", String.valueOf(result));
            //Fetching name from that object
            name = json.getString("id");
            // Toast.makeText(this, "NAME :  " + name, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    //Doing the same with this method as we did with getName()
    private String getCourse(int position) {
        String course = "";
        try {
            JSONObject json = result.getJSONObject(position);
            course = json.getString(Config.TAG_USERNAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return course;
    }

    //Doing the same with this method as we did with getName()


    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Setting the values to textviews for a selected item
        // Toast.makeText(this, "NAME :  " + name, Toast.LENGTH_SHORT).show();

        textViewName.setText(getName(position));

    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        textViewName.setText("");
    }

/*
    class Adapter1 extends ArrayAdapter<Allergy> {
        ArrayList<Allergy> demoList;
        LayoutInflater vi;
        int Resource;
        ViewHolder holder;
        public ImageView imageview;

        public Adapter1(Context context, int resource, ArrayList<Allergy> objects) {
            super(context, resource, objects);
            vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Resource = resource;
            demoList = objects;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            View v = convertView;
            if (v == null) {
                holder = new ViewHolder();
                v = vi.inflate(Resource, null);

                holder.dis = (TextView) v.findViewById(R.id.id);
                holder.name = (TextView) v.findViewById(R.id.value);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }


            holder.name.setText(demoList.get(position).getName());
            holder.dis.setText(demoList.get(position).getId());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    String name = demoList.get(position).getName();
                    String id = demoList.get(position).getId();
                    edt_cat.setText(name);
                    edt_catid.setText(id);
                    lv.setVisibility(View.GONE);

                    // Toast.makeText(Search_act.this, "" + name + id, Toast.LENGTH_SHORT).show();
                }
            });
            return v;

        }

        class ViewHolder {


            public TextView dis;
            public TextView name;

        }
    }
*/

}
