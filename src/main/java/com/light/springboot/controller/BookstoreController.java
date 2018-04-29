package com.light.springboot.controller;


import com.light.springboot.entity.Book;
import com.light.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@RequestMapping("/bookstore")
public class BookstoreController {

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping(value={"", "/"})
    public ModelAndView showStore(){
        List<Book> books = (List<Book>) bookRepository.findAll();
        ModelAndView mav = new ModelAndView("bookstore");
        mav.addObject("books", books);
        return mav;
    }


    @RequestMapping(path="book", params="id")
    @ResponseBody
    public ModelAndView getBookById(@RequestParam("id") Long id){
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()){
            Book b = book.get();
            List<Book> books = new LinkedList<>();
            books.add(b);
            ModelAndView mav = new ModelAndView("book");
            mav.addObject("books", books);
            return mav;
        }
        else{
            List<Book> books = new LinkedList<>();
            ModelAndView mav = new ModelAndView("book");
            mav.addObject("books", books);
            return mav;
        }

    }

    @RequestMapping(path="addtocart",params={"id", "count"})
    @ResponseBody
    public void addtocar(@RequestParam("id") Long id, @RequestParam("count") Long count,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Optional<Book> book = bookRepository.findById(id);

        PrintWriter out = response.getWriter();

        String url = request.getHeader("Referer");

        if (book.isPresent()){
            Map<Long, Long> cart = ( Map<Long, Long>) session.getAttribute("cart");
            Long stock = book.get().getStock();
            if (cart==null){
                cart=new HashMap<Long, Long>();
            }
            if(cart.containsKey(id)){
                if (cart.get(id) + count > stock){
                    out.println("over");
                    out.close();
                    return;
                }
                cart.put(id, cart.get(id) + count);
            }
            else{
                cart.put(id, count);
            }
            for (Long bookid : cart.keySet()) {
                System.out.println(bookid + ":" + cart.get(bookid));
            }
            session.setAttribute("cart", cart);
            out.println("good");
            out.close();
        }
        else{
            out.println("bad");
            out.close();
        }

    }

    @RequestMapping(path="addtocart",params={"id", "all"})
    @ResponseBody
    public void addalltocar(@RequestParam("id") Long id,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Optional<Book> book = bookRepository.findById(id);

        PrintWriter out = response.getWriter();

        if (book.isPresent()){
            Map<Long, Long> cart = (Map<Long, Long>) session.getAttribute("cart");
            Long stock = book.get().getStock();
            if (cart==null){
                cart=new HashMap<Long, Long>();
            }
            if(cart.containsKey(id)){
                cart.put(id, stock);
            }
            for (Long bookid : cart.keySet()) {
                System.out.println(bookid + ":" + cart.get(bookid));
            }
            session.setAttribute("cart", cart);
            out.println("good");
            out.close();
        }
        else{
            out.println("bad");
            out.close();
        }

    }

    @RequestMapping("/delefromcart")
    @ResponseBody
    public void deletefromcart(@RequestParam("id") Long id,
                               HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Map<Long, Long> cart = (Map<Long, Long>) session.getAttribute("cart");
        PrintWriter out = response.getWriter();

        if (cart==null){
            out.println("no cart");
            out.close();
        }
        else{
            System.out.println(id);
            if (cart.containsKey(id)){
                cart.remove(id);
                session.setAttribute("cart", cart);
                out.println("good");
                out.close();
            }
            else{
                System.out.println("no found");
                out.println("bad");
                out.close();
            }

        }
    }

    @RequestMapping("/changecount")
    @ResponseBody
    public void changecount(@RequestParam("id") Long id, @RequestParam("count") Long count,
                            HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Map<Long, Long> cart = (Map<Long, Long>) session.getAttribute("cart");
        PrintWriter out = response.getWriter();

        if (cart==null){
            out.println("no cart");
            out.close();
        }
        else if (count != null){
            if (cart.containsKey(id)) {
                cart.put(id, count);
                System.out.println("update--"+id+":"+count);
                session.setAttribute("cart", cart);
                out.println("good");
                out.close();
            } else {
                out.println("bad");
                out.close();
            }
        }
        else{
            if (cart.containsKey(id)) {
                cart.put(id, Long.valueOf(1));
                System.out.println("update--"+id+":"+cart.get(id));
                session.setAttribute("cart", cart);
                out.println("good");
                out.close();
            }
        }
    }

    @RequestMapping("/saveBook")
    @ResponseBody
    public String saveBook() {
        Book b = new Book();

        b.setAuthor("Lover");
        b.setLanguage("Chinese");
        b.setPrice((long) 25);
        b.setPublished("2016");
        b.setSales("50 billion");
        b.setTitle("Peace");
        System.out.println("b.id = " + b.getId() + "\n");
        bookRepository.save(b);
        return "bookstore";
    }

}
