package one.digitalinnovation.beerstock.service;

import lombok.AllArgsConstructor;
import one.digitalinnovation.beerstock.dto.BeerDTO;
import one.digitalinnovation.beerstock.entity.Beer;
import one.digitalinnovation.beerstock.exception.BeerAlreadyRegisteredException;
import one.digitalinnovation.beerstock.exception.BeerNotFoundException;
import one.digitalinnovation.beerstock.exception.BeerStockExceedException;
import one.digitalinnovation.beerstock.mapper.BeerMapper;
import one.digitalinnovation.beerstock.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper = BeerMapper.INSTANCE;

    public BeerDTO createBeer(BeerDTO beerDTO) throws BeerAlreadyRegisteredException {
        verifyIfAlreadyRegistered(beerDTO.getName());
        Beer beer = beerMapper.toModel(beerDTO);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(savedBeer);
    }

    public BeerDTO replace(BeerDTO beerDTO) throws BeerNotFoundException {
        Beer beerSaved = verifyExists(beerDTO.getId());
        Beer newBeer = beerMapper.INSTANCE.toModel(beerDTO);
        newBeer.setId(beerSaved.getId());
        Beer save = beerRepository.save(newBeer);
        return beerMapper.toDTO(save);


    }

    public BeerDTO findByName(String name) throws BeerNotFoundException {
        Beer foundBeer = beerRepository.findByName(name)
                .orElseThrow(() -> new BeerNotFoundException(name));

        return beerMapper.toDTO(foundBeer);
    }

    public List<BeerDTO> listAll() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) throws BeerNotFoundException {
        verifyExists(id);
        beerRepository.deleteById(id);
    }

    public BeerDTO increment(Long id, int quantityToIncrement) throws BeerNotFoundException, BeerStockExceedException {
        Beer beerToIncrementStock = verifyExists(id);

        int quantityAfterIncrement = quantityToIncrement + beerToIncrementStock.getQuantity();

        if (quantityAfterIncrement <= beerToIncrementStock.getMax()) {
            beerToIncrementStock.setQuantity(beerToIncrementStock.getQuantity() + quantityToIncrement);
            Beer incrementedBeerStock = beerRepository.save(beerToIncrementStock);
            return beerMapper.toDTO(incrementedBeerStock);
        }
        throw new BeerStockExceedException(id, quantityAfterIncrement);
    }

    public BeerDTO decrement(Long id, int quantityToDecrement) throws BeerNotFoundException, BeerStockExceedException {
        Beer beerToDecrementStock = verifyExists(id);

        int quantityAfterDecrement = beerToDecrementStock.getQuantity() - quantityToDecrement;

        if (quantityAfterDecrement >= 0) {
            beerToDecrementStock.setQuantity(beerToDecrementStock.getQuantity() - quantityToDecrement);
            Beer decrementedBeerStock = beerRepository.save(beerToDecrementStock);
            return beerMapper.toDTO(decrementedBeerStock);
        }
        throw new BeerStockExceedException(id, quantityAfterDecrement);
    }

    private Beer verifyExists(Long id) throws BeerNotFoundException {
        return beerRepository.findById(id)
                .orElseThrow(() -> new BeerNotFoundException(id));
    }

    private void verifyIfAlreadyRegistered(String name) throws BeerAlreadyRegisteredException {
        Optional<Beer> optionalBeer = beerRepository.findByName(name);
        if (optionalBeer.isPresent()) {
            throw new BeerAlreadyRegisteredException(name);
        }
    }
}
