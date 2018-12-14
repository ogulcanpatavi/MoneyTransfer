package com.revolut.money_transfer.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import com.revolut.money_transfer.dto.Account;
import com.revolut.money_transfer.utilities.DBConnection;
import com.revolut.money_transfer.utilities.Encryption;
import com.revolut.money_transfer.utilities.Queries;

public class AccountDAOImpl implements AccountDAO {

	private final static Logger LOGGER = Logger.getLogger(AccountDAOImpl.class.getName());

	@Override
	public ArrayList<Account> getListOfAccounts() {
		ArrayList<Account> res = new ArrayList<>();
		Connection connection = DBConnection.getDBConnection();
		Statement stmt = null;
		try {
			connection.setAutoCommit(false);
			stmt = connection.createStatement();
			;
			ResultSet rs = stmt.executeQuery(Queries.GET_ACCOUNTS);
			while (rs.next()) {
				Account a = new Account();
				a.setAcctNum(Encryption.decrypt((String) rs.getObject("AcctNum")));
				a.setCurrency((Integer) rs.getObject("Currency"));
				a.setBalance((BigDecimal) rs.getObject("Balance"));
				res.add(a);
			}
			stmt.close();
			connection.commit();
		} catch (SQLException e) {
			LOGGER.warning("Exception Message " + e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.warning("Exception Message " + e1.getMessage());
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.warning("Exception Message " + e.getMessage());
			}
		}
		return res;
	}

	@Override
	public Account getAccountByNum(String num) {
		LOGGER.info("Retrieving Account information...");
		Account a = new Account();
		Connection connection = DBConnection.getDBConnection();
		PreparedStatement preparedStatement = null;
		String query = Queries.GET_ACCOUNT_BY_ID;

		try {
			connection.setAutoCommit(false);
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, Encryption.encrypt(num));
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				a.setAcctNum(Encryption.decrypt((String) rs.getObject("AcctNum")));
				a.setCurrency((Integer) rs.getObject("Currency"));
				a.setBalance((BigDecimal) rs.getObject("Balance"));
			}
			preparedStatement.close();
			connection.commit();
		} catch (SQLException e) {
			LOGGER.warning("Exception Message " + e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.warning("Exception Message " + e1.getMessage());
			}
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.warning("Exception Message " + e.getMessage());
			}
		}
		if (StringUtils.isBlank(a.getAcctNum())){
			return null;
		}else{
			return a;
		}
	}
	
	@Override
	public boolean updateBalance(BigDecimal fromBalance, String fromAcctNum, BigDecimal toBalance, String toAcctNum){
		LOGGER.info("Updating balance...");
		Connection connection = DBConnection.getDBConnection();
		PreparedStatement firstpreparedStatement = null;
		PreparedStatement secondpreparedStatement = null;
		String query = Queries.UPDATE_BALANCE;
		try {
			connection.setAutoCommit(false);
			firstpreparedStatement = connection.prepareStatement(query);
			firstpreparedStatement.setObject(1, fromBalance);
			firstpreparedStatement.setString(2, Encryption.encrypt(fromAcctNum));
			
			secondpreparedStatement = connection.prepareStatement(query);
			secondpreparedStatement.setObject(1, toBalance);
			secondpreparedStatement.setString(2, Encryption.encrypt(toAcctNum));
		
			firstpreparedStatement.executeUpdate();
			firstpreparedStatement.close();
			secondpreparedStatement.executeUpdate();
			secondpreparedStatement.close();
			connection.commit();
		} catch (SQLException e) {
			LOGGER.warning("Exception Message " + e.getMessage());
			try {
				connection.rollback();
			} catch (SQLException e1) {
				LOGGER.warning("Exception Message " + e1.getMessage());
			}
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				LOGGER.warning("Exception Message " + e.getMessage());
			}
		}
		return true;	
	}
}
