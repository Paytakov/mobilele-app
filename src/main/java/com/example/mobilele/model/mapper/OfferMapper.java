package com.example.mobilele.model.mapper;

import com.example.mobilele.model.dto.AddOfferDTO;
import com.example.mobilele.model.dto.CardListingOfferDTO;
import com.example.mobilele.model.entity.OfferEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    OfferEntity addOfferDtoToOfferEntity(AddOfferDTO addOfferDTO);

    @Mapping(source = "model.name", target = "model")
    @Mapping(source = "model.brand.name", target = "brand")
    CardListingOfferDTO offerEntityToCardListingOfferDto(OfferEntity offerEntity);
}
