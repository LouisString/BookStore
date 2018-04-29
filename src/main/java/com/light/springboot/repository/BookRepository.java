package com.light.springboot.repository;

import com.light.springboot.entity.Book;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;
import java.util.Optional;

/**
 * Created by wan on 2017/1/17.
 */
@Repository
@Table(name="books4")
@Qualifier("bookRepository")
public interface BookRepository extends CrudRepository<Book, Long > {
    @Nullable
    Optional<Book> findById(Long id);

    @Nullable
    List<Book> findByTitle(String title);

    @Nullable
    List<Book> findByAuthor(String author);

    @Nullable
    List<Book> findByLanguage(String language);

    @Nullable
    List<Book> findByPublished(String published);

    @Nullable
    List<Book> findBySales(String sales);

    @Nullable
    List<Book> findByPrice(Long price);


}