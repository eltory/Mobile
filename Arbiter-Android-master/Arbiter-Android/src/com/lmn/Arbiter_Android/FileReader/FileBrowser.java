package com.lmn.Arbiter_Android.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lmn.Arbiter_Android.R;

/**
 * Created by pc on 2016-06-07.
 */
public class FileBrowser extends ListActivity {
    private List<String> item = null;
    private List<String> path = null;
    private String root = "/";
    private TextView mPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_browser);
        mPath = (TextView) findViewById(R.id.path);
        getDir(root);
    }

    private void getDir(String dirPath) {
        mPath.setText("Location: " + dirPath);
        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        File[] files = f.listFiles();
        if (!dirPath.equals(root)) {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            path.add(file.getPath());
            if (file.isDirectory())
                item.add(file.getName() + "/");
            else
                item.add(file.getName());
        }
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
        setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        File file = new File(path.get(position));
        if (file.isDirectory())
        {
            if (file.canRead())
                getDir(path.get(position));
            else
            {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("[" + file.getName() + "] folder can't be read!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
            }
        }
        else
        {
            String fileName = file.getName();
            if (fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).equalsIgnoreCase("png") ||
                    fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).equalsIgnoreCase("jpg") ||
                    fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).equalsIgnoreCase("gif"))
            {

                Intent intent = new Intent();
                intent.putExtra("path", path.get(position));
                intent.putExtra("name",fileName);
                setResult(202, intent);
                finish();
            }
            else if (fileName.substring(fileName.lastIndexOf(".")+1, fileName.length()).equalsIgnoreCase("shp"))
            {
                fileName = fileName.substring(0,fileName.lastIndexOf("."));
                Intent intent = new Intent();
                intent.putExtra("name",fileName);
                setResult(303, intent);
                finish();
            }
            else {
                new AlertDialog.Builder(this)
                        .setIcon(R.drawable.ic_launcher)
                        .setTitle("[" + file.getName() + "] can not be selected.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        }).show();
            }
        }
    }
}
