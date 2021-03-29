package one.digitalinnovation.beerstock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BeerStockExceedException extends Exception {

    public BeerStockExceedException(Long id, int quantityIncrement) {
        super(String.format("Beer with ID to increment informed exceeds the max stock capacity: %s", id, quantityIncrement));
    }
}
