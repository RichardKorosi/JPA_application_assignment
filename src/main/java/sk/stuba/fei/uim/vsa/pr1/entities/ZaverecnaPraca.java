/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stuba.fei.uim.vsa.pr1.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.*;

import sk.stuba.fei.uim.vsa.pr1.enumerations.Status;
import sk.stuba.fei.uim.vsa.pr1.enumerations.Typ;

/**
 *
 * @author edu
 */
@Entity
public class ZaverecnaPraca implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long thesisId;
    @Column(unique = true)
    private String registrationNumber;
    private String title;
    private String description;
    private String institute;
    @ManyToOne
    @JoinColumn(name = "pedagog_id", nullable = false)
    private Pedagog supervisor;
    @OneToOne
    @JoinColumn(name = "student_id")
    private Student student;
    private LocalDate pubDate;
    private LocalDate deadline;
    @Enumerated(EnumType.STRING)
    private Typ typ;
    @Enumerated(EnumType.STRING)
    private Status status;


    public Long getThesisId() {
        return thesisId;
    }

    public void setThesisId(Long thesisId) {
        this.thesisId = thesisId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public Pedagog getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Pedagog supervisor) {
        this.supervisor = supervisor;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getPubDate() {
        return pubDate;
    }

    public void setPubDate(LocalDate pubDate) {
        this.pubDate = pubDate;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public Typ getTyp() {
        return typ;
    }

    public void setTyp(Typ typ) {
        this.typ = typ;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "ZaverecnaPraca{id: "+ thesisId + " registrationNumber=" + registrationNumber + ", title=" + title + ", description=" + description + ", institute=" + institute +  ", pubDate=" + pubDate + ", deadline=" + deadline + ", typ=" + typ + ", status=" + status + '}';
    }
    
    
    
    
    
    
    
}
