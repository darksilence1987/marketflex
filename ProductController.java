import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@PostMapping("/products/create")
public String createProduct(@RequestParam("image") MultipartFile image, Model model) {
    // Process the uploaded image here
    return "redirect:/products";
}
