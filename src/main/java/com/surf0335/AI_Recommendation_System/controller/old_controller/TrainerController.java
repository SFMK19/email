package com.surf0335.AI_Recommendation_System.controller.old_controller;

import com.surf0335.AI_Recommendation_System.model.Classfortrain;
import com.surf0335.AI_Recommendation_System.model.Comment;
import com.surf0335.AI_Recommendation_System.model.Course;
import com.surf0335.AI_Recommendation_System.model.Trainer;
import com.surf0335.AI_Recommendation_System.model.User;
import com.surf0335.AI_Recommendation_System.repository.ClassRepo;
import com.surf0335.AI_Recommendation_System.repository.CourseRepository;
import com.surf0335.AI_Recommendation_System.services.CommentService;
import com.surf0335.AI_Recommendation_System.services.StoreImage;
import com.surf0335.AI_Recommendation_System.services.TrainerService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trainers")
public class TrainerController {

    private TrainerService trainerService;
    private CommentService commentService;

    @Autowired
    private StoreImage storeImage;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ClassRepo classRepo;
    public TrainerController(CommentService commentService, TrainerService trainerService) {
        this.commentService = commentService;
        this.trainerService = trainerService;
    }

    @GetMapping("/list")
    public String listTrainers(Model theModel) {
        List<Trainer> trainers = trainerService.listTrainers();

        // add to the spring model
        theModel.addAttribute("trainers", trainers);

        return "trainers/list-trainers";
    }
    
    @GetMapping("/userlist")
    public String Trainersforuser(Model theModel) {
        List<Trainer> trainers = trainerService.listTrainers();

        // add to the spring model
        theModel.addAttribute("trainers", trainers);

        return "trainers/trainerforuser";
    }

    @PostMapping("/save")
    public String createOrUpdateTrainer(@RequestParam(name ="file",required = false) MultipartFile file, @ModelAttribute("trainer") Trainer trainer) {
        
        if (file != null && !file.isEmpty()) {
            
                String fileName = file.getOriginalFilename();
                String url = storeImage.store(file); // storeImage should have a method saveImage(MultipartFile file)
                
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/")
                        .path(fileName)
                        .toUriString();
        
                trainer.setImageUrl(fileDownloadUri);
            }
         else if (trainer.getId() != null) {
            Trainer existingTrainer = trainerService.getTrainerById(trainer.getId()).orElse(null);
            if (existingTrainer != null && existingTrainer.getImageUrl() != null) {
                trainer.setImageUrl(existingTrainer.getImageUrl());
            } 
        }    
        Trainer createdTrainer = trainerService.createOrUpdateTrainer(trainer);
        return "redirect:/trainers/list";
    }
        
    @GetMapping("/delete/{trainerId}")
    public String deleteTrainer(@PathVariable Long trainerId, RedirectAttributes redirectAttributes) {
        try {
            trainerService.deleteTrainer(trainerId);
            redirectAttributes.addFlashAttribute("successMessage", "Trainer has been successfully deleted.");
        } catch (TrainerNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Trainer could not be found.");
        }
        return "redirect:/trainers/list";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        List<Course> courses = courseRepository.findAll();
        List<Classfortrain> level= classRepo.findAll();
        // create model attribute to bind fo data
        Trainer theTrainer = new Trainer();
        theModel.addAttribute("trainer", theTrainer);
        theModel.addAttribute("courses", courses);
        theModel.addAttribute("level", level);

        return "trainers/trainer-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("trainerId") Long theId,
                                    Model theModel) {
        // Get the trainer from the service
        Optional<Trainer> theTrainer = trainerService.getTrainerById(theId);
        List<Course> courses = courseRepository.findAll();
        List<Classfortrain> level= classRepo.findAll();
        // Check if the trainer exists
        if (theTrainer.isPresent()) {
            // Set trainer as a model attribute to pre-populate the form
            theModel.addAttribute("trainer", theTrainer.get());
            theModel.addAttribute("courses", courses);
            theModel.addAttribute("level", level);
            // Send over to our form
            return "trainers/trainer-form-update";
        } else {
            // If trainer with the given ID does not exist, handle the error gracefully
            // For example, you can redirect to an error page or display a message to the user
            // Here, we'll redirect to the trainers list page
            return "redirect:/trainers/list";
        }
    }

    @GetMapping("/showFormForReadonly")
    public String showFormForReadonly(@RequestParam("trainerId") Long theId,
                                    Model theModel) {

        // Get the trainer from the service
        Optional<Trainer> theTrainer = trainerService.getTrainerById(theId);
        List<Classfortrain> level= classRepo.findAll();
        // Check if the trainer exists
        // Check if the trainer exists
        if (theTrainer.isPresent()) {
            // Set trainer as a model attribute to pre-populate the form
            theModel.addAttribute("trainer", theTrainer.get());
            theModel.addAttribute("level", level);
            // Send over to our form
            return "trainers/trainer-form-readonly";
        } else {
            // If trainer with the given ID does not exist, handle the error gracefully
            // For example, you can redirect to an error page or display a message to the user
            // Here, we'll redirect to the trainers list page
            return "redirect:/trainers/list";
        }
    }
    @GetMapping("/showFormForReadonlyforuser")
    public String showFormForReadonlyforuser(@RequestParam("trainerId") Long theId,
                                    Model theModel,HttpSession session) {

        // Get the trainer from the service
        Optional<Trainer> theTrainer = trainerService.getTrainerById(theId);
        List<Classfortrain> level= classRepo.findAll();
        User user = (User) session.getAttribute("user");

        // Check if the trainer exists
        if (theTrainer.isPresent()) {
            
            // Set trainer as a model attribute to pre-populate the form
            theModel.addAttribute("trainer", theTrainer.get());
            theModel.addAttribute("level", level);
            theModel.addAttribute("user", user);
            // Send over to our form
            return "trainers/trainer-form-readonly-user";
        } else {
            // If trainer with the given ID does not exist, handle the error gracefully
            // For example, you can redirect to an error page or display a message to the user
            // Here, we'll redirect to the trainers list page
            return "redirect:/trainers/userlist";
        }
    }

    @GetMapping("/{trainerId}/comments/new")
    public String showAddCommentForm(@PathVariable Long trainerId, Model model) {
        if (!model.containsAttribute("comment")) {
            model.addAttribute("comment", new Comment());  // Add this line if missing
        }

        Optional<Trainer> trainer = trainerService.getTrainerById(trainerId);
        if (trainer.get() != null){
            model.addAttribute("trainerId", trainerId);
            model.addAttribute("trainerName", trainer.get().getName());
        } else {model.addAttribute("message", "Not able to find the trainer");}

        return "trainers/addComment-form.html";  // This should be the path to your form JSP file.
    }

    @PostMapping("/{trainerId}/comments/save")
    public String saveComment(@PathVariable Long trainerId, @ModelAttribute("comment") Comment comment, BindingResult result, Model model) {
        // Fetch the associated Trainer
        Optional<Trainer> trainer = trainerService.getTrainerById(trainerId); // Ensure this method exists and fetches the trainer correctly
        if (trainer.get() == null) {
            model.addAttribute("message", "Trainer not found");
            return "trainers/addComment-form"; // Still display the form but with an error message
        } else {
            model.addAttribute("trainer", trainer.get());
            // Set the trainer to the comment
            comment.setTrainer(trainer.get());
        }

        // Save the comment
        commentService.saveComment(comment);

        // Instead of redirecting, add a success message to the model and return the same form
        model.addAttribute("message", "Comment successfully added!");
        return "trainers/successcomment"; // The view should be able to display this message
    }

    @GetMapping("/{trainerId}/comments")
    public String viewTrainerComments(@PathVariable Long trainerId, Model model) {
        List<Comment> comments = commentService.getCommentsByTrainerId(trainerId);
        model.addAttribute("comments", comments);

        Optional<Trainer> trainer = trainerService.getTrainerById(trainerId);
        if (trainer.get() != null){
            model.addAttribute("trainerId", trainerId);
            model.addAttribute("trainerName", trainer.get().getName());
        }

        return "trainers/viewComments"; // Thymeleaf template to display comments
    }
    @GetMapping("/{trainerId}/usercomments")
    public String viewTrainerCommentsforuser(@PathVariable Long trainerId, Model model) {
        List<Comment> comments = commentService.getCommentsByTrainerId(trainerId);
        model.addAttribute("comments", comments);

        Optional<Trainer> trainer = trainerService.getTrainerById(trainerId);
        if (trainer.get() != null){
            model.addAttribute("trainerId", trainerId);
            model.addAttribute("trainerName", trainer.get().getName());
        }

        return "trainers/viewCommentsforuser"; // Thymeleaf template to display comments
    }
   
}
