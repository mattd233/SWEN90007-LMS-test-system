import db.DBConnection;
import db.mapper.InstructorMapper;
import db.mapper.SubjectMapper;
import domain.Exam;
import domain.Instructor;
import domain.Subject;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Program {


    public static void main(String args[]) throws Exception {
        List<Exam> exams = SubjectMapper.getAllExamsWithSubject("SWEN90007");
        for (Exam exam : exams) {
            System.out.println(exam.getTitle());
        }
    }


}
