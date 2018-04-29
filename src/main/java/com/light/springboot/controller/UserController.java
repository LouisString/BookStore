package com.light.springboot.controller;

import com.light.springboot.entity.Book;
import com.light.springboot.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.*;

@Controller
public class UserController {

    @Autowired
    private BookRepository bookRepository;

    @RequestMapping("/user")
    public String user(@AuthenticationPrincipal Principal principal, Model model){
        model.addAttribute("username", principal.getName());
        return "user";
    }

    @RequestMapping("/cart")
    @ResponseBody
    public ModelAndView showcart(HttpServletRequest request, HttpServletResponse response){
        HttpSession session = request.getSession();
        Map<Long, Long> cart = (Map<Long, Long>) session.getAttribute("cart");

        Map<Book, Long> items=new HashMap<>();
        Optional<Book> b;
        Long id;
        Long count;
        Boolean shownone = false;

        if (cart==null)
            shownone = true;
        else if(cart.size() == 0)
            shownone = true;
        if(shownone){
            ModelAndView mav = new ModelAndView("cart");
            mav.addObject("messages", "Add books RIGHT NOW!");
            mav.addObject("items", null);
            mav.addObject("order", null);
            return mav;
        }
        for (Map.Entry<Long, Long> entry: cart.entrySet()){
            id = entry.getKey();

            count = entry.getValue();
            b = bookRepository.findById(id);
            if (b.isPresent()){
                items.put(b.get(), count);
                System.out.println("cart--"+id+":"+count);
            }
        }
        ModelAndView mav = new ModelAndView("cart");
        mav.addObject("messages", null);
        mav.addObject("items", items);
        mav.addObject("order", null);
        return mav;
    }

    @RequestMapping("cart/order")
    @ResponseBody
    public ModelAndView order(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        Map<Long, Long> cart = (Map<Long, Long>) session.getAttribute("cart");
        Map<Long, Long> left_cart = new HashMap<>(cart);
        Map<Map<String, List<Long>>, Long> order = new HashMap<>();

        Optional<Book> b;
        Book book;
        Long id, count, btprice;
        Long tprice = Long.valueOf(0);
        Long old_stock;
        String title;
        List<Long> cplist;
        Map<String, List<Long>> binfo = new HashMap<>();

        Boolean shownone = false;

        if (cart==null)
            shownone = true;
        else if(cart.size() == 0)
            shownone = true;
        if(shownone){
            ModelAndView mav = new ModelAndView("cart");
            mav.addObject("messages", "Add books RIGHT NOW!");
            mav.addObject("items", null);
            mav.addObject("order", null);
            return mav;
        }
        for(Map.Entry<Long, Long> entry: cart.entrySet()){
            id = entry.getKey();
            count = entry.getValue();
            b = bookRepository.findById(id);
            if (b.isPresent()){
                book = b.get();
                if (book.getStock() < count){
                    continue;
                }
                left_cart.remove(id);
                title = book.getTitle();
                btprice = count * (book.getPrice());
                tprice += btprice;
                cplist = new LinkedList<>();
                cplist.add(count);
                cplist.add(btprice);
                old_stock = book.getStock();
                binfo.put(title, cplist);
                book.setStock(old_stock - count);
                System.out.println("o-c: "+(old_stock - count)+"in book: "+ book.getStock());
                System.out.println("order success++"+id+":"+title+", btprice: "+btprice+" count: "+count);
                bookRepository.save(book);
            }
        }
        order.put(binfo, tprice);
        session.setAttribute("cart", left_cart);
        ModelAndView mav = new ModelAndView("cart");
        mav.addObject("messages", null);
        mav.addObject("items", null);
        mav.addObject("order", order);
        return mav;
    }

}