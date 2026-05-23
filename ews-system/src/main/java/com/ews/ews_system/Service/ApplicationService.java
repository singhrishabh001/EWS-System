package com.ews.ews_system.Service;

import com.ews.ews_system.Model.EWSApplication;
import com.ews.ews_system.Model.User;
import com.ews.ews_system.Repository.ApplicationRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void saveApplication(EWSApplication application){

        application.setStatus("Pending");
        applicationRepository.save(application);
    }
//    public List<EWSApplication> getAllApplication(){
//        return applicationRepository.findAll();
//    }

    public List<EWSApplication> getPendingApplication(){
        return applicationRepository.findByStatus("Pending");
    }
    public EWSApplication getApplicationById(Long id){
        return applicationRepository.findById(id).orElse(null);
    }

    public void approveApplication(Long id) throws Exception{
        EWSApplication application = applicationRepository.findById(id).orElse(null);
        application.setStatus("Approved");
        application.setDate(LocalDate.now().toString());
        generatePdf(application);
        applicationRepository.save(application);
    }

    public void rejectApplication(Long id , String reason){
        EWSApplication application = applicationRepository.findById(id).orElse(null);
        application.setStatus("Rejected");
        application.setDate(LocalDate.now().toString());
        application.setRejectReason(reason);
        applicationRepository.save(application);
    }

    public List<EWSApplication> getApplicationByUser(User user){
        return applicationRepository.findByUser(user);
    }

//    PDF Generation, convert HTML into PDF

    public void generatePdf(EWSApplication app) throws Exception{
        Context context = new Context();
        context.setVariable("app",app);
        String fileName = "ews_" + app.getId() + ".pdf";
        String pdff =System.getProperty("user.dir") + "/uploads/certificates/";
        File pdfFolder = new File(pdff);
        if(!pdfFolder.exists()){
            pdfFolder.mkdirs();
        }
        String path = pdff + fileName;
        String photoPath =System.getProperty("user.dir") + "/uploads/photos/" + app.getPhoto();
        byte[] imageBytes = Files.readAllBytes(Paths.get(photoPath));
        String  base64Image = Base64.getEncoder().encodeToString(imageBytes);
        context.setVariable("photoPath",base64Image);


        OutputStream os = new FileOutputStream(path);
        String html = templateEngine.process("certificate",context);
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, "file:///");
        builder.toStream(os);
        builder.run();
        os.close();
        app.setCertificateFile(fileName);
        applicationRepository.save(app);

    }
}
