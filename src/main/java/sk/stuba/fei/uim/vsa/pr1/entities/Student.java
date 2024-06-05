/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stuba.fei.uim.vsa.pr1.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author edu
 */
@Entity
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aisId;
    private String name;
    @Column(unique = true)
    private String email;
    private Long year;
    private Long semester;
    private String program;
    @OneToOne(mappedBy = "student")
    private ZaverecnaPraca finalThesis;

    public Long getAisId() {
        return aisId;
    }

    public void setAisId(Long aisId) {
        this.aisId = aisId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Long getSemester() {
        return semester;
    }

    public void setSemester(Long semester) {
        this.semester = semester;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public ZaverecnaPraca getFinalThesis() {
        return finalThesis;
    }

    public void setFinalThesis(ZaverecnaPraca finalThesis) {
        this.finalThesis = finalThesis;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.aisId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Student other = (Student) obj;
        if (!Objects.equals(this.aisId, other.aisId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Student{" + "aisId=" + aisId + ", name=" + name + ", email=" + email + ", year=" + year + ", semester=" + semester + ", program=" + program + ", finalThesis=" + finalThesis + '}';
    }

    
    
}
