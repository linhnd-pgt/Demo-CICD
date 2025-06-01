package com.thanhhuong.rookbooks.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@Tag(name = "Get revenue controller", description = "Operations related to book's management")
public interface IBookResource {

    @Operation(summary = "Get book by id", description = "Get book's information based on the given id in path variable")
    @GetMapping("/books/get/{bookId}")
    ResponseEntity<?> getBookById(@PathVariable("bookId") Long bookId);

    @Operation(summary = "Get paginated book list by  category id and sorting key", description = "Get paginated book list's information by  category id and sorting key")
    @GetMapping("/books/get")
    ResponseEntity<?> getBookListPaginatedAndSorted(@RequestParam("sortBy") String sortBy,
                                                    @RequestParam("categoryId") Long categoryId,
                                                    @RequestParam(name = "page", defaultValue = "1") int page,
                                                    @RequestParam("size") int size);

}
