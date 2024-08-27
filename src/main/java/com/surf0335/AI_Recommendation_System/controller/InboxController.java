package com.surf0335.AI_Recommendation_System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InboxController {
    @GetMapping("/inbox")
    public String getinboxPage(Model m){
        return "Inbox";
    }

/*    @Autowired
    private EmailService emailService; // Assume you have an EmailService to fetch emails
    @Autowired
    private TextMiningService textMiningService; // Assume you have a TextMiningService to process emails

    @GetMapping("/inbox")
    public String getInbox(Model model) {
        List<Email> emails = emailService.getAllEmails();
        model.addAttribute("emails", emails);
        return "inbox";
    }

    @PostMapping("/extract")
    public String extractInformation(@RequestParam("emailId") Long emailId, Model model) {
        Email selectedEmail = emailService.getEmailById(emailId);
        ExtractedInfo extractedInfo = textMiningService.extractInformation(selectedEmail.getContent());

        // Assuming you want to create a calendar notification
        CalendarNotification notification = new CalendarNotification();
        notification.setTitle("Meeting with " + extractedInfo.getStudentName());
        notification.setDate(extractedInfo.getDate());
        notification.setDescription("Meeting with " + extractedInfo.getStudentName() + " from " + extractedInfo.getSchoolName());

        // Add the notification to the model
        model.addAttribute("notification", notification);

        // Reload the inbox view with the selected email and extracted info
        model.addAttribute("selectedEmail", selectedEmail);
        List<Email> emails = emailService.getAllEmails();
        model.addAttribute("emails", emails);

        return "inbox";
    }*/
}
