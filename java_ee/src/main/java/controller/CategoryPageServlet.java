package controller;

import java.io.IOException;
import java.io.Serial;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.BookDAO;
import dao.CategoryDAO;
import dao.CustomerDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import model.Database;
import thymeleaf.ThymeleafConfig;

@WebServlet("/categoryPage")
public class CategoryPageServlet extends HttpServlet {
	@Serial
	private static final long serialVersionUID = 1L;

    public CategoryPageServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		TemplateEngine engine = ThymeleafConfig.getTemplateEngine();
		WebContext context = new WebContext(request, response, request.getServletContext());

		HttpSession session = request.getSession();
		Integer customerId = (Integer) session.getAttribute("customerId");

		response.setCharacterEncoding("UTF-8");
		
		Database db = Database.getInstance();
		BookDAO bookDAO = db.getBookDAO();
		CategoryDAO categoryDao = db.getCategoryDAO();
		CustomerDAO customerDAO = db.getCustomerDAO();

		//String categoryId = request.getParameter("categoryId") != null ? request.getParameter("categoryId"): "1";
		String categoryId = request.getParameter("categoryId");

		if (bookDAO.isNumeric(categoryId) || !categoryDao.categoryIdExists(categoryId)) {
			//response.sendRedirect(request.getContextPath() + "/categoryPage");
			response.sendRedirect(request.getContextPath() + "/categoryPage?categoryId=1");
			return;
		}

		String categoryName = categoryDao.getCategoryById(Integer.parseInt(categoryId));
		String customerName = customerDAO.getCustomer(customerId).getFirstname();
		
		context.setVariable("allCategories", categoryDao.getAllCategories());
		context.setVariable("categoryName", categoryName);
		context.setVariable("customerName", customerName);
		context.setVariable("booksForCategory", bookDAO.getBooks(categoryName));
		engine.process("category_page.html", context, response.getWriter());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doGet(request, response);
	}
}
