package ru.konovalov.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.konovalov.chat.model.Message;
import ru.konovalov.chat.model.User;
import ru.konovalov.chat.repository.MessageRepository;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepository messageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting(Model model) {
//        model.addAttribute("name", name);
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Message> messages;

        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTag(filter);
        } else {
            messages = messageRepository.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @RequestParam String text,
            @RequestParam String tag, Model model,
            @RequestParam("file") MultipartFile file) {
        Message message = new Message(text, tag, user);

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()){
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()){
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            try {
                file.transferTo(new File(uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            message.setFilename(resultFileName);
        }

        messageRepository.save(message);
        Iterable<Message> messages = messageRepository.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }
}
