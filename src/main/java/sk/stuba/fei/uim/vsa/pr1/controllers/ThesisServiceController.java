/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sk.stuba.fei.uim.vsa.pr1.controllers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import sk.stuba.fei.uim.vsa.pr1.AbstractThesisService;
import sk.stuba.fei.uim.vsa.pr1.entities.Pedagog;
import sk.stuba.fei.uim.vsa.pr1.entities.Student;
import sk.stuba.fei.uim.vsa.pr1.entities.ZaverecnaPraca;
import sk.stuba.fei.uim.vsa.pr1.enumerations.Status;
import sk.stuba.fei.uim.vsa.pr1.enumerations.Typ;

import javax.persistence.*;

/**
 *
 * @author edu
 * Richard Korosi
 * 111313
 * xkorosi@stuba.sk
 */
public class ThesisServiceController extends AbstractThesisService<Student, Pedagog, ZaverecnaPraca>
{
    public ThesisServiceController() {
        super();
    }

    private void makeOperation(Object object, String operation, EntityManager em) {
        em.getTransaction().begin();
            if (operation.equals("persist")) {
                em.persist(object);
            } else if (operation.equals("remove")) {
                //JPA chcelo nech to mergnem pred deletom
                // asi kovli tomu ze posielan Object aby to osetrilo nejake pripadne chyby
                object = em.merge(object);
                em.remove(object);
            } else if (operation.equals("merge")) {
                em.merge(object);
            }
        em.getTransaction().commit();
    }

    @Override
    public Student createStudent(Long aisId, String name, String email) {
        EntityManager em = this.emf.createEntityManager();
        try {
            Student student = new Student();
            student.setAisId(aisId);
            student.setName(name);
            student.setEmail(email);
            this.makeOperation(student, "persist", em);
            return student;
        } catch (Exception exception) {
            return null;
        } finally {
            em.close();
        }
    }
    @Override
    public Student getStudent(Long id) {
        EntityManager em = this.emf.createEntityManager();
        if(id == null)
            throw new IllegalArgumentException();
        try {
            return em.find(Student.class, id);
        }catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Student updateStudent(Student student) {
        EntityManager em = this.emf.createEntityManager();
        if(student == null || student.getAisId() == null)
            throw new IllegalArgumentException();
        try{
            this.makeOperation(student, "merge", em);
            return student;
        }catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Student> getStudents() {
        EntityManager em = this.emf.createEntityManager();
        try{
            List<Student> studentList;
            TypedQuery<Student> sQuery = em.createQuery("SELECT s FROM Student s", Student.class);
            studentList = sQuery.getResultList();
            return studentList;
        } catch (Exception e){
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    @Override
    public Student deleteStudent(Long id) {
        EntityManager em = this.emf.createEntityManager();
        if(id == null)
            throw new IllegalArgumentException();
        try{
            Student student = this.getStudent(id);
            ZaverecnaPraca zP = this.getThesisByStudent(id);
            //Ak metody nenajdu, nehodia exception tu, ale u seba ju aj odchytia
            // preto musim tu kontrolovat ci nevratili null
            if (student == null || zP == null)
                return null;
            zP.setStudent(null);
            if(zP.getStatus().equals(Status.TAKEN))
                zP.setStatus(Status.FREE);
            if(this.updateThesis(zP) == null)
                //Ak nastala chyba pri update (vynimku odchyti updateThesis cize tu sa len vrati null)
                return null;
            this.makeOperation(student, "remove", em);
            return student;
        }catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Pedagog createTeacher(Long aisId, String name, String email, String department) {
        EntityManager em = this.emf.createEntityManager();
        try{
            Pedagog pedagog = new Pedagog();
            pedagog.setAisId(aisId);
            pedagog.setName(name);
            pedagog.setEmail(email);
            pedagog.setDepartment(department);
            pedagog.setInstitute(department);
            this.makeOperation(pedagog, "persist", em);
            return pedagog;
        } catch (Exception exception){
            return null;
        } finally {
            em.close();
        }

    }
    @Override
    public Pedagog getTeacher(Long id) {
        EntityManager em = this.emf.createEntityManager();
        if(id == null)
            throw new IllegalArgumentException();
        try{
            return em.find(Pedagog.class, id);
        } catch (Exception exception){
            return null;
        } finally {
            em.close();
        }

    }

    @Override
    public Pedagog updateTeacher(Pedagog teacher) {
        EntityManager em = this.emf.createEntityManager();
        if(teacher == null || teacher.getAisId() == null)
            throw new IllegalArgumentException();
        try{
            this.makeOperation(teacher, "merge", em);
            return teacher;
        } catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }
    @Override
    public List<Pedagog> getTeachers() {
        EntityManager em = this.emf.createEntityManager();
        try{
            List<Pedagog> pedagogList;
            TypedQuery<Pedagog> sQuery = em.createQuery("SELECT p FROM Pedagog p", Pedagog.class);
            pedagogList = sQuery.getResultList();
            return pedagogList;
        } catch (Exception exception){
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    @Override
    public Pedagog deleteTeacher(Long id) {
        EntityManager em = this.emf.createEntityManager();
        if(id == null)
            throw new IllegalArgumentException();
        try{
            Pedagog pedagog = this.getTeacher(id);
            //Znova, vynimka sa chyti ked tak v getTeacher a nie tu, takze kontrolujem null
            if(pedagog == null)
                return null;
            if(!this.getThesesByTeacher(pedagog.getAisId()).isEmpty()){
                List<ZaverecnaPraca> pedagogsTs = this.getThesesByTeacher(pedagog.getAisId());
                for(ZaverecnaPraca zP : pedagogsTs){
                    if(this.deleteThesis(zP.getThesisId()) == null)
                        //Ak nastala chyba pri deleteThesis (vynimka, ktora bola odchytena ale v deleteThesis)
                        // tak returni null.
                        return null;
                }
            }
            this.makeOperation(pedagog, "remove", em);
            return pedagog;
        }catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public ZaverecnaPraca makeThesisAssignment(Long supervisor, String title, String type, String description) {
        if(supervisor == null)
            throw new IllegalArgumentException();
        EntityManager em = this.emf.createEntityManager();
        try{
            ZaverecnaPraca zP = new ZaverecnaPraca();
            Pedagog pedagog = this.getTeacher(supervisor); // netreba kontroloval ci je null (riadok 240)
            zP.setRegistrationNumber(createUniqueRegistrationNumber());
            zP.setTitle(title);
            zP.setDescription(description);
            zP.setInstitute(pedagog.getInstitute()); //ak sa nenasiel pedagog, tak je to null a teda neviem dat get -> vynimka -> catch -> return null
            zP.setSupervisor(pedagog);
            zP.setStudent(null);
            zP.setPubDate(LocalDate.now());
            zP.setDeadline(LocalDate.now().plusMonths(3));
            zP.setTyp(Typ.valueOf(type));
            zP.setStatus(Status.FREE);
            pedagog.getPedagogsTheses().add(zP);
            this.makeOperation(zP, "persist", em);
            this.makeOperation(pedagog, "merge", em);
            return zP;
        }
        catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    private String createUniqueRegistrationNumber(){
        Random random = new Random();
        int randomEndNumber = random.nextInt(1000);
        String registrationNumber;
        registrationNumber = "FEI-" + System.currentTimeMillis() + "-" + UUID.randomUUID() + "-" + randomEndNumber;
        return registrationNumber;
    }

    @Override
    public ZaverecnaPraca assignThesis(Long thesisId, Long studentId) {
        if (thesisId == null || studentId == null){
            throw new IllegalArgumentException();
        }
        EntityManager em = this.emf.createEntityManager();
        ZaverecnaPraca zP;
        Student student;
        try {
            zP = em.find(ZaverecnaPraca.class, thesisId);
            student = em.find(Student.class, studentId);
        } catch (Exception e){
            em.close();
            return null;
        }
        if(zP == null || student == null)
            return null;
        if (LocalDate.now().isAfter(zP.getDeadline()) || zP.getStatus().equals(Status.TAKEN) || zP.getStatus().equals(Status.TURNEDIN)){
            throw new IllegalStateException();
        }
        try {
            zP.setStudent(student);
            student.setFinalThesis(zP);
            zP.setStatus(Status.TAKEN);
            this.makeOperation(student, "merge", em);
            this.makeOperation(zP, "merge", em);
            return zP;
        } catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public ZaverecnaPraca submitThesis(Long thesisId) {
        if (thesisId == null){
            throw new IllegalArgumentException();
        }
        EntityManager em = this.emf.createEntityManager();
        ZaverecnaPraca zP;
        try {
            zP = em.find(ZaverecnaPraca.class, thesisId);
        } catch (Exception e){
            return null;
        }
        if(zP == null)
            return null;
        if (LocalDate.now().isAfter(zP.getDeadline()) || zP.getStudent() == null || zP.getStatus().equals(Status.FREE) ||  zP.getStatus().equals(Status.TURNEDIN)){
            throw new IllegalStateException();
        }
        try{
            zP.setStatus(Status.TURNEDIN);
            this.makeOperation(zP, "merge", em);
            return zP;
        }catch (Exception e){
            return null;
        } finally {
            em.close();
        }

    }

    @Override
    public ZaverecnaPraca deleteThesis(Long id) {
        if(id == null)
            throw new IllegalArgumentException();
        EntityManager em = this.emf.createEntityManager();
        try{
            ZaverecnaPraca zP = em.find(ZaverecnaPraca.class, id);
            // Na zaklade:
            // Vymazanie záverečnej práce nemá vplyv na ostatné entity.
            // Nakoniec som sa rozhodol, pre zachovanie konzistencie dát, pre vynullovanie referencie pri študentovi
            // a odstránenie z Listu u Pedagóga.
            if(zP.getStudent() != null){ // ak zP ani neexistuje tak vynimka, a teda null
                Student student = em.find(Student.class,(zP.getStudent().getAisId()));
                if(student != null){
                    student.setFinalThesis(null);
                    this.makeOperation(student, "merge", em);
                }
            }
            if(zP.getSupervisor() != null){ // ak zP ani neexistuje tak vynimka, a teda null
                Pedagog pedagog = em.find(Pedagog.class, (zP.getSupervisor().getAisId()));
                if(pedagog != null){
                    pedagog.getPedagogsTheses().remove(zP);
                    this.makeOperation(pedagog, "merge", em);
                }
            }
            this.makeOperation(zP, "merge", em);
            this.makeOperation(zP, "remove", em);
            return zP;
        }catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<ZaverecnaPraca> getTheses() {
        EntityManager em = this.emf.createEntityManager();
        try{
            List<ZaverecnaPraca> zPList;
            TypedQuery<ZaverecnaPraca> sQuery = em.createQuery("SELECT z FROM ZaverecnaPraca z", ZaverecnaPraca.class);
            zPList = sQuery.getResultList();
            return zPList;
        } catch (Exception e){
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<ZaverecnaPraca> getThesesByTeacher(Long teacherId) {
        EntityManager em = this.emf.createEntityManager();
        try{
            List<ZaverecnaPraca> zPList;
            TypedQuery<ZaverecnaPraca> sQuery = em.createQuery("SELECT z FROM ZaverecnaPraca z WHERE z.supervisor.aisId = :teacherId", ZaverecnaPraca.class);
            sQuery.setParameter("teacherId", teacherId);
            zPList = sQuery.getResultList();
            return zPList;
        } catch (Exception e){
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }

    @Override
    public ZaverecnaPraca getThesisByStudent(Long studentId) {
        EntityManager em = this.emf.createEntityManager();
        try{
            ZaverecnaPraca zP;
            TypedQuery<ZaverecnaPraca> sQuery = em.createQuery("SELECT z FROM ZaverecnaPraca z WHERE z.student.aisId = :studentId", ZaverecnaPraca.class);
            sQuery.setParameter("studentId", studentId);
            zP = sQuery.getSingleResult();
            return zP;
        }catch (Exception exception){
            return null;
        }finally {
            em.close();
        }
    }

    @Override
    public ZaverecnaPraca getThesis(Long id) {
        if(id == null){
            throw new IllegalArgumentException();
        }
        EntityManager em = this.emf.createEntityManager();
        try {
            return em.find(ZaverecnaPraca.class, id);
        } catch (Exception e){
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public ZaverecnaPraca updateThesis(ZaverecnaPraca thesis) {
        if(thesis == null || thesis.getThesisId() == null)
            throw new IllegalArgumentException();
        EntityManager em = this.emf.createEntityManager();
        try{
            this.makeOperation(thesis, "merge", em);
            return thesis;
        }catch (Exception exception){
            return null;
        } finally {
            em.close();
        }
    }
}
