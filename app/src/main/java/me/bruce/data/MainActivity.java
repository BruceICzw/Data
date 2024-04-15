package me.bruce.data;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //declare buttons and edittext
    EditText RollNo, Name, Marks;
    Button Insert, Delete, Update, View, ViewAll;

    //declare database object
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RollNo = findViewById(R.id.Rollno);
        Name = findViewById(R.id.Name);
        Marks = findViewById(R.id.Marks);
        Insert = findViewById(R.id.Insert);
        Delete= findViewById(R.id.Delete);
        Update= findViewById(R.id.Update);
        View= findViewById(R.id.View);
        ViewAll=findViewById(R.id.ViewAll);


        //set onClickListener for all buttons
Insert.setOnClickListener(this);
Delete.setOnClickListener(this);
Update.setOnClickListener(this);
View.setOnClickListener(this);
ViewAll.setOnClickListener(this);
//Creating database and table
        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR,name VARCHAR,marks VARCHAR);");

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View v) {
    if(v == Insert){
        //Checking for empty fields
        if(RollNo.getText().toString().trim().isEmpty() ||
                Name.getText().toString().trim().isEmpty() ||
                Marks.getText().toString().trim().isEmpty()){
            showMessage("Error", "Please enter all values");
            return;
        }
        //Inserting record
        db.execSQL("INSERT INTO student VALUES('"+RollNo.getText()+"','"+Name.getText()+
                "','"+Marks.getText()+"');");
        showMessage("Success", "Record added");
        clearText();
    }
    if (v == Delete) {
        if(RollNo.getText().toString().trim().isEmpty())
        {
            showMessage("Error", "Please enter RollNo");
            return;
        }
        Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+RollNo.getText()+"'", null);
        if(c.moveToFirst())
        {
            db.execSQL("DELETE FROM student WHERE rollno='"+RollNo.getText()+"'");
            showMessage("Success", "Record Deleted");
        }
        else
        {
            showMessage("Error", "Invalid RollNo");
        }
        clearText();
    }
        // Updating a record in the Student table
        if(v==Update)
        {
            // Checking for empty roll number
            if(RollNo.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter RollNo");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+RollNo.getText()+"'", null);
            if(c.moveToFirst()) {
                db.execSQL("UPDATE student SET name='" + Name.getText() + "',marks='" + Marks.getText() +
                        "' WHERE rollno='"+RollNo.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }
        // Display a record from the Student table
        if(v ==View)
        {
            // Checking for empty roll number
            if(RollNo.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+RollNo.getText()+"'", null);
            if(c.moveToFirst())
            {
                Name.setText(c.getString(1));
                Marks.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid RollNo");
                clearText();
            }
        }

        // Displaying all the records
        if(v==ViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Rollno: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Marks: "+c.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }

    }


    public  void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText(){
        RollNo.setText("");
        Name.setText("");
        Marks.setText("");
        RollNo.requestFocus();
    }
}