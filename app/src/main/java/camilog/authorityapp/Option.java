package camilog.authorityapp;

/**
 * Created by diego on 14-01-16.
 */

//Hold the data obtained from listing all the files and directories
public class Option implements Comparable<Option> {
    private String name;
    private String path;
    private String data;

    public Option(String n,String d,String p) {
        name = n;
        data = d;
        path = p;
    }

    @Override
    public int compareTo(Option o) {
        if(this.name != null)
            return this.name.toLowerCase().compareTo(o.getName().toLowerCase());
        else
            throw new IllegalArgumentException();
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }

    public String getPath() {
        return path;
    }


}
