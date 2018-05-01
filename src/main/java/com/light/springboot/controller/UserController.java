package com.light.springboot.controller;

import com.light.springboot.entity.Book;
import com.light.springboot.entity.BookOrder;
import com.light.springboot.entity.OrderPrimaryKey;
import com.light.springboot.entity.User;
import com.light.springboot.repository.BookOrderRepository;
import com.light.springboot.repository.BookRepository;
import com.light.springboot.utils.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @Autowired
    private BookOrderRepository orderRepository;


    @RequestMapping("/user")
    public ModelAndView user(){

        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();

        ModelAndView mav = new ModelAndView("user");
        mav.addObject("user", user);
        return mav;
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


        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();

        Optional<Book> b;
        Book book;
        Long id, count, btprice, order_id, user_id=user.getId();
        Long tprice = Long.valueOf(0);
        Long old_stock;
        String title;
        List<Long> cplist;
        OrderPrimaryKey PK;
        Date date;
        String deliver_status, addr;
        BookOrder bookOrder;
        Map<String, List<Long>> binfo = new HashMap<>();

        order_id = (orderRepository.getSize() != 0)? orderRepository.findMaxOrderId():Long.valueOf(1);
        order_id++;
        System.out.println(order_id);

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
            /*id means the book's id*/
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
                bookRepository.save(book);
                System.out.println("o-c: "+(old_stock - count)+"in book: "+ book.getStock());
                System.out.println("order success++"+id+":"+title+", btprice: "+btprice+" count: "+count);
                PK = new OrderPrimaryKey(order_id, id, user_id);
                date = new Date();
                deliver_status = "Not Delivered";
                addr = "Dreamland";
                bookOrder = new BookOrder(PK, count, addr, btprice, deliver_status, date);
                orderRepository.save(bookOrder);
            }
        }
        List<BookOrder> orders = orderRepository.findAll();

        for(int i = 0; i < orders.size(); ++i){
            orders.get(i).toString();
        }
        order.put(binfo, tprice);
        session.setAttribute("cart", left_cart);
        ModelAndView mav = new ModelAndView("cart");
        mav.addObject("messages", null);
        mav.addObject("items", null);
        mav.addObject("order", order);
        return mav;
    }

    @RequestMapping("/user/uploadImage")
    @ResponseBody
    public String uploadImage(String imgcode, HttpServletRequest request ){

        imgcode = request.getParameter("imgcode");
        System.out.println(imgcode);
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();

        System.out.println(imgcode);

        String imgstr = imgcode.substring(imgcode.indexOf(",")+1);
        String imgPath = user.getId() + "_image";
        Base64.GenerateImage(imgstr, "E:/课程相关文件/Grade 2-2/软工经济学/java projects/我能跑起来/src/main/resources/static/image/users/"+imgPath+".jpg");
        return"haha";
    }

}














