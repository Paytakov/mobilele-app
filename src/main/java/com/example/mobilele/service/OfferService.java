package com.example.mobilele.service;

import com.example.mobilele.model.dto.AddOfferDTO;
import com.example.mobilele.model.dto.CardListingOfferDTO;
import com.example.mobilele.model.entity.ModelEntity;
import com.example.mobilele.model.entity.OfferEntity;
import com.example.mobilele.model.entity.UserEntity;
import com.example.mobilele.model.mapper.OfferMapper;
import com.example.mobilele.repository.ModelRepository;
import com.example.mobilele.repository.OfferRepository;
import com.example.mobilele.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final UserRepository userRepository;
    private final ModelRepository modelRepository;

    public OfferService(OfferRepository offerRepository,
                        OfferMapper offerMapper,
                        UserRepository userRepository,
                        ModelRepository modelRepository) {

        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.userRepository = userRepository;
        this.modelRepository = modelRepository;
    }

    public void addOffer(AddOfferDTO addOfferDTO, UserDetails userDetails) {
        OfferEntity newOffer = offerMapper.addOfferDtoToOfferEntity(addOfferDTO);

        UserEntity seller = userRepository.findByEmail(userDetails.getUsername()).
                orElseThrow();

        ModelEntity model = modelRepository.findById(addOfferDTO.getModelId()).orElseThrow();

        newOffer.setModel(model);
        newOffer.setSeller(seller);

        offerRepository.save(newOffer);
    }

    public List<CardListingOfferDTO> findOfferByOfferName(String query) {
        return this.offerRepository
                .findAllByModel_NameContains(query)
                .stream()
                .map(offerEntity -> offerMapper.offerEntityToCardListingOfferDto(offerEntity))
                .toList();
    }
}
