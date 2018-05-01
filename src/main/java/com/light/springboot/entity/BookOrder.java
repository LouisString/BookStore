package com.light.springboot.entity;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity

public class BookOrder {

    @EmbeddedId
    private OrderPrimaryKey orderPK;

    private Long count;

    private String addr;

    private Long price;

    private String deliver_status;

    private Date date;

    public BookOrder(){

    }

    public BookOrder(OrderPrimaryKey orderPK, Long count, String addr, Long price,
                     String deliver_status, Date date) {
        this.orderPK = orderPK;
        this.count = count;
        this.addr = addr;
        this.price = price;
        this.deliver_status = deliver_status;
        this.date = date;
    }

    public OrderPrimaryKey getOrderPK() {
        return orderPK;
    }

    public void setOrderPK(OrderPrimaryKey orderPK) {
        this.orderPK = orderPK;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
    public String getAddr() {
        return addr;
    }


    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Long getPrice() {
        return price;
    }


    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDeliver_status() {
        return deliver_status;
    }

    public void setDeliver_status(String deliver_status) {
        this.deliver_status = deliver_status;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return orderPK.toString() + "; "
                + count + "; " +addr +"; " + price + "; " +deliver_status + "; " +date.toString();
    }
}
