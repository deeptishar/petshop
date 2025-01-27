package org.pet.shop.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.pet.shop.dto.PetDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PetShopJourneyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Sql("classpath:test-data.sql")
    @Test
    public void getCreateAndUpdatePetWithParents() throws Exception {

        // create pet
        PetDto postRequest = new PetDto(null,
                "Katy Purry",
                "cat",
                "black",
                new PetDto.ParentDto(
                        UUID.fromString("456db6b1-60c3-44d6-b30d-baa0ca2fa132"),
                        UUID.fromString("789db6b1-60c3-44d6-b30d-baa0ca2fa132")
                )
        );

        var createdPetDto = createPet(postRequest);
        var petId = createdPetDto.id();

        // Get created pet
        var petDtoRetrieved = getPetDto(petId);

        // Verify Pet
        assertEquals(createdPetDto, petDtoRetrieved);

        // update pet
        var putRequest = new PetDto(
                petId,
                "updatedName",
                "dog",
                "white",
                new PetDto.ParentDto(UUID.randomUUID(), UUID.randomUUID()
                )
        );
        var updatedPetDto = updatePet(putRequest, petId);

        // Get created pet
        var fetchupdatedPetDto = getPetDto(petId);

        // verify Updated pet
        assertEquals(updatedPetDto, fetchupdatedPetDto);
    }

    private PetDto getPetDto(UUID petId) throws Exception {
        var getResult = mockMvc.perform(MockMvcRequestBuilders.get("/pet/" + petId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        return objectMapper.readValue(getResult.getResponse().getContentAsString(), PetDto.class);
    }

    private PetDto updatePet(PetDto putRequest, UUID petId) throws Exception {
        String UpdateRequest = objectMapper.writeValueAsString(putRequest);

        var putResult = mockMvc.perform(MockMvcRequestBuilders.put("/pet/" + petId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(UpdateRequest))
                .andExpect(status().isOk()).andReturn();

        return objectMapper.readValue(putResult.getResponse().getContentAsString(), PetDto.class);
    }

    private PetDto createPet(PetDto request) throws Exception {
        String createRequest = objectMapper.writeValueAsString(request);

        var postResult = mockMvc.perform(MockMvcRequestBuilders.post("/pet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated()).andReturn(); // Assuming the end

        // fetch Response
        return objectMapper.readValue(postResult.getResponse().getContentAsString(), PetDto.class);
    }
}
