package br.com.vieira.rest_wtih_spring_boot__and_java.controllers.docs;

import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v1.PersonDTO;
import br.com.vieira.rest_wtih_spring_boot__and_java.data.dto.v2.PersonDTOV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PersonControllerDocs {
    @Operation(summary = "Find All People", description = "Find All People", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                    )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)})
    List<PersonDTO> findAll();

    @Operation(summary = "Finds a Person", description = "Finda a specific Person by your ID", tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = PersonDTO.class))),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)})
    PersonDTO findById(@PathVariable("id") Long id);

    PersonDTO create(@RequestBody PersonDTO person);

    PersonDTOV2 create(@RequestBody PersonDTOV2 person);

    PersonDTO update(@RequestBody PersonDTO person);

    ResponseEntity<?> delete(@PathVariable("id") Long id);
}
