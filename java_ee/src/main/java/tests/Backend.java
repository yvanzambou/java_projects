package tests;

import model.Book;
import model.Category;
import model.Database;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/backend")
public class Backend extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    public Backend() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if (action == null || action.isEmpty()) {
            action = "showSelectionForm";
        } else {
            action = request.getParameter("action");
        }

        switch (action) {
            case "showSelectionForm":
                showSelectionForm(out);
                break;
            case "insertBook":
                String BuchName = request.getParameter("BuchName") != null ? request.getParameter("BuchName"): "";
                String BuchISBN = request.getParameter("BuchISBN") != null ? request.getParameter("BuchISBN"): "";
                String BuchPreis = request.getParameter("BuchPreis") != null ? request.getParameter("BuchPreis"): "";
                String BuchJahr =request.getParameter("BuchJahr") != null ? request.getParameter("BuchJahr"): "";
                String BuchAutor = request.getParameter("BuchAutor") != null ? request.getParameter("BuchAutor"): "";
                String BuchVerlag = request.getParameter("BuchVerlag") != null ? request.getParameter("BuchVerlag"): "";
                String BuchBeschreibung = request.getParameter("BuchBeschreibung") != null ? request.getParameter("BuchBeschreibung"): "";
                String BuchImagePath = request.getParameter("BuchImagePath") != null ? request.getParameter("BuchImagePath"): "";
                String[] selectedCategories = request.getParameterValues("BuchKategorie");
                List<String> errors = (List<String>)request.getAttribute("errors");

                insertBook(out, BuchName, BuchISBN, BuchPreis, BuchJahr, BuchAutor, BuchVerlag, BuchBeschreibung, BuchImagePath, errors, selectedCategories);
                break;
            case "showBooks":
                showBooks(out);
                break;
            case "showCategories":
                showCategories(out);
                break;
            case "insertCategory":
                insertCategory(out);
                break;
            default:
                showSelectionForm(out);
                break;
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            response.sendRedirect("backend");
            return;
        }

        Database db = Database.getInstance();

        switch (action) {
            case "insertBook":
                response.setContentType("text/html;charset=UTF-8");
                PrintWriter out = response.getWriter();

                String BuchTitel = request.getParameter("BuchName");
                String BuchISBN = request.getParameter("BuchISBN");
                String BuchPreis = request.getParameter("BuchPreis");
                String BuchJahr =request.getParameter("BuchJahr");
                String BuchAutor = request.getParameter("BuchAutor");
                String BuchVerlag = request.getParameter("BuchVerlag");
                String BuchBeschreibung = request.getParameter("BuchBeschreibung");
                String BuchImagePath = request.getParameter("BuchImagePath");
                String[] BuchKategorie = request.getParameterValues("BuchKategorie");

                List<String> errors = new ArrayList<>();
                try {
                    double price = Double.parseDouble(BuchPreis);
                    if(price <= 0) {
                        errors.add("<p style=\"color:red; font-size:16px\">Negative Preis nicht erlaubt</p>\r\n");
                    }
                } catch (NumberFormatException e) {
                    errors.add("<p style=\"color:red; font-size:16px\">Ungültiger Wert - Preis-Format: 1.00 oder 1</p>\r\n");
                }
                if(!db.getBookDAO().hasValidIsbn(BuchISBN)) {
                    errors.add("<p style=\"color:red; font-size:16px\">ungültige ISBN-Nummer, Beispiel: 123-4-56-789012-3</p>\r\n");
                }
                if(db.getBookDAO().isbnAlreadyExists(BuchISBN)) {
                    errors.add("<p style=\"color:red; font-size:16px\">ISBN-Nummer schon vorhanden</p>\r\n");
                }
                if(!db.getBookDAO().hasValidDate(BuchJahr)) {
                    errors.add("<p style=\"color:red; font-size:16px\">ungültiges Jahr. Datum zwischen 1900 und 2024</p>\r\n");
                }
                if(!errors.isEmpty()) {
                    request.setAttribute("errors", errors);
                    doGet(request, response);
                    for(String error: errors) {
                        out.println(error);
                    }
                } else {
                    Book book = new Book(0,
                            BuchTitel,
                            BuchAutor,
                            BuchBeschreibung,
                            Double.parseDouble(BuchPreis),
                            BuchVerlag,
                            Integer.parseInt(BuchJahr),
                            BuchISBN,
                            BuchImagePath);

                    db.getBookDAO().insertBook(book, BuchKategorie);
                    response.sendRedirect("backend");
                }
                break;
            case "insertCategory":
                String newCategory = request.getParameter("NeueBuchKategorie") != null ? request.getParameter("NeueBuchKategorie"): "";
                Category category = new Category(0, newCategory);
                if (!newCategory.isEmpty()) {
                    if(!db.getCategoryDAO().categoryAlreadyExists(category)) {
                        db.getCategoryDAO().insertCategory(category);
                    }
                    response.sendRedirect("backend");
                }
                break;
            default:
                break;
        }
        doGet(request, response);
    }

    private void showSelectionForm(PrintWriter out) {
        String form = "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "    <meta charset=\"utf-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
                + "    <title>Aktion auswählen</title>\r\n"
                + "    <style>\r\n"
                + "        .action-btn {\r\n"
                + "            margin-bottom: 5px;\r\n"
                + "        }\r\n"
                + "    </style>\r\n"
                + "</head>"
                + "<body>\r\n"
                + "    <h3>Bitte wählen Sie eine Aktion aus:</h3>\r\n"
                + "    <a href='?action=insertBook'><button class=\"action-btn\">Neues Buch hinzufügen</button></a><br>"
                + "    <a href='?action=showBooks'><button class=\"action-btn\">Alle Bücher anzeigen</button></a><br>"
                + "	   <a href='?action=showCategories'><button class=\"action-btn\">Alle Kategorie anzeigen</button></a><br>"
                + "	   <a href='?action=insertCategory'><button class=\"action-btn\">Neue Kategorie hinzufügen</button></a>"
                + "</body>\r\n"
                + "</html>";
        out.println(form);
    }

    private void insertBook(PrintWriter out, String BuchName, String BuchISBN,
                            String BuchPreis, String BuchJahr, String BuchAutor,
                            String BuchVerlag, String BuchBeschreibung, String BuchImagePath, List<String> errors, String[] selectedCategories) {

        Database db = Database.getInstance();
        String formular = "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "	<meta charset=\"utf-8\">\r\n"
                + "	<title>Buch hinzufügen</title>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "	<h3>Fügen Sie ein neues Buch hinzu:</h3><br>\r\n";
        if (errors != null) {
            for (String error : errors) {
                formular += error;
            }
        }
        formular +=  " <table>\r\n"
                + "			<form action='backend?action=insertBook' method=\"post\">\r\n"
                + "			<tr>\r\n"
                + "				<td>\r\n"
                + "					<table>\r\n"
                + "			<tr>\r\n"
                + "				<td>Name</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchName\" value = '"+BuchName+"' required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>ISBN</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchISBN\" value = '"+BuchISBN+"' placeholder=\"123-4-56-789012-3\"required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>Preis</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchPreis\" value = '"+BuchPreis+"' placeholder=\"0.00\"required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>Erscheinungsjahr</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchJahr\" placeholder=\"JJJJ\" value = '"+BuchJahr+"' required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>Autor</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchAutor\" value = '"+BuchAutor+"' required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>Verlag</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchVerlag\" value = '"+BuchVerlag+"' required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>Beschreibung</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchBeschreibung\" value = '"+BuchBeschreibung+"' required></td>\r\n"
                + "			</tr>\r\n"
                + 			"<tr>\r\n"
                + "				<td>Bild-Pfad</td>\r\n"
                + "				<td><input type=\"text\" name=\"BuchImagePath\" value = '"+BuchImagePath+"' required></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td>Kategorie</td>\r\n"
                + "				<td><select class=\"select form-control\" name=\"BuchKategorie\" multiple required style=\"padding-right: 30px;\">";
        for (Category category : db.getCategoryDAO().getAllCategories()) {
            formular += "<option value=\"" + category.getName() + "\"";
            if(selectedCategories != null) {
                for (String selectedCategory: selectedCategories) {
                    if  (String.valueOf(category.getCategoryId()).equals(selectedCategory)) {
                        formular += " selected";
                        break;
                    }
                }
            }
            formular += ">" + category.getName() + "</option>\r\n";
        }
        formular += "</select></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td><br><br></td>\r\n"
                + "			</tr>\r\n"
                + "			<tr>\r\n"
                + "				<td><input type=\"reset\" value=\"Reset\"></td>\r\n"
                + "				<td><input type=\"submit\" value=\"Hinzufügen\"></td>\r\n"
                + "			</tr>\r\n"
                + "			</form>\r\n"
                + "</body>\r\n"
                + "</html>";

        out.println(formular);
        out.close();
    }

    private void insertCategory(PrintWriter out) {

        String form = "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "	<meta charset=\"utf-8\">\r\n"
                + "	<title>Neue Kategorie</title>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "	<h3>Fügen Sie ein neue Kategorie hinzu:</h3><br>\r\n"
                + "		<form action='backend?action=insertCategory' method=\"post\">\r\n"
                + "			<table>\r\n"
                + "				<tr>\r\n"
                + "					<td>Neue Kategorie</td>\r\n"
                + "				</tr>\r\n"
                + "				<tr>\r\n"
                + "					<td><input type=\"text\" name=\"NeueBuchKategorie\" ></td>\r\n"
                + "				</tr>\r\n"
                + "				<tr>\r\n"
                + "					<td><input type=\"submit\" value=\"Neue Kategorie hinzufügen\"></td>\r\n"
                + "				</tr>\r\n"
                + "			</table>\r\n"
                + "		</form>\r\n"
                + "</body>\r\n"
                + "</html>";

        out.println(form);

    }

    private void showCategories(PrintWriter out) {

        Database db = Database.getInstance();
        String form = "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "	<meta charset=\"utf-8\">\r\n"
                + "	<title>Neue Kategorie</title>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "	<h3>Kategorie anzeigen</h3><br>\r\n"
                + "		<form action='backend?action=showCategories' method=\"post\">\r\n"
                + "			<table>\r\n"
                + "            <tr>\r\n"
                + "                <td>Vorhandene Kategorie:</td>\r\n"
                + "            </tr>\r\n"
                + "            <tr>\r\n"
                + "                <td>\r\n"
                + "                    <ul>\r\n";
        for (Category category : db.getCategoryDAO().getAllCategories()) {
            form += "<li>" + category.getName() + "</li>\r\n";
        }
        form += "              </ul>\r\n"
                + "                </td>\r\n"
                + "            </tr>\r\n"
                + "			</table>\r\n"
                + "		</form>\r\n"
                + "</body>\r\n"
                + "</html>";
        out.println(form);
    }

    private void showBooks(PrintWriter out) {

        Database db = Database.getInstance();
        String form = "<!DOCTYPE html>\r\n"
                + "<html>\r\n"
                + "<head>\r\n"
                + "    <meta charset=\"utf-8\">\r\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
                + "    <title>AMAZON LIGHT</title>\r\n"
                + "    <link rel=\"stylesheet\" href=\"https://cdn.jsdelivr.net/npm/bulma@0.9.4/css/bulma.min.css\">\r\n"
                + "    <style>\r\n"
                + "        th {\r\n"
                + "            padding-bottom: 20px;\r\n"
                + "            font-size: 1.5rem;\r\n"
                + "        }\r\n"
                + "        table {\r\n"
                + "            width: 100%;\r\n"
                + "            border-collapse: collapse;\r\n"
                + "        }\r\n"
                + "        th, td {\r\n"
                + "            padding: 8px;\r\n"
                + "            text-align: left;\r\n"
                + "            border-bottom: 1px solid #ddd;\r\n"
                + "        }\r\n"
                + "        /* Stil für gerade Zeilen */\r\n"
                + "        tr:nth-child(even) {\r\n"
                + "            background-color: #f2f2f2;\r\n"
                + "        }\r\n"
                + "        /* Stil für ungerade Zeilen (optional) */\r\n"
                + "        tr:nth-child(odd) {\r\n"
                + "            background-color: #ffffff;\r\n"
                + "        }"
                + "    </style>\r\n"
                + "</head>\r\n"
                + "<body>\r\n"
                + "    <section class=\"section\">\r\n"
                + "    </section>\r\n"
                + "\r\n"
                + "    <div class=\"resumePage\" style=\"margin: auto; width: 95%; margin-top: -30px; margin-bottom: 50px;\">\r\n"
                + "        <span style=\"font-size: 1.7rem;\"><strong>Datenbankübersicht</strong></span>\r\n"
                + "        <hr>\r\n"
                + "\r\n"
                + "        <table class=\"cart\" width=\"100%\">\r\n"
                + "            <tr>\r\n"
                + "                <th>ID</th>\r\n"
                + "                <th>Titel</th>\r\n"
                + "                <th>Autor</th>\r\n"
                + "                <th>Beschreibung</th>\r\n"
                + "                <th>Preis</th>\r\n"
                + "                <th>Verlag</th>\r\n"
                + "                <th>Jahr</th>\r\n"
                + "                <th>ISBN</th>\r\n"
                + "                <th>Bild-Pfad</th>\r\n"
                + "                <th>Kategorie</th>\r\n"
                + "            </tr>\r\n"
                + "			\r\n";
        for(Book b: db.getBookDAO().getAllBooks()) {
            form += "<tr>\r\n"
                    +"	<td><span>"+b.getBookId()+"</span></td>\r\n"
                    +"  <td><span>"+b.getTitle()+"</span></td>\r\n"
                    +"  <td><span>"+b.getAuthor()+"</span><br><br></td>\r\n"
                    +"  <td><span>"+b.getDescription()+"</span></td>\r\n"
                    +"  <td><span>"+b.getPrice()+" €</span></td>\r\n"
                    +"  <td><span>"+b.getPublisher()+"</span></td>\r\n"
                    +"  <td><span>"+b.getPublishingYear()+"</span></td>\r\n"
                    +"  <td><span>"+b.getIsbn()+"</span></td>\r\n"
                    +"  <td><span>"+b.getImagePath()+"</span></td>\r\n"
                    +"  <td><span>"+db.getCategoryDAO().getAllCategoriesForBook(b)+"</span></td>\r\n"
                    +"</tr>\r\n";
        }
        form += " 	       </table>\r\n"
                + "		</div>\r\n"
                + "		\r\n"
                + "		</body>\r\n"
                + "		\r\n"
                + "</html>";
        out.println(form);
    }
}