/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stuba.fei.uim.vsa.pr1.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author edu
 */
@Entity
public class Pedagog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long aisId;
    private String name;
    @Column(unique = true)
    private String email;
    private String institute;
    private String department;
    @OneToMany(mappedBy="supervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZaverecnaPraca> pedagogsTheses;

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

    public String getInstitute() {
        return institute;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<ZaverecnaPraca> getPedagogsTheses() {
        return pedagogsTheses;
    }

    public void setPedagogsTheses(List<ZaverecnaPraca> pedagogsTheses) {
        this.pedagogsTheses = pedagogsTheses;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.aisId);
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
        final Pedagog other = (Pedagog) obj;
        if (!Objects.equals(this.aisId, other.aisId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Pedagog{" + "aisId=" + aisId + ", name=" + name + ", email=" + email + ", institute=" + institute + ", department=" + department + ", pedaogsTheses=" + pedagogsTheses + '}';
    }
    

    
    
}
