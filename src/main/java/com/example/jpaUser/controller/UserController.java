package com.example.jpaUser.controller;

import com.example.jpaUser.DTO.passwordDTO;
import com.example.jpaUser.model.User;
import com.example.jpaUser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/v2/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    //Lấy danh sách users
    @GetMapping()
    public String getUsers(Model model) {
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user-management";
    }
    //Tìm kiếm user theo tên
    @GetMapping("/search")
    public String searchUser(@RequestParam("name") String name, Model model) {
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);
        model.addAttribute("users", users);
        return "user-management";
    }
    //Lấy chi tiết user theo id
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //Lấy địa chỉ
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        List<User> users = userRepository.findAll();
        List<String> addresses = users.stream().map(User::getAddress).collect(Collectors.toList());
        model.addAttribute("addresses", addresses);
        return "create-user";
    }
    //Tạo mới user
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
    //Cập nhật thông tin user
    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") Integer id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(updatedUser.getName());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            userRepository.save(user);
            return "redirect:/" + id;
        } else {
            return "error";
        }
    }
    //Xóa user
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Integer id) {
        userRepository.deleteById(id);
        return "redirect:/";
    }
    // Thay đổi ảnh avatar
    @PutMapping("/{id}/update-avatar")
    public ResponseEntity<User> updateAvatar(@PathVariable("id") Integer id, @RequestBody User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setAvatar(updatedUser.getAvatar());
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    //Thay đổi mật khẩu
    @PutMapping("/{id}/update-password")
    public String updatePassword(@PathVariable("id") Integer id, @RequestBody passwordDTO changePasswordRequest) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String oldPassword = changePasswordRequest.getOldPassword();
            String newPassword = changePasswordRequest.getNewPassword();

            // Kiểm tra mật khẩu cũ có khớp với mật khẩu hiện tại không
            if (user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                User savedUser = userRepository.save(user);
                return "redirect:/" + id;
            } else {
                return "error";
            }
        } else {
            return "error";
        }
    }
    //Quên mật khẩu
    @PostMapping("/{id}/forgot-password")
    public String forgotPassword(@PathVariable("id") Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // Tạo một mật khẩu mới ngẫu nhiên
            String newPassword = generateRandomPassword();
            // Cập nhật mật khẩu mới cho người dùng
            user.setPassword(newPassword);
            User savedUser = userRepository.save(user);

            return "user-management";
        } else {
            return "error";
        }
    }
    private String generateRandomPassword() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        int PASSWORD_LENGTH = 8;
            StringBuilder password = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < PASSWORD_LENGTH; i++) {
                int randomIndex = random.nextInt(CHARACTERS.length());
                password.append(CHARACTERS.charAt(randomIndex));
            }
            return password.toString();
    }
}
