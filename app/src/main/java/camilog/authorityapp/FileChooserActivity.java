package camilog.authorityapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by diego on 15-01-16.
 */
public class FileChooserActivity extends ListActivity{
    private File currentDir;
    private FileArrayAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentDir = new File("/sdcard/");
        fill(currentDir);
    }

    //Get all files and folders for the current directory
    private void fill(File f) {

        //Get an array of all the files and directories in the current directory
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<Option> dir = new ArrayList<Option>();
        List<Option> fls = new ArrayList<Option>();

        //Sort files and directories into the appropriate ListArray
        try {
            for (File ff: dirs) {
                if (ff.isDirectory())
                    dir.add(new Option(ff.getName(),"Folder",ff.getAbsolutePath()));
                else
                    fls.add(new Option(ff.getName(),"File Size: "+ff.length(),ff.getAbsolutePath()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Sort the ListArrays alphabetically and pass folders array to directory array.
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase("sdcard"))
            dir.add(0,new Option("..","Parent Directory",f.getParent()));
        adapter = new FileArrayAdapter(FileChooserActivity.this,R.layout.file_view,dir);
        this.setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Option o = adapter.getItem(position);
        String path;

        if(o.getData().equalsIgnoreCase("folder")||o.getData().equalsIgnoreCase("parent directory")){
            currentDir = new File(o.getPath());
            fill(currentDir);
        }
        else {
            path = onFileClick(o);
            Intent data = new Intent();
            data.putExtra("keyPath", path);
            setResult(this.RESULT_OK,data);
            finish();
        }
    }

    private String onFileClick(Option o) {
        return o.getPath();
    }
}
