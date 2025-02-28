package controller;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BookDAO;
import model.Book;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import model.Database;
import thymeleaf.ThymeleafConfig;

@WebServlet("/bookDetailServlet")
public class BookDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public BookDetailServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		TemplateEngine engine = ThymeleafConfig.getTemplateEngine();
		WebContext context = new WebContext(request, response, request.getServletContext());
		response.setCharacterEncoding("UTF-8");
		
		Database db = Database.getInstance();
		BookDAO bookDAO = db.getBookDAO();
		
		String bookId = request.getParameter("bookId") != null ? request.getParameter("bookId"): "1";

		if(bookDAO.isNumeric(bookId) || !bookDAO.bookIdExists(bookId)) {
			response.sendRedirect(request.getContextPath() + "/categoryPage");
	        return;
		}
		
		Book book = bookDAO.getBookById(Integer.parseInt(bookId));
		
		context.setVariable("book", book);
		context.setVariable("bookPrice", book.getPriceFormat());
		engine.process("book_detail.html", context, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
