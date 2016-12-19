package com.kiuber.blog.bean;

import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Kiuber on 2016/12/19.
 */

public class FileBean implements Serializable {

    private String file_name;
    private String file_time;
    private int file_folder_num;


    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_time() {
        return file_time;
    }

    public void setFile_time(String file_time) {
        this.file_time = file_time;
    }

    public int getFile_folder_num() {
        return file_folder_num;
    }

    public void setFile_folder_num(int file_folder_num) {
        this.file_folder_num = file_folder_num;
    }
}
