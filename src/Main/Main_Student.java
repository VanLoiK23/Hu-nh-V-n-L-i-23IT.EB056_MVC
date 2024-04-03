package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Controller.Controller_sv;

import Model.SinhVien;
import View.Giaodien_Student;

public class Main_Student {
public static void main(String[] args) {
	Giaodien_Student frame=new Giaodien_Student();
    Controller_sv sv=new Controller_sv(frame);

    frame.main(args);
}
}
