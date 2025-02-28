package model;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("warenkorbHandler")
@SessionScoped
public class WarenkorbHandler implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    private List<CartItem> cartItems;
    private double totalPrice;
    private Integer bookId;
    
    @PostConstruct
    public void init() {
        cartItems = new ArrayList<>();
        totalPrice = 0.0;
    }
  
    public void addToCart() {
    	Database db = Database.getInstance(); 
        if (bookId != null) {
            Book book = db.getBookDAO().getBookById(bookId);
            if (book != null) {
                addBookToCart(book);
                bookId = null;
            } else {
            	System.out.println("BOOK NOT FOUND");
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Book not found"));
            }
        }
    }
    
    private void addBookToCart(Book book) {
        boolean bookExists = false;
        for (CartItem item : cartItems) {
            if (item.getBook().getBookId() == book.getBookId()) {
                item.setQuantity(item.getQuantity() + 1);
                bookExists = true;
                break;
            }
        }

        if (!bookExists) {
        	cartItems.add(new CartItem(book, 1));
        }
        //cartState();
        calculateTotalPrice();

        // hiermit wird verhindert, dass das Aktualisieren des Warenkorbs, das aktuelle Buch nochmal einfügt
        FacesContext fc = FacesContext.getCurrentInstance();
    	ExternalContext ec = fc.getExternalContext();
    	try {
			ec.redirect("warenkorb.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /*
    public void cartState() {
        System.out.println("Aktueller Inhalt des Warenkorbs:");
        for (CartItem ci: cartItems) {
            System.out.print("Book ID: "+ ci.getBook().getBookId());
            System.out.print(" - Menge: "+ ci.getQuantity());
            System.out.println();
        }
    }
    */

    public void removeFromCart(Book book) {
    	cartItems.removeIf(item -> item.getBook().equals(book));
        //cartState();
        calculateTotalPrice();
    }
    
    private void calculateTotalPrice() {
        totalPrice = cartItems.stream()
                .mapToDouble(item -> item.getBook().getPrice() * item.getQuantity())
                .sum();
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public String getTotalPrice() {
        return getPriceFormat(totalPrice);
    }

    public Integer getBookId() {
        return bookId;
    }
    
    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }
    
    public void incrementQuantity(Book book) {
        for (CartItem item : cartItems) {
            if (item.getBook().equals(book)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }
        //cartState();
        calculateTotalPrice();
    }
    
    public void decrementQuantity(Book book) {
        for (CartItem item : cartItems) {
            if (item.getBook().equals(book) && item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                break;
            }
        }
        //cartState();
        calculateTotalPrice();
    }

    public String getPriceFormat(double price) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return nf.format(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
}

