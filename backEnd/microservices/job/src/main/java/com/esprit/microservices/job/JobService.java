package com.esprit.microservices.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Optional<Job> getJobById(int id) {
        return jobRepository.findById(id);
    }

    public Page<Job> getJobByNom(String name, Pageable pageable) {
        return jobRepository.jobByNom("%" + name + "%", pageable);
    }

    public Job addJob(Job job) {
        return jobRepository.save(job);
    }

    public Job updateJob(int id, Job newJob) {
        if (jobRepository.findById(id).isPresent()) {
            Job existing = jobRepository.findById(id).get();
            existing.setNom(newJob.getNom());
            existing.setEtat(newJob.getEtat());
            return jobRepository.save(existing);
        }
        return null;
    }

    /**
     * Modification de l'état du poste : "oui" = disponible, "non" = occupé.
     */
    public Job updateEtat(int id, String etat) {
        if (jobRepository.findById(id).isPresent()) {
            Job existing = jobRepository.findById(id).get();
            existing.setEtat(etat);
            return jobRepository.save(existing);
        }
        return null;
    }

    public String deleteJob(int id) {
        if (jobRepository.findById(id).isPresent()) {
            jobRepository.deleteById(id);
            return "job supprimé";
        }
        return "job non supprimé";
    }
}
