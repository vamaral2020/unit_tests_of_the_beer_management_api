package one.digitalinnovation.beerstock.controller;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.dto.QuantityDTO;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerStockExceedException;
import one.digitalinnovation.beerstock.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/beers")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerController implements BeerControllerDocs{
    
    private final BeerService beerService;
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDTO createBeer(@RequestBody @Valid BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        return beerService.createBeer(beerDTO);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public BeerDTO replace(@RequestBody @Valid BeerDTO beerDTO) throws BeerNotFoundException {
      return  beerService.replace(beerDTO);

    }

    @GetMapping("/{name}")
    public BeerDTO findByName(@PathVariable  String name) throws BeerNotFoundException {
        return beerService.findByName(name);
    }

    @GetMapping
    public List<BeerDTO> listBeers() {
        return beerService.listAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable  Long id) throws BeerNotFoundException {
        beerService.deleteById(id);
    }

    @PatchMapping("/{id}/increment")
    public BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceedException {
        return beerService.increment(id, quantityDTO.getQuantity());
    }
    @PatchMapping("/{id}/decrement")
    public BeerDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) throws BeerNotFoundException, BeerStockExceedException {
        return beerService.decrement(id, quantityDTO.getQuantity());
    }
}
