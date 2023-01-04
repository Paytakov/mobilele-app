package com.example.mobilele.web;

import com.example.mobilele.exception.ObjectNotFoundException;
import com.example.mobilele.model.dto.AddOfferDTO;
import com.example.mobilele.model.dto.SearchOfferDTO;
import com.example.mobilele.service.BrandService;
import com.example.mobilele.service.OfferService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;

@Controller
public class OfferController {

    private final OfferService offerService;
    private final BrandService brandService;

    public OfferController(OfferService offerService,
                           BrandService brandService) {
        this.offerService = offerService;
        this.brandService = brandService;
    }

    @GetMapping("/offers/all")
    public String allOffers(Model model,
                            @PageableDefault(
                                    sort = "price",
                                    direction = Sort.Direction.ASC,
                                    page = 0,
                                    size = 5) Pageable pageable) {

        model.addAttribute("offers", offerService.getAllOffers(pageable));
        return "offers";
    }

    @GetMapping("/offers/add")
    public String addOffer(Model model) {
        if (!model.containsAttribute("addOfferModel")) {
            model.addAttribute("addOfferModel", new AddOfferDTO());
        }

        model.addAttribute("brands", brandService.getAllBrands());

        return "offer-add";
    }

    @PostMapping("/offers/add")
    public String addOffer(@Valid AddOfferDTO addOfferModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal UserDetails userDetails) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOfferModel", addOfferModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addOfferModel",
                    bindingResult);
            return "redirect:/offers/add";
        }

        offerService.addOffer(addOfferModel, userDetails);

        return "redirect:/offers/all";
    }

    @GetMapping("/offers/search")
    public String searchQuery(@Valid SearchOfferDTO searchOfferDTO,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("searchOfferModel", searchOfferDTO);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.searchOfferModel",
                    bindingResult);

            return "offer-search";
        }

        if (!model.containsAttribute("searchOfferModel")) {
            model.addAttribute("searchOfferModel", searchOfferDTO);
        }

        if (!searchOfferDTO.isEmpty()) {
            model.addAttribute("offers", offerService.searchOffer(searchOfferDTO));
        }

        return "offer-search";

    }

    @ModelAttribute(name = "searchOfferModel")
    private SearchOfferDTO initSearchModel() {
        return new SearchOfferDTO();
    }

    @GetMapping("/offers/{id}")
    public String getOfferDetail(
            @PathVariable("id") UUID id,
            Model model) {

        var offerDto =
                offerService.getOfferByUUID(id)
                        .orElseThrow(() ->
                                new ObjectNotFoundException(
                                        "Offer with id " + id + " not found!"
                                ));

        model.addAttribute("offer", offerDto);

        return "details";
    }

    // @PreAuthorize("isOwner(#principal.name, #id)")
    @PreAuthorize("isOwner(#id)")
    @DeleteMapping("/offers/{id}")
    public String deleteOffer(@PathVariable("id") UUID id) {
        offerService.deleteOfferById(id);

        return "redirect:/offers/all";
    }
}
