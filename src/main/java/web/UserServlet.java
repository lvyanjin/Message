package web;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import pojo.Comment;
import pojo.Follow;
import pojo.Message;
import pojo.User;
import utils.JdbcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet(name = "UserServlet")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            User user = (User)request.getSession().getAttribute("user");
            Connection conn = JdbcUtils.getConnection();
            String sql = "select * from user";
            String sql1 = "select * from message where name =?";
            String sql2 = "select * from comment where created_by =?";
            String sql3 = "select * from follow where follow_name =?";
            QueryRunner runner = new QueryRunner();
            String name = user.getUsername();

            BeanListHandler<User> handler = new BeanListHandler<>(User.class);
            BeanListHandler<Message> handler1 = new BeanListHandler<>(Message.class);
            BeanListHandler<Comment> handler2 = new BeanListHandler<>(Comment.class);
            BeanListHandler<Follow> handler3 = new BeanListHandler<>(Follow.class);
            List<User> userList = runner.query(conn, sql, handler);
            List<Message> messageList = runner.query(conn,sql1,handler1,name);
            List<Comment> commentList = runner.query(conn, sql2, handler2, name);
            List<Follow> followList = runner.query(conn, sql3, handler3, name);
            System.out.println(user.getId());
            System.out.println("USER"+userList.get(0).getUsername());

            System.out.println(userList);
            System.out.println(messageList);
            System.out.println(commentList);

            request.setAttribute("userList",userList);
            request.setAttribute("messageList",messageList);
            request.setAttribute("commentList",commentList);
            request.setAttribute("followList",followList);
            request.getRequestDispatcher("/pages/usercenter.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }
}
