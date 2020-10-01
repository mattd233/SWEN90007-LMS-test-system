package main.java;

import main.java.db.DBConnection;
import main.java.db.mapper.*;
import main.java.domain.*;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// This is for testing code, ignore this class.
public class Program {


    public static void main(String args[]) throws Exception {
        Exam exam = new Exam("SWEN90009", "Sam's exam", "stupid exam");
        System.out.println(ExamMapper.insert(exam));

    }


}
