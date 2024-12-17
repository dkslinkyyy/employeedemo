package se.dawid.officemanager.test.service;

import se.dawid.officemanager.test.object.Occupation;
import se.dawid.officemanager.test.repository.OccupationRepository;

import java.util.List;

public class OccupationService {

    private final OccupationRepository occupationRepository;

    public OccupationService(OccupationRepository occupationRepository) {
        this.occupationRepository = occupationRepository;
    }

    public Occupation getOccupationByTitle(String title) {
        return occupationRepository.findByIdentifier(title);
    }

    public void createOccupation(Occupation occupation) {
        this.occupationRepository.create(occupation);
    }

    public List<Occupation> getAllOccupations() {
        return occupationRepository.findAll();
    }
}
