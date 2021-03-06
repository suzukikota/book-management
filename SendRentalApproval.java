package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.BookBean;
import bean.Employee_InfoBean;

@WebServlet("/SendRentalApproval")
public class SendRentalApproval extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public SendRentalApproval() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String title = request.getParameter("title");
		String btn = request.getParameter("btn");

		//	「承認」が選択された場合
		if(btn.equals("approval")) {

		BookBean bookBean=new BookBean();
		bookBean.UpdateRentalWating(btn, title);

		List<BookBean> list=bookBean.Rental(title);
		for(int i=0;i<list.size();i++) {
			bookBean=list.get(i);
		}

		String selectName=bookBean.getRental();
		String selectTitle=bookBean.getTitle();
		Employee_InfoBean employee_InfoBean=new Employee_InfoBean();

		List<Employee_InfoBean> list2=employee_InfoBean.Employee_InfoDBtoList3(selectName);
		for(int i=0;i<list.size();i++) {
			employee_InfoBean=list2.get(i);
		}

		String mail=employee_InfoBean.getMail();
		String EmailTitle = "書籍のレンタル申請の承認";

        String message = "書籍のレンタル申請の承認を受け取りました。"+"\r\n"
        				+ "こちらは自動送信になります。"+"\r\n"
        				+ "申請者名:"+selectName +"さん"+"\r\n"
        				+ "書籍名:"+selectTitle+"\r\n"
        				+ "レンタルの申請を承りました。"+"\r\n"
        				+ "次の帰社日にお受け取り下さい。"+"\r\n"
        				+ "よろしくお願いいたします"+"\r\n"
        				+ "※このメール内容に心あたりのない場合は、お手数ですが、総務までご連絡をお願いいたします。";

        response.setContentType("text/html; charset=UTF-8");

        PrintWriter out = response.getWriter();
        try{
            Properties property = new Properties();

            property.put("mail.smtp.host","smtp.gmail.com");

            property.put("mail.smtp.auth", "true");
            property.put("mail.smtp.starttls.enable", "true");
            property.put("mail.smtp.host", "smtp.gmail.com");
            property.put("mail.smtp.port", "587");
            property.put("mail.smtp.debug", "true");

            Session session = Session.getInstance(property, new javax.mail.Authenticator(){
                protected PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication("syosekikanri.sakuracom@gmail.com", "hpg8jcwpr427");//送信者Googleアカウント 本番ではここにさくら総務のGoogleアカウント情報を記入
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);

            InternetAddress toAddress =
                    new InternetAddress(mail,selectName+"さん");// employee_infoテーブルのmailカラムに格納されたメールアドレスの中から、申請者に該当するメールアドレス宛に送る

            mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);

            InternetAddress fromAddress =
                    new InternetAddress("syosekikanri.sakuracom@gmail.com","さくら書籍管理");//	送信者情報	本番ではここに("さくら総務メールアドレス","さくら総務")

            mimeMessage.setFrom(fromAddress);

            mimeMessage.setSubject(EmailTitle, "ISO-2022-JP");

            mimeMessage.setText(message,"ISO-2022-JP");

            Transport.send(mimeMessage);

            out.println("<htm><body>");
            out.println("■レンタル申請の承認を申請者へ送信しました。");
            out.println("<br>");
            out.println("<button onclick=\"location.href='OkLogin.jsp'\">管理用画面ホーム</button>");
            out.println("<body></html>");
        }
        catch(Exception e){
        	out.println("<htm><body>");
            out.println("■レンタル申請の承認を申請者へ送信しました。");
            out.println("<br>");
            out.println("<button onclick=\"location.href='OkLogin.jsp'\">管理用画面ホーム</button>");
            out.println("<body></html>");
        }

        out.close();

     // 「拒否」が選択された場合

    }else if(btn.equals("rejection")) {
    		BookBean bookBean=new BookBean();

    		List<BookBean> list=bookBean.Rental(title);
    		for(int i=0;i<list.size();i++) {
    			bookBean=list.get(i);
    		}

    		String selectName=bookBean.getRental();
    		String selectTitle=bookBean.getTitle();
    		Employee_InfoBean employee_InfoBean=new Employee_InfoBean();

    		List<Employee_InfoBean> list2=employee_InfoBean.Employee_InfoDBtoList3(selectName);
    		for(int i=0;i<list.size();i++) {
    			employee_InfoBean=list2.get(i);
    		}
    		String mail=employee_InfoBean.getMail();
    		String EmailTitle = "書籍のレンタル申請の否認";

            String message = "書籍のレンタル申請の否認を受け取りました。"+"\r\n"
            				+ "こちらは自動送信になります。"+"\r\n"
            				+ "申請者名:"+selectName +"さん"+"\r\n"
            				+ "書籍名:"+selectTitle+"\r\n"
            				+ "レンタルの申請を承ることが出来ませんでした。"+"\r\n"
            				+ "よろしくお願いいたします。"+"\r\n"
            				+ "※このメール内容に心あたりのない場合は、お手数ですが、総務までご連絡をお願いいたします。";

            response.setContentType("text/html; charset=UTF-8");

            PrintWriter out = response.getWriter();
            try{
                Properties property = new Properties();

                property.put("mail.smtp.host","smtp.gmail.com");

                property.put("mail.smtp.auth", "true");
                property.put("mail.smtp.starttls.enable", "true");
                property.put("mail.smtp.host", "smtp.gmail.com");
                property.put("mail.smtp.port", "587");
                property.put("mail.smtp.debug", "true");

                Session session = Session.getInstance(property, new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication("syosekikanri.sakuracom@gmail.com", "hpg8jcwpr427");//送信者Googleアカウント 本番ではここにさくら総務のGoogleアカウント情報を記入
                    }
                });

                MimeMessage mimeMessage = new MimeMessage(session);

                InternetAddress toAddress =
                        new InternetAddress(mail,selectName+"さん");// employee_infoテーブルのmailカラムに格納されたメールアドレスの中から、申請者に該当するメールアドレス宛に送る

                mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);

                InternetAddress fromAddress =
                        new InternetAddress("syosekikanri.sakuracom@gmail.com","さくら書籍管理");//	送信者情報	本番ではここに("さくら総務メールアドレス","さくら総務")

                mimeMessage.setFrom(fromAddress);

                mimeMessage.setSubject(EmailTitle, "ISO-2022-JP");

                mimeMessage.setText(message,"ISO-2022-JP");

                Transport.send(mimeMessage);
                bookBean.UpdateRentalWating(btn, title);
                out.println("<htm><body>");
                out.println("■レンタル申請の否認を申請者へ送信しました。");
                out.println("<br>");
                out.println("<button onclick=\"location.href='OkLogin.jsp'\">管理用画面ホーム</button>");
                out.println("<body></html>");
            }
            catch(Exception e){
                out.println("<html><body>");
                out.println("■レンタル申請の否認を申請者への送信に失敗しました");
                out.println("<br>");
                out.println("<button onclick=\"location.href='OkLogin.jsp'\">管理用画面ホーム</button>");
                out.println("</body></html>");
            }

            out.close();
    }
	}
}