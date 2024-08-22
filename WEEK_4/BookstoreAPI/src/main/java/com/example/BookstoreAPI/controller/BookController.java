package com.example.BookstoreAPI.controller;

import com.example.BookstoreAPI.model.Book;
import com.example.BookstoreAPI.exception.ResourceNotFoundException;
import com.example.BookstoreAPI.dto.BookDTO;
import com.example.BookstoreAPI.mapper.BookMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    private List<Book> books = new ArrayList<>();
    private final BookMapper bookMapper = BookMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            bookDTOs.add(bookMapper.bookToBookDTO(book));
        }
        return ResponseEntity.ok(bookDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable int id) {
        Book book = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));
        
        BookDTO bookDTO = bookMapper.bookToBookDTO(book);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "This is a custom header value");
        return ResponseEntity.ok().headers(headers).body(bookDTO);
    }

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO) {
        Book book = bookMapper.bookDTOToBook(bookDTO);
        books.add(book);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/books/" + book.getId());
        headers.add("Custom-Header", "Book Created Successfully");

        BookDTO createdBookDTO = bookMapper.bookToBookDTO(book);
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(createdBookDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        Book book = books.stream()
                .filter(b -> b.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + id));

        books.remove(book);
        return ResponseEntity.noContent().build();
    }
}
