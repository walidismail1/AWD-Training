package esprit.candidat;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/candidats")
@Tag(name = "Candidats", description = "API for managing candidates (candidats)")
public class CandidatRESTApi {

    @Autowired
    private CandidatService candidatService;

    @Autowired
    private CandidatRepository candidatRepository;

    @Operation(summary = "Health check", description = "Returns a welcome message from the candidate microservice.")
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/hello")
    public String sayHello() {
        String title = "Hello, i'm the candidate Micro-Service";
        System.out.println(title);
        return title;
    }

    @Operation(summary = "List all candidates", description = "Returns the list of all candidates in the system.")
    @ApiResponse(responseCode = "200", description = "List of all candidates")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }

    @Operation(summary = "Get candidate by ID", description = "Returns a single candidate by their unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate found"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Candidat> getCandidatById(
            @Parameter(description = "Candidate ID") @PathVariable("id") int id) {
        return candidatRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Search candidates by name", description = "Returns a paginated list of candidates whose last name contains the given search string.")
    @ApiResponse(responseCode = "200", description = "Paginated list of matching candidates")
    @GetMapping(value = "/search/candidatByNom", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Candidat> candidatByNom(
            @Parameter(description = "Name or part of the name to search (e.g. Maroua)") @RequestParam("name") String name,
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return candidatRepository.candidatByNom(name, PageRequest.of(page, size));
    }

    @Operation(summary = "Create a candidate", description = "Adds a new candidate. Provide nom, prenom and email in the request body.")
    @ApiResponse(responseCode = "201", description = "Candidate created successfully")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Candidat> createCandidat(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Candidate to create (nom, prenom, email)") @RequestBody Candidat candidat) {
        return new ResponseEntity<>(candidatService.addCandidat(candidat), HttpStatus.CREATED);
    }

    @Operation(summary = "Update a candidate", description = "Updates an existing candidate by ID. Only nom, prenom and email are updated.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Candidate updated"),
            @ApiResponse(responseCode = "404", description = "Candidate not found")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Candidat> updateCandidat(
            @Parameter(description = "Candidate ID to update") @PathVariable("id") int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated nom, prenom, email") @RequestBody Candidat candidat) {
        Candidat updated = candidatService.updateCandidat(id, candidat);
        return updated != null
                ? new ResponseEntity<>(updated, HttpStatus.OK)
                : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Delete a candidate", description = "Deletes a candidate by ID. Returns a message indicating success or failure.")
    @ApiResponse(responseCode = "200", description = "Deletion result message (candidat supprimé or candidat non supprimé)")
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteCandidat(
            @Parameter(description = "Candidate ID to delete") @PathVariable("id") int id) {
        return new ResponseEntity<>(candidatService.deleteCandidat(id), HttpStatus.OK);
    }
}
