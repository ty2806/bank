package com.example.bank.controller;

import com.example.bank.payload.AccountDto;
import com.example.bank.payload.TransactionDto;
import com.example.bank.payload.UserDto;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import com.example.bank.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @PostMapping("/users/{userId}/accounts")
    public ResponseEntity<AccountDto> createAccount(@PathVariable(name = "userId") Long userId,
                                                    @Valid @RequestBody AccountDto accountDto,
                                                    HttpServletResponse response) {
        AccountDto createdAccount = accountService.createAccount(userId, accountDto);
        Cookie cookie = new Cookie("accountCreated", "true");
        cookie.setMaxAge(24 * 60 * 60); // 24 hours
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/accounts")
    public List<AccountDto> getAccountsByUserId(@PathVariable(name = "userId") Long userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "id") Long id,
                                                     @CookieValue(name = "accountCreated", defaultValue = "false") String accountCreated) {
        if ("true".equals(accountCreated)) {
            System.out.println("Account creation was confirmed via cookie.");
        } else {
            System.out.println("No recent account creation cookie found.");
        }

        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(name = "id") Long id,
                                                    @Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDto));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable(name = "id") Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>("account deleted.", HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/accounts/{id}/statement", produces = "application/pdf")
    public void getStatementByDate(@PathVariable(name = "userId") Long userId,
                                   @PathVariable(name = "id") Long id,
                                   @RequestParam(name = "startDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
                                   @RequestParam(name = "endDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
                                   HttpServletResponse response) {

        List<TransactionDto> transactionDtos = transactionService.getTransactionsByAccountIdBetweenDate(id, startDate.get(), endDate.get());
        AccountDto accountDto = accountService.getAccountById(id);
        UserDto userDto = userService.getUserById(userId);

        try (PDDocument document = new PDDocument()) {
            // Create a new blank page and add it to the document
            PDPage page = new PDPage();
            document.addPage(page);
            log.info("created a pdf file for statement response");
            // Create a new content stream which will "draw" the content to the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Begin the Content stream
                contentStream.beginText();
                // Set font and font size
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                // Set line spacing
                contentStream.setLeading(14.5f);
                // Move to the start position
                contentStream.newLineAtOffset(100, 700);
                // Write text
                contentStream.showText(userDto.toString());
                contentStream.newLine();
                contentStream.showText(accountDto.toString());
                contentStream.newLine();
                for (TransactionDto tx : transactionDtos) {
                    contentStream.showText(tx.toString());
                    contentStream.newLine();
                }
                // End the content stream
                contentStream.endText();
                // Make sure to close the content stream
                contentStream.close();
            }

            // Save the final pdf document to a byte array
            response.setContentType("application/pdf");
            document.save(response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            // Handle exceptions properly
            e.printStackTrace();
        }
    }
}
