package com.revolut.money_transfer.dao;

import com.revolut.money_transfer.dto.FXRates;

public interface FxRatesDAO {

	FXRates getRate(int fromCurrency, int toCurrency);

}
