package com.esprit.microservices.job;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mic2/job")
@Tag(name = "Job", description = "Job microservice API")
public class JobRESTApi {

    private static final String TITLE = "Hello, i'm the Job Micro-Service";

    @Autowired
    private JobService jobService;

    @GetMapping("/hello")
    @Operation(summary = "Health check", description = "Returns a message indicating the job microservice is running")
    public String sayHello() {
        return "hello i am job microservice im working fine";
    }

    @GetMapping(value = "/jobs", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get all jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
    }

    @GetMapping(value = "/jobs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable int id) {
        return jobService.getJobById(id)
                .map(job -> new ResponseEntity<>(job, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/jobs/search/jobByNom", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get job by name")
    public ResponseEntity<Page<Job>> getJobByNom(@RequestParam String name,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        Page<Job> result = jobService.getJobByNom(name, PageRequest.of(page, size));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping(value = "/jobs/{id}/etat", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update job state", description = "Etat = oui (poste disponible), etat = non (poste occupé)")
    public ResponseEntity<Job> updateEtat(@PathVariable int id, @RequestBody EtatRequest etatRequest) {
        Job updated = jobService.updateEtat(id, etatRequest.getEtat());
        return updated != null
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/jobs", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a job")
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        return new ResponseEntity<>(jobService.addJob(job), HttpStatus.CREATED);
    }

    @PutMapping(value = "/jobs/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a job")
    public ResponseEntity<Job> updateJob(@PathVariable int id, @RequestBody Job job) {
        Job updated = jobService.updateJob(id, job);
        return updated != null
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/jobs/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a job")
    public ResponseEntity<String> deleteJob(@PathVariable int id) {
        return new ResponseEntity<>(jobService.deleteJob(id), HttpStatus.OK);
    }

    public static class EtatRequest {
        private String etat;

        public String getEtat() {
            return etat;
        }

        public void setEtat(String etat) {
            this.etat = etat;
        }
    }
}
