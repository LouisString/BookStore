package com.light.springboot.controller;

import com.light.springboot.entity.Book;
import com.light.springboot.entity.BookOrder;
import com.light.springboot.entity.OrderPrimaryKey;
import com.light.springboot.entity.User;
import com.light.springboot.repository.BookOrderRepository;
import com.light.springboot.repository.BookRepository;
import com.light.springboot.repository.UserRepository;
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
        mav.addObject("message", null);
        mav.addObject("orders", null);
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
        Date date = new Date();
        String deliver_status = "Not Delivered";
        String addr = "Dreamland";
        BookOrder bookOrder;
        Map<String, List<Long>> binfo = new HashMap<>();
        Set<BookOrder> userOrders = user.getOrders();

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
                bookOrder = new BookOrder(PK, count, addr, btprice, deliver_status, date);
                orderRepository.save(bookOrder);
                userOrders.add(bookOrder);
            }
        }
        user.setOrders(userOrders);

        for(BookOrder bOrder: user.getOrders()){
            System.out.println(bOrder.toString());
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
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();

        System.out.println(imgcode);

        String imgstr = imgcode.substring(imgcode.indexOf(",")+1);
        String imgPath = user.getId() + "_image";
        Base64.GenerateImage(imgstr, "E:/课程相关文件/Grade 2-2/软工经济学/java projects/我能跑起来/src/main/resources/static/image/users/"+imgPath+".jpg");
        return"haha";
    }

    @RequestMapping("/user/orders")
    @ResponseBody
    public ModelAndView getOrder(){
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();

        Set<BookOrder> orders = user.getOrders();

        System.out.println("orders个数: "+orders.size() );

        if (orders.size() == 0){
            ModelAndView mav = new ModelAndView("user");
            mav.addObject("user", null);
            mav.addObject("message", "Ops! You have no orders");
            mav.addObject("orders", null);
            return mav;
        }

        Long oid, bid, price,bprice, count, booklistprice;
        String dstate, title, date;
        Optional<Book> b;
        List<Long> countAndPrice;
        Book book;

        List<String> dateAndDstate;
        Map<Long, List<String>> oidAndDstate;
        Map<String, List<Long>> bookcAndP;
        Map<Map<String, List<Long>>, Long> booklist;

        Map<Map<Long, List<String>>, Map<Map<String, List<Long>>, Long>> modalOrders = new TreeMap<Map<Long, List<String>>, Map<Map<String, List<Long>>, Long>>(new Comparator<Map<Long, List<String>>>(){

            @Override
            public int compare(Map<Long, List<String>> o1,Map<Long, List<String>> o2) {
                int result = 0;
                for (Long oid1:o1.keySet()){
                    for(Long oid2:o2.keySet()){
                        if (oid1 > oid2){
                            result =  1;
                            break;
                        }
                        else if (oid1.equals(oid2)) {
                            result = 0;
                            break;
                        }
                        else {
                            result = -1;
                            break;
                        }
                    }
                }
                return result;
            }
        });

        /*同一oid的书单是绑定在一起的，作为一个整体订单*/
        for (BookOrder order:orders){
            oid = order.getOrderPK().getOid();
            dstate = order.getDeliver_status();
            date = order.getDate().toString();

            dateAndDstate = new LinkedList<>();
            dateAndDstate.add(dstate);
            dateAndDstate.add(date);

            oidAndDstate = new HashMap<>();
            oidAndDstate.put(oid, dateAndDstate);
            /*已经有该订单中部分书目，再新增该书*/
            if (modalOrders.containsKey(oidAndDstate)){
                booklist = modalOrders.get(oidAndDstate);
                bid = order.getOrderPK().getBid();
                b = bookRepository.findById(bid);
                if (b.isPresent()){
                    book = b.get();
                    title = book.getTitle();
                    price = book.getPrice();
                    count = order.getCount();
                    bprice = count*price;
                    countAndPrice = new LinkedList<>();
                    countAndPrice.add(count);
                    countAndPrice.add(bprice);
                    /*booklist应当只有一项，该项的key是个map，记录了所有书title和（count， price）list，该项value是总价*/
                    for(Map<String, List<Long>> bcap: booklist.keySet()){
                        booklistprice = booklist.get(bcap);
                        booklistprice += bprice;
                        booklist.remove(bcap);
                        bcap.put(title, countAndPrice);
                        booklist.put(bcap, booklistprice);
                        System.out.println("-------booklist更新："+"oid: "+oid+", title: "+title+", count: "+countAndPrice.get(0)+", price: "+countAndPrice.get(1)+", 总价："+booklist.get(bcap));
                    }
                    /*更新， 该数不存在也不会出错*/
                    modalOrders.put(oidAndDstate, booklist);
                }

            }
            /*该订单中没有书，这是第一本书, 新建书列表*/
            else{
                booklist = new HashMap<>();
                bookcAndP = new HashMap<>();
                booklistprice = Long.valueOf(0);
                bid = order.getOrderPK().getBid();
                b = bookRepository.findById(bid);
                if (b.isPresent()) {
                    book = b.get();
                    title = book.getTitle();
                    price = book.getPrice();
                    count = order.getCount();
                    bprice = count * price;
                    countAndPrice = new LinkedList<>();
                    countAndPrice.add(count);
                    countAndPrice.add(bprice);

                    bookcAndP.put(title, countAndPrice);
                    booklistprice = bprice;
                    System.out.println("-------booklist新建："+"oid: "+oid+", title: "+title+", count: "+countAndPrice.get(0)+", price: "+countAndPrice.get(1)+", 总价："+booklistprice);
                }
                /*防止该书不存在了*/
                booklist.put(bookcAndP, booklistprice);

                modalOrders.put(oidAndDstate, booklist);
            }

        }

        ModelAndView mav = new ModelAndView("user");
        mav.addObject("user", null);
        mav.addObject("message", null);
        mav.addObject("orders", modalOrders);
        return mav;

    }

}














