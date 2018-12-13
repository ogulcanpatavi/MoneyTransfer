package com.revolut.money_transfer.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.revolut.money_transfer.dto.FXRates;
import com.revolut.money_transfer.utilities.DBConnection;
import com.revolut.money_transfer.utilities.Queries;

public class FxRatesDAOImpl implements FxRatesDAO{
	
	private final static Logger LOGGER = Logger.getLogger(FxRatesDAOImpl.class.getName());
	
	@Override
	public FXRates getRate(int fromCurrency, int toCurrency){
		FXRates f = new FXRates();
		Connection connection = DBConnection.getDBConnection();
		PreparedStatement preparedStatement = null;
		String query = Queries.GET_FX_RATE;

		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, fromCurrency);
			preparedStatement.setInt(2, toCurrency);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				f.setFromCurrency(fromCurrency);
				f.setToCurrency(toCurrency);
				f.setRate((BigDecimal) rs.getObject("Rate"));
			}
			preparedStatement.close();
			connection.commit();
		} catch (SQLException e) {
			LOGGER.warning("Exception Message " + e.getMessage());
		} catch (Exception e) {
			LOGGER.warning("Exception Message " + e.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.warning("Exception Message " + e.getMessage());
			}
		}
		if (f.getRate() == null){
			LOGGER.info("No Rate Available at this moment. From: " + fromCurrency + " To: " + toCurrency);
			return null;
		}else{
			LOGGER.info("Rate is " + f.getRate() + " From: " + fromCurrency + " To: " + toCurrency);
			return f;
		}
	
	}
}
