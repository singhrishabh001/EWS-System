package com.ews.ews_system.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EWSApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String fatherName;
    private  String aadhar;
    private  String income;
    private String address;
    private  String dob;
    private  String gender;
    private  String photo;
    private String incomeCertificate;
    private String status;
    private String rejectReason;
    private String certificateFile;
    private String mobile_no;
    private String date;
    @ManyToOne
    private User user;
}
